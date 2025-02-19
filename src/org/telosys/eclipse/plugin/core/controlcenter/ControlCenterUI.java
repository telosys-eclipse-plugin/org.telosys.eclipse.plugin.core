package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.telosys.eclipse.plugin.core.telosys.TelosysConsoleForEclipse;
import org.telosys.eclipse.plugin.core.telosys.commons.ConsoleColor;

public class ControlCenterUI {
	
	private final Font boldFont;
    private final IProject project;
//    private final TelosysEclipseConsole telosysConsole  = new TelosysEclipseConsole("Telosys");
//    private final TelosysEclipseConsole telosysConsole2 = new TelosysEclipseConsole("Telosys #2");
    
	public ControlCenterUI(IProject project, Font boldFont) {
		super();
		this.project = project;
		this.boldFont = boldFont;
	}
	private void createStyledText(Composite composite) {
		String part1 = "Telosys Control Center for project " ;
		String part2 = project.getName();
		String fullText = part1 + part2;  
        // Create a StyledText widget
        StyledText styledText = new StyledText(composite, SWT.READ_ONLY );
        styledText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        styledText.setText(fullText);
        // Define bold style range 
        StyleRange style = new StyleRange();
        style.start = part1.length();
        style.length = part2.length();
        style.fontStyle = SWT.BOLD;
        // Apply bold style on StyledText
        styledText.setStyleRange(style);
	}
	protected void createUI(Composite parent) {
		// Create your SWT widgets here
		parent.setLayout(new GridLayout(1, false)); // 1 col
		
		Composite container = parent;
		
		// Label 
		createStyledText(container);

		TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); // Fill remaining space

		ControlCenterTab1 tab1 = new ControlCenterTab1(project, boldFont);
		tab1.createTab(tabFolder);

		ControlCenterTab2 tab2 = new ControlCenterTab2(project, boldFont);
		tab2.createTab(tabFolder);

		ControlCenterTabTests tabTests = new ControlCenterTabTests();
		tabTests.createTab(tabFolder);
//		createTab1(tabFolder, "  Tab 1  ") ;
		
//		createTab2(tabFolder, "  Tab 2  ") ;
		
	}

//	private void createTab1(TabFolder tabFolder, String tabTitle) {
//		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
//		tabItem.setText(tabTitle);
//		Composite tabContent = new Composite(tabFolder, SWT.NONE);
//		createTabContent1(tabContent, "Tab content 1");
//		tabItem.setControl(tabContent);
//	}
//	private void createTabContent1(Composite tabContent, String s) {
//		tabContent.setLayout(new GridLayout(1, false)); // One column layout
//		new Label(tabContent, SWT.NONE).setText(s);
//		
//		Button button ;
//		
//		button = new Button(tabContent, SWT.PUSH);
//        button.setText("activate console 1");
//        button.addListener(SWT.Selection, event -> {
//        	telosysConsole.showConsoleView();
//        	//telosysConsole.activate();
//        });       
//
//        button = new Button(tabContent, SWT.PUSH);
//        button.setText("Print in console 1");
//        button.addListener(SWT.Selection, event -> {
//        	telosysConsole.println("Hello in Telosys console!");
//        });       
//        
//        
//		button = new Button(tabContent, SWT.PUSH);
//        button.setText("activate console #2");
//        button.addListener(SWT.Selection, event -> {
//        	//telosysConsole2.activate();
//        	telosysConsole2.showConsoleView();
//        });       
//
//        button = new Button(tabContent, SWT.PUSH);
//        button.setText("Print in console #2");
//        button.addListener(SWT.Selection, event -> {
//        	telosysConsole2.println("Hello in Telosys console #2 !");
//        });       
//        
//        button = new Button(tabContent, SWT.PUSH);
//        button.setText("Print RED in console #2");
//        button.addListener(SWT.Selection, event -> {
//        	telosysConsole2.println("Hello RED in Telosys console #2 !", MsgColor.RED);
//        });       
//        
//        button = new Button(tabContent, SWT.PUSH);
//        button.setText("Print BOX in console #2");
//        button.addListener(SWT.Selection, event -> {
//        	telosysConsole2.println("┌────────┐");
//        	telosysConsole2.println("│  Box   │");
//        	telosysConsole2.println("└────────┘");
//        });       
//	}
	
//	private void createTab2(TabFolder tabFolder, String tabTitle) {
//		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
//		tabItem.setText(tabTitle);
//		Composite tabContent = new Composite(tabFolder, SWT.NONE);
//		createTabContent2(tabContent, "Tab content 2");
//		tabItem.setControl(tabContent);
//	}
//	private void createTabContent2(Composite tabContent, String s) {
//		new Label(tabContent, SWT.NONE).setText(s);
//		
//	}
}
