package org.telosys.eclipse.plugin.core.controlcenter;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
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
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ProjectUtil;
import org.telosys.eclipse.plugin.core.commons.TelosysApiException;
import org.telosys.eclipse.plugin.core.commons.TelosysEvolution;
import org.telosys.eclipse.plugin.core.commons.Tuple2;
import org.telosys.tools.api.TelosysProject;

public class ControlCenterTab3 {
	
	private static final String TAB_TITLE = "Generation";
	
    private final IProject project;
    private final TelosysProject telosysProject; 
	private final Font boldFont;
    private Combo modelsCombo;
    private Table entitiesTable;
    
	public ControlCenterTab3(IProject project, Font boldFont) {
		super();
		this.project = project;
		this.telosysProject = ProjectUtil.getTelosysProject(project);
		this.boldFont = boldFont;
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
        
        populateModelsCombo();
        
        return tabContent;
	}

	private Composite createPartComposite(Composite tabContent) {
        Composite composite = new Composite(tabContent, SWT.BORDER);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(new GridLayout());
        return composite;
	}
	
	private Composite createLeftBar1(Composite parent) {
		// Create Composite for Bar
		Composite bar = new Composite(parent, SWT.NONE);
		// Use GridLayout with N columns
		GridLayout gridLayout = new GridLayout(4, false);
		gridLayout.marginWidth = 0; // 10; // Margin around the widgets
		gridLayout.marginHeight = 0; // 10;
		gridLayout.horizontalSpacing = 10; // Space between widgets
		bar.setLayout(gridLayout);
		bar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        //--- Label
        Label label = new Label(bar, SWT.NONE);
        label.setText("Model");
        //label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        //--- ComboBox
        modelsCombo = new Combo(bar, SWT.DROP_DOWN | SWT.READ_ONLY);
        GridData comboGridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        comboGridData.widthHint = 100; // Set minimum width (e.g., 150 pixels)
        modelsCombo.setLayoutData(comboGridData);
        
        modelsCombo.setFont(boldFont);
        modelsCombo.addSelectionListener( new ComboChangeSelectionListener( 
        		(modelSelected)-> populateEntitiesList(modelSelected) )
        		); 
        // Ensure all buttons have equal width
        GridData buttonGridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        buttonGridData.widthHint = 120; // Minimum button width (adjust as needed)
        
        //--- Button
        Button editModel = new Button(bar, SWT.PUSH);
        editModel.setText("Edit Model");
        editModel.setLayoutData(buttonGridData);
        //--- Button
        Button checkModel = new Button(bar, SWT.PUSH);
        checkModel.setText("Check Model");
        checkModel.setLayoutData(buttonGridData);       
        return bar;
	}

	private Composite createLeftPart(Composite tabContent) {
        Composite leftPart = createPartComposite(tabContent);
        
        createLeftBar1(leftPart);
        
//        //--- Label
//        Label label = new Label(leftPart, SWT.NONE);
//        label.setText("Model");
//        //--- ComboBox
//        modelsCombo = new Combo(leftPart, SWT.DROP_DOWN | SWT.READ_ONLY);
//        modelsCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
//        modelsCombo.setFont(boldFont);
//        modelsCombo.addSelectionListener( new ComboChangeSelectionListener( 
//        		(modelSelected)-> populateEntitiesList(modelSelected) )
//        		); 
        //--- Label
        Label label;
        label = new Label(leftPart, SWT.NONE);
        label.setText("Entities");

        //--- Table for ENTITIES
        entitiesTable = EntitiesTable.createTable(leftPart);
        
        //--- Buttons bar
        Tuple2<Composite,GridData> tuple = createButtonBar(leftPart, entitiesTable, new RefreshModelListener(entitiesTable), 1);
        Composite buttonBar = tuple.getElement1();
        GridData  buttonGridData = tuple.getElement2();
        
//        Button checkModel = new Button(buttonBar, SWT.PUSH);
//        checkModel.setText("Check Model");
//        checkModel.setLayoutData(buttonGridData);

        Button newEntity = new Button(buttonBar, SWT.PUSH);
        newEntity.setText("New Entity");
        newEntity.setLayoutData(buttonGridData);

        return leftPart;
	}
	
	private Tuple2<Composite,GridData> createButtonBar(Composite parent, Table table, Listener refreshListener, int additionalColumns) {
		// Create Composite for Button Bar
        Composite buttonBar = new Composite(parent, SWT.NONE);
        // Use GridLayout with 3 columns
        GridLayout gridLayout = new GridLayout(3 + additionalColumns, true); // true = equal column width
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
        
        return new Tuple2<>(buttonBar, buttonGridData);
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
        createButtonBar(rightPart, table, new RefreshBundleListener(table), 0);
        return rightPart;
	}
	
	private Composite createBottomPart(Composite tabContent) {
        // Bottom Section (Spanning Both Columns)
        Composite bottomPart = new Composite(tabContent, SWT.BORDER);
        bottomPart.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 2, 1));
        bottomPart.setLayout(new GridLayout(5, false)); // 5 columns for 5 widgets 
        
        // Left-aligned buttons
        Button btn1 = new Button(bottomPart, SWT.PUSH);
        btn1.setText("New Model");
        btn1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

        Button newModel = new Button(bottomPart, SWT.PUSH);
        newModel.setText("Install Model");
        newModel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        
        // Button centered inside Bottom Section
        Button centeredButton = new Button(bottomPart, SWT.PUSH);
        centeredButton.setText(" Launch generation ");
        centeredButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
        
        // Right-aligned buttons
        Button btn5 = new Button(bottomPart, SWT.PUSH);
        btn5.setText("New Bundle");
        btn5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

        Button installBundle = new Button(bottomPart, SWT.PUSH);
        installBundle.setText("Install Bundle");
        installBundle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        
        
        return bottomPart;
	}
	
	private void populateModelsCombo() {
		List<String> models = telosysProject.getModelNames();
		models.add(0, ""); // First element is void = no model
        modelsCombo.setItems(models.toArray(new String[0]));
        modelsCombo.select(0); // Select first model by default
	}

	private void populateEntitiesList(String modelName) {
		// Clear table (remove all rows)
		entitiesTable.removeAll();
		try {
			List<String> entities = TelosysEvolution.getEntities(telosysProject, modelName);
	        // Add Rows
	        for (String entityName : entities ) { 
	            TableItem item = new TableItem(entitiesTable, SWT.NONE);
	            item.setChecked(true); // All checked by default
	            item.setText(0, entityName); // Text for Column 0
//	            item.setText(1, "Entity Col-1:" + i); // Text for Column 1          
	            item.setData(entityName); // Any object 
	        }
		} catch (TelosysApiException e) {
			DialogBox.showError(e.getMessage());
		}
	}
}
