package org.telosys.eclipse.plugin.core.commons.dialogbox;

import java.io.File;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.telosys.eclipse.plugin.core.commons.AbstractDialogBox;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ProjectExplorerUtil;
import org.telosys.eclipse.plugin.core.commons.WorkbenchUtil;
import org.telosys.tools.api.TelosysProject;

public class NewModelDialogBox extends AbstractDialogBox {

	private final TelosysProject telosysProject;
	
	private Text   inputField;
	private String modelName;

	/**
	 * Constructor
	 * @param parentShell
	 */
	public NewModelDialogBox(TelosysProject telosysProject) {
		super(WorkbenchUtil.getActiveWindowShell(), "New Model");
		this.telosysProject = telosysProject;
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
			if ( modelFolder != null ) {
				// Model created => refresh and expand
				ProjectExplorerUtil.reveal(modelFolder);
				// close dialgox box
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
