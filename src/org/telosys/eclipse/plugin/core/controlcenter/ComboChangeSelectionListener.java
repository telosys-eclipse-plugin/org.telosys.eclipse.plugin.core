package org.telosys.eclipse.plugin.core.controlcenter;

import java.util.function.Consumer;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.telosys.eclipse.plugin.core.commons.DialogBox;

public class ComboChangeSelectionListener extends SelectionAdapter {

	private final Consumer<String> action;
	
    public ComboChangeSelectionListener(Consumer<String> action) {
		super();
		this.action = action;
	}

	@Override
    public void widgetSelected(SelectionEvent e) {
    	if ( e.widget instanceof Combo ) {
    		Combo combo = (Combo)e.widget;
            String selected = combo.getText();
            if ( ! selected.equals( combo.getData() ) ) {
            	// Selected element has changed 
            	// 1) Update current element 
                combo.setData(selected);
            	// 2) Launch processing for the new selected element
                action.accept(selected);
            }
    	}
    	else {
            DialogBox.showError("Unexpected widget in event (widget is not a Combo)");
    	}
    }

}
