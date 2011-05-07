package com.ttProject;
/*
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.bouncycastle.util.encoders.Base64;
*/
import org.apache.mina.util.ExceptionMonitor;

import com.ttProject.red5.server.adapter.library.edge.RtmpClientEx;

//import com.ttProject.red5.server.adapter.library.RtmpClientEx;

public class TestClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestClass();
	}
	public TestClass() {
//		Properties prop = System.getProperties();
//		prop.list(System.out);
		ExceptionHandler eh = new ExceptionHandler();
		ExceptionMonitor.setInstance(eh);
		RtmpClientEx rcex = new RtmpClientEx();
		rcex.setListener(new RcexListener(rcex));
		rcex.setExceptionHandler(eh);
		rcex.connect("localhost", 11935, "live");
/*		SyncData sdata = new SyncData("aiueo", 135, true);
		SyncData sdata2;
		try {
			String data = toString(sdata);
			System.out.println(data); // このStringデータをやり取りすることでデータのやりとりを進める。
			sdata2 = (SyncData)fromString(data);
			System.out.println(sdata2.getData1() + ":" + sdata2.getData2() + ":" + sdata2.isData3());
		}
		catch(IOException e) {
		}
		catch(ClassNotFoundException e) {
		}*/
	}
/*	private Object fromString(String s) throws IOException, ClassNotFoundException {
		byte[] data = Base64.decode(s);
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
		Object o = ois.readObject();
		ois.close();
		return o;
	}
	private String toString(Serializable o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		return new String(Base64.encode(baos.toByteArray()));
	}*/
}
