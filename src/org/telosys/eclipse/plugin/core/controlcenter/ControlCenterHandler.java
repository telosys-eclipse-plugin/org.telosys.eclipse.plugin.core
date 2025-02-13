package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.telosys.eclipse.plugin.core.commons.AbstractMenuHandler;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ProjectExplorerUtil;
import org.telosys.eclipse.plugin.core.commons.WorkbenchUtil;
import org.telosys.eclipse.plugin.core.telosys.TelosysChecker;

public class ControlCenterHandler extends AbstractMenuHandler {

	/**
	 * id of the editor to open (see editor extension point defined in "plugin.xml") 
	 */
	private static final String EDITOR_ID = "org.telosys.eclipse.plugin.core.controlcenter.ControlCenterEditor";
	
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	log("ControlCenterHandler.execute()");
    	// Retrieve the current selected project from current selected elements
    	IProject project = ProjectExplorerUtil.getProjectFromSelection();
        if ( project != null ) {
        	// check project type (in case of bad enable/disable on menu items)
        	if ( TelosysChecker.isTelosysProject(project) ) {
                openOrActiveEditorPage(project);
        	}
        	else {
        		notTelosysProjectMessage();
        	}
        }
        return null;
    }
    
    private void openOrActiveEditorPage(IProject project) {
    	// Create editor input for the selected project
        ControlCenterEditorInput projectEditorInput = new ControlCenterEditorInput(project);
        // Open or activate editor page
        if ( ! activateAlreadyOpenEditor(projectEditorInput) ) {
        	log("ControlCenterHandler.execute() editor NOT already open");
            // Open the editor for the selected project
        	openEditor(projectEditorInput);
        }
        else {
        	log("ControlCenterHandler.execute() editor already open");
        }
    }
    
    /**
     * Search an already open editor for the given project and activate it if found
     * @param projectEditorInput
     * @return
     */
    private boolean activateAlreadyOpenEditor(ControlCenterEditorInput projectEditorInput) {
    	String projectName = projectEditorInput.getName();
    	log("ControlCenterHandler.activateAlreadyOpenEditor() projectEditorInput - projectName = " + projectName );
    	IWorkbenchPage workbenchPage = WorkbenchUtil.getActiveWindowPage();
    	IEditorReference[] editors = workbenchPage.getEditorReferences();
    	for (IEditorReference editorRef : editors) {
        	log("ControlCenterHandler.activateAlreadyOpenEditor() IEditorReference id = " + editorRef.getId() );
    		if ( EDITOR_ID.equals(editorRef.getId() ) ) {
            	log("ControlCenterHandler.activateAlreadyOpenEditor() same ID : " + EDITOR_ID );
            	IEditorInput editorInput = getEditorInput(editorRef);
            	log("ControlCenterHandler.activateAlreadyOpenEditor() IEditorInput : " + editorInput );
            	
            	if ( isSameProject(projectName, editorInput)) {
                	log("ControlCenterHandler.activateAlreadyOpenEditor() same IEditorInput : " + editorInput );
    				// An editor is already open for the given project => activate the page 
    				workbenchPage.activate(editorRef.getEditor(true));
    				return true;
            	}
    		}
    	}
		return false; 
    }

    private boolean isSameProject(String projectName, IEditorInput editorInput) {
    	boolean r = false;
    	log("ControlCenterHandler.isSameProject("+projectName+","+editorInput.getName()+")");    	
    	if (editorInput instanceof ControlCenterEditorInput) {
    		ControlCenterEditorInput controlCenterEditorInput = (ControlCenterEditorInput) editorInput;
    		r = projectName.equals(controlCenterEditorInput.getName());
    	}
		log("ControlCenterHandler.isSameProject() return : " + r );
    	return r;
    }
    
    private IEditorInput getEditorInput(IEditorReference editorRef) {
    	try {
			return editorRef.getEditorInput();
		} catch (PartInitException e) {
			DialogBox.showError("Cannot get IEditorInput from editor", "PartInitException: " + e.getMessage() );
			return null;
		}
    }

    private void openEditor(ControlCenterEditorInput projectEditorInput) {
    	// Open editor 
    	IWorkbenchPage workbenchPage = WorkbenchUtil.getActiveWindowPage();
    	if ( workbenchPage != null ) {
        	try {
				workbenchPage.openEditor(projectEditorInput, EDITOR_ID);
			} catch (PartInitException e) {
				DialogBox.showError("Cannot open editor", "PartInitException: " + e.getMessage() );
			}
    	}
    }
    
}

