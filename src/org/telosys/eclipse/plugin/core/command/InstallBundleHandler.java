package org.telosys.eclipse.plugin.core.command;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.telosys.eclipse.plugin.core.commons.AbstractMenuHandler;
import org.telosys.eclipse.plugin.core.commons.ProjectExplorerUtil;
import org.telosys.tools.api.InstallationType;
import org.telosys.tools.api.TelosysProject;

public class InstallBundleHandler extends AbstractMenuHandler {

    // When the user opens a menu containing the command, 
    // isEnabled() is called to determine whether the command should be enabled (clickable) or disabled (grayed out).
    @Override
    public boolean isEnabled() {
    	IProject project = ProjectExplorerUtil.getSingleSelectedProject();
    	// In a project and the selected project is not a Telosys model/bundle directory
    	return project != null  &&  !isModelDirectory(project)  &&  !isBundleDirectory(project) ;
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	IProject project = ProjectExplorerUtil.getSingleSelectedProject(true);
        if ( project != null ) {
    		openDialogBox(project);
    		// TODO
//        	if ( isTelosysProject(project) ) {
//        		openDialogBox(project);
//        	}
//        	else {
//        		notTelosysProjectMessage();
//        	}
        }
        return null;
    	
//    	IProject project = ProjectExplorerUtil.getProjectFromSelection();
//        if ( project != null ) {
//        	Shell shell = getShell();
//        	TelosysProject telosysProject = getTelosysProject(project);
//        	String depot = telosysProject.getTelosysToolsCfg().getDepotForBundles(); 
//        	InstallDialogBox dialogBox = new InstallDialogBox(shell, project, depot, InstallationType.BUNDLE );
//        	dialogBox.open(); // show dialog box immediately 
//        	// nothing else to do, all the work is done in the DialogBox component
//        }
//        else {
//        	DialogBox.showError("Cannot get project for selected element");
//        }
//        return null;
    }
    
    private void openDialogBox(IProject project) {
    	TelosysProject telosysProject = getTelosysProject(project);
    	String depot = telosysProject.getTelosysToolsCfg().getDepotForBundles(); 
    	InstallDialogBox dialogBox = new InstallDialogBox(getShell(), project, depot, InstallationType.BUNDLE );
    	dialogBox.open(); // show dialog box immediately 
    	// nothing else to do, all the work is done in the DialogBox component        	
    }
    
}
