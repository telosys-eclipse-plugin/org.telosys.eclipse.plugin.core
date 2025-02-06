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
	private static final int TABLE_STYLE = 
			  SWT.BORDER 
			| SWT.H_SCROLL | SWT.V_SCROLL 
			| SWT.SINGLE | SWT.FULL_SELECTION | SWT.HIDE_SELECTION 
			| SWT.NO_FOCUS
			| SWT.CHECK ;
	
	
	/**
	 * Creates a standard table in the given composite
	 * @param composite
	 * @return
	 */
	protected static Table createTable(Composite composite) {
        Table table = new Table(composite, TABLE_STYLE);
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
	
	/**
	 * Creates a listener to manage row selection and update checkbox state 
	 * @return
	 */
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
	
	protected static Menu createPopupMenu(Table table, String editMenuItemText, Listener editMenuItemListener) {
		//--- Menu
        Menu menu = new Menu(table);
        //--- Menu Item
        MenuItem menuItem = new MenuItem(menu, SWT.NONE);
        menuItem.setText(editMenuItemText);
        menuItem.addListener(SWT.Selection, editMenuItemListener);
        
        return menu ;
	}
	
	protected static TableItem getSingleSelectedRow(Table table) {
		TableItem[] selectedItems =  table.getSelection();
		if ( selectedItems.length == 1 ) {
			// one and only one selected row 
			return selectedItems[0];
		}
		return null;
	}
	protected static String getSingleSelectedRowDataAsString(Table table) {
		TableItem tableItem = getSingleSelectedRow(table);
		if ( tableItem != null ) {
			Object data = tableItem.getData();
			if ( data instanceof String ) {
				return (String)data;
			}
		}
		return null;
	}	
}
