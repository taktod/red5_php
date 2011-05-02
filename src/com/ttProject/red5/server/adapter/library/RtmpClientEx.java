package com.ttProject.red5.server.adapter.library;

import java.util.Map;

import org.red5.server.api.event.IEvent;
import org.red5.server.api.event.IEventDispatcher;
import org.red5.server.api.service.IPendingServiceCall;
import org.red5.server.api.service.IPendingServiceCallback;
import org.red5.server.api.stream.IClientStream;
import org.red5.server.net.rtmp.Channel;
import org.red5.server.net.rtmp.RTMPClient;
import org.red5.server.net.rtmp.RTMPConnection;
import org.red5.server.net.rtmp.codec.RTMP;
import org.red5.server.net.rtmp.event.IRTMPEvent;
import org.red5.server.net.rtmp.event.Notify;
import org.red5.server.net.rtmp.message.Header;
import org.red5.server.stream.AbstractClientStream;
import org.red5.server.stream.IStreamData;

/**
 * Class to connect other rtmp server.
 */
public class RtmpClientEx extends RTMPClient{
	private String server;
	private int port;
	private String application;
	private String name;
	private RTMPConnection conn;
	private Integer streamId = null;
	private IRtmpClientEx listener;
	
	public RtmpClientEx() {
	}
	/**
	 * @param server
	 * @param port
	 * @param application
	 * @param name
	 * @param listener
	 */
	public RtmpClientEx(String server, int port, String application, String name,
			IRtmpClientEx listener) {
		super();
		this.server = server;
		this.port = port;
		this.application = application;
		this.name = name;
		this.listener = listener;
	}
	/**
	 * @return the host
	 */
	public String getHost() {
		return server;
	}
	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.server = host;
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * @return the app
	 */
	public String getApplication() {
		return application;
	}
	/**
	 * @param application the app to set
	 * app/room
	 */
	public void setApplication(String application) {
		this.application = application;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the listener
	 */
	public IRtmpClientEx getListener() {
		return listener;
	}
	/**
	 * @param listener the listener to set
	 */
	public void setListener(IRtmpClientEx listener) {
		this.listener = listener;
	}
	/**
	 * @param streamId the streamId to set
	 */
	public void setStreamId(Integer streamId) {
		this.streamId = streamId;
	}

	public void connect() {
		this.connect(server, port, application);
	}
	public void connect(Object[] params) {
		this.connect(server, port, makeDefaultConnectionParams(server, port, application), null, params);
	}
	@Override
	public void connect(String server, int port,
			Map<String, Object> connectionParams) {
		this.connect(server, port, connectionParams, null);
	}
	@Override
	public void connect(String server, int port,
			Map<String, Object> connectionParams,
			IPendingServiceCallback connectCallback) {
		this.connect(server, port, connectionParams, connectCallback, null);
	}
	@Override
	public void connect(String server, int port,
			Map<String, Object> connectionParams,
			IPendingServiceCallback connectCallback,
			Object[] connectCallArguments) {
		super.connect(server, port, connectionParams, connectCallback,
				connectCallArguments);
	}
	@Override
	public void connect(String server, int port, String application) {
		this.connect(server, port, application, null);
	}
	@Override
	public void connect(String server, int port, String application,
			IPendingServiceCallback connectCallback) {
		this.connect(server, port, makeDefaultConnectionParams(server, port, application), connectCallback, null);
	}
	@Override
	public void disconnect() {
		if(conn != null) {
			IClientStream stream = conn.getStreamById(streamId);
			if(stream != null) {
				stream.close();
				stream = null;
			}
		}
		super.disconnect();
	}
	@Override
	public void connectionOpened(RTMPConnection conn, RTMP state) {
		super.connectionOpened(conn, state);
		this.conn = conn;
		if(listener != null) {
			listener.onConnect();
		}
	}
	@Override
	public void connectionClosed(RTMPConnection conn, RTMP state) {
		if(listener != null) {
			listener.onDisconnect();
		}
		super.connectionClosed(conn, state);
	}
	@Override
	protected void onInvoke(RTMPConnection conn, Channel channel, Header header,
			Notify notify, RTMP rtmp) {
		super.onInvoke(conn, channel, header, notify, rtmp);
		if(listener != null) {
			listener.onInvoke(notify.getCall());
		}
	}
	@Override
	public void createStream(IPendingServiceCallback callback) {
		final RtmpClientEx rcex = this;
		if(callback == null) {
			callback = new IPendingServiceCallback() {
				
				@Override
				public void resultReceived(IPendingServiceCall call) {
					Integer streamIdInt = (Integer)call.getResult();
					if(conn != null && streamIdInt != null) {
						NetStream stream = new NetStream();
						stream.setConnection(conn);
						rcex.streamId = streamIdInt;
						stream.setStreamId(streamIdInt);
						conn.addClientStream(stream);
						listener.onCreateStream(streamIdInt);
					}
				}
			};
		}
		super.createStream(callback);
	}
	private class NetStream extends AbstractClientStream implements IEventDispatcher {

		@Override
		public void close() {
			if(listener != null) {
				listener.onStreamClose();
			}
		}

		@Override
		public void start() {
			if(listener != null) {
				listener.onStreamStart();
			}
		}

		@Override
		public void stop() {
			if(listener != null) {
				listener.onStreamStop();
			}
		}

		@Override
		public void dispatchEvent(IEvent event) {
			if(event instanceof IRTMPEvent && event instanceof IStreamData) {
				IRTMPEvent rtmpEvent = (IRTMPEvent)event;
				if(rtmpEvent.getHeader().getSize() != 0) {
					if(listener != null) {
						listener.onDispatchEvent(rtmpEvent);
					}
				}
			}
		}
	}
}
