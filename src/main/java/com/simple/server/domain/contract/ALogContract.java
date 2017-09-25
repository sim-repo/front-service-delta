package com.simple.server.domain.contract;


public abstract class ALogContract extends AContract{
	
	private static final long serialVersionUID = 1L;
			
	protected String messageHeaderValue;
	
	protected String messageBodyValue;

	public String getMessageHeaderValue() {
		return messageHeaderValue;
	}

	public void setMessageHeaderValue(String messageHeaderValue) {
		this.messageHeaderValue = messageHeaderValue;
	}

	public String getMessageBodyValue() {
		return messageBodyValue;
	}

	public void setMessageBodyValue(String messageBodyValue) {
		this.messageBodyValue = messageBodyValue;
	}	
	
}
