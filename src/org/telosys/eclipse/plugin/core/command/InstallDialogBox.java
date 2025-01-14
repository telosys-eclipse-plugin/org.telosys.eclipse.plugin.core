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
import org.telosys.tools.api.InstallationType;
import org.telosys.tools.commons.depot.DepotElement;
import org.telosys.tools.commons.depot.DepotRateLimit;
import org.telosys.tools.commons.depot.DepotResponse;

public class InstallDialogBox extends AbstractDialogBox {

	private static final int BOX_WIDTH  = 600;
	private static final int BOX_HEIGHT = 500;

	private final IProject project;
	private final String   depotFromConfiguration;
	private final InstallationType installationType;
	private final String   elementName; // "model" or "bundle"
	
	// UI widgets 
	private Text  depotText;
	private List  elementsList;
	private Text  message1Text;
	private Text  message2Text;

	private java.util.List<DepotElement> currentDepotElements = new LinkedList<>(); // initial state = void list to avoid null

	private static Layout createDialogBoxLayout() {
		GridLayout layout = new GridLayout(1, false);
		layout.marginRight = 5;
		layout.marginLeft = 10;
		return layout;
	}
//	private static String getTitle(InstallationType installationType) {
//		String s = "Install ";
//		switch (installationType) {
//		case BUNDLE: return s + "bundle";
//		case MODEL:  return s + "model";
//		default:     return s;
//		}
//	}
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
		this.project = project;
		this.installationType = installationType;
		this.elementName = getElementName(installationType);
		this.depotFromConfiguration = depot;
	}
	
	@Override
	protected Point getInitialSize() {
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
    
	protected void createRow1(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(3, false));  // 3 columns with no margin between them

        Label label = new Label(container, SWT.NONE);
        label.setText("Depot:");
        label.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT));

        // Text that fills all the remaining space
        depotText = new Text(container, SWT.BORDER);
        //text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        GridData data = new GridData();  
        data.grabExcessHorizontalSpace = true;
        //data.minimumWidth = 120;
        data.widthHint = 300;
        depotText.setLayoutData(data);
        depotText.setText(depotFromConfiguration);

        // Button with width based on its text
        Button button = new Button(container, SWT.PUSH);
        button.setText("Get " + elementName + "s from depot");
        GridData buttonData = new GridData(SWT.DEFAULT, SWT.DEFAULT); 
        buttonData.horizontalAlignment = SWT.END;
        button.setLayoutData(buttonData);
        // Add selection listener to handle button click
        button.addSelectionListener(new ButtonGetFromDepotSelectionAdapter(this));
        
//        // Compute Text width 
//        int totalWidth = container.getSize().x;  // Total width of the container
//        int labelWidth = label.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;  // Width of Label
//        int buttonWidth = button.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;  // Width of Button
//        int remainingWidth = totalWidth - labelWidth - buttonWidth;  // Remaining width for Text

        // Set the computed width for Text
//        GridData textData = new GridData(SWT.FILL, SWT.CENTER);
//		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
//	    gridData.heightHint = 200; // Fixed vertical size in pixels
//        textData.widthHint = remainingWidth;
//        GridData data =  new GridData();
//         data.horizontalAlignment = SWT.FILL;
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
//		ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(getShell()) ;
//		try {
//			// Run task to get elements
//			progressMonitorDialog.run(false, false, getDepotElementsTask);
//			Optional<String> taskError = getDepotElementsTask.getError();
//			if ( taskError.isEmpty() ) {
//				// No error => Populate list with task result
//				populateList(getDepotElementsTask.getDepotResponse());
//			}
//			else {
//				// Error
//				DialogBox.showError("Cannot get elements from depot", taskError.get());
//			}
//		} catch (InvocationTargetException e) {
//			DialogBox.showError("Error during task", e.getMessage());
//		} catch (InterruptedException e) {
//			DialogBox.showInformation("Task interrupted");
//		}
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
		DialogBox.showInformation("installElementsFromDepot()");
		String depot = depotText.getText();
		if ( depot != null && !depot.isEmpty() ) {
			java.util.List<DepotElement> selectedElements = getSelectedElements();
			if ( selectedElements != null && !selectedElements.isEmpty() ) {
				InstallDepotElementsTask task = new InstallDepotElementsTask(project, depot, selectedElements, installationType);
				runTask(task);
				String result = task.getResult();
				DialogBox.showInformation(result);
			}
			else {
				DialogBox.showInformation("No selection, nothing to install.");
			}
		}
		else {
			DialogBox.showInformation("Depot is required!");
		}
	}
	private java.util.List<DepotElement> getSelectedElements() {
		String[] selectedElements = elementsList.getSelection();
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
	
	//    private void runGetDepotElementsTask(GetDepotElementsTaskWithProgress task, Shell shell) {
//		ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(shell) ;
//		try {
//			progressMonitorDialog.run(false, false, task);
//			populateList(task.getElements());
//		} catch (InvocationTargetException e) {
//			DialogBox.showError("Error during task", e.getMessage());
//		} catch (InterruptedException e) {
//			DialogBox.showInformation("Task interrupted");
//		}
//    }
	@Override
	protected void createContent(Composite container) {
		
		createRow1(container);
		
//        //--- Button with width based on its text
//        Button button = new Button(container, SWT.PUSH);
//        button.setText("Get models from depot");
//        button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
//        // Add selection listener to handle button click
//        button.addSelectionListener(new ButtonSelectionAdapter(this));
		
		
		//--- Text for message 1
	    message1Text = createMessage(container);

        //--- List of models/bundles available in the depot   
		elementsList = new List(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL );
//		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.horizontalAlignment = SWT.LEFT ;
//		gd.verticalAlignment   = SWT.TOP ;
//		gd.heightHint  = 200 ;
//		//gd.widthHint   = 300 ;
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
	    gridData.heightHint = 200; // Fixed vertical size in pixels
	    elementsList.setLayoutData(gridData);
		
//	    elementsList.add("Loading elements from depot...");
//	    elementsList.add("Please wait");
		
		//--- Text for message 2 (must look like Label)
//	    message2Text = new Text(container, SWT.READ_ONLY);
//	    message2Text.setBackground(container.getBackground()); // Match the parent background
//	    message2Text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
//		//message.setBorderVisible(false); // Remove the border
	    message2Text = createMessage(container);

		Button button2 = new Button(container, SWT.PUSH);
	    button2.setText("Install selected " + elementName + "s");
		button2.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT));
		button2.addSelectionListener(new ButtonInstallFromDepotSelectionAdapter(this));				
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
		// set specific labels for the buttons
		//createButton(parent, IDialogConstants.OK_ID,     "Install", true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	
//	@Override
//	protected void afterDisplayingDialog() {
//		DialogBox.showInformation("afterDisplayingDialog");
//	}
	
//	@Override
//	protected void okPressed() {
////		modelName = inputField.getText();
//		//TODO: Launch installation
//		super.okPressed();
//	}
	
	private String buildStatusMessage(DepotResponse depotResponse) {
		int nb = depotResponse.getElements().size();
		String plural = nb > 1 ? "s" : "";
		DepotRateLimit rateLimit = depotResponse.getRateLimit();
		return nb + " " + elementName + plural + " found in depot"
				+ "          "
				+ "( API rate limit: " + rateLimit.getRemaining() + "/" + rateLimit.getLimit() + " )" ;
	}
	private void populateList(DepotResponse depotResponse) {
		message1Text.setText( depotResponse.getElements().size() + " element(s) found in depot");
		elementsList.removeAll();
		for ( DepotElement e : depotResponse.getElements() ) {
			elementsList.add( e.getName() ); 
			// List widget does not directly support tooltips for individual items
		}
		currentDepotElements = depotResponse.getElements();
//		DepotRateLimit rateLimit = depotResponse.getRateLimit();
		//message2Text.setText( "API rate limit: " + rateLimit.getRemaining() + "/" + rateLimit.getLimit() );
		message2Text.setText(buildStatusMessage(depotResponse));
	}
}
