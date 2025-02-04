package org.telosys.eclipse.plugin.core.commons;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class TelosysEclipseConsole {
	
    private final String consoleName ;
    private final MessageConsole       messageConsole;
    private final MessageConsoleStream messageConsoleStream ;
    private final MessageConsoleStream messageConsoleStreamRed ;
    
    private boolean visible = false;

    public TelosysEclipseConsole(String name) {
		super();
		this.consoleName = name;
		messageConsole = getConsole(name);
        if ( messageConsole != null ) {
        	messageConsoleStream    = messageConsole.newMessageStream();
            messageConsoleStreamRed = getRedStream(messageConsole);
        }
        else {
        	messageConsoleStream    = null;
            messageConsoleStreamRed = null;
        }
	}
    private MessageConsoleStream getRedStream(MessageConsole messageConsole) {
        MessageConsoleStream out = messageConsole.newMessageStream();
        out.setColor(new Color(Display.getDefault(), 255, 0, 0)); // Red color
        return out;
    }
	private IConsoleManager getConsoleManager() {
        ConsolePlugin consolePlugin = ConsolePlugin.getDefault();
        if ( consolePlugin != null ) {
            IConsoleManager consoleManager = consolePlugin.getConsoleManager();
            if ( consoleManager != null ) {
                return consoleManager;
            }
            else {
            	DialogBox.showError("Cannot get ConsoleManager");
            }
        }
        else {
        	DialogBox.showError("Cannot get ConsolePlugin");
        }
        return null;
    }
    
    /**
     * Get or create the console 
     */
    private MessageConsole getConsole(String consoleName) {
        IConsoleManager consoleManager = getConsoleManager();
        IConsole[] existingConsoles = consoleManager.getConsoles();

        // Check if the console already exists
        for (IConsole console : existingConsoles) {
            if (console.getName().equals(consoleName)) {
            	// Console found
                return (MessageConsole) console;
            }
        }

        // If console does not exist, create a new one
        MessageConsole newConsole = new MessageConsole(consoleName, null);
        consoleManager.addConsoles(new IConsole[]{newConsole});
        return newConsole;
    }
    
    public void showConsoleView() {
    	getConsoleManager().showConsoleView(messageConsole);
    }
    
    private void checkVisibility() {
        // Bring console to front the first time
        if ( ! visible ) {
            getConsoleManager().showConsoleView(messageConsole);
            visible = true;
        }
    }

    public void println(String message) {
        println(message, MsgColor.NONE);
    }
    public void println(String message, MsgColor color) {
    	if (color == MsgColor.RED ) {
    		messageConsoleStreamRed.println(message);
    	}
    	else {
    		messageConsoleStream.println(message);
    	}
    }
    
    public void print(String message) {
        print(message, MsgColor.NONE);
    }
    public void print(String message, MsgColor color) {
    	if (color == MsgColor.RED ) {
    		messageConsoleStreamRed.print(message);
    	}
    	else {
    		messageConsoleStream.print(message);
    	}
    }
    
    public void println() {
        messageConsoleStream.println();
    }

    public void clear() {
        messageConsole.clearConsole();
    }
}
