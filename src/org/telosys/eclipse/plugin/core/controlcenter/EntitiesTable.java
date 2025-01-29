package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class EntitiesTable {

	/**
	 * Creates a Table widget with Checkboxes, Scrollbar, and Multi-Selection
	 * @param composite
	 * @return
	 */
	protected static Table createTable(Composite composite) {
        //--- Table(with Checkboxes, Scrollbar, and Multi-Selection)
        Table table = new Table(composite, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
        table.setHeaderVisible(false);
        table.setLinesVisible(true);

        // Make sure the table has 12 visible rows
        int rowHeight = table.getItemHeight();
        int visibleRows = 12;
        int tableHeight = rowHeight * visibleRows + table.getHeaderHeight();

        // Set Table Layout
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.heightHint = tableHeight;
        table.setLayoutData(gridData);

        // Define One Column
        TableColumn column1 = new TableColumn(table, SWT.LEFT);
        column1.setWidth(400); // Set column width
        // Define One Column
        TableColumn column2 = new TableColumn(table, SWT.LEFT);
        column2.setWidth(300); // Set column width

        // Add Rows
        for (int i = 1; i <= 20; i++) { // More than 12 rows to enable scrolling
            TableItem item = new TableItem(table, SWT.NONE);
            //item.setText("Item " + i);
            item.setText(0, "Item Col1 " + i); // Item for Column 1
            item.setText(1, "Itel Col2 " + i); // Item for Column 2            
            item.setData("Data" + i); // Any object 
        }
        
        // Add Double-Click Listener
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent event) {
//                Point point = new Point(event.x, event.y);
//                TableItem item = table.getItem(point); // Get item under cursor
//                if (item != null) {
//                    String rowData = (String) item.getData();
//                    if (rowData != null) {
//                        System.out.println("Double-clicked on: " + rowData );
//                    }
//                }
//                edit(table.getItem(point));
            	
                edit(table.getItem(new Point(event.x, event.y)));
            }
            @Override
            public void mouseDown(MouseEvent event) {
                if ((event.stateMask & SWT.MOD1) != 0) { // Check if Ctrl key is pressed (SWT.MOD1 = CTRL)
//                    Point point = new Point(event.x, event.y);
//                    TableItem item = table.getItem(point); // Get item under cursor
//
//                    if (item != null) {
//                        RowData rowData = (RowData) item.getData();
//                        if (rowData != null) {
//                            System.out.println("Ctrl + Clicked on: " + rowData.getName() + " (ID: " + rowData.getId() + ")");
//                        }
//                    }
                    edit(table.getItem(new Point(event.x, event.y)));
                }
            }
        });
        return table;
	}
	
	protected static void edit(TableItem item) {
        if (item != null) {
            String rowData = (String) item.getData();
            if (rowData != null) {
                System.out.println("Edit entity: " + rowData );
            }
        }
        else {
            System.out.println("TableItem is null");
        }
	}
}
