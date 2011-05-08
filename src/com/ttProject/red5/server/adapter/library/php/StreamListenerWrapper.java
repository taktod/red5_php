package com.ttProject.red5.server.adapter.library.php;

import org.red5.server.api.stream.IBroadcastStream;
import org.red5.server.api.stream.IStreamListener;
import org.red5.server.api.stream.IStreamPacket;

import com.ttProject.red5.server.adapter.ApplicationAdapterPhp;

public class StreamListenerWrapper implements IStreamListener {
	private ApplicationAdapterPhp adapter;
	private String phpfile;

	/**
	 * @param adapter
	 * @param phpfile
	 */
	public StreamListenerWrapper(ApplicationAdapterPhp adapter, String phpfile) {
		this.adapter = adapter;
		this.phpfile = phpfile;
	}

	@Override
	public void packetReceived(IBroadcastStream stream, IStreamPacket packet) {
		adapter.execute(phpfile, adapter, stream, packet);
	}

}
