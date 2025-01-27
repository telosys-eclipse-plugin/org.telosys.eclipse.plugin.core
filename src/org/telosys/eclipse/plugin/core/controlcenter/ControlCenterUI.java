package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class ControlCenterUI {
	
    private final IProject project;
    
	public ControlCenterUI(IProject project) {
		super();
		this.project = project;
	}

	protected void createUI(Composite parent) {
		// Create your SWT widgets here

		
//		Composite container = parent;
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false)); // 1 col
		
		// Example: Label and Text Input Field
		new Label(container, SWT.NONE).setText("Project Name: " + project.getName());

		TabFolder tabFolder = new TabFolder(container, SWT.NONE);

//		TabItem tabItem1 = new TabItem(tabFolder, SWT.NONE);
//		tabItem1.setText("  Tab 1  ");
//		Composite tabContent1 = new Composite(tabFolder, SWT.NONE);
//		createTabContent1(tabContent1, "Tab content 1");
//		tabItem1.setControl(tabContent1);
		createTab1(tabFolder, "  Tab 2  ") ;
		
//		TabItem tabItem2 = new TabItem(tabFolder, SWT.NONE);
//		tabItem2.setText("  Tab 2  ");
//		Composite tabContent2 = new Composite(tabFolder, SWT.NONE);
//		createTabContent2(tabContent2,  "Tab content 2");
//		tabItem2.setControl(tabContent2);
		createTab2(tabFolder, "  Tab 2  ") ;
	}

	private void createTab1(TabFolder tabFolder, String tabTitle) {
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(tabTitle);
		Composite tabContent = new Composite(tabFolder, SWT.NONE);
		createTabContent1(tabContent, "Tab content 1");
		tabItem.setControl(tabContent);
	}
	private void createTabContent1(Composite tabContent, String s) {
		tabContent.setLayout(new GridLayout(1, false)); // One column layout
		new Label(tabContent, SWT.NONE).setText(s);
		Button button = new Button(tabContent, SWT.PUSH);
        button.setText("Click Me");
        
	}
	
	private void createTab2(TabFolder tabFolder, String tabTitle) {
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(tabTitle);
		Composite tabContent = new Composite(tabFolder, SWT.NONE);
		createTabContent2(tabContent, "Tab content 2");
		tabItem.setControl(tabContent);
	}
	private void createTabContent2(Composite tabContent, String s) {
		new Label(tabContent, SWT.NONE).setText(s);
		
	}
}
