package com.simple.server.config;

public enum ErrorType {
	PubTask("PubTask"), SubTask("SubTask"), WriteTask("WriteTask"), UNKNOWN("UNKNOWN");
	
	private final String value;

	ErrorType(String value) {
		this.value = value;
	}

	public static ErrorType fromValue(String value) {
		if (value != null) {
			for (ErrorType e : values()) {
				if (e.value.equals(value)) {
					return e;
				}
			}
		}
		// you may return a default value
		return getDefault();
		// or throw an exception
		// throw new IllegalArgumentException("Invalid color: " + value);
	}

	public String toValue() {
		return value;
	}

	public static ErrorType getDefault() {
		return UNKNOWN;
	}
}
