package org.telosys.eclipse.plugin.core.commons;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class WorkbenchUtil {
    
    public static IWorkbench getWorkbench() {
    	IWorkbench workbench = PlatformUI.getWorkbench();
    	if ( workbench == null ) {
    		// Not supposed to happen 
			DialogBox.showError("Cannot get workbench!");
    	}
   		return workbench;
    }
    
    public static IWorkbenchWindow getActiveWindow() {
    	IWorkbenchWindow workbenchWindow = null;
    	IWorkbench workbench = getWorkbench();
    	if ( workbench != null ) {
    		workbenchWindow = workbench.getActiveWorkbenchWindow();
    		if ( workbenchWindow == null ) {
        		// Not supposed to happen 
    			DialogBox.showError("Cannot get IWorkbenchWindow from IWorkbench!");
    		}
    	}
    	return workbenchWindow;
    }    

    public static Shell getActiveWindowShell() {
    	Shell shell = null;
    	IWorkbenchWindow workbenchWindow = getActiveWindow();
    	if ( workbenchWindow != null ) {
    		shell = workbenchWindow.getShell();
    		if ( shell == null ) {
        		// Not supposed to happen 
    			DialogBox.showError("Cannot get Shell from IWorkbenchWindow!");
    		}
    	}
    	return shell;
    }    

    public static IWorkbenchPage getActiveWindowPage() {
    	IWorkbenchPage workbenchPage = null;
    	IWorkbenchWindow workbenchWindow = getActiveWindow();
    	if ( workbenchWindow != null ) {
    		workbenchPage = workbenchWindow.getActivePage();
    		if ( workbenchPage == null ) {
        		// Not supposed to happen 
    			DialogBox.showError("Cannot get IWorkbenchPage from IWorkbenchWindow!");
    		}
    	}
    	return workbenchPage;
    }    
}
