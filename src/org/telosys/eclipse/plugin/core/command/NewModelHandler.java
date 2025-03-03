package org.telosys.eclipse.plugin.core.command;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IProject;
import org.telosys.eclipse.plugin.core.commons.AbstractMenuHandler;
import org.telosys.eclipse.plugin.core.commons.ProjectExplorerUtil;
import org.telosys.eclipse.plugin.core.telosys.TelosysCommand;
import org.telosys.tools.api.TelosysProject;

public class NewModelHandler extends AbstractMenuHandler {

    @Override
    public Object execute(ExecutionEvent event)  {
    	IProject project = ProjectExplorerUtil.getSelectedTelosysProject(true);
        if ( project != null ) {
//    		NewModelDialogBox dialogBox = new NewModelDialogBox( getShell() );
//    		if ( dialogBox.open() == Window.OK ) {
//    			String modelName = dialogBox.getModelName();
//    			File file = processNewModelCommand(project, modelName);
//    			if ( file != null ) {
//    				// Model created => refresh and expand
//    				ProjectExplorerUtil.reveal(file);
//    			}
//    		}
            TelosysProject telosysProject = getTelosysProject(project);
            TelosysCommand.newModel(telosysProject);
        }
        return null;
        
    }


//    private File processNewModelCommand(IProject project, String modelName) {
//    	TelosysProject telosysProject = getTelosysProject(project);
//    	if ( telosysProject.modelFolderExists(modelName) ) {
//    		DialogBox.showWarning("Model '" + modelName + "' already exists");
//    		return null;
//    	}
//    	else {
//    		return telosysProject.createNewDslModel(modelName);
//    	}
//    }
}
