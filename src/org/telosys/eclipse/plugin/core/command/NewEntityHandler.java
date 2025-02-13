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
import org.telosys.eclipse.plugin.core.commons.dialogbox.NewEntityFromModelDialogBox;
import org.telosys.eclipse.plugin.core.commons.dialogbox.NewEntityFromProjectDialogBox;
import org.telosys.eclipse.plugin.core.telosys.TelosysChecker;

public class NewEntityHandler extends AbstractMenuHandler {
	
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	IResource selectedResource = ProjectExplorerUtil.getSingleSelectedResource();
    	if ( selectedResource != null ) {
	    	if ( TelosysChecker.isInModelDirectory(selectedResource) ) {
	    		IContainer modelContainer = selectedResource instanceof IContainer ? (IContainer)selectedResource : selectedResource.getParent();
	    		newEntityFromModelFolder(modelContainer);
	    	}
	    	else {
	    		newEntityFromProjectFolder(selectedResource);
	    	}
    	}
        return null;
    }

    private void newEntityFromModelFolder(IContainer modelFolder) {
    	File osDirFile = WorkspaceUtil.getFileFromResource(modelFolder);
    	if ( osDirFile != null ) {
    		String modelName = osDirFile.getName();
    		IProject project = modelFolder.getProject();
        	// Open dialog box
        	NewEntityFromModelDialogBox dialogBox = new NewEntityFromModelDialogBox(getTelosysProject(project), modelName);
        	dialogBox.open(); // All the work is done in Dialog Box
    	}
    	else {
    		DialogBox.showError("Cannot get OS file from model folder \n" + modelFolder.getFullPath());
    	}
    }
    
    private void newEntityFromProjectFolder(IResource selectedResource) {
		IProject project = selectedResource.getProject();
    	String[] projectModels = getProjectModels(project);
    	// Open dialog box
    	NewEntityFromProjectDialogBox dialogBox = new NewEntityFromProjectDialogBox(getTelosysProject(project), project.getName(), projectModels );
    	dialogBox.open(); // All the work is done in Dialog Box
    }

}
