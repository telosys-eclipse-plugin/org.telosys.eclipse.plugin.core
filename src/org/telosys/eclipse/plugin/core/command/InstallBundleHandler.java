package org.telosys.eclipse.plugin.core.command;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.telosys.eclipse.plugin.core.commons.AbstractMenuHandler;
import org.telosys.eclipse.plugin.core.commons.ProjectExplorerUtil;
import org.telosys.eclipse.plugin.core.commons.dialogbox.InstallDialogBox;
import org.telosys.eclipse.plugin.core.telosys.TelosysChecker;
import org.telosys.tools.api.InstallationType;

public class InstallBundleHandler extends AbstractMenuHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	IProject project = ProjectExplorerUtil.getSingleSelectedProject(true);
        if ( project != null ) {
        	if ( TelosysChecker.isTelosysProject(project) ) {
        		// Open the Dialog Box
            	InstallDialogBox dialogBox = new InstallDialogBox(getTelosysProject(project), InstallationType.BUNDLE );
            	dialogBox.open(); // show dialog box immediately 
        	}
        	else {
        		notTelosysProjectMessage();
        	}
        }
        return null;
    }
}
