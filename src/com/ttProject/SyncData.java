package com.ttProject;

import java.io.Serializable;

public class SyncData implements Serializable{
	private static final long serialVersionUID = -5395372888823727529L;
	private String data1;
	private int data2;
	private boolean data3;

	/**
	 * @param data1
	 * @param data2
	 * @param data3
	 */
	public SyncData(String data1, int data2, boolean data3) {
		this.data1 = data1;
		this.data2 = data2;
		this.data3 = data3;
	}
	/**
	 * @return the data1
	 */
	public String getData1() {
		return data1;
	}
	/**
	 * @param data1 the data1 to set
	 */
	public void setData1(String data1) {
		this.data1 = data1;
	}
	/**
	 * @return the data2
	 */
	public int getData2() {
		return data2;
	}
	/**
	 * @param data2 the data2 to set
	 */
	public void setData2(int data2) {
		this.data2 = data2;
	}
	/**
	 * @return the data3
	 */
	public boolean isData3() {
		return data3;
	}
	/**
	 * @param data3 the data3 to set
	 */
	public void setData3(boolean data3) {
		this.data3 = data3;
	}
}
