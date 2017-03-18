package com.simple.server.domain.contract;

public class StatusMsg extends AContract{
	
	private static final long serialVersionUID = 1L;
			
	private String code;
	private String message;
	
	public StatusMsg() {
	}

	public StatusMsg(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String getClazz() {
		return this.getClass().getName();
	}
	
	
}
