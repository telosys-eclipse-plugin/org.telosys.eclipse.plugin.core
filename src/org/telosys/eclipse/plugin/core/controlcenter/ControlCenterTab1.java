package org.telosys.eclipse.plugin.core.controlcenter;

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
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.telosys.eclipse.plugin.core.commons.ProjectUtil;
import org.telosys.eclipse.plugin.core.commons.Tuple2;
import org.telosys.tools.api.TelosysProject;


public class ControlCenterTab1 {
	
	private static final String TAB_TITLE = " Generation ";
	
    private final IProject eclipseProject;
    private final TelosysProject telosysProject; 
	private final Font boldFont;
	
    private Combo modelsCombo;
    private Table entitiesTable;
    
    private Combo bundlesCombo;
    private Table templatesTable;
    private Button copyStaticFilesCheckBox;
    
    private Button launchGeneration; 
    
	public ControlCenterTab1(IProject eclipseProject, Font boldFont) {
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
        
        // Populate Models Combo-Box
        TelosysCommand.populateModels(telosysProject, modelsCombo);
        // Populate Bundles Combo-Box
        TelosysCommand.populateBundles(telosysProject, bundlesCombo);
        
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
	private Combo createCombo(Composite composite) {
	    Combo combo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
	    combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	    combo.setFont(boldFont);
	    return combo;
	}

	/**
	 * Create the left part : Model and Entities 
	 * @param tabContent
	 * @return
	 */
	private Composite createLeftPart(Composite tabContent) {
        Composite leftPart = createPartComposite(tabContent);
        
        //--- Bar with Label + Combo + Button
        createLeftSubPart1(leftPart);
        
        //--- Bar with Label
        createLeftSubPart2(leftPart);

        //--- Table for ENTITIES
        entitiesTable = TableUtils.createTable(leftPart); 
        TableUtils.createTableColumn(entitiesTable, 300); // Column 0
        TableUtils.createTableColumn(entitiesTable, 160); // Column 1
        TableUtils.createTableColumn(entitiesTable, 160); // Column 2  
		// Popup menu for Right-click -> "Edit entity"
        Listener menuItemListener = new EditEntityMenuItemListener(telosysProject, modelsCombo, entitiesTable);
		Menu menu = TableUtils.createPopupMenu(entitiesTable, "Edit", menuItemListener);
		entitiesTable.setMenu(menu);

        //--- Bar with standards buttons + specific for model
        createLeftSubPart4(leftPart);
        
        return leftPart;
	}
	private Composite createLeftSubPart1(Composite parent) {
		// Create Composite for Bar
		Composite bar = createBarPanel(parent, 3); // 3 columns
        //--- Label
        Label label = new Label(bar, SWT.NONE);
        label.setText("Model");
        //label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        //--- ComboBox
        modelsCombo = createCombo(bar);
        modelsCombo.addSelectionListener( new ComboChangeSelectionListener( 
        		//(modelSelected)-> populateEntitiesTable(modelSelected) )
        		(modelSelected)-> TelosysCommand.populateEntities(telosysProject, modelSelected, entitiesTable) )
        		); 
        //--- Button
        Button editModel = new Button(bar, SWT.PUSH);
        editModel.setText("📝 Edit Model");
        editModel.setLayoutData(createButtonGridData());
        editModel.addListener(SWT.Selection, event -> {
        	TelosysCommand.editModel(telosysProject, modelsCombo);
        });        
        
        return bar;
	}
	private void createLeftSubPart2(Composite leftPart) {
        //--- Bar: Label 
		Composite bar = createBarPanel(leftPart, 1); // 1 column only
        // Label
        Label label = new Label(bar, SWT.NONE);
        label.setText("Entities");
        label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false)); // Align LEFT
	}
	private void createLeftSubPart4(Composite leftPart) {
		//--- Common buttons + 2 buttons
        Tuple2<Composite,GridData> tuple = createButtonsBar(leftPart, entitiesTable, null, new RefreshModelsListener(telosysProject, modelsCombo, entitiesTable), 2);
        Composite buttonBar = tuple.getElement1();
        GridData  buttonGridData = tuple.getElement2();
        //--- Button
        Button newEntity = new Button(buttonBar, SWT.PUSH);
        newEntity.setText("📄 New Entity");
        newEntity.setLayoutData(buttonGridData);
        newEntity.addListener(SWT.Selection, event -> {
        	TelosysCommand.newEntity(telosysProject, modelsCombo);
        });
        //--- Button
        Button checkModel = new Button(buttonBar, SWT.PUSH);
        checkModel.setText("✔ Check Model");
        checkModel.setLayoutData(createButtonGridData());       
        checkModel.addListener(SWT.Selection, event -> {
        	TelosysCommand.checkModel(telosysProject, modelsCombo);
        });        

	}
	
	private Tuple2<Composite,GridData> createButtonsBar(Composite parent, Table table, Button checkBox, Listener refreshListener, int additionalColumns) {
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
        selectAll.setText("🗹 Select All");
        selectAll.setLayoutData(buttonGridData);
        selectAll.addListener(SWT.Selection, event -> {
        	//table.selectAll();
            for (TableItem item : table.getItems()) {
                item.setChecked(true);
            }
            if ( checkBox != null && checkBox.getEnabled() == true) {
            	checkBox.setSelection(true);
            }
        });

        Button deselectAll = new Button(buttonBar, SWT.PUSH);
        deselectAll.setText("☐ Deselect All");
        deselectAll.setLayoutData(buttonGridData);
        deselectAll.addListener(SWT.Selection, event -> {
        	//table.deselectAll();
            for (TableItem item : table.getItems()) {
                item.setChecked(false);
            }
            if ( checkBox != null ) {
            	checkBox.setSelection(false);
            }
        });

        Button refresh = new Button(buttonBar, SWT.PUSH);
        refresh.setText("⟳ Refresh");
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
        createRightSubPart1(rightPart);

        //--- Bar: Label + CheckBox
        createRightSubPart2(rightPart);
        
        //--- Table for TEMPLATES
        templatesTable = TableUtils.createTable(rightPart);
        TableUtils.createTableColumn(templatesTable, 300); // Column 0
        TableUtils.createTableColumn(templatesTable, 160); // Column 1
        TableUtils.createTableColumn(templatesTable, 160); // Column 2
		// Set pop-up menu 
		Menu menu = TableUtils.createPopupMenu(templatesTable, "Edit", new EditTemplateMenuItemListener(telosysProject, bundlesCombo, templatesTable));
		templatesTable.setMenu(menu);
        
        //--- Buttons bar
        createRightSubPart4(rightPart);
        //--- End
        return rightPart;
	}
	private Composite createRightSubPart1(Composite rightPart) {
		// Create Composite for Bar
		Composite bar = createBarPanel(rightPart, 3);
        //--- Label
        Label label = new Label(bar, SWT.NONE);
        label.setText("Bundle");
        //--- ComboBox for BUNDLES
        bundlesCombo = createCombo(bar);
        bundlesCombo.addSelectionListener( new ComboChangeSelectionListener( 
        		//(bundleSelected)-> populateTemplatesTable(bundleSelected) )
        		(bundleSelected)-> TelosysCommand.populateTemplates(telosysProject, bundleSelected, copyStaticFilesCheckBox, templatesTable) )
        		);
        //--- Button
        GridData buttonGridData = createButtonGridData();
        Button editBundle = new Button(bar, SWT.PUSH);
        editBundle.setText("📝 Edit Bundle");
        editBundle.setLayoutData(buttonGridData);
        editBundle.addListener(SWT.Selection, 
        		(event) -> TelosysCommand.editBundle(telosysProject, bundlesCombo) );  
        return bar;
	}
	private void createRightSubPart2(Composite rightPart) {
        //--- Bar: Label + CheckBox
		Composite bar = createBarPanel(rightPart, 2);
        // Label
        Label label = new Label(bar, SWT.NONE);
        label.setText("Templates");
        label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false)); // Align LEFT
        // Check Box
        copyStaticFilesCheckBox = new Button(bar, SWT.CHECK);
        copyStaticFilesCheckBox.setText("Copy static files");
        copyStaticFilesCheckBox.setSelection(false); // Set state to not checked
        copyStaticFilesCheckBox.setEnabled(false); // Disabled by default
        copyStaticFilesCheckBox.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false)); // Align RIGHT and grabExcessHorizontalSpace
	}
	private void createRightSubPart4(Composite rightPart) {
		//--- Common buttons 
		RefreshBundlesListener refreshListener = new RefreshBundlesListener(telosysProject, bundlesCombo, templatesTable, copyStaticFilesCheckBox);
        //Tuple2<Composite,GridData> tuple = createButtonBar(rightPart, templatesTable, copyStaticFilesCheckBox, refreshListener, 0);
        createButtonsBar(rightPart, templatesTable, copyStaticFilesCheckBox, refreshListener, 0);
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
//        Button newModel = new Button(bottomPart, SWT.PUSH);
//        newModel.setText("➕ New Model");
//        newModel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        Button newModel = createBottomButton(bottomPart, "➕ New Model", SWT.LEFT);
        newModel.addListener(SWT.Selection, event -> {
        	TelosysCommand.newModel(telosysProject, modelsCombo);
        });        

        //--- Button (LEFT-aligned)
//        Button installModel = new Button(bottomPart, SWT.PUSH);
//        installModel.setText("📥 Install Model");
//        installModel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        Button installModel = createBottomButton(bottomPart, "📥 Install Model", SWT.LEFT);
        installModel.addListener(SWT.Selection, event -> {
        	TelosysCommand.installModels(telosysProject, modelsCombo);
        });        
        
        //--- Button (centered)
        launchGeneration = new Button(bottomPart, SWT.PUSH);
        launchGeneration.setText("🚀 Launch generation ");
        //launchGeneration.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
		GridData gridData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
		gridData.widthHint = 180; // Minimum button width 
		launchGeneration.setLayoutData(gridData);
        launchGeneration.addListener(SWT.Selection, event -> {
        	launchGeneration.setEnabled(false);
        	boolean copyStaticFiles = copyStaticFilesCheckBox.getSelection();
        	TelosysCommand.launchGeneration(eclipseProject, modelsCombo, entitiesTable, bundlesCombo, templatesTable, copyStaticFiles);
        	launchGeneration.setEnabled(true);
        });  
        
        //--- Button (RIGHT-aligned)
//        Button newBundle = new Button(bottomPart, SWT.PUSH);
//        newBundle.setText("New Bundle");
//        newBundle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        Button newBundle = createBottomButton(bottomPart, "➕ New Bundle", SWT.RIGHT);
//        Label tmpLabel = new Label(bottomPart, SWT.NONE);
//        tmpLabel.setText("                "); // not visible
//        tmpLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

        //--- Button (RIGHT-aligned)
//        Button installBundle = new Button(bottomPart, SWT.PUSH);
//        installBundle.setText("📥 Install Bundle");
//        installBundle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        Button installBundle = createBottomButton(bottomPart, "📥 Install Bundle", SWT.RIGHT);
        installBundle.addListener(SWT.Selection, event -> {
        	TelosysCommand.installBundles(telosysProject, bundlesCombo);
        });  
        return bottomPart;
	}
	private Button createBottomButton(Composite composite, String buttonText, int horizontalAlignment) {
		Button button = new Button(composite, SWT.PUSH);
		button.setText(buttonText);
		GridData gridData = new GridData(horizontalAlignment, SWT.CENTER, false, false);
		gridData.widthHint = 140; // Minimum button width 
		button.setLayoutData(gridData);
		return button;
	}
}
