package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.tools.api.TelosysProject;

public class RefreshBundlesListener implements Listener {

	private final TelosysProject telosysProject;
	private final Combo bundlesCombo;
	private final Table table;

	public RefreshBundlesListener(TelosysProject telosysProject, Combo bundlesCombo, Table table) {
		this.telosysProject = telosysProject;
		this.bundlesCombo = bundlesCombo;
		this.table = table;
	}

	
	@Override
	public void handleEvent(Event event) {
			DialogBox.showInformation("TODO: RefreshBundleListener");
	}

}
