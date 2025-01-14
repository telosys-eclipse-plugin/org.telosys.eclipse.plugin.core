package org.telosys.eclipse.plugin.core.commons;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

public class ProjectUtil {
    
    /**
     * Show information message
     * @param message
     */
    public static String getOSFullPath(IProject eclipseProject) {
        if (eclipseProject != null && eclipseProject.exists()) {
        	// Get the absolute path in the local file system
            IPath location = eclipseProject.getLocation();
            if (location != null) {
            	// Converts to an OS-specific absolute path
                return location.toOSString(); 
            }        	
        }
        else {
        	DialogBox.showWarning("Cannot get OS full path: project is null or doesn't exist!");
        }
    	return null;
    }
    
    /**
     * Refreshes the resource hierarchy from this resource and its children (to the specified depth) relative to the local file system.
     * @param eclipseProject
     */
    public static void refresh(IProject eclipseProject) {
        try {
        	eclipseProject.refreshLocal(IResource.DEPTH_INFINITE, null);
        } catch (CoreException e) {
            // Handle the exception appropriately
        	DialogBox.showError("Cannot refresh project " + eclipseProject.getName() + "\n"
        			+ "Exception: " + e.getClass().getCanonicalName() + "\n"
        			+ "Message: "  + e.getMessage() );
        }
    }
}
