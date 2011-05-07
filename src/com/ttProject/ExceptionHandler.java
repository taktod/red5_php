package com.ttProject;

import java.net.ConnectException;

import org.apache.mina.util.ExceptionMonitor;
import org.red5.server.net.rtmp.ClientExceptionHandler;

public class ExceptionHandler extends ExceptionMonitor implements ClientExceptionHandler {
	@Override
	public void exceptionCaught(Throwable error) {
	}
	@Override
	public void handleException(Throwable error) {
		if(error instanceof ConnectException) {
			System.out.println(error.getMessage());
		}
		else {
			error.printStackTrace();
			System.out.println(error.getMessage());
			System.out.println(error.getCause());
			error.getCause().printStackTrace();
		}
	}
}
