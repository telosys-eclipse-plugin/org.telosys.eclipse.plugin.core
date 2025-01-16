package org.telosys.eclipse.plugin.core.command;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ProjectExplorerUtil;
import org.telosys.eclipse.plugin.core.commons.ProjectUtil;
import org.telosys.tools.api.TelosysProject;

public class CheckModelHandler extends AbstractCommandHandler {
	
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
        	CheckModelDialogBox dialogBox = new CheckModelDialogBox(getShell(), selectedProject, projectModels, selectedModel );
        	dialogBox.open(); // show dialog box immediately 
        	// nothing else to do, all the work is done in the DialogBox component        	
    	}
    	else {
        	DialogBox.showError("Cannot get project for selected element");
    	}
        return null;
    }
    
    private IFolder getSelectedModel(IResource selectedElement) {
    	if ( selectedElement instanceof IFolder ) {
    		IFolder folder = (IFolder)selectedElement;
    		// Parent level 1
    		IContainer parent1 = folder.getParent();
    		if ( parent1 instanceof IFolder ) {
    			if ( "models".equals(parent1.getName()) ) {
    	    		// Parent level 2
    	    		IContainer parent2 = parent1.getParent();
    	    		if ( parent2 instanceof IFolder ) {
    	    			if ( "TelosysTools".equals(parent2.getName()) ) {
    	    				return folder;
    	    			}
    	    		}
    			}
    		}
    	}
    	return null;
    }
    
    private String[] getProjectModels(IProject project) {
		TelosysProject telosysProject = ProjectUtil.getTelosysProject(project);
		return telosysProject.getModelNames().toArray(new String[0]);
    }
}
