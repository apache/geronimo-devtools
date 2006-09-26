package org.apache.geronimo.st.ui.commands;

import org.apache.geronimo.st.core.GeronimoServerDelegate;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.server.core.IServerWorkingCopy;

public class SetVMArgsCommand extends ServerCommand {
	
	protected String args;

	protected String oldArgs;
	
	GeronimoServerDelegate gs;

	public SetVMArgsCommand(IServerWorkingCopy server, String args) {
		super(server, args);
		this.args = args;
	}

	public void execute() {
		gs = (GeronimoServerDelegate) server.getAdapter(GeronimoServerDelegate.class);
		if (gs == null) {
			gs = (GeronimoServerDelegate) server.loadAdapter(GeronimoServerDelegate.class, new NullProgressMonitor());
		}
		oldArgs = gs.getVMArgs();
		gs.setVMArgs(args);
	}

	public void undo() {
		if (gs != null) {
			gs.setVMArgs(oldArgs);
		}
	}

}
