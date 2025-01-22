package org.telosys.eclipse.plugin.core.command;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.telosys.eclipse.plugin.core.commons.AbstractDialogBox;
import org.telosys.eclipse.plugin.core.commons.Const;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ModelCheckStatus;
import org.telosys.eclipse.plugin.core.commons.ProjectUtil;
import org.telosys.eclipse.plugin.core.commons.TelosysEvolution;
import org.telosys.tools.api.TelosysProject;

public class CheckModelFromProjectDialogBox extends AbstractDialogBox {

	private static final int BOX_WIDTH  = 600;
	private static final int BOX_HEIGHT = 700;

	private final IProject  project;
	private final String[]  allModels;
	
	// UI widgets 
	private Combo  modelCombo;
	private Text   checkModelResultText;

	private static Layout createDialogBoxLayout() {
		GridLayout layout = new GridLayout(1, false);
		layout.marginRight = 5;
		layout.marginLeft = 10;
		return layout;
	}

	/**
	 * Constructor
	 */
	public CheckModelFromProjectDialogBox(Shell parentShell, IProject project, String[] allModels) {
		super(parentShell, "Check Model", createDialogBoxLayout());
		log("CONSTRUCTOR()");
		this.project = project;
		this.allModels = allModels;
	}
	
	@Override
	protected Point getInitialSize() {
		log("getInitialSize()");
		// Default size : width, height
		return new Point(BOX_WIDTH, BOX_HEIGHT);
	}
    
	static class ButtonCheckModelSelectionAdapter extends SelectionAdapter {
        private final CheckModelFromProjectDialogBox dialogBox;
        public ButtonCheckModelSelectionAdapter(CheckModelFromProjectDialogBox dialogBox) {
            this.dialogBox = dialogBox;
        }
        @Override
        public void widgetSelected(SelectionEvent e) {
            // Button clicked
        	this.dialogBox.checkModel();
        }
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
	}
	private Button createButton(Composite container, String buttonText, SelectionListener listener) {
        Button button = new Button(container, SWT.PUSH);
        button.setText(buttonText);
        GridData gridData = new GridData(SWT.DEFAULT, SWT.DEFAULT);
        gridData.widthHint = 200; // Set the button width
        gridData.horizontalSpan = 2;
        button.setLayoutData(gridData);
        // Add selection listener to handle button click
        button.addSelectionListener(listener);
        return button;
	}
	
	private void checkModel() {
		String modelName = modelCombo.getText(); // current selected item in combo
		checkModelResultText.setText("Checking model '" + modelName + "' ...");
		if ( modelName != null && !modelName.isEmpty() && !modelName.isBlank()) {
			checkModel(modelName);
		}
		else {
			DialogBox.showWarning("No model selected");
		}
	}
	private void checkModel(String modelName) {
		TelosysProject telosysProject = ProjectUtil.getTelosysProject(project);
		ModelCheckStatus modelCheckStatus = TelosysEvolution.checkModel(telosysProject, modelName);
		checkModelResultText.setText(modelCheckStatus.getFullReport());
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

		createButton(composite, "Check model", new ButtonCheckModelSelectionAdapter(this));
		
	    //--- Multi-lines text box to print installation result 
		checkModelResultText = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.READ_ONLY ); // SWT.H_SCROLL
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData.horizontalSpan = 2;
        gridData.widthHint  = Const.CHECK_MODEL_REPORT_SIZE_WIDTH;    // Fixed horizontal size in pixels	
	    gridData.heightHint = Const.CHECK_MODEL_REPORT_SIZE_HEIGHT ;  // Fixed vertical size in pixels		
		checkModelResultText.setLayoutData(gridData); // reuse same GridData
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		log("createButtonsForButtonBar()");
		// define buttons to be displayed at the bottom right of the window 
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	
}
