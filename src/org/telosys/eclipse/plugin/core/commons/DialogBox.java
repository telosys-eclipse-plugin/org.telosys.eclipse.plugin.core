package org.telosys.eclipse.plugin.core.commons;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class DialogBox {
    
	/**
	 * Get the active shell
	 * @return
	 */
	private static Shell getShell() {
		//return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		return WorkbenchUtil.getActiveWindowShell();
    }
    
    /**
     * Show information message
     * @param message
     */
    public static void showInformation(String message) {
        MessageDialog.openInformation(getShell(), "Information", message);
    }
    /**
     * Show information message
     * @param title
     * @param message
     */
    public static void showInformation(String title, String message) {
        MessageDialog.openInformation(getShell(), title, message);
    }

    /**
     * Show warning message
     * @param message
     */
    public static void showWarning(String message) {
        MessageDialog.openWarning(getShell(), "Warning", message);
    }
    /**
     * Show warning message
     * @param title
     * @param message
     */
    public static void showWarning(String title, String message) {
        MessageDialog.openWarning(getShell(), title, message);
    }
	
    /**
     * Show error message
     * @param message
     */
    public static void showError(String message) {
        showError("Error", message);
    }
    /**
     * Show error message
     * @param title
     * @param message
     */
    public static void showError(String title, String message) {
        MessageDialog.openError(getShell(), title, message);
    }
}
