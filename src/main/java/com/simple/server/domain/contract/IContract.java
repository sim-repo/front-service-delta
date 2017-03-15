package com.simple.server.domain.contract;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.messaging.MessageChannel;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.simple.server.config.ContentType;
import com.simple.server.config.EndpointType;
import com.simple.server.config.EventType;



@JsonPropertyOrder({"clazz"})
@JsonInclude(Include.NON_EMPTY)
public interface IContract extends Serializable{
	
	static final long serialVersionUID = 1L;
	
	@JsonGetter("clazz")
	String getClazz();
	
	UUID getJuuid();						
		
	EventType getEventId();
	
	String getResponseURI();
		
	String getServiceIdFrom();						
	
	String getMethodHandler();	
	
	Boolean getIsDirectInsert();
	
	MessageChannel getChannel();	
	
	EndpointType getEndPointId();	
	
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
	
	void setEndPointId(EndpointType endPointId);
	
	void setChannel(MessageChannel channel);			
	
	void setResponseURI(String responseURI);		
	
	void setJuuid(UUID juuid);
	
	void setEventId(EventType eventId);
}
