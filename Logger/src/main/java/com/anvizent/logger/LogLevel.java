package com.anvizent.logger;

public enum LogLevel {

	NONE(0), MINIMAL(1), REQUIRED(2), SEMI(3), MIDIUM(4), ALL(5);

	private int level;

	private LogLevel(int level) {
		this.level = level;
	}

	public boolean canLogFor(LogLevel logLevel) {
		return this.level <= logLevel.level;
	}
}
