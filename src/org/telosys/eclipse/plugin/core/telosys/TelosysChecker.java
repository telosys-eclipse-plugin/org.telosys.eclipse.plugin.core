package org.telosys.eclipse.plugin.core.telosys;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.telosys.eclipse.plugin.core.commons.Const;

public class TelosysChecker {

	/**
	 * Returns true if the given project can be considered as a Telosys project 
	 * @param project
	 * @return
	 */
	public static boolean isTelosysProject(IProject project) {
		if ( project != null ) {
			IFolder telosystoolsFolder = project.getFolder(Const.TELOSYSTOOLS);
			return telosystoolsFolder != null && telosystoolsFolder.exists();
		}
    	return false;
	}
	
	/**
	 * Returns true if the given resource can be considered as a Telosys model folder <br>
	 * True if the resource is a folder or project containing a file "model.yaml"
	 * @param resource
	 * @return
	 */
	public static boolean isModelDirectory(IResource resource) {
		// The resource can be a IFolder (inside a IProject) or a IProject 
    	if ( resource instanceof IFolder ) {
    		IFolder folder = (IFolder)resource;
    		IFile file = folder.getFile(Const.MODEL_YAML);
    		return file != null && file.exists();
    	}
    	else if ( resource instanceof IProject ) {
    		IProject project = (IProject)resource;
    		IFile file = project.getFile(Const.MODEL_YAML);
    		return file != null && file.exists();
    	}
    	return false;
    }
	
	/**
	 * Returns true if the given resource is a Telosys model directory <br>
	 * or is any kind of resource located in a Telosys model directory
	 * @param resource
	 * @return
	 */
	public static boolean isInModelDirectory(IResource resource) {
		if ( isModelDirectory(resource) ) {
			return true;
		}
		else {
			IContainer parent = resource.getParent();
			if ( parent != null ) {
				return isModelDirectory(parent);
			}
		}
		return false;
	}

	public static boolean isBundleDirectory(IResource resource) {
		// The resource can be a IFolder (inside a IProject) or a IProject 
    	if ( resource instanceof IFolder ) {
    		IFolder folder = (IFolder)resource;
    		IFile file = folder.getFile(Const.TEMPLATES_CFG);
    		return file != null && file.exists();
    	}
    	else if ( resource instanceof IProject ) {
    		IProject project = (IProject)resource;
    		IFile file = project.getFile(Const.TEMPLATES_CFG);
    		return file != null && file.exists();
    	}
    	return false;
    }
}
