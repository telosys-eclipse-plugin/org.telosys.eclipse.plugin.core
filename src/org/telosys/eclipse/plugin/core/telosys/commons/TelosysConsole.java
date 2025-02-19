package org.telosys.eclipse.plugin.core.telosys.commons;

public abstract class TelosysConsole {
	
	private final TelosysConsoleType consoleType;
	
	public TelosysConsole(TelosysConsoleType consoleType) {
		super();
		this.consoleType = consoleType;
	}
	
	public TelosysConsoleType getConsoleType() {
		return consoleType;
	}

	public String getConsoleName() {
		return consoleType.getConsoleName();
	}
	
	public abstract void clear();
	
	public abstract void println(String message);
	
	public abstract void println(String message, ConsoleColor color);
	
	public abstract void showConsoleView();
}
