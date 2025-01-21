package org.telosys.eclipse.plugin.core.command;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.telosys.eclipse.plugin.core.commons.AbstractDialogBox;
import org.telosys.eclipse.plugin.core.commons.DialogBox;

public class NewEntityFromModelDialogBox extends AbstractDialogBox {

	private static final int BOX_WIDTH  = 600;
	private static final int BOX_HEIGHT = 300;

	private final String  modelName;
	
	// UI widgets 
	private Text   entityNameText;
	
	// Output data
	private String entityName = "";
	
	private static Layout createDialogBoxLayout() {
		GridLayout layout = new GridLayout(1, false);
		layout.marginRight = 5;
		layout.marginLeft = 10;
		return layout;
	}

	/**
	 * Constructor
	 * @param parentShell
	 * @param modelName
	 */
	public NewEntityFromModelDialogBox(Shell parentShell, String modelName) {
		super(parentShell, "New Entity", createDialogBoxLayout());
		log("CONSTRUCTOR()");
		this.modelName = modelName;
	}

	public String getModelName() {
		return modelName;
	}
	public String getEntityName() {
		return entityName;
	}
	
	@Override
	protected Point getInitialSize() {
		log("getInitialSize()");
		// Default size : width, height
		return new Point(BOX_WIDTH, BOX_HEIGHT);
	}
    
	private void createLabel(Composite composite, String text) {
        Label label = new Label(composite, SWT.NONE);
        label.setText(text);
        label.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT));
	}
	private void createRowModel(Composite composite) {
        createLabel(composite,  "Model: ");
        createLabel(composite,  this.modelName);
	}
	private void createRowEntity(Composite composite) {
        createLabel(composite,  "Entity name: ");
        //--- Input field (Text)
		entityNameText = new Text(composite, SWT.BORDER);
        GridData gridData = new GridData();  
        gridData.grabExcessHorizontalSpace = true;
        gridData.widthHint = 300;
        entityNameText.setLayoutData(gridData);
	}
	
	@Override
	protected void createContent(Composite parent) {
		log("createContent()");
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));  // 2 columns
		//--- Model : label + combo box
		createRowModel(composite);
		//--- Entity name : label + input text
		createRowEntity(composite);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		log("createButtonsForButtonBar()");
		// define buttons to be displayed at the bottom right of the window 
		// set specific labels for the buttons
		createButton(parent, IDialogConstants.OK_ID,     "Create", true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void okPressed() {
		// "OK" = "Create" button  
		entityName = entityNameText.getText().trim();
		if ( entityName.isEmpty() || entityName.isBlank() ) {
			DialogBox.showWarning("No entity name!");
			return;
		}
		super.okPressed(); // Return to handler
	}
}
