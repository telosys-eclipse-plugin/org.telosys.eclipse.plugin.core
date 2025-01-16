package org.telosys.eclipse.plugin.core.command;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.telosys.eclipse.plugin.core.commons.ProjectUtil;
import org.telosys.tools.api.InstallationType;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.commons.depot.DepotElement;

/**
 * Eclipse runnable task with a progress bar 
 *  
 * @author L. Guerin
 *
 */
public class InstallDepotElementsTask implements IRunnableWithProgress 
{
	private final IProject project;
	private final String   depot;
	private final InstallationType installationType;
	private final List<DepotElement> elements;

	// Installation result : text + count 
	private final StringBuilder sbResult;
	private int numberOfElementsInstalled = 0 ;

	
	/**
	 * Constructor
	 */
	public InstallDepotElementsTask(IProject project, String depot, List<DepotElement> elements, InstallationType installationType ) {
		super();
		this.project = project;
		this.depot = depot;
		this.elements = elements;
		this.installationType = installationType;
		this.sbResult = new StringBuilder();
	}

	public String getResult() {
		return sbResult.toString();
	}
	public int getInstallationsCount() {
		return numberOfElementsInstalled;
	}

	
	//--------------------------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void run(IProgressMonitor progressMonitor) throws InvocationTargetException, InterruptedException 
	{
		int totalWorkTasks = 2 ;
		progressMonitor.beginTask("Installing elements from depot", totalWorkTasks );
		
		progressMonitor.worked(1); // 1/2
		installElementsFromDepot();
		
		//--- Notifies that the work is done; that is, either the main task is completed or the user canceled it.
		progressMonitor.done();		
	}
	
	private void installElementsFromDepot() {
		TelosysProject telosysProject = new TelosysProject(ProjectUtil.getOSFullPath(project));
		numberOfElementsInstalled = 0;
		for (DepotElement e : elements) {
			try {
				if ( telosysProject.downloadAndInstallBranch(depot, e.getName(), e.getDefaultBranch(), installationType) ) {
					sbResult.append(" . '" + e.getName() + "' : installed. ");
					numberOfElementsInstalled++;
				} else {
					sbResult.append(" . '" + e.getName() + "' : not installed (already exists). ");
				}
			} catch (Exception ex) {
				sbResult.append(" . '" + e.getName() + "' : ERROR : " + ex.getMessage());
			}
			sbResult.append("\n");
		}
		sbResult.append(numberOfElementsInstalled + "/" + elements.size() + " installed. \n");
	}
}
