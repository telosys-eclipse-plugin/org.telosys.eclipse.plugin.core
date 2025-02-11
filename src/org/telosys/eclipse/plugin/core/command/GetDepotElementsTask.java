package org.telosys.eclipse.plugin.core.command;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.telosys.eclipse.plugin.core.commons.ProjectUtil;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.commons.depot.DepotRateLimit;
import org.telosys.tools.commons.depot.DepotResponse;

/**
 * Eclipse runnable task with a progress bar 
 *  
 * @author L. Guerin
 *
 */
public class GetDepotElementsTask implements IRunnableWithProgress 
{
//	private final IProject project;
	private final TelosysProject telosysProject;	
	private final String   depot;

	/**
	 * Initial state : void list and no error
	 */
	private DepotResponse depotResponse;
	private String    error = null; 
	
	/**
	 * Constructor
	 * @param project
	 * @param depot
	 */
//	public GetDepotElementsTask(IProject project, String depot) {
//		super();
//		this.project = project;
//		this.depot = depot;
//	}
	public GetDepotElementsTask(TelosysProject telosysProject, String depot) {
		super();
		this.telosysProject = telosysProject;
		this.depot = depot;
	}

	public DepotResponse getDepotResponse() {
		return depotResponse;
	}
	public Optional<String> getError() {
		return error != null ? Optional.of(error) : Optional.empty();
	}
	
	//--------------------------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void run(IProgressMonitor progressMonitor) throws InvocationTargetException, InterruptedException 
	{
		int totalWorkTasks = 2 ;
		progressMonitor.beginTask("Retrieving elements from depot", totalWorkTasks );
		
		progressMonitor.worked(1); // 1/2
		getElementsFromDepot();
		
		//--- Notifies that the work is done; that is, either the main task is completed or the user canceled it.
		progressMonitor.done();		
	}
	
	private void getElementsFromDepot() {
//		TelosysProject telosysProject = new TelosysProject(ProjectUtil.getOSFullPath(project));
		try {
			depotResponse = telosysProject.getModelsAvailableInDepot(depot); 
			if (depotResponse.getHttpStatusCode() == 200) {
				// OK, no error
				error = null;
				//elements = depotResponse.getElements();
			} else {
				// HTTP ERROR 
				if (depotResponse.getHttpStatusCode() == 403) {
					DepotRateLimit rateLimit = depotResponse.getRateLimit();
					error = "http status " + depotResponse.getHttpStatusCode() + " (Forbidden) " + rateLimit.getStandardMessage() ;
				}
				else {
					error = "http status " + depotResponse.getHttpStatusCode();
				}
			}
		} catch (Exception e) {
			error = e.getMessage();
		}				
	}
}
