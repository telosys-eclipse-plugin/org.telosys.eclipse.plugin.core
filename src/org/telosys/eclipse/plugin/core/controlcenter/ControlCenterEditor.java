package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class ControlCenterEditor extends EditorPart {
	
    private Font boldFont;
    private IProject project;
    

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
	
	@Override
	public void createPartControl(Composite parent) {
        // Create a bold font
        Display display = parent.getDisplay();
        FontData[] fontData = display.getSystemFont().getFontData();
        for (FontData fd : fontData) {
            fd.setStyle(SWT.BOLD);
        }
        boldFont = new Font(display, fontData);
        
		ControlCenterUI ui = new ControlCenterUI(project, boldFont);
		ui.createUI(parent);
	}
	
    @Override
    public void dispose() {
        // Dispose of the font when the editor is closed
        if (boldFont != null && !boldFont.isDisposed()) {
            boldFont.dispose();
        }
        super.dispose();
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



}
