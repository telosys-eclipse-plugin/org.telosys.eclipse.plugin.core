package org.telosys.eclipse.plugin.core.telosys;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.telosys.eclipse.plugin.core.telosys.commons.ConsoleColor;
import org.telosys.eclipse.plugin.core.telosys.commons.TelosysConsole;
import org.telosys.eclipse.plugin.core.telosys.commons.TelosysConsoleType;

public class TelosysConsoleForEclipse extends TelosysConsole {
	
//    private final String consoleName ;
	private final IConsoleManager      consoleManager; 
    private final MessageConsole       messageConsole;
    private final MessageConsoleStream messageConsoleStream ;
    private final MessageConsoleStream messageConsoleStreamRED ;
    
    public TelosysConsoleForEclipse(TelosysConsoleType consoleType, MessageConsole messageConsole, IConsoleManager consoleManager) {
		super(consoleType);		
		this.messageConsole = messageConsole;
		this.consoleManager = consoleManager;
		
		// Standard stream
       	this.messageConsoleStream    = messageConsole.newMessageStream();
       	
		// RED color stream
        this.messageConsoleStreamRED = messageConsole.newMessageStream();
        this.messageConsoleStreamRED.setColor(new Color(Display.getDefault(), 255, 0, 0)); // Red color
	}
    
	@Override
    public void showConsoleView() {
    	consoleManager.showConsoleView(messageConsole);
    }
    
    // Print functions using expected streams with "asyncExec()" 
    // You can always use "asyncExec("), it’s a safe practice.
    // It ensures that updates to UI components (like the console) are handled correctly.
    // It avoids potential issues that could arise if SWT or other UI libraries change their threading requirements or if your code becomes more complex.
    
	@Override
    public void println(String message) {
        println(message, ConsoleColor.NONE);
    }
	
	@Override
    public void println(String message, ConsoleColor color) {
    	MessageConsoleStream stream = messageConsoleStream;
    	if (color == ConsoleColor.RED ) {
    		stream = messageConsoleStreamRED;
    	}
    	asyncPrintln(stream, message);
    }
    private void asyncPrintln(MessageConsoleStream stream, String message) {
        // The asyncExec() method ensures that stream.println(message) is executed safely on the SWT UI thread.
        Display.getDefault().asyncExec( () -> stream.println(message) );
    }
    
	@Override
    public void clear() {
        messageConsole.clearConsole();
    }
}
