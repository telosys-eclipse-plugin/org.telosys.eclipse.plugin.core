package org.telosys.eclipse.plugin.core.controlcenter;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.telosys.eclipse.plugin.core.commons.DialogBox;
import org.telosys.eclipse.plugin.core.commons.ProjectUtil;
import org.telosys.eclipse.plugin.core.commons.TelosysApiException;
import org.telosys.eclipse.plugin.core.commons.TelosysEvolution;
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
			todo("Edit Model", optionalModelName.get(), telosysProject);
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
			TelosysProject telosysProject = ProjectUtil.getTelosysProject(eclipseProject);
			try {
				TelosysEvolution.launchGeneration(telosysProject, optionalModelName.get(), entityNames, optionalBundleName.get(), templateNames, flagCopyResources);
			} catch (TelosysApiException e) {
				DialogBox.showError(e.getMessage());
			}
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
