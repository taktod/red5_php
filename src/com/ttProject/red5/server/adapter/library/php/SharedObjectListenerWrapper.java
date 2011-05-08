package com.ttProject.red5.server.adapter.library.php;

import java.util.List;
import java.util.Map;

import org.red5.server.api.IAttributeStore;
import org.red5.server.api.so.ISharedObjectBase;
import org.red5.server.api.so.ISharedObjectListener;

import com.ttProject.red5.server.adapter.ApplicationAdapterPhp;

public class SharedObjectListenerWrapper implements ISharedObjectListener {
	private ApplicationAdapterPhp adapter;
	private String phpfile_clear;
	private String phpfile_connect;
	private String phpfile_delete;
	private String phpfile_disconnect;
	private String phpfile_send;
	private String phpfile_update1;
	private String phpfile_update2;
	private String phpfile_update3;

	@Override
	public void onSharedObjectClear(ISharedObjectBase so) {
		adapter.execute(phpfile_clear, adapter, so);
	}

	@Override
	public void onSharedObjectConnect(ISharedObjectBase so) {
		adapter.execute(phpfile_connect, adapter, so);
	}

	@Override
	public void onSharedObjectDelete(ISharedObjectBase so, String name) {
		adapter.execute(phpfile_delete, adapter, so, name);
	}

	@Override
	public void onSharedObjectDisconnect(ISharedObjectBase so) {
		adapter.execute(phpfile_disconnect, adapter, so);
	}

	@Override
	public void onSharedObjectSend(ISharedObjectBase so, String key,
			List<?> list) {
		adapter.execute(phpfile_send, adapter, so, key, list);
	}

	@Override
	public void onSharedObjectUpdate(ISharedObjectBase so,
			IAttributeStore attribute) {
		adapter.execute(phpfile_update1, adapter, so, attribute);
	}

	@Override
	public void onSharedObjectUpdate(ISharedObjectBase so,
			Map<String, Object> map) {
		adapter.execute(phpfile_update2, adapter, so, map);
	}

	@Override
	public void onSharedObjectUpdate(ISharedObjectBase so, String key,
			Object value) {
		adapter.execute(phpfile_update3, adapter, so, key, value);
	}
}
