package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.telosys.tools.api.TelosysProject;

public class EditEntityMenuItemListener implements Listener {

	private final TelosysProject telosysProject;
	private final Combo modelsCombo;
	private final Table table;
	
	public EditEntityMenuItemListener(TelosysProject telosysProject, Combo modelsCombo, Table table) {
		this.telosysProject = telosysProject;
		this.modelsCombo = modelsCombo;
		this.table = table;
	}
	
	@Override
	public void handleEvent(Event event) {
		// event:
		//   type = 13 ( Selection = 13 )
		//   widget : org.eclipse.swt.widgets.MenuItem
		String entityName = TableUtils.getSingleSelectedRowDataAsString(table);
		//DialogBox.showInformation("Edit ENTITY \n entityName = " + entityName );
		if ( entityName != null) {
			TelosysCommand.editEntity(telosysProject, modelsCombo, entityName);
		}		
	}

}
