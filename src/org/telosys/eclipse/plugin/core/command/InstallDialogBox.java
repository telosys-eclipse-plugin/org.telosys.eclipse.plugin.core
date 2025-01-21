package org.telosys.eclipse.plugin.core.command;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.telosys.eclipse.plugin.core.commons.AbstractDialogBox;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ProjectUtil;
import org.telosys.tools.api.InstallationType;
import org.telosys.tools.commons.depot.DepotElement;
import org.telosys.tools.commons.depot.DepotRateLimit;
import org.telosys.tools.commons.depot.DepotResponse;

public class InstallDialogBox extends AbstractDialogBox {

	private static final int BOX_WIDTH  = 600;
	private static final int BOX_HEIGHT = 700;

	private final IProject project;
	private final String   depotFromConfiguration;
	private final InstallationType installationType;
	private final String   elementName; // What to install ("model" or "bundle")
	
	// UI widgets 
	private Text   depotText;
	private List   elementsFoundList;
	private Text   elementsFoundText;
	private Button installButton;
	private Text   installResultText;

	private java.util.List<DepotElement> currentDepotElements = new LinkedList<>(); // initial state = void list to avoid null

	private static Layout createDialogBoxLayout() {
		GridLayout layout = new GridLayout(1, false);
		layout.marginRight = 5;
		layout.marginLeft = 10;
		return layout;
	}

	private static String getElementName(InstallationType installationType) {
		switch (installationType) {
		case BUNDLE: return "bundle";
		case MODEL:  return "model";
		default:     return "(unknown-element)";
		}
	}

	/**
	 * Constructor
	 * @param parentShell
	 * @param project
	 * @param depot
	 * @param installationType
	 */
	public InstallDialogBox(Shell parentShell, IProject project, String depot, InstallationType installationType) {
		super(parentShell, "Install "+getElementName(installationType), createDialogBoxLayout());
		log("CONSTRUCTOR()");
		this.project = project;
		this.installationType = installationType;
		this.elementName = getElementName(installationType);
		this.depotFromConfiguration = depot;
	}
	
	@Override
	public void create() {
		log("create() : before call super.create()");
		super.create();
		log("create() : after call super.create()");
//		// Init the window
//		log("create() : set focus");
//		// getFromDepotButton.forceFocus(); // NOK
//		// getShell().setDefaultButton(getFromDepotButton); // NOK
//		// Explicitly set focus to the Button when the shell opens
//		getShell().addListener(SWT.Activate, e -> { 
//			System.out.println("Shell activated (via Listener SWT.Activate)!"); 
//			//getFromDepotButton.setFocus(); // NOK
//			getFromDepotButton.forceFocus(); // NOK
//			} );
		
//		getShell().setTabList(new Control[] {elementsFromDepotButton, elementsFoundList}); // Error Widget has the wrong parent
	}	
	@Override
	protected Point getInitialSize() {
		log("getInitialSize()");
		// Default size : width, height
		return new Point(BOX_WIDTH, BOX_HEIGHT);
	}
    
	static class ButtonGetFromDepotSelectionAdapter extends SelectionAdapter {
        private final InstallDialogBox dialogBox;
        public ButtonGetFromDepotSelectionAdapter(InstallDialogBox dialogBox) {
            this.dialogBox = dialogBox;
        }
        @Override
        public void widgetSelected(SelectionEvent e) {
            // Button clicked
        	this.dialogBox.getElementsFromDepotAndPopulateList();
        }
    }
    
	static class ButtonInstallFromDepotSelectionAdapter extends SelectionAdapter {
        private final InstallDialogBox dialogBox;
        public ButtonInstallFromDepotSelectionAdapter(InstallDialogBox dialogBox) {
            this.dialogBox = dialogBox;
        }
        @Override
        public void widgetSelected(SelectionEvent e) {
            // Button clicked
        	this.dialogBox.installElementsFromDepot();
        }
    }
    
	private void createRow1(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(2, false));  // 2 columns with no margin between them

        //--- Label
        Label label = new Label(container, SWT.NONE);
        label.setText("Depot:");
        label.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT));

        //--- Input field
        depotText = new Text(container, SWT.BORDER);
        GridData data = new GridData();  
        data.grabExcessHorizontalSpace = true;
        data.widthHint = 300;
        depotText.setLayoutData(data);
        depotText.setText(depotFromConfiguration);
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
	
	private void getElementsFromDepotAndPopulateList() {
		String depot = depotText.getText();
		if ( depot != null && !depot.isEmpty() ) {
			getElementsFromDepotAndPopulateList(depot);
		}
		else {
			DialogBox.showWarning("Depot is required");
		}
	}
	private void getElementsFromDepotAndPopulateList(String depot) {
		GetDepotElementsTask getDepotElementsTask = new GetDepotElementsTask(project, depot);
		runTask(getDepotElementsTask);
		Optional<String> taskError = getDepotElementsTask.getError();
		if ( taskError.isEmpty() ) {
			// No error => Populate list with task result
			populateList(getDepotElementsTask.getDepotResponse());
		}
		else {
			// Error
			DialogBox.showError("Cannot get elements from depot", taskError.get());
		}
	}

	private void installElementsFromDepot() {
		String depot = depotText.getText();
		if ( depot != null && !depot.isEmpty() ) {
			java.util.List<DepotElement> selectedElements = getSelectedElements();
			if ( selectedElements != null && !selectedElements.isEmpty() ) {
				installResultText.setText(""); // clear result text
				InstallDepotElementsTask task = new InstallDepotElementsTask(project, depot, selectedElements, installationType);
				runTask(task);
				String result = task.getResult();
				installResultText.setText(result); // clear result text
				if ( task.getInstallationsCount() > 0 ) {
					ProjectUtil.refresh(project);
				}
			}
			else {
				DialogBox.showInformation("Nothing selected (nothing to install).");
			}
		}
		else {
			DialogBox.showInformation("Depot is required!");
		}
	}
	private java.util.List<DepotElement> getSelectedElements() {
		String[] selectedElements = elementsFoundList.getSelection();
		if ( selectedElements != null && selectedElements.length > 0 && !currentDepotElements.isEmpty()) {
			java.util.List<String> selectedElementsList = Arrays.asList(selectedElements);
			return currentDepotElements.stream()
						.filter(element -> selectedElementsList.contains(element.getName()) )
						.collect(Collectors.toList());
		}
		return new LinkedList<>();
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

		//--- Depot : label + input field
		createRow1(container);
		
		//--- Button for getting elements from depot
		createButton(container, "Get " + elementName + "s from depot", 
				new ButtonGetFromDepotSelectionAdapter(this));
		
        //--- List of models/bundles available in the depot   
		elementsFoundList = new List(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL );
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
	    gridData.heightHint = 200; // Fixed vertical size in pixels
	    elementsFoundList.setLayoutData(gridData);
		
		//--- Text for message 2 (must look like Label)
	    elementsFoundText = createMessage(container);

		//--- Button for installation
	    installButton = createButton(container, "Install selected " + elementName + "s", 
										new ButtonInstallFromDepotSelectionAdapter(this));
	    installButton.setEnabled(false);

	    //--- Multi-lines text box to print installation result 
		installResultText = new Text(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.READ_ONLY ); // SWT.H_SCROLL
		installResultText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		installResultText.setLayoutData(gridData); // reuse same GridData
	}
	private Text createMessage(Composite container) { // (must look like Label)
	    Text msgText = new Text(container, SWT.READ_ONLY);
	    msgText.setBackground(container.getBackground()); // Match the parent background
	    msgText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		//message.setBorderVisible(false); // Remove the border
	    return msgText;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		log("createButtonsForButtonBar()");
		// define buttons to be displayed at the bottom right of the window 
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	
	private String buildStatusMessage(DepotResponse depotResponse) {
		int nb = depotResponse.getElements().size();
		String plural = nb > 1 ? "s" : "";
		DepotRateLimit rateLimit = depotResponse.getRateLimit();
		return nb + " " + elementName + plural + " found in depot"
				+ "          "
				+ "( API rate limit: " + rateLimit.getRemaining() + "/" + rateLimit.getLimit() + " )" ;
	}
	private void populateList(DepotResponse depotResponse) {
		elementsFoundList.removeAll();
		for ( DepotElement e : depotResponse.getElements() ) {
			elementsFoundList.add( e.getName() ); 
			// List widget does not directly support tooltips for individual items
		}
		currentDepotElements = depotResponse.getElements();
		elementsFoundText.setText(buildStatusMessage(depotResponse));
	    installButton.setEnabled(!depotResponse.getElements().isEmpty());
	}
}
