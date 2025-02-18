package org.telosys.eclipse.plugin.core.commons;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;
import org.telosys.eclipse.plugin.commons.LoggerUtil;

public class PluginImages {

	private static final Logger LOGGER = LoggerUtil.getLogger(PluginImages.class.getName() );
	
	// image registry
	private static final ImageRegistry staticImageRegistry = new ImageRegistry() ;

	private static Bundle staticBundle = null ;
	
    /**
     * Initialize image registry for the given Plugin Bundle 
     * @param bundle
     */
    public static void initImageRegistry(Bundle bundle) {
    	LOGGER.info("initImageRegistry(" + bundle + ")" );
    	if ( bundle != null ) {
        	staticBundle = bundle;
        	initImageRegistry();
    	}
    	else {
    		DialogBox.showError("initPluginBundle() Bundle is null");
    	}
    }
	
	// image keys
	public static final String TELOSYS_LOGO  = "TELOSYS_LOGO" ; 
	
	public static final String ERROR     = "ERROR" ; 
	public static final String WARNING   = "WARNING" ; 
	public static final String BLANK     = "BLANK" ;
	
    //------------------------------------------------------------------------------------------------
	// Init the registry
	private static void initImageRegistry() {
//		registerImage(TELOSYS_LOGO, Const.TELOSYS_IMAGE ) ;
//		registerImage(ENTITY_FILE, "entity_16pix.png" ) ; // v 3.0.0
		registerImage(ERROR,       "error_16pix.png" ) ; // v 3.0.0
		registerImage(WARNING,     "warning_16pix.png" ) ; // v 3.0.0

		registerImage(BLANK,     "blank_16pix.png" ) ; // v 3.3.0	
	}
	
	public static Image getImage(String imageKey) {
		Image image = staticImageRegistry.get(imageKey);
		if ( image == null ) {
			DialogBox.showError("Cannot get image for key = '" + imageKey + "' " );
		}
		return image;
	}

	//------------------------------------------------------------------------------------------------
	/**
	 * Register the given image with the given key
	 * @param imageKey
	 * @param imageFileName
	 * @return
	 */
	private static void registerImage(String imageKey, String imageFileName) {
		URL imageURL = getImageURL(imageFileName);
		checkImageExistence(imageURL);
		if ( imageURL != null ) {
			LOGGER.fine("Register image: '" + imageKey + "' -> " + imageURL);
			staticImageRegistry.put(imageKey, ImageDescriptor.createFromURL(imageURL) );			
		}
		else {
			LOGGER.warning("Cannot get image URL for '" + imageFileName + "'");
		}
		//return ImageDescriptor.createFromURL(url);
	}
    private static void checkImageExistence(URL imageURL) {
        // Check if the image exists in the plugin's resources
    	LOGGER.info("check Image Existence: '" + imageURL + "' " );
        if (imageURL != null) {
            // Check if the image exists locally (for a file-based URL)
            File file = new File(imageURL.getFile());
            if ( file.exists() ) {
            	LOGGER.fine("image '" + file.getName() + "' exists. " );
            }
            else {
            	LOGGER.warning("image '" + file.getName() + "' doesn't exist. " );
    			DialogBox.showError("image '" + file.getName() + "' doesn't exist. ");
            }
        }
    }
	
//    private static Bundle getPluginBundle() {
//    	if ( $bundle == null ) {
//    		DialogBox.showError("getPluginBundle() Bundle is null");
//    	}
//    	return $bundle;
//    }
	//--- Icons folder embedded in the Eclipse Plugin
	private static final String IMAGES_PLUGIN_FOLDER     = "icons";

    //------------------------------------------------------------------------------------------------
    /**
     * Returns the plugin image file URL for the given file name 
     * @param sImageFile the image file
     * @return
     */
	private static URL getImageURL(String sImageFile) {
		URL imageURL = null ;
		URL baseURL = getBaseURL();
		if ( baseURL != null )
		{
			try {
				imageURL = new URL(baseURL, IMAGES_PLUGIN_FOLDER + "/" + sImageFile );
			} catch (MalformedURLException e) {
				DialogBox.showError("Cannot get image URL for '" + sImageFile + "'.", e.getMessage() );
			}
		}
		return imageURL ;
    }
	
    //------------------------------------------------------------------------------------------------
    /**
     * Returns the plugin base URL ( ie "file:/c:/xxx/xxx/eclipse/plugins/myplugin" ) 
     * @return
     */
    private static URL getBaseURL()
    {
        URL resolvedUrl = null;
        if ( staticBundle != null )
        {
            // URL pluginRelativeUrl = Platform.find($bundle, new Path(""));
            URL pluginRelativeUrl = FileLocator.find(staticBundle, new Path(""), null);
            if (pluginRelativeUrl != null)
            {
                try
                {
                    //resolvedUrl = Platform.resolve(pluginRelativeUrl);
                    resolvedUrl = FileLocator.resolve(pluginRelativeUrl);
                } catch (IOException e)
                {
                    DialogBox.showError("Cannot resolve relative URL '" + pluginRelativeUrl +"' !", e.getMessage() ) ; 
                }
            }
            else
            {
            	DialogBox.showError("Cannot get plugin directory : Platform.find() return null !");
            }
        }
        else
        {
        	DialogBox.showError("Cannot get plugin directory ( bundle is null ) !");
        }
        return resolvedUrl;
    }	
}
