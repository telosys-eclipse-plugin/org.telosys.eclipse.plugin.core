package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class ControlCenterTab3 {
	
	private static final String TAB_TITLE = "Generation";
	
    private final IProject project;
    
	public ControlCenterTab3(IProject project) {
		super();
		this.project = project;
	}

	protected TabItem createTab(TabFolder tabFolder) {
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(TAB_TITLE);
//		Composite tabContent = new Composite(tabFolder, SWT.NONE);
//		createTabContent(tabContent, "Tab content 1");
		tabItem.setControl(createTabContent(tabFolder));
		return tabItem;
	}
	
	private Composite createTabContent(TabFolder tabFolder) {
		Composite tabContent = new Composite(tabFolder, SWT.NONE);
		
        // Use GridLayout with 2 equal columns
        GridLayout gridLayout = new GridLayout(2, true); // 2 columns, equal width
        tabContent.setLayout(gridLayout);

        // Left Section
//        Composite leftPart = new Composite(tabContent, SWT.BORDER);
//        leftPart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//        leftPart.setLayout(new GridLayout());
//        Label leftLabel = new Label(leftPart, SWT.NONE);
//        leftLabel.setText("Left Side Content");
        createLeftPart(tabContent);

        // Right Section
//        Composite rightPart = new Composite(tabContent, SWT.BORDER);
//        rightPart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//        rightPart.setLayout(new GridLayout());
//        Label rightLabel = new Label(rightPart, SWT.NONE);
//        rightLabel.setText("Right Side Content");
        createRightPart(tabContent);
        
//		tabContent.setLayout(new GridLayout(1, false)); // One column layout
//		new Label(tabContent, SWT.NONE).setText("Project " + this.project.getName());
//		Button button = new Button(tabContent, SWT.PUSH);
//        button.setText("Click Me");
        
        // Bottom Section (Spanning Both Columns)
        Composite bottomPart = new Composite(tabContent, SWT.BORDER);
        GridData bottomGridData = new GridData(SWT.FILL, SWT.BOTTOM, true, false, 2, 1);
        bottomPart.setLayoutData(bottomGridData);
        bottomPart.setLayout(new GridLayout(1, false)); // 1 column to center content

        // Button centered inside Bottom Section
        Button centeredButton = new Button(bottomPart, SWT.PUSH);
        centeredButton.setText("Centered Button");
        centeredButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
        
        return tabContent;
	}
	private Composite createLeftPart(Composite tabContent) {
        Composite leftPart = new Composite(tabContent, SWT.BORDER);
        leftPart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        leftPart.setLayout(new GridLayout());
        //--- Label
        Label label = new Label(leftPart, SWT.NONE);
        label.setText("Model");
        //--- ComboBox
        Combo combo = new Combo(leftPart, SWT.DROP_DOWN | SWT.READ_ONLY);
        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        combo.setItems(new String[]{"Option 1", "Option 2", "Option 3"}); // Example options
        combo.select(0); // Select first option by default
        //--- Label
        label = new Label(leftPart, SWT.NONE);
        label.setText("Entities");

//        //--- Table(with Checkboxes, Scrollbar, and Multi-Selection)
//        Table table = new Table(leftPart, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
//        table.setHeaderVisible(false);
//        table.setLinesVisible(true);
//
//        // Make sure the table has 12 visible rows
//        int rowHeight = table.getItemHeight();
//        int visibleRows = 12;
//        int tableHeight = rowHeight * visibleRows + table.getHeaderHeight();
//
//        // Set Table Layout
//        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
//        gridData.heightHint = tableHeight;
//        table.setLayoutData(gridData);
//
//        // Define One Column
//        TableColumn column = new TableColumn(table, SWT.LEFT);
//        column.setWidth(250); // Set column width
//
//        // Add Rows
//        for (int i = 1; i <= 20; i++) { // More than 12 rows to enable scrolling
//            TableItem item = new TableItem(table, SWT.NONE);
//            item.setText("Item " + i);
//            item.setData("Data" + i); // Any object 
//        }
        //--- Table for ENTITIES
        EntitiesTable.createTable(leftPart);
        return leftPart;
	}

	private Composite createRightPart(Composite tabContent) {
        Composite rightPart = new Composite(tabContent, SWT.BORDER);
        rightPart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        rightPart.setLayout(new GridLayout());
        Label rightLabel = new Label(rightPart, SWT.NONE);
        rightLabel.setText("Right Side Content");
        return rightPart;
	}
	private Composite createBottomPart(Composite tabContent) {
        // Bottom Section (Spanning Both Columns)
        Composite bottomPart = new Composite(tabContent, SWT.BORDER);
        bottomPart.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 2, 1));
        bottomPart.setLayout(new GridLayout(1, false)); // 1 column to center content
        // Button centered inside Bottom Section
        Button centeredButton = new Button(bottomPart, SWT.PUSH);
        centeredButton.setText("Generate");
        centeredButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
        return bottomPart;
	}
}
