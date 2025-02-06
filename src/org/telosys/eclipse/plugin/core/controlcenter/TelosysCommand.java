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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ProjectUtil;
import org.telosys.eclipse.plugin.core.commons.TelosysApiException;
import org.telosys.eclipse.plugin.core.commons.TelosysEclipseConsole;
import org.telosys.eclipse.plugin.core.commons.TelosysEvolution;
import org.telosys.eclipse.plugin.core.commons.TelosysLoggerForEclipse;
import org.telosys.eclipse.plugin.core.commons.WorkbenchUtil;
import org.telosys.eclipse.plugin.core.commons.WorkspaceUtil;
import org.telosys.tools.api.TelosysProject;

public class TelosysCommand {

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
	protected static void populateModels(TelosysProject telosysProject, Combo modelsCombo, String currentModel) {
		// Populate combo box
		List<String> models = telosysProject.getModelNames();
		models.add(0, ""); // First element is void = no model
        modelsCombo.setItems(models.toArray(new String[0]));
        // Re-select current model if any
        int selectedIndex = 0; // Select first model by default (= no model)
        if ( currentModel != null ) {
            int index = modelsCombo.indexOf(currentModel); // Search in values
            if (index >= 0) { // Found in the Combo
            	selectedIndex = index;
            }
        }
        modelsCombo.select(selectedIndex);
	}
	protected static void populateEntities(TelosysProject telosysProject, String modelName, Table entitiesTable) {
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
			todo("Check Model", optionalModelName.get(), telosysProject);
		}
	}
	
	protected static void newEntity(TelosysProject telosysProject, Combo modelsCombo) {
		Optional<String> optionalModelName = getCurrentSelection(modelsCombo);
		if ( optionalModelName.isPresent() ) {
			todo("New Enity in model ", optionalModelName.get(), telosysProject);
		}
	}
	
	protected static void newModel(TelosysProject telosysProject) {
		todo("New Model ", "", telosysProject);
	}

	protected static void installModel(TelosysProject telosysProject) {
		todo("Install Model ", "", telosysProject);
	}
	
	//---------------------------------------------------------------------------------------
	// BUNDLES
	//---------------------------------------------------------------------------------------
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
	protected static void launchGeneration(IProject eclipseProject, Combo modelsCombo, Table entitiesTable, Combo bundlesCombo, Table templatesTable, boolean flagCopyResources) {
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
