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
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.commons.bundles.TargetsDefinitions;


public class ControlCenterTab3 {
	
	private static final String TAB_TITLE = "Generation";
	
    private final IProject eclipseProject;
    private final TelosysProject telosysProject; 
	private final Font boldFont;
    private Combo modelsCombo;
    private Table entitiesTable;
    private Combo bundlesCombo;
    private Table templatesTable;
    private Button launchGeneration; 
    
	public ControlCenterTab3(IProject eclipseProject, Font boldFont) {
		super();
		this.eclipseProject = eclipseProject;
		this.telosysProject = ProjectUtil.getTelosysProject(eclipseProject);
		this.boldFont = boldFont;
	}

	protected TabItem createTab(TabFolder tabFolder) {
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(TAB_TITLE);
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
        populateBundlesCombo();
        
        return tabContent;
	}

	private Composite createPartComposite(Composite tabContent) {
        Composite composite = new Composite(tabContent, SWT.BORDER);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(new GridLayout());
        return composite;
	}
	
	private GridData createButtonGridData() {
        // Ensure all buttons have equal width
        GridData buttonGridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        buttonGridData.widthHint = 120; // Minimum button width (adjust as needed)
		return buttonGridData;
	}
	private Composite createBarPanel(Composite parent, int numColums) {
		// Create Composite for Bar
		Composite bar = new Composite(parent, SWT.NONE);
		// Use GridLayout with N columns
		GridLayout gridLayout = new GridLayout(numColums, false);
		gridLayout.marginWidth = 0; // 10; // Margin around the widgets
		gridLayout.marginHeight = 0; // 10;
		gridLayout.horizontalSpacing = 10; // Space between widgets
		bar.setLayout(gridLayout);
		bar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		return bar;
	}

	/**
	 * Create the left part : Model and Entities 
	 * @param tabContent
	 * @return
	 */
	private Composite createLeftPart(Composite tabContent) {
        Composite leftPart = createPartComposite(tabContent);
        
        //--- Bar with Label + Combo + Buttons
        createLeftBar1(leftPart);
        
        //--- Label
        Label label = new Label(leftPart, SWT.NONE);
        label.setText("Entities");

        //--- Table for ENTITIES
        entitiesTable = TableUtils.createTable(leftPart); 
        TableUtils.createTableColumn(entitiesTable, 300); // Column 0
        TableUtils.createTableColumn(entitiesTable, 160); // Column 1

        //--- Bar with standards buttons + specific for model
        createLeftBar2(leftPart);
        
        return leftPart;
	}
	private Composite createLeftBar1(Composite parent) {
		// Create Composite for Bar
		Composite bar = createBarPanel(parent, 4);
        //--- Label
        Label label = new Label(bar, SWT.NONE);
        label.setText("Model");
        //label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        //--- ComboBox
        modelsCombo = new Combo(bar, SWT.DROP_DOWN | SWT.READ_ONLY);
        modelsCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));        
        modelsCombo.setFont(boldFont);
        modelsCombo.addSelectionListener( new ComboChangeSelectionListener( 
        		(modelSelected)-> populateEntitiesTable(modelSelected) )
        		); 
        //--- Button
        Button editModel = new Button(bar, SWT.PUSH);
        editModel.setText("Edit Model");
        editModel.setLayoutData(createButtonGridData());
        editModel.addListener(SWT.Selection, event -> {
        	TelosysCommand.editModel(telosysProject, modelsCombo);
        });        
        
        //--- Button
        Button checkModel = new Button(bar, SWT.PUSH);
        checkModel.setText("Check Model");
        checkModel.setLayoutData(createButtonGridData());       
        checkModel.addListener(SWT.Selection, event -> {
        	TelosysCommand.checkModel(telosysProject, modelsCombo);
        });        
        return bar;
	}
	private void createLeftBar2(Composite leftPart) {
		//--- Common buttons + 1 button 
        Tuple2<Composite,GridData> tuple = createButtonBar(leftPart, entitiesTable, new RefreshModelListener(entitiesTable), 1);
        Composite buttonBar = tuple.getElement1();
        GridData  buttonGridData = tuple.getElement2();
        //--- 
        Button newEntity = new Button(buttonBar, SWT.PUSH);
        newEntity.setText("New Entity");
        newEntity.setLayoutData(buttonGridData);
        newEntity.addListener(SWT.Selection, event -> {
        	TelosysCommand.newEntity(telosysProject, modelsCombo);
        });        
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

	/**
	 * Create the right part : Bundle and Templates 
	 * @param tabContent
	 * @return
	 */
	private Composite createRightPart(Composite tabContent) {
        Composite rightPart = createPartComposite(tabContent);
        //--- Label + Combo + Button(s)
        createRightBar1(rightPart);
        //--- Label
        Label label = new Label(rightPart, SWT.NONE);
        label.setText("Templates");
        //--- Table for TEMPLATES
        templatesTable = TableUtils.createTable(rightPart);
        TableUtils.createTableColumn(templatesTable, 300); // Column 0
        TableUtils.createTableColumn(templatesTable, 160); // Column 1
        TableUtils.createTableColumn(templatesTable, 160); // Column 2
        //--- Buttons bar
        createButtonBar(rightPart, templatesTable, new RefreshBundleListener(templatesTable), 0);
        //--- End
        return rightPart;
	}
	private Composite createRightBar1(Composite parent) {
		// Create Composite for Bar
		Composite bar = createBarPanel(parent, 3);
		
        //--- Label
        Label label = new Label(bar, SWT.NONE);
        label.setText("Bundle");
        
        //--- ComboBox for BUNDLES
        bundlesCombo = new Combo(bar, SWT.DROP_DOWN | SWT.READ_ONLY);
        bundlesCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        bundlesCombo.setFont(boldFont);
        bundlesCombo.addSelectionListener( new ComboChangeSelectionListener( 
        		(bundleSelected)-> populateTemplatesTable(bundleSelected) )
        		);

        //--- Button
        GridData buttonGridData = createButtonGridData();
        Button editBundle = new Button(bar, SWT.PUSH);
        editBundle.setText("Edit Bundle");
        editBundle.setLayoutData(buttonGridData);
        editBundle.addListener(SWT.Selection, event -> {
        	TelosysCommand.editBundle(telosysProject, bundlesCombo);
        });  
        return bar;
	}
	
	/**
	 * Create the bottom part : set of buttons ( New xxx, Install xxx and Launch Generation )
	 * @param tabContent
	 * @return
	 */
	private Composite createBottomPart(Composite tabContent) {
        // Bottom Section (Spanning Both Columns)
        Composite bottomPart = new Composite(tabContent, SWT.BORDER);
        bottomPart.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 2, 1));
        bottomPart.setLayout(new GridLayout(5, false)); // 5 columns for 5 widgets 
        
        //--- Button (LEFT-aligned)
        Button newModel = new Button(bottomPart, SWT.PUSH);
        newModel.setText("New Model");
        newModel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        newModel.addListener(SWT.Selection, event -> {
        	TelosysCommand.newModel(telosysProject);
        });        

        //--- Button (LEFT-aligned)
        Button installModel = new Button(bottomPart, SWT.PUSH);
        installModel.setText("Install Model");
        installModel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        installModel.addListener(SWT.Selection, event -> {
        	TelosysCommand.installModel(telosysProject);
        });        
        
        //--- Button (centered)
        launchGeneration = new Button(bottomPart, SWT.PUSH);
        launchGeneration.setText(" Launch generation ");
        launchGeneration.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
        launchGeneration.addListener(SWT.Selection, event -> {
        	launchGeneration.setEnabled(false);
        	TelosysCommand.launchGeneration(eclipseProject, modelsCombo, entitiesTable, bundlesCombo, templatesTable, true);
        	launchGeneration.setEnabled(true);
        });  
        
        //--- Button (RIGHT-aligned)
//        Button newBundle = new Button(bottomPart, SWT.PUSH);
//        newBundle.setText("New Bundle");
//        newBundle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        Label tmpLabel = new Label(bottomPart, SWT.NONE);
        tmpLabel.setText("                "); // not visible
        tmpLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

        //--- Button (RIGHT-aligned)
        Button installBundle = new Button(bottomPart, SWT.PUSH);
        installBundle.setText("Install Bundle");
        installBundle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        installBundle.addListener(SWT.Selection, event -> {
        	TelosysCommand.installBundle(telosysProject);
        });  
        return bottomPart;
	}
	
	private void populateModelsCombo() {
		List<String> models = telosysProject.getModelNames();
		models.add(0, ""); // First element is void = no model
        modelsCombo.setItems(models.toArray(new String[0]));
        modelsCombo.select(0); // Select first model by default
	}
	private void populateEntitiesTable(String modelName) {
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

	private void populateBundlesCombo() {
		List<String> bundles = telosysProject.getBundleNames();
		bundles.add(0, ""); // First element is void = no model
        bundlesCombo.setItems(bundles.toArray(new String[0]));
        bundlesCombo.select(0); // Select first model by default
	}
	private void populateTemplatesTable(String bundleName) {
		// Clear table (remove all rows)
		templatesTable.removeAll();
		if ( bundleName.isBlank() || bundleName.isEmpty() ) {
			return ;
		}
		else {
			try {
				TargetsDefinitions targets = telosysProject.getTargetDefinitions(bundleName);
		        // Add Rows
		        for (TargetDefinition target : targets.getTemplatesTargets() ) { 
		            TableItem item = new TableItem(templatesTable, SWT.NONE);
		            item.setChecked(true); // All checked by default
		            item.setText(0, target.getTemplate()); // Text for Column 0
		            item.setText(1, target.getId()); // Text for Column 1          
		            item.setData(target); // Any object 
		        }
			} catch (Exception e) {
				DialogBox.showError(e.getMessage());
			}
		}
	}
}
