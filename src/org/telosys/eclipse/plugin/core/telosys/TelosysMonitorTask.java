package org.telosys.eclipse.plugin.core.telosys;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * Eclipse runnable task with a progress bar 
 *  
 * @author L. Guerin
 *
 */
public class TelosysMonitorTask implements IRunnableWithProgress {
	// Input Data
	private final String    waitMessage;
	private final Supplier<String>  supplierTask;
	// Output Data
	private String result;
	
	/**
	 * Constructor
	 */
	public TelosysMonitorTask(String waitMessage, Supplier<String> supplierTask) {
		super();
		this.waitMessage = waitMessage;
		this.supplierTask = supplierTask;
		this.result = null; 
	}

	public String getResult() {
		return result;
	}
	
	@Override
	public void run(IProgressMonitor progressMonitor) throws InvocationTargetException, InterruptedException 
	{
		int totalWorkTasks = 2 ;
		
		// Print wait message
		progressMonitor.beginTask(waitMessage, totalWorkTasks );
		progressMonitor.worked(1); // 1/2
		// Run the task
		result = supplierTask.get();
		// Notifies that the work is done (either the main task is completed or the user canceled it)
		progressMonitor.done();		
	}	
}
