/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
