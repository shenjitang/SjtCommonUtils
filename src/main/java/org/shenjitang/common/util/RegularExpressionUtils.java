package org.shenjitang.common.util;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionUtils {

	private RegularExpressionUtils() {

	}

	public enum RegularExpression {

		WHITE_SPACE("[\\s\\p{Zs}]"),

		DATE_FORMAT(
				"(\\d{4}年\\d{1,2}月\\d{1,2}日)|(\\d{4}年\\d{1,2}月)|(\\d{4}年\\d{1,2}-\\d{1,2}月)|(\\d{4}年\\d{1,2}月-\\d{1,2}月)|(\\d{4}\\-\\d{1,2}\\-\\d{1,2})|(\\d{4}\\-\\d{1,2})|(\\d{4}\\.\\d{1,2}\\.\\d{1,2})|(\\d{4}\\.\\d{1,2})|(\\d{4}年)|(\\d{4}年\\-\\d{4}年)|(\\d{4}\\-\\d{4}年)"),

		TEXT_DATE(
				"(\\d{4}年\\d{1,2}月\\d{1,2}日)|(\\d{4}年\\d{1,2}月)|(\\d{4}年\\d{1,2}-\\d{1,2}月)|(\\d{4}年\\d{1,2}月-\\d{1,2}月)|(\\d{4}\\-\\d{1,2}\\-\\d{1,2})|(\\d{4}\\-\\d{1,2})|(\\d{4}年)|(\\d{4}年\\-\\d{4}年)|(\\d{4}\\-\\d{4}年)"),
		
		REDIRECT_KEYWORD(
				"(\\d{4}年\\d{1,2}月\\d{1,2}日)|(\\d{4}年\\d{1,2}月)|(\\d{4}年\\d{1,2}-\\d{1,2}月)|(\\d{4}年\\d{1,2}月-\\d{1,2}月)|(\\d{4}\\-\\d{1,2}\\-\\d{1,2})|(\\d{4}\\-\\d{1,2})"),

		URL(
				"^(http://|www.|ftp://){1}(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$"),

		SIMPLE_NUMERIC("[\\-]?\\d{1,}"),

		NUMERIC_2("[\\-]?[\\d.]+"),

		NUMERIC("[\\-]?[\\d.,]+[%]?"),

		NUMERIC_AND_CHARACTER("[a-zA-z]?[\\-]?[\\d.,]+[%]?"),

		PUNCTUATION("(，|。|：|？|；)"),

		HTML_TAG("<(\\S*?)[^>]*>.*?</>|<.*?/>"),

		HTML_COMMENTS("<!--(?s).*?-->"),

		HTML_SPECIAL_SYMBOLS(
				"(&lt;|&gt;|&trade;|&reg;|&quot;|&times;|&sect;|&copy;|&nbsp;)"),

		JAVASCRIPT("<script(.*)?</script>"),

		CHINSES("[\u4e00-\u9fa5]+"),

		TRANSIT_IDENTIFIER_PATTERN("(\\{.*\\})+"),

		DATE_WILDCARD_PATTERN(
				"YYYY|yyyy|YY|yy|Y|y|MMM|mmm|MM|mm|M|m|DD|dd|D|d|Q|q|W|w"),

		DATE_WILDCARD_PATTERN_SELECTED(
				"\\[(YYYY|yyyy|YY|yy|Y|y|MMM|mmm|MM|mm|M|m|DD|dd|D|d|Q|q|W|w)\\]"),
				
		DATE_WILDCARD_PATTERN_NOT_SELECTED(
				"\\(.*?\\)"),

		ILLEGAL_CHARACTERS(
				"\\:|\\?|\\%|\\$|\\!|\\@|\\#|\\^|\\&|\\*|\\(|\\)|\\]|\\[|\\\\|\\/|\\-|\\~");

		private final String pattern;

		private RegularExpression(String pattern) {
			this.pattern = pattern;
		}

		public String getPattern() {
			return pattern;
		}
	}

	public static boolean find(String input, String reg) {
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(input);
		String value = m.find() ? m.group() : "";
		return StringUtils.isNotEmpty(value) && value.equals(input);
	}

	public static boolean find(String input, RegularExpression reg) {
		Pattern p = Pattern.compile(reg.getPattern(), Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(input);
		String value = m.find() ? m.group() : "";
		return StringUtils.isNotEmpty(value) && value.equals(input);
	}

	public static String match(String input, RegularExpression reg) {
		Pattern p = Pattern.compile(reg.getPattern(), Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(input);
		return m.find() ? m.group() : "";
	}

	public static String match(String input, String reg) {
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(input);
		return m.find() ? m.group() : "";
	}
	
	public static String match(String input, String reg, int index) {
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(input);
		return m.find() ? m.group(index) : "";
	}
	
	public static void main(String[] args) {
	}
}
