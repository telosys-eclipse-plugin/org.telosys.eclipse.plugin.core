package org.telosys.eclipse.plugin.core.commons;

import java.util.Optional;

import org.eclipse.swt.widgets.Combo;

public class ComboUtil {

	private ComboUtil() {
	}

	public static Optional<ComboItem> getItemByValue(Combo combo, Optional<String> optionalValue) {
        if ( optionalValue.isPresent() ) {
        	String value = optionalValue.get();
            int index = combo.indexOf(value); // Search in values
            if (index >= 0) { // Found in the Combo
            	return Optional.of(new ComboItem(index, value));
            }
        }
        return Optional.empty();
	}
	
	public static Optional<String> selectItem(Combo combo, Optional<ComboItem> optionalItem) {
		if ( optionalItem.isPresent() ) {
			// Found => select item found
			combo.select(optionalItem.get().getIndex());
			return Optional.of(optionalItem.get().getValue());
		}
		else {
			// Not Found => select first item 
			combo.select(0);
			return Optional.empty();
		}
	}
}
