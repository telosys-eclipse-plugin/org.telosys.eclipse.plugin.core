package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ProjectUtil;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.commons.dbcfg.yaml.DatabaseDefinition;

public class ControlCenterTab2 {
	private static final String TAB_TITLE = " Databases ";
	
	private static final int TABLE_STYLE = 
			  SWT.BORDER 
			| SWT.H_SCROLL | SWT.V_SCROLL 
			| SWT.SINGLE | SWT.FULL_SELECTION   // | SWT.HIDE_SELECTION 
			| SWT.NO_FOCUS;
			//| SWT.CHECK ;
	
    private final IProject eclipseProject;
    private final TelosysProject telosysProject; 
	private final Font boldFont;
	
	private Table databasesTable;
	private Table librariesTable;
	private Text  databaseDefinitionText;
	
	public ControlCenterTab2(IProject eclipseProject, Font boldFont) {
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
        //GridLayout gridLayout = new GridLayout(3, true); // 3 columns, equal width
        GridLayout gridLayout = new GridLayout(3, false); // 3 columns, not equal width
        tabContent.setLayout(gridLayout);

        // Databases and Libraries 
        createPart1(tabContent);

        // Database info
        createPart2(tabContent);
        
        // Buttons bar
        createPart3(tabContent);

        //        // Populate Models Combo-Box
//        TelosysCommand.populateModels(telosysProject, modelsCombo);
//        // Populate Bundles Combo-Box
//        TelosysCommand.populateBundles(telosysProject, bundlesCombo);
        
        TelosysCommand.populateDatabases(telosysProject, databasesTable);
        
        return tabContent;
	}
	
	private Composite createPartComposite(Composite tabContent) {
        Composite composite = new Composite(tabContent, SWT.BORDER);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(new GridLayout());
        return composite;
	}
	private Composite createPanel(Composite parent, int numColums) {
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
	private GridData createButtonGridData() {
		return createButtonGridData(SWT.DEFAULT, 0);
	}
	private GridData createButtonGridData(int widthHint) {
		return createButtonGridData(SWT.DEFAULT, widthHint);
	}
	private GridData createButtonGridData(int widthHint, int horizontalAlignment) {
        GridData buttonGridData = new GridData(horizontalAlignment, SWT.CENTER, false, false);
        if ( widthHint > 0 ) {
            buttonGridData.widthHint = widthHint; // Minimum button width (adjust as needed)
        }
		return buttonGridData;
	}
	private Composite createPart1(Composite tabContent) {
        Composite part = createPartComposite(tabContent);
        
        Composite panel = createPanel(part, 3);
        //--- Label
        Label label = new Label(panel, SWT.NONE);
        label.setText("Databases");
        //--- Filler
        Label filler = new Label(panel, SWT.NONE);
        filler.setText(" ");
        filler.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        //--- Button
        Button editDatabases = new Button(panel, SWT.PUSH);
        editDatabases.setText("📝 Edit Databases");
        editDatabases.setLayoutData(createButtonGridData());
        editDatabases.addListener(SWT.Selection, event -> {
        	TelosysCommand.editDatabases(telosysProject);
        });  

        Composite panel2 = createPanel(part, 1);
        databasesTable = createTable(panel2, 226, true); // about 10 rows
        TableUtils.createTableColumn(databasesTable, 120, "Id"); // Column 0
        TableUtils.createTableColumn(databasesTable, 120, "Type"); // Column 1
        TableUtils.createTableColumn(databasesTable, 200, "Name"); // Column 2
        // Selection Listener
        databasesTable.addListener(SWT.Selection, event -> {
            TableItem selectedItem = (TableItem) event.item;
            if (selectedItem != null) {
                System.out.println("Selected Row: " + selectedItem.getText(0) + " - " + selectedItem.getText(1));
                if ( selectedItem.getData() instanceof DatabaseDefinition) {
                    DatabaseDefinition databaseDefinition = (DatabaseDefinition) selectedItem.getData();
                    TelosysCommand.showDatabaseConfig(databaseDefinition, databaseDefinitionText);
                }
                else {
                	DialogBox.showError("Item data is not an instance of 'DatabaseDefinition'");
                }
            }
        });


        Composite panel3 = createPanel(part, 2);
        //--- Label
        label = new Label(panel3, SWT.NONE);
        label.setText("Libraries (JDBC drivers should be here)");
        //--- Filler
        filler = new Label(panel3, SWT.NONE);
        filler.setText(" ");
        filler.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        Composite panel4 = createPanel(part, 1);
        librariesTable = createTable(panel4, 128, false); // about 6 rows
        
        Composite panel5 = createPanel(part, 3);
        Button refresh = new Button(panel5, SWT.PUSH);
        refresh.setText("⟳ Refresh");
        refresh.setLayoutData(createButtonGridData());
        refresh.addListener(SWT.Selection, event -> {
        	TelosysCommand.refreshDatabasesAndLibraries(telosysProject, databasesTable, librariesTable, databaseDefinitionText);
        });  
        //--- Filler
        filler = new Label(panel5, SWT.NONE);
        filler.setText(" ");
        filler.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        //--- Button
        Button newModelFromDB = new Button(panel5, SWT.PUSH);
        newModelFromDB.setText("✨ New Model from Database");
        newModelFromDB.setLayoutData(createButtonGridData());
        
        return part;
	}
	private Table createTable(Composite panel, int tableHeight, boolean showHeaders) {
        Table table = new Table(panel, TABLE_STYLE);
        table.setHeaderVisible(showHeaders);
        table.setLinesVisible(true);

        // Set Table Layout
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        // Make sure the table has N visible rows
//        int rowHeight = 22;
//        int tableHeight = rowHeight * visibleRows; // + table.getHeaderHeight();
        gridData.heightHint = tableHeight;
        table.setLayoutData(gridData);        
        return table;
	}

	private Composite createPart2(Composite tabContent) {
        Composite part = createPartComposite(tabContent);
        
        //Composite panel = createPanel(part, 1);

        // SWT.MULTI: This style allows the Text widget to handle multiple lines of text.
        databaseDefinitionText = new Text(part, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL );
        // Set the layout data for the Text widget
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.widthHint = 400; 
        databaseDefinitionText.setLayoutData(gridData);
        // Create a Font with a fixed-size character font like "Consolas" or "Courier"
        FontData fontData = new FontData("Consolas", 10, SWT.NORMAL); // You can also use "Courier"
        Font font = new Font(tabContent.getDisplay(), fontData);

        // Set the font to the Text widget
        databaseDefinitionText.setFont(font);
        
        databaseDefinitionText.setText("");
        return part;
	}
	private Button createButton(Composite composite, String buttonText) {
	    Button button = new Button(composite, SWT.PUSH);
	    button.setText(buttonText);
        GridData gridData = new GridData();
        gridData.widthHint = 160; // Set the desired width in pixels
        button.setLayoutData(gridData);
//	    button.setLayoutData(createButtonGridData(160));
	    return button;
	}

	private Composite createPart3(Composite tabContent) {
        Composite part = new Composite(tabContent, SWT.BORDER);
        part.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true));
        part.setLayout(new GridLayout());
        
        Composite panel = createPanel(part, 1);
        
        createButton(panel, "Test connection");
        createButton(panel, "Get database info");
        createButton(panel, "Get schemas");
        createButton(panel, "Get catalogs");
        createButton(panel, "Get tables");
        createButton(panel, "Get columns");
        createButton(panel, "Get Primary Keys");
        createButton(panel, "Get Foreign Keys");
        
        return part;
	}
}
