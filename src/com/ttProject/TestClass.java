package com.ttProject;

import com.ttProject.red5.server.adapter.library.edge.RtmpClientEx;

public class TestClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestClass();
	}
	public TestClass() {
		// 接続先サーバーが起動していない場合RtmpClientExはExceptionを吐きますが、connectをtry-catchで囲っても取得できません。
		// 操作したえればExceptionをキャッチするためのクラスをつくってしかるべきところに設置する必要あり。
//		ExceptionHandler eh = new ExceptionHandler();
//		ExceptionMonitor.setInstance(eh); // apache mina用
//		rcex.setExceptionHandler(eh); // RtmpClientEx用
		RtmpClientEx rcex = new RtmpClientEx();
		rcex.setListener(new RcexListener(rcex));
		rcex.connect("localhost", 1935, "live");
	}
}
