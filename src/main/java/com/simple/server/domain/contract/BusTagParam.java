package com.simple.server.domain.contract;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.simple.server.config.SqlType;

@JsonAutoDetect
public class BusTagParam extends ALogContract{
	private static final long serialVersionUID = 1L;
	private int id;	
	@JsonBackReference
	private BusTagTemplate tagTemplate;
	@JsonProperty("param_name")
	private String paramName;
	@JsonProperty("desc")
	private String paramDesc;
	@JsonProperty("type")
	private String sqlType;
	private String sample;
	private int required;

	public BusTagParam(){};

	public BusTagParam(BusTagTemplate tag, String paramName, String paramDesc) {
		super();
		this.tagTemplate = tag;
		this.paramName = paramName;
		this.paramDesc = paramDesc;
	}
	@JsonIgnore
	@Override
	public String getClazz() {
		return BusTagParam.class.getSimpleName();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamDesc() {
		return paramDesc;
	}

	public void setParamDesc(String paramDesc) {
		this.paramDesc = paramDesc;
	}
	
	public BusTagTemplate getTagTemplate() {
		return tagTemplate;
	}

	public void setTagTemplate(BusTagTemplate tagTemplate) {
		this.tagTemplate = tagTemplate;
	}

	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}

	public String getSample() {
		return sample;
	}

	public void setSample(String sample) {
		this.sample = sample;
	}

	public int getRequired() {
		return required;
	}

	public void setRequired(int required) {
		this.required = required;
	}
	
}
