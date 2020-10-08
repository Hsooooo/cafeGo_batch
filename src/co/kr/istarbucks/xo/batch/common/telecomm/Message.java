package co.kr.istarbucks.xo.batch.common.telecomm;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import co.kr.istarbucks.xo.batch.common.util.ByteUtil;


@edu.umd.cs.findbugs.annotations.SuppressWarnings("HARD_CODE_KEY") 
public class Message {
	private static final Logger tcLogger = Logger.getLogger ("TELECOM");
	
	private static final char cSTX = 2;
	private static final char cETX = 3;
	private static final char cFS  = 28;
	private static final char cSI  = 15;
	
	private static byte[] STX = new byte[] {(byte) cSTX};
	private static byte[] ETX = new byte[] {(byte) cETX};
	private static byte[] FS  = new byte[] {(byte) cFS};
	private static byte[] SI  = new byte[] {(byte) cSI};
	
	private static byte[] MAKER_CODE      = "EH".getBytes();
	private static byte[] TEXT_TYPE_M5    = "M5".getBytes();
	private static byte[] TEXT_TYPE_M6    = "M6".getBytes();
	private static byte[] CONTROL_CD      = "=".getBytes();
	private static byte[] CONTROL_CD_P    = "P".getBytes();
	private static byte[] SETTLE_INDEX    = " ".getBytes();
	private static byte[] TERM_TYPE_KT    = "KT".getBytes();
	private static byte[] TERM_TYPE_UPLUS = "LM".getBytes();
	private static byte[] WCC_M5_KEYIN    = "@".getBytes();
//	private static byte[] WCC_M5_SWIPE    = "A".getBytes();
//	private static byte[] WCC_M6_KEYIN    = "P".getBytes();
//	private static byte[] WCC_M6_SWIPE    = "Q".getBytes();
	
	private static final String[] RESPONSE_HEADER_FIELD_LIST = {
			"MakerCd",
			"EncKey",
			"TextType",
			"ControlCd",
			"SettleIndex",
			"SeqNo",
			"TermType"
	};
	
	private static final String[] RESPONSE_FIELD_LIST = {
		"TermMngNo",
		"RespCd",
		"BuyerCd",
		"",
		"ApprovDT",
		"",
		"ApprovNo",
//		"",
		"",
		"IssuerCd",
		"IssuerNm",
		"ChainNo",
		"BuyerNm",
		"ScreenControlCd",
		"ScreenExpr",
		"",
		"Notice"
};	
	
	private static String KT    = "KT";
//	private static String UPLUS = "UPLUS";
	
	public static String retrievePoint() {
		return null;
	}
	
	public static Map<String, String> parseResponse2(String textType, byte[] response) throws UnsupportedEncodingException {
		
		Map<String, String> result = new HashMap<String, String>();
		
		byte[] content = new byte[response.length - 2];
		System.arraycopy(response, 1, content, 0, response.length - 2);
		
		String[] responseText = new String(content,"euc-kr").split(new String(FS));

		if (responseText != null && responseText.length != 0) {
			for (int i = 0; i < responseText.length; i++) {
							
				if (i == 0) {
					String field0 = responseText[i];

					String makerCd     = field0.substring( 0,  2);
					String encKey      = field0.substring( 2, 10);
					String rTextType   = field0.substring(10, 12);
					String controlCd   = field0.substring(12, 13);
					String settleIndex = field0.substring(13, 14);
					String seqNo       = field0.substring(14, 18);
					String termType    = field0.substring(18, 20);
					
					result.put(RESPONSE_HEADER_FIELD_LIST[0], makerCd);     // MakerCd
					result.put(RESPONSE_HEADER_FIELD_LIST[1], encKey);      // EncKey
					result.put(RESPONSE_HEADER_FIELD_LIST[2], rTextType);   // TextType
					result.put(RESPONSE_HEADER_FIELD_LIST[3], controlCd);   // ControlCd
					result.put(RESPONSE_HEADER_FIELD_LIST[4], settleIndex); // SettleIndex
					result.put(RESPONSE_HEADER_FIELD_LIST[5], seqNo);       // SeqNo
					result.put(RESPONSE_HEADER_FIELD_LIST[6], termType);    // TermType
					
					tcLogger.info("[" + StringUtils.leftPad(Integer.toString(i), 2, "0")+ "]" + " :: " + 
								StringUtils.defaultString(makerCd)     + ", " + 
								StringUtils.defaultString(encKey)      + ", " +
								StringUtils.defaultString(rTextType)   + ", " +
								StringUtils.defaultString(controlCd)   + ", " +
								StringUtils.defaultString(settleIndex) + ", " +
								StringUtils.defaultString(seqNo)       + ", " +
								StringUtils.defaultString(termType));
				} else if (i == 9) {
					String[] field9 = responseText[i].split(new String(SI));
					
					result.put(RESPONSE_FIELD_LIST[i - 1], StringUtils.defaultString(field9[1]));

					
					tcLogger.info("[" + StringUtils.leftPad(Integer.toString(i), 2, "0")+ "]" + " :: " + StringUtils.defaultString(field9[0]) + ", " + StringUtils.defaultString(field9[1]));
				} else {
					
					result.put(RESPONSE_FIELD_LIST[i - 1], StringUtils.defaultString(responseText[i]));
//					
//					fieldIndex++;
					
					tcLogger.info("[" + StringUtils.leftPad(Integer.toString(i), 2, "0")+ "]" + " :: " + StringUtils.defaultString(responseText[i]));
				}
			}
		}
		
		return result;
	}
	
	/** 
	 * KT-Vanilla 응답메시지 파싱 
	 * 
	 * @param textType
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, String> parseResponseKtVanilla(String textType, byte[] response) throws UnsupportedEncodingException {
		
		Map<String, String> result = new HashMap<String, String>();
		
		byte[] content = new byte[response.length - 2];
		System.arraycopy(response, 1, content, 0, response.length - 2);
		
		String[] responseText = new String(content,"euc-kr").split(new String(FS));

		if (responseText != null && responseText.length != 0) {
			for (int i = 0; i < responseText.length; i++) {
							
				if (i == 0) {
					String field0 = responseText[i];

					String makerCd     = field0.substring( 0,  2);			// (2) 메이커 코드
					String encKey      = field0.substring( 2, 10);			// (8) 암호화 키
					String rTextType   = field0.substring(10, 12);			// (2) 전문 구분코드
					String controlCd   = field0.substring(12, 13);			// (1) 거래 제어코드
					String settleIndex = field0.substring(13, 14);			// (1) 정산 인덱스
					String seqNo       = field0.substring(14, 18);			// (4) 거래 일련번호
					String termType    = field0.substring(18, 20);			// (2) 단말기 구분코드
					
					result.put(RESPONSE_HEADER_FIELD_LIST[0], makerCd);     // MakerCd
					result.put(RESPONSE_HEADER_FIELD_LIST[1], encKey);      // EncKey
					result.put(RESPONSE_HEADER_FIELD_LIST[2], rTextType);   // TextType
					result.put(RESPONSE_HEADER_FIELD_LIST[3], controlCd);   // ControlCd
					result.put(RESPONSE_HEADER_FIELD_LIST[4], settleIndex); // SettleIndex
					result.put(RESPONSE_HEADER_FIELD_LIST[5], seqNo);       // SeqNo
					result.put(RESPONSE_HEADER_FIELD_LIST[6], termType);    // TermType
					
					tcLogger.info("[" + StringUtils.leftPad(Integer.toString(i), 2, "0")+ "]" + " :: " + 
								StringUtils.defaultString(makerCd)     + ", " + 
								StringUtils.defaultString(encKey)      + ", " +
								StringUtils.defaultString(rTextType)   + ", " +
								StringUtils.defaultString(controlCd)   + ", " +
								StringUtils.defaultString(settleIndex) + ", " +
								StringUtils.defaultString(seqNo)       + ", " +
								StringUtils.defaultString(termType));
				} else {
					result.put(RESPONSE_FIELD_LIST[i - 1], StringUtils.defaultString(responseText[i].trim()));
					
					tcLogger.info("[" + StringUtils.leftPad(Integer.toString(i), 2, "0")+ "]" + " :: " + StringUtils.defaultString(responseText[i]));
				}
			}
		}
		
		return result;
	}
	
	public static Map<String, String> parseResponse(String textType, byte[] response) {
		Map<String, String> result = new HashMap<String, String>();
		StringBuffer temp          = new StringBuffer(128);
		
		byte[] tt                  = new byte[0];
		boolean isHeaderSection    = true;
		int part       = -1;
		int fieldIndex = 0;
		for (int i = 0; i < response.length; i++) {
			part = (int) response[i];
			
			switch (part) {
				case cSTX :	
					// start parsing
					break;
				case cETX :	
					// terminate at next time (because of LLC)
					break;
				case cFS :
					// set field
					if (isHeaderSection) { // is first field?
						tcLogger.info("test->" + temp.toString());
						result.put(RESPONSE_HEADER_FIELD_LIST[0], temp.toString().substring( 0,  2).trim()); // MakerCd
						result.put(RESPONSE_HEADER_FIELD_LIST[1], temp.toString().substring( 2, 10).trim()); // EncKey
						result.put(RESPONSE_HEADER_FIELD_LIST[2], temp.toString().substring(10, 12).trim()); // TextType
						result.put(RESPONSE_HEADER_FIELD_LIST[3], temp.toString().substring(12, 13).trim()); // ControlCd
						result.put(RESPONSE_HEADER_FIELD_LIST[4], temp.toString().substring(13, 14).trim()); // SettleIndex
						result.put(RESPONSE_HEADER_FIELD_LIST[5], temp.toString().substring(14, 18).trim()); // SeqNo
						result.put(RESPONSE_HEADER_FIELD_LIST[6], temp.toString().substring(18, 20).trim()); // TermType
						isHeaderSection = false;
//						fieldIndex++;
					} else {
						if (fieldIndex == 3 || 
								fieldIndex == 5 || 
								fieldIndex == 8 || 
								fieldIndex == 15) {
							// skipped. No mapping field.
							tcLogger.info("test[skip]->" + temp.toString());
							fieldIndex++;
						} else {
							tcLogger.info("field->" + RESPONSE_FIELD_LIST[fieldIndex]);
							result.put(RESPONSE_FIELD_LIST[fieldIndex], temp.toString().trim());
//							result.put(RESPONSE_FIELD_LIST[fieldIndex], new String(tt, Charset.forName("EUC-KR")));
							fieldIndex++;	
						}						
					}
					
					temp.delete(0, temp.length());
					tt = new byte[0];
					break;
				case cSI :
					fieldIndex++;
					break;
				default :
					// append field buffer
					temp.append((char) response[i]);
					tt = ByteUtil.addArray(tt, ByteUtil.intToBytes((char) response[i]));
					break;
			}
		}
		if (temp.length() > 0) {
			result.put(RESPONSE_FIELD_LIST[fieldIndex], temp.toString().trim());
		}
		
		// for debugging
		int i = 0;
		for (String fieldId : RESPONSE_HEADER_FIELD_LIST) {
			tcLogger.info("[" + StringUtils.leftPad(Integer.toString(i), 2, "0")+ "]" + fieldId + " :: " + result.get(fieldId));
			i++;
		}		
		for (String fieldId : RESPONSE_FIELD_LIST) {
			tcLogger.info("[" + StringUtils.leftPad(Integer.toString(i), 2, "0")+ "]" + fieldId + " :: " + result.get(fieldId));
			i++;
		}
		
		return result;
	}
	
	public static void retrieveSale(String telecommType) {
		
		String seqNo         = "";
		String encKey        = "";
		String termMngNo     = ""; 
		String cardData      = "";
		String unknownField1 = "";
		String totalAmount   = ""; 
		String orgApprovNo   = "";
		String orgSaleDate   = "";
		String passwd        = "";
		String pluCd         = "";
		String LRC           = "";
		
		byte[] reqMessage    = null;
		
		if (KT.equals(telecommType)) {
			seqNo         = "0234";
			encKey        = "STA" + StringUtils.leftPad(seqNo, 5, "0");
			termMngNo     = StringUtils.rightPad("812151", 10, " "); 
			cardData      = StringUtils.rightPad("7578117076808570", 37, " ");
			unknownField1 = "00";
			totalAmount   = "3600"; 
			orgApprovNo   = "";
			orgSaleDate   = "";
			passwd        = StringUtils.rightPad("", 16, " ");
			pluCd         = "KT" + "0001";
			LRC           = "";
			
			reqMessage = ByteUtil.addArray(reqMessage, STX);
			reqMessage = ByteUtil.addArray(reqMessage, MAKER_CODE);
			reqMessage = ByteUtil.addArray(reqMessage, encKey.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, TEXT_TYPE_M5);
			reqMessage = ByteUtil.addArray(reqMessage, CONTROL_CD);
			reqMessage = ByteUtil.addArray(reqMessage, SETTLE_INDEX);
			reqMessage = ByteUtil.addArray(reqMessage, seqNo.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, TERM_TYPE_KT);
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, termMngNo.trim().getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, WCC_M5_KEYIN);
			reqMessage = ByteUtil.addArray(reqMessage, cardData.trim().getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, unknownField1.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, totalAmount.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, orgApprovNo.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, orgSaleDate.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, passwd.trim().getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, pluCd.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, ETX);
			reqMessage = ByteUtil.addArray(reqMessage, LRC.getBytes());
		} else {
			seqNo         = "0232";
			encKey        = "STA" + StringUtils.leftPad(seqNo, 5, "0");
			termMngNo     = StringUtils.rightPad("812151", 10, " "); 
			cardData      = StringUtils.rightPad("2010014852610016", 37, " ");
			unknownField1 = "00";
			totalAmount   = "500"; 
			orgApprovNo   = "";
			orgSaleDate   = "";
			passwd        = StringUtils.rightPad("", 16, " ");
			pluCd         = "";
			LRC           = "";
			
			reqMessage = ByteUtil.addArray(reqMessage, STX);
			reqMessage = ByteUtil.addArray(reqMessage, MAKER_CODE);
			reqMessage = ByteUtil.addArray(reqMessage, encKey.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, TEXT_TYPE_M5);
			reqMessage = ByteUtil.addArray(reqMessage, CONTROL_CD);
			reqMessage = ByteUtil.addArray(reqMessage, SETTLE_INDEX);
			reqMessage = ByteUtil.addArray(reqMessage, seqNo.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, TERM_TYPE_UPLUS);
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, termMngNo.trim().getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, WCC_M5_KEYIN);
			reqMessage = ByteUtil.addArray(reqMessage, cardData.trim().getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, unknownField1.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, totalAmount.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, orgApprovNo.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, orgSaleDate.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, passwd.trim().getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, pluCd.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, ETX);
			reqMessage = ByteUtil.addArray(reqMessage, LRC.getBytes());
		}
		
		tcLogger.info("reqText->" + new String(reqMessage));
	}
	
	public static void cancelSale(String telecommType) {
		String seqNo         = "";
		String encKey        = "";
		String termMngNo     = ""; 
		String cardData      = "";
		String unknownField1 = "";
		String totalAmount   = ""; 
		String orgApprovNo   = "";
		String orgSaleDate   = "";
		String passwd        = "";
		String pluCd         = "";
		String LRC           = "";
		
		byte[] reqMessage    = null;
		
		if (KT.equals(telecommType)) {
			seqNo         = "0235";
			encKey        = "STA" + StringUtils.leftPad(seqNo, 5, "0");
			termMngNo     = StringUtils.rightPad("812151", 10, " "); 
			cardData      = StringUtils.rightPad("7578117076808570", 37, " ");
			unknownField1 = "00";
			totalAmount   = "3600"; 
			orgApprovNo   = "601161244294";
			orgSaleDate   = "150601"; // RRMMDD
			passwd        = StringUtils.rightPad("", 16, " ");
			pluCd         = "KT" + "0001";
			LRC           = "";
			
			reqMessage = ByteUtil.addArray(reqMessage, STX);
			reqMessage = ByteUtil.addArray(reqMessage, MAKER_CODE);
			reqMessage = ByteUtil.addArray(reqMessage, encKey.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, TEXT_TYPE_M6);
			reqMessage = ByteUtil.addArray(reqMessage, CONTROL_CD);
			reqMessage = ByteUtil.addArray(reqMessage, SETTLE_INDEX);
			reqMessage = ByteUtil.addArray(reqMessage, seqNo.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, TERM_TYPE_KT);
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, termMngNo.trim().getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, WCC_M5_KEYIN);
			reqMessage = ByteUtil.addArray(reqMessage, cardData.trim().getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, unknownField1.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, totalAmount.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, orgApprovNo.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, orgSaleDate.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, passwd.trim().getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, FS);
//			reqMessage = ByteUtil.addArray(reqMessage, FS);
//			reqMessage = ByteUtil.addArray(reqMessage, pluCd.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, ETX);
			reqMessage = ByteUtil.addArray(reqMessage, LRC.getBytes());
		} else {
			seqNo         = "0232";
			encKey        = "STA" + StringUtils.leftPad(seqNo, 5, "0");
			termMngNo     = StringUtils.rightPad("812151", 10, " "); 
			cardData      = StringUtils.rightPad("2010014852610016", 37, " ");
			unknownField1 = "00";
			totalAmount   = "500"; 
			orgApprovNo   = "";
			orgSaleDate   = "";
			passwd        = StringUtils.rightPad("", 16, " ");
			pluCd         = "";
			LRC           = "";
			
			reqMessage = ByteUtil.addArray(reqMessage, STX);
			reqMessage = ByteUtil.addArray(reqMessage, MAKER_CODE);
			reqMessage = ByteUtil.addArray(reqMessage, encKey.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, TEXT_TYPE_M6);
			reqMessage = ByteUtil.addArray(reqMessage, CONTROL_CD);
			reqMessage = ByteUtil.addArray(reqMessage, SETTLE_INDEX);
			reqMessage = ByteUtil.addArray(reqMessage, seqNo.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, TERM_TYPE_UPLUS);
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, termMngNo.trim().getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, WCC_M5_KEYIN);
			reqMessage = ByteUtil.addArray(reqMessage, cardData.trim().getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, unknownField1.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, totalAmount.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, orgApprovNo.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, orgSaleDate.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, passwd.trim().getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, FS);
			reqMessage = ByteUtil.addArray(reqMessage, FS);
//			reqMessage = ByteUtil.addArray(reqMessage, FS);
//			reqMessage = ByteUtil.addArray(reqMessage, pluCd.getBytes());
			reqMessage = ByteUtil.addArray(reqMessage, ETX);
			reqMessage = ByteUtil.addArray(reqMessage, LRC.getBytes());
		}
		
		tcLogger.info("reqText->" + new String(reqMessage));
	}
	
	public static byte[] retrieveSaleResponse(String telecommType) {
		byte[] resMessage    = null;
		
		String LRC             = "";
		String seqNo           = "";
		String encKey          = "";
		String termMngNo       = "";
		String respCd          = "0000";
		String buyerCd         = "062";
		String approveDt       = "1506011612441"; // YYMMDDHH24MISSN (N : 0~6, 0-Sunday)
		String approveNo       = "601161244294";
		String issuerCd        = "062";
		String issuerNm        = "KT 올레멤버십";
		String chainNo         = "AD60130000";
		String buyerNm         = "KT 올레멤버십";
		String screenControlCd = "D";
		String screenExpr      = "승인601161244294할인금액    1000지불금액    3600잔여한도   20000";
		String notice          = "할인금액:   100할인금액   1000잔여한도   9000지불금액  20000";
		
		seqNo         = "0234";
		encKey        = "STA" + StringUtils.leftPad(seqNo, 5, "0");
		termMngNo     = StringUtils.rightPad("812151", 10, " ");
		
		resMessage = ByteUtil.addArray(resMessage, STX);
		resMessage = ByteUtil.addArray(resMessage, MAKER_CODE);
		resMessage = ByteUtil.addArray(resMessage, encKey.getBytes());
		resMessage = ByteUtil.addArray(resMessage, TEXT_TYPE_M6);
		resMessage = ByteUtil.addArray(resMessage, CONTROL_CD_P);
		resMessage = ByteUtil.addArray(resMessage, SETTLE_INDEX);
		resMessage = ByteUtil.addArray(resMessage, seqNo.getBytes());
		resMessage = ByteUtil.addArray(resMessage, TERM_TYPE_KT);
		resMessage = ByteUtil.addArray(resMessage, FS);
		resMessage = ByteUtil.addArray(resMessage, termMngNo.trim().getBytes());
		resMessage = ByteUtil.addArray(resMessage, FS);
		resMessage = ByteUtil.addArray(resMessage, respCd.getBytes());
		resMessage = ByteUtil.addArray(resMessage, FS);
		resMessage = ByteUtil.addArray(resMessage, buyerCd.getBytes());
		resMessage = ByteUtil.addArray(resMessage, FS);
		resMessage = ByteUtil.addArray(resMessage, FS);
		resMessage = ByteUtil.addArray(resMessage, approveDt.getBytes());
		resMessage = ByteUtil.addArray(resMessage, FS);
		resMessage = ByteUtil.addArray(resMessage, FS);
		resMessage = ByteUtil.addArray(resMessage, approveNo.getBytes());
		resMessage = ByteUtil.addArray(resMessage, FS);
		resMessage = ByteUtil.addArray(resMessage, FS);
		resMessage = ByteUtil.addArray(resMessage, SI);
		resMessage = ByteUtil.addArray(resMessage, issuerCd.getBytes());
		resMessage = ByteUtil.addArray(resMessage, FS);
		resMessage = ByteUtil.addArray(resMessage, issuerNm.getBytes(Charset.forName("UTF-8")));
		resMessage = ByteUtil.addArray(resMessage, FS);
		resMessage = ByteUtil.addArray(resMessage, chainNo.getBytes());
		resMessage = ByteUtil.addArray(resMessage, FS);
		resMessage = ByteUtil.addArray(resMessage, buyerNm.getBytes(Charset.forName("UTF-8")));
		resMessage = ByteUtil.addArray(resMessage, FS);
		resMessage = ByteUtil.addArray(resMessage, screenControlCd.getBytes());
		resMessage = ByteUtil.addArray(resMessage, FS);
		resMessage = ByteUtil.addArray(resMessage, screenExpr.getBytes(Charset.forName("UTF-8")));
		resMessage = ByteUtil.addArray(resMessage, FS);
		resMessage = ByteUtil.addArray(resMessage, FS);
		resMessage = ByteUtil.addArray(resMessage, notice.getBytes(Charset.forName("UTF-8")));
		
		resMessage = ByteUtil.addArray(resMessage, ETX);
		resMessage = ByteUtil.addArray(resMessage, LRC.getBytes());
		
		tcLogger.info("resText->" + new String(resMessage));
		return resMessage;
	}
	
	public static void main(String[] args) {
		Message.retrieveSale(KT);
		Message.cancelSale(KT);
		byte[] response = Message.retrieveSaleResponse(KT);
		
		Message.parseResponse("M6", response);
		
		tcLogger.info("");
		tcLogger.info("");
		
		try {
			Message.parseResponse2("M6", response);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			tcLogger.error(e.getMessage(), e);
		}
	}
}