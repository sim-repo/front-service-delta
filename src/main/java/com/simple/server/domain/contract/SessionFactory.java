package com.simple.server.domain.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = SessionFactory.class)
public class SessionFactory extends ALogContract{
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String endpointId;
	private String strSessionFactory;
	private String endpointGroupId;
	private Boolean defaultEndpointId;
	
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

	public String getEndpointId() {
		return endpointId;
	}

	public void setEndpointId(String endpointId) {
		this.endpointId = endpointId;
	}

	public String getStrSessionFactory() {
		return strSessionFactory;
	}

	public void setStrSessionFactory(String strSessionFactory) {
		this.strSessionFactory = strSessionFactory;
	}

	public String getEndpointGroupId() {
		return endpointGroupId;
	}

	public void setEndpointGroupId(String endpointGroupId) {
		this.endpointGroupId = endpointGroupId;
	}

	public Boolean getDefaultEndpointId() {
		return defaultEndpointId;
	}

	public void setDefaultEndpointId(Boolean defaultEndpointId) {
		this.defaultEndpointId = defaultEndpointId;
	}
	
	
	
}
