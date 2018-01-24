package com.simple.server.domain.contract;

import java.io.Serializable;

import org.springframework.messaging.MessageChannel;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.simple.server.config.ContentType;


@JsonPropertyOrder({"clazz"})
@JsonInclude(Include.NON_EMPTY)
public interface IContract extends Serializable{
	
	static final long serialVersionUID = 1L;
	
	@JsonGetter("clazz")
	String getClazz();
	
	String getJuuid();						
		
	String getEventId();
	
	String getResponseURI();
		
	String getServiceIdFrom();						
	
	String getMethodHandler();	
	
	Boolean getIsDirectInsert();
	
	MessageChannel getChannel();	
	
	String getEndPointId();	
	
	String getServiceOutDatetime();
	
	String getResponseContractClass();
	
	ContentType getResponseContentType();
		
	
		
	void setResponseContractClass(String responseContractClass);
	
	void setResponseContentType(ContentType responseContentType);
	
	void setServiceOutDatetime(String serviceOutDatetime);
	
	void setRequestInDatetime(String requestInDatetime);
	
	void setMethodHandler(String methodHandler);
			
	void setServiceIdFrom(String serviceIdFrom);	
	
	void setIsDirectInsert(Boolean directInsert);
	
	void setEndPointId(String endPointId);
	
	void setChannel(MessageChannel channel);			
	
	void setResponseURI(String responseURI);		
	
	void setJuuid(String juuid);
	
	void setEventId(String eventId);
	
	void copyFrom(IContract msg) throws Exception;
}
