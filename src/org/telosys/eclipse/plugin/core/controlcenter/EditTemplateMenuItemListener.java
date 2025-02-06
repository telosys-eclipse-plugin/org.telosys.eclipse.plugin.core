package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.telosys.tools.api.TelosysProject;

public class EditTemplateMenuItemListener implements Listener {

	private final TelosysProject telosysProject;
	private final Combo bundlesCombo;
	private final Table templatesTable;
	
	public EditTemplateMenuItemListener(TelosysProject telosysProject, Combo bundlesCombo, Table templatesTable) {
		this.telosysProject = telosysProject;
		this.bundlesCombo = bundlesCombo;
		this.templatesTable = templatesTable;
	}
	
	@Override
	public void handleEvent(Event event) {
		// event:
		//   type = 13 ( Selection = 13 )
		//   widget : org.eclipse.swt.widgets.MenuItem
		String templateName = TableUtils.getSingleSelectedRowDataAsString(templatesTable);
		//DialogBox.showInformation("Edit ENTITY \n entityName = " + entityName );
		if ( templateName != null) {
			TelosysCommand.editTemplate(telosysProject, bundlesCombo, templateName);
		}		
	}

}
