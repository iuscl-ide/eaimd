/* EAIMD - eaimd.org - 2023 */
package org.eaimd.ai_md.editor.content_assist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import org.eaimd.ai_md.parser.AiMdParser;
import org.eaimd.ai_md.resources.CR;
import org.eaimd.ai_md.utils.CU;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessorExtension;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import com.vladsch.flexmark.ast.Code;
import com.vladsch.flexmark.ast.CodeBlock;
import com.vladsch.flexmark.ast.Emphasis;
import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.HtmlBlock;
import com.vladsch.flexmark.ast.IndentedCodeBlock;
import com.vladsch.flexmark.ast.StrongEmphasis;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.ast.NodeVisitor;
import com.vladsch.flexmark.util.ast.VisitHandler;
import com.vladsch.flexmark.util.ast.Visitor;

public class AiMdContentAssistProcessor implements IContentAssistProcessorExtension {

	public static enum AiMdCompletionProposalKey {
		AI_BLOCKS, AI_ID,
		
		BOLD_TEXT, ITALIC_TEXT, BOLD_ITALIC_TEXT,
		STRIKETROUGH_TEXT, QUOTED_TEXT,
		INLINE_CODE, FENCED_CODE_BLOCK, INDENTED_CODE_BLOCK,
		HEADING_LEVEL_1, HEADING_LEVEL_2, HEADING_LEVEL_3, HEADING_LEVEL_4, HEADING_LEVEL_5, HEADING_LEVEL_6,
		UNORDERED_LIST, ORDERED_LIST, DEFINITION_LIST,
		LINK, IMAGE,
		HORIZONTAL_RULE,
		SIMPLE_TABLE, DETAILED_TABLE,
		TASKS
		};

	private static final char[] NO_CHARS = new char[0];

	private static final LinkedHashMap<AiMdCompletionProposalKey, AiMdCompletionProposal> defaultProposals = new LinkedHashMap<>();
	
	private static final LinkedHashMap<String, AiMdCompletionProposal> warningProposals = new LinkedHashMap<>();

	static {
		defaultProposals.putAll(CR.getAiMdCompletionProposals());
		
		warningProposals.put("Html", new AiMdCompletionProposal("No Markdown elements available inside an HTML block",
				"",
				0, 0, 0,
				CR.getImage("message_warning"),
				null, null,
				0, 0));
		warningProposals.put("Code", new AiMdCompletionProposal("No Markdown elements available inside a code inline or code block",
				"",
				0, 0, 0,
				CR.getImage("message_warning"),
				null, null,
				0, 0));
	}
	
	private static final ArrayList<String> linearNodes = new ArrayList<>();
	private static final int[] cursor = new int[1];
		
	public void visitNode(Node node) {
		
		if ((node.getStartOffset() <= cursor[0]) && (cursor[0] <= node.getEndOffset())) {
			linearNodes.add(node.getClass().getSimpleName());
		}
	}
	
	private NodeVisitor nodeVisitor = new NodeVisitor(
		new VisitHandler<HtmlBlock>(HtmlBlock.class, new Visitor<HtmlBlock>() {
			@Override
			public void visit(HtmlBlock htmlBlock) {
				visitNode(htmlBlock);
				nodeVisitor.visitChildren(htmlBlock);
			}
		}),

		new VisitHandler<Heading>(Heading.class, new Visitor<Heading>() {
			@Override
			public void visit(Heading heading) {
				visitNode(heading);
				nodeVisitor.visitChildren(heading);
			}
		}),

		new VisitHandler<Emphasis>(Emphasis.class, new Visitor<Emphasis>() {
		@Override
		public void visit(Emphasis emphasis) {
			visitNode(emphasis);
			nodeVisitor.visitChildren(emphasis);
		}
		}),
		new VisitHandler<StrongEmphasis>(StrongEmphasis.class, new Visitor<StrongEmphasis>() {
			@Override
			public void visit(StrongEmphasis strongEmphasis) {
				visitNode(strongEmphasis);
				nodeVisitor.visitChildren(strongEmphasis);
			}
		}),

		new VisitHandler<Code>(Code.class, new Visitor<Code>() {
			@Override
			public void visit(Code code) {
				visitNode(code);
			}
		}),
		new VisitHandler<FencedCodeBlock>(FencedCodeBlock.class, new Visitor<FencedCodeBlock>() {
			@Override
			public void visit(FencedCodeBlock fencedCodeBlock) {
				visitNode(fencedCodeBlock);
			}
		}),
		new VisitHandler<IndentedCodeBlock>(IndentedCodeBlock.class, new Visitor<IndentedCodeBlock>() {
			@Override
			public void visit(IndentedCodeBlock indentedCodeBlock) {
				visitNode(indentedCodeBlock);
			}
		}),
		new VisitHandler<CodeBlock>(CodeBlock.class, new Visitor<CodeBlock>() {
			@Override
			public void visit(CodeBlock codeBlock) {
				visitNode(codeBlock);
			}
		})
	);

	public ICompletionProposal[] runContentAssist(String markdownString, int offset, String lineDelimiter) {
		
		//ArrayList<ICompletionProposal> completionProposals = new ArrayList<>();
		
		/* Parse document in a node */
		Node documentNode = AiMdParser.getParser().parse(markdownString);
		linearNodes.clear();
		cursor[0] = offset;
		nodeVisitor.visitChildren(documentNode);
		//L.p(linearNodes.toString());

		AiMdCompletionProposal[] completionProposals;

		LinkedHashSet<AiMdCompletionProposalKey> validProposals = new LinkedHashSet<AiMdCompletionProposalKey>(Arrays.asList(AiMdCompletionProposalKey.values()));
		AiMdCompletionProposal warningProposal = null;
		
		if (linearNodes.size() == 0) {
			/* OK */
		}
		else if (linearNodes.contains("Code") || linearNodes.contains("FencedCodeBlock") ||
				linearNodes.contains("IndentedCodeBlock") || linearNodes.contains("CodeBlock")) {
			warningProposal = warningProposals.get("Code");
		}
		else if (linearNodes.contains("HtmlBlock")) {
			warningProposal = warningProposals.get("Html");
		}
		else {
			if (linearNodes.contains("Heading")) {
				validProposals.remove(AiMdCompletionProposalKey.HEADING_LEVEL_1);
				validProposals.remove(AiMdCompletionProposalKey.HEADING_LEVEL_2);
				validProposals.remove(AiMdCompletionProposalKey.HEADING_LEVEL_3);
				validProposals.remove(AiMdCompletionProposalKey.HEADING_LEVEL_4);
				validProposals.remove(AiMdCompletionProposalKey.HEADING_LEVEL_5);
				validProposals.remove(AiMdCompletionProposalKey.HEADING_LEVEL_6);
			}
			if (linearNodes.get(linearNodes.size() - 1).equals("Emphasis")) {
				validProposals.remove(AiMdCompletionProposalKey.ITALIC_TEXT);
				validProposals.remove(AiMdCompletionProposalKey.BOLD_ITALIC_TEXT);
			}
			if (linearNodes.get(linearNodes.size() - 1).equals("StrongEmphasis")) {
				validProposals.remove(AiMdCompletionProposalKey.BOLD_TEXT);
				validProposals.remove(AiMdCompletionProposalKey.BOLD_ITALIC_TEXT);
			}
		}

		if (warningProposal != null) {
			completionProposals = new AiMdCompletionProposal[1];
			warningProposal.setReplacementOffset(offset);
			completionProposals[0] = warningProposal;
			return completionProposals;
		}
		
		completionProposals = new AiMdCompletionProposal[validProposals.size()];
		int index = 0;
		int lineDelimiterChars = lineDelimiter.length();
		for (AiMdCompletionProposalKey validProposal : validProposals) {
			AiMdCompletionProposal completionProposal = defaultProposals.get(validProposal);
			//completionProposal.setReplacementString(completionProposal.getReplacementString().replaceAll("\\CR", lineDelimiter));
			if (validProposal == AiMdCompletionProposalKey.AI_BLOCKS) {
				String uniqueId = CU.generateUniqueId();
				String replacement = completionProposal.getReplacementString();
				int startReplacementIndex = replacement.indexOf("id: ") + 4;
				completionProposal.setReplacementString(replacement.substring(0, startReplacementIndex) + uniqueId + replacement.substring(startReplacementIndex + 8));
			}
			if (validProposal == AiMdCompletionProposalKey.AI_ID) {
				completionProposal.setReplacementString(CU.generateUniqueId());
			}
			completionProposal.setReplacementOffset(offset);
			completionProposal.setCursorPosition(completionProposal.getCursorPositionChars() +
					completionProposal.getCursorPositionLineDelimiters() * lineDelimiterChars);
			completionProposals[index] = completionProposal;
			index++;
		}
		
		return completionProposals;
		// defaultProposals.values().toArray(new ICompletionProposal[defaultProposals.values().size()]);
	}
	
	public char[] getAutoActivationCharacters() {
	
		return NO_CHARS;
	}
	
	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer textViewer, int offset) {
		
		Document document = (Document) textViewer.getDocument();
		String aiMdString = document.get();
		String lineDelimiter = document.getDefaultLineDelimiter();
		
		return runContentAssist(aiMdString, offset, lineDelimiter);
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer textViewer, int offset) {
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

	@Override
	public boolean isCompletionProposalAutoActivation(char paramChar, ITextViewer paramITextViewer, int paramInt) {
		return false;
	}

	@Override
	public boolean isContextInformationAutoActivation(char paramChar, ITextViewer paramITextViewer, int paramInt) {
		return false;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

}
