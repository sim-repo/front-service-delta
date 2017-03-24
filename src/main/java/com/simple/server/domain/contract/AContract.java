package com.simple.server.domain.contract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.springframework.messaging.MessageChannel;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.simple.server.config.AppConfig;
import com.simple.server.config.ContentType;
import com.simple.server.config.EndpointType;
import com.simple.server.config.ErrorType;
import com.simple.server.config.EventType;
import com.simple.server.config.OperationType;
import com.simple.server.config.RoleType;

@JsonAutoDetect
public abstract class AContract implements IContract {
	
	protected static final long serialVersionUID = 1L;
	
	protected String clazz;		
	protected String juuid;		
	protected String eventId;		
	
	protected String senderId;	
	protected String endPointId;	
	
	protected String subscriberId;
	protected String subscriberHandler;
	protected String subscriberStoreClass;
	protected String publisherId;
	protected String publisherHandler;
	protected String publisherStoreClass;	
	
	
	protected String operationType;	
	protected String serviceIdFrom;	
	protected String serviceIdTo;			
	protected String serviceRoleFrom = RoleType.FRONT_SERVICE.toString();	
	protected String serviceOutDatetime;	
	protected String requestInDatetime;			
	protected String responseURI;	
	protected String responseContentType;	
	protected String responseContractClass;
	protected String methodHandler;		
	protected Boolean isDirectInsert = false; 	
	protected Class<? extends ALogContract> logClass; 

	protected String messageHeaderValue;
	protected String messageBodyValue;	
	protected String logDatetime;	
	protected String loggerId;	
	
	protected String errorId;
	protected String details;
	
	
	@JsonIgnore
	@XmlTransient 
	protected MessageChannel channel;
	
	
	@Override
	public String getJuuid() {	
		return juuid;
	}
	
	@XmlElement(name = "juuid")
	@Override
	public void setJuuid(String juuid) {
		this.juuid = juuid;
	}

	@Override
	public EventType getEventId() {
		return EventType.fromValue(eventId);
	}
	
	@XmlElement(name = "eventId")
	@Override
	public void setEventId(EventType eventId) {
		this.eventId = eventId.toValue();
	}
	
	
	public EndpointType getSenderId() {
		return EndpointType.fromValue(senderId);
	}

	@XmlElement(name = "senderId")
	public void setSenderId(EndpointType senderId) {
		this.senderId = senderId.toValue();
	}

	@Override
	public EndpointType getEndPointId() {
		return EndpointType.fromValue(endPointId);
	}

	@Override
	public void setEndPointId(EndpointType endPointId) {
		this.endPointId = endPointId.toValue();
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
		return ContentType.fromValue(responseContentType);
	}
	
	public void setResponseContentType(ContentType responseContentType){
		this.responseContentType = responseContentType.toValue();
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
	@XmlTransient 
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
		return OperationType.fromValue(operationType);
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType.toValue();
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

	@Override
	public Boolean getIsDirectInsert() {
		return isDirectInsert;
	}
	@Override
	public void setIsDirectInsert(Boolean isDirectInsert) {
		this.isDirectInsert = isDirectInsert;
	}

	public EndpointType getSubscriberId() {
		return EndpointType.fromValue(subscriberId);
	}

	public void setSubscriberId(EndpointType subscriberId) {
		this.subscriberId = subscriberId.toValue();
	}

	public String getSubscriberHandler() {
		return subscriberHandler;
	}

	public void setSubscriberHandler(String subscriberHandler) {
		this.subscriberHandler = subscriberHandler;
	}

	public String getSubscriberStoreClass() {
		return subscriberStoreClass;
	}

	public void setSubscriberStoreClass(String subscriberStoreClass) {
		this.subscriberStoreClass = subscriberStoreClass;
	}

	public EndpointType getPublisherId() {
		return EndpointType.fromValue(publisherId);
	}

	public void setPublisherId(EndpointType publisherId) {
		this.publisherId = publisherId.toValue();
	}

	public String getPublisherHandler() {
		return publisherHandler;
	}

	public void setPublisherHandler(String publisherHandler) {
		this.publisherHandler = publisherHandler;
	}

	public String getPublisherStoreClass() {
		return publisherStoreClass;
	}

	public void setPublisherStoreClass(String publisherStoreClass) {
		this.publisherStoreClass = publisherStoreClass;
	}

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

	public String getLogDatetime() {
		return logDatetime;
	}

	public void setLogDatetime(String logDatetime) {
		this.logDatetime = logDatetime;
	}

	public String getLoggerId() {
		return loggerId;
	}

	public void setLoggerId(String loggerId) {
		this.loggerId = loggerId;
	}

	public ErrorType getErrorId() {
		return ErrorType.fromValue(errorId);
	}

	public void setErrorId(ErrorType errorId) {
		this.errorId = errorId.toValue();
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	
	@Override
	public void copyFrom(IContract _msg) throws Exception{
		AContract msg = (AContract)_msg;
		
		this.setServiceOutDatetime(new SimpleDateFormat(AppConfig.DATEFORMAT).format(Calendar.getInstance().getTime()));		
		if(this.getJuuid() == null)
			this.setJuuid(msg.getJuuid());		
		
		if(this.getEndPointId().equals(EndpointType.UNKNOWN))
			this.setEndPointId(msg.getEndPointId());
		
		if(this.getSenderId().equals(EndpointType.UNKNOWN))
			this.setSenderId(msg.getSenderId());
		
		if(this.getPublisherId().equals(EndpointType.UNKNOWN))
			this.setPublisherId(msg.getPublisherId());
		
		if(this.getSubscriberId().equals(EndpointType.UNKNOWN))
			this.setSubscriberId(msg.getSubscriberId());
		
		if(this.getEventId().equals(EventType.UNKNOWN))
			this.setEventId(msg.getEventId());
		
		if(this.getResponseURI() == null)
			this.setResponseURI(msg.getResponseURI());
		
		if(this.getResponseContentType() == null)
			this.setResponseContentType(msg.getResponseContentType());
		
		if(this.getResponseContractClass() == null)
			this.setResponseContractClass(msg.getResponseContractClass());	
		
		if(this.getMethodHandler() == null)
			this.setMethodHandler(msg.getMethodHandler());
		
		if(this.getMessageBodyValue() == null)
			this.setMessageBodyValue(msg.toString());
		
		if(this.getLogDatetime() == null){
			this.setLogDatetime(msg.getLogDatetime());
		}		
	
		this.setMessageHeaderValue(this.getClass().getSimpleName());					
	}
	
}

