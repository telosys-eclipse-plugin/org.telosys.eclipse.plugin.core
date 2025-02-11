package org.telosys.eclipse.plugin.core.command;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.telosys.eclipse.plugin.core.commons.AbstractMenuHandler;
import org.telosys.eclipse.plugin.core.commons.ProjectExplorerUtil;
import org.telosys.eclipse.plugin.core.commons.Validator;
import org.telosys.eclipse.plugin.core.commons.dialogbox.InstallDialogBox;
import org.telosys.tools.api.InstallationType;
import org.telosys.tools.api.TelosysProject;

public class InstallBundleHandler extends AbstractMenuHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	IProject project = ProjectExplorerUtil.getSingleSelectedProject(true);
        if ( project != null ) {
        	if ( Validator.isTelosysProject(project) ) {
        		// Open the window
        		openDialogBox(project);
        	}
        	else {
        		notTelosysProjectMessage();
        	}
        }
        return null;
    }
    
    private void openDialogBox(IProject project) {
    	TelosysProject telosysProject = getTelosysProject(project);
    	String depot = telosysProject.getTelosysToolsCfg().getDepotForBundles(); 
//    	InstallDialogBox dialogBox = new InstallDialogBox(getShell(), project, depot, InstallationType.BUNDLE );
    	InstallDialogBox dialogBox = new InstallDialogBox(telosysProject, depot, InstallationType.BUNDLE );
    	dialogBox.open(); // show dialog box immediately 
    	// nothing else to do, all the work is done in the DialogBox component        	
    }
    
}
