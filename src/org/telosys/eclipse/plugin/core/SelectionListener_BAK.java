package org.telosys.eclipse.plugin.core;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

public class SelectionListener_BAK implements ISelectionListener{

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		System.out.println("selectionChanged: ISelection = " + selection);
		
	}

}
