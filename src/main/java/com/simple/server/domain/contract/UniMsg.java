package com.simple.server.domain.contract;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonAutoDetect
@JsonDeserialize(as = UniMsg.class)
@JsonIgnoreProperties(ignoreUnknown = true)

@XmlRootElement(name = "Message")
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
	@XmlElement(name = "body")
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
		return "UniMsg [id=" + id + ", body=" + body + ", clazz=" + clazz + ", juuid=" + juuid + ", eventId=" + eventId
				+ ", senderId=" + senderId + ", endPointId=" + endPointId + ", subscriberId=" + subscriberId
				+ ", subscriberHandler=" + subscriberHandler + ", subscriberStoreClass=" + subscriberStoreClass
				+ ", publisherId=" + publisherId + ", publisherHandler=" + publisherHandler + ", publisherStoreClass="
				+ publisherStoreClass + ", operationType=" + operationType + ", serviceIdFrom=" + serviceIdFrom
				+ ", serviceIdTo=" + serviceIdTo + ", serviceRoleFrom=" + serviceRoleFrom + ", serviceOutDatetime="
				+ serviceOutDatetime + ", requestInDatetime=" + requestInDatetime + ", responseURI=" + responseURI
				+ ", responseContentType=" + responseContentType + ", responseContractClass=" + responseContractClass
				+ ", methodHandler=" + methodHandler + ", isDirectInsert=" + isDirectInsert + ", logClass=" + logClass
				+ ", messageHeaderValue=" + messageHeaderValue + ", messageBodyValue=" + messageBodyValue
				+ ", logDatetime=" + logDatetime + ", loggerId=" + loggerId + ", errorId=" + errorId + ", details="
				+ details + ", channel=" + channel + "]";
	}

	
	
	
}
