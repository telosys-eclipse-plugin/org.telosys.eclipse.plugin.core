package org.telosys.eclipse.plugin.core.command;

import java.io.File;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.window.Window;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ProjectExplorerUtil;
import org.telosys.eclipse.plugin.core.commons.ProjectUtil;
import org.telosys.eclipse.plugin.core.commons.WorkspaceUtil;
import org.telosys.tools.api.TelosysProject;

public class NewModelHandler extends AbstractCommandHandler {
	
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	IProject project = ProjectExplorerUtil.getProjectFromSelection();
        if ( project != null ) {
    		NewModelDialogBox dialogBox = new NewModelDialogBox( getShell() );
    		if ( dialogBox.open() == Window.OK ) {
    			String modelName = dialogBox.getModelName();
    			File file = processNewModelCommand(project, modelName);
    			if ( file != null ) {
    				// Model created => refresh and expand
    	            ProjectUtil.refresh(project);
    	            ProjectExplorerUtil.expandFolder(WorkspaceUtil.getIFolder(file));
    			}
    		}
        }
        // else : if multiple selected elements 
        else {
        	DialogBox.showError("Cannot get project for selected element");
        }
        return null;
    }

//    // When the user opens a menu containing the command, 
//    // isEnabled() is called to determine whether the command should be enabled (clickable) or disabled (grayed out).
//    @Override
//    public boolean isEnabled() {
//    	//return ProjectExplorerUtil.getSingleSelectedProject() != null ;
//    	return isValidSelectionForModelCommand();
//    }
//    
//    @Override
//    public void setEnabled(Object evaluationContext) {
//        // Use your logic to decide and set the enabled state
//        setBaseEnabled(isEnabled());
//    }    
//
//    public boolean isValidSelectionForModelCommand() {
//        Object selectedElement = ProjectExplorerUtil.getSingleSelectedElement();
//        System.out.println("isValidSelectionForModelCommand(): selectedElement = " + selectedElement);
//        if ( selectedElement instanceof IProject ) {
//        	return true;
//        }
//        else if ( selectedElement instanceof IFolder ) {
//        	IFolder folder = (IFolder) selectedElement;
//        	String folderName = folder.getName();
//        	return "TelosysTools".equals(folderName) || "models".equals(folderName);
//        }
//		return false;
//    }
    
    private File processNewModelCommand(IProject project, String modelName) {
    	TelosysProject telosysProject = getTelosysProject(project);
    	if ( telosysProject.modelFolderExists(modelName) ) {
    		DialogBox.showWarning("Model '" + modelName + "' already exists");
    		return null;
    	}
    	else {
    		return telosysProject.createNewDslModel(modelName);
    	}
    }
}
