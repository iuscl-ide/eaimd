/* EAIMD - eaimd.org - 2023 */
package org.eaimd.ai_md.editor.folding;

import org.eclipse.jface.text.Position;

public class AiMdReferenceHeaderFoldingPosition extends Position {

	public AiMdReferenceHeaderFoldingPosition() {
		super();
	}

	public AiMdReferenceHeaderFoldingPosition(int offset, int length) {
		super(offset, length);
	}

	public AiMdReferenceHeaderFoldingPosition(int offset) {
		super(offset);
	}
}
