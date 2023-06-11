/* EAIMD - eaimd.org - 2023 */
package org.eaimd.ai_md.editor.folding;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.reconciler.Reconciler;
import org.eclipse.jface.text.source.projection.ProjectionViewer;

public class AiMdFoldingReconciler extends Reconciler {

    private AiMdFoldingReconcilerStrategy aiMdFoldingReconcilerStrategy;

    public AiMdFoldingReconciler() {
    	
    	this.setIsIncrementalReconciler(false);
    	
        aiMdFoldingReconcilerStrategy = new AiMdFoldingReconcilerStrategy();
        this.setReconcilingStrategy(aiMdFoldingReconcilerStrategy, IDocument.DEFAULT_CONTENT_TYPE);
    }

    @Override
    public void install(ITextViewer textViewer) {
        super.install(textViewer);
        
        aiMdFoldingReconcilerStrategy.setProjectionViewer((ProjectionViewer) textViewer);
    }
}