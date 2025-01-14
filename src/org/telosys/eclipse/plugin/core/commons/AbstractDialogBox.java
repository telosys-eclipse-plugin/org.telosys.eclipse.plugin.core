package org.telosys.eclipse.plugin.core.commons;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public abstract class AbstractDialogBox extends Dialog {
	
	private final String title ;
	private final Layout layout ;
	private Composite container ;
	
	private Layout createDefaultLayout() {
		GridLayout layout = new GridLayout(2, false);
		layout.marginRight = 5;
		layout.marginLeft = 10;
		return layout;
	}
	
	protected GridData getColSpan(int n) {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = n;
		return gd;
	}
	
	/**
	 * Constructor to create a new DialogBox with the default layout : GridLayout with 2 columns
	 * @param parentShell
	 * @param title
	 */
	public AbstractDialogBox(Shell parentShell, String title) {
		super(parentShell);
		this.title = title ;
		this.layout = createDefaultLayout() ;
	}

	/**
	 * Constructor to create a new DialogBox with a specific layout
	 * @param parentShell
	 * @param title
	 * @param layout
	 */
	public AbstractDialogBox(Shell parentShell, String title, Layout layout) {
		super(parentShell);
		this.title = title;
		this.layout = layout;
	}
	
//    @Override
//    public int open() {
//        int result = super.open();  // Show the dialog
//        afterDisplayingDialog();    // Call the method after dialog is opened
//        return result;
//    }
    
	protected abstract void createContent(Composite container) ;
	
//	protected void afterDisplayingDialog() {
//		// Designed to be overriden in subclass if necessary
//	}
	
	// overriding this methods allows to set the title of the custom dialog
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(title);
	}  
		
	@Override
	protected Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);
//		GridLayout layout = new GridLayout(2, false);
//		layout.marginRight = 5;
//		layout.marginLeft = 10;
		container.setLayout(layout);
		createContent(container); // abstract method implemented in child component
		return container;
	}

	protected Text createLabelAndField(String labelText) {
		return createLabelAndField(labelText, "", SWT.BORDER) ;
	}

	protected Text createLabelAndField(String labelText, String initialValue) {
		return createLabelAndField(labelText, initialValue, SWT.BORDER) ;
	}
	
	protected Text createLabelAndField(String labelText, String fieldValue, int style) {
		// label
		Label label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		label.setText(labelText);
		// input field
		Text txt = new Text(container, style);
		txt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt.setText(fieldValue);		
		return txt ;
	}
	
	@Override
	protected Point getInitialSize() {
		// Default size : width, height
		return new Point(450, 200);
	}

}
