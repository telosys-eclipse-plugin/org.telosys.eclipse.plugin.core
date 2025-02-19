package org.telosys.eclipse.plugin.core.telosys;

import org.telosys.eclipse.plugin.core.telosys.commons.TelosysConsole;
import org.telosys.tools.commons.logger.GenericLogger;

public class TelosysLoggerForEclipse extends GenericLogger {

	private final TelosysConsole console ;
	
	public TelosysLoggerForEclipse(TelosysConsole console) {
		super();
		this.console = console;
	}

	@Override
	protected void print(String msg) {
		console.println(msg);
	}

}
