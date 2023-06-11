/* EAIMD - eaimd.org - 2023 */
package org.eaimd.ai_md.editor.outline;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Image;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level=AccessLevel.PRIVATE)
@Getter
@Setter
public class AiMdOutlineNode {
	
	public static final AiMdOutlineNode[] NO_CHILDREN = new AiMdOutlineNode[0];
	
	String label;
	Image image;

	final AiMdOutlineNode parentOutlineNode;
	ArrayList<AiMdOutlineNode> childOutlineNodes;
	
	String type;
	String subType;

	int start = 0;
	int length = 0;
	
	public AiMdOutlineNode() {
		/* root */
		parentOutlineNode = null;
	}	
	
	private AiMdOutlineNode(AiMdOutlineNode parentOutlineNode) {
		this.parentOutlineNode = parentOutlineNode;
	}
	
	public AiMdOutlineNode addChildNode() {

		AiMdOutlineNode child = new AiMdOutlineNode(this);
		if (childOutlineNodes == null) {
			childOutlineNodes = new ArrayList<>();
		}
		childOutlineNodes.add(child);
		
		return child;
	}
	
	public AiMdOutlineNode[] findChildOutlineNodes() {
		
		if (childOutlineNodes == null) {
			return NO_CHILDREN;
		}
		return (AiMdOutlineNode[]) childOutlineNodes.toArray(new AiMdOutlineNode[childOutlineNodes.size()]);
	}
}
