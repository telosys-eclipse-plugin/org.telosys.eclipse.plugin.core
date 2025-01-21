package org.telosys.eclipse.plugin.core.command;

import java.io.File;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.window.Window;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ProjectExplorerUtil;
import org.telosys.eclipse.plugin.core.commons.WorkspaceUtil;
import org.telosys.tools.api.TelosysProject;

public class NewEntityHandler extends AbstractCommandHandler {
	
	private String getSelectedProject(IFolder selectedModelFolder) {
    	if ( selectedModelFolder != null ) {
    		// one model is selected
    		return selectedModelFolder.getName();
    	}
    	else {
    		// no model selected
    		return null;
    	}
	}
	private String getSelectedModel(IFolder selectedModelFolder) {
    	if ( selectedModelFolder != null ) {
    		// one model is selected
    		return selectedModelFolder.getName();
    	}
    	else {
    		// no model selected
    		return null;
    	}
	}
	
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
//    	String selectedModel = null;
//    	IProject selectedProject = null;
    	IResource selectedResource = ProjectExplorerUtil.getSingleSelectedResource();
    	if ( selectedResource != null ) {
	    	if ( isInModelFolder(selectedResource) ) {
	    		IContainer modelContainer = selectedResource instanceof IContainer ? (IContainer)selectedResource : selectedResource.getParent();
	    		newEntityFromModelFolder(modelContainer);
	    	}
	    	else {
	    		newEntityFromProjectFolder(selectedResource);
	    	}
    	}
//    	if ( selectedResource != null ) {
//        	IFolder selectedModelFolder = getSelectedModel(selectedResource);
//        	if ( selectedModelFolder != null ) {
//        		// one model is selected
//        		selectedModel = selectedModelFolder.getName();
//        		selectedProject = selectedModelFolder.getProject();
//        	}
//        	else {
//        		// no model selected
//        		selectedModel = null;
//        		selectedProject = selectedResource.getProject();
//        	}
//        	String[] projectModels = getProjectModels(selectedProject);
//        	// Open dialog box
//        	NewEntityDialogBox dialogBox = new NewEntityDialogBox(getShell(), selectedProject, projectModels, selectedModel );
//    		if ( dialogBox.open() == Window.OK ) {
//    			String modelName = dialogBox.getModelName();
//    			String entityName = dialogBox.getEntityName();
//    			File file = executeNewEntityCommand(selectedProject, modelName, entityName);
//    			if ( file != null ) {
//    				// File created => reveal in Project Explorer
//                	ProjectExplorerUtil.reveal(file);
//    			}
//            }
//    	}
//    	else {
//        	DialogBox.showError("Cannot get project for selected element");
//    	}
        return null;
    }

    private void newEntityFromModelFolder(IContainer modelFolder) {
    	//IContainer container = (IContainer)selectedResource;
    	File osDirFile = WorkspaceUtil.getFileFromResource(modelFolder);
    	if ( osDirFile != null ) {
    		String modelName = osDirFile.getName();
        	NewEntityFromModelDialogBox dialogBox = new NewEntityFromModelDialogBox(getShell(), modelName);
    		if ( dialogBox.open() == Window.OK ) {
    			String entityName = dialogBox.getEntityName();
    	    	DialogBox.showInformation("TODO", "Create entity '"+ entityName + "' in model '"+ modelName + "'");
//    			File file = executeNewEntityCommand(selectedProject, modelName, entityName);
//    			if ( file != null ) {
//    				// File created => reveal in Project Explorer
//                	ProjectExplorerUtil.reveal(file);
//    			}
            }
    	}
    	else {
    		DialogBox.showError("Cannot get OS file from model folder \n" + modelFolder.getFullPath());
    	}
    }
    private void newEntityFromProjectFolder(IResource selectedResource) {
//    	DialogBox.showInformation("newEntityFromProjectFolder("+ selectedResource.getName() + ")");    	
		IProject selectedProject = selectedResource.getProject();
    	String[] projectModels = getProjectModels(selectedProject);
    	// Open dialog box
    	NewEntityFromProjectDialogBox dialogBox = new NewEntityFromProjectDialogBox(getShell(), selectedProject, projectModels );
		if ( dialogBox.open() == Window.OK ) {
			String modelName = dialogBox.getModelName();
			String entityName = dialogBox.getEntityName();
			File file = executeNewEntityCommand(selectedProject, modelName, entityName);
			if ( file != null ) {
				// File created => reveal in Project Explorer
            	ProjectExplorerUtil.reveal(file);
			}
        }
    }

    
    private File executeNewEntityCommand(IProject project, String modelName, String entityName) {
//    	DialogBox.showInformation("executeNewEntityCommand()", "Create entity '" + entityName + "' in model '" + modelName+"'");
    	TelosysProject telosysProject = getTelosysProject(project);
    	try {
    		return telosysProject.createNewDslEntity(modelName, entityName);
		} catch (Exception e) {
    		DialogBox.showError("Cannot create entity '" + entityName + "'", 
    				"Exception: \n" + e.getClass().getCanonicalName() + "\n" + e.getMessage());
    		return null;
		}
    }
}
