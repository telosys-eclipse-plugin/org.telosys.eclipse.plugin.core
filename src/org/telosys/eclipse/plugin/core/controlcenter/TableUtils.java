package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.telosys.eclipse.plugin.commons.Logger;

public class TableUtils {

	/*
	 * Selection: 
	 *  SWT.MULTI (multiple line selection) or SWT.SINGLE (single line selection)
	 *  SWT.FULL_SELECTION (full row selection => a full line should be drawn)
	 *  SWT.HIDE_SELECTION (behavior when the widget loses focus )
	 *     HIDE_SELECTION hides the selection highlight whenever focus is not in the Table. However when you click on the Table it will still show.
	 */
	public static final int TABLE_STYLE = 
			  SWT.BORDER 
			| SWT.H_SCROLL | SWT.V_SCROLL 
			| SWT.SINGLE | SWT.FULL_SELECTION | SWT.HIDE_SELECTION 
			| SWT.NO_FOCUS
			| SWT.CHECK ;
	
	
//	public static final int TABLE_STYLE = 
//			  SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.NO_FOCUS | SWT.CHECK ;
// NO FOCUS = no effect 
	
	
	/**
	 * Creates a standard table in the given composite
	 * @param composite
	 * @return
	 */
	protected static Table createTable(Composite composite) {
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
        
        // Listeners 
		table.addListener(SWT.Selection, createSelectionListener()); 
		
		// Popup menu 
		Menu menu = createPopupMenu(table, "My menu action");
		table.setMenu(menu);
		
        return table;
	}
	
	/**
	 * Creates a column in the given table
	 * @param table
	 * @param width
	 * @return
	 */
	protected static TableColumn createTableColumn(Table table, int width) {
        TableColumn tableColumn = new TableColumn(table, SWT.LEFT);
        tableColumn.setWidth(width); // Set column width
        return tableColumn;
	}
	
	private static Listener createSelectionListener() {
		return new Listener() {
			@Override
			public void handleEvent(Event event) {
				Logger.log("TableSelectionListener.handleEvent:", event);
				// event.widget : org.eclipse.swt.widgets.Table
				// event.item   : org.eclipse.swt.widgets.TableItem
		        TableItem item = (TableItem) event.item;
			    if (event.detail == SWT.CHECK) {
			    	// when user click on the checkbox located on the left of the table row 
			    	Logger.log("Selection listener: click on Checkbox : " + item.getText() + " | Checked: " + item.getChecked());
			    } else {
			    	// when user click on the row or on the image if any but out of the checkbox ( detail = 0 ) 
			    	Logger.log("Selection listener: click out of Checkbox : " + item.getText()  + " | Checked: " + item.getChecked());
				    boolean isCtrlKeyPressed = (event.stateMask & SWT.CTRL) != 0;
				    if ( isCtrlKeyPressed ) {
				    	// Ctrl-click => Toggle checkbox state
				    	item.setChecked(!item.getChecked()); 
				    }
			    }
			}
		};
	}
	
	private static Menu createPopupMenu(Table table, String menuItemText) {
		//--- Menu
        Menu contextMenu = new Menu(table);
        //--- Menu Item
        MenuItem menuItem = new MenuItem(contextMenu, SWT.NONE);
        menuItem.setText(menuItemText);
        menuItem.addListener(SWT.Selection, new PopupMenuItemListener(table));
        
        return contextMenu ;
	}
}
