package com.ttProject.red5.server.adapter.library.php;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.ttProject.red5.server.adapter.ApplicationAdapterPhp;

public class ActionListenerWrapper implements ActionListener {
	private ApplicationAdapterPhp adapter;
	private String phpfile;
	
	/**
	 * @param phpfile
	 * @param adapter
	 */
	public ActionListenerWrapper(ApplicationAdapterPhp adapter, String phpfile) {
		this.phpfile = phpfile;
		this.adapter = adapter;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		adapter.execute(phpfile, adapter, event);
	}
}
