package com.ttProject.red5.server.adapter.library.php;

import org.red5.server.api.event.IEvent;
import org.red5.server.api.event.IEventListener;

import com.ttProject.red5.server.adapter.ApplicationAdapterPhp;


public class EventListenerWrapper implements IEventListener {
	private ApplicationAdapterPhp adapter;
	private String phpfile;
	
	/**
	 * @param adapter
	 * @param phpfile
	 */
	public EventListenerWrapper(ApplicationAdapterPhp adapter, String phpfile) {
		this.adapter = adapter;
		this.phpfile = phpfile;
	}

	@Override
	public void notifyEvent(IEvent event) {
		adapter.execute(phpfile, adapter, event);
	}
}
