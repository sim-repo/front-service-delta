package com.simple.server.config;

public enum OperationType {
	
	READ("READ"), WRITE("WRITE"), PUB("PUB"), SUB("SUB"), MON_START("MON_START"), MON_REP("MON_REP"), UNKNOWN("UNKNOWN");
	
	private final String value;

	OperationType(String value) {
		this.value = value;
	}

	public static OperationType fromValue(String value) {
		if (value != null) {
			for (OperationType operation : values()) {
				if (operation.value.equals(value)) {
					return operation;
				}
			}
		}
		return getDefault();
	}

	public String toValue() {
		return value;
	}

	public static OperationType getDefault() {
		return UNKNOWN;
	}
}
