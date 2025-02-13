package org.telosys.eclipse.plugin.core.telosys;

import java.util.LinkedList;
import java.util.List;

public class ModelCheckStatus {

	private static final String EOL = "\n";
	
	private final String       modelName;
	private final boolean      isOK;
	private final List<String> reportLines;
	
	public ModelCheckStatus(String modelName, boolean isOK) {
		super();
		this.modelName = modelName;
		this.isOK = isOK;
		this.reportLines = new LinkedList<>();
	}

	public ModelCheckStatus(String modelName, boolean isOK, List<String> reportLines) {
		super();
		this.modelName = modelName;
		this.isOK = isOK;
		this.reportLines = reportLines;
	}
	
	public ModelCheckStatus(String modelName, boolean isOK, String reportSingleLine) {
		super();
		this.modelName = modelName;
		this.isOK = isOK;
		this.reportLines = new LinkedList<String>();
		this.reportLines.add(reportSingleLine);
	}
	
	public String getModelName() {
		return modelName;
	}

	public boolean isOK() {
		return isOK;
	}
	
	public List<String> getReportLines() {
		return reportLines;
	}

	public String getFullReport() {
		StringBuilder sb = new StringBuilder();
		if ( this.isOK() ) {
			sb.append("Model '" + modelName + "' is OK (no error)." + EOL);
		}
		else {
			sb.append("Model '" + modelName + "' is not valid!" + EOL);
			for ( String s : getReportLines() ) {
				sb.append(" . " + s + EOL);
			}
		}
		return sb.toString();
	}
}
