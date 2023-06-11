/* EAIMD - eaimd.org - 2023 */
package org.eaimd.ai_md.editor;

import org.eaimd.ai_md.editor.formatter.AiMdFormatter;
import org.eaimd.ai_md.producer.AiMdProducer;
import org.eaimd.ai_md.resources.CR;
import org.eaimd.ai_md.ui.UI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.StatusLineContributionItem;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level=AccessLevel.PRIVATE)
public class AiMdMultiPageEditorContributor extends MultiPageEditorActionBarContributor {

	private abstract class ReplaceDocumentSelection {

		@SneakyThrows(BadLocationException.class)
		public ReplaceDocumentSelection() {
			super();

			TextSelection textSelection = (TextSelection) aiMdSourceTextEditor.getSelectionProvider().getSelection();
			String replacedSelection = replace(textSelection.getText());
			IDocument document = aiMdSourceTextEditor.getDocumentProvider().getDocument(aiMdSourceTextEditor.getEditorInput());
			document.replace(textSelection.getOffset(), textSelection.getLength(), replacedSelection);
		}
		
		public abstract String replace(String selection);
	}

	AiMdMultiPageEditor aiMdMultiPageEditor;
	
	//AiMdPreferencesMdFormatter mdPreferences;
	
	AiMdTextEditor aiMdSourceTextEditor;
	
	IMenuManager aiMdMenuManager;
	IToolBarManager aiMdToolBarManager;
	IStatusLineManager aiMdStatusLineManager;

	/* First page */

	private static StatusLineContributionItem statusLineLinkField;

	private static StatusLineContributionItem statusLinePositionField;

	/* Second page */
	
	Action wordWrapAction;
	String wordWrapActionId;

	Action showWhitespaceCharactersAction;
	String showWhitespaceCharactersActionId;

	Separator editSeparatorAction = new Separator();
	String editSeparatorActionId = "org.eaimd.ui.editor.ai-md.action.editSeparatorAction";

	Action formatMarkdownAction;
	String formatMarkdownActionId = "org.eaimd.ui.editor.ai-md.action.formatMarkdownAction";

	Action repairBrokenTextAction;
	String repairBrokenTextActionId = "org.eaimd.ui.editor.ai-md.action.repairBrokenTextAction";
	
	Separator formatSeparatorAction = new Separator();
	String formatSeparatorActionId = "org.eaimd.ui.editor.ai-md.action.formatSeparatorAction";

	Action boldFormatAction;
	String boldFormatActionId = "org.eaimd.ui.editor.ai-md.action.boldFormatAction";

	Action italicFormatAction;
	String italicFormatActionId = "org.eaimd.ui.editor.ai-md.action.italicFormatAction";

	Separator aiSeparatorAction = new Separator();
	String aiSeparatorActionId = "org.eaimd.ui.editor.ai-md.action.aiSeparatorAction";

	Action aiSplitViewAction;
	final String aiSplitViewActionId = "org.eaimd.ui.editor.ai-md.action.aiSplitViewAction";

	Action aiCollapseHeadersAction;
	String aiCollapseHeadersActionId = "org.eaimd.ui.editor.ai-md.action.aiCollapseHeadersAction";

	Action aiGenerateAction;
	String aiGenerateActionId = "org.eaimd.ui.editor.ai-md.action.aiGenerateAction";

	Action htmlGenerateAction;
	String htmlGenerateActionId = "org.eaimd.ui.editor.ai-md.action.htmlGenerateAction";

	private void createActions() {
		
		
		/* Second page */
		
		/* Format Selected Markdown Source Text */
		formatMarkdownAction = UI.ActionFactory.create(formatMarkdownActionId, "Format Markdown source", "Format selected Markdown source text",
				CR.getImageDescriptor("markdown-action-format-md"), () -> {
			new ReplaceDocumentSelection() {
				@Override
				public String replace(String selection) {
					return AiMdFormatter.formatMarkdown(selection, aiMdMultiPageEditor.getMdPreferences());
				}
			};
		});

		/* Format Selected Markdown Source Text */
		repairBrokenTextAction = UI.ActionFactory.create(repairBrokenTextActionId, "Repair broken text paragraphs", "Repair broken text to re-create paragraphs",
				CR.getImageDescriptor("markdown-action-repair-paragraph"), () -> {
			new ReplaceDocumentSelection() {
				@Override
				public String replace(String selection) {
					return AiMdFormatter.repairBrokenText(selection);
				}
			};
		});

			boldFormatAction = UI.ActionFactory.create(boldFormatActionId, "Bold format selected text", "Markdown Bold format selected text",
			CR.getImageDescriptor("PD_Toolbar_bold"), () -> {
				new ReplaceDocumentSelection() {
					@Override
					public String replace(String selection) {
						String selectionTrim = selection.trim();
						if (selectionTrim.startsWith("**") && selectionTrim.endsWith("**")) {
							int startPos = selection.indexOf("**");
							int endPos = selection.lastIndexOf("**");
							if (startPos < endPos) {
								return selection.substring(0, startPos) + selection.substring(startPos + 2,  endPos) + selection.substring(endPos + 2);
							}
						}
						return "**" + selection + "**";
					}
				};
		});
		
			italicFormatAction = UI.ActionFactory.create(italicFormatActionId, "Italic format selected text", "Markdown Italic format selected text",
				CR.getImageDescriptor("PD_Toolbar_italic"), () -> {
			new ReplaceDocumentSelection() {
				@Override
				public String replace(String selection) {
					String selectionTrim = selection.trim();
					if (selectionTrim.startsWith("*") && selectionTrim.endsWith("*")) {
						int startPos = selection.indexOf("*");
						int endPos = selection.lastIndexOf("*");
						if (startPos < endPos) {
							return selection.substring(0, startPos) + selection.substring(startPos + 1,  endPos) + selection.substring(endPos + 1);
						}
					}
					return "*" + selection + "*";
				}
			};
		});

			aiSplitViewAction = UI.ActionFactory.create(aiSplitViewActionId, "Split view", "Split view", CR.getImageDescriptor("split_vertical"), () -> {
			boolean isEditorSplitView = !aiMdMultiPageEditor.isEditorSplitView();
			aiMdMultiPageEditor.setEditorSplitView(isEditorSplitView);
			aiSplitViewAction.setChecked(isEditorSplitView);
			
			aiMdMultiPageEditor.changeSplitView(isEditorSplitView);
		});
		/* Initial */
		aiSplitViewAction.setChecked(true);

			aiCollapseHeadersAction = UI.ActionFactory.create(aiCollapseHeadersActionId, "Collapse all AI headers", "Collapse all AI text headers",
				CR.getImageDescriptor("collapseall"), () -> {
			aiMdSourceTextEditor.collapseAllReferenceFoldingAnnotations();
		});

			aiGenerateAction = UI.ActionFactory.create(aiGenerateActionId, "Generate", "Generate", CR.getImageDescriptor("run"), () -> {

			AiMdProducer.generateMdFromAiMd(aiMdSourceTextEditor.getDocument().get(), aiMdSourceTextEditor.getCursorPositionString(), aiMdMultiPageEditor.findTargetFileName());
		});
		
			htmlGenerateAction = UI.ActionFactory.create(aiGenerateActionId, "Generate HTML", "Generate HTML", CR.getImageDescriptor("run"), () -> {

			aiMdMultiPageEditor.viewHtmlFromAiMd();
		});
	}

	@Override
	public void contributeToMenu(IMenuManager menuManager) {
		aiMdMenuManager = new MenuManager("AI MD");
		menuManager.prependToGroup(IWorkbenchActionConstants.MB_ADDITIONS, aiMdMenuManager);
//		aiMdMenuManager.add(aiGenerateAction);
//		aiMdMenuManager.add(new Separator());
	}

	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		aiMdToolBarManager = toolBarManager;
//		aiMdToolBarManager.add(aiGenerateAction);
//		aiMdToolBarManager.add(new Separator());
	}
	
	@Override
	public void contributeToStatusLine(IStatusLineManager statusLineManager) {
		aiMdStatusLineManager = statusLineManager;
		//statusLineManager.add(statusLineLinkField);
		super.contributeToStatusLine(statusLineManager);
	}

	public AiMdMultiPageEditorContributor() {
		super();

		createActions();
		
		statusLinePositionField = new StatusLineContributionItem("statusLinePositionField", 80);
		statusLinePositionField.setText("0");
		
		statusLineLinkField = new StatusLineContributionItem("statusLineLinkField", 120);
		statusLineLinkField.setText("");

	}
	
	@Override
	public void setActiveEditor(IEditorPart editorPart) {

		if (editorPart == null) {
			return;
		}
		
		aiMdMultiPageEditor = (AiMdMultiPageEditor) editorPart;
		
		aiMdSourceTextEditor = aiMdMultiPageEditor.getAiMdTextEditor();
//		aiMdPreferences = aiMdMultiPageEditor.getPreferences();
		
			wordWrapAction = (Action) aiMdSourceTextEditor.getAction(ITextEditorActionConstants.WORD_WRAP);
		wordWrapAction.setImageDescriptor(CR.getImageDescriptor("wordwrap"));
		wordWrapActionId = wordWrapAction.getId();

			showWhitespaceCharactersAction = (Action) aiMdSourceTextEditor.getAction(ITextEditorActionConstants.SHOW_WHITESPACE_CHARACTERS);
		showWhitespaceCharactersAction.setImageDescriptor(CR.getImageDescriptor("show_whitespace_chars"));
		showWhitespaceCharactersActionId = showWhitespaceCharactersAction.getId();
		
		super.setActiveEditor(aiMdMultiPageEditor);
	}
	
	@Override
	public void setActivePage(IEditorPart editorPart) {
		
		IActionBars actionBars = getActionBars();
		if ((aiMdMultiPageEditor == null) || (actionBars == null)) {
			return;
		}

		aiMdToolBarManager.remove(wordWrapActionId);
		aiMdToolBarManager.remove(showWhitespaceCharactersActionId);
		
		aiMdToolBarManager.remove(editSeparatorActionId);

		aiMdMenuManager.remove(formatMarkdownActionId);
		aiMdToolBarManager.remove(formatMarkdownActionId);
		
		aiMdMenuManager.remove(repairBrokenTextActionId);
		aiMdToolBarManager.remove(repairBrokenTextActionId);

		aiMdToolBarManager.remove(formatSeparatorActionId);
		
		aiMdToolBarManager.remove(boldFormatActionId);
		aiMdToolBarManager.remove(italicFormatActionId);

		aiMdToolBarManager.remove(aiSeparatorActionId);
		
		aiMdToolBarManager.remove(aiSplitViewActionId);
		aiMdToolBarManager.remove(aiCollapseHeadersActionId);
		aiMdToolBarManager.remove(aiGenerateActionId);
		aiMdToolBarManager.remove(htmlGenerateActionId);
		
		/* Status line */
		aiMdStatusLineManager.add(statusLinePositionField);
		statusLinePositionField.setText(aiMdSourceTextEditor.getCursorPositionString());
		aiMdStatusLineManager.add(statusLineLinkField);
		
		/* Update */
		aiMdToolBarManager.update(true);

		if (aiMdMultiPageEditor.getActivePage() == aiMdMultiPageEditor.getAiMdPreferencesEditorPageIndex()) {
			/* Update */
			aiMdToolBarManager.update(true);
			aiMdStatusLineManager.update(true);
		}
		if (aiMdMultiPageEditor.getActivePage() == aiMdMultiPageEditor.getAiMdTextEditorPageIndex()) {

			aiMdToolBarManager.add(wordWrapAction);
			aiMdToolBarManager.add(showWhitespaceCharactersAction);
			
			aiMdToolBarManager.add(editSeparatorAction);

			aiMdMenuManager.add(formatMarkdownAction);
			aiMdToolBarManager.add(formatMarkdownAction);

			aiMdMenuManager.add(repairBrokenTextAction);
			aiMdToolBarManager.add(repairBrokenTextAction);

			aiMdToolBarManager.add(formatSeparatorAction);

			aiMdToolBarManager.add(boldFormatAction);
			aiMdToolBarManager.add(italicFormatAction);
			
			aiMdToolBarManager.add(aiSeparatorAction);
			
			aiMdToolBarManager.add(aiSplitViewAction);
			aiMdToolBarManager.add(aiCollapseHeadersAction);
			aiMdToolBarManager.add(aiGenerateAction);
			aiMdToolBarManager.add(htmlGenerateAction);

			
			/* Update */
			aiMdToolBarManager.update(true);
			aiMdStatusLineManager.update(true);
		}
	}

	public static StatusLineContributionItem getStatusLineLinkField() {
		return statusLineLinkField;
	}
	
	public static StatusLineContributionItem getStatusLinePositionField() {
		return statusLinePositionField;
	}
}
