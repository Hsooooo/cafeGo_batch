package co.kr.istarbucks.xo.batch.common.sms;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * SMS �߼� ���� ��ƿ Ŭ����
 * @author JUNO
 *
 */
public class SMSUtil {
	
	private static final HashMap<String, String> codeTable = new HashMap<String, String>();
	private static final HashMap<String, String> sendNameTable = new HashMap<String, String>();
	
	static {
		
		codeTable.put("001", "Birth");	//���� ����
		codeTable.put("002", "1+1");	//1+1 ����
		codeTable.put("003", "Coffee");	//���� ����
		codeTable.put("004", "PO");		//PO ����
		codeTable.put("005", "Star");	//��15 ����
		codeTable.put("006", "Charge");	//��������	
		codeTable.put("007", "CoffeeFood");	//coffee & food ����
		codeTable.put("008", "Cake");	//Ȧ����ũ ����
		codeTable.put("009", "Auto");	//�ڵ����� ����
		codeTable.put("999", "Target");	//�ڵ����� ����
		
		
		/*
		 * ��� ���ڴ� ������ sendname ���� 3�� �׸� ���´�.
		 */

		sendNameTable.put("002", "smsc");	//1+1 ����
		sendNameTable.put("004", "smsc");	//PO ����
		sendNameTable.put("006", "smsc");	//��������
		
		sendNameTable.put("001", "couponStat1");	//���� ����
		sendNameTable.put("003", "couponStat1");	//���� ����
		sendNameTable.put("005", "couponStat1");	//��15 ����
		
		sendNameTable.put("009", "couponStat2");	//�ڵ����� ����
		sendNameTable.put("008", "couponStat2");	//Ȧ����ũ ����
		
		sendNameTable.put("007", "couponStat2");	//coffee & food ����
		
	}
	
	public static String getCouponName(String code) {
		String result = null;
		result = codeTable.get(code);
		
		return result;
	}
	public static String getSendName(String code) {
		String result = null;
		
		result = sendNameTable.get(code);
		
		return result; 
	}
	
	
	

	/**
	 * ctn �ڸ���
	 * @param ctn
	 * @return
	 */
	public static Map<String, String> getDivCtn(String ctn) {
		
		Map<String, String> pCtn = new HashMap<String, String>();
		
		String a1 = null;
		String a2 = null;
		String a3 = null;
		
		a1 = ctn.substring(0, 3);
		
		if ( (ctn.length() - a1.length()) > 7){
			a2 = ctn.substring(3, 7);
			a3 = ctn.substring(7, 11);
		}		
		if ( (ctn.length() - a1.length()) ==7) {
			a2 = ctn.substring(3,6);
			a3 = ctn.substring(6, 10);
		}
		
		pCtn.put("Rphone1", a1);
		pCtn.put("Rphone2", a2);
		pCtn.put("Rphone3", a3);
		
		return pCtn;
	}
	
	/**
	 * ȯ�� ���� �޽��� ġȯ
	 * @param origin
	 * @param src
	 * @param dest
	 * @return
	 */
	public static String replaceString( String origin, String src, String dest )
	  {
	      if( origin==null ) return null;
	      StringBuffer sb = new StringBuffer( origin.length() );

	      int srcLength = src.length();

	      int preOffset = 0;
	      int offset = 0;
	      offset=origin.indexOf( src, preOffset );
	      while( offset!=-1 )
	      {
	          sb.append( origin.substring( preOffset,offset ) );
	          sb.append( dest );
	          preOffset = offset + srcLength;
	          offset=origin.indexOf( src, preOffset );
	      }
	      sb.append( origin.substring( preOffset, origin.length() ) );

	      return sb.toString();
	  }
	
	public static String getErrorMsg(Exception e) {
		String desc = null;
		
		if (e.getMessage() != null) {
			desc = e.getMessage();
			
			if ("null".equals(desc)){
				desc = String.valueOf(e);
			}
		} else {
			desc = String.valueOf(e);
		}
		
		int len = desc.indexOf("SQLException");
				
		if (len > 0) {
			desc = desc.substring(len, desc.length());
		}
		
		return desc;
	}
	
//	private String chEnc(String text) {
//		
//		String result = text;
//		
//		try {
//			result = new String(result.getBytes("EUC-KR"), "ISO-8859-1");
//		}catch (UnsupportedEncodingException e) {
//			
//		}
//		
//		return result;
//		
//	}

}
