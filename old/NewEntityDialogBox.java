package org.telosys.eclipse.plugin.core.command;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.telosys.eclipse.plugin.core.commons.AbstractDialogBox;
import org.telosys.eclipse.plugin.core.commons.DialogBox;

public class NewEntityDialogBox extends AbstractDialogBox {

	private static final int BOX_WIDTH  = 600;
	private static final int BOX_HEIGHT = 300;

	private final IProject  project;
	private final String[]  allModels;
	private final String    currentModel;
	
	// UI widgets 
	private Combo  modelCombo;
	//private Button checkModelButton;
	private Text   entityNameText;
	
	// Output data
	private String modelName = "";
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
	 * @param project
	 * @param allModels
	 * @param currentModel
	 */
	public NewEntityDialogBox(Shell parentShell, IProject project, String[] allModels, String currentModel) {
		super(parentShell, "New Entity", createDialogBoxLayout());
		log("CONSTRUCTOR()");
		this.project = project;
		this.allModels = allModels;
		this.currentModel = currentModel;
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
    
	private void createRowProject(Composite composite, IProject project) {
        //--- Label
        Label label = new Label(composite, SWT.NONE);
        label.setText("Project: ");
        label.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT));

        //--- ComboBox
        label = new Label(composite, SWT.NONE);
        label.setText(project.getName());
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
        if ( currentModel != null ) {
            int index = modelCombo.indexOf(currentModel); // Find the index of the value
            if (index != -1) {
            	modelCombo.select(index); // Select the item if it exists
            }
        }
	}
	private void createRow2(Composite composite) {
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
        createRowProject(composite, this.project); 
		//--- Model : label + combo box
		createRowModel(composite);
		//--- Entity name : label + input text
		createRow2(composite);
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
		if ( modelName.isEmpty() || modelName.isBlank() ) {
			DialogBox.showWarning("No model selected!");
			return;
		}
		if ( entityName.isEmpty() || entityName.isBlank() ) {
			DialogBox.showWarning("No entity name!");
			return;
		}
		super.okPressed(); // Return to handler
	}
}
