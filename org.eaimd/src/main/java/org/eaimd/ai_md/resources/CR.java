/* EAIMD - eaimd.org - 2023 */
package org.eaimd.ai_md.resources;

import java.io.InputStream;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

import org.eaimd.ai_md.editor.content_assist.AiMdCompletionProposal;
import org.eaimd.ai_md.editor.content_assist.AiMdContentAssistProcessor.AiMdCompletionProposalKey;
import org.eaimd.ai_md.ui.UI;
import org.eaimd.ai_md.utils.CU;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class CR {
	
	//	public enum Colors {
//		DEFAULT_FG_COLOR, HEADING_FG_COLOR,
//		HEADER_COMMENT_FG_COLOR, COMMENT_FG_COLOR,
//		DIVIDER_LINE_FG_COLOR,
//		LINK_FG_COLOR,
//		IMAGE_BG_COLOR,
//		HTML_FG_COLOR,
//		FENCED_CODE_BG_COLOR,
//		INDENTED_CODE_BG_COLOR,
//		INLINE_CODE_BG_COLOR;
//	}
	
	public enum Colors { ControlFace, ControlLight, ControlShadow,
		ListFont, ListFont66, ListFont33, ListFontSelected,
		List, 
		ListSelectedAndFocus, ListSelectedNotFocus,
		ListSelectedSecondaryAndFocus, ListSelectedSecondaryNotFocus,
		ListNotSelectedButFocus };

	public enum Fonts { NORMAL, BOLD, BIGGER, BIGGER_BOLD, RECENTS, SMALLER, MONOSPACED, MONOSPACED_BIGGER }

	private final static HashMap<String, Image> resourceImageRegistry = new HashMap<>();
	private final static HashMap<String, ImageDescriptor> resourceImageDescriptorRegistry = new HashMap<>();

	private final static FormToolkit formsToolkit = new FormToolkit(Display.getCurrent());
	
	private final static HashMap<Colors, Color> colorRegistry = new HashMap<>();
	
	
	private static final EnumMap<Fonts, Font> fontRegistry = new EnumMap<>(Fonts.class);
	
	private static AiMdCompletionProposalsSupport aiMdCompletionProposalsSupport;
	
	public static void load(UI ui, Shell shell) {
		
		Display display = ui.getDisplay();
		
		/* Images, icons */
//		loadGifImageResourceToRegistry("md-file-toolbar-nottext");
		loadGifImageResourceToRegistry("html");
		loadGifImageResourceToRegistry("stylesheet");
		loadGifImageResourceToRegistry("ui_props");
		loadGifImageResourceToRegistry("PD_Toolbar_source");
		
		loadGifImageResourceToRegistry("PD_Toolbar_bold");
		loadGifImageResourceToRegistry("PD_Toolbar_bold_disabled");
		loadGifImageResourceToRegistry("PD_Toolbar_italic");
		loadGifImageResourceToRegistry("PD_Toolbar_italic_disabled");
		
		loadGifImageResourceToRegistry("unknown");
		loadGifImageResourceToRegistry("tag-image");
		loadGifImageResourceToRegistry("show_properties_view");
		loadPngImageResourceToRegistry("split_vertical");
		loadPngImageResourceToRegistry("collapseall");
		loadGifImageResourceToRegistry("run");
		
		loadGifImageResourceToRegistry("XSDIdentityConstraintDefinitionKey");
		
		loadGifImageResourceToRegistry("XML_file");
		
		loadGifImageResourceToRegistry("initializ_parameter");
		loadGifImageResourceToRegistry("initializ_parameter_context");
		
		loadGifImageResourceToRegistry("reload_nav_16");
		
		
		loadPngImageResourceToRegistry("ai-md-multi-page-editor");
		
		loadPngImageResourceToRegistry("wordwrap");
		loadPngImageResourceToRegistry("show_whitespace_chars");
		
		loadPngImageResourceToRegistry("markdown-action-format-md");
//		loadPngImageResourceToRegistry("markdown-action-word-wrap");
		loadPngImageResourceToRegistry("markdown-action-create-80");
		loadPngImageResourceToRegistry("markdown-action-repair-paragraph");
		
		loadPngImageResourceToRegistry("markdown-header");
		loadPngImageResourceToRegistry("markdown-content-assist-proposal");
		
		loadPngImageResourceToRegistry("toml-action-verify-file");
		loadPngImageResourceToRegistry("toml-action-comment");
		loadPngImageResourceToRegistry("toml-completion-proposal");
		
		loadPngImageResourceToRegistry("message_warning");
		
		
		loadPngImageResourceToRegistry("classf_generate");
		loadPngImageResourceToRegistry("fileType_filter");
		loadPngImageResourceToRegistry("refresh");
		
		loadPngImageResourceToRegistry("project");
		
		loadPngImageResourceToRegistry("add");
		loadPngImageResourceToRegistry("remove");
		loadPngImageResourceToRegistry("move-up");
		loadPngImageResourceToRegistry("move-down");
		
		loadPngImageResourceToRegistry("asterisk");
		loadPngImageResourceToRegistry("edit_template");

		loadPngImageResourceToRegistry("save");
		
//		loadGifImageResourceToRegistry("md-preference-toolbar-notdefault");
//		loadGifImageResourceToRegistry("md-preference-toolbar-apply");
//		loadGifImageResourceToRegistry("md-preference-toolbar-reload");
//		
//		loadPngImageResourceToRegistry("md-heading-1");
//		loadPngImageResourceToRegistry("md-heading-2");
//		loadPngImageResourceToRegistry("md-heading-3");
//		loadPngImageResourceToRegistry("md-heading-4");
//		loadPngImageResourceToRegistry("md-heading-5");
//		loadPngImageResourceToRegistry("md-heading-6");
//		
//		loadPngImageResourceToRegistry("md-action-word-wrap");
//		loadPngImageResourceToRegistry("md-action-format-md");
//		loadPngImageResourceToRegistry("md-action-create-80");
//		loadPngImageResourceToRegistry("md-action-repair-paragraph");
		
//		/* Colors */
//		loadColorToRegistry(Colors.DEFAULT_FG_COLOR.name(), 0, 0, 0); /* Text Black */
//		loadColorToRegistry(Colors.HEADING_FG_COLOR.name(), 187, 0, 0); /* Trac Red */
//		loadColorToRegistry(Colors.HEADER_COMMENT_FG_COLOR.name(), 63, 95, 191); /* JavaDoc Blue */
//		loadColorToRegistry(Colors.COMMENT_FG_COLOR.name(), 63, 127, 95); /* Java Comment Green */
//		loadColorToRegistry(Colors.DIVIDER_LINE_FG_COLOR.name(), 120, 120, 120); /* Line Number Gray */
//		loadColorToRegistry(Colors.LINK_FG_COLOR.name(), 0, 0, 238); /* Standard Web Link Blue */
//		loadColorToRegistry(Colors.IMAGE_BG_COLOR.name(), 255, 243, 224); /* Image Background Orange */
//		loadColorToRegistry(Colors.HTML_FG_COLOR.name(), 127, 0, 127); /* Java Keyword Purple */
//		loadColorToRegistry(Colors.FENCED_CODE_BG_COLOR.name(), 255, 255, 224); /* Code Background Yellow */
//		loadColorToRegistry(Colors.INDENTED_CODE_BG_COLOR.name(), 255, 255, 223); /* Code Background Yellow */
//		loadColorToRegistry(Colors.INLINE_CODE_BG_COLOR.name(), 254, 254, 190); /* Code Background Yellow */
		
		
		colorRegistry.put(Colors.ControlFace, display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		colorRegistry.put(Colors.ControlLight, display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		colorRegistry.put(Colors.ControlShadow, display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
		
		// SWT.COLOR_LIST_BACKGROUND 25
		colorRegistry.put(Colors.List, display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		// SWT.COLOR_LIST_FOREGROUND 24
		colorRegistry.put(Colors.ListFont, display.getSystemColor(SWT.COLOR_LIST_FOREGROUND));
		// SWT.COLOR_LIST_SELECTION_TEXT 27
		colorRegistry.put(Colors.ListFontSelected, display.getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));

		// SWT.COLOR_LIST_SELECTION 26
		colorRegistry.put(Colors.ListSelectedAndFocus, display.getSystemColor(SWT.COLOR_LIST_SELECTION)); 
		colorRegistry.put(Colors.ListSelectedNotFocus, display.getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));

		colorRegistry.put(Colors.ListSelectedSecondaryAndFocus, new Color(display,
				CR.blend(colorRegistry.get(Colors.ListSelectedAndFocus).getRGB(), colorRegistry.get(Colors.ListFontSelected).getRGB(), 80)));
		colorRegistry.put(Colors.ListSelectedSecondaryNotFocus, new Color(display,
				CR.blend(colorRegistry.get(Colors.ListSelectedNotFocus).getRGB(), colorRegistry.get(Colors.ListFontSelected).getRGB(), 70)));

		
		/* Tab before anything selected */
		colorRegistry.put(Colors.ListNotSelectedButFocus, colorRegistry.get(Colors.ControlFace));
		
		
		/* Fonts */
		Font defaultFont = shell.getFont();
		int defaultFontHeight = defaultFont.getFontData()[0].getHeight();
		fontRegistry.put(Fonts.NORMAL, defaultFont);
		fontRegistry.put(Fonts.BOLD, ui.newFontAttributes(defaultFont, SWT.BOLD));
		fontRegistry.put(Fonts.BIGGER, ui.newFontSize(defaultFont, defaultFontHeight + 5));
		fontRegistry.put(Fonts.BIGGER_BOLD, ui.newFontSize(fontRegistry.get(Fonts.BOLD), defaultFontHeight + 5));
		fontRegistry.put(Fonts.RECENTS, ui.newFontSize(defaultFont, defaultFontHeight  + 1));
		fontRegistry.put(Fonts.SMALLER, ui.newFontSize(defaultFont, defaultFontHeight - 3));
		Font monospacedFont = getsMonospacedFont(display);
		fontRegistry.put(Fonts.MONOSPACED, monospacedFont);
		fontRegistry.put(Fonts.MONOSPACED_BIGGER, ui.newFontSize(monospacedFont, monospacedFont.getFontData()[0].getHeight() + 5));
		
		
		AiMdCompletionProposalsSupport markdownProposalsSupport = CU.tomlDeserialize(getTextResourceAsString("texts/markdown-content-assist-proposals.toml"), AiMdCompletionProposalsSupport.class);

		aiMdCompletionProposalsSupport = CU.tomlDeserialize(getTextResourceAsString("texts/aimd-content-assist-proposals.toml"), AiMdCompletionProposalsSupport.class);
		aiMdCompletionProposalsSupport.getCompletionProposals().putAll(markdownProposalsSupport.getCompletionProposals());
		
		for (AiMdCompletionProposal completionProposal : aiMdCompletionProposalsSupport.getCompletionProposals().values()) {
			completionProposal.setImage(getImage("markdown-content-assist-proposal"));
		}
	}

	private static int blend(int v1, int v2, int ratio) {
		
		return (ratio * v1 + (100 - ratio) * v2) / 100;
	}

	public static RGB blend(RGB c1, RGB c2, int ratio) {
		
		int r = blend(c1.red, c2.red, ratio);
		int g = blend(c1.green, c2.green, ratio);
		int b = blend(c1.blue, c2.blue, ratio);
		return new RGB(r, g, b);
	}

	private static void loadGifImageResourceToRegistry(String name) {
		
		loadImageResourceToRegistry(name, "gif");
	}

	private static void loadPngImageResourceToRegistry(String name) {
		
		loadImageResourceToRegistry(name, "png");
	}

	private static void loadImageResourceToRegistry(String name, String ext) {
		
		Image registryImage = getResourceAsImage("icons/" + name + "." + ext);
		resourceImageRegistry.put(name, registryImage);
		resourceImageDescriptorRegistry.put(name, ImageDescriptor.createFromImage(registryImage));
	}

	public static Image getImage(String name) {
		
		return resourceImageRegistry.get(name);
	}

	public static ImageDescriptor getImageDescriptor(String name) {
		
		return resourceImageDescriptorRegistry.get(name);
	}

	public static InputStream getResourceAsInputStream(String resourceName) {
		
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
	}
	
	public static Image getResourceAsImage(String imageResourceName) {
		
		InputStream inputStream = getResourceAsInputStream(imageResourceName);
		return new Image(Display.getDefault(), inputStream);
	}

	public static String getTextResourceAsString(String textResourceName) {
		
		return CU.loadInputStreamInString(getResourceAsInputStream(textResourceName));
	}

	public static FormToolkit getFormsToolkit() {
		return formsToolkit;
	}

	public static Font findFont(Fonts fonts) {
		return fontRegistry.get(fonts);
	}

	private static Font getsMonospacedFont(Display display) {

		String os = System.getProperty("os.name");
		os = CU.deleteWhitespace(os).toLowerCase(Locale.US);

		String fontSerialization = "";
		
		if (os.contains("windows")) {
			fontSerialization = "Consolas,10";
		}
		else {
			fontSerialization = "Liberation Mono,10";
		}

//		switch (os) {
//		case "linux":
//			fontSerialization = "adobe-courier,10";
//			break;
//		case "macosx":
//			fontSerialization = "Monaco,10";
//			break;
//		case "windows":
//			fontSerialization = "Consolas,10";
//			break;
//		default:
//			fontSerialization = "Courier New,10";
//			break;
//		}
		
		return new Font(display, toFontData(fontSerialization));
	}

	public static FontData toFontData(Object fontSerialization) {
		
		return toFontData(fontSerialization.toString());
	}

	public static FontData toFontData(String fontSerialization) {
		
		String[] splitValues = fontSerialization.split(",");
		int index = 0;
		String name = "";
		int height = 0;
		int style = SWT.NORMAL;
		
		for (String splitValue : splitValues) {
			splitValue = splitValue.trim();
			switch (index) {
				case 0: 
					name = splitValue;
					break;
				case 1:
					height = Integer.valueOf(splitValue);
					break;
				case 2, 3:
					style = style | (splitValue.equalsIgnoreCase("bold") ? SWT.BOLD : SWT.ITALIC);
					break;
				default:
					/* ILB */
			}
			index++;
		}

		return new FontData(name, height, style);
	}

//	//	private static void loadColorToRegistry(String name, int red, int green, int blue) {
//		
//		colorRegistry.put(name, new Color(Display.getDefault(), red, green, blue));
//	}
	
//	//	public static Color getColor(String name) {
//		return colorRegistry.get(name);
//	}

	public static Color getColor(Colors colors) {
		return colorRegistry.get(colors);
	}

	//	public static IPreferenceStore getPreferenceStore() {
//		return preferenceStore;
//	}
	
	public static LinkedHashMap<AiMdCompletionProposalKey, AiMdCompletionProposal> getAiMdCompletionProposals() {
		
		return aiMdCompletionProposalsSupport.getCompletionProposals();
	}
}
