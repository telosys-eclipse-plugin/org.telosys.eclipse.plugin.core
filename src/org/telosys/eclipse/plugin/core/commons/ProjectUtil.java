package org.telosys.eclipse.plugin.core.commons;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.telosys.eclipse.plugin.commons.DialogBox;
import org.telosys.eclipse.plugin.core.telosys.TelosysConsoleProvider;
import org.telosys.eclipse.plugin.core.telosys.TelosysLoggerForEclipse;
import org.telosys.eclipse.plugin.core.telosys.commons.TelosysConsoleType;
import org.telosys.tools.api.TelosysProject;

public class ProjectUtil {
    
    /**
     * Returns the Operating System full path for the given Eclipse project
     * @param eclipseProject
     * @return
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
        	DialogBox.showError("Cannot get OS full path: project is null or doesn't exist!");
        }
    	return null;
    }
    
    public static TelosysProject getTelosysProject(IProject eclipseProject) {
//    	TelosysLoggerForEclipse logger = new TelosysLoggerForEclipse(new TelosysConsoleForEclipse(TelosysConsoleType.TELOSYS_LOGGER));
    	TelosysLoggerForEclipse logger = new TelosysLoggerForEclipse( TelosysConsoleProvider.getConsole(TelosysConsoleType.TELOSYS_LOGGER) );
   		return getTelosysProject(eclipseProject, logger);
    }
    
    public static TelosysProject getTelosysProject(IProject eclipseProject, TelosysLoggerForEclipse logger) {
   		return new TelosysProject(ProjectUtil.getOSFullPath(eclipseProject), logger);
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
