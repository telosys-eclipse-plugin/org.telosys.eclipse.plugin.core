package org.telosys.eclipse.plugin.core.commons;

import java.io.File;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.navigator.CommonViewer;

public class WorkspaceUtil {
	
	private static void showError(String title, String message) {
		if ( Config.SHOW_ERROR  ) {
			DialogBox.showError(title, message);
		}
	}

//	/**
//	 * Returns the Eclipse workspace (set of resources : projects, files, ...) Based
//	 * on "ResourcesPlugin.getWorkspace()"
//	 * 
//	 * @return the workspace instance.
//	 */
//	public static IWorkspace getWorkspace() {
//		return ResourcesPlugin.getWorkspace();
//	}

	/**
	 * Returns the Eclipse workspace root
	 * @return 
	 */
	public static IWorkspaceRoot getWorkspaceRoot() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		if ( workspace != null ) {
			IWorkspaceRoot workspaceRoot = workspace.getRoot();
			if ( workspaceRoot != null ) {
				return workspaceRoot;
			}
			else {
				showError("getWorkspaceRoot()", "Cannot get workspace root");
			}
		}
		else {
			showError("getWorkspaceRoot()", "Cannot get workspace");
		}
		return null;
	}

	
	/**
	 * NB: works only after refresh 
	 * @param file
	 * @return
	 */
	public static IResource findResource(File file) {
		if (file != null && file.exists() ) {
			// Find the corresponding resource in workspace
			IWorkspaceRoot workspaceRoot = getWorkspaceRoot();
			if (workspaceRoot != null) {
				// Convert the file's absolute path to an IPath
				IPath location   = Path.fromOSString(file.getAbsolutePath());
				// find the resource by path 
				// including resources that are in a phantom state 
				// (e.g., those that may not have been fully synchronized or refreshed yet).
				//return workspaceRoot.findMember(location , true); // includePhantoms = true
				return workspaceRoot.findMember(location);
			}
		}
		return null;
	}
	
	public static IPath getIPath(File file) {
		if (file != null && file.exists() ) {
			// Convert the File path to an IPath
			return Path.fromOSString(file.getAbsolutePath());
		}
		return null;
	}

//	/**
//	 * Returns a IFile for the given File
//	 * @param file
//	 * @return
//	 */
//	public static IFile getIFile(File file) {
//		IPath osAbsolutePath = getIPath(file);
//		if ( osAbsolutePath != null ) {
//			IWorkspaceRoot workspaceRoot = getWorkspaceRoot();
//			if (workspaceRoot != null) {
//				return workspaceRoot.getFileForLocation(osAbsolutePath);
//			}
//		}
//		return null;
//	}
		
	/**
	 * Returns a IContainer (IProject or IFolder) for the given File
	 * @param file
	 * @return
	 */
	public static IContainer getIContainer(File file) {
		IPath osAbsolutePath = getIPath(file);
		if ( osAbsolutePath != null ) {
			IWorkspaceRoot workspaceRoot = getWorkspaceRoot();
			if (workspaceRoot != null) {
				return workspaceRoot.getContainerForLocation(osAbsolutePath);
			}
		}
		return null; // The file is not in the workspace
	}

	/**
	 * Returns a IProject for the given File
	 * @param file
	 * @return
	 */
	public static IFolder getIFolder(File file) {
		IContainer container = getIContainer(file);
		// Check if the resource is an IFolder
		if (container != null && container.getType() == IResource.FOLDER) {
			return (IFolder) container;
		}
		return null;
	}
	
	public static void refresh(IResource resource) {
		refresh(resource, IResource.DEPTH_INFINITE);
	}
	
	public static void refresh(IResource resource, int depth) {
        try {
        	// Refreshes the resource hierarchy from this resource and its children (to the specified depth) 
        	resource.refreshLocal(depth, null);
        } catch (CoreException e) {
            // Handle the exception appropriately
        	DialogBox.showError("Cannot refresh resource '" + resource.getName() + "' \n"
        			+ "Exception: " + e.getClass().getCanonicalName() + "\n"
        			+ "Message: "  + e.getMessage() );
        }
    }
	
    public static void refresh(File file) {
    	IResource resource = findResource(file); 
    	if ( resource != null ) {
    		// Container found => refresh it 
    		refresh(resource);
    	}
    	// else resource not found : not an error if Model/Bundle outside the workspace
    }
    


}
