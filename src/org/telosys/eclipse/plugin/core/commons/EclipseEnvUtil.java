package org.telosys.eclipse.plugin.core.commons;

import java.io.File;

import org.eclipse.core.runtime.Platform;

public class EclipseEnvUtil {
    
    public static String getCurrentDirFromSystem() {
    	return System.getProperty("user.dir");
    }

    public static String getCurrentDirFromPlatform() {
        String installPath = Platform.getInstallLocation().getURL().getPath();
        File file = new File(installPath);
        return file.getAbsolutePath();
    }
}
