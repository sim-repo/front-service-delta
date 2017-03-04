package com.simple.server.domain.contract;

public class Status extends AContract{
	
	private static final long serialVersionUID = 1L;
			
	private String code;
	private String message;
	private String errorId;
	private String details;
	
	public Status() {
	}

	public Status(String code, String message) {
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

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getErrorId() {
		return errorId;
	}

	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}

	@Override
	public String getClazz() {
		return Status.class.getSimpleName();
	}
	
	
}
