package com.simple.server.domain.contract;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class BusReportItem {
	
	String param;
	String val;
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}	
}
