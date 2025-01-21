package org.telosys.eclipse.plugin.core.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ProjectUtil;
import org.telosys.eclipse.plugin.core.commons.TelosysEvolution;
import org.telosys.tools.api.TelosysProject;

public abstract class AbstractCommandHandler extends AbstractHandler {
	
	protected TelosysProject getTelosysProject(IProject project) {
		return new TelosysProject(ProjectUtil.getOSFullPath(project));
	}
	
	protected boolean telosysProjectHasSpecificModelFolder(IProject project) {
    	TelosysProject telosysProject = getTelosysProject(project);
    	return TelosysEvolution.hasSpecificModelFolder(telosysProject);
    }

	protected Shell getShell() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		if (shell == null) {
			DialogBox.showError("Cannot get Shell from workbench!");
		}
		return shell;
	}
	
//	protected IFolder getSelectedModel(IResource selectedElement) {
////    	if ( selectedElement instanceof IFolder ) {
////    		IFolder folder = (IFolder)selectedElement;
////    		// Parent level 1
////    		IContainer parent1 = folder.getParent();
////    		if ( parent1 instanceof IFolder ) {
////    			if ( "models".equals(parent1.getName()) ) {
////    	    		// Parent level 2
////    	    		IContainer parent2 = parent1.getParent();
////    	    		if ( parent2 instanceof IFolder ) {
////    	    			if ( "TelosysTools".equals(parent2.getName()) ) {
////    	    				return folder;
////    	    			}
////    	    		}
////    			}
////    		}
////    	}
////    	return null;
//		if ( isModelFolder(selectedElement) ) {
//			return (IFolder)selectedElement;
//		}
//		return null;
//	}

	private static final String MODEL_YAML   = "model.yaml";
	private static final String TELOSYSTOOLS = "TelosysTools";

	/**
	 * Returns true if the given resource is a Telosys model folder <br>
	 * or is any kind of resource located in a Telosys model folder
	 * @param resource
	 * @return
	 */
	protected boolean isInModelFolder(IResource resource) {
		if ( isModelFolder(resource) ) {
			return true;
		}
		else {
			IContainer parent = resource.getParent();
			if ( parent != null ) {
				return isModelFolder(parent);
			}
		}
		return false;
	}
	/**
	 * Returns true if the given resource can be considered as a Telosys model folder <br>
	 * True if the resource is a folder or project containing a file "model.yaml"
	 * @param resource
	 * @return
	 */
	protected boolean isModelFolder(IResource resource) {
		// The resource can be a IFolder (inside a IProject) or a IProject 
    	if ( resource instanceof IFolder ) {
    		IFolder folder = (IFolder)resource;
    		IFile file = folder.getFile(MODEL_YAML);
    		return file != null && file.exists();
    	}
    	else if ( resource instanceof IProject ) {
    		IProject project = (IProject)resource;
    		IFile file = project.getFile(MODEL_YAML);
    		return file != null && file.exists();
    	}
    	return false;
    }
	protected boolean isTelosysProject(IProject project) {
		if ( project != null ) {
			IFolder telosystoolsFolder = project.getFolder(TELOSYSTOOLS);
			return telosystoolsFolder != null && telosystoolsFolder.exists();
		}
    	return false;
	}
	
	protected String[] getProjectModels(IProject project) {
		TelosysProject telosysProject = ProjectUtil.getTelosysProject(project);
		return telosysProject.getModelNames().toArray(new String[0]);
    }
}
