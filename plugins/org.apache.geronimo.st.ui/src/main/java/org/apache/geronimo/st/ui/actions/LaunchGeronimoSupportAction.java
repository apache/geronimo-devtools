package org.apache.geronimo.st.ui.actions;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.geronimo.st.ui.internal.Messages;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.internal.browser.WorkbenchBrowserSupport;
import org.eclipse.wst.server.core.IServer;

/**
 * @version $Rev$ $Date: 2006-11-05 17:47:11 -0500 (Sun, 05 Nov 2006) $
 */
public class LaunchGeronimoSupportAction implements IActionDelegate {

	public static final String WASCE_SERVER_PREFIX = "com.ibm.wasce";

	private IServer server;

	public LaunchGeronimoSupportAction() {
		super();
	}

	public URL getConsoleUrl() throws MalformedURLException {
		if (server != null) {
			return new URL(Messages.supportWebPageURL);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {

		try {
			int style = IWorkbenchBrowserSupport.AS_EDITOR
					| IWorkbenchBrowserSupport.STATUS;
			IWebBrowser browser = WorkbenchBrowserSupport.getInstance()
					.createBrowser(
							style,
							"supportWebPage",
							Messages.bind(Messages.supportWebPage, server.getName()),
							Messages.bind(Messages.supportWebPageTooltip, server
									.getName()));
			URL url = getConsoleUrl();
			if (url != null)
				browser.openURL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (PartInitException e) {
			e.printStackTrace();
		}

	}

	/*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
         *      org.eclipse.jface.viewers.ISelection)
         */
        public void selectionChanged(IAction action, ISelection selection) {

                server = (IServer) ((StructuredSelection) selection).getFirstElement();

                boolean enable = server != null
                                && server.getServerType().getId().startsWith(WASCE_SERVER_PREFIX);

                action.setEnabled(enable);

        }

}
