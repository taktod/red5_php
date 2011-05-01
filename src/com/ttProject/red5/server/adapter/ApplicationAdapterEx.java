package com.ttProject.red5.server.adapter;

import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IConnection;

/**
 * アプリケーションを起動したら、どこかのサーバーにつながって、そのサーバーのメッセージをうけとって、必要のあるアプリケーション相手に接続を実施する。
 * んで、playも実施する。
 */
public class ApplicationAdapterEx extends ApplicationAdapter {
	public String streamEvent(IConnection conn, Object[] params) {
		return "";
	}
}
