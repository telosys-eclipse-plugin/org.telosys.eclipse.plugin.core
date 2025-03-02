package org.telosys.eclipse.plugin.core.commons.dialogbox;

import java.io.File;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;
import org.telosys.eclipse.plugin.commons.DialogBox;
import org.telosys.eclipse.plugin.commons.WorkbenchUtil;
import org.telosys.eclipse.plugin.core.commons.AbstractDialogBox;
import org.telosys.eclipse.plugin.core.telosys.TelosysCommand;
import org.telosys.tools.api.TelosysProject;

public class NewEntityFromModelDialogBox extends AbstractDialogBox {

	private static final int BOX_WIDTH  = 600;
	private static final int BOX_HEIGHT = 300;

	// Input data
	private final TelosysProject telosysProject;
	private final String  modelName;
	
	// UI widgets 
	private Text   entityNameText;
	
	// Output data
	private String  entityName = "";
	private File    entityFile = null;
	
	private static Layout createDialogBoxLayout() {
		GridLayout layout = new GridLayout(1, false);
		layout.marginRight = 5;
		layout.marginLeft = 10;
		return layout;
	}

	/**
	 * Constructor
	 * @param telosysProject
	 * @param modelName
	 */
	public NewEntityFromModelDialogBox(TelosysProject telosysProject, String modelName) {
		super(WorkbenchUtil.getActiveWindowShell(), "New Entity", createDialogBoxLayout());
		log("CONSTRUCTOR()");
		this.telosysProject = telosysProject;
		this.modelName = modelName;
	}

	public String getModelName() {
		return modelName;
	}
	public String getEntityName() {
		return entityName;
	}
	public File getEntityFile() {
		return entityFile;
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
		entityName = entityNameText.getText();
		if ( isValidName(entityName) ) {
			entityFile = TelosysCommand.newEntity(telosysProject, modelName, entityName);
			if ( entityFile != null ) { // Entity file created
            	// Close Dialog-Box and return OK 
				super.okPressed(); 
			}
		}
		else {
			DialogBox.showWarning("Invalid entity name!");
		}
	}
}
