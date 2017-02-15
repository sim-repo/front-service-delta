package com.simple.server.domain.contract.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class Dimension {
	
	public String id;
	public String desc;
	public String type;
	public Integer min_val;
	public Integer max_val;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getMin_val() {
		return min_val;
	}
	public void setMin_val(Integer min_val) {
		this.min_val = min_val;
	}
	public Integer getMax_val() {
		return max_val;
	}
	public void setMax_val(Integer max_val) {
		this.max_val = max_val;
	}		
	
}
