package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.telosys.eclipse.plugin.commons.Logger;
import org.telosys.eclipse.plugin.core.commons.DialogBox;

public class PopupMenuItemListener implements Listener {

	private final Table table;
	
	public PopupMenuItemListener(Table table) {
		this.table = table;
	}
	
	@Override
	public void handleEvent(Event event) {
		// event:
		//   type = 13 ( Selection = 13 )
		//   widget : org.eclipse.swt.widgets.MenuItem
		Logger.log("PopupMenuItemListener.handleEvent:", event);
		TableItem[] selectedItems =  table.getSelection();
		Logger.log("PopupMenuItemListener: handleEvent: selectedItems.length = " + selectedItems.length );
		if ( selectedItems.length == 1 ) {
			int columnCount = -1;
			// one and only one selected row 
			TableItem tableItem = selectedItems[0];
			String values = "Column values : \n";
			if ( tableItem.getParent() != null ) {
				Table table = tableItem.getParent() ;
				columnCount = table.getColumnCount();
				for ( int i=0; i < columnCount ; i++ ) {
					values = values + " index " + i + " : " + tableItem.getText(i) + "\n";
				}
			}
			DialogBox.showInformation("Edit \n"
					+ "columnCount = " + columnCount + "\n"
					+ values
					+ " data=" + tableItem.getData() );
		}
	}

}
