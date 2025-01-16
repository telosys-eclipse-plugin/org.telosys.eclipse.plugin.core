package org.telosys.eclipse.plugin.core.commons;

import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.api.TelosysModelException;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.dsl.DslModelError;
import org.telosys.tools.dsl.DslModelErrors;

/**
 * New Telosys evolutions to be added in "Telosys Java API" 
 * 
 * @author laguerin
 *
 */
public class TelosysEvolution {

	// TODO:  TelosysProject.checkModel(String modelName)
	public static ModelCheckStatus checkModel(TelosysProject telosysProject, String modelName) {
		try {
			telosysProject.loadModel(modelName);
			return new ModelCheckStatus(modelName, true);
		} catch (TelosysModelException tme) {
			return new ModelCheckStatus(modelName, false, buildReportLines(tme));
		}		

	}
	
	private static List<String> buildReportLines(TelosysModelException tme) {
		List<String> reportLines = new LinkedList<>();
		reportLines.add("Invalid model '" + tme.getModelName() + "'" );
		// Print parsing errors
		reportLines.add(tme.getMessage());
		DslModelErrors errors = tme.getDslModelErrors();
		if ( errors != null ) {
			for ( DslModelError e : errors.getErrors() ) {
				reportLines.add( " . " + e.getReportMessage() );
			}
		}
		return reportLines;
	}
}
