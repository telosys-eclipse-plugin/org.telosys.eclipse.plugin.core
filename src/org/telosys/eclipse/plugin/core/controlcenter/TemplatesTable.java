package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.telosys.eclipse.plugin.commons.Logger;
import org.telosys.eclipse.plugin.core.commons.PluginImages;

public class TemplatesTable {

	/**
	 * Creates a Table widget
	 * @param composite
	 * @return
	 */
	protected static Table createTable(Composite composite) {
        Table table = TableUtils.createTable(composite); 

        // Column 0
        TableUtils.createTableColumn(table, 300);
        // Column 1
        TableUtils.createTableColumn(table, 160);
        // Column 2
        TableUtils.createTableColumn(table, 160);

        Logger.log("Table created: table.getColumnCount() = " + table.getColumnCount() );
        return table;
	}
	
	protected static void populateTable(Table table, int n) {
        // Add Rows
        for (int i = 1; i <= n; i++) { // More than 12 rows to enable scrolling
            TableItem tableItem = new TableItem(table, SWT.NONE);
            //item.setText("Item " + i);
            tableItem.setChecked(true); // All checked by default
    		if ( i % 4 == 0 ) {
    			tableItem.setImage( PluginImages.getImage(PluginImages.WARNING ) ) ;
    		}
    		// Set text for each cell of the row (for each column)
    		int colIndex=0;
    		tableItem.setText(colIndex++, "Template Col0:" + i); // Text for Column
    		tableItem.setText(colIndex++, "Template Col1:" + i); // Text for Column       
    		tableItem.setText(colIndex++, "Template Col2:" + i); // Text for Column       

    		//    		String[] rowValues = new String[]{ "Col#0"+i, "Col#1"+i, "Col#2"+i, "Col#3"+i };
//    		String[] rowValues = new String[]{ "Col#0"+i, "Col#1"+i };
//    		tableItem.setText(rowValues);
            tableItem.setData("Template Data" + i); // Any object 
        }
	}
}
