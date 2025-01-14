package org.telosys.eclipse.plugin.core.commons;

import java.io.File;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class WorkspaceUtil {

	/**
	 * Returns the Eclipse workspace (set of resources : projects, files, ...) Based
	 * on "ResourcesPlugin.getWorkspace()"
	 * 
	 * @return the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	/**
	 * Returns the Eclipse workspace root
	 * 
	 * @return the current workspace root
	 */
	public static IWorkspaceRoot getWorkspaceRoot() {
		return getWorkspace().getRoot();
	}

	/**
	 * Converts the given filesystem "File" to an Eclipse workspace "IFile" object
	 * <br>
	 * Returns null if the given file is not under the location of the workspace
	 * 
	 * @param file
	 * @return
	 */
	public static IFile getIFileFromFile_OLD(File file) {

		IWorkspaceRoot root = getWorkspaceRoot();
		if (root != null) {
			String sAbsolutePath = file.getAbsolutePath();
			IPath path = new Path(sAbsolutePath);
			/*
			 * getFileForLocation(path) : The path should be absolute; a relative path will
			 * be treated as absolute. The path segments need not be valid names. The
			 * resulting file need not exist in the workspace. This method returns null when
			 * the given file system location is not under the location of any existing
			 * project in the workspace.
			 */
			IFile iFile = root.getFileForLocation(path);
			if (iFile != null) {
				return iFile;
			}
		}
		return null;
	}

//	public static IResource findResource(File file) {
//		if (file != null && file.exists() ) {
//			// Convert the File path to an IPath
//			IPath osAbsolutePath  = Path.fromOSString(file.getAbsolutePath());
//			// Find the corresponding resource in workspace
//			IWorkspaceRoot workspaceRoot = getWorkspaceRoot();
//			if (workspaceRoot != null) {
//				// find the resource by path 
//				// including resources that are in a phantom state 
//				// (e.g., those that may not have been fully synchronized or refreshed yet).
//				return workspaceRoot.findMember(path, true); // includePhantoms = true
//			}
//		}
//		return null;
//	}
	
	public static IPath getIPath(File file) {
		if (file != null && file.exists() ) {
			// Convert the File path to an IPath
			return Path.fromOSString(file.getAbsolutePath());
		}
		return null;
	}

	/**
	 * Returns a IFile for the given File
	 * @param file
	 * @return
	 */
	public static IFile getIFile(File file) {
		IPath osAbsolutePath = getIPath(file);
		if ( osAbsolutePath != null ) {
			IWorkspaceRoot workspaceRoot = getWorkspaceRoot();
			if (workspaceRoot != null) {
				return workspaceRoot.getFileForLocation(osAbsolutePath);
			}
		}
		return null;
	}
		
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
		return null;
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
}
