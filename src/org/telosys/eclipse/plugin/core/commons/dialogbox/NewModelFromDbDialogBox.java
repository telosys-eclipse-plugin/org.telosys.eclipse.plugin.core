package org.telosys.eclipse.plugin.core.commons.dialogbox;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.telosys.eclipse.plugin.commons.DialogBox;
import org.telosys.eclipse.plugin.commons.WorkbenchUtil;
import org.telosys.eclipse.plugin.core.commons.AbstractDialogBox;

public class NewModelFromDbDialogBox extends AbstractDialogBox {

//	private final TelosysProject telosysProject;

	private Text   inputField;
	private String modelName;

	/**
	 * Constructor
	 */
//	public NewModelFromDbDialogBox(TelosysProject telosysProject) {
//		super(WorkbenchUtil.getActiveWindowShell(), "New Model from database");
//		this.telosysProject = telosysProject;
//	}
	public NewModelFromDbDialogBox() {
		super(WorkbenchUtil.getActiveWindowShell(), "New Model from database");
	}

	public String getModelName() {
		return modelName;
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
		if ( isValidName( inputField.getText() ) ) {
			modelName = inputField.getText();
			super.okPressed();
		}
		else {
			DialogBox.showWarning("Enter a valid model name please.");
		}
	}

}
