package com.ttProject.red5.server.adapter.library.php;

import org.red5.server.api.IConnection;
import org.red5.server.api.listeners.IConnectionListener;

import com.ttProject.red5.server.adapter.ApplicationAdapterPhp;

public class ConnectionListenerWrapper implements IConnectionListener{
	private ApplicationAdapterPhp adapter;
	private String phpfile_connect;
	private String phpfile_disconnect;
	
	/**
	 * @param adapter
	 * @param phpfile_connect
	 * @param phpfile_disconnect
	 */
	public ConnectionListenerWrapper(ApplicationAdapterPhp adapter,
			String phpfile_connect, String phpfile_disconnect) {
		this.adapter = adapter;
		this.phpfile_connect = phpfile_connect;
		this.phpfile_disconnect = phpfile_disconnect;
	}
	@Override
	public void notifyConnected(IConnection conn) {
		adapter.execute(phpfile_connect, adapter, conn);
	}
	@Override
	public void notifyDisconnected(IConnection conn) {
		adapter.execute(phpfile_disconnect, adapter, conn);
	}
}
