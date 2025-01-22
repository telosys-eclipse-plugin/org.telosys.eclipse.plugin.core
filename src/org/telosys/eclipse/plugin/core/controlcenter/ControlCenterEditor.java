package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class ControlCenterEditor extends EditorPart {
	
    private IProject project;
    
	@Override
	public void createPartControl(Composite parent) {
		// Create your SWT widgets here
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false)); // Example layout

		// Example: Label and Text Input Field
		new Label(container, SWT.NONE).setText("Project Name:");
		Text projectNameText = new Text(container, SWT.BORDER);
		projectNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// Example: Combo Box
		new Label(container, SWT.NONE).setText("Options:");
		Combo combo = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		combo.setItems(new String[] { "Option 1", "Option 2", "Option 3" });
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	}

	@Override
	public void setFocus() {
		// Set focus logic if needed
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// Handle saving logic
	}

	@Override
	public void doSaveAs() {
		// Handle save-as logic
	}

	@Override
	public boolean isDirty() {
		// Return true if the editor's state is dirty (unsaved changes)
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);

		// Validate and extract project from the input
		if (input instanceof ControlCenterEditorInput) {
			this.project = ((ControlCenterEditorInput) input).getProject();
	        // Set editor tab caption and tooltip
	        setPartName(this.project.getName());
	        setTitleToolTip("Editing project: " + this.project.getFullPath().toString());
		} else {
			throw new PartInitException("Invalid input: Must be a ProjectEditorInput");
		}
	}

}
