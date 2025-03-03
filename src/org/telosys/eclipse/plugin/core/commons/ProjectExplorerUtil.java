package org.telosys.eclipse.plugin.core.commons;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.telosys.eclipse.plugin.commons.DialogBox;
import org.telosys.eclipse.plugin.commons.WorkbenchUtil;
import org.telosys.eclipse.plugin.commons.WorkspaceUtil;
import org.telosys.eclipse.plugin.core.telosys.TelosysChecker;

public class ProjectExplorerUtil {
	

	private static void showError(String title, String message) {
		if ( Config.SHOW_ERROR ) {
			DialogBox.showError(title, message);
		}
	}

	private static IViewPart getProjectExplorerViewPart() { // IViewPart extends IWorkbenchPart, IPersistable
        // Find the Project Explorer view
		IWorkbenchWindow activeWorkbenchWindow = WorkbenchUtil.getActiveWindow();
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
	public static IProject getSingleSelectedProject(boolean showMessageIfNoProject) {
		IProject project = getProjectFromSingleSelectedElement();
		if ( project == null && showMessageIfNoProject ) {
			DialogBox.showInformation("Select one and only one project please.");
		}
		return project;
	}
	
	public static IFolder getSingleSelectedFolder() {
        Object selectedElement = ProjectExplorerUtil.getSingleSelectedElement();
        if ( selectedElement instanceof IFolder ) { // not null and instance of IProject
        	return (IFolder)selectedElement;
        }
		return null;
	}

	public static Set<IProject> getSelectedProjects(IStructuredSelection selection) {
		Set<IProject> projects = new HashSet<>();
	    if (selection == null || selection.isEmpty()) {
	        return projects; // Return empty list if no selection
	    }
	    for ( Object element : selection ) {
	        IProject project = getProjectFromElement(element);
	        if ( project != null ) {
	            projects.add(project); // HashSet avoid duplicates
	        }
	    }
	    return projects;
	}	
	public static IProject getSelectedTelosysProject(boolean verbose) {
		Set<IProject> projects = getSelectedProjects(getStructuredSelection());
		if (projects.size() == 1) {
			// Single project found
			IProject project = projects.iterator().next(); // First (single) element
			if ( TelosysChecker.isTelosysProject(project) ) {
				return project;
			}
			else {
				if ( verbose ) DialogBox.showInformation("The selected project is not a Telosys project.");
			}
		}
		else if ( projects.size() > 1) {
			// Many projects found
			if ( verbose ) DialogBox.showInformation("Select only one project please.");
		}
		return null;
	}
//	private static boolean isTelosysProject(IProject project) {
//		if ( project != null ) {
//			IFolder telosystoolsFolder = project.getFolder(Const.TELOSYSTOOLS);
//			return telosystoolsFolder != null && telosystoolsFolder.exists();
//		}
//    	return false;
//	}
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
		if ( element == null ) {
			showError("getProjectFromElement", "element parameter is null");
			return null;
		}
        if ( element instanceof IProject ) { 
        	return (IProject)element;
        }
        else if ( element instanceof IResource ) {
        	// Returns the project which contains this resource.
        	return ((IResource) element).getProject();
        }
        else {
           	showError("getProjectFromElement()", "Unexpected type for project element : " + element.getClass().getCanonicalName());
            return null;
        }
	}

	/**
	 * Expands all ancestors of the given folder so that it becomes visible in this viewer's tree control, 
	 * and then expands all the subtree rooted at the given folder (full expansion)
	 * @param container
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
        	showError("expand(IContainer container)", "container is null");
		}
	}
	
    public static void reveal(File file) {
    	if ( file != null ) {
    		if ( file.exists()) {
    			File folder = null;
    			if ( file.isDirectory() ) {
    				// eg a model/bundle directory
    				folder = file;
    			}
    			else if ( file.isFile() ) {
    				// eg an entity file
    				folder = file.getParentFile();
    			}
    			if ( folder != null && folder.isDirectory() ) {
        			// NB: findResource cannot be used because the Workspace as not yet been refreshed
        	    	IContainer container = WorkspaceUtil.getIContainer(folder);
        	    	if ( container != null ) {
        	    		WorkspaceUtil.refresh(container);
        	    		expand(container);
        	    	}
        	    	// else: not an error (a model or a bundle can be located outside the project)
//        	    	else {
//                		showError("reveal(File file)", "folder to reveal not found in workspace \n" + folder.getAbsolutePath() );
//        	    	}
    			}
    		}
    		else {
        		showError("reveal(File file)", "file doesn't exist");
    		}
    	}
    	else {
    		showError("reveal(File file)", "file is null");
    	}
    }
    
	
}
