package com.ttProject.red5.server.adapter.library.php;

import java.util.Iterator;

import org.red5.server.api.IConnection;
import org.red5.server.api.service.IPendingServiceCallback;
import org.red5.server.api.service.IServiceCapableConnection;

import com.caucho.quercus.env.ArrayValueImpl;
import com.caucho.quercus.env.ConstStringValue;
import com.caucho.quercus.env.Env;
import com.caucho.quercus.env.StringBuilderValue;
import com.caucho.quercus.env.Value;
import com.ttProject.red5.server.adapter.ApplicationAdapterPhp;

public class ConnectionWrapper extends Wrapper {
	IConnection conn;
	
	/**
	 * @param conn
	 */
	public ConnectionWrapper(IConnection conn) {
		this.conn = conn;
	}

	public void invoke(String func, ArrayValueImpl array, IPendingServiceCallback callback) {
		if(!(conn instanceof IServiceCapableConnection)) {
			return;
		}
		Value value;
		int size = array.getSize();
		Iterator<Value> iter = array.getKeyIterator(getEnv());
		Object[] params = new Object[array.getSize()];
		for(int i = 0;i < size;i ++) {
			value = array.get(iter.next());
			if(value == null) {
				break;
			}
			if(value instanceof StringBuilderValue || value instanceof ConstStringValue) {
				params[i] = fromHexString(value.toJavaString());
			}
			else {
				params[i] = value.toJavaObject();
			}
		}
		((IServiceCapableConnection)conn).invoke(func, params, callback);
	}
	
	private Env getEnv() {
		Object adapter = conn.getScope().getContext().getBean("web.handler");
		if(adapter instanceof ApplicationAdapterPhp) {
			return ((ApplicationAdapterPhp)adapter).getEnv();
		}
		throw new RuntimeException("Call Quercus Object with invalid ApplicationAdapter");
	}
}
