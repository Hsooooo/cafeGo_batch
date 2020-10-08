package co.kr.istarbucks.xo.batch.common.telecomm;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import co.kr.istarbucks.xo.batch.common.util.ByteUtil;
import co.kr.istarbucks.xo.batch.exception.XOException;

@edu.umd.cs.findbugs.annotations.SuppressWarnings("HARD_CODE_KEY") 
public class MessageProvider {
	
	private final Logger tcLogger = Logger.getLogger ("TELECOM");
	
	private static final char cSTX = 2;
	private static final char cETX = 3;
	private static final char cETX2 = 13;
	private static final char cFS  = 28;
//	private static final char cSI  = 15;
	
	private static byte[] STX = new byte[] {(byte) cSTX};
	private static byte[] ETX = new byte[] {(byte) cETX};
	private static byte[] ETX2 = new byte[] {(byte) cETX2};
	private static byte[] FS  = new byte[] {(byte) cFS};
//	private static byte[] SI  = new byte[] {(byte) cSI};
	
	private static byte[] MAKER_CODE      = "EH".getBytes();
//	private static byte[] TEXT_TYPE_M5    = "M5".getBytes();
//	private static byte[] TEXT_TYPE_M6    = "M6".getBytes();
	private static byte[] CONTROL_CD      = "=".getBytes();
//	private static byte[] CONTROL_CD_P    = "P".getBytes();
	private static byte[] SETTLE_INDEX    = " ".getBytes();
//	private static byte[] TERM_TYPE_KT    = "KT".getBytes();
//	private static byte[] TERM_TYPE_UPLUS = "LM".getBytes();
	private static byte[] WCC_M5_KEYIN    = "@".getBytes();
//	private static byte[] WCC_M5_SWIPE    = "A".getBytes();
	private static byte[] WCC_M6_KEYIN    = "P".getBytes();
//	private static byte[] WCC_M6_SWIPE    = "Q".getBytes();
	
	private String TERM_MNG_NO;
	
//	private static String KT    = "KT";
//	private static String UPLUS = "UPLUS";	
	
	private static final String messageIdentifier   = "SV."; // not used
//	private char FS  = 28;
//	private char STX = 2;
//	private char F   = 70;
//	private char D   = 68;
//	private char ETX = 3;
	
	private TelecommUtil telecommUtil;
	
	public MessageProvider(String term_mng_no, TelecommUtil telecommUtil) {
		try {
			
			if(StringUtils.isBlank(term_mng_no)) {
				throw new XOException("��Ż� �͹̳ξ��̵� ����!!");
			}
			
			this.TERM_MNG_NO = term_mng_no;
			this.telecommUtil = telecommUtil;
			
		} catch (Exception e) {
			tcLogger.error(e.getMessage(), e);
		}
	}
	
	public byte[] setHeader(String textType, String pCardData, byte[] message, String termType) {
		
		// ���̷����� �ֹ���ȣ�� ��� 4�ڸ��� �ƴϱ� ������
		// ���޻� van�� �ֹ���ȣ�� �ѱ�� �ǹ̰� ���⶧���� �ϵ��ڵ� �س���
		String seqNo         = "";
		String encKey        = "";
		byte[] inMessage	 = message;
		
		if ("M8".equals(textType)) {
			seqNo         = "0000";
			encKey        = "STA" + StringUtils.leftPad(seqNo, 5, "0");

		} else {
			seqNo         = "0234";
			encKey        = "STA" + StringUtils.leftPad(seqNo, 5, "0");
		}	
		
		inMessage = ByteUtil.addArray(inMessage, MAKER_CODE);
		inMessage = ByteUtil.addArray(inMessage, encKey.getBytes());
		inMessage = ByteUtil.addArray(inMessage, textType.getBytes());
		inMessage = ByteUtil.addArray(inMessage, CONTROL_CD);
		inMessage = ByteUtil.addArray(inMessage, SETTLE_INDEX);
		inMessage = ByteUtil.addArray(inMessage, seqNo.getBytes());
		inMessage = ByteUtil.addArray(inMessage, termType.getBytes());

		tcLogger.info("setHeader=" + new String(inMessage));
		
		return inMessage;
	}
	
	public byte[] setBody(String textType, String pCardData, byte[] message, int amount,
			String pOrgApprovNo,
			String pOrgSaleDate,
			String termType,
			String yymmdd) {
		
		String termMngNo     = ""; 
		String cardData      = "";
		String orgApprovNo   = "";
		String orgSaleDate   = "";
		String unknownField1 = "";
		String totalAmount   = ""; 
		String passwd        = "";
		String pluCd         = "";
		byte[] inMessage 	 = message;
		
		if ("M8".equals(textType)) {
			termMngNo     = StringUtils.rightPad(TERM_MNG_NO, 10, " "); 
			cardData      = StringUtils.rightPad(pCardData, 37, " ");
			unknownField1 = "00";
			totalAmount   = ""; 
			orgApprovNo   = "";
			orgSaleDate   = "";
			passwd        = StringUtils.rightPad("", 16, " ");
			pluCd         = "KT".equals(termType) ? telecommUtil.getDiscountType() : "";
		} else if ("M6".equals(textType)) {
			termMngNo     = StringUtils.rightPad(TERM_MNG_NO, 10, " "); 
			cardData      = StringUtils.rightPad(pCardData, 37, " ");
			unknownField1 = "00";
			totalAmount   = Integer.toString(amount); 
			orgApprovNo   = pOrgApprovNo;
			orgSaleDate   = pOrgSaleDate;
			passwd        = StringUtils.rightPad("", 16, " ");
			pluCd         = "KT".equals(termType) ? telecommUtil.getDiscountType() : "";
		} else {
			termMngNo     = StringUtils.rightPad(TERM_MNG_NO, 10, " "); 
			cardData      = StringUtils.rightPad(pCardData, 37, " ");
			unknownField1 = "00";
			totalAmount   = Integer.toString(amount); 
			orgApprovNo   = "";
			orgSaleDate   = "";
			passwd        = StringUtils.rightPad("", 16, " ");
			pluCd         = "KT".equals(termType) ? telecommUtil.getDiscountType() : "";
		}		
		
		inMessage = ByteUtil.addArray(inMessage, FS);
		inMessage = ByteUtil.addArray(inMessage, termMngNo.trim().getBytes());
		inMessage = ByteUtil.addArray(inMessage, FS);
		if ("M6".equals(textType)) {
			inMessage = ByteUtil.addArray(inMessage, WCC_M6_KEYIN);
		} else {
			inMessage = ByteUtil.addArray(inMessage, WCC_M5_KEYIN);
		}
		inMessage = ByteUtil.addArray(inMessage, cardData.trim().getBytes());
		inMessage = ByteUtil.addArray(inMessage, FS);
		inMessage = ByteUtil.addArray(inMessage, FS);
		// ī���ȣ ��ȿ�Ⱓ, ����� ī��� ��ȿ�Ⱓ�� ���⶧���� 00 ǥ��
		inMessage = ByteUtil.addArray(inMessage, unknownField1.getBytes());
		inMessage = ByteUtil.addArray(inMessage, FS);
		inMessage = ByteUtil.addArray(inMessage, totalAmount.getBytes());
		inMessage = ByteUtil.addArray(inMessage, FS);
		
		tcLogger.info("kt_birth:"+telecommUtil.isKt_birth());
		tcLogger.info("lg_birth:"+telecommUtil.isLg_birth());
		
		tcLogger.info("termType:"+termType);
		tcLogger.info("textType:"+textType);
		
		//KT ����ī�� ����Ʈ ��ȸ/����/������� ������Ϸ� ����(���� KT�� ������� ����ó�� ����)
		//U+ ����ī�� ����Ʈ ��ȸ/����/������� ������Ϸ� ����
		if( ("KT".equals(termType) && telecommUtil.isKt_birth()) || "LM".equals(termType) && telecommUtil.isLg_birth() ) {
			if("M5".equals(textType) || "M6".equals(textType) || "M8".equals(textType)) {
				inMessage = ByteUtil.addArray(inMessage, yymmdd.getBytes());
			}
		}

		inMessage = ByteUtil.addArray(inMessage, FS);
		inMessage = ByteUtil.addArray(inMessage, orgApprovNo.getBytes());
		inMessage = ByteUtil.addArray(inMessage, FS);
		inMessage = ByteUtil.addArray(inMessage, orgSaleDate.getBytes());
		inMessage = ByteUtil.addArray(inMessage, FS);
		inMessage = ByteUtil.addArray(inMessage, passwd.trim().getBytes());
		inMessage = ByteUtil.addArray(inMessage, FS);
		inMessage = ByteUtil.addArray(inMessage, FS);
		
//		if (!"M6".equals(textType)) {
		inMessage = ByteUtil.addArray(inMessage, FS);
//		}
		
		inMessage = ByteUtil.addArray(inMessage, pluCd.getBytes());
		
		tcLogger.info("setBody=" + new String(inMessage));
		
		return inMessage;
	}
	
	/**
	 * KT-Vanilla ���� �޽��� Set.
	 * @param textType
	 * @param pCardData
	 * @param message
	 * @param amount
	 * @param pOrgApprovNo
	 * @param pOrgSaleDate
	 * @param termType
	 * @param yymmdd
	 * @return
	 */
	public byte[] setBodyKtVanilla(String textType, String pCardData, byte[] message, int amount,
			String pOrgApprovNo,
			String pOrgSaleDate,
			String termType,
			String yymmdd) {
		
		String termMngNo     = ""; 
		String cardData      = "";
		String orgApprovNo   = "";
		String orgSaleDate   = "";
		String unknownField1 = "";
		String totalAmount   = ""; 
		String passwd        = "";
		String pluCd         = "";
		byte[] inMessage 	 = message;
		
		if ("M8".equals(textType)) {
			termMngNo     = StringUtils.rightPad(TERM_MNG_NO, 6, " "); 
			cardData      = StringUtils.rightPad(pCardData, 44, " ");
			unknownField1 = "00";
			totalAmount   = StringUtils.rightPad("500", 3, " "); 
			orgApprovNo   = "";
			orgSaleDate   = "";
			passwd        = StringUtils.rightPad("", 16, " ");
			pluCd         = "KT".equals(termType) ? StringUtils.rightPad(telecommUtil.getDiscountType(), 6, " ") : "";
		} else if ("M6".equals(textType)) {
			termMngNo     = StringUtils.rightPad(TERM_MNG_NO, 6, " "); 
			cardData      = StringUtils.rightPad(pCardData, 44, " ");
			unknownField1 = "00";
			totalAmount   = Integer.toString(amount); 
			orgApprovNo   = pOrgApprovNo;
			orgSaleDate   = pOrgSaleDate;
			passwd        = StringUtils.rightPad("", 16, " ");
			pluCd         = "KT".equals(termType) ? StringUtils.rightPad(telecommUtil.getDiscountType(), 6, " ") : "";
		} else {
			termMngNo     = StringUtils.rightPad(TERM_MNG_NO, 6, " "); 
			cardData      = StringUtils.rightPad(pCardData, 44, " ");
			unknownField1 = "00";
			totalAmount   = Integer.toString(amount); 
			orgApprovNo   = "";
			orgSaleDate   = "";
			passwd        = StringUtils.rightPad("", 16, " ");
			pluCd         = "KT".equals(termType) ? StringUtils.rightPad(telecommUtil.getDiscountType(), 6, " ") : "";
		}		
		
		inMessage = ByteUtil.addArray(inMessage, FS);
		inMessage = ByteUtil.addArray(inMessage, termMngNo.trim().getBytes());	// �ܸ��� ������ȣ(6)
		inMessage = ByteUtil.addArray(inMessage, FS);
		if ("M6".equals(textType)) {
			inMessage = ByteUtil.addArray(inMessage, WCC_M6_KEYIN);
		} else {
			inMessage = ByteUtil.addArray(inMessage, WCC_M5_KEYIN);				// WCC(1)
		}
		inMessage = ByteUtil.addArray(inMessage, cardData.trim().getBytes());	// ī���ȣ(44)
		inMessage = ByteUtil.addArray(inMessage, FS);
		inMessage = ByteUtil.addArray(inMessage, FS);
		inMessage = ByteUtil.addArray(inMessage, unknownField1.getBytes());		// �ҺαⰣ(2)
		inMessage = ByteUtil.addArray(inMessage, FS);
		inMessage = ByteUtil.addArray(inMessage, totalAmount.getBytes());		// �Ѱŷ��ݾ�(3)
		inMessage = ByteUtil.addArray(inMessage, FS);
		
		tcLogger.info("kt_birth:"+telecommUtil.isKt_birth());
		tcLogger.info("lg_birth:"+telecommUtil.isLg_birth());
		
		tcLogger.info("termType:"+termType);
		tcLogger.info("textType:"+textType);
		
		//KT ����ī�� ����Ʈ ��ȸ/����/������� ������Ϸ� ����(���� KT�� ������� ��������)
		//U+ ����ī�� ����Ʈ ��ȸ/����/������� ������Ϸ� ����
		if( ("KT".equals(termType) && telecommUtil.isKt_birth()) || "LM".equals(termType) && telecommUtil.isLg_birth() ) {
			if("M5".equals(textType) || "M6".equals(textType) || "M8".equals(textType)) {
				inMessage = ByteUtil.addArray(inMessage, yymmdd.getBytes());
			}
		}

		inMessage = ByteUtil.addArray(inMessage, FS);
		
		// ���� ��ҽÿ��� �����ι�ȣ/���ŷ����ڸ� �����Ѵ�.
		if ("M6".equals(textType)){
			inMessage = ByteUtil.addArray(inMessage, orgApprovNo.getBytes());		// �����ι�ȣ
		}
		inMessage = ByteUtil.addArray(inMessage, FS);
		if ("M6".equals(textType)){
			inMessage = ByteUtil.addArray(inMessage, orgSaleDate.getBytes());		// ���ŷ�����
		}
		inMessage = ByteUtil.addArray(inMessage, FS);
//		message = ByteUtil.addArray(message, passwd.trim().getBytes());		// DSC�������
		inMessage = ByteUtil.addArray(inMessage, FS);
		inMessage = ByteUtil.addArray(inMessage, FS);
		inMessage = ByteUtil.addArray(inMessage, FS);
		
		inMessage = ByteUtil.addArray(inMessage, pluCd.getBytes());
		
		tcLogger.info("setBody=" + new String(inMessage));
		
		return inMessage;
	}
	
	
	public byte[] wrap(byte[] message) {
		String LRC = "";
		byte[] inMessage = message;
		inMessage = ByteUtil.addArray(STX, inMessage);		
		inMessage = ByteUtil.addArray(inMessage, ETX);
		inMessage = ByteUtil.addArray(inMessage, LRC.getBytes());
		
		return inMessage;
	}
	
	public byte[] wrapKtVanilla(byte[] message) {
		
		String CRC = "10";
		byte[] inMessage = message;
		
		inMessage = ByteUtil.addArray(STX, inMessage);
		inMessage = ByteUtil.addArray(inMessage, ETX2);
		inMessage = ByteUtil.addArray(inMessage, CRC.getBytes());
		
		return inMessage;
	}
	
	public byte[] getHeader(String trCode) {
		byte[] header = null;
		header = this.messageIdentifier.getBytes();
		header = ByteUtil.addArray(header, this.TERM_MNG_NO.getBytes());
		header = ByteUtil.addArray(header, FS);
		header = ByteUtil.addArray(header, "4".getBytes());
		header = ByteUtil.addArray(header, "0".getBytes());
		header = ByteUtil.addArray(header, trCode.getBytes());	
		return header;
	}
	
	/**
	 * FD ���� ������ body ����
	 * @param paramMap - body�� �� field��ȣ�� ���� ���� map
	 * @return FD���� ������ body
	 */
	public byte[] getBody(String trCode, Map<String, byte[]> paramMap) {
		byte[] body = getHeader(trCode);
		
		String key   = null;
		byte[] value = null;
		
		Iterator<String> ir = paramMap.keySet().iterator();
		while(ir.hasNext()) {
			key   = (String) ir.next();
			value = paramMap.get(key);
			body  = setBody(key, value, body);			
		}
		
		tcLogger.info(trCode + "(" + body.length+") :: " + new String(body));
		
		return body;
	}
	
	/**
	 * FD ���� ���� ����
	 * @param fieldIdentifier - ���� ���� field ��ȣ
	 * @param value - ���� ���� field ��
	 * @param msg - ���� ������ ���� strigbuffer
	 * @return ���� ������ msg�� �߰��ؼ� �����Ѵ�.
	 */
	private byte[] setBody(String fieldIdentifier, byte[] value, byte[] msg) {
		byte[] inMsg = msg;
		
		inMsg = ByteUtil.addArray(inMsg, FS);
		inMsg = ByteUtil.addArray(inMsg, fieldIdentifier.getBytes());
		inMsg = ByteUtil.addArray(inMsg, value);
		
		return inMsg;
	}
	
	public byte[] getPrefix(int msgLength) {
		byte[] prefix = new byte[6];
		byte[] size   = null;
		
		prefix[0] = (byte) cSTX;
		prefix[1] = (byte) ((char) 70);
		prefix[2] = (byte) ((char) 68);
		prefix[3] = (byte) cSTX;
		size = ByteUtil.intToBytes(msgLength);
		System.arraycopy(size, 2, prefix, 4, 2);
		
		return prefix;
	}
	
	public byte[] getSuffix() {
		byte[] suffix = new byte[4];
		
		suffix[0] = (byte) cETX;
		suffix[1] = (byte) ((char) 70);
		suffix[2] = (byte) ((char) 68);
		suffix[3] = (byte) cETX;
		
		return suffix;
	}	
	
	
	public static void main(String[] args) {
		
	}
}