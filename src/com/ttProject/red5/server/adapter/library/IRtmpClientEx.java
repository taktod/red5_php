package com.ttProject.red5.server.adapter.library;

import org.red5.server.api.event.IEvent;
import org.red5.server.api.service.IServiceCall;

public interface IRtmpClientEx {
	public void onConnect();
	public void onDisconnect();
	public void onCreateStream(Integer streamId);
	public void onStreamStart();
	public void onStreamStop();
	public void onStreamClose();
	public void onDispatchEvent(IEvent event);
	public void onInvoke(IServiceCall call);
}
