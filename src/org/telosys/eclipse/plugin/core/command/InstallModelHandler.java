package org.telosys.eclipse.plugin.core.command;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ProjectExplorerUtil;
import org.telosys.tools.api.InstallationType;
import org.telosys.tools.api.TelosysProject;

public class InstallModelHandler extends AbstractCommandHandler {
	
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	IProject project = ProjectExplorerUtil.getProjectFromSelection();
        if ( project != null ) {
        	Shell shell = getShell();
        	TelosysProject telosysProject = getTelosysProject(project);
        	String depot = telosysProject.getTelosysToolsCfg().getDepotForModels(); 
        	InstallDialogBox dialogBox = new InstallDialogBox(shell, project, depot, InstallationType.MODEL );
        	dialogBox.open(); // show dialog box immediately 
        	
        	// TOD MOVE 
        	// runTaskWithProgressMonitor(new GetDepotElementsTaskWithProgress(), shell);
        	
//        	dialogBox.initWithProgressMonitor(new GetDepotElementsTaskWithProgress()) 
//            // Run the Progress Task to fetch data
//        	GetDepotElementsTaskWithProgress task = new GetDepotElementsTaskWithProgress();
//        	task.run();
        	
//    		if ( dialogBox.open() == Window.OK ) {
//    			String modelName = dialogBox.getModelName();
//    			File file = execCommand(project, modelName);
//    			if ( file != null ) {
//    				// Model created => refresh and expand
//    	            ProjectUtil.refresh(project);
//    	            ProjectExplorerUtil.expandFolder(WorkspaceUtil.getIFolder(file));
//    			}
//    		}
        }
        // else : if multiple selected elements 
        else {
        	DialogBox.showError("Cannot get project for selected element");
        }
        return null;
    }

    
//    private void execCommand(IProject project) {
//    }
//
//    private void runTaskWithProgressMonitor(IRunnableWithProgress task, Shell shell) {
//		ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(shell) ;
//		try {
//			progressMonitorDialog.run(false, false, task);
//		} catch (InvocationTargetException e) {
//			DialogBox.showError("Error during task", e.getMessage());
//		} catch (InterruptedException e) {
//			DialogBox.showInformation("Task interrupted");
//		}
//    }

}
