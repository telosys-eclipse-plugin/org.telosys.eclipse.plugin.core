package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
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

        // Left Part (Models and Entities)
        createLeftPart(tabContent);

        // Right Part (Bundles and Templates)
        createRightPart(tabContent);
        
        // Bottom Part (Button)
        createBottomPart(tabContent);
        
        return tabContent;
	}

	private Composite createPartComposite(Composite tabContent) {
        Composite composite = new Composite(tabContent, SWT.BORDER);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(new GridLayout());
        return composite;
	}
	private Composite createLeftPart(Composite tabContent) {
        Composite leftPart = createPartComposite(tabContent);
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
        //--- Table for ENTITIES
        Table table = EntitiesTable.createTable(leftPart);
        EntitiesTable.populateTable(table, 20);
        //--- Buttons bar
        createButtonBar(leftPart, table, new RefreshModelListener(table));
        return leftPart;
	}
	
	private void createButtonBar(Composite parent, Table table, Listener refreshListener) {
		// Create Composite for Button Bar
        Composite buttonBar = new Composite(parent, SWT.NONE);
//        RowLayout rowLayout = new RowLayout(SWT.HORIZONTAL);
//        rowLayout.spacing = 10;  // Space between buttons
//        rowLayout.marginLeft = 0;
//        buttonBar.setLayout(rowLayout);
        
        // Use GridLayout with 3 columns
        GridLayout gridLayout = new GridLayout(3, true); // true = equal column width
        gridLayout.marginWidth = 0; // 10;  // Margin around the buttons
        gridLayout.marginHeight = 0; // 10;
        gridLayout.horizontalSpacing = 10; // Space between buttons
        buttonBar.setLayout(gridLayout);        
        // Ensure all buttons have equal width
        GridData buttonGridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        buttonGridData.widthHint = 120; // Minimum button width (adjust as needed)

        // Create Buttons
        Button selectAll = new Button(buttonBar, SWT.PUSH);
        selectAll.setText("Select All");
        selectAll.setLayoutData(buttonGridData);
        selectAll.addListener(SWT.Selection, event -> {
        	//table.selectAll();
            for (TableItem item : table.getItems()) {
                item.setChecked(true);
            }
        });

        Button deselectAll = new Button(buttonBar, SWT.PUSH);
        deselectAll.setText("Deselect All");
        deselectAll.setLayoutData(buttonGridData);
        deselectAll.addListener(SWT.Selection, event -> {
        	table.deselectAll();
            for (TableItem item : table.getItems()) {
                item.setChecked(false);
            }
        });

        Button refresh = new Button(buttonBar, SWT.PUSH);
        refresh.setText("Refresh");
        refresh.setLayoutData(buttonGridData);
        refresh.addListener(SWT.Selection, refreshListener);
	}

	private Composite createRightPart(Composite tabContent) {
        Composite rightPart = createPartComposite(tabContent);
        //--- Label
        Label label = new Label(rightPart, SWT.NONE);
        label.setText("Bundle");
        //--- ComboBox for BUNDLES
        Combo combo = new Combo(rightPart, SWT.DROP_DOWN | SWT.READ_ONLY);
        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        combo.setItems(new String[]{"Bundle 1", "Bundle 2", "Bundle 3"}); // Example options
        combo.select(0); // Select first option by default
        //--- Label
        label = new Label(rightPart, SWT.NONE);
        label.setText("Templates");
        //--- Table for TEMPLATES
        Table table = TemplatesTable.createTable(rightPart);
        TemplatesTable.populateTable(table, 20);
        //--- Buttons bar
        createButtonBar(rightPart, table, new RefreshBundleListener(table));
        return rightPart;
	}
	
	private Composite createBottomPart(Composite tabContent) {
        // Bottom Section (Spanning Both Columns)
        Composite bottomPart = new Composite(tabContent, SWT.BORDER);
        bottomPart.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 2, 1));
        bottomPart.setLayout(new GridLayout(1, false)); // 1 column to center content
        // Button centered inside Bottom Section
        Button centeredButton = new Button(bottomPart, SWT.PUSH);
        centeredButton.setText("Launch generatation");
        centeredButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
        return bottomPart;
	}
}
