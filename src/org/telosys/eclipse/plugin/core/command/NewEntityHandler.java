package org.telosys.eclipse.plugin.core.command;

import java.io.File;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.window.Window;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ProjectExplorerUtil;
import org.telosys.eclipse.plugin.core.commons.ProjectUtil;
import org.telosys.eclipse.plugin.core.commons.WorkspaceUtil;
import org.telosys.tools.api.TelosysProject;

public class NewEntityHandler extends AbstractCommandHandler {
	
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	String selectedModel = null;
    	IProject selectedProject = null;
    	IResource selectedResource = ProjectExplorerUtil.getSingleSelectedResource();
    	if ( selectedResource != null ) {
        	IFolder selectedModelFolder = getSelectedModel(selectedResource);
        	if ( selectedModelFolder != null ) {
        		// one model is selected
        		selectedModel = selectedModelFolder.getName();
        		selectedProject = selectedModelFolder.getProject();
        	}
        	else {
        		// no model selected
        		selectedModel = null;
        		selectedProject = selectedResource.getProject();
        	}
        	String[] projectModels = getProjectModels(selectedProject);
        	// Open dialog box
        	// Open dialog box
        	NewEntityDialogBox dialogBox = new NewEntityDialogBox(getShell(), selectedProject, projectModels, selectedModel );
    		if ( dialogBox.open() == Window.OK ) {
    			//TODO 
    			String modelName = dialogBox.getModelName();
    			String entityName = dialogBox.getEntityName();
            	DialogBox.showInformation("TODO", "Create entity '" + entityName + "' in model '" + modelName+"'");
    		}
        	// nothing else to do, all the work is done in the DialogBox component        	
    	}
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
