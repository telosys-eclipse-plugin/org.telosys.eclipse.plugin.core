package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.telosys.tools.api.TelosysProject;

public class CheckModelsListener implements Listener {

	private final TelosysProject telosysProject;
	private final Combo modelsCombo;

	public CheckModelsListener(TelosysProject telosysProject, Combo modelsCombo) {
		this.telosysProject = telosysProject;
		this.modelsCombo = modelsCombo;
	}

	@Override
	public void handleEvent(Event event) {
		TelosysCommand.checkModel(telosysProject, modelsCombo);
	}

}
