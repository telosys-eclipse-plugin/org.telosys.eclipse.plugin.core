package org.telosys.eclipse.plugin.core.telosys;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.telosys.eclipse.plugin.commons.DialogBox;
import org.telosys.eclipse.plugin.core.telosys.commons.TelosysConsole;
import org.telosys.eclipse.plugin.core.telosys.commons.TelosysConsoleType;

public class TelosysConsoleProvider {

	private static final Map<String,TelosysConsole> consolesMap = new HashMap<>();
	
	public static final TelosysConsole getConsole(TelosysConsoleType consoleType) {
		String consoleName = consoleType.getConsoleName();
		
		TelosysConsole telosysConsole = consolesMap.get(consoleName);
		if ( telosysConsole != null ) {
			return telosysConsole;
		}
		else {
			MessageConsole messageConsole = findOrCreateEclipseConsole(consoleName);
	        if ( messageConsole != null ) {
	        	telosysConsole = new TelosysConsoleForEclipse(consoleType, messageConsole, getConsoleManager());
	        	consolesMap.put(consoleName, telosysConsole);
	        	return telosysConsole;
	        }
	        else {
	            DialogBox.showError("Cannot find or create Eclipse console '" + consoleName + "'");
	            return null;
	        }
		}
	}

	private static MessageConsole findOrCreateEclipseConsole(String consoleName) {
		IConsoleManager consoleManager = getConsoleManager();
		MessageConsole messageConsole = findConsole(consoleManager, consoleName);
		if ( messageConsole != null ) {
			return messageConsole;
		}
		else {
	        MessageConsole newConsole = new MessageConsole(consoleName, null); // 2nd param = ImageDescriptor
	        consoleManager.addConsoles(new IConsole[]{newConsole});
	        return newConsole;
		}
	}
	
	private static IConsoleManager getConsoleManager() {
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
	
    private static MessageConsole findConsole(IConsoleManager consoleManager, String consoleName) {
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
    
//    private static MessageConsole createConsole(IConsoleManager consoleManager, String consoleName) {
//        MessageConsole newConsole = new MessageConsole(consoleName, null);
//        consoleManager.addConsoles(new IConsole[]{newConsole});
//        return newConsole;
//    } 
}
