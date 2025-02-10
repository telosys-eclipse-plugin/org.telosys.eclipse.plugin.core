package org.telosys.eclipse.plugin.core.command;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.telosys.eclipse.plugin.core.commons.AbstractDialogBox;

public class NewModelDialogBox extends AbstractDialogBox {

	private Text   inputField;
	private String modelName;

	public String getModelName() {
		return modelName ;
	}

	/**
	 * Constructor
	 * @param parentShell
	 */
	public NewModelDialogBox(Shell parentShell) {
		super(parentShell, "New Model");
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
	protected void okPressed() {
		modelName = inputField.getText();
		super.okPressed();
	}
}
