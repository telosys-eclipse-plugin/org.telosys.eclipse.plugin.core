package org.telosys.eclipse.plugin.core.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ProjectExplorerUtil;
import org.telosys.eclipse.plugin.core.commons.ProjectUtil;
import org.telosys.tools.api.TelosysProject;

public class InitHandler extends AbstractHandler {
	
	protected TelosysProject getTelosysProject(IProject project) {
		return new TelosysProject(ProjectUtil.getOSFullPath(project));
	}
	
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
//    	System.out.println("In handler : event = " + event);
        
    	IProject project = ProjectExplorerUtil.getSingleSelectedProject();
        if ( project != null ) {
//                DialogBoxUtil.showInformation("Init this project : " + project.getName() + " \n" 
//                		+ "(" + project.getFullPath().toOSString() + ") \n" 
//                		+ "(" + ProjectUtil.getOSFullPath(project) + ") \n" 
//                		+ "Eclipse directory: \n" 
//                		+ EclipseEnvUtil.getCurrentDirFromSystem() + " \n" 
//                		+ EclipseEnvUtil.getCurrentDirFromPlatform()+ " \n" 
//                		);
                processInitCommand(project);
                ProjectUtil.refresh(project);
        }
        else {
        	// not supposed to happen
            DialogBox.showError("Cannot use this command without a single selected project!" );        	
        }
        return null;
    }
    
    // When the user opens a menu containing the command, 
    // isEnabled() is called to determine whether the command should be enabled (clickable) or disabled (grayed out).
    @Override
    public boolean isEnabled() {
    	return ProjectExplorerUtil.getSingleSelectedProject() != null ;
    }
    
    private void processInitCommand(IProject project) {
    	TelosysProject telosysProject = getTelosysProject(project);
    	String result = telosysProject.initProject();
    	DialogBox.showInformation("Init result : \n" + result);    
    }

}
