package org.telosys.eclipse.plugin.core.commons.dialogbox;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.telosys.eclipse.plugin.core.commons.WorkbenchUtil;

public class ResultDialogBox extends Dialog {
	
    private final String windowTitle;
    private final String resultTitle;
    private final String resultDetail;
    
    public ResultDialogBox(String windowTitle, String resultTitle, String resultDetail) {
        super(WorkbenchUtil.getActiveWindowShell());
        this.windowTitle = windowTitle;
        this.resultTitle = resultTitle;
        this.resultDetail = resultDetail;
    }
    public ResultDialogBox(String windowTitle, String resultDetail) {
        super(WorkbenchUtil.getActiveWindowShell());
        this.windowTitle = windowTitle;
        this.resultTitle = "";
        this.resultDetail = resultDetail;
    }
    
    private int countLines(String text) {
    	int count = 0 ;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n') {
                count++;
            }
        }
        return count;
    }
    
    @Override
    protected Control createDialogArea(Composite parent) {
    	// Dialog box window title
    	getShell().setText(windowTitle);
    	
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new GridLayout(1, false));
        
        //--- Label
        if ( resultTitle != null && ! resultTitle.isEmpty() ) {
            Label label = new Label(container, SWT.NONE);
            label.setText(resultTitle);
        }
        //--- Text box for result
        Text text = new Text(container, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.widthHint  = 600;
        // Count the number of lines
        int initialHeigth = countLines(resultDetail) * 20;
        if ( initialHeigth < 400 ) {
        	initialHeigth = 400;
        }
        else if ( initialHeigth > 800 ) {
        	initialHeigth = 800;
        }
        gridData.heightHint = initialHeigth;
        text.setLayoutData(gridData);        
        text.setText(resultDetail);
        
        return container;
    }

    @Override
    protected boolean isResizable() {
        return true;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, OK, "Close", true);
    }
}