package org.telosys.eclipse.plugin.core.commons;

public class ComboItem {

	private final int index ;
	private final String value;
	
	public ComboItem(int index, String value) {
		super();
		this.index = index;
		this.value = value;
	}

	public int getIndex() {
		return index;
	}

	public String getValue() {
		return value;
	}
}
