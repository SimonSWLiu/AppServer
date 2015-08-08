package com.onemenu.server.protocol.req;

public class BasePageRequestData extends BaseRequestData {
	
	public String startNum;
	public String range;
	
	public void setStartNum(String startNum) {
		this.startNum = startNum;
	}
	
	public void setRange(String range) {
		this.range = range;
	}
	
}
