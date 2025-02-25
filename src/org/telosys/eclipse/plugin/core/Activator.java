package org.telosys.eclipse.plugin.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.telosys.eclipse.plugin.commons.CustomRootLogger;
import org.telosys.eclipse.plugin.commons.EclipseConsoleAsLoggerHandler;
import org.telosys.eclipse.plugin.core.commons.PluginImages;
import org.telosys.eclipse.plugin.core.telosys.DbModelObserver;
import org.telosys.tools.db.observer.DatabaseObserverProvider;

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
		System.out.println("Telosys-Plugin: Activator (class " + this.getClass().getName() + ")");
		
		CustomRootLogger.setup(PLUGIN_ID, Level.ALL, new EclipseConsoleAsLoggerHandler("Telosys logger: core") );		
		Logger logger = Logger.getLogger(this.getClass().getName());
		
		logger.info("-------------------------------------------------" );
		logger.info("Telosys plugin startup: Activator.start()" );
		logger.info("Current working directory: " + System.getProperty("user.dir"));
		logger.info("Starting Plugin " + PLUGIN_ID );
		logger.info("BundleContext: " + bundleContext );
        // Attach listener 
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addSelectionListener(new SelectionListener_BAK());
		
		// bundle = bundleContext.getBundle();
		// Image Registry initialization 
		
		logger.info("Init ImageRegistry " );
		PluginImages.initImageRegistry(bundleContext.getBundle()); 
		logger.info("Init Telosys observers (for task output) " );
		DatabaseObserverProvider.setModelObserverClass(DbModelObserver.class);
//		DatabaseObserverProvider.setMetadataObserverClass(DbMetadataObserver.class);
//		DbMetadataObserver.setActive(false);

		logger.info("-------------------------------------------------" );
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
//		LOGGER.info("-------------------------------------------------" );
//		LOGGER.info("Stoping Plugin " + PLUGIN_ID );
//		LOGGER.info("-------------------------------------------------" );
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
