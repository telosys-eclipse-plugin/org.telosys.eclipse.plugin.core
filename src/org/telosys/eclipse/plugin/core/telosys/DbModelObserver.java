package org.telosys.eclipse.plugin.core.telosys;

import org.telosys.eclipse.plugin.core.telosys.commons.TelosysConsole;
import org.telosys.eclipse.plugin.core.telosys.commons.TelosysConsoleType;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.observer.TaskObserver2;

public class DbModelObserver implements TaskObserver2<Integer, String> {
	
	private final TelosysConsole console;
	
	/**
	 * Constructor
	 */
	public DbModelObserver() {
		super();
		this.console = TelosysConsoleProvider.getConsole(TelosysConsoleType.TELOSYS_CONSOLE);
	}
	
	@Override
	public void notify(Integer level, String msg) {
		
		int n = level < 10 ? level : 10 ;
		String s = StrUtil.repeat(' ', n);
		console.println(s + msg );
	}
}
