package com.ttProject;

import org.red5.server.api.event.IEvent;
import org.red5.server.api.service.IServiceCall;

import com.ttProject.red5.server.adapter.library.IRtmpClientEx;
import com.ttProject.red5.server.adapter.library.RtmpClientEx;

public class RcexListener implements IRtmpClientEx {
	private RtmpClientEx rcex;
	public RcexListener(RtmpClientEx rcex) {
		this.rcex = rcex;
	}
	@Override
	public void onConnect() {
		System.out.println("connect");
		rcex.createStream(null);
	}

	@Override
	public void onDisconnect() {
		System.out.println("disconnect");
	}

	@Override
	public void onCreateStream(Integer streamId) {
		System.out.println("createStream" + streamId);
		rcex.play(streamId, "135", -2000, -2);
	}

	@Override
	public void onStreamStart() {
		System.out.println("streamStart");
	}

	@Override
	public void onStreamStop() {
		System.out.println("streamStop");
	}

	@Override
	public void onStreamClose() {
		System.out.println("streamClose");
	}

	@Override
	public void onDispatchEvent(IEvent event) {
		System.out.println("dispatchEvent");
	}

	@Override
	public void onInvoke(IServiceCall call) {
		System.out.println("invoke");
		System.out.println("func: " + call.getServiceMethodName());
		System.out.println("message: " + call.getArguments()[0].toString());
	}
}
