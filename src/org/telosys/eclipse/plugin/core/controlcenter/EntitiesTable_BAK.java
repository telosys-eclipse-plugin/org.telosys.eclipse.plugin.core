package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.telosys.eclipse.plugin.core.commons.PluginImages;

public class EntitiesTable_BAK {

	/**
	 * Creates a Table widget with Checkboxes, Scrollbar, and Multi-Selection
	 * @param composite
	 * @return
	 */
	protected static Table createTable(Composite composite) {
        //--- Table(with Checkboxes, Scrollbar, and Multi-Selection)
        //Table table = new Table(composite, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
        Table table = new Table(composite, TableConst.TABLE_STYLE);
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

        
//        table.addListener(SWT.Selection, event -> {
//            if (event.detail != SWT.CHECK) { // Allow checkbox interaction but prevent row selection
//                table.deselectAll();
//            }
//        });        
		table.addListener(SWT.Selection, event -> {
			System.out.println("Listener SWT.Selection: event " + event );
	        TableItem item = (TableItem) event.item;
		    if (event.detail == SWT.CHECK) {
		        System.out.println("Checkbox toggled: " + item.getText() + " | Checked: " + item.getChecked());
		        table.deselectAll();
		    } else {
		        System.out.println("Row selected: " + item.getText()  + " | Checked: " + item.getChecked());
		    }			
		});
        
//        // Add Double-Click and Ctrl-Click Listeners
//        table.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseDoubleClick(MouseEvent event) {
//                edit(table.getItem(new Point(event.x, event.y)));
//            }
//            @Override
//            public void mouseDown(MouseEvent event) {
//                if ((event.stateMask & SWT.MOD1) != 0) { // Check if Ctrl key is pressed (SWT.MOD1 = CTRL)
//                    edit(table.getItem(new Point(event.x, event.y)));
//                }
//                else {
//                	TableItem item = table.getItem(new Point(event.x, event.y));
//                	if ( item != null ) {
//                    	item.setChecked(!item.getChecked()); // Toggle checkbox state on click
//                	}
//                }
//            }
//        });

        table.addListener(SWT.MouseDown, event -> {
            if (event.button != 1 || event.count > 1) { // Ignore right-clicks (button 3) and double-clicks (count > 1)
                return;
            }

            Point point = new Point(event.x, event.y);
            TableItem item = table.getItem(point); // Get clicked row

            if (item != null) {
                Rectangle bounds = item.getBounds(0); // Get checkbox column bounds (column index 0)
                if (bounds.contains(point)) {
                    item.setChecked(!item.getChecked()); // Toggle checkbox state
                }
            }
        });
        
     // Create a context menu
        Menu contextMenu = new Menu(table);
        MenuItem menuItem = new MenuItem(contextMenu, SWT.NONE);
        menuItem.setText("Perform Action");

        // Add a listener to detect right-clicks and show the menu
        table.addListener(SWT.MenuDetect, event -> {
            Point point = table.toControl(event.x, event.y); // Convert to table coordinates
            TableItem item = table.getItem(point); // Get the clicked row

            if (item != null) {
                table.setMenu(contextMenu); // Attach menu only if a row is clicked
                menuItem.addListener(SWT.Selection, e -> {
    		        table.deselectAll();
                    System.out.println("Action performed on: " + item.getText());
                });
            } else {
                table.setMenu(null); // No menu if not clicking on a row
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
	protected static void populateTable(Table table, int n) {
        // Add Rows
        for (int i = 1; i <= n; i++) { // More than 12 rows to enable scrolling
            TableItem item = new TableItem(table, SWT.NONE);
            //item.setText("Item " + i);
            item.setChecked(true); // All entities checked by default
    		if ( i % 4 == 0 ) {
    			item.setImage( PluginImages.getImage(PluginImages.WARNING ) ) ;
    		}
            item.setText(0, "Item Col1 " + i); // Text for Column 1
            item.setText(1, "Item Col2 " + i); // Text for Column 2            
            item.setText(2, "Item Col3 " + i); // Text for Column 3            
            item.setData("Data" + i); // Any object 
        }
	}
	
//    /**
//     * Returns a "warning image" if the entity has warnings, 
//     *  or null if none "entity icon" if no warning
//     * @param entity
//     * @return
//     */
//    protected static Image getEntityWarningImage(int i) {
////		if ( hasWarnings(entity) ) {
//		if ( i % 4 == 0 ) {
//			return PluginImages.getImage(PluginImages.WARNING ) ; // 16 pix
//		}
//		else {
//			// do not return null to keep same TableItem checkbox size
//			//return PluginImages.getImage(PluginImages.BLANK ); // 16 pix
//			return null;
//		}
//    }

}
