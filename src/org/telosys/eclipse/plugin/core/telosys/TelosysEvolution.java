package org.telosys.eclipse.plugin.core.telosys;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import org.telosys.tools.api.TelosysModelException;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.commons.bundles.TargetsDefinitions;
import org.telosys.tools.dsl.DslModelError;
import org.telosys.tools.dsl.DslModelErrors;
import org.telosys.tools.dsl.DslModelManager;
import org.telosys.tools.dsl.DslModelUtil;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.Model;

/**
 * New Telosys evolutions to be added in "Telosys Java API" 
 * 
 * @author laguerin
 *
 */
public class TelosysEvolution {

	// TODO:  TelosysProject.hasSpecificModelFolder()
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

	// TODO:  replace "getDslEntityFile"
	public static File getEntityFile(TelosysProject telosysProject, String modelName, String entityName) throws TelosysApiException {
		try {
			return  telosysProject.getDslEntityFile(modelName, entityName);
		} catch (Exception e) {
			throw new TelosysApiException("Cannot get entity file for entity '" + entityName + "' in model '" + modelName + "'", e);
		}
	}

	// TODO
	public static File getTemplateFile(TelosysProject telosysProject, String bundleName, String templateFileName) {
		File bundleFolder = telosysProject.getBundleFolder(bundleName);
		File templateFile = new File(bundleFolder, templateFileName);
		return templateFile;
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
	
	public static void launchGeneration(TelosysProject telosysProject, String modelName, List<String> entityNames, String bundleName, List<String> templateNames,  boolean flagCopyResources) throws TelosysApiException {
		Model model ;
		try {
			model = telosysProject.loadModel(modelName);
		} catch (Exception e) {
			throw new TelosysApiException("Cannot load model", e);
		}
		List<String> selectedEntities = selectEntities(model, entityNames);
		List<TargetDefinition> selectedTargets = selectTargets(telosysProject, bundleName, templateNames);

		try {
			telosysProject.launchGeneration(model, selectedEntities, bundleName, selectedTargets, flagCopyResources);
		} catch (Exception e) {
			throw new TelosysApiException("Cannot launch generation", e);
		}
	}
	private static List<String> selectEntities(Model model, List<String> entityNames ) throws TelosysApiException {
		List<String> selectedEntities = new LinkedList<>();
		for ( Entity entity : model.getEntities() ) {
			if ( entityNames.contains(entity.getClassName() ) ) {
				selectedEntities.add(entity.getClassName());
			}
		}
		return selectedEntities;
	}
	private static List<TargetDefinition> selectTargets(TelosysProject telosysProject, String bundleName, List<String> templateNames ) throws TelosysApiException {
		try {
			List<TargetDefinition> targets = new LinkedList<>();
			TargetsDefinitions targetsDefinitions = telosysProject.getTargetDefinitions(bundleName);
			for ( TargetDefinition targetDef : targetsDefinitions.getTemplatesTargets() ) {
				if ( templateNames.contains(targetDef.getTemplate() ) ) {
					targets.add(targetDef);
				}
			}
			return targets;
		} catch (Exception e) {
			throw new TelosysApiException("Cannot get selected templates", e);
		}
	}	
	
	// TODO
	public static final String TELOSYS_TOOLS_FOLDER = "TelosysTools" ;	
	public static final String DATABASES_YAML    = "databases.yaml" ;
	public static final String LIB_FOLDER = "lib" ;	
	
	public static File getTelosysToolsFolder(TelosysProject telosysProject) {
		String projectFolderAbsolutePath = telosysProject.getProjectFolder();
		File projectFolder = new File(projectFolderAbsolutePath);
		return new File(projectFolder, TELOSYS_TOOLS_FOLDER);
	}
	public static File getLibFolder(TelosysProject telosysProject) {
		File telosysToolsFolder = getTelosysToolsFolder(telosysProject); 
		return new File(telosysToolsFolder, LIB_FOLDER);
	}
	
	public static File getDatabasesFile(TelosysProject telosysProject) {
		File telosysToolsFolder = getTelosysToolsFolder(telosysProject); 
		return new File(telosysToolsFolder, DATABASES_YAML);
	}
	
	// TODO
	public static List<File> getLibFiles(TelosysProject telosysProject) throws TelosysApiException {
		File libFolder = getLibFolder(telosysProject);
		if ( libFolder.exists() && libFolder.isDirectory() ) {
			return getJarFilesFromDir(libFolder);
		}
		else {
			throw new TelosysApiException("Lib directory not found ('" + libFolder + "')");
		}
	}
	
	private static List<File> getJarFilesFromDir(File dir) throws TelosysApiException {
//		DirectoryStream.Filter<Path> filter = (filePath) -> 
//		{
//			String fileName = filePath.toString();
//			return fileName.endsWith(".jar") || fileName.endsWith(".zip");
//		};
		Predicate<Path> predicate = (filePath) -> 
		{
			String fileName = filePath.toString();
			return fileName.endsWith(".jar") || fileName.endsWith(".zip");
		};
		return getFilesFromDir(dir, predicate);
	}
	
	private static List<File> getFilesFromDir(File dir, Predicate<Path> predicate) throws TelosysApiException {
		List<File> files = new ArrayList<>();
		DirectoryStream.Filter<Path> filter = (filePath) -> predicate.test(filePath); 
		try ( DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir.toPath(), filter) ) {
			for (Path path : dirStream) {
				files.add(path.toFile());
			}
		}
		catch ( IOException e) {
			throw new TelosysApiException("Cannot get files from directory '" + dir.getName() + "'", e);
		}
		return files;
	}

	private static List<File> getFilesFromDir(File dir, DirectoryStream.Filter<Path> filter) throws TelosysApiException {
		List<File> entries = new ArrayList<>();
		try ( DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir.toPath(), filter) ) {
			for (Path path : dirStream) {
				entries.add(path.toFile());
			}
		}
		catch ( IOException e) {
			throw new TelosysApiException("Cannot get files from directory '" + dir.getName() + "'", e);
		}
		return entries;
	}

}
