package org.telosys.eclipse.plugin.core.commons;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.api.TelosysModelException;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.dsl.DslModelError;
import org.telosys.tools.dsl.DslModelErrors;
import org.telosys.tools.dsl.DslModelManager;
import org.telosys.tools.dsl.DslModelUtil;

/**
 * New Telosys evolutions to be added in "Telosys Java API" 
 * 
 * @author laguerin
 *
 */
public class TelosysEvolution {

	// TODO:  TelosysProject.checkModel(String modelName)
	public static boolean hasSpecificModelFolder(TelosysProject telosysProject){
    	String specificModelsFolder = telosysProject.getTelosysToolsCfg().getSpecificModelsFolderAbsolutePath();
    	return specificModelsFolder != null && !specificModelsFolder.isEmpty() && !specificModelsFolder.isBlank() ;
	}

	// TODO:  Telosys.createEntity(File modelFolder, String entityName)

	// TODO:  TelosysProject.getEntities(String modelName)
	public static List<String> getEntities(TelosysProject telosysProject, String modelName) throws TelosysApiException {
		File modelFolder = telosysProject.getModelFolder(modelName); 
		try {
			return  DslModelUtil.getEntityNames(modelFolder);
		} catch (Exception e) {
			throw new TelosysApiException("Cannot get entities for model '" + modelName + "'", e);
		}
	}

	// TODO:  TelosysProject.checkModel(String modelName)
	public static ModelCheckStatus checkModel(TelosysProject telosysProject, String modelName) {
		try {
			telosysProject.loadModel(modelName);
			return new ModelCheckStatus(modelName, true);
		} catch (TelosysModelException tme) {
			return new ModelCheckStatus(modelName, false, buildReportLines(tme));
		}
	}
	
	// TODO:  Telosys.checkModel(File modelFolder)
	public static ModelCheckStatus checkModel(File modelFolder) {
		if ( modelFolder != null ) {
			String modelName = modelFolder.getName();
			if ( modelFolder.isDirectory() ) {
				DslModelManager modelManager = new DslModelManager();
				modelManager.loadModel(modelFolder);
				DslModelErrors errors = modelManager.getErrors();
				if ( errors.getNumberOfErrors() == 0 ) {
					// No errors => status OK
					return new ModelCheckStatus(modelName, true); 
				}
				else {
					// 1 or more errors => status report errors
					return new ModelCheckStatus(modelName, false, buildReportLines(modelName, errors));
				}
			}
			else {
				// Not supposed to happen 
				return new ModelCheckStatus(modelName, false, "ERROR: The given model is not a directory!");
			}
		}
		else {
			// Not supposed to happen 
			return new ModelCheckStatus("(null-model-name)", false, "ERROR: The given model is null!");
		}
	}
	
	private static List<String> buildReportLines(TelosysModelException tme) {
//		List<String> reportLines = new LinkedList<>();
//		reportLines.add("Invalid model '" + tme.getModelName() + "'" );
//		// Print parsing errors
//		reportLines.add(tme.getMessage());
//		DslModelErrors errors = tme.getDslModelErrors();
//		if ( errors != null ) {
//			for ( DslModelError e : errors.getErrors() ) {
//				reportLines.add( " . " + e.getReportMessage() );
//			}
//		}
//		return reportLines;
		return buildReportLines(tme.getModelName(), tme.getDslModelErrors());
	}
	private static List<String> buildReportLines(String modelName, DslModelErrors errors) {
		List<String> reportLines = new LinkedList<>();
		reportLines.add("Invalid model '" + modelName + "'" );
		// Print parsing errors
		if ( errors != null ) {
			for ( DslModelError e : errors.getErrors() ) {
				reportLines.add( " . " + e.getReportMessage() );
			}
		}
		return reportLines;
	}
}
