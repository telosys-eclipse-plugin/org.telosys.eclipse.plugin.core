package org.telosys.eclipse.plugin.core;

import java.util.logging.Logger;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.telosys.eclipse.plugin.commons.LoggerUtil;
import org.telosys.eclipse.plugin.core.commons.PluginImages;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
	
	private static final Logger LOGGER = LoggerUtil.getLogger(Activator.class.getName() );


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
		System.out.println("Telosys-Plugin: current working directory: " + System.getProperty("user.dir"));
		LOGGER.info("-------------------------------------------------" );
		LOGGER.info("Starting Plugin " + PLUGIN_ID );
		LOGGER.info("BundleContext: " + bundleContext );
        // Attach listener 
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addSelectionListener(new SelectionListener_BAK());
		
		// bundle = bundleContext.getBundle();
		// Image Registry initialization 
		PluginImages.initImageRegistry(bundleContext.getBundle()); 
		LOGGER.info("-------------------------------------------------" );
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		LOGGER.info("-------------------------------------------------" );
		LOGGER.info("Stoping Plugin " + PLUGIN_ID );
		LOGGER.info("-------------------------------------------------" );
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
