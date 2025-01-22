package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class ControlCenterEditorInput implements IEditorInput {

    private final IProject project;

    /**
     * Constructor
     * @param project
     */
    public ControlCenterEditorInput(IProject project) {
        this.project = project;
    }

    public IProject getProject() {
        return project;
    }

    @Override
    public boolean exists() {
        return project.exists();
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return null; // Provide an image descriptor if needed
    }

    @Override
    public String getName() {
        return project.getName();
    }

    @Override
    public IPersistableElement getPersistable() {
        return null;
    }

    @Override
    public String getToolTipText() {
        return "Editor for project: " + project.getName();
    }

    @Override
    public <T> T getAdapter(Class<T> adapter) {
        return null;
    }
}

