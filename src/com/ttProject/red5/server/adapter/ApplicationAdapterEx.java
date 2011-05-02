package com.ttProject.red5.server.adapter;

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
import org.red5.server.net.rtmp.event.IRTMPEvent;
import org.red5.server.stream.BroadcastScope;
import org.red5.server.stream.IBroadcastScope;
import org.red5.server.stream.IProviderService;

import com.ttProject.red5.server.adapter.library.BroadcastStream;

/**
 * アプリケーションを起動したら、どこかのサーバーにつながって、そのサーバーのメッセージをうけとって、必要のあるアプリケーション相手に接続を実施する。
 * んで、playも実施する。
 * アプリケーションが始まったら対象サーバーに接続しようとする。
 */
public class ApplicationAdapterEx extends ApplicationAdapter {
	@Override
	public boolean appStart(IScope scope) {
		final ApplicationAdapterEx aaex = this;
		final IScope scopeex = scope;
		System.out.println("appStart!!!!!!");
		this.addScheduledJob(10000, new IScheduledJob() {
			@Override
			public void execute(ISchedulingService service)
					throws CloneNotSupportedException {
				// 10秒ごとに、対象サーバーに接続を実施する。
				List<String> list = aaex.getBroadcastStreamNames(scopeex);
				for(String name : list) {
					System.out.println(name);
				}
			}
		});
		return super.appStart(scope);
	}
	private BroadcastStream outputStream = null;
	@Override
	public void streamBroadcastStart(IBroadcastStream stream) {
//		System.out.println(stream.getCodecInfo().getAudioCodecName());
//		System.out.println(stream.getCodecInfo().getVideoCodecName());
		// 内部接続するときに[付きでアクセスしたら外したのにミラーリングする。
		String publishName = stream.getPublishedName();
		if(publishName.startsWith("[")) {
			if(outputStream == null) {
			// ミラーリング用のストリームを生成しておく。
			String outputName = publishName.substring(1);
			IScope scope = stream.getScope();
			outputStream = new BroadcastStream(outputName);
			outputStream.setScope(scope);
			
			IProviderService providerService = (IProviderService) this.getContext().getBean(IProviderService.BEAN_NAME);
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
						// これをBroadcastにDispatchして別のストリームとして再生させる。
						outputStream.dispatchEvent((IEvent)packet);
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
		System.out.println(publishName);
		if(publishName.startsWith("[")) {
			outputStream.stop();
			// すでにこの放送をみている全員が視聴できなくする必要あり。
//			Status unpublished = new Status(StatusCodes.NS_PLAY_UNPUBLISHNOTIFY);
//			StatusMessage unpublishedMsg = new StatusMessage();
//			unpublishedMsg.setBody(unpublished);
//			outputStream.onPipeConnectionEvent(event)

			// unpublish notifyをunsubscribeをいれても、次のBroadcastStreamをつくるとこわれるっぽい。
			// 継承されるストリームも停止させる。(ほかのユーザーが接続していない場合は停止させてOK)
/*			String outputName = publishName.substring(1);
			IProviderService providerService = (IProviderService) this.getContext().getBean(IProviderService.BEAN_NAME);
			IBroadcastScope bsScope = (BroadcastScope) providerService.getLiveProviderInput(scope, outputName, true);
			bsScope.removeAttribute(IBroadcastScope.STREAM_ATTRIBUTE);
			providerService.unregisterBroadcastStream(stream.getScope(), outputName, outputStream);
			outputStream.stop();
			outputStream.close();
			outputStream = null;// */
		}
		super.streamBroadcastClose(stream);
	}
	public String streamEvent(IConnection conn, Object[] params) {
		return "";
	}
}
