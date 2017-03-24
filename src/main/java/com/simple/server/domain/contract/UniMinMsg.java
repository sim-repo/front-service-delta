package com.simple.server.domain.contract;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.simple.server.config.ContentType;
import com.simple.server.config.EventType;


@JsonAutoDetect
@JsonDeserialize(as = UniMinMsg.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UniMinMsg {
	
	protected String clazz;
	protected String juuid;
	protected String eventId;
	protected String body;
	protected String url;
	protected String datetime;
	protected String contentType;
	
	public String getClazz() {
		return this.getClass().getName();
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	public String getJuuid() {
		return juuid;
	}
	public void setJuuid(String juuid) {
		this.juuid = juuid;
	}
	public EventType getEventId() {
		return EventType.valueOf(eventId);
	}
	public void setEventId(EventType eventId) {
		this.eventId = eventId.toValue();
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}	
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public ContentType getContentType() {
		return ContentType.valueOf(contentType);
	}
	public void setContentType(ContentType contentType) {
		this.contentType = contentType.toValue();
	}
	public void copyFrom(IContract msg){
		if(msg instanceof UniMsg){
			UniMsg uniMsg = (UniMsg)msg;
			this.setBody(uniMsg.getBody());
			this.setClazz(uniMsg.getBody());
			this.setEventId(uniMsg.getEventId());
			this.setJuuid(uniMsg.getJuuid().toString());
			this.setUrl(uniMsg.getResponseURI());
			
		}
	}
	@Override
	public String toString() {
		return "UniMinMsg [clazz=" + clazz + ", juuid=" + juuid + ", eventId=" + eventId + ", body=" + body + ", url="
				+ url + ", datetime=" + datetime + ", contentType=" + contentType + "]";
	}
	
	
	
}
