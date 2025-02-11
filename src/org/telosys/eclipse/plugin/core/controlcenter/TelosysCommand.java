package org.telosys.eclipse.plugin.core.controlcenter;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.telosys.eclipse.plugin.commons.Logger;
import org.telosys.eclipse.plugin.core.command.CheckModelFromModelDialogBox;
import org.telosys.eclipse.plugin.core.commons.ComboItem;
import org.telosys.eclipse.plugin.core.commons.ComboUtil;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ModelCheckStatus;
import org.telosys.eclipse.plugin.core.commons.ProjectUtil;
import org.telosys.eclipse.plugin.core.commons.TelosysApiException;
import org.telosys.eclipse.plugin.core.commons.TelosysEclipseConsole;
import org.telosys.eclipse.plugin.core.commons.TelosysEvolution;
import org.telosys.eclipse.plugin.core.commons.TelosysLoggerForEclipse;
import org.telosys.eclipse.plugin.core.commons.WorkbenchUtil;
import org.telosys.eclipse.plugin.core.commons.WorkspaceUtil;
import org.telosys.eclipse.plugin.core.commons.dialogbox.InstallDialogBox;
import org.telosys.eclipse.plugin.core.commons.dialogbox.NewModelDialogBox;
import org.telosys.tools.api.InstallationType;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.commons.bundles.TargetsDefinitions;

public class TelosysCommand {
	
	private static final String TELOSYS_API_ERROR = "Telosys API error";

	private static boolean isValidName(String name) {
		if ( name == null ) return false;
		if ( name.isEmpty() ) return false;
		if ( name.isBlank() ) return false;
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
        Logger.log("populateModels ( optionalCurrentModel = '" + optionalCurrentModel +"' )");
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
        Logger.log("populateEntities ( optionalModelName = '" + optionalModelName +"' )");
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
        Logger.log("refreshModels()");
//		String currentModel = null;
//		Optional<String> optionalModelName = getCurrentSelection(modelsCombo);
//		if ( optionalModelName.isPresent() ) {
//			currentModel = optionalModelName.get();
//		}
//		// Reload all models in combobox
//        Logger.log("refreshModels() --> populateModels(currentModel='" + currentModel + "')");
//		return TelosysCommand.populateModels(telosysProject, modelsCombo, currentModel);
//        Logger.log("refreshModels() --> populateModels(currentModel='" + currentModel + "')");
		return populateModels(telosysProject, modelsCombo, getCurrentSelection(modelsCombo));
	}
	public static void refreshModelsAndEntities(TelosysProject telosysProject, Combo modelsCombo, Table entitiesTable) {
        Logger.log("refreshModelsAndEntities() ");
        Optional<String> optionalCurrentModel = refreshModels(telosysProject, modelsCombo); 
        Logger.log("refreshModelsAndEntities() : optionalCurrentModel='" + optionalCurrentModel + "')");
		populateEntities(telosysProject, optionalCurrentModel, entitiesTable);
	}
	protected static void editModel(TelosysProject telosysProject, Combo modelsCombo) {
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
	protected static void editEntity(TelosysProject telosysProject, Combo modelsCombo, String entityName) {
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
	protected static void checkModel(TelosysProject telosysProject, Combo modelsCombo) {
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
				Shell shell = WorkbenchUtil.getActiveWindowShell();
		    	CheckModelFromModelDialogBox dialogBox = new CheckModelFromModelDialogBox(shell, osDirFile, modelCheckStatus.getFullReport());
		    	dialogBox.open(); // show dialog box to print the result 
		    	// nothing else to do (all the work is done)
			}
			else {
				DialogBox.showError("Cannot get model directory");
			}
		}
	}
	
	protected static void newEntity(TelosysProject telosysProject, Combo modelsCombo) {
		Optional<String> optionalModelName = getCurrentSelection(modelsCombo);
		if ( optionalModelName.isPresent() ) {
			todo("New Enity in model ", optionalModelName.get(), telosysProject);
		}
	}
	
	public static void newModel(TelosysProject telosysProject) {
		NewModelDialogBox dialogBox = new NewModelDialogBox(telosysProject, null);
		dialogBox.open();
	}
	public static void newModel(TelosysProject telosysProject, Combo modelsCombo) {
		NewModelDialogBox dialogBox = new NewModelDialogBox(telosysProject, modelsCombo);
		dialogBox.open();
	}

	protected static void installModel(TelosysProject telosysProject) {
    	String depot = telosysProject.getTelosysToolsCfg().getDepotForModels(); 
    	InstallDialogBox dialogBox = new InstallDialogBox(telosysProject, depot, InstallationType.MODEL );
    	dialogBox.open(); // show dialog box immediately 
	}
	
	//---------------------------------------------------------------------------------------
	// BUNDLES
	//---------------------------------------------------------------------------------------
	public static void populateBundles(TelosysProject telosysProject, Combo bundlesCombo) {
		populateBundles(telosysProject, bundlesCombo, Optional.empty());
	}
	private static Optional<String> populateBundles(TelosysProject telosysProject, Combo bundlesCombo, Optional<String> optionalCurrentBundle) {
        Logger.log("populateBundles ( optionalCurrentBundle = '" + optionalCurrentBundle +"' )");
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
        Logger.log("refreshBundles()");
//		String currentBundle = null;
//		Optional<String> optionalBundleName = getCurrentSelection(bundlesCombo);
//		if ( optionalBundleName.isPresent() ) {
//			currentBundle = optionalBundleName.get();
//		}
//		// Reload all bundles in combo-box
//        Logger.log("refreshBundles() --> populateBundles(currentBundle='" + currentBundle + "')");
		return populateBundles(telosysProject, bundlesCombo, getCurrentSelection(bundlesCombo));
	}
	public static void refreshBundlesAndTemplates(TelosysProject telosysProject, Combo bundlesCombo, Button copyStaticFilesCheckBox, Table templatesTable) {
        Logger.log("refreshBundlesAndTemplates()");
        Optional<String> optionalCurrentBundle = refreshBundles(telosysProject, bundlesCombo); 
        Logger.log("refreshBundlesAndTemplates() : optionalCurrentBundle='" + optionalCurrentBundle + "'");
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
	protected static void editBundle(TelosysProject telosysProject, Combo bundlesCombo) {
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
	protected static void editTemplate(TelosysProject telosysProject, Combo bundlesCombo, String templateName) {
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

	protected static void newBundle(TelosysProject telosysProject) {
		todo("New Bundle ", "(NOT YET AVAILABLE IN TELOSYS API)", telosysProject);
	}
	
	protected static void installBundle(TelosysProject telosysProject) {
		todo("Install Bundle ", "", telosysProject);
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
			TelosysEclipseConsole console = new TelosysEclipseConsole("Telosys Code Generation");
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
	// TODO
	//---------------------------------------------------------------------------------------
	private static void todo(String command, String modelName, TelosysProject telosysProject) {
		DialogBox.showInformation("TODO: \n" 
				+ command + " " + modelName + "\n" 
        		+ "in project: \n" + telosysProject.getProjectFolder() );
	}
	
}
