package com.ttProject.red5.server.adapter.library.php;

import org.red5.server.api.so.ISharedObject;

import com.caucho.quercus.env.Value;

public class SharedObjectWrapper extends Wrapper {
	ISharedObject so;
	/**
	 * @param so
	 */
	public SharedObjectWrapper(ISharedObject so) {
		this.so = so;
	}
	public boolean setAttribute(String name, Object param) {
		if(param instanceof String) {
			so.setAttribute(name, fromHexString(param.toString()));
		}
		else if(param instanceof Value) {
			so.setAttribute(name, ((Value)param).toJavaObject());
		}
		else {
			so.setAttribute(name, param);
		}
		return true;
	}
}
