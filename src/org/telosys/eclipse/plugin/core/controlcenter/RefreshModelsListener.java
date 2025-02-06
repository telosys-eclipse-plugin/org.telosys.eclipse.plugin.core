package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
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
		String currentModel = null;
		String modelName = modelsCombo.getText();
		if ( modelName.isBlank() || modelName.isEmpty() ) {
			currentModel = null;
		}
		else {
			currentModel = modelName;
		}

		//DialogBox.showInformation("RefreshModelListener: currentModel = " + currentModel);
		// Reload all models in combobox
		TelosysCommand.populateModels(telosysProject, modelsCombo, currentModel);
		// Reload entities if current model
		if ( currentModel != null ) {
			TelosysCommand.populateEntities(telosysProject, currentModel, entitiesTable);
		}
	}

}
