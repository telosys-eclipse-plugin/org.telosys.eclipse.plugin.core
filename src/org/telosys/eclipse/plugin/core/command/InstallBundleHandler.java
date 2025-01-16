package org.telosys.eclipse.plugin.core.command;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ProjectExplorerUtil;
import org.telosys.tools.api.InstallationType;
import org.telosys.tools.api.TelosysProject;

public class InstallBundleHandler extends AbstractCommandHandler {
	
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	IProject project = ProjectExplorerUtil.getProjectFromSelection();
        if ( project != null ) {
        	Shell shell = getShell();
        	TelosysProject telosysProject = getTelosysProject(project);
        	String depot = telosysProject.getTelosysToolsCfg().getDepotForBundles(); 
        	InstallDialogBox dialogBox = new InstallDialogBox(shell, project, depot, InstallationType.BUNDLE );
        	dialogBox.open(); // show dialog box immediately 
        	// nothing else to do, all the work is done in the DialogBox component
        }
        else {
        	DialogBox.showError("Cannot get project for selected element");
        }
        return null;
    }
}
