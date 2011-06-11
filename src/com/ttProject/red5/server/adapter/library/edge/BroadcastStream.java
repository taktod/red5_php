/*******************************************************************************
 * Copyright (c) 2008, 2010 Xuggle Inc.  All rights reserved.
 *  
 * This file is part of Xuggle-Xuggler-Red5.
 *
 * Xuggle-Xuggler-Red5 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Xuggle-Xuggler-Red5 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Xuggle-Xuggler-Red5.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * I Change some of this library to use stream reflection. (taktod)
 *******************************************************************************/
package com.ttProject.red5.server.adapter.library.edge;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.mina.core.buffer.IoBuffer;
import org.red5.server.api.IScope;
import org.red5.server.api.event.IEvent;
import org.red5.server.api.stream.IAudioStreamCodec;
import org.red5.server.api.stream.IBroadcastStream;
import org.red5.server.api.stream.IStreamCodecInfo;
import org.red5.server.api.stream.IStreamListener;
import org.red5.server.api.stream.IStreamPacket;
import org.red5.server.api.stream.IVideoStreamCodec;
import org.red5.server.api.stream.ResourceExistException;
import org.red5.server.api.stream.ResourceNotFoundException;
import org.red5.server.messaging.IMessageComponent;
//import org.red5.server.messaging.IMessageOutput;
import org.red5.server.messaging.IPipe;
import org.red5.server.messaging.IPipeConnectionListener;
import org.red5.server.messaging.IProvider;
import org.red5.server.messaging.OOBControlMessage;
import org.red5.server.messaging.PipeConnectionEvent;
import org.red5.server.net.rtmp.event.AudioData;
import org.red5.server.net.rtmp.event.IRTMPEvent;
import org.red5.server.net.rtmp.event.Invoke;
import org.red5.server.net.rtmp.event.Notify;
import org.red5.server.net.rtmp.event.VideoData;
import org.red5.server.net.rtmp.status.Status;
import org.red5.server.net.rtmp.status.StatusCodes;
import org.red5.server.stream.AbstractStream;
import org.red5.server.stream.AudioCodecFactory;
import org.red5.server.stream.BroadcastScope;
import org.red5.server.stream.IBroadcastScope;
//import org.red5.server.stream.IConsumerService;
import org.red5.server.stream.IProviderService;
import org.red5.server.stream.IStreamData;
import org.red5.server.stream.VideoCodecFactory;
import org.red5.server.stream.codec.StreamCodecInfo;
import org.red5.server.stream.message.RTMPMessage;
import org.red5.server.stream.message.StatusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of IBroadcastStream that allows connection-less
 * providers to still publish a Red5 stream.
 * 
 * Don't worry if you don't understand what that means.  See
 * {@link AudioTranscoderDemo} for an example of this in action.
 * 
 */
public class BroadcastStream extends AbstractStream implements IBroadcastStream, IProvider, IPipeConnectionListener
{
	/** Listeners to get notified about received packets. */
	private Set<IStreamListener> listeners = new CopyOnWriteArraySet<IStreamListener>();
	final private Logger log = LoggerFactory.getLogger(this.getClass());

	private String publishedName;
	private IPipe livePipe;
	private IScope scope;

	// Codec handling stuff for frame dropping
	private StreamCodecInfo mCodecInfo;

	/** is there need to check video codec? */
	protected boolean checkVideoCodec = false;
	/** is there need to check audio codec? */
	protected boolean checkAudioCodec = false; // this need to be checked for FME
	/** total number of bytes received */
	protected long bytesReceived;
	/** is this stream still active? */
	protected volatile boolean closed;
	/** is there need to send start notification? */
	protected boolean sendStartNotification = true;
	/** Stores timestamp of first packet */
	protected long firstPacketTime = -1;
	
	protected long latestTimeStamp = -1;

	public BroadcastStream(String name)
	{
		publishedName = name;
		livePipe = null;
		log.trace("name: {}", name);

		// we want to create a video codec when we get our
		// first video packet.
		mCodecInfo = new StreamCodecInfo();
		creationTime = 0L;
	}

	@Override
	public IProvider getProvider()
	{
		log.trace("getProvider()");
		return this;
	}

	@Override
	public String getPublishedName()
	{
		log.trace("getPublishedName()");
		return publishedName;
	}

	@Override
	public String getSaveFilename()
	{
		log.trace("getSaveFilename()");
		throw new Error("unimplemented method");
	}

	@Override
	public void addStreamListener(IStreamListener listener)
	{
		log.trace("addStreamListener(listener: {})", listener);
		listeners.add(listener);
	}

	@Override
	public Collection<IStreamListener> getStreamListeners()
	{
		log.trace("getStreamListeners()");
		return listeners;
	}

	@Override
	public void removeStreamListener(IStreamListener listener)
	{
		log.trace("removeStreamListener({})", listener);
		listeners.remove(listener);
	}

	@Override
	public void saveAs(String filePath, boolean isAppend)
		throws IOException, ResourceNotFoundException, ResourceExistException
	{
		log.trace("saveAs(filepath:{}, isAppend:{})", filePath, isAppend);
		throw new Error("unimplemented method");
	}

	@Override
	public void setPublishedName(String name)
	{
		log.trace("setPublishedName(name:{})", name);
		publishedName = name;
	}

	@Override
	public void close()
	{
		// check active
		if(closed) {
			return;
		}
		closed = true;
		if(livePipe != null) {
			livePipe.unsubscribe((IProvider)this);
		}
//		sendPublishStopNotify();
//		connMsgOut.unsubscribe(this);// this may be for clientStream
//		notifyBroadcastClose();
	}
	public void terminateGhostConnection() {
		if(livePipe.getConsumers().size() != 0) {
			// まだつながっているユーザーが存在するため、このままおいておく。
			System.out.println(livePipe.getConsumers().size());
			return;
		}
		// 誰も接続していないので、データを削除する。
		IProviderService providerService = (IProviderService)scope.getContext().getBean(IProviderService.BEAN_NAME);
		IBroadcastScope bsScope = (BroadcastScope) providerService.getLiveProviderInput(scope, publishedName, true);
		bsScope.removeAttribute(IBroadcastScope.STREAM_ATTRIBUTE);
		providerService.unregisterBroadcastStream(scope, publishedName);
		System.out.println(providerService);

		log.trace("close()");
	}

	@Override
	public IStreamCodecInfo getCodecInfo()
	{
		log.trace("getCodecInfo()");
		// we don't support this right now.
		return mCodecInfo;
	}

	@Override
	public String getName()
	{
		log.trace("getName(): {}", publishedName);
		// for now, just return the published name
		return publishedName;
	}

	public void setScope(IScope scope)
	{
		this.scope = scope;
	}

	@Override
	public IScope getScope()
	{
		log.trace("getScope(): {}", scope);
		return scope;
	}

/*	@Override
	public void start()
	{
		closed = false;
		Status status = new Status(StatusCodes.NS_PLAY_PUBLISHNOTIFY);
		StatusMessage smessage = new StatusMessage();
		smessage.setBody(status);
		try {
			livePipe.pushMessage(smessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		this.mLivePipe.subscribe(getProvider(), new HashMap<String, Object>() {{put("play", "start");}});
		log.trace("start()");
	}*/
	@Override
	public void start() {
//		IConsumerService consumerManager = (IConsumerService) getScope().getContext().getBean(IConsumerService.KEY);
		checkVideoCodec = true;
		firstPacketTime = -1;
		latestTimeStamp = -1;
		// this is for clientStream
//		connMsgOut = consumerManager.getConsumerOutput(this);
//		connMsgOut.subscribe(this, null);
		setCodecInfo(new StreamCodecInfo());
		closed = false;
		bytesReceived = 0;
		creationTime = System.currentTimeMillis();
		// こっちのnotifyははじめのpacketデータを取得してそこで送るべき
		Status status = new Status(StatusCodes.NS_PLAY_PUBLISHNOTIFY);
		StatusMessage smessage = new StatusMessage();
		smessage.setBody(status);
		try {
			livePipe.pushMessage(smessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop()
	{
		// unscribeを送る。(とめることはできるが、始めることができない。)
//		this.mLivePipe.unsubscribe(this.getProvider());
      
		// notifyを送る。(Notifyはcloseで送るべき)
		Status status = new Status(StatusCodes.NS_PLAY_UNPUBLISHNOTIFY);
		StatusMessage smessage = new StatusMessage();
		smessage.setBody(status);
		try {
			livePipe.pushMessage(smessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// BroadcastCloseイベントをおくっておく。
		// XXX mLivePipeから何人視聴している状態か確認して、視聴しているユーザーがいる間は、closeしても内部の動作をとめないようにしておく。
		// これでうまくいけそう。
//		System.out.println(mLivePipe.getConsumers().size()); (接続人数を取得できる。)
		log.trace("stop");
	}

	@Override
	public void onOOBControlMessage(IMessageComponent source, IPipe pipe,
			OOBControlMessage oobCtrlMsg)
	{
		log.trace("onOOBControlMessage");
	}

	@Override
	public void onPipeConnectionEvent(PipeConnectionEvent event)
	{
		log.trace("onPipeConnectionEvent(event:{})", event);
		switch (event.getType())
		{
		case PipeConnectionEvent.PROVIDER_CONNECT_PUSH:
			if (event.getProvider() == this
				&& (event.getParamMap() == null || !event.getParamMap().containsKey("record")))
			{
				this.livePipe = (IPipe) event.getSource();
			}
			break;
		case PipeConnectionEvent.PROVIDER_DISCONNECT:
			if (this.livePipe == event.getSource())
			{
				// これでunpublishedがいくようになった？
				this.livePipe = null;
//				this.livePipe.unsubscribe(this.getProvider());
			}
			break;
		case PipeConnectionEvent.CONSUMER_CONNECT_PUSH:
			System.out.println("connect push");
			break;
		case PipeConnectionEvent.CONSUMER_DISCONNECT:
			System.out.println("disconnect");
			break;
		default:
			break;
		}
	}
/*
	public void dispatchEvent(IEvent event)
	{
		try {
			log.trace("dispatchEvent(event:{})", event);
			if (event instanceof IRTMPEvent)
			{
				IRTMPEvent rtmpEvent = (IRTMPEvent) event;
				if (mLivePipe != null)
				{
					RTMPMessage msg = RTMPMessage.build(rtmpEvent);
					//RTMPMessage msg = new RTMPMessage();
					//msg.setBody(rtmpEvent);

					if (mCreationTime == null)
						mCreationTime = (long)rtmpEvent.getTimestamp();
					try
					{
						if (event instanceof AudioData)
						{
							mCodecInfo.setHasAudio(true);
							// 本来ならここでオーディオデータも取得する必要があるが、Red5がAudioデータにきちんと対処していないので、データが抜け落ちている。
						}
						else if (event instanceof VideoData)
						{
							IVideoStreamCodec videoStreamCodec = null;
							if (mCodecInfo.getVideoCodec() == null)
							{
								videoStreamCodec = VideoCodecFactory.getVideoCodec(((VideoData) event).getData());
								mCodecInfo.setVideoCodec(videoStreamCodec);
							} else if (mCodecInfo != null) {
								videoStreamCodec = mCodecInfo.getVideoCodec();
							}

							if (videoStreamCodec != null) {
								videoStreamCodec.addData(((VideoData) rtmpEvent).getData());
							}

							if (mCodecInfo!= null) {
								mCodecInfo.setHasVideo(true);
							}
						}
						mLivePipe.pushMessage(msg);

						// Notify listeners about received packet
						if (rtmpEvent instanceof IStreamPacket)
						{
							for (IStreamListener listener : getStreamListeners())
							{
								try
								{
									listener.packetReceived(this, (IStreamPacket) rtmpEvent);
								}
								catch (Exception e)
								{
									log.error("Error while notifying listener " + listener, e);
								}
							}
						}
					}
					catch (IOException ex)
					{
						// ignore
						log.error("Got exception: {}", ex);
					}
				}
			}
		} finally {
		}
	}*/
	public void dispatchEvent(IEvent event) {
		if(!(event instanceof IRTMPEvent) && (event.getType() != IEvent.Type.STREAM_CONTROL) && (event.getType() != IEvent.Type.STREAM_DATA) || closed) {
			log.debug("dispatchEvent: {}", event.getType());
			return;
		}
		// get stream codec
		IStreamCodecInfo codecInfo = getCodecInfo();
		StreamCodecInfo info = null;
		if(codecInfo instanceof StreamCodecInfo) {
			info = (StreamCodecInfo) codecInfo;
		}
		// create the event
		IRTMPEvent rtmpEvent;
		try {
			rtmpEvent = (IRTMPEvent)event;
		}
		catch(ClassCastException e) {
			log.error("Class cast exception in event dispatch", e);
			return;
		}
		int eventTime = -1;
		IoBuffer buf = null;
		if(rtmpEvent instanceof IStreamData && (buf = ((IStreamData<?>)rtmpEvent).getData()) != null) {
			bytesReceived += buf.limit();
		}
		if(rtmpEvent instanceof AudioData) {
			// splitmediaLabs - begin AAC fix
			IAudioStreamCodec audioStreamCodec = null;
			if(checkAudioCodec) {
				audioStreamCodec = AudioCodecFactory.getAudioCodec(buf);
				if(info != null) {
					info.setAudioCodec(audioStreamCodec);
				}
				checkAudioCodec = false;
			}
			else if(codecInfo != null) {
				audioStreamCodec = codecInfo.getAudioCodec();
			}
			if(audioStreamCodec != null) {
				audioStreamCodec.addData(buf.asReadOnlyBuffer());
			}
			if(info != null) {
				info.setHasAudio(true);
			}
			eventTime = rtmpEvent.getTimestamp();
		}
		else if(rtmpEvent instanceof VideoData) {
			IVideoStreamCodec videoStreamCodec = null;
			if(checkVideoCodec) {
				videoStreamCodec = VideoCodecFactory.getVideoCodec(buf);
				if(info != null) {
					info.setVideoCodec(videoStreamCodec);
				}
				checkVideoCodec = false;
			}
			else if(codecInfo != null) {
				videoStreamCodec = codecInfo.getVideoCodec();
			}
			if(videoStreamCodec != null) {
				videoStreamCodec.addData(buf.asReadOnlyBuffer());
			}
			if(info != null) {
				info.setHasVideo(true);
			}
			eventTime = rtmpEvent.getTimestamp();
		}
		else if (rtmpEvent instanceof Invoke) {
			eventTime = rtmpEvent.getTimestamp();
			return;
		}
		else if(rtmpEvent instanceof Notify) {
			Notify notifyEvent = (Notify) rtmpEvent;
			if(metaData == null && notifyEvent.getHeader().getDataType() == Notify.TYPE_STREAM_METADATA) {
				try {
					metaData = notifyEvent.duplicate();
				}
				catch (Exception e) {
					log.warn("Metadata could not be duplicated for this stream.", e);
				}
			}
			eventTime = rtmpEvent.getTimestamp();
		}
		if(eventTime > latestTimeStamp) {
			latestTimeStamp = eventTime;
		}
//		checkSendNotifications(event); // this is the notification for client player's publish
		try {
			if(livePipe != null) {
				RTMPMessage msg = RTMPMessage.build(rtmpEvent, eventTime);
				livePipe.pushMessage(msg);
			}
		} catch (IOException e) {
			stop();
		}
		if(rtmpEvent instanceof IStreamPacket) {
			for(IStreamListener listener : getStreamListeners()) {
				try {
					listener.packetReceived(this, (IStreamPacket)rtmpEvent);
				}
				catch (Exception e) {
					log.error("Error while notifying listener {}", listener, e);
				}
			}
		}
	}
/*	private void checkSendNotifications(IEvent event) {
		IEventListener source = event.getSource();
		sendStartNotifications(source);
	}*/
/*	private void sendStartNotifications(IEventListener source) {
		if(sendStartNotification) {
			sendStartNotification = false;
			if(source instanceof IConnection) {
				IScope scope = ((IConnection) source).getScope();
				if(scope.hasHandler()) {
					Object handler = scope.getHandler();
					if(handler instanceof IStreamAwareScopeHandler) {
						((IStreamAwareScopeHandler) handler).streamPublishStart(this);
					}
				}
			}
			sendPublishStartNotify();
			notifyBroadcastStart();
		}
	}*/
/*	private void sendPublishStartNotify() {
		Status publishStatus = new Status(StatusCodes.NS_PUBLISH_START);
		publishStatus.setClientid()
	}*/
	@Override
	public long getCreationTime()
	{
		return creationTime;
	}

	@Override
	public Notify getMetaData()
	{
		return null;
	}
	public long getBytesReceived() {
		return bytesReceived;
	}
}