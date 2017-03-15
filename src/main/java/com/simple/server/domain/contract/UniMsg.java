package com.simple.server.domain.contract;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class UniMsg extends AContract{
	private static final long serialVersionUID = 1L;
	protected int id;
	protected String body;
	
	@Override
	public String getClazz() {
		return this.getClass().getName();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "UniMsg [body=" + body + ", clazz=" + clazz + ", juuid=" + juuid + ", eventId=" + eventId + ", senderId="
				+ senderId + ", endPointId=" + endPointId + ", operationType=" + operationType + ", serviceIdFrom="
				+ serviceIdFrom + ", serviceIdTo=" + serviceIdTo + ", serviceRoleFrom=" + serviceRoleFrom
				+ ", serviceOutDatetime=" + serviceOutDatetime + ", requestInDatetime=" + requestInDatetime
				+ ", responseURI=" + responseURI + ", responseContentType=" + responseContentType
				+ ", responseContractClass=" + responseContractClass + ", methodHandler=" + methodHandler
				+ ", logClass=" + logClass + ", channel=" + channel + "]";
	}
	
	
	
}
