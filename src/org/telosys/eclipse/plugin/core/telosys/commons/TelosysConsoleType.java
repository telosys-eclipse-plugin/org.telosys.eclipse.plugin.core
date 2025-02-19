package org.telosys.eclipse.plugin.core.telosys.commons;

public enum TelosysConsoleType {

	TEST1          ("Telosys Test #1"),
	TEST2          ("Telosys Test #2"),
	
	TELOSYS_LOGGER    ("Telosys Logger"),
	TELOSYS_CONSOLE   ("Telosys Console");
//	CODE_GENERATION("Telosys Code Generation"),
//	MODEL_CREATION ("Telosys Model Creation");

    private final String consoleName;

    private TelosysConsoleType(String consoleName) {
        this.consoleName = consoleName;
    }

    public String getConsoleName() {
    	return consoleName;
    }
}
