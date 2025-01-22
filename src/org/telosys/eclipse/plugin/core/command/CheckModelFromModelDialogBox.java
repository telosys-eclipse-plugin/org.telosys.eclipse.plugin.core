package org.telosys.eclipse.plugin.core.command;

import java.io.File;

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
import org.telosys.eclipse.plugin.core.commons.Const;

public class CheckModelFromModelDialogBox extends AbstractDialogBox {

	private static final int BOX_WIDTH  = 600;
	private static final int BOX_HEIGHT = 700;

	private final String modelName;
	private final String modelCheckReport;
	
	// UI widgets 
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
	public CheckModelFromModelDialogBox(Shell parentShell, File modelDirectory, String modelCheckReport) {
		super(parentShell, "Check Model", createDialogBoxLayout());
		log("CONSTRUCTOR()");
		this.modelName = modelDirectory.getName();
		this.modelCheckReport = modelCheckReport;
	}
	
	@Override
	protected Point getInitialSize() {
		log("getInitialSize()");
		// Default size : width, height
		return new Point(BOX_WIDTH, BOX_HEIGHT);
	}
    
	private void createRowModel(Composite composite) {
        //--- Label
        Label label = new Label(composite, SWT.NONE);
        label.setText("Model: " + this.modelName);
        label.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	@Override
	protected void createContent(Composite parent) {
		log("createContent()");
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(1, false));  // 1 column
		//--- Model : label + combo box
		createRowModel(composite);

	    //--- Multi-lines text box to print installation result 
		checkModelResultText = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.READ_ONLY ); // SWT.H_SCROLL
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		// No horizontalSpan (just 1 column)
        gridData.widthHint  = Const.CHECK_MODEL_REPORT_SIZE_WIDTH;    // Fixed horizontal size in pixels	
	    gridData.heightHint = Const.CHECK_MODEL_REPORT_SIZE_HEIGHT ;  // Fixed vertical size in pixels		
		checkModelResultText.setLayoutData(gridData); // reuse same GridData
		checkModelResultText.setText(modelCheckReport); // print report 
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		log("createButtonsForButtonBar()");
		// define buttons to be displayed at the bottom right of the window 
		//createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		createButton(parent, IDialogConstants.CANCEL_ID, "Close", false);
	}
	
}
