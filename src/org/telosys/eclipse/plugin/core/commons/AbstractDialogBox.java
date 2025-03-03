package org.telosys.eclipse.plugin.core.commons;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public abstract class AbstractDialogBox extends Dialog {
	
	
	private static final Logger LOGGER = Logger.getLogger(AbstractDialogBox.class.getName()); 
	static {
		LOGGER.setLevel(Level.ALL);
	}
	
	protected void log(String msg) {
		if ( Config.LOG_FROM_DIALOG_BOX ) {
			System.out.println("[LOG-DialogBox] - " + msg);
		}
	}
	protected boolean isValidName(String name) {
		if ( name == null ) return false;
		if ( name.isEmpty() ) return false;
		if ( name.isBlank() ) return false;
		return true;
	}

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
	
	protected abstract void createContent(Composite container) ;

    @Override
    public int open() {
		log("open() : before call super.open()");
        int result = super.open();  // Show the dialog (MODAL)
        // 
		log("open() : after call super.open() => Window is closed");
        //afterDisplayingDialog();    // Call the method after dialog is opened
        return result;
    }
    
	// overriding this methods allows to set the title of the custom dialog
	@Override
	protected void configureShell(Shell shell) {
		log("configureShell()");
		super.configureShell(shell);
		shell.setText(title);
	}  
		
	@Override
	protected Control createDialogArea(Composite parent) {
		log("createDialogArea()");
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

}
