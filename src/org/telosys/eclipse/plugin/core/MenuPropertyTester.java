package org.telosys.eclipse.plugin.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.telosys.eclipse.plugin.core.commons.Config;
import org.telosys.eclipse.plugin.core.commons.Validator;

public class MenuPropertyTester extends PropertyTester {
	
	public static final String IS_TELOSYS_PROJECT    = "isTelosysProject";
	public static final String ACCEPT_INIT_COMMAND   = "acceptInitCommand";
	public static final String ACCEPT_MODEL_COMMANDS = "acceptModelCommands";

	public static final String ALWAYS_TRUE  = "alwaysTrue";
	public static final String ALWAYS_FALSE = "alwaysFalse";

	private static void log(String msg) {
		if ( Config.LOG_FROM_PROPERTY_TESTER ) {
			System.out.println("[LOG-MenuPropertyTester] - " + msg);
		}
	}
	
    public MenuPropertyTester() {
		super();
		log("Constructor");
	}

    private String getParamInfo(Object o) {
    	if ( o != null ) {    		
    		return o.toString() + "(" + o.getClass().getCanonicalName() + ")";
    	}
    	else {
    		return null;
    	}
    }
    
	@Override
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
    	
//        log("--------------------------" );
//        log("test(receiver, property, args, expectedValue)" );
//        log(" - receiver      = " + getParamInfo(receiver) );
//        log(" - property      = " + getParamInfo(property) ) ;
//        log(" - args          = " + getParamInfo(args) );
//        log(" - expectedValue = " + getParamInfo(expectedValue) );
        log("--------------------------" );
        log("test("+receiver+", '"+ property+"', args, " + expectedValue+")" );
        
        boolean result = false;
    	if ( IS_TELOSYS_PROJECT.equalsIgnoreCase(property) ) {
    		result = isSingleTelosysProjectSelected(receiver) ;
    	}
    	if ( ACCEPT_INIT_COMMAND.equalsIgnoreCase(property) ) {
    		result = acceptInitCommand(receiver) ;
    	}
    	if ( ACCEPT_MODEL_COMMANDS.equalsIgnoreCase(property) ) {
    		result = acceptModelCommands(receiver) ;
    	}
    	else if ( ALWAYS_TRUE.equalsIgnoreCase(property) ) {
    		result = true;
    	}
    	else if ( ALWAYS_FALSE.equalsIgnoreCase(property) ) {
    		result = false;
    	}
        log("test return : " + result );
        log("--------------------------" );
        return result;
        
    }
    
    /**
     * @param receiver
     * @return
     */
    private boolean isSingleTelosysProjectSelected(Object receiver) {
    	log("isSingleTelosysProjectSelected(" + receiver + ")" );
		Set<IProject> projects = getSelectedProjects(getList(receiver));
		if ( projects.size() == 1 ) {
			IProject project = projects.iterator().next(); // First (single) element
			// allowed for any single Telosys project 
			return Validator.isTelosysProject(project);
		}
    	return false;
    }
    
    /**
     * Returns true if the given selected element(s) accept the "Init" command
     * @param receiver
     * @return
     */
    private boolean acceptInitCommand(Object receiver) {
    	log("acceptInitCommand(" + receiver + ")" );
		Set<IProject> projects = getSelectedProjects(getList(receiver));
		if ( projects.size() == 1 ) {
			// allowed for any single project that is not a "Model" or a "Bundle"
			IProject project = projects.iterator().next(); // First (single) element
			return ( ! Validator.isModelDirectory(project) ) && ( ! Validator.isBundleDirectory(project) ); 
		}
    	return false;
    }

    /**
     * Returns true if the given selected element(s) accept all "Model" commands 
     * @param receiver
     * @return
     */
    private boolean acceptModelCommands(Object receiver) {
    	log("acceptModelCommands(" + receiver + ")" );
		Set<IProject> projects = getSelectedProjects(getList(receiver));
		if ( projects.size() == 1 ) {
			// allowed for any single project 
			IProject project = projects.iterator().next(); // First (single) element
			return Validator.isTelosysProject(project) || Validator.isModelDirectory(project);
		}
    	return false;
    }

//	private static boolean isTelosysProject(IProject project) {
//		if ( project != null ) {
//			IFolder telosystoolsFolder = project.getFolder(Const.TELOSYSTOOLS);
//			return telosystoolsFolder != null && telosystoolsFolder.exists();
//		}
//    	return false;
//	}
//	
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
//	
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
//
	
    
    private List<?> getList(Object receiver) {
    	if ( receiver instanceof List) {
    		List<?> list = (List<?>) receiver;
    		log("receiver is a List (size=" + list.size() + ")");
    		for ( Object o : list ) {
        		log(" - " + getParamInfo(o) );
    		}
        	return list;
    	}
    	return null;
    }
    
	private Set<IProject> getSelectedProjects(List<?> list) {
		Set<IProject> projects = new HashSet<>();
	    if (list == null || list.isEmpty()) {
	        return projects; // Return empty list if no selection
	    }
	    for ( Object element : list ) {
	        IProject project = getProjectFromElement(element);
	        if ( project != null ) {
	            projects.add(project); // HashSet avoid duplicates
	        }
	    }
	    return projects;
	}
	private IProject getProjectFromElement(Object element) {
		if ( element == null ) {
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
            return null;
        }
	}
}
