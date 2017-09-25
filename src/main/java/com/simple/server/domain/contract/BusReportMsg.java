package com.simple.server.domain.contract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class BusReportMsg  extends AContract{
	
	private static final long serialVersionUID = 1L;
	private String tag;
	private String sqlTemplate;
	private List<BusReportItem> items;

			
	@Override
	public String getClazz() {
		return this.getClass().getName();
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getSqlTemplate() {
		return sqlTemplate;
	}

	public void setSqlTemplate(String sqlTemplate) {
		this.sqlTemplate = sqlTemplate;
	}

	public List<BusReportItem> getItems() {
		return items;
	}

	public void setItems(List<BusReportItem> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "BusReportMsg [tag=" + tag + ", sqlTemplate=" + sqlTemplate + ", items=" + items + "]";
	}

}
