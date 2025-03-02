package org.telosys.eclipse.plugin.core.commons.dialogbox;

import java.io.File;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.telosys.eclipse.plugin.commons.DialogBox;
import org.telosys.eclipse.plugin.commons.WorkbenchUtil;
import org.telosys.eclipse.plugin.core.commons.AbstractDialogBox;
import org.telosys.eclipse.plugin.core.commons.ProjectExplorerUtil;
import org.telosys.eclipse.plugin.core.telosys.TelosysCommand;
import org.telosys.tools.api.TelosysProject;

public class NewModelDialogBox extends AbstractDialogBox {

	private final TelosysProject telosysProject;
	private final Combo modelsCombo;
	
	private Text   inputField;
	private String modelName;

	/**
	 * Constructor
	 * @param telosysProject
	 * @param modelsCombo
	 */
	public NewModelDialogBox(TelosysProject telosysProject, Combo modelsCombo) {
		super(WorkbenchUtil.getActiveWindowShell(), "New Model");
		this.telosysProject = telosysProject;
		this.modelsCombo = modelsCombo;
	}
	
	@Override
	protected Point getInitialSize() {
		// return the window size : width, height
		return new Point(450, 200);
	}
	
	@Override
	protected void createContent(Composite container) {
		inputField = createLabelAndField("Model name");
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// set specific labels for the buttons
		createButton(parent, IDialogConstants.OK_ID,     "Create", true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void okPressed() { // "Create" pressed
		modelName = inputField.getText();
		if ( isValidName(modelName) ) {
			File modelFolder = createModel(modelName);
			if ( modelFolder != null ) { // Model created 				
				// Refresh combo-box if any
				if ( modelsCombo != null ) {
					TelosysCommand.refreshModels(telosysProject, modelsCombo);
				}
				// Refresh and expand folder in Project Explorer
				ProjectExplorerUtil.reveal(modelFolder);
				// close dialog-box
				super.okPressed();
			}
		}
		else {
			DialogBox.showWarning("Enter a valid model name please.");
		}
	}
	
    private File createModel(String modelName) {
    	if ( telosysProject.modelFolderExists(modelName) ) {
    		DialogBox.showWarning("Model '" + modelName + "' already exists");
    		return null;
    	}
    	else {
    		File modelFolder = telosysProject.createNewDslModel(modelName);
    		if ( modelFolder == null ) {
        		DialogBox.showError("Cannot create model '" + modelName + "' (Telosys return null)");
    		}
    		return modelFolder;
    	}
    }
}
