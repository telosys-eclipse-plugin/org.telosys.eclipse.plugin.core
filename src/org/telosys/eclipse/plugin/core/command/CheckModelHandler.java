package org.telosys.eclipse.plugin.core.command;

import java.io.File;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.telosys.eclipse.plugin.core.commons.AbstractMenuHandler;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ProjectExplorerUtil;
import org.telosys.eclipse.plugin.core.commons.WorkspaceUtil;
import org.telosys.eclipse.plugin.core.commons.dialogbox.CheckModelFromModelDialogBox;
import org.telosys.eclipse.plugin.core.commons.dialogbox.CheckModelFromProjectDialogBox;
import org.telosys.eclipse.plugin.core.telosys.ModelCheckStatus;
import org.telosys.eclipse.plugin.core.telosys.TelosysEvolution;
import org.telosys.eclipse.plugin.core.telosys.TelosysChecker;

public class CheckModelHandler extends AbstractMenuHandler {
	
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	IResource selectedResource = ProjectExplorerUtil.getSingleSelectedResource();
    	if ( selectedResource != null ) {
	    	if ( TelosysChecker.isInModelDirectory(selectedResource) ) {
	    		IContainer modelContainer = selectedResource instanceof IContainer ? (IContainer)selectedResource : selectedResource.getParent();
	    		checkModelFromModelFolder(modelContainer);
	    	}
	    	else {
	    		checkModelFromProjectFolder(selectedResource);
	    	}
    	}
    	else {
        	DialogBox.showError("Cannot get project for selected element");
    	}
        return null;
        
    }
    
    private void checkModelFromProjectFolder(IResource selectedResource) {
		IProject selectedProject = selectedResource.getProject();
    	String[] projectModels = getProjectModels(selectedProject);
    	// Open dialog box to choose a model and check it 
    	CheckModelFromProjectDialogBox dialogBox = new CheckModelFromProjectDialogBox(selectedProject, projectModels );
    	dialogBox.open(); // show dialog box immediately 
    	// nothing else to do, all the work is done in the DialogBox component        	
    }
    
    private void checkModelFromModelFolder(IContainer modelFolder) {
    	File osDirFile = WorkspaceUtil.getFileFromResource(modelFolder);
    	if ( osDirFile != null ) {
    		//--- Check the model
    		ModelCheckStatus modelCheckStatus = TelosysEvolution.checkModel(osDirFile);
    		//--- Show the result
        	CheckModelFromModelDialogBox dialogBox = new CheckModelFromModelDialogBox(osDirFile, modelCheckStatus.getFullReport());
        	dialogBox.open(); // show dialog box to print the result 
        	// nothing else to do (all the work is done)
    	}
    	else {
    		DialogBox.showError("Cannot get OS file from model folder \n" + modelFolder.getFullPath());
    	}
    }
}
