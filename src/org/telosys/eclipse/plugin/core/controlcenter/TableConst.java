package org.telosys.eclipse.plugin.core.controlcenter;

import org.eclipse.swt.SWT;

public class TableConst {

	/*
	 * Selection: 
	 *  SWT.MULTI (multiple line selection) or SWT.SINGLE (single line selection)
	 *  SWT.FULL_SELECTION (full row selection => a full line should be drawn)
	 *  SWT.HIDE_SELECTION (behavior when the widget loses focus )
	 *     HIDE_SELECTION hides the selection highlight whenever focus is not in the Table. However when you click on the Table it will still show.
	 */
	public static final int TABLE_STYLE = 
			  SWT.BORDER 
			| SWT.H_SCROLL | SWT.V_SCROLL 
			| SWT.SINGLE | SWT.FULL_SELECTION | SWT.HIDE_SELECTION 
			| SWT.NO_FOCUS
			| SWT.CHECK ;
	
	
//	public static final int TABLE_STYLE = 
//			  SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.NO_FOCUS | SWT.CHECK ;
// NO FOCUS = no effect 
}
