package org.telosys.eclipse.plugin.core.command;

import java.io.File;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.telosys.eclipse.plugin.core.commons.AbstractMenuHandler;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ProjectExplorerUtil;
import org.telosys.tools.api.TelosysProject;

public class NewModelHandler extends AbstractMenuHandler {

//    // When the user opens a menu containing the command, 
//    // isEnabled() is called to determine whether the command should be enabled (clickable) or disabled (grayed out).
//    @Override
//    public boolean isEnabled() {
//    	IProject project = ProjectExplorerUtil.getSelectedTelosysProject(false);
//    	// In a project and the selected project is not a Telosys model/bundle directory
//    	return project != null  ; // &&  !isModelDirectory(project)  &&  !isBundleDirectory(project) ;
//    }

    @Override
    public void setEnabled(Object context) {
    	log("setEnabled(Object context) "  + context.getClass().getCanonicalName() );
//        if (context != null && context instanceof EvaluationContext) {
//            EvaluationContext evaluationContext = (EvaluationContext) context;
//
//            IWorkbenchPart activePart = (IWorkbenchPart) evaluationContext
//                    .getVariable("activePart");
//            ISelection selection = HandlerUtil.getCurrentStructuredSelection(evaluationContext);
//        }
    	IProject project = ProjectExplorerUtil.getSelectedTelosysProject(false);
    	// In a project and the selected project is not a Telosys model/bundle directory
    	setBaseEnabled(project != null); 
    }
    
	
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	IProject project = ProjectExplorerUtil.getSelectedTelosysProject(true);
        if ( project != null ) {
    		NewModelDialogBox dialogBox = new NewModelDialogBox( getShell() );
    		if ( dialogBox.open() == Window.OK ) {
    			String modelName = dialogBox.getModelName();
    			File file = processNewModelCommand(project, modelName);
    			if ( file != null ) {
    				// Model created => refresh and expand
    				ProjectExplorerUtil.reveal(file);
    			}
    		}
        }
        return null;
    }

//    @Override
//    public Object execute(ExecutionEvent event) throws ExecutionException {
//    	IProject project = ProjectExplorerUtil.getProjectFromSelection();
//        if ( project != null ) {
//        	if ( isTelosysProject(project) ) {
//        		NewModelDialogBox dialogBox = new NewModelDialogBox( getShell() );
//        		if ( dialogBox.open() == Window.OK ) {
//        			String modelName = dialogBox.getModelName();
//        			File file = processNewModelCommand(project, modelName);
//        			if ( file != null ) {
//        				// Model created => refresh and expand
//        				ProjectExplorerUtil.reveal(file);
//        			}
//        		}
//        	}
//        	else {
//            	DialogBox.showWarning(" You cannot create a model here.\n\n This project is not a Telosys project.");
//        	}
//        }
//        // else : if multiple selected elements 
//        else {
//        	DialogBox.showError("Cannot get project for selected element");
//        }
//        return null;
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
