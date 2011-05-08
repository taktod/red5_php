package com.ttProject.red5.server.adapter.library.php;

import org.red5.server.api.IScope;
import org.red5.server.api.listeners.IScopeListener;

import com.ttProject.red5.server.adapter.ApplicationAdapterPhp;

public class ScopeListenerWrapper implements IScopeListener {
	private ApplicationAdapterPhp adapter;
	private String phpfile_create;
	private String phpfile_remove;

	/**
	 * @param adapter
	 * @param phpfile_create
	 * @param phpfile_remove
	 */
	public ScopeListenerWrapper(ApplicationAdapterPhp adapter,
			String phpfile_create, String phpfile_remove) {
		this.adapter = adapter;
		this.phpfile_create = phpfile_create;
		this.phpfile_remove = phpfile_remove;
	}

	@Override
	public void notifyScopeCreated(IScope scope) {
		adapter.execute(phpfile_create, adapter, scope);
	}

	@Override
	public void notifyScopeRemoved(IScope scope) {
		adapter.execute(phpfile_remove, adapter, scope);
	}

}
