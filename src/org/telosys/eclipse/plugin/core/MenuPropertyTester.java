package org.telosys.eclipse.plugin.core;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.telosys.eclipse.plugin.core.commons.ProjectExplorerUtil;

public class MenuPropertyTester extends PropertyTester {
	
	public static final String MODEL_COMMAND_PROPERTY = "modelCommandsEnabled";
	
    public MenuPropertyTester() {
		super();
        System.out.println("MenuPropertyTester.test(): constructor ");
	}

	@Override
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
    	
        System.out.println("MenuPropertyTester.test(): property = " + property);
    	if ( MODEL_COMMAND_PROPERTY.equals(property) ) return testModelCommand();
//        if (SELECTED_FOLDER_PROPERTY.equals(property)) {
//            if (receiver instanceof ISelection) {
//                ISelection selection = (ISelection) receiver;
//                IResource selectedResource = getSelectedResource(selection);
//                if (selectedResource instanceof IFolder) {
//                    IFolder folder = (IFolder) selectedResource;
//                    IPath folderPath = folder.getFullPath();
//                    return "foo/bar".equals(folderPath.lastSegment());
//                }
//            }
//        }
        return false;
    }
    
    private boolean testModelCommand() {
        Object selectedElement = ProjectExplorerUtil.getSingleSelectedElement();
        System.out.println("MenuPropertyTester.testModelCommand(): selectedElement = " + selectedElement);
        if ( selectedElement instanceof IProject ) {
        	return true;
        }
        else if ( selectedElement instanceof IFolder ) {
        	IFolder folder = (IFolder) selectedElement;
        	String folderName = folder.getName();
        	return "TelosysTools".equals(folderName) || "models".equals(folderName);
        }
		return false;
    }
}
