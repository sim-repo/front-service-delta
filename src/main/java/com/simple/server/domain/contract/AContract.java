package com.simple.server.domain.contract;

import java.util.UUID;
import org.springframework.messaging.MessageChannel;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.simple.server.config.ContentType;
import com.simple.server.config.EndpointType;
import com.simple.server.config.OperationType;
import com.simple.server.config.RoleType;

@JsonAutoDetect
public abstract class AContract implements IContract{
	
	private static final long serialVersionUID = 1L;
	
	protected String clazz;
	
	protected UUID juuid;
	
	protected EndpointType endPointId;
	
	protected OperationType operationType;
	
	protected String serviceIdFrom;
	
	protected String serviceIdTo;			

	protected String serviceRoleFrom = RoleType.FRONT_SERVICE.toString();
	
	protected String serviceOutDatetime;
	
	protected String requestInDatetime;
			
	protected String responseURI;
	
	protected ContentType responseContentType;
	
	protected String responseContractClass;

	protected String methodHandler;
		
	protected Class<? extends ALogContract> logClass; 
	
	@JsonIgnore
	protected MessageChannel channel;
	
	
	@Override
	public UUID getJuuid() {	
		return juuid;
	}
	
	@Override
	public void setJuuid(UUID juuid) {
		this.juuid = juuid;
	}

	@Override
	public EndpointType getEndPointId() {
		return endPointId;
	}

	@Override
	public void setEndPointId(EndpointType endPointId) {
		this.endPointId = endPointId;
	}
		
	@Override
	public String getResponseURI() {
		return responseURI;
	}

	@Override
	public void setResponseURI(String responseURI) {
		this.responseURI = responseURI;
	}

	public String getServiceIdFrom() {
		return serviceIdFrom;
	}

	public void setServiceIdFrom(String serviceIdFrom) {
		this.serviceIdFrom = serviceIdFrom;
	}

	public String getServiceIdTo() {
		return serviceIdTo;
	}

	public void setServiceIdTo(String serviceIdTo) {
		this.serviceIdTo = serviceIdTo;
	}
	
	@Override
	public ContentType getResponseContentType() {
		return responseContentType;
	}
	
	public void setResponseContentType(ContentType responseContentType){
		this.responseContentType = responseContentType;
	}

	@Override
	public String getMethodHandler() {
		return methodHandler;
	}

	@Override
	public void setMethodHandler(String methodHandler) {
		this.methodHandler = methodHandler;
	}

	@Override
	public MessageChannel getChannel() {
		return channel;
	}

	@Override
	public void setChannel(MessageChannel channel) {
		this.channel = channel;
	}

	public String getResponseContractClass() {
		return responseContractClass;
	}

	public void setResponseContractClass(String responseContractClass) {
		this.responseContractClass = responseContractClass;
	}

	public String getServiceRoleFrom() {
		return serviceRoleFrom;
	}


	public Class<? extends ALogContract> getLogClass() {
		return logClass;
	}

	public void setLogClass(Class<? extends ALogContract> logClass) {
		this.logClass = logClass;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	public String getServiceOutDatetime() {
		return serviceOutDatetime;
	}

	public void setServiceOutDatetime(String serviceOutDatetime) {
		this.serviceOutDatetime = serviceOutDatetime;
	}

	public String getRequestInDatetime() {
		return requestInDatetime;
	}

	public void setRequestInDatetime(String requestInDatetime) {
		this.requestInDatetime = requestInDatetime;
	}

	public void setServiceRoleFrom(String serviceRoleFrom) {
		this.serviceRoleFrom = serviceRoleFrom;
	}
	
}

