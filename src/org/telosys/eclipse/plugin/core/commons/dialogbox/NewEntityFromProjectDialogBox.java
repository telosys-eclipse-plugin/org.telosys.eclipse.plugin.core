package org.telosys.eclipse.plugin.core.commons.dialogbox;

import java.io.File;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;
import org.telosys.eclipse.plugin.core.commons.AbstractDialogBox;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.WorkbenchUtil;
import org.telosys.eclipse.plugin.core.telosys.TelosysCommand;
import org.telosys.tools.api.TelosysProject;

public class NewEntityFromProjectDialogBox extends AbstractDialogBox {

	private static final int BOX_WIDTH  = 600;
	private static final int BOX_HEIGHT = 300;

	// Input data
	private final TelosysProject telosysProject;
	private final String    projectName;
	private final String[]  allModels;
	
	// UI widgets 
	private Combo  modelCombo;
	private Text   entityNameText;
	
	// Output data
	private String modelName = "";
	private String entityName = "";
	private File   entityFile = null;

	
	private static Layout createDialogBoxLayout() {
		GridLayout layout = new GridLayout(1, false);
		layout.marginRight = 5;
		layout.marginLeft = 10;
		return layout;
	}

	/**
	 * Constructor
	 * @param telosysProject
	 * @param projectName
	 * @param allModels
	 */
	public NewEntityFromProjectDialogBox(TelosysProject telosysProject, String projectName, String[] allModels) {
		super(WorkbenchUtil.getActiveWindowShell(), "New Entity", createDialogBoxLayout());
		log("CONSTRUCTOR()");
		this.telosysProject = telosysProject;
		this.projectName = projectName;
		this.allModels = allModels;
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
    
	private void createRowProject(Composite composite) {
        //--- Label
        Label label = new Label(composite, SWT.NONE);
        label.setText("Project: ");
        label.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT));

        //--- Label
        label = new Label(composite, SWT.NONE);
        label.setText(projectName);
        label.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	private void createRowModel(Composite composite) {
        //--- Label
        Label label = new Label(composite, SWT.NONE);
        label.setText("Model: ");
        label.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT));

        //--- ComboBox
        modelCombo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
        GridData data = new GridData();  
        data.grabExcessHorizontalSpace = true;
        data.widthHint = 300;
        modelCombo.setLayoutData(data);
        modelCombo.setVisibleItemCount(10);
        // combo items (models)
        modelCombo.setItems(allModels);
	}
	private void createRowEntity(Composite composite) {
        //--- Label
        Label label = new Label(composite, SWT.NONE);
        label.setText("Entity name: ");
        label.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT));

        //--- Text
		entityNameText = new Text(composite, SWT.BORDER);
		entityNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}
	
	@Override
	protected void createContent(Composite parent) {
		log("createContent()");
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));  // 2 columns
        //--- Project
        createRowProject(composite); 
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
		modelName  = modelCombo.getText().trim();
		entityName = entityNameText.getText().trim();
		if ( ! isValidName(modelName) ) {
			DialogBox.showWarning("Invalid model name (not selected)!");
			return;
		}
		if ( ! isValidName(entityName) ) {
			DialogBox.showWarning("Invalid entity name!");
			return;
		}
		// Try to create entity
		entityFile = TelosysCommand.newEntity(telosysProject, modelName, entityName);
		if ( entityFile != null ) { // Entity file created
        	// Close Dialog-Box and return OK 
			super.okPressed(); 
		}
	}
}
