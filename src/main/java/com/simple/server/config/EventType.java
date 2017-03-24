package com.simple.server.config;

public enum EventType {
	
	ANY("ANY"), 
	CHANGE_CUST("CHANGE_CUST"), 
	CHANGE_CONTACT("CHANGE_CONTACT"), 
	CHANGE_SORDER("CHANGE_SORDER"), 
	CONFIRM("CONFIRM"), 
	SETTINGS("SETTINGS"),	
	OKT_CONTACT_CUST("OKT_CONTACT_CUST"),
	OKT_CONTACT_EMPLOYEE("OKT_CONTACT_EMPLOYEE"),
	OKT_CUSTOMER("OKT_CUSTOMER"),
	OKT_EMPLOYEE("OKT_EMPLOYEE"),
	
	UNKNOWN("UNKNOWN");
	
	private final String value;

	EventType(String value) {
		this.value = value;
	}

	public static EventType fromValue(String value) {
		if (value != null) {
			for (EventType event : values()) {
				if (event.value.equals(value)) {
					return event;
				}
			}
		}
		return getDefault();
	}

	public String toValue() {
		return value;
	}

	public static EventType getDefault() {
		return UNKNOWN;
	}
}
