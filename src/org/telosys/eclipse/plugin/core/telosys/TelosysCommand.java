package org.telosys.eclipse.plugin.core.telosys;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Logger;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.telosys.eclipse.plugin.core.commons.ComboItem;
import org.telosys.eclipse.plugin.core.commons.ComboUtil;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ProjectExplorerUtil;
import org.telosys.eclipse.plugin.core.commons.ProjectUtil;
import org.telosys.eclipse.plugin.core.commons.WorkbenchUtil;
import org.telosys.eclipse.plugin.core.commons.WorkspaceUtil;
import org.telosys.eclipse.plugin.core.commons.dialogbox.CheckModelFromModelDialogBox;
import org.telosys.eclipse.plugin.core.commons.dialogbox.InstallDialogBox;
import org.telosys.eclipse.plugin.core.commons.dialogbox.NewEntityFromModelDialogBox;
import org.telosys.eclipse.plugin.core.commons.dialogbox.NewModelDialogBox;
import org.telosys.eclipse.plugin.core.commons.dialogbox.ResultDialogBox;
import org.telosys.eclipse.plugin.core.telosys.commons.TelosysConsole;
import org.telosys.eclipse.plugin.core.telosys.commons.TelosysConsoleType;
import org.telosys.tools.api.InstallationType;
import org.telosys.tools.api.MetaDataOptionsImpl;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.commons.bundles.TargetsDefinitions;
import org.telosys.tools.commons.dbcfg.yaml.DatabaseDefinition;
import org.telosys.tools.commons.dbcfg.yaml.DatabaseDefinitions;

public class TelosysCommand {
	private static final Logger LOGGER = Logger.getLogger(TelosysCommand.class.getName());
	
	private static final String TELOSYS_API_ERROR = "Telosys API error";

	private static boolean isValidName(String name) {
		if ( name == null ) return false;
		if ( name.isEmpty() ) return false;
		if ( name.isBlank() ) return false;
		return true;
	}
	private static boolean checkNotNull(Object... args) {
		int i = 0 ;
		for ( Object arg : args ) {
			i++;
			if ( arg == null ) {
	    		DialogBox.showError("Parameter #" + i + " is null! "); 
	    		return false;
			}
		}
		return true;
	}	
	private static Optional<String> getCurrentSelection(Combo modelsCombo){
		String modelName = modelsCombo.getText();
		if ( modelName.isBlank() || modelName.isEmpty() ) {
			return Optional.empty();
		}
		else {
			return Optional.of(modelName);
		}
	}
	
	//---------------------------------------------------------------------------------------
	// MODELS and ENTITIES
	//---------------------------------------------------------------------------------------
	public static void populateModels(TelosysProject telosysProject, Combo modelsCombo) {
		populateModels(telosysProject, modelsCombo, Optional.empty());
	}
	private static Optional<String> populateModels(TelosysProject telosysProject, Combo modelsCombo, Optional<String> optionalCurrentModel) {
        LOGGER.fine("populateModels ( optionalCurrentModel = '" + optionalCurrentModel +"' )");
		// Populate combo box
		List<String> models = telosysProject.getModelNames();
		models.add(0, ""); // First element is void = no model
        modelsCombo.setItems(models.toArray(new String[0])); // All models 
        // Re-select current model if any
        Optional<ComboItem> optionalItem = ComboUtil.getItemByValue(modelsCombo, optionalCurrentModel);
//		if ( optionalItem.isPresent() ) {
//			// Found => select it
//			modelsCombo.select(optionalItem.get().getIndex());
//			return Optional.of(optionalItem.get().getValue());
//		}
//		else {
//			// Not Found => select first (blank)
//			modelsCombo.select(0);
//			return Optional.empty();
//		}
		return ComboUtil.selectItem(modelsCombo, optionalItem);
	}
	public static void populateEntities(TelosysProject telosysProject, String modelName, Table entitiesTable) {
		Optional<String> optionalModelName = isValidName(modelName) ? Optional.of(modelName) : Optional.empty();
		populateEntities(telosysProject, optionalModelName, entitiesTable);

	}
	private static void populateEntities(TelosysProject telosysProject, Optional<String> optionalModelName, Table entitiesTable) {
        LOGGER.fine("populateEntities ( optionalModelName = '" + optionalModelName +"' )");
		// Clear table (remove all rows)
		entitiesTable.removeAll();
		if ( optionalModelName.isPresent() ) {
			try {
				List<String> entities = TelosysEvolution.getEntities(telosysProject, optionalModelName.get());
		        // Add Rows
		        for (String entityName : entities ) { 
		            TableItem item = new TableItem(entitiesTable, SWT.NONE);
		            item.setChecked(true); // All checked by default
		            item.setText(0, entityName); // Text for Column 0
	//	            item.setText(1, "Entity Col-1:" + i); // Text for Column 1          
		            item.setData(entityName); // Any object 
		        }
			} catch (TelosysApiException e) {
				DialogBox.showError(TELOSYS_API_ERROR, e.getMessage());
			}
		}
	}
	public static Optional<String> refreshModels(TelosysProject telosysProject, Combo modelsCombo) {
		if ( !checkNotNull(telosysProject, modelsCombo) ) return Optional.empty();
        LOGGER.fine("refreshModels()");
//		String currentModel = null;
//		Optional<String> optionalModelName = getCurrentSelection(modelsCombo);
//		if ( optionalModelName.isPresent() ) {
//			currentModel = optionalModelName.get();
//		}
//		// Reload all models in combobox
//        LOGGER.fine("refreshModels() --> populateModels(currentModel='" + currentModel + "')");
//		return TelosysCommand.populateModels(telosysProject, modelsCombo, currentModel);
//        LOGGER.fine("refreshModels() --> populateModels(currentModel='" + currentModel + "')");
		return populateModels(telosysProject, modelsCombo, getCurrentSelection(modelsCombo));
	}
	public static void refreshModelsAndEntities(TelosysProject telosysProject, Combo modelsCombo, Table entitiesTable) {
		if ( !checkNotNull(telosysProject, modelsCombo, entitiesTable) ) return ;
        LOGGER.fine("refreshModelsAndEntities() ");
        Optional<String> optionalCurrentModel = refreshModels(telosysProject, modelsCombo); 
        LOGGER.fine("refreshModelsAndEntities() : optionalCurrentModel='" + optionalCurrentModel + "')");
		populateEntities(telosysProject, optionalCurrentModel, entitiesTable);
	}
	public static void editModel(TelosysProject telosysProject, Combo modelsCombo) {
		if ( !checkNotNull(telosysProject, modelsCombo, modelsCombo) ) return ;
		Optional<String> optionalModelName = getCurrentSelection(modelsCombo);
		if ( optionalModelName.isPresent() ) {
			String modelName = optionalModelName.get();
			File modelFile = telosysProject.getModelInfoFile(modelName);
			if ( modelFile != null && modelFile.exists() ) {				
				openEditorForFile(modelFile);
			}
			else {
				DialogBox.showWarning("Model file not found!");
			}
		}
	}
	public static void editEntity(TelosysProject telosysProject, Combo modelsCombo, String entityName) {
		if ( !checkNotNull(telosysProject, modelsCombo, entityName) ) return ;
		Optional<String> optionalModelName = getCurrentSelection(modelsCombo);
		if ( optionalModelName.isPresent() ) {
			String modelName = optionalModelName.get();
			File entityFile = telosysProject.getDslEntityFile(modelName, entityName);
			if ( entityFile != null && entityFile.exists() ) {				
				openEditorForFile(entityFile);
			}
			else {
				DialogBox.showWarning("Entity file not found!");
			}
		}
	}
	public static void checkModel(TelosysProject telosysProject, Combo modelsCombo) {
		if ( !checkNotNull(telosysProject, modelsCombo) ) return ;
		Optional<String> optionalModelName = getCurrentSelection(modelsCombo);
		if ( optionalModelName.isPresent() ) {
			checkModel(telosysProject, optionalModelName.get());
		}
	}
	private static void checkModel(TelosysProject telosysProject, String modelName) {
		if ( modelName != null ) {
			File osDirFile = telosysProject.getModelFolder(modelName);
			if ( osDirFile != null ) {
				//--- Check the model
				ModelCheckStatus modelCheckStatus = TelosysEvolution.checkModel(osDirFile);
				//--- Show the result
		    	CheckModelFromModelDialogBox dialogBox = new CheckModelFromModelDialogBox(osDirFile, modelCheckStatus.getFullReport());
		    	dialogBox.open(); // show dialog box to print the result 
		    	// nothing else to do (all the work is done)
			}
			else {
				DialogBox.showError("Cannot get model directory");
			}
		}
	}
	
	public static void newEntity(TelosysProject telosysProject, Combo modelsCombo, Table entitiesTable) {
		if ( !checkNotNull(telosysProject, modelsCombo, entitiesTable) ) return ;
		Optional<String> optionalModelName = getCurrentSelection(modelsCombo);
		if ( optionalModelName.isPresent() ) {
			String modelName = optionalModelName.get();
			// Open dialog-box to get entity name and try to create it
	    	NewEntityFromModelDialogBox dialogBox = new NewEntityFromModelDialogBox(telosysProject, modelName);
	    	int r = dialogBox.open();
	    	if ( r == Window.OK ) {
	    		// Refresh entities to show the new entity
	    		populateEntities(telosysProject, modelName, entitiesTable);
	    	}
		}
	}
	public static File newEntity(TelosysProject telosysProject, String modelNameArg, String entityNameArg) {
		if ( !checkNotNull(telosysProject, modelNameArg, entityNameArg) ) return null;
		String modelName  = modelNameArg.trim();
		String entityName = entityNameArg.trim();
		// Try to create entity
    	try {
    		File entityFile = telosysProject.createNewDslEntity(modelName, entityName);
			if ( entityFile != null ) {
				// File created => reveal in Project Explorer
            	ProjectExplorerUtil.reveal(entityFile);
			}
			return entityFile;
		} catch (Exception e) {
    		DialogBox.showError("Cannot create entity '" + entityName + "' \n" 
    				+ "\n" +
    				e.getMessage()  + " \n" 
    				);
    		return null;
		}
	}
	
	public static void newModel(TelosysProject telosysProject) {
		if ( !checkNotNull(telosysProject) ) return ;
		NewModelDialogBox dialogBox = new NewModelDialogBox(telosysProject, null);
		dialogBox.open();
	}
	public static void newModel(TelosysProject telosysProject, Combo modelsCombo) {
		if ( !checkNotNull(telosysProject, modelsCombo) ) return ;
		NewModelDialogBox dialogBox = new NewModelDialogBox(telosysProject, modelsCombo);
		dialogBox.open();
	}

	public static void installModels(TelosysProject telosysProject, Combo modelsCombo) {
		if ( !checkNotNull(telosysProject, modelsCombo) ) return ;
    	InstallDialogBox dialogBox = new InstallDialogBox(telosysProject, InstallationType.MODEL );
    	dialogBox.open(); // show dialog box immediately 
    	if ( dialogBox.getInstallationsCount() > 0 ) {
    		refreshModels(telosysProject, modelsCombo);
    	}
	}
	
	//---------------------------------------------------------------------------------------
	// BUNDLES
	//---------------------------------------------------------------------------------------
	public static void populateBundles(TelosysProject telosysProject, Combo bundlesCombo) {
		populateBundles(telosysProject, bundlesCombo, Optional.empty());
	}
	private static Optional<String> populateBundles(TelosysProject telosysProject, Combo bundlesCombo, Optional<String> optionalCurrentBundle) {
        LOGGER.fine("populateBundles ( optionalCurrentBundle = '" + optionalCurrentBundle +"' )");
		// Populate combo box
		List<String> bundles = telosysProject.getBundleNames();
		bundles.add(0, ""); // First element is void 
		bundlesCombo.setItems(bundles.toArray(new String[0])); // All other elements
        // Re-select current bundle if any
		Optional<ComboItem> optionalItem = ComboUtil.getItemByValue(bundlesCombo, optionalCurrentBundle);
//		if ( optionalItem.isPresent() ) {
//			// Found => select
//	        bundlesCombo.select(optionalItem.get().getIndex());
//	        return Optional.of(optionalItem.get().getValue());
//		}
//		else {
//			return Optional.empty();
//		}
		return ComboUtil.selectItem(bundlesCombo, optionalItem);
	}
	private static void setCopyStaticFilesCheckBoxState(Optional<String> optionalBundleName, Button copyStaticFilesCheckBox) {
		boolean state = false;
		if ( optionalBundleName.isPresent() ) {
			state = true;
		}
		copyStaticFilesCheckBox.setEnabled(state);
		copyStaticFilesCheckBox.setSelection(state);
	}
	public static Optional<String> refreshBundles(TelosysProject telosysProject, Combo bundlesCombo) {
        LOGGER.fine("refreshBundles()");
//		String currentBundle = null;
//		Optional<String> optionalBundleName = getCurrentSelection(bundlesCombo);
//		if ( optionalBundleName.isPresent() ) {
//			currentBundle = optionalBundleName.get();
//		}
//		// Reload all bundles in combo-box
//        LOGGER.fine("refreshBundles() --> populateBundles(currentBundle='" + currentBundle + "')");
		return populateBundles(telosysProject, bundlesCombo, getCurrentSelection(bundlesCombo));
	}
	public static void refreshBundlesAndTemplates(TelosysProject telosysProject, Combo bundlesCombo, Button copyStaticFilesCheckBox, Table templatesTable) {
        LOGGER.fine("refreshBundlesAndTemplates()");
        Optional<String> optionalCurrentBundle = refreshBundles(telosysProject, bundlesCombo); 
        LOGGER.fine("refreshBundlesAndTemplates() : optionalCurrentBundle='" + optionalCurrentBundle + "'");
   		populateTemplates(telosysProject, optionalCurrentBundle, copyStaticFilesCheckBox, templatesTable);
	}
	
	public static void populateTemplates(TelosysProject telosysProject, String bundleName, Button copyStaticFilesCheckBox, Table templatesTable) {
		Optional<String> optionalBundleName = isValidName(bundleName) ? Optional.of(bundleName) : Optional.empty();
		populateTemplates(telosysProject, optionalBundleName, copyStaticFilesCheckBox, templatesTable);
	}
	private static void populateTemplates(TelosysProject telosysProject, Optional<String> optionalBundleName, Button copyStaticFilesCheckBox, Table templatesTable) {
		// Reset "Copy Static Files" check box
		setCopyStaticFilesCheckBoxState(optionalBundleName, copyStaticFilesCheckBox);
		// Clear table (remove all rows)
		templatesTable.removeAll();
		// Populate table if current bundle
		if ( optionalBundleName.isPresent() ) {
			try {
				TargetsDefinitions targets = telosysProject.getTargetDefinitions(optionalBundleName.get());
		        // Add Rows
		        for (TargetDefinition target : targets.getTemplatesTargets() ) { 
		            TableItem item = new TableItem(templatesTable, SWT.NONE);
		            item.setChecked(true); // All checked by default
		            item.setText(0, target.getTemplate()); // Text for Column 0
		            //item.setText(1, target.getId()); // Text for Column 1    
		            // Data (any object) used for "Edit" command => Template file name
		            item.setData(target.getTemplate());
		        }
			} catch (Exception e) {
				DialogBox.showError(TELOSYS_API_ERROR, e.getMessage());
			}
		}
	}
	public static void editBundle(TelosysProject telosysProject, Combo bundlesCombo) {
		Optional<String> optionalBundleName = getCurrentSelection(bundlesCombo);
		if ( optionalBundleName.isPresent() ) {
			String bundleName = optionalBundleName.get();
			// Get bundle file
			File bundleFile = telosysProject.getBundleConfigFile(bundleName);
			if (bundleFile != null && bundleFile.exists() ) {
				openEditorForFile(bundleFile);
			}
			else {
				DialogBox.showError("Bundle file not found!");
			}
		}
	}
	public static void editTemplate(TelosysProject telosysProject, Combo bundlesCombo, String templateName) {
		Optional<String> optionalBundleName = getCurrentSelection(bundlesCombo);
		if ( optionalBundleName.isPresent() ) {
			String bundleName = optionalBundleName.get();
			// Get the template file located in the given bundle folder
			File templateFile = TelosysEvolution.getTemplateFile(telosysProject, bundleName, templateName);
			if ( templateFile != null && templateFile.exists() ) {				
				openEditorForFile(templateFile);
			}
			else {
				DialogBox.showWarning("Template file not found!");
			}
		}
	}

	public static void newBundle(TelosysProject telosysProject) {
		// TODO
		DialogBox.showInformation("TODO: \n" 
				+ "'New Bundle' NOT YET AVAILABLE IN TELOSYS API \n" 
        		+ "Current project: \n" + telosysProject.getProjectFolder() );
	}
	
	public static void installBundles(TelosysProject telosysProject, Combo bundlesCombo) {
    	InstallDialogBox dialogBox = new InstallDialogBox(telosysProject, InstallationType.BUNDLE );
    	dialogBox.open(); // show dialog box immediately 
    	if ( dialogBox.getInstallationsCount() > 0 ) {
    		refreshBundles(telosysProject, bundlesCombo);
    	}
	}
	
	//---------------------------------------------------------------------------------------
	// GENERATION
	//---------------------------------------------------------------------------------------
	private static boolean notNullNotVoid(String s) {
		if ( s == null ) return false;
		if ( s.trim().isEmpty() ) return false;
		return true; // OK 
	}
	public static void launchGeneration(IProject eclipseProject, Combo modelsCombo, Table entitiesTable, Combo bundlesCombo, Table templatesTable, boolean flagCopyResources) {
		// Retrieve required information from UI widgets 
		Optional<String> optionalModelName = getCurrentSelection(modelsCombo);
		Optional<String> optionalBundleName = getCurrentSelection(bundlesCombo);
		List<String> entityNames = new LinkedList<>();
		for (TableItem item : entitiesTable.getItems() ) {
			if ( item.getChecked() ) {
				String entityName = item.getText(0);
				if ( notNullNotVoid(entityName) ) {
					entityNames.add(entityName.trim());
				}
			}
		}
		List<String> templateNames = new LinkedList<>();
		for (TableItem item : templatesTable.getItems() ) {
			if ( item.getChecked() ) {
				String templateName = item.getText(0);
				if ( notNullNotVoid(templateName) ) {
					templateNames.add(templateName.trim());
				}
			}
		}
		if ( optionalModelName.isPresent() && optionalBundleName.isPresent() && !entityNames.isEmpty() && !templateNames.isEmpty() ) {
//			TelosysConsoleForEclipse console = new TelosysConsoleForEclipse(TelosysConsoleType.TELOSYS_CONSOLE);
			TelosysConsole console = TelosysConsoleProvider.getConsole(TelosysConsoleType.TELOSYS_CONSOLE);
			console.clear();
			console.showConsoleView();
			TelosysProject telosysProject = ProjectUtil.getTelosysProject(eclipseProject, new TelosysLoggerForEclipse(console));
			try {
				TelosysEvolution.launchGeneration(telosysProject, optionalModelName.get(), entityNames, optionalBundleName.get(), templateNames, flagCopyResources);
				//console.showConsoleView();
			} catch (TelosysApiException e) {
				//console.showConsoleView();
				DialogBox.showError(e.getMessage());
			}
		}
	}
	
	//---------------------------------------------------------------------------------------
	// COMMONS
	//---------------------------------------------------------------------------------------
	private static void openEditorForFile(File file) {
		IFile iFile = WorkspaceUtil.getIFile(file);
		if ( iFile != null ) {
			// File is located in the Eclipse Workspace
			openEditorForFileInworkspace(iFile);
		}
		else {
			// File is located out of the Eclipse Workspace
			openEditorForFileOutOfworkspace(file);
		}
	}
	private static void openEditorForFileInworkspace(IFile fileInWorkspace) {
        if (fileInWorkspace.exists()) {
            try {
                //IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                IWorkbenchPage page = WorkbenchUtil.getActiveWindowPage();
                // Opens an editor on the given file resource. 
                // If the page already has an editor open on the target object then thateditor is brought to front; 
                // otherwise, a new editor is opened. 
                // If activate == true the editor will be activated. 
                // IDE.openEditor(page, fileInWorkspace, "org.eclipse.ui.DefaultTextEditor", true);
                IDE.openEditor(page, fileInWorkspace);
            } catch (Exception e) {
                e.printStackTrace();
    			DialogBox.showError("Exception: " + e.getMessage() );
            }
        }
        else {
			DialogBox.showError("File not found!\n" + fileInWorkspace );
        }
	}
	
	private static void openEditorForFileOutOfworkspace(File fileOutOfWorkspace) {
        if (fileOutOfWorkspace.exists()) {
        	//IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileOutOfWorkspace.toURI());
        	IFileStore fileStore = EFS.getLocalFileSystem().fromLocalFile(fileOutOfWorkspace);
        	//fileStore = fileStore.getChild(names[i]);

            // Get the active page in Eclipse
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

            try {

                // Open the file using the FileEditorInput
                //IEditorPart editor = IDE.openEditor(page, fileStoreEditorInput, true);
                
                // Opens an editor on the given IFileStore object.
                // Unlike the other "openEditor" methods, this one can be used to open files 
                // that reside outside the workspace resource set.
                IDE.openEditorOnFileStore(page, fileStore);

            } catch (Exception e) {
                e.printStackTrace();
    			DialogBox.showError("Exception: " + e.getMessage() );
            }
        }		
        else {
			DialogBox.showError("File not found!\n" + fileOutOfWorkspace );
        }
	}
	
	//---------------------------------------------------------------------------------------
	// DATABASES
	//---------------------------------------------------------------------------------------
	public static void populateDatabases(TelosysProject telosysProject, Table databasesTable) {
        LOGGER.fine("populateDatabases()");
		// Clear table (remove all rows)
        databasesTable.removeAll();
        // Populate with db configurations
		DatabaseDefinitions databasesConfigurations;
		try {
			databasesConfigurations = telosysProject.getDatabaseDefinitions();
		} catch (Exception e) {
			DialogBox.showError("Cannot get databases configuration.\n " + e.getMessage());
			return;
		}
		List<DatabaseDefinition> databases = databasesConfigurations.getDatabases();
		if ( databases != null && ! databases.isEmpty()) {
			// print(databasesConfigurations.getDatabases().size()+ " database(s) defined" );
			for ( DatabaseDefinition databaseDefinition : databases ) {
	            TableItem item = new TableItem(databasesTable, SWT.NONE);
//		            item.setChecked(true); // All checked by default
	            item.setText(0, databaseDefinition.getId());   // Text for Column 0
	            item.setText(1, databaseDefinition.getType()); // Text for Column 1          
	            item.setText(2, databaseDefinition.getName()); // Text for Column 2          
	            item.setData(databaseDefinition); // Any object 
			}
			// Pack the columns to fit the content
			// NO Pack! databasesTable.pack();
		}
		else {
			// message: " no database defined "
		}
	}
	public static void populateLibraries(TelosysProject telosysProject, Table librariesTable) {
        LOGGER.fine("populateLibraries()");
		// Clear table (remove all rows)
        librariesTable.removeAll();
        // Populate 
        List<File> files; 
		try {
			files = TelosysEvolution.getLibFiles(telosysProject); 
		} catch (Exception e) {
			DialogBox.showError("Cannot get lib files.\n " + e.getMessage());
			return;
		}
		for ( File file : files ) {
            TableItem item = new TableItem(librariesTable, SWT.NONE);
            item.setText(0, file.getName()); // Text for Column 0
		}
	}
	public static void populateLibraries(TelosysProject telosysProject, org.eclipse.swt.widgets.List librariesList) {
        LOGGER.fine("populateLibraries()");
		// Clear (remove all rows)
        librariesList.removeAll();
        // Populate 
        List<File> files; 
		try {
			files = TelosysEvolution.getLibFiles(telosysProject); 
		} catch (Exception e) {
			DialogBox.showError("Cannot get lib files.\n " + e.getMessage());
			return;
		}
		for ( File file : files ) {
            librariesList.add(file.getName());
		}
	}
	
	public static void editDatabases(TelosysProject telosysProject) {
		File databasesFile = TelosysEvolution.getDatabasesFile(telosysProject);
		if ( databasesFile != null && databasesFile.exists() ) {				
			openEditorForFile(databasesFile);
		}
		else {
			DialogBox.showWarning("Databases file not found!");
		}
	}
//	public static void refreshDatabasesAndLibraries(TelosysProject telosysProject, Table databasesTable, Table librariesTable, Text dbText) {
//        LOGGER.fine("refreshDatabasesAndLibraries()");
//		dbText.setText("");
//		populateDatabases(telosysProject, databasesTable);
//		populateLibraries(telosysProject, librariesTable);
//	}
	public static void refreshDatabasesAndLibraries(TelosysProject telosysProject, Table databasesTable,  org.eclipse.swt.widgets.List librariesList, Text dbText) {
        LOGGER.fine("refreshDatabasesAndLibraries()");
		dbText.setText("");
		populateDatabases(telosysProject, databasesTable);
		populateLibraries(telosysProject, librariesList);
	}
	
	public static void showDatabaseConfig(DatabaseDefinition dd, Text text) {
		StringBuilder sb = new StringBuilder();
		sb.append( " . Database id   : '" + dd.getId() + "' " ).append("\n");
		sb.append( " . Name          : " + dd.getName() ).append("\n");
		sb.append( " . Type          : " + dd.getType() ).append("\n");
		
		sb.append( " . Connection : " ).append("\n");
		sb.append( "   - JDBC URL      : " + dd.getUrl()  ).append("\n");
		sb.append( "   - Driver class  : " + dd.getDriver() ).append("\n");
		sb.append( "   - User          : " + dd.getUser() ).append("\n");
		sb.append( "   - Password      : " + dd.getPassword() ).append("\n");
		
		sb.append( " . Metadata : " ).append("\n");
		sb.append( "   - Catalog            : " + dd.getCatalog() ).append("\n");
		sb.append( "   - Schema             : " + dd.getSchema() ).append("\n");
		sb.append( "   - Table types        : " + dd.getTableTypes() ).append("\n");
		sb.append( "   - Table name pattern : " + dd.getTableNamePattern() ).append("\n");
		sb.append( "   - Table name exclude : " + dd.getTableNameExclude() ).append("\n");
		sb.append( "   - Table name include : " + dd.getTableNameInclude() ).append("\n");

		sb.append( " . Model creation options : " ).append("\n");
		sb.append( "   - links ManyToOne  : " + dd.isLinksManyToOne() ).append("\n");
		sb.append( "   - links OneToMany  : " + dd.isLinksOneToMany() ).append("\n");
		sb.append( "   - links ManyToMany : " + dd.isLinksManyToMany() ).append("\n");		
		sb.append( "   - db comment       : " + dd.isDbComment() ).append("\n");
		sb.append( "   - db catalog       : " + dd.isDbCatalog() ).append("\n");
		sb.append( "   - db schema        : " + dd.isDbSchema() ).append("\n");
		sb.append( "   - db table         : " + dd.isDbTable() ).append("\n");
		sb.append( "   - db view          : " + dd.isDbView() ).append("\n");
		sb.append( "   - db name          : " + dd.isDbName() ).append("\n");
		sb.append( "   - db type          : " + dd.isDbType() ).append("\n");
		sb.append( "   - db default value : " + dd.isDbDefaultValue() ).append("\n");
		
		text.setText(sb.toString());
	}	
	
	//--------------------------------------------------------------------------------------------------------
	// New model from database 
	//--------------------------------------------------------------------------------------------------------
	public static void newModelFromDatabase(TelosysProject telosysProject, String modelName, DatabaseDefinition currentDatabaseDefinition) {
		if ( !checkNotNull(telosysProject, modelName, currentDatabaseDefinition)) return;
		if ( telosysProject.modelFolderExists(modelName) ) {
			DialogBox.showWarning("Model '" + modelName + "' already exists!");
		}
		else {
			boolean r = executeWithProgressMonitorDialog("Creating a new model from database...  Please wait.", 
					() -> newModelFromDatabaseTask(telosysProject, modelName, currentDatabaseDefinition) );
			if ( r == true ) {
				// Refresh and expand folder in Project Explorer
				File modelFolder = telosysProject.getModelFolder(modelName);
				ProjectExplorerUtil.reveal(modelFolder);
			}
		}
	}
	private static String newModelFromDatabaseTask(TelosysProject telosysProject, String modelName, DatabaseDefinition currentDatabaseDefinition) {
		// Just try to connect to database (no meta-data)
		try {
			String databaseId = currentDatabaseDefinition.getId();
			telosysProject.createNewDslModelFromDatabase(modelName, databaseId);
			return "OK, model '" + modelName + "' successfully created from database '" + databaseId + "'." ;
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("ERROR \n");
			sb.append("\n");
			sb.append("Exception:" + e.getClass().getName() + " : " + e.getMessage() + "\n");
			if ( e.getCause() != null ) {
				sb.append("Cause: " + e.getCause().getMessage() + "\n");
			}
			return sb.toString();
		}
	}

	//--------------------------------------------------------------------------------------------------------
	// Database : test connection
	//--------------------------------------------------------------------------------------------------------
	public static void testConnection(TelosysProject telosysProject, DatabaseDefinition currentDatabaseDefinition) {
		if ( !checkNotNull(telosysProject, currentDatabaseDefinition)) return;
		executeWithProgressMonitorDialog("Trying to connect...  Please wait.", 
				() -> testConnectionTask(telosysProject, currentDatabaseDefinition),
				false);
	}
	private static String testConnectionTask(TelosysProject telosysProject, DatabaseDefinition currentDatabaseDefinition) {
		// Just try to connect to database (no meta-data)
		try {
			boolean r = telosysProject.checkDatabaseConnection(currentDatabaseDefinition.getId()) ;
			if ( r ) {
				return "OK, successful connection test.";
			}
			else {
				return "ERROR \n Cannot connect to database.\n (return = false)";
			}
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("ERROR \n");
			sb.append("Cannot connect to database.\n");
			sb.append("\n");
			sb.append("Exception:" + e.getClass().getName() + " : " + e.getMessage() + "\n");
			if ( e.getCause() != null ) {
				sb.append("Cause: " + e.getCause().getMessage() + "\n");
			}
			return sb.toString();
		}
	}
	
	//--------------------------------------------------------------------------------------------------------
	// Database : get meta-data
	//--------------------------------------------------------------------------------------------------------
	public static void getDatabaseInfo(TelosysProject telosysProject, DatabaseDefinition currentDatabaseDefinition) {
		if ( !checkNotNull(telosysProject, currentDatabaseDefinition)) return;
		MetaDataOptionsImpl metadataOptions = new MetaDataOptionsImpl();
		metadataOptions.setInfo(true);
		executeWithProgressMonitorDialog("Trying to get database info...  Please wait.", 
				() -> getMetaDataTask(telosysProject, currentDatabaseDefinition, metadataOptions) );		
	}
	public static void getDatabaseSchemas(TelosysProject telosysProject, DatabaseDefinition currentDatabaseDefinition) {
		if ( !checkNotNull(telosysProject, currentDatabaseDefinition)) return;
		MetaDataOptionsImpl metadataOptions = new MetaDataOptionsImpl();
		metadataOptions.setSchemas(true);
		executeWithProgressMonitorDialog("Trying to get schemas...  Please wait.", 
				() -> getMetaDataTask(telosysProject, currentDatabaseDefinition, metadataOptions) );		
	}
	public static void getDatabaseCatalogs(TelosysProject telosysProject, DatabaseDefinition currentDatabaseDefinition) {
		if ( !checkNotNull(telosysProject, currentDatabaseDefinition)) return;
		MetaDataOptionsImpl metadataOptions = new MetaDataOptionsImpl();
		metadataOptions.setCatalogs(true);
		executeWithProgressMonitorDialog("Trying to get catalogs...  Please wait.", 
				() -> getMetaDataTask(telosysProject, currentDatabaseDefinition, metadataOptions) );		
	}
	public static void getDatabaseTables(TelosysProject telosysProject, DatabaseDefinition currentDatabaseDefinition) {
		if ( !checkNotNull(telosysProject, currentDatabaseDefinition)) return;
		MetaDataOptionsImpl metadataOptions = new MetaDataOptionsImpl();
		metadataOptions.setTables(true);
		executeWithProgressMonitorDialog("Trying to get tables...  Please wait.", 
				() -> getMetaDataTask(telosysProject, currentDatabaseDefinition, metadataOptions) );		
	}
	public static void getDatabaseColumns(TelosysProject telosysProject, DatabaseDefinition currentDatabaseDefinition) {
		if ( !checkNotNull(telosysProject, currentDatabaseDefinition)) return;
		MetaDataOptionsImpl metadataOptions = new MetaDataOptionsImpl();
		metadataOptions.setColumns(true);
		executeWithProgressMonitorDialog("Trying to get columns...  Please wait.", 
				() -> getMetaDataTask(telosysProject, currentDatabaseDefinition, metadataOptions) );		
	}
	public static void getDatabasePK(TelosysProject telosysProject, DatabaseDefinition currentDatabaseDefinition) {
		if ( !checkNotNull(telosysProject, currentDatabaseDefinition)) return;
		MetaDataOptionsImpl metadataOptions = new MetaDataOptionsImpl();
		metadataOptions.setPrimaryKeys(true);
		executeWithProgressMonitorDialog("Trying to get primary keys...  Please wait.", 
				() -> getMetaDataTask(telosysProject, currentDatabaseDefinition, metadataOptions) );		
	}
	public static void getDatabaseFK(TelosysProject telosysProject, DatabaseDefinition currentDatabaseDefinition) {
		if ( !checkNotNull(telosysProject, currentDatabaseDefinition)) return;
		MetaDataOptionsImpl metadataOptions = new MetaDataOptionsImpl();
		metadataOptions.setForeignKeys(true);
		executeWithProgressMonitorDialog("Trying to get foreign keys...  Please wait.", 
				() -> getMetaDataTask(telosysProject, currentDatabaseDefinition, metadataOptions) );		
	}
	public static String getMetaDataTask(TelosysProject telosysProject, DatabaseDefinition databaseDefinition, MetaDataOptionsImpl metadataOptions) {
		try {
			return telosysProject.getMetaData(databaseDefinition, metadataOptions);
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("ERROR \n");
			sb.append("Cannot get database meta-data.\n");
			sb.append("\n");
			sb.append("Exception:" + e.getClass().getName() + " : " + e.getMessage() + "\n");
			if ( e.getCause() != null ) {
				sb.append("Cause: " + e.getCause().getMessage() + "\n");
			}
			return sb.toString();
		}
	}
	
	//--------------------------------------------------------------------------------------------------------
	// Task execution with ProgressMonitorDialog
	//--------------------------------------------------------------------------------------------------------
	private static boolean executeWithProgressMonitorDialog(String waitMessage, Supplier<String> supplierTask) {
		return executeWithProgressMonitorDialog(waitMessage, supplierTask, true);
	}
	private static boolean executeWithProgressMonitorDialog(String waitMessage, Supplier<String> supplierTask, boolean useResultDialogBox) {
		TelosysMonitorTask monitorTask = new TelosysMonitorTask(waitMessage, supplierTask );
		try {
			// Run task 
			String result = executeWithProgressMonitorDialog(monitorTask);
			if ( result.startsWith("ERROR") ) {
				DialogBox.showError(result);
			} 
			else {
				if ( useResultDialogBox ) {
					// Show resulting metadata in a specific dialog-box
					ResultDialogBox dialogBox = new ResultDialogBox("Database metadata", result);
					dialogBox.open();
				}
				else {
					DialogBox.showInformation(result);
				}
				return true; // OK
			}
		} catch (InvocationTargetException e) {
			DialogBox.showError("Error during task", e.getMessage());
		} catch (InterruptedException e) {
			// DialogBox.showInformation("Task interrupted");
			// Not an error
		}
		return false;
	}
	private static String executeWithProgressMonitorDialog(TelosysMonitorTask runnableTask) throws InvocationTargetException, InterruptedException{
		ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(WorkbenchUtil.getActiveWindowShell()) ;
		// Run task 
		progressMonitorDialog.run(false, false, runnableTask);
		return runnableTask.getResult();
	}
	
}
