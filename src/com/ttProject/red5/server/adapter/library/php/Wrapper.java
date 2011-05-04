package com.ttProject.red5.server.adapter.library.php;

public class Wrapper {
	protected String fromHexString(String hexString) {
		byte[] bytes = new byte[hexString.length() / 2];
		for(int i = 0;i < bytes.length;i ++) {
			bytes[i] = (byte)Integer.parseInt(hexString.substring(i * 2, (i + 1) * 2), 16);
		}
		return new String(bytes);
	}
}
