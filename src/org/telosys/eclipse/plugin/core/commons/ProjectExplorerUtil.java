package org.telosys.eclipse.plugin.core.commons;

import java.io.File;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;

public class ProjectExplorerUtil {
	
	private static void showError(String title, String message) {
		if ( Config.SHOW_ERROR ) {
			DialogBox.showError(title, message);
		}
	}

	private static IViewPart getProjectExplorerViewPart() { // IViewPart extends IWorkbenchPart, IPersistable
        // Find the Project Explorer view
		IWorkbenchWindow activeWorkbenchWindow =  PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if ( activeWorkbenchWindow != null ) {
	        // Find the Project Explorer view
			IViewPart viewPart = activeWorkbenchWindow.getActivePage().findView("org.eclipse.ui.navigator.ProjectExplorer");
			if (viewPart != null) {
				return viewPart; 
			}
			else {
				// Not supposed to happen
	        	showError("getViewPart()", "Cannot find 'ProjectExplorer' view");
			}
		}
		else {
			// Not supposed to happen
        	showError("getViewPart()", "Cannot get active workbench window");
		}
        return null;
	}
	
	private static CommonNavigator getProjectExplorerViewPartAsCommonNavigator() {
		IViewPart part = getProjectExplorerViewPart();
        if (part instanceof CommonNavigator) {
            return (CommonNavigator) part;
        } else {
			// Not supposed to happen
        	// showError at lower level 
        	return null;
        }
	}

	private static CommonViewer getProjectExplorerViewPartAsCommonViewer () {
		CommonNavigator commonNavigator = getProjectExplorerViewPartAsCommonNavigator();
        if (commonNavigator != null) {
            return commonNavigator.getCommonViewer();
        } else {
			// Not supposed to happen
        	// showError at lower level 
        	return null;
        }
	}
	
	public static ISelection getCurrentSelection() {
		CommonViewer commonViewer = getProjectExplorerViewPartAsCommonViewer();
        if (commonViewer != null) {
            return commonViewer.getSelection();
        } else {
        	// Not supposed to happen
        	// showError at lower level 
            return null;
        }    
	}
	public static IStructuredSelection getStructuredSelection() {
		ISelection selection = getCurrentSelection();
        if (selection != null ) {
        	if ( selection instanceof IStructuredSelection) {
        		return (IStructuredSelection) selection;
        	}
        	else {
            	// Not supposed to happen
            	showError("getStructuredSelection()", "Project Explorer selection is not an instance of 'IStructuredSelection'.");
        	}
        }
        else {
        	// Not supposed to happen
        	// showError at lower level 
        }
    	return null;
	}

	public static Object getFirstSelectedElement() {
		IStructuredSelection structuredSelection = getStructuredSelection(); 
		if ( structuredSelection != null ) {
			return structuredSelection.getFirstElement();
		}
		return null;
	}

	public static Object getSingleSelectedElement() {
		IStructuredSelection structuredSelection = getStructuredSelection(); 
		// Only 1 element selected 
		if ( structuredSelection != null && structuredSelection.size() == 1 ) {
			return structuredSelection.getFirstElement();
		}
		return null;
	}

	public static IResource getSingleSelectedResource() {
        Object selectedElement = ProjectExplorerUtil.getSingleSelectedElement();
        if ( selectedElement instanceof IResource ) { // not null and instance of IProject
        	return (IResource)selectedElement;
        }
		return null;
	}
	public static IProject getSingleSelectedProject() {
        Object selectedElement = ProjectExplorerUtil.getSingleSelectedElement();
        if ( selectedElement instanceof IProject ) { // not null and instance of IProject
        	return (IProject)selectedElement;
        }
		return null;
	}
	public static IFolder getSingleSelectedFolder() {
        Object selectedElement = ProjectExplorerUtil.getSingleSelectedElement();
        if ( selectedElement instanceof IFolder ) { // not null and instance of IProject
        	return (IFolder)selectedElement;
        }
		return null;
	}


	/**
	 * Returns the current project for current selected element(s) (one or many)
	 * @return
	 */
	public static IProject getProjectFromSelection() {
		// Use the first one 
        return getProjectFromElement( ProjectExplorerUtil.getFirstSelectedElement() );
	}
	
	/**
	 * Returns the current project for the current unique selected element
	 * @return
	 */
	public static IProject getProjectFromSingleSelectedElement() {
        return getProjectFromElement( ProjectExplorerUtil.getSingleSelectedElement() );
	}

	private static IProject getProjectFromElement(Object element) {
        if ( element instanceof IProject ) { // not null and instance of IProject
        	return (IProject)element;
        }
        else if ( element instanceof IResource ) {
        	IResource resource = (IResource)element;
        	return resource.getProject(); // Returns the project which contains this resource.
        }
        else {
        	if ( element != null ) {
            	showError("getProjectFromElement()", "Unexpected type for project element : " + element.getClass().getCanonicalName());
        	}
            return null; 
        }
	}

	/**
	 * Expands all ancestors of the given folder so that it becomes visible in this viewer's tree control, 
	 * and then expands all the subtree rooted at the given folder (full expansion)
	 * @param folder
	 */
	public static void expand(IContainer container) {
		expand(container, Integer.MAX_VALUE); // Integer.MAX_VALUE => full expansion
	}
	public static void expand(IContainer container, int level) {
		if ( container != null ) {
			CommonViewer commonViewer = getProjectExplorerViewPartAsCommonViewer();
			commonViewer.expandToLevel(container, level); // Expand to one level (use Integer.MAX_VALUE for full expansion)
		}
		else {
        	showError("expandFolder(IFolder folder)", "folder is null");
		}
	}
//	/**
//	 * Expands all ancestors of the given folder so that it becomes visible in this viewer's tree control, 
//	 * and then expands the subtree rooted at the given folder to the given level.
//	 * @param folder
//	 * @param level
//	 */
//	public static void expandFolder(IFolder folder, int level) {
//		if ( folder != null ) {
//			CommonViewer commonViewer = getProjectExplorerViewPartAsCommonViewer();
//	        if (commonViewer != null) {
//	            // Expand the folder in the viewer
//	        	commonViewer.expandToLevel(folder, level); // Expand to one level (use Integer.MAX_VALUE for full expansion)
//	        }
//		}
//		else {
//        	showError("expandFolder(IFolder folder, int level)", "folder is null");
//		}
//	}
	
    public static void reveal(File file) {
    	if ( file != null ) {
    		if ( file.exists()) {
    			File folder = null;
    			if ( file.isDirectory() ) {
    				// eg a model directory
    				folder = file;
    			}
    			else if ( file.isFile() ) {
    				// eg an entity file
    				folder = file.getParentFile();
    			}
    			if ( folder != null && folder.isDirectory() ) {
        	    	// IResource resource = WorkspaceUtil.findResource(parent);
        			// findResource cannot be used because the Workspace as not yet been refreshed
        	    	IContainer container = WorkspaceUtil.getIContainer(folder);
        	    	if ( container != null ) {
//        	    		if ( resource instanceof IContainer ) {
//            	    		// Resource found => refresh and expand parent
//            	    		IContainer container = (IContainer) resource;
            	    		WorkspaceUtil.refresh(container);
            	    		expand(container);
//        	    		}
        	    	}
        	    	else {
                		showError("reveal(File file)", "folder to reveal not found in workspace \n" + folder.getAbsolutePath() );
        	    	}
    			}
    		}
    		else {
        		showError("reveal(File file)", "file doesn't exist");
    		}
    	}
    	else {
    		showError("reveal(File file)", "file is null");
    	}
    	// else resource not found : not an error if Model/Bundle is outside the workspace
    }
    
	
}
