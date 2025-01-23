package org.telosys.eclipse.plugin.core.command;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.telosys.eclipse.plugin.core.commons.AbstractMenuHandler;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ProjectExplorerUtil;
import org.telosys.eclipse.plugin.core.commons.ProjectUtil;
import org.telosys.eclipse.plugin.core.commons.Validator;
import org.telosys.tools.api.TelosysProject;

public class InitHandler extends AbstractMenuHandler {
	
	protected TelosysProject getTelosysProject(IProject project) {
		return new TelosysProject(ProjectUtil.getOSFullPath(project));
	}
	
//    // When the user opens a menu containing the command, 
//    // isEnabled() is called to determine whether the command should be enabled (clickable) or disabled (grayed out).
//    @Override
//    public boolean isEnabled() {
//    	IProject project = ProjectExplorerUtil.getSingleSelectedProject();
//    	// In a project and the selected project is not a Telosys model/bundle directory
//    	return project != null  &&  !isModelDirectory(project)  &&  !isBundleDirectory(project) ;
//    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        
    	IProject project = ProjectExplorerUtil.getSingleSelectedProject(true);
        if ( project != null ) {
        	if ( Validator.isTelosysProject(project) ) {
                processInitCommand(project);
                ProjectUtil.refresh(project);
        	}
        	else {
        		notTelosysProjectMessage();
        	}
        }
        return null;
    }

    private void processInitCommand(IProject project) {
    	TelosysProject telosysProject = getTelosysProject(project);
    	String result = telosysProject.initProject();
    	DialogBox.showInformation("Init result : \n" + result);    
    }

}
