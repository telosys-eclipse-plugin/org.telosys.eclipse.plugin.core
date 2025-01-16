package org.telosys.eclipse.plugin.core.command;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
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
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ModelCheckStatus;
import org.telosys.eclipse.plugin.core.commons.ProjectUtil;
import org.telosys.eclipse.plugin.core.commons.TelosysEvolution;
import org.telosys.tools.api.TelosysModelException;
import org.telosys.tools.api.TelosysProject;

public class CheckModelDialogBox extends AbstractDialogBox {

	private static final int BOX_WIDTH  = 600;
	private static final int BOX_HEIGHT = 700;

	private final IProject  project;
	private final String[]  allModels;
	private final String    currentModel;
	
	// UI widgets 
	private Combo  modelCombo;
	private Button checkModelButton;
	private Text   checkModelResultText;

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
	 * @param model
	 */
	public CheckModelDialogBox(Shell parentShell, IProject project, String[] allModels, String model) {
		super(parentShell, "Check Model", createDialogBoxLayout());
		log("CONSTRUCTOR()");
		this.project = project;
		this.allModels = allModels;
		this.currentModel = model;
	}
	
	@Override
	protected Point getInitialSize() {
		log("getInitialSize()");
		// Default size : width, height
		return new Point(BOX_WIDTH, BOX_HEIGHT);
	}
    
	static class ButtonCheckModelSelectionAdapter extends SelectionAdapter {
        private final CheckModelDialogBox dialogBox;
        public ButtonCheckModelSelectionAdapter(CheckModelDialogBox dialogBox) {
            this.dialogBox = dialogBox;
        }
        @Override
        public void widgetSelected(SelectionEvent e) {
            // Button clicked
        	this.dialogBox.checkModel();
        }
    }
    
	private void createRow1(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(2, false));  // 2 columns with no margin between them

        //--- Label
        Label label = new Label(container, SWT.NONE);
        label.setText("Model: ");
        label.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT));

        //--- ComboBox
        modelCombo = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
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
	
	private Button createButton(Composite container, String buttonText, SelectionListener listener) {
        Button button = new Button(container, SWT.PUSH);
        button.setText(buttonText);
        GridData gridData = new GridData(SWT.DEFAULT, SWT.DEFAULT);
        gridData.widthHint = 200; // Set the button width
        button.setLayoutData(gridData);
        // Add selection listener to handle button click
        button.addSelectionListener(listener);
        return button;
	}
	
	private void checkModel() {
		String modelName = modelCombo.getText(); // current selected item in combo
		checkModelResultText.setText("Checking model '" + modelName + "' ...");
		if ( modelName != null ) {
			checkModel(modelName);
		}
		else {
			DialogBox.showWarning("No model selected");
		}
	}
	private void checkModel(String modelName) {
		TelosysProject telosysProject = ProjectUtil.getTelosysProject(project);
		ModelCheckStatus modelCheckStatus = TelosysEvolution.checkModel(telosysProject, modelName);
//		if ( modelCheckStatus.isOK() ) {
//			checkModelResultText.setText("Model '" + modelName + "' is OK (no error).");
//		}
//		else {
//			checkModelResultText.setText("Model '" + modelName + "' is not valid!");
//		}
		checkModelResultText.setText(modelCheckStatus.getFullReport());
	}
	
	private void runTask(IRunnableWithProgress runnable) {
		ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(getShell()) ;
		try {
			// Run task 
			progressMonitorDialog.run(false, false, runnable);
		} catch (InvocationTargetException e) {
			DialogBox.showError("Error during task", e.getMessage());
		} catch (InterruptedException e) {
			DialogBox.showInformation("Task interrupted");
		}
	}
	
	@Override
	protected void createContent(Composite container) {
		log("createContent()");

		//--- Model : label + combo box
		createRow1(container);
		
		//--- Button 
		checkModelButton = createButton(container, "Check model", 
				new ButtonCheckModelSelectionAdapter(this));
		
	    //--- Multi-lines text box to print installation result 
		checkModelResultText = new Text(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.READ_ONLY ); // SWT.H_SCROLL
		checkModelResultText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
	    gridData.heightHint = 200; // Fixed vertical size in pixels		
		checkModelResultText.setLayoutData(gridData); // reuse same GridData
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		log("createButtonsForButtonBar()");
		// define buttons to be displayed at the bottom right of the window 
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	
}
