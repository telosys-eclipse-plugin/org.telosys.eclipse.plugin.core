package org.telosys.eclipse.plugin.core.telosys;

import org.telosys.tools.commons.logger.GenericLogger;

public class TelosysLoggerForEclipse extends GenericLogger {

	private final TelosysEclipseConsole console ;
	
	public TelosysLoggerForEclipse(TelosysEclipseConsole console) {
		super();
		this.console = console;
	}

	@Override
	protected void print(String msg) {
		console.println(msg);
	}

}
