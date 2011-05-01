package com.ttProject;

import com.ttProject.red5.server.adapter.library.RtmpClientEx;

public class TestClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestClass();
	}
	public TestClass() {
		RtmpClientEx rcex = new RtmpClientEx();
		rcex.setListener(new RcexListener(rcex));
		rcex.connect("localhost", 1935, "live");
	}
}
