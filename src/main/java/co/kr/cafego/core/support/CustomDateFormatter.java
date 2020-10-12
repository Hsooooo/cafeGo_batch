package co.kr.cafego.core.support;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.format.datetime.DateFormatter;

/**
 * DateFormatter 확장
 * @author 유성진
 */
public class CustomDateFormatter extends DateFormatter {

	public CustomDateFormatter(String string) {
		setPattern(string);
	}

	@Override
	public Date parse(String text, Locale locale) throws ParseException {
		String newText = text;
		String regex = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$";
		String regexYYYYMM = "^[0-9]{4}-[0-9]{2}$";
		StringBuilder sb = new StringBuilder();
		if(Pattern.matches(regex, newText)){
			
			newText = sb.append(newText).append(" 00:00").toString();
		} else if(Pattern.matches(regexYYYYMM, newText)) {
			//newText = newText.substring(0, 7) ;
			newText =sb.append(newText).append("-01 00:00").toString();
		}
		return super.parse(newText, locale);
	}

}
