/* EAIMD - eaimd.org - 2023 */
package org.eaimd.ai_md.editor.formatter;

import org.eaimd.ai_md.parser.AiMdParser;
import org.eaimd.ai_md.preferences.AiMdPreferencesMdFormatter;
import org.eaimd.ai_md.utils.CU;

import com.vladsch.flexmark.formatter.Formatter;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.vladsch.flexmark.util.sequence.LineAppendable;

public class AiMdFormatter {
	
	private static MutableDataSet formatterOptions = new MutableDataSet();
	static {
		formatterOptions.setFrom(AiMdParser.getParserOptions());
		formatterOptions.set(Formatter.FENCED_CODE_SPACE_BEFORE_INFO, Boolean.valueOf(true));
	}
	private static int formatFlagsOptions = 0;
	static {
		formatFlagsOptions = formatFlagsOptions | LineAppendable.F_TRIM_TRAILING_WHITESPACE;	
	}

	
	public static String formatMarkdown(String markdownString, AiMdPreferencesMdFormatter mdPreferences) {

		MutableDataSet formatterCustomOptions = MutableDataSet.merge(formatterOptions);

		int formatFlags = formatFlagsOptions;

		if (mdPreferences != null) {
			if (mdPreferences.getFormatCollapseLineWhitespace()) {
				formatFlags = formatFlags | LineAppendable.F_COLLAPSE_WHITESPACE;
			}

			formatterCustomOptions.set(Formatter.RIGHT_MARGIN, mdPreferences.getFormatRightMarginWrap() ? mdPreferences.getFormatRightMarginColumns() : Integer.valueOf(0));
			
			formatterCustomOptions.set(Formatter.MAX_BLANK_LINES, mdPreferences.getFormatCollapseEmptyLines() ? Integer.valueOf(2) : Integer.valueOf(100));
			formatterCustomOptions.set(Formatter.MAX_TRAILING_BLANK_LINES, mdPreferences.getFormatCollapseTrailingEmptyLines() ? Integer.valueOf(1) : Integer.valueOf(100));
		}
		if (formatFlags != 0) {
			formatterCustomOptions.set(Formatter.FORMAT_FLAGS, formatFlags);	
		}

		Formatter formatter = Formatter.builder(formatterCustomOptions).build();
		
		return formatter.render(AiMdParser.getParser().parse(markdownString));
	}

	public static String repairBrokenText(String text) {

		String enter = "\r\n";
		if (!text.contains(enter)) {
			enter = "\r";
			if (!text.contains(enter)) {
				enter = "\n";
				if (!text.contains(enter)) {
					enter = CU.E;				
				}
			}
		}
		
		String one = Character.valueOf((char) 1) + "";
		
		/* Double enter */
		text = text.replace(enter + enter, one + one);
		text = text.replace(one + enter, one + one);
		
		/* Sign and enter */
		text = text.replace("\"" + enter, "\"" + one);
		text = text.replace("'" + enter, "'" + one);
		
		text = text.replace("-" + enter, "-" + one);
		text = text.replace("_" + enter, "_" + one);
		
		text = text.replace("." + enter, "." + one);
		text = text.replace("?" + enter, "?" + one);
		text = text.replace("!" + enter, "!" + one);
		
		/* Enter and sign */
		text = text.replace(enter + "1", one + "1");
		text = text.replace(enter + "2", one + "2");
		text = text.replace(enter + "3", one + "3");
		text = text.replace(enter + "4", one + "4");
		text = text.replace(enter + "5", one + "5");
		text = text.replace(enter + "6", one + "6");
		text = text.replace(enter + "7", one + "7");
		text = text.replace(enter + "8", one + "8");
		text = text.replace(enter + "9", one + "9");
		text = text.replace(enter + "0", one + "0");
		
		text = text.replace(enter + "\"", one + "\"");
		text = text.replace(enter + "'", one + "'");
		text = text.replace(enter + "-", one + "-");
		text = text.replace(enter + "_", one + "_");

		if (enter.endsWith("\n")) {
			text = text.replaceAll("\n[\\s]*", "\n");	
		}
		else {
			text = text.replaceAll("\r[\\s]*", "\r");
		}
		
		/* Main replace */
		text = text.replace(enter, ' ' + "");
		text = text.replace(one, enter);

		return text;
	}
}
