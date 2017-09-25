package com.simple.server.domain.contract;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.simple.server.domain.contract.BusClassificator;

public class BusClassificator extends ALogContract{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	private String desc;
	@JsonIgnore
	private BusClassificator parent = null;	

	private Integer level = 1;
	private Short clickable = 0;
	private Set<BusClassificator> children = new HashSet<BusClassificator>();
	
	
	
	@Override
	public String getClazz() {
		return this.getClass().getName();
	}				
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public BusClassificator getParent() {
		return parent;
	}
	public void setParent(BusClassificator parent) {
		this.parent = parent;
	}	
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Short getClickable() {
		return clickable;
	}
	public void setClickable(Short clickable) {
		this.clickable = clickable;
	}
	public Set<BusClassificator> getChildren() {
		return children;
	}
	public void setChildren(Set<BusClassificator> children) {
		this.children = children;
	}

	
	
}
