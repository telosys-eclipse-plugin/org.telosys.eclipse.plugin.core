package org.telosys.eclipse.plugin.core;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.telosys.eclipse.plugin.core.commons.PluginImages;

import org.telosys.eclipse.plugin.commons.Logger;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.telosys.eclipse.plugin.core"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	//private static Bundle bundle;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		plugin = this;
		Logger.active = true;
		Logger.log("-------------------------------------------------" );
		Logger.log("Starting Plugin " + PLUGIN_ID );
		Logger.log("BundleContext: " + bundleContext );
        // Attach listener 
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addSelectionListener(new SelectionListener_BAK());
		
		// bundle = bundleContext.getBundle();
		// Image Registry initialization 
		PluginImages.initImageRegistry(bundleContext.getBundle()); 
		Logger.log("-------------------------------------------------" );
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		Logger.log("-------------------------------------------------" );
		Logger.log("Stoping Plugin " + PLUGIN_ID );
		Logger.log("-------------------------------------------------" );
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
