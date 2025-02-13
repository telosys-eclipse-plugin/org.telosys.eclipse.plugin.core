package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.telosys.eclipse.plugin.core.commons.MsgColor;
import org.telosys.eclipse.plugin.core.telosys.TelosysEclipseConsole;

public class ControlCenterTabTests {
	
	private static final String TAB_TITLE = "Tests ";
	
    private final TelosysEclipseConsole telosysConsole1  = new TelosysEclipseConsole("Telosys #1");
    private final TelosysEclipseConsole telosysConsole2 = new TelosysEclipseConsole("Telosys #2");



	protected TabItem createTab(TabFolder tabFolder) {
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(TAB_TITLE);
		tabItem.setControl(createTabContent(tabFolder));
		return tabItem;
	}
	
	private Composite createTabContent(TabFolder tabFolder) {
		Composite tabContent = new Composite(tabFolder, SWT.NONE);
		tabContent.setLayout(new GridLayout(1, false)); // One column layout
		
		new Label(tabContent, SWT.NONE).setText("Tab content for tests");
		
		Button button ;
		
		button = new Button(tabContent, SWT.PUSH);
        button.setText("activate console #1");
        button.addListener(SWT.Selection, event -> {
        	telosysConsole1.showConsoleView();
        	//telosysConsole.activate();
        });       

        button = new Button(tabContent, SWT.PUSH);
        button.setText("Print in console #1");
        button.addListener(SWT.Selection, event -> {
        	telosysConsole1.println("Hello in Telosys console!");
        });       
        
        
		button = new Button(tabContent, SWT.PUSH);
        button.setText("activate console #2");
        button.addListener(SWT.Selection, event -> {
        	//telosysConsole2.activate();
        	telosysConsole2.showConsoleView();
        });       

        button = new Button(tabContent, SWT.PUSH);
        button.setText("Print in console #2");
        button.addListener(SWT.Selection, event -> {
        	telosysConsole2.println("Hello in Telosys console #2 !");
        });       
        
        button = new Button(tabContent, SWT.PUSH);
        button.setText("Print RED in console #2");
        button.addListener(SWT.Selection, event -> {
        	telosysConsole2.println("Hello RED in Telosys console #2 !", MsgColor.RED);
        });       
        
        button = new Button(tabContent, SWT.PUSH);
        button.setText("Print BOX in console #2");
        button.addListener(SWT.Selection, event -> {
        	telosysConsole2.println("┌────────┐");
        	telosysConsole2.println("│  Box   │");
        	telosysConsole2.println("└────────┘");
        });       		
		return tabContent;
	}
}
