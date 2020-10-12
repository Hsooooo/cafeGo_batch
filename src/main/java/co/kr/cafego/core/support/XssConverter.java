package co.kr.cafego.core.support;

import java.util.regex.Pattern;

import org.springframework.core.convert.converter.Converter;

/**
 * 크로스 사이트 컨버터 </br>
 * @author 유성진
 * @since 2014. 6. 11.
 */
public class XssConverter implements Converter<String, String>{

	@Override
	public String convert(String input) {
		return convertString(input);
	}

	private String convertString(String str) {
		String tmpStr = str;
		
		tmpStr = tmpStr.replaceAll("(?i)<script[^>]*>[\\w|\\t|\\r|\\W]*</script>" , "");
		tmpStr = tmpStr.replaceAll("(?i)<head[^>]*>[\\w|\\t|\\r|\\W]*</head>" , "");
		
		// Avoid anything between script tags
		Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
		tmpStr = scriptPattern.matcher(tmpStr).replaceAll("");
        // Remove any lonesome </script> tag
        scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
        tmpStr = scriptPattern.matcher(tmpStr).replaceAll("");
        // Avoid eval(...) expressions
        scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        tmpStr = scriptPattern.matcher(tmpStr).replaceAll("");
        // Avoid expression(...) expressions
        scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        tmpStr = scriptPattern.matcher(tmpStr).replaceAll("");
        // Avoid javascript:... expressions
        scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
        tmpStr = scriptPattern.matcher(tmpStr).replaceAll("");
        // Avoid vbscript:... expressions
        scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
        tmpStr = scriptPattern.matcher(tmpStr).replaceAll("");
        // Avoid onload= expressions
        // Avoid onLoad= expressions
        scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        tmpStr = scriptPattern.matcher(tmpStr).replaceAll("");
        // Avoid anything in a onerror='...' type of expression
        // Avoid anything in a onError='...' type of expression
        scriptPattern = Pattern.compile("on(.*?)[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        tmpStr = scriptPattern.matcher(tmpStr).replaceAll("");
        scriptPattern = Pattern.compile("on(.*?)[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        tmpStr = scriptPattern.matcher(tmpStr).replaceAll("");
		
//		str = str.replaceAll("<", "&lt;");
//		str = str.replaceAll(">", "&gt;");
//		str = str.replaceAll("'", "\"");
//		str = str.replaceAll("\"", "&quot;");
		return tmpStr;
	}
}
