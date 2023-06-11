/* EAIMD - eaimd.org - 2023 */
package org.eaimd.ai_md.editor.outline;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.reconciler.Reconciler;

public class AiMdOutlineReconciler extends Reconciler {

	public AiMdOutlineReconciler() {
		this.setReconcilingStrategy(new AiMdOutlineReconcilerStrategy(), IDocument.DEFAULT_CONTENT_TYPE);
	}
}