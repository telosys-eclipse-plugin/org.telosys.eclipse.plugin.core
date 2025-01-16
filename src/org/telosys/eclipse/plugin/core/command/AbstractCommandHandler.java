package org.telosys.eclipse.plugin.core.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ProjectUtil;
import org.telosys.tools.api.TelosysProject;

public abstract class AbstractCommandHandler extends AbstractHandler {
	
	protected TelosysProject getTelosysProject(IProject project) {
		return new TelosysProject(ProjectUtil.getOSFullPath(project));
	}

	protected Shell getShell() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		if (shell == null) {
			DialogBox.showError("Cannot get Shell from workbench!");
		}
		return shell;
	}
	
	protected IFolder getSelectedModel(IResource selectedElement) {
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
	
	protected String[] getProjectModels(IProject project) {
		TelosysProject telosysProject = ProjectUtil.getTelosysProject(project);
		return telosysProject.getModelNames().toArray(new String[0]);
    }
}
