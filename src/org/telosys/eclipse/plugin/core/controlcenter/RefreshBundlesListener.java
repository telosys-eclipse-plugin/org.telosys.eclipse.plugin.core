package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.telosys.eclipse.plugin.core.telosys.TelosysCommand;
import org.telosys.tools.api.TelosysProject;

public class RefreshBundlesListener implements Listener {

	private final TelosysProject telosysProject;
	private final Combo bundlesCombo;
	private final Table templatesTable;
	private final Button copyStaticFilesCheckBox;

	public RefreshBundlesListener(TelosysProject telosysProject, Combo bundlesCombo, Table templatesTable, Button copyStaticFilesCheckBox) {
		this.telosysProject = telosysProject;
		this.bundlesCombo = bundlesCombo;
		this.templatesTable = templatesTable;
		this.copyStaticFilesCheckBox = copyStaticFilesCheckBox;
	}

	
	@Override
	public void handleEvent(Event event) {
//		// Get current bundle if any
//		String currentBundle = null;
//		String bundleName = bundlesCombo.getText();
//		if ( bundleName.isBlank() || bundleName.isEmpty() ) {
//			currentBundle = null;
//		}
//		else {
//			currentBundle = bundleName;
//		}
//
//		// Reload bunldes in combo box
//		TelosysCommand.populateBundles(telosysProject, bundlesCombo, currentBundle);
//		// Reload templates if current bundle
//		if ( currentBundle != null ) {
//			TelosysCommand.populateTemplates(telosysProject, currentBundle, copyStaticFilesCheckBox, templatesTable);
//		}
//		else {
//			// No current bundle => activate = false 
//			TelosysCommand.setCopyStaticFilesCheckBoxState(currentBundle, copyStaticFilesCheckBox);
//		}
		TelosysCommand.refreshBundlesAndTemplates(telosysProject, bundlesCombo, copyStaticFilesCheckBox, templatesTable);
	}

}
