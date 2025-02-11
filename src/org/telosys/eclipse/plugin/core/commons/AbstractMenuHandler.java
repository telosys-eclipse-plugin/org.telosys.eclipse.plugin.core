package org.telosys.eclipse.plugin.core.commons;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;
import org.telosys.tools.api.TelosysProject;

public abstract class AbstractMenuHandler extends AbstractHandler {
	
	protected void log(String msg) {
		if ( Config.LOG_FROM_MENU_HANDLER ) {
			System.out.println("[LOG-MenuHandler] - " + msg);
		}
	}

	protected TelosysProject getTelosysProject(IProject project) {
		return new TelosysProject(ProjectUtil.getOSFullPath(project));
	}
	
	protected boolean telosysProjectHasSpecificModelFolder(IProject project) {
    	TelosysProject telosysProject = getTelosysProject(project);
    	return TelosysEvolution.hasSpecificModelFolder(telosysProject);
    }

	protected Shell getShell() {
//		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
//		if (shell == null) {
//			DialogBox.showError("Cannot get Shell from workbench!");
//		}
//		return shell;
		return WorkbenchUtil.getActiveWindowShell();
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

//	/**
//	 * Returns true if the given resource is a Telosys model directory <br>
//	 * or is any kind of resource located in a Telosys model directory
//	 * @param resource
//	 * @return
//	 */
//	protected boolean isInModelDirectory(IResource resource) {
//		if ( isModelDirectory(resource) ) {
//			return true;
//		}
//		else {
//			IContainer parent = resource.getParent();
//			if ( parent != null ) {
//				return isModelDirectory(parent);
//			}
//		}
//		return false;
//	}
//	/**
//	 * Returns true if the given resource can be considered as a Telosys model folder <br>
//	 * True if the resource is a folder or project containing a file "model.yaml"
//	 * @param resource
//	 * @return
//	 */
//	protected boolean isModelDirectory(IResource resource) {
//		// The resource can be a IFolder (inside a IProject) or a IProject 
//    	if ( resource instanceof IFolder ) {
//    		IFolder folder = (IFolder)resource;
//    		IFile file = folder.getFile(Const.MODEL_YAML);
//    		return file != null && file.exists();
//    	}
//    	else if ( resource instanceof IProject ) {
//    		IProject project = (IProject)resource;
//    		IFile file = project.getFile(Const.MODEL_YAML);
//    		return file != null && file.exists();
//    	}
//    	return false;
//    }
//	protected boolean isBundleDirectory(IResource resource) {
//		// The resource can be a IFolder (inside a IProject) or a IProject 
//    	if ( resource instanceof IFolder ) {
//    		IFolder folder = (IFolder)resource;
//    		IFile file = folder.getFile(Const.TEMPLATES_CFG);
//    		return file != null && file.exists();
//    	}
//    	else if ( resource instanceof IProject ) {
//    		IProject project = (IProject)resource;
//    		IFile file = project.getFile(Const.TEMPLATES_CFG);
//    		return file != null && file.exists();
//    	}
//    	return false;
//    }
	

	
	protected String[] getProjectModels(IProject project) {
		TelosysProject telosysProject = ProjectUtil.getTelosysProject(project);
		return telosysProject.getModelNames().toArray(new String[0]);
    }

	protected void notTelosysProjectMessage() {
		DialogBox.showInformation("This project is not a Telosys project.");
	}

}
