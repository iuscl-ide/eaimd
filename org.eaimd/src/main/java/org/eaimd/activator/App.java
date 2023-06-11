/* EAIMD - eaimd.org - 2023 */
package org.eaimd.activator;

import org.eaimd.ai_md.resources.CR;
import org.eaimd.ai_md.ui.UI;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import lombok.Getter;

public class App {

	@Getter
	private static UI ui;
	
	public static void initialize() {
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
		Shell applicationShell = workbenchWindow.getShell();

		ui = new UI(false, applicationShell.getDisplay());
		ui.computeSizes(applicationShell);
		
		/* Resources */
		CR.load(ui, applicationShell);
	}
	
}
