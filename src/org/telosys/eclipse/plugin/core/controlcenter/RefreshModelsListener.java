package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.telosys.eclipse.plugin.core.telosys.TelosysCommand;
import org.telosys.tools.api.TelosysProject;

public class RefreshModelsListener implements Listener {

	private final TelosysProject telosysProject;
	private final Combo modelsCombo;
	private final Table entitiesTable;

	public RefreshModelsListener(TelosysProject telosysProject, Combo modelsCombo, Table entitiesTable) {
		this.telosysProject = telosysProject;
		this.modelsCombo = modelsCombo;
		this.entitiesTable = entitiesTable;
	}

	@Override
	public void handleEvent(Event event) {
		TelosysCommand.refreshModelsAndEntities(telosysProject, modelsCombo, entitiesTable);
	}

}
