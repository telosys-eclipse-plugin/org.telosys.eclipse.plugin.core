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
    
//    private boolean visible = false;

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
            DialogBox.showError("Cannot find or create console '" + name + "'");
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
	private MessageConsole getConsole(String consoleName) {
		IConsoleManager consoleManager = getConsoleManager();
		MessageConsole messageConsole = findConsole(consoleManager, consoleName);
		if ( messageConsole != null ) {
			return messageConsole;
		}
		else {
			return createConsole(consoleManager, consoleName);
		}
	}
	
	public String getName() {
		return consoleName;
	}
	
//    /**
//     * Get or create the console 
//     */
//    private MessageConsole getConsole(String consoleName) {
//        IConsoleManager consoleManager = getConsoleManager();
//        IConsole[] existingConsoles = consoleManager.getConsoles();
//
//        // Check if the console already exists
//        for (IConsole console : existingConsoles) {
//            if (console.getName().equals(consoleName)) {
//            	// Console found
//                return (MessageConsole) console;
//            }
//        }
//
//        // If console does not exist, create a new one
//        MessageConsole newConsole = new MessageConsole(consoleName, null);
//        consoleManager.addConsoles(new IConsole[]{newConsole});
//        return newConsole;
//    }
    
    private MessageConsole findConsole(IConsoleManager consoleManager, String consoleName) {
        IConsole[] existingConsoles = consoleManager.getConsoles();
        // Check if the console already exists
        for (IConsole console : existingConsoles) {
            if (console.getName().equals(consoleName)) {
            	// Console found
                return (MessageConsole) console;
            }
        }
        return null; // Not found
    }
    private MessageConsole createConsole(IConsoleManager consoleManager, String consoleName) {
        MessageConsole newConsole = new MessageConsole(consoleName, null);
        consoleManager.addConsoles(new IConsole[]{newConsole});
        return newConsole;
    }    
    
    public void showConsoleView() {
    	getConsoleManager().showConsoleView(messageConsole);
    }
    
//    /**
//	 * Shows this console in all console views. This console will be become visible
//	 * if another console is currently pinned.   
//	 */
//    public void activate() {
//    	messageConsole.activate();
//    }
    
//    private void checkVisibility() {
//        // Bring console to front the first time
//        if ( ! visible ) {
//            getConsoleManager().showConsoleView(messageConsole);
//            visible = true;
//        }
//    }

    // Print functions using expected streams with "asyncExec()" 
    // You can always use "asyncExec("), it’s a safe practice.
    // It ensures that updates to UI components (like the console) are handled correctly.
    // It avoids potential issues that could arise if SWT or other UI libraries change their threading requirements or if your code becomes more complex.
    
    public void println(String message) {
        println(message, MsgColor.NONE);
    }
    public void println(String message, MsgColor color) {
    	MessageConsoleStream stream = messageConsoleStream;
    	if (color == MsgColor.RED ) {
    		stream = messageConsoleStreamRed;
    	}
    	println(stream, message);
    }
    private void println(MessageConsoleStream stream, String message) {
        // The asyncExec() method ensures that stream.println(message) is executed safely on the SWT UI thread.
        Display.getDefault().asyncExec(() -> {
            stream.println(message);
        });
    }
    
    public void print(String message) {
        print(message, MsgColor.NONE);
    }
    public void print(String message, MsgColor color) {
    	MessageConsoleStream stream = messageConsoleStream;
    	if (color == MsgColor.RED ) {
    		stream = messageConsoleStreamRed;
    	}
    	print(stream, message);
    }
    private void print(MessageConsoleStream stream, String message) {
        // The asyncExec() method ensures that stream.println(message) is executed safely on the SWT UI thread.
        Display.getDefault().asyncExec(() -> {
            stream.print(message);
        });
    }
    
//    public void println() {
//        messageConsoleStream.println();
//    }

    public void clear() {
        messageConsole.clearConsole();
    }
}
