package com.simple.server.domain.contract;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.simple.server.config.ErrorType;



@JsonAutoDetect
@JsonDeserialize(as = ErrMsg.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrMsg extends AContract{
	
	private Integer id;
	private ErrorType errorId;
	private String details;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public ErrorType getErrorId() {
		return errorId;
	}
	public void setErrorId(ErrorType errorId) {
		this.errorId = errorId;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	@Override
	public String getClazz() {
		return this.getClass().getName();
	}
}
