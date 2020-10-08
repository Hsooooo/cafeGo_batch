/*
 * @(#) $Id: OnmUtil.java,v 1.2 2016/10/31 06:58:41 dev99 Exp $
 * Starbucks XO
 * Copyright 2013 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.commons.configuration.AbstractFileConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * OnmUtil.
 * @author leeminjung
 * @version $Revision: 1.2 $
 */
public class OnmUtil {
	
	protected final static Log log = LogFactory.getLog ("OnmUtil");
	
	public OnmUtil () {

	}
	
	
	/**
	 * 프로퍼티의 네임으로 파일읽기
	 * @param 프로퍼티의 이름
	 * @return
	 */
	public static Configuration readProperties ( String propertiesName ) {
		Configuration config = null;
		try {
			config = new PropertiesConfiguration (propertiesName);
			( (AbstractFileConfiguration) config ).setReloadingStrategy (new FileChangedReloadingStrategy ());
		} catch ( ConfigurationException e ) {
			log.error(e.getMessage(), e);
		}
		return config;
	}
	
	
	//문자열 이름 변경 
	//String inFileName : 업로드 파일 이름
	//String newFilename : 날짜변경 파일 이름
	//String filePath  : 업로드 파일 경로 
	@SuppressWarnings ( "unused" )
	public static String getFileName ( String inFileName, String newFilename, String filePath ) {
		//submit 폼에서 받은 파일 이름 변경 				
		String fileName = inFileName.substring (inFileName.lastIndexOf ("\\") + 1);
		String fileName1 = fileName;
		String fileName2 = "";
		if ( fileName.lastIndexOf ('.') > 0 ) {
			fileName1 = fileName.substring (0, fileName.lastIndexOf ("."));
			fileName2 = fileName.substring (fileName.lastIndexOf (".") + 1);
		}
		String fullFilenName = filePath + "/" + newFilename + "." + fileName2;
		return fullFilenName;
	}
	
	/*****************************************************
	 * 문자열에 구분자가 포함 유무 리턴
	 ******************************************************/
	public static boolean characterCheck ( String str, String delim ) {
//        if(str.indexOf(delim) > -1){
//        	return true ;
//        }else{
//        	return false;
//        }
		int count = 0;
		if ( delim.equals ("&") || ( delim.equals ("=") ) ) {
			count = 1;
		}
		StringTokenizer st = new StringTokenizer (str, delim);
		
		if ( st.countTokens () > count ) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/*****************************************************
	 * 날짜 구하기
	 ******************************************************/
	public static String getDate ( String date, String division ) {
		String lm_sDate = "";
		String lm_sReDate = "";
		String lm_sYear = "";
		String lm_sMonth = "";
		String lm_sDay = "";
		String lm_sDiv = "";
		

		lm_sDate = date;
		lm_sDiv = division;
		
		if ( lm_sDate.length () > 7 ) {
			
			lm_sYear = lm_sDate.substring (0, 4);
			lm_sMonth = lm_sDate.substring (4, 6);
			lm_sDay = lm_sDate.substring (6, 8);
			
			if(StringUtils.isEmpty(lm_sDiv)) {
				lm_sReDate = lm_sYear + "년" + lm_sMonth + "월" + lm_sDay + "일";
			} else {
				lm_sReDate = lm_sYear + lm_sDiv + lm_sMonth + lm_sDiv + lm_sDay;
			}
		}
		
		return lm_sReDate;
	}
	
	
	/*****************************************************
	 * 문자형 영문 숫자 체크
	 ******************************************************/
	public static boolean isAlphaNumeric ( String str ) {
		if ( str == null ) return false;
		char[] ch = str.toCharArray ();
		for ( int i = 0; i < ch.length; i++ ) {
			if ( ! ( ( ch[i] >= '0' && ch[i] <= '9' ) || ( ch[i] >= 'a' && ch[i] <= 'z' ) || ( ch[i] >= 'A' && ch[i] <= 'Z' ) ) )
			    return false;
		}
		return true;
	}
	
	/*****************************************************
	 * 문자형의 숫자 체크
	 ******************************************************/
	public static boolean isNumber ( String str ) {
		if ( str == null ) return false;
		char[] ch = str.toCharArray ();
		for ( int i = 0; i < ch.length; i++ ) {
			if ( ! ( ch[i] >= '0' && ch[i] <= '9' ) ) return false;
		}
		return true;
	}
	
	public static int lengthByGetBytes ( String str )
	        throws java.io.UnsupportedEncodingException {
		return OnmUtil.lengthByGetBytes (str, null);
	}
	
	public static int lengthByGetBytes ( String str, String enc )
	        throws java.io.UnsupportedEncodingException {
		if ( enc != null ) {
			byte[] bytes = str.getBytes ("iso-8859-1");
			return bytes.length;
		} else {
			return str.getBytes ().length;
		}
	}
	
	public static int lengthByCharacter ( String str ) {
		char[] ch = str.toCharArray ();
		int length = 0;
		for ( int i = 0; i < ch.length; i++ ) {
			if ( Character.UnicodeBlock.of (ch[i]) == Character.UnicodeBlock.HANGUL_SYLLABLES ||
			     Character.UnicodeBlock.of ((char) i) == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO ) length += 2;
			else length++;
		}
		return length;
	}
	
	@SuppressWarnings ( "unused" )
	private static boolean validDate ( String year, String month, String date ) {
		int yearInt = Integer.parseInt (year);
		int monthInt = Integer.parseInt (month);
		int dateInt = Integer.parseInt (date);
		
		if ( yearInt < 100 ) yearInt += 2000;
		monthInt--;
		
		Calendar cal = Calendar.getInstance ();
		cal.set (Calendar.YEAR, yearInt);
		cal.set (Calendar.MONTH, monthInt);
		cal.set (Calendar.DATE, dateInt);
		
		int fYear = cal.get (Calendar.YEAR);
		int fMonth = cal.get (Calendar.MONTH);
		int fDate = cal.get (Calendar.DATE);
		
		if ( ( fYear != yearInt ) || ( fMonth != monthInt ) ||
		     ( fDate != dateInt ) )
		    return false;
		return true;
	}
	
	public String convertDB ( String s ) {
		int i, len = s.length ();
		StringBuffer tmp = new StringBuffer (len + 1000);
		for ( i = 0; i < len; i++ ) {
			if ( s.charAt (i) == '"' ) {
				tmp.append ("\\\"");
			} else if ( s.charAt (i) == '\'' ) {
				tmp.append ("\\'");
			} else {
				tmp.append (s.charAt (i));
			}
		}
		return tmp.toString ();
	}
	
	public String convertHtml ( String s ) {
		int i, len = s.length ();
		StringBuffer tmp = new StringBuffer (len + 1000);
		for ( i = 0; i < len; i++ ) {
			if ( s.charAt (i) == '\n' ) {
				tmp.append ("<br>");
			} else if ( s.charAt (i) == ' ' ) {
				tmp.append ("&nbsp;");
			} else {
				tmp.append (s.charAt (i));
			}
		}
		return tmp.toString ();
	}
	
	public String convertSubject ( String s ) {
		int i, len = s.length ();
		StringBuffer tmp = new StringBuffer (len + 1000);
		for ( i = 0; i < len; i++ ) {
			if ( s.charAt (i) == '<' ) {
				tmp.append ("&lt");
			} else if ( s.charAt (i) == '>' ) {
				tmp.append ("&gt");
			} else {
				tmp.append (s.charAt (i));
			}
		}
		return tmp.toString ();
	}
	
	/*****************************************************
	 * null 문자열을 공백으로 변환
	 * 인자 :
	 * 반환형 :
	 ******************************************************/
	public static String checkNull ( String str ) {
		String strTmp;
		if ( str == null ) {
			strTmp = "";
		} else {
			strTmp = str;
		}
		return strTmp;
	}
	
	/*****************************************************
	 * null 문자열을 "0"으로 변환
	 * 인자 :
	 * 반환형 :
	 ******************************************************/
	public String changeNull ( String str ) {
		String strTmp;
		if ( str == null ) {
			strTmp = "0";
		} else {
			strTmp = str;
		}
		return strTmp;
	}
	
	/*****************************************************
	 * ' 문자열을 "으로 변환
	 * 인자 :
	 * 반환형 :
	 ******************************************************/
	public String changePoint ( String s ) {
		int i, len = s.length ();
		StringBuffer tmp = new StringBuffer (len + 1000);
		for ( i = 0; i < len; i++ ) {
			if ( s.charAt (i) == '\'' ) {
				tmp.append ("&quot");
			} else {
				tmp.append (s.charAt (i));
			}
		}
		return tmp.toString ();
	}
	
	
	/*****************************************************
	 * TextArea에서 입력받은 캐리지 리턴값을 <br>태크로 변환
	 * 인자 :
	 * 반환형 :
	 ******************************************************/
	public String checkBr ( String comment ) {
		int length = comment.length ();
		StringBuffer buffer = new StringBuffer ();
		
		for ( int i = 0; i < length; ++i ) {
			String comp = comment.substring (i, i + 1);
			
			if ( "\r".compareTo (comp) == 0 ) {
				comp = comment.substring (++i, i + 1);
				
				if ( "\n".compareTo (comp) == 0 ) {
					buffer.append ("<BR>\r");
				} else {
					buffer.append ("\r");
				}
			}
			buffer.append (comp);
		}
		return buffer.toString ();
	}
	
	
	/*****************************************************
	 * DB에 저장하는 날짜
	 ******************************************************/
	public static String getDay () {
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMdd", Locale.KOREA);
		Date currentTime_1 = new Date ();
		String dateString = formatter.format (currentTime_1);
		
		return dateString;
	}
	
	/*****************************************************
	 * money의 계산
	 ******************************************************/
	public static String DecimalPointint ( int _count ) {
		String count = String.valueOf (_count);
		int sum_len = count.length ();
		int rem_len = sum_len / 3;
		int len = sum_len - ( rem_len * 3 );
		StringBuffer sum = new StringBuffer();
		
		if ( count.length () > 3 ) {
			if ( len == 0 ) {
				sum.append("");
			} else {
				sum.append(count.substring (0, len)).append(",");
			}
			
			for ( int i = len; i < sum_len - 3; i = i + 3 ) {
				sum.append(count.substring (i, i + 3)).append(",");
			}
			sum.append(count.substring (sum_len - 3, sum_len));
		} else {
			sum.append(count);
		}
		
		return sum.toString();
	}
	
	public static String DecimalPointString ( String _count ) {
		String count = _count;
		int sum_len = count.length ();
		int rem_len = sum_len / 3;
		int len = sum_len - ( rem_len * 3 );
		StringBuffer sum = new StringBuffer();
		
		if ( count.length () > 3 ) {
			if ( len == 0 ) {
				sum.append("");
			} else {
				sum.append(count.substring (0, len)).append(",");
			}
			
			for ( int i = len; i < sum_len - 3; i = i + 3 ) {
				sum.append(count.substring (i, i + 3)).append(",");
			}
			sum.append(count.substring (sum_len - 3, sum_len));
		} else {
			sum.append(count);
		}
		
		return sum.toString();
	}
	
	
	/*****************************************************
	 * 해당 url 로 이동
	 ******************************************************/
	public String Redirect ( String gourl ) {
		String str = "<META http-equiv='refresh' content='0;url=" + gourl + "'>";
		return str;
	}
	
	/*****************************************************
	 * 해당 문자열의 length 부분 나머지를 0으로
	 * 인자 :
	 * 반환형 :
	 ******************************************************/
	public static String intConversionChar ( String s, int num ) {
		
		int len = s.length ();
		int addLen = num - len;
		StringBuffer tmp = new StringBuffer (len + 1000);
		int i = 0;
		if ( addLen > 0 ) {
			for ( i = 0; i < addLen; i++ ) {
				tmp.append ("0");
			}
			
		}
		return tmp.toString () + s;
	}
	
	/**
	 * 문자열 합치기
	 * @param sb	문자열 StringBuffer
	 * @param args	문자열에 추가되는 객체들
	 * @return
	 */
	public String concatString (StringBuffer sb, Object... args) {
		sb.setLength(0);
		for(Object o : args){
			sb.append(o);
		}
		return sb.toString();
	}
	
	/**
	 * 문자열 합치기
	 * @param args	문자열에 추가되는 객체들
	 * @return
	 */
	public String concatString (Object... args) {
		StringBuffer sb = new StringBuffer ();
		for(Object o : args){
			sb.append(o);
		}
		return sb.toString();
	}	
}
