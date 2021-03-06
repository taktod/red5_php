package com.ttProject;

import org.red5.server.api.event.IEvent;
import org.red5.server.api.event.IEventDispatcher;
import org.red5.server.api.service.IServiceCall;

import com.ttProject.red5.server.adapter.library.edge.IRtmpClientEx;
import com.ttProject.red5.server.adapter.library.edge.RtmpClientEx;

public class RcexListener implements IRtmpClientEx, IEventDispatcher {
	private RtmpClientEx rcex;
	public RcexListener(RtmpClientEx rcex) {
		this.rcex = rcex;
		this.rcex.setStreamEventDispatcher(this);
	}
	@Override
	public void onConnect() {
		System.out.println("connect");
		rcex.play("test", this);
	}
	@Override
	public void onDisconnect() {
		System.out.println("disconnect");
	}

	@Override
	public void onCreateStream(Integer streamId) {
		System.out.println("createStream" + streamId);
	}

	@Override
	public Object onInvoke(IServiceCall call) {
		System.out.println("invoke");
		System.out.println("func: " + call.getServiceMethodName());
		return null;
	}
	@Override
	public void dispatchEvent(IEvent event) {
		System.out.println("dispatchEvent");
	}
}
