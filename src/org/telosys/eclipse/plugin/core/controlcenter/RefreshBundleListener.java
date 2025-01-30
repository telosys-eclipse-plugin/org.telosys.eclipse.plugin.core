package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.telosys.eclipse.plugin.core.commons.DialogBox;

public class RefreshBundleListener implements Listener {

	private final Table table;
	
	public RefreshBundleListener(Table table) {
		this.table = table;
	}
	
	@Override
	public void handleEvent(Event event) {
			DialogBox.showInformation("TODO: RefreshBundleListener");
	}

}
