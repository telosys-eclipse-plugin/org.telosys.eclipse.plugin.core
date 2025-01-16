package org.telosys.eclipse.plugin.core.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ProjectUtil;
import org.telosys.tools.api.TelosysProject;

public abstract class AbstractCommandHandler extends AbstractHandler {
	
	protected TelosysProject getTelosysProject(IProject project) {
		return new TelosysProject(ProjectUtil.getOSFullPath(project));
	}

	protected Shell getShell() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		if (shell == null) {
			DialogBox.showError("Cannot get Shell from workbench!");
		}
		return shell;
	}
}
