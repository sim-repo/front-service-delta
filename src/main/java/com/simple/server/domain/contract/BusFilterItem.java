package com.simple.server.domain.contract;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonAutoDetect
public class BusFilterItem extends ALogContract{
	
	private static final long serialVersionUID = 1L;
	@JsonBackReference
	private BusFilterGroup filterGroup;
	@JsonProperty("id")
	private Integer id;	
	@JsonProperty("model_field")
	private String modelField;

	
	
	@JsonProperty("value")
	private String value;
	@JsonProperty("value_caption")
	private String valueCaption;
	@JsonProperty("min_value")
	private Integer minValue;
	@JsonProperty("max_value")
	private Integer maxValue;
		
	@JsonProperty("type")
	private String type;
	@JsonIgnore
	@Override
	public String getClazz() {
		return this.getClass().getName();
	}
	public BusFilterGroup getFilterGroup() {
		return filterGroup;
	}
	public void setFilterGroup(BusFilterGroup filterGroup) {
		this.filterGroup = filterGroup;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getModelField() {
		return modelField;
	}
	public void setModelField(String modelField) {
		this.modelField = modelField;
	}
	public Integer getMinValue() {
		return minValue;
	}
	public void setMinValue(Integer minValue) {
		this.minValue = minValue;
	}
	public Integer getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}
	public String getValueCaption() {
		return valueCaption;
	}
	public void setValueCaption(String valueCaption) {
		this.valueCaption = valueCaption;
	}
	

}
