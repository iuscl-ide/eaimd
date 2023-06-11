/* EAIMD - eaimd.org - 2023 */
package org.eaimd.ai_md.resources;

import java.util.LinkedHashMap;

import org.eaimd.ai_md.editor.content_assist.AiMdCompletionProposal;
import org.eaimd.ai_md.editor.content_assist.AiMdContentAssistProcessor.AiMdCompletionProposalKey;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AiMdCompletionProposalsSupport {

	@Getter
	LinkedHashMap<AiMdCompletionProposalKey, AiMdCompletionProposal> completionProposals = new LinkedHashMap<>();
}
