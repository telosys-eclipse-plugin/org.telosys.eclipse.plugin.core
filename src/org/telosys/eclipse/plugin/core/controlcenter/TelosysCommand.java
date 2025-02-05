package org.telosys.eclipse.plugin.core.controlcenter;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
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
	// MODELS
	//---------------------------------------------------------------------------------------
	protected static void editModel(TelosysProject telosysProject, Combo modelsCombo) {
		Optional<String> optionalModelName = getCurrentSelection(modelsCombo);
		if ( optionalModelName.isPresent() ) {
			String modelName = optionalModelName.get();
			File modelFile = telosysProject.getModelInfoFile(modelName);
			IFile iFile = WorkspaceUtil.getIFile(modelFile);
			if ( iFile != null ) {
				// File is located in the Eclipse Workspace
				openEditorForFileInworkspace(iFile);
			}
			else {
				// File is located out of the Eclipse Workspace
				openEditorForFileOutOfworkspace(modelFile);
			}
		}
		else {
			DialogBox.showError("No model!" );
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
			todo("Edit Bundle", optionalBundleName.get(), telosysProject);
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
