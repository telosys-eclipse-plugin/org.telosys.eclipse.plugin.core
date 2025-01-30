package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.telosys.eclipse.plugin.commons.Logger;
import org.telosys.eclipse.plugin.core.commons.PluginImages;

public class EntitiesTable {

	/**
	 * Creates a Table widget for ENTITIES
	 * @param composite
	 * @return
	 */
	protected static Table createTable(Composite composite) {
        Table table = TableUtils.createTable(composite); 

        // Column 0
        TableUtils.createTableColumn(table, 300);
        // Column 1
        TableUtils.createTableColumn(table, 160);

        Logger.log("Table created: table.getColumnCount() = " + table.getColumnCount() );
        return table;
	}
	
	protected static void populateTable(Table table, int n) {
        // Add Rows
        for (int i = 1; i <= n; i++) { // More than 12 rows to enable scrolling
            TableItem item = new TableItem(table, SWT.NONE);
            //item.setText("Item " + i);
            item.setChecked(true); // All checked by default
    		if ( i % 4 == 0 ) {
    			item.setImage( PluginImages.getImage(PluginImages.WARNING ) ) ;
    		}
            item.setText(0, "Entity Col-0:" + i); // Text for Column 0
            item.setText(1, "Entity Col-1:" + i); // Text for Column 1          
            item.setData("EntityData-row#" + i); // Any object 
        }
	}
	
}
