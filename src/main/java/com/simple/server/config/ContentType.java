package com.simple.server.config;

public enum ContentType {
	
	JsonPlainText("JsonPlainText"), 
	XmlPlainText("XmlPlainText"), 
	ApplicationJson("ApplicationJson"), 
	ApplicationXml("ApplicationXml"), 
	UNKNOWN("UNKNOWN");

	private final String value;

	ContentType(String value) {
		this.value = value;
	}

	public static ContentType fromValue(String value) {
		if (value != null) {
			for (ContentType ct : values()) {
				if (ct.value.equals(value)) {
					return ct;
				}
			}
		}
		return getDefault();
	}

	public String toValue() {
		return value;
	}

	public static ContentType getDefault() {
		return UNKNOWN;
	}
}
