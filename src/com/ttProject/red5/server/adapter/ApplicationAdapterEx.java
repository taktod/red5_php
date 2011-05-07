package com.ttProject.red5.server.adapter;

import static org.red5.server.api.ScopeUtils.getScopeService;
import java.util.List;

import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.event.IEvent;
import org.red5.server.api.scheduling.IScheduledJob;
import org.red5.server.api.scheduling.ISchedulingService;
import org.red5.server.api.stream.IBroadcastStream;
import org.red5.server.api.stream.IStreamListener;
import org.red5.server.api.stream.IStreamPacket;
import org.red5.server.api.stream.IStreamService;
import org.red5.server.net.rtmp.event.IRTMPEvent;
import org.red5.server.stream.BroadcastScope;
import org.red5.server.stream.IBroadcastScope;
import org.red5.server.stream.IProviderService;
import org.red5.server.stream.StreamService;

import com.ttProject.red5.server.adapter.library.edge.BroadcastStream;

/**
 * アプリケーションを起動したら、どこかのサーバーにつながって、そのサーバーのメッセージをうけとって、必要のあるアプリケーション相手に接続を実施する。
 * んで、playも実施する。
 * アプリケーションが始まったら対象サーバーに接続しようとする。
 */
public class ApplicationAdapterEx extends ApplicationAdapter {
	// アプリケーションの接続先は一定になってるものとする。
	@SuppressWarnings("unused")
	private String server;
	@SuppressWarnings("unused")
	private int port;
	@SuppressWarnings("unused")
	private String application;
	
	/**
	 * @param server the server to set
	 */
	public void setServer(String server) {
		this.server = server;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * @param application the application to set
	 */
	public void setApplication(String application) {
		this.application = application;
	}
	// データのセットする処理は
	@Override
	public boolean appStart(IScope scope) {
		final IScope scopeex = scope;
		this.addScheduledJob(10000, new IScheduledJob() {
			@Override
			public void execute(ISchedulingService service)
					throws CloneNotSupportedException {
				List<String> list = getBroadcastStreamNames(scopeex);
				for(String name : list) {
					IBroadcastStream stream = getPublishStream(scopeex, name);
					if(stream instanceof BroadcastStream) {
						((BroadcastStream) stream).terminateGhostConnection();
					}
				}
			}
		});
		return super.appStart(scope);
	}
	@Override
	public void streamBroadcastStart(IBroadcastStream stream) {
		// 内部接続するときに[付きでアクセスしたら外したのにミラーリングする。
		String publishName = stream.getPublishedName();
		if(publishName.startsWith("[")) {
			String outputName = publishName.substring(1);
			IBroadcastStream outputStream = getPublishStream(stream.getScope(), outputName);
			if(outputStream == null) {
				// ミラーリング用のストリームを生成しておく。
				IScope scope = stream.getScope();
				outputStream = new BroadcastStream(outputName);
				((BroadcastStream)outputStream).setScope(scope);
				
				IProviderService providerService = (IProviderService) this.getContext().getBean(IProviderService.BEAN_NAME);
				System.out.println(providerService);
				if(providerService.registerBroadcastStream(scope, outputName, outputStream)) {
					IBroadcastScope bsScope = (BroadcastScope) providerService.getLiveProviderInput(scope, outputName, true);
					bsScope.setAttribute(IBroadcastScope.STREAM_ATTRIBUTE, outputStream);
				}
				else {
					throw new RuntimeException("Failed to make mirroring scope");
				}
			}
			outputStream.start();
			stream.addStreamListener(new IStreamListener() {
				@Override
				public void packetReceived(IBroadcastStream stream, IStreamPacket packet) {
					// TODO Auto-generated method stub
					if(packet instanceof IRTMPEvent) {
						//System.out.println("this is IRTMPevent");
						try {
							IBroadcastStream bstream = getPublishStream(stream.getScope(), stream.getPublishedName().substring(1));
							// これをBroadcastにDispatchして別のストリームとして再生させる。
							((BroadcastStream)bstream).dispatchEvent((IEvent)packet);
						}
						catch(Exception e) {
							
						}
					}
				}
			});
		}
		// 放送を開始したときに、自分につながっているサーバー宛に放送を開始したことを通知する。
		super.streamBroadcastStart(stream);
	}
	@Override
	public void streamBroadcastClose(IBroadcastStream stream) {
		// 放送を停止したときに、自分につながっているサーバー宛に放送が停止したことを通知する。
		String publishName = stream.getPublishedName();
		if(publishName.startsWith("[")) {
			IBroadcastStream bstream = getPublishStream(stream.getScope(), publishName.substring(1));
			if(stream instanceof BroadcastStream) {
				bstream.stop();
			}
		}
		super.streamBroadcastClose(stream);
	}
	public String streamEvent(IConnection conn, Object[] params) {
		return "";
	}
	public IBroadcastStream getPublishStream(IScope scope, String name) {
		if(!getBroadcastStreamNames(scope).contains(name)) {
			return null;
		}
		IProviderService providerService = (IProviderService) scope.getContext().getBean(IProviderService.BEAN_NAME);
		IBroadcastScope bs = (BroadcastScope)providerService.getLiveProviderInput(scope, name, true);
		return (IBroadcastStream)bs.getAttribute(IBroadcastScope.STREAM_ATTRIBUTE);
	}
	@Override
	public IBroadcastStream getBroadcastStream(IScope scope, String name) {
		if(!getBroadcastStreamNames(scope).contains(name)) {
			return null;
		}
		IStreamService service = (IStreamService) getScopeService(scope, IStreamService.class, StreamService.class);
		if (!(service instanceof StreamService)) {
			return null;
		}
		IBroadcastScope bs = ((StreamService) service).getBroadcastScope(scope, name);
		return (IBroadcastStream) bs.getAttribute(IBroadcastScope.STREAM_ATTRIBUTE);
	}

/*	@Override
	public void streamPlayItemPause(ISubscriberStream stream, IPlayItem item,
			int position) {
		System.out.println("streamPlayItemPause");
		// TODO Auto-generated method stub
		super.streamPlayItemPause(stream, item, position);
	}
	@Override
	public void streamPlayItemPlay(ISubscriberStream stream, IPlayItem item,
			boolean isLive) {
		System.out.println("streamPlayItemPlay");
		System.out.println(this.getName()); // アプリケーション名
		System.out.println(stream.getScope().getContextPath()); // これがroomのパスっぽい
		System.out.println(item.getName()); // 放送の名前を取得することができる。
		// scopeにattributeとしてRtmpClientExをつけるようにしておく。
		// TODO Auto-generated method stub
		super.streamPlayItemPlay(stream, item, isLive);
	}
	@Override
	public void streamPlayItemResume(ISubscriberStream stream, IPlayItem item,
			int position) {
		System.out.println("streamPlayItemResume");
		// TODO Auto-generated method stub
		super.streamPlayItemResume(stream, item, position);
	}
	@Override
	public void streamPlayItemSeek(ISubscriberStream stream, IPlayItem item,
			int position) {
		System.out.println("streamPlayItemSeek");
		// TODO Auto-generated method stub
		super.streamPlayItemSeek(stream, item, position);
	}
	@Override
	public void streamPlayItemStop(ISubscriberStream arg0, IPlayItem arg1) {
		System.out.println("streamPlayItemStop");
		// TODO Auto-generated method stub
		super.streamPlayItemStop(arg0, arg1);
	}*/
}
