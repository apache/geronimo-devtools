package org.apache.geronimo.st.ui.refactoring;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ltk.core.refactoring.TextFileChange;

public class MovedTextFileChange extends TextFileChange {

	private IFile fCurrentFile;

	public MovedTextFileChange(String name, IFile newFile, IFile currentFile) {
		super(name, newFile);
		fCurrentFile = currentFile;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.TextChange#getCurrentDocument(org.eclipse.core.runtime.IProgressMonitor)
	 * 
	 * Override getCurrentDocument to return the document of the fCurrentFile instead of the fFile.  Since fFile
	 * does not exist, it creates problems when displaying preview information
	 */
	public IDocument getCurrentDocument(IProgressMonitor pm) throws CoreException {
		if (pm == null)
			pm = new NullProgressMonitor();
		IDocument result = null;
		pm.beginTask("", 2); //$NON-NLS-1$
		ITextFileBufferManager manager = FileBuffers.getTextFileBufferManager();
		try {
			IPath path = fCurrentFile.getFullPath();
			manager.connect(path, LocationKind.NORMALIZE, pm);
			ITextFileBuffer buffer = manager.getTextFileBuffer(path, LocationKind.NORMALIZE);
			result = buffer.getDocument();
		} finally {
			if (result != null)
				manager.disconnect(fCurrentFile.getFullPath(), LocationKind.NORMALIZE, pm);
		}
		pm.done();
		return result;
	}

}
