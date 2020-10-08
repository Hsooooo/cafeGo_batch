package co.kr.istarbucks.xo.batch.common.telecomm;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import kt.ktds.aes256cipher.AES256Cipher;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import co.kr.istarbucks.xo.batch.common.util.ByteUtil;
import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;


public class TelecommBroker {
	private final Logger tcLogger = Logger.getLogger ("TELECOM");
	
	private final Configuration conf;
	private String serviceMode = "";
	
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
		"",
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
	
	//private static final String[] RESPONSE_FIELD_LIST = {
	//		"MakerCd",
	//		"EncKey",
	//		"TextType",
	//		"ControlCd",
	//		"SettleIndex",
	//		"SeqNo",
	//		"TermType",
	//		"TermMngNo",
	//		"RespCd",
	//		"BuyerCd",
	//		"ApprovDT",
	//		"ApprovNo",
	//		"IssuerCd",
	//		"IssuerNm",
	//		"ChainNo",
	//		"BuyerNm",
	//		"ScreenControlCd",
	//		"ScreenExpr",
	//		"Notice"
	//};
		
	private final String vanServerIp;
	private final int vanServerPort;
	
	// KT-Vanilla 서버 추가. 20180810
	private final String ktVanillaServerIp;
	private final int ktVanillaServerPort;
	
	/** 일반전문 timeout */
	private final int READ_TIMEOUT;
	
	/** Connection timeout(sec.) */
	private final int CONN_TIMEOUT;
	
	/** Connect retry max timeout(sec.) */
	private final int CONN_RETRY_MAX_CNT;
	
	/** buffer size */
	private static final int BUFFER_SIZE        = 1024;    
	private int connectRetryCnt    = 0;
	private int connectRetryCntKtVanilla    = 0;
	private final StringBuffer logBuf    = new StringBuffer();
	
	String merchantNumber          = null;
	
	private final TelecommUtil telecommUtil;
	
	//Constructor
//	public TelecommBroker(String merchantNumber) { 
//		this.vanServerIp        = "10.100.114.47";
//		this.vanServerPort      = 3283;
//		
//		this.CONN_TIMEOUT       = 20 * 1000;
//		this.READ_TIMEOUT       = 20 * 1000;		     
//		this.CONN_RETRY_MAX_CNT = 1;
//		
//		this.merchantNumber     = merchantNumber;
//	}
	
	//Constructor
	public TelecommBroker() { 
		this.conf = CommPropertiesConfiguration.getConfiguration("telecom.properties");
		this.serviceMode   = conf.getString("telecom.service.mode");
		//서비스모드가 정의되어 있지 않으면 운영모드로 설정
		if(StringUtils.isBlank(this.serviceMode)){
			this.serviceMode = "real";
		}
		tcLogger.info("===== TelecommBroker INIT:("+this.serviceMode+") ===============================");
		
		vanServerIp        = conf.getString(serviceMode+".van.kicc.ip");
		vanServerPort      = conf.getInt(serviceMode+".van.kicc.port");
		
		// KT-Vanilla 서버 추가. 20180330
		ktVanillaServerIp	= conf.getString(serviceMode+".kt.vanilla.ip");
		ktVanillaServerPort	= conf.getInt(serviceMode+".kt.vanilla.port");
		
		CONN_TIMEOUT       = conf.getInt(serviceMode+".conn.timeout");
		READ_TIMEOUT       = conf.getInt(serviceMode+".read.timeout");		     
		CONN_RETRY_MAX_CNT = conf.getInt(serviceMode+".conn.retry_count");
		
		merchantNumber     = conf.getString(serviceMode+".term_mng_no");
		
		//제휴사 카드 조회, 할인승인 시 인증번호로 생년월일 사용 유무
		String telecommType = conf.getString("telecomm.birthYmd.check");
		telecommUtil = new TelecommUtil(telecommType);
	}
	
	/**
	 * 포인트 조회 요청전문 (전문구분코드:M8)
	 * @param termType
	 * @param cardData
	 * @return
	 */
	public Map<String, String> inquireForPoint(String termType, String cardData) {
		Map<String, byte[]> parameters = null;
		Map<String, String> result     = null;
		
		SocketChannel sc    = null;
		long startTime      = System.currentTimeMillis();
		StringBuffer logBuf = new StringBuffer(1024);

		try {
			tcLogger.info("===== TelecommBroker [POINT_INQUIRE] START ===============================");
			tcLogger.info("===== [inquireForPoint] {termType=" + termType + ", cardData=" + cardData + "} =====");
			
			String pTermType = "";
			if ("K".equals(termType)) {
				pTermType = "KT";
			} else if ("U".equals(termType)) {
				pTermType = "LM";
			} else {
				throw new TelecomException("Parameter::termType is not valid!"); 
			}
			if(StringUtils.isBlank(cardData)){
				throw new TelecomException("Parameter::cardData is Null!");
			}
			
			parameters = new HashMap<String, byte[]>();
			parameters.put("TermType", pTermType.getBytes());
			parameters.put("cardData", cardData.getBytes());
			parameters.put("TextType", "M8".getBytes());
			
			sc = connect();
			
			if (sc.isConnected()) {
				result = this.sendMessage(sc, "M8", parameters, cardData, 0, "", "", pTermType,"");
			}
		} catch (TelecomException te) {
			tcLogger.error("inquireForPoint TelecomException -> " + te.getMessage());
			
			result = new HashMap<String, String>();
			result.put("RespCd", 	 "9997");
			result.put("ScreenExpr", "통신사제휴 : 지원하지 않는 통신사{KT, U+만 가능} 또는 필수 파라미터 누락");
		} catch (IOException ioe) {
			tcLogger.error("inquireForPoint IOException -> " + ioe.getMessage(),ioe);
			
			result = new HashMap<String, String>();
			result.put("RespCd", 	 "9998");
			result.put("ScreenExpr", "통신사 제휴 : 통신사 Connection 에러");
		} catch (Exception e) {
			tcLogger.error("inquireForPoint Exception -> " + e.getMessage(),e);
			
			result = new HashMap<String, String>();
			result.put("RespCd", 	 "9999");
			result.put("ScreenExpr", "통신사 제휴 : 통신사 시스템(기타) 에러");
		} finally {
			try {
				disconnect(sc);
			} catch (IOException e) {
				tcLogger.error("inquireForPoint disconnect IOException -> " + e.getMessage(), e);
			}
			
			tcLogger.info(logBuf.append("==== TelecommBroker [POINT_INQUIRE] END ").append (" [").append (System.currentTimeMillis () - startTime).append ("ms] ===============================").toString());			
			tcLogger.info("==== TelecommBroker [POINT_INQUIRE] END =================================");
		}
		
		return result;
	}
	
	/**
	 * 포인트 조회 요청전문 (전문구분코드:M8)
	 * @param termType
	 * @param cardData
	 * @return
	 */
	public Map<String, String> inquireForPoint(String termType, String cardData, String birthYmd) {
		Map<String, byte[]> parameters = null;
		Map<String, String> result     = null;
		
		SocketChannel sc    = null;
		long startTime      = System.currentTimeMillis();
		StringBuffer logBuf = new StringBuffer(1024);

		try {
			tcLogger.info("==== TelecommBroker [POINT_INQUIRE] START ===============================");
			tcLogger.info("===== [inquireForPoint] {termType=" + termType + ", cardData=" + cardData + ", birthYmd="+birthYmd+ "} =====");
			
			String pYYMMDD = "";
			String pTermType = "";
			if ("K".equals(termType)) {
				pTermType = "KT";
				pYYMMDD = "OL"+birthYmd;
			} else if ("U".equals(termType)) {
				pTermType = "LM";
				pYYMMDD = "OL"+birthYmd;
			} else {
				throw new TelecomException("Parameter::termType is not valid!"); 
			}
			if(StringUtils.isBlank(cardData)){
				throw new TelecomException("Parameter::cardData is Null!");
			}
			if(StringUtils.isBlank(birthYmd)){
				throw new TelecomException("Parameter::birthYmd is Null!");
			}
			
			parameters = new HashMap<String, byte[]>();
			parameters.put("TermType", pTermType.getBytes());
			parameters.put("cardData", cardData.getBytes());
			parameters.put("TextType", "M8".getBytes());
			
			sc = connect();
			
			if (sc.isConnected()) {
				result = this.sendMessage(sc, "M8", parameters, cardData, 0, "", "", pTermType, pYYMMDD);
			}
		} catch (TelecomException te) {
			tcLogger.error("inquireForPoint TelecomException -> " + te.getMessage());
			
			result = new HashMap<String, String>();
			result.put("RespCd", 	 "9997");
			result.put("ScreenExpr", "통신사제휴 : 지원하지 않는 통신사{KT, U+만 가능} 또는 필수 파라미터 누락");
		} catch (IOException ioe) {
			tcLogger.error("inquireForPoint IOException -> " + ioe.getMessage(), ioe);
			
			result = new HashMap<String, String>();
			result.put("RespCd", 	 "9998");
			result.put("ScreenExpr", "통신사 제휴 : 통신사 Connection 에러");
		} catch (Exception e) {
			tcLogger.error("inquireForPoint Exception -> " + e.getMessage(),e);
			
			result = new HashMap<String, String>();
			result.put("RespCd", 	 "9999");
			result.put("ScreenExpr", "통신사 제휴 : 통신사 시스템(기타) 에러");
		} finally {
			try {
				disconnect(sc);
			} catch (IOException e) {
				tcLogger.error("inquireForPoint disconnect IOException -> " + e.getMessage(), e);
			}
			
			tcLogger.info(logBuf.append("==== TelecommBroker [POINT_INQUIRE] END ").append (" [").append (System.currentTimeMillis () - startTime).append ("ms] ===============================").toString());			
			tcLogger.info("==== TelecommBroker [POINT_INQUIRE] END =================================");
		}
		
		return result;
	}
	
	/**
	 * 할인 조회 요청전문 (전문구분:M5)
	 * @param termType
	 * @param cardData
	 * @return
	 */
	public Map<String, String> inquireForDiscount(String termType, String cardData, int amount) {
		Map<String, String> result = null;
		Map<String, byte[]> parameters = new HashMap<String, byte[]>();
		
		SocketChannel sc    = null;
		long startTime      = System.currentTimeMillis();
		StringBuffer logBuf = new StringBuffer(1024);
		
		try {
			this.tcLogger.info("==== TelecommBroker [DISCOUT] START ===============================");
			tcLogger.info("===== [inquireForDiscount] {termType=" + termType + ", cardData=" + cardData + ", amount=" + amount + "} =====");
			
			String pTermType = "";
			if ("K".equals(termType)) {
				pTermType = "KT";
			} else if ("U".equals(termType)) {
				pTermType = "LM";
			} else {
				throw new TelecomException("Parameter::termType is not valid!"); 
			}
			if(StringUtils.isBlank(cardData)){
				throw new TelecomException("Parameter::cardData is Null!");
			}
			
			parameters.put("TermType", pTermType.getBytes());
			parameters.put("cardData", cardData.getBytes());
			parameters.put("TextType", "M5".getBytes());
			
			sc = connect();
			if (sc.isConnected()){
				result = this.sendMessage(sc, "M5", parameters, cardData, amount, "", "", pTermType,"");
			}
		} catch (TelecomException te) {
			tcLogger.error("inquireForDiscount TelecomException -> " + te.getMessage());
			
			result = new HashMap<String, String>();
			result.put("RespCd", 	 "9997");
			result.put("ScreenExpr", "통신사제휴 : 지원하지 않는 통신사{KT, U+만 가능} 또는 필수 파라미터 누락");
		} catch (IOException ioe) {
			tcLogger.error("inquireForDiscount IOException -> " + ioe.getMessage(),ioe);
			
			result = new HashMap<String, String>();
			result.put("RespCd", 	 "9998");
			result.put("ScreenExpr", "통신사 제휴 : 통신사 Connection 에러");
		} catch (Exception e) {
			tcLogger.error("inquireForDiscount Exception -> " + e.getMessage(),e);
			
			result = new HashMap<String, String>();
			result.put("RespCd", 	 "9999");
			result.put("ScreenExpr", "통신사 제휴 : 통신사 시스템(기타) 에러");
		} finally {
			try {
				disconnect(sc);
			} catch (IOException e) {
				tcLogger.error("inquireForDiscount disconnectd IOException -> " + e.getMessage(),e);
			}
			tcLogger.info(logBuf.append("==== TelecommBroker [DISCOUT] END ").
					append (" [").append (System.currentTimeMillis () - startTime).
					append ("ms] ===============================").
					toString());
			tcLogger.info("==== TelecommBroker [DISCOUT] END =================================");
		}
		
		return result;
	}
	
	/**
	 * 할인 조회 요청전문 (전문구분:M5)
	 * @param termType
	 * @param cardData
	 * @return
	 */
	public Map<String, String> inquireForDiscount(String termType, String cardData, int amount, String birthYmd) {
		Map<String, String> result = null;
		Map<String, byte[]> parameters = new HashMap<String, byte[]>();
		
		SocketChannel sc    = null;
		long startTime      = System.currentTimeMillis();
		StringBuffer logBuf = new StringBuffer(1024);
		
		try {
			this.tcLogger.info("==== TelecommBroker [POINT_DISCOUT] START ===============================");
			tcLogger.info("===== [inquireForDiscount] {termType=" + termType + ", cardData=" + cardData + ", amount=" + amount + ", birthYmd="+birthYmd+ "} =====");
			
			String pYYMMDD = "";
			String pTermType = "";
			if ("K".equals(termType)) {
				pTermType = "KT";
				pYYMMDD = "OL"+birthYmd;
			} else if ("U".equals(termType)) {
				pTermType = "LM";
				pYYMMDD = "OL"+birthYmd;
			} else {
				throw new TelecomException("Parameter::termType is not valid!"); 
			}
			if(StringUtils.isBlank(cardData)){
				throw new TelecomException("Parameter::cardData is Null!");
			}
			if(StringUtils.isBlank(birthYmd)){
				throw new TelecomException("Parameter::birthYmd is Null!");
			}
			
			parameters.put("TermType", pTermType.getBytes());
			parameters.put("cardData", cardData.getBytes());
			parameters.put("TextType", "M5".getBytes());
			
			sc = connect();
			if (sc.isConnected()){
				result = this.sendMessage(sc, "M5", parameters, cardData, amount, "", "", pTermType, pYYMMDD);
			}
		} catch (TelecomException te) {
			tcLogger.error("inquireForDiscount TelecomException -> " + te.getMessage());
			
			result = new HashMap<String, String>();
			result.put("RespCd", 	 "9997");
			result.put("ScreenExpr", "통신사제휴 : 지원하지 않는 통신사{KT, U+만 가능} 또는 필수 파라미터 누락");
		} catch (IOException ioe) {
			tcLogger.error("inquireForDiscount IOException -> " + ioe.getMessage(),ioe);
			
			result = new HashMap<String, String>();
			result.put("RespCd", 	 "9998");
			result.put("ScreenExpr", "통신사 제휴 : 통신사 Connection 에러");
		} catch (Exception e) {
			tcLogger.error("inquireForDiscount Exception -> " + e.getMessage(),e);
			
			result = new HashMap<String, String>();
			result.put("RespCd", 	 "9999");
			result.put("ScreenExpr", "통신사 제휴 : 통신사 시스템(기타) 에러");
		} finally {
			try {
				disconnect(sc);
			} catch (IOException e) {
				tcLogger.error("inquireForDiscount disconnectd IOException -> " + e.getMessage(),e);
			}
			tcLogger.info(logBuf.append("==== TelecommBroker [POINT_DISCOUT] END ").
					append (" [").append (System.currentTimeMillis () - startTime).
					append ("ms] ===============================").
					toString());
			tcLogger.info("==== TelecommBroker [POINT_DISCOUT] END =================================");
		}
		
		return result;
	}
	
	/**
	 * 할인 조회 취소 요청전문 (전문구분:M6)
	 * @param termType
	 * @param cardData
	 * @return
	 */
	public Map<String, String> inquireForDiscountCancel(String termType, String cardData, int amount, String orgApprovNo, String orgSaleDate) {
		Map<String, String> result = null;
		Map<String, byte[]> parameters = new HashMap<String, byte[]>();
		
		SocketChannel sc    = null;
		long startTime      = System.currentTimeMillis();
		StringBuffer logBuf = new StringBuffer(1024);
		
		try {
			tcLogger.info("==== TelecommBroker [POINT_DISCOUT_CANCEL] START ===============================");
			tcLogger.info("===== [inquireForDiscountCancel] {termType=" + termType + ", cardData=" + cardData + ", amount=" + amount + ", orgApprovNo=" + orgApprovNo + ", orgSaleDate=" + orgSaleDate + "} =====");
			
			String pTermType = "";
			if ("K".equals(termType)) {
				pTermType = "KT";
			} else if ("U".equals(termType)) {
				pTermType = "LM";
			} else {
				throw new TelecomException("Parameter::termType is not valid!"); 
			}
			if(StringUtils.isBlank(cardData)){
				throw new TelecomException("Parameter::cardData is Null!");
			}
			if(StringUtils.isBlank(orgApprovNo)){
				throw new TelecomException("Parameter::orgApprovNo is Null!");
			}
			if(StringUtils.isBlank(orgSaleDate)){
				throw new TelecomException("Parameter::orgSaleDate is Null!");
			}
			
			parameters.put("TermType", pTermType.getBytes());
			parameters.put("cardData", cardData.getBytes());
			parameters.put("TextType", "M6".getBytes());
			
			sc = connect();
			
			if (sc.isConnected()){
				result = this.sendMessage(sc, "M6", parameters, cardData, amount, orgApprovNo, orgSaleDate, pTermType,"");
			}
			
		} catch (TelecomException te) {
			tcLogger.error("inquireForDiscountCancel TelecomException -> " + te.getMessage());
			
			result = new HashMap<String, String>();
			result.put("RespCd", 	 "9997");
			result.put("ScreenExpr", "통신사제휴 : 지원하지 않는 통신사{KT, U+만 가능} 또는 필수 파라미터 누락");
		} catch (IOException ioe) {
			tcLogger.error("inquireForDiscountCancel IOException -> " + ioe.getMessage(),ioe);
			
			result = new HashMap<String, String>();
			result.put("RespCd", 	 "9998");
			result.put("ScreenExpr", "통신사 제휴 : 통신사 Connection 에러");
		} catch (Exception e) {
			tcLogger.error("inquireForDiscountCancel Exception -> " + e.getMessage(),e);
			
			result = new HashMap<String, String>();
			result.put("RespCd", 	 "9999");
			result.put("ScreenExpr", "통신사 제휴 : 통신사 시스템(기타) 에러");
		} finally {
			try {
				disconnect(sc);
			} catch (IOException e) {
				tcLogger.error("inquireForDiscountCancel disconnect IOException -> " + e.getMessage(),e);
			}
			tcLogger.info(logBuf.append("==== TelecommBroker [POINT_DISCOUT_CANCEL] END ").
					append (" [").append (System.currentTimeMillis () - startTime).
					append ("ms] ===============================").
					toString());			
			tcLogger.info("==== TelecommBroker [POINT_DISCOUT_CANCEL] END =================================");
		}
		
		return result;
	}	
	
	/**
	 * 할인 조회 취소 요청전문 (전문구분:M6)
	 * @param termType
	 * @param cardData
	 * @return
	 */
	public Map<String, String> inquireForDiscountCancel(String termType, String cardData, int amount, String orgApprovNo, String orgSaleDate, String birthYmd) {
		Map<String, String> result = null;
		Map<String, byte[]> parameters = new HashMap<String, byte[]>();
		
		SocketChannel sc    = null;
		long startTime      = System.currentTimeMillis();
		StringBuffer logBuf = new StringBuffer(1024);
		
		try {
			tcLogger.info("==== TelecommBroker [POINT_DISCOUT_CANCEL] START ===============================");
			tcLogger.info("===== [inquireForDiscountCancel] {termType=" + termType + ", cardData=" + cardData + ", amount=" + amount + ", orgApprovNo=" + orgApprovNo + ", orgSaleDate=" + orgSaleDate + ", birthYmd=" + birthYmd + "} =====");
			
			String pYYMMDD = "";
			String pTermType = "";
			if ("K".equals(termType)) {
				pTermType = "KT";
				pYYMMDD = "OL"+birthYmd;
			} else if ("U".equals(termType)) {
				pTermType = "LM";
				pYYMMDD = "OL"+birthYmd;
			} else {
				throw new TelecomException("Parameter::termType is not valid!"); 
			}
			if(StringUtils.isBlank(cardData)){
				throw new TelecomException("Parameter::cardData is Null!");
			}
			if(StringUtils.isBlank(orgApprovNo)){
				throw new TelecomException("Parameter::orgApprovNo is Null!");
			}
			if(StringUtils.isBlank(orgSaleDate)){
				throw new TelecomException("Parameter::orgSaleDate is Null!");
			}
			if(StringUtils.isBlank(birthYmd)){
				throw new TelecomException("Parameter::birthYmd is Null!");
			}
			
			parameters.put("TermType", pTermType.getBytes());
			parameters.put("cardData", cardData.getBytes());
			parameters.put("TextType", "M6".getBytes());
			
			/* KT인 경우 KT-Vanilla 서버에 우선 접속되도록 변경. 20180330
			 * 
			 * - KT-Vanilla에 우선 접속 시도 후, 미연결시 기존 VAN으로 접속한다.
			 */
			if("K".equals(termType)){
				
				sc = connectKtVanilla();
				
				if(sc.isConnected()){
					tcLogger.info("KT-Vanilla connected..");
					result = this.sendMessageKtVanilla(sc, "M6", parameters, cardData, amount, orgApprovNo, orgSaleDate, pTermType, pYYMMDD);
					
					//정상처리 아닐경우 VAN사로 다시 시도
					if(!"0000".equals(String.valueOf(result.get("RespCd")))){
						disconnect(sc);
						
						sc = connect();
						
						if (sc.isConnected()) {
							tcLogger.info("VAN connected..");
							result = this.sendMessage(sc, "M6", parameters, cardData, amount, orgApprovNo, orgSaleDate, pTermType, pYYMMDD);
						}
					}
				}else{
					tcLogger.info("KT-Vanilla connection failed.. connecting to VAN...");
					// Vanilla에 접속 안될 경우 기존 VAN 접속
					sc = connect();
					
					if (sc.isConnected()) {
						tcLogger.info("VAN connected..");
						result = this.sendMessage(sc, "M6", parameters, cardData, amount, orgApprovNo, orgSaleDate, pTermType, pYYMMDD);
					}
				}
			}else{
				
				sc = connect();
				
				if (sc.isConnected()){
					result = this.sendMessage(sc, "M6", parameters, cardData, amount, orgApprovNo, orgSaleDate, pTermType, pYYMMDD);
				}
			}
			
		} catch (TelecomException te) {
			tcLogger.error("inquireForDiscountCancel TelecomException -> " + te.getMessage());
			
			result = new HashMap<String, String>();
			result.put("RespCd", 	 "9997");
			result.put("ScreenExpr", "통신사제휴 : 지원하지 않는 통신사{KT, U+만 가능} 또는 필수 파라미터 누락");
		} catch (IOException ioe) {
			tcLogger.error("inquireForDiscountCancel IOException -> " + ioe.getMessage(),ioe);
			
			result = new HashMap<String, String>();
			result.put("RespCd", 	 "9998");
			result.put("ScreenExpr", "통신사 제휴 : 통신사 Connection 에러");
		} catch (Exception e) {
			tcLogger.error("inquireForDiscountCancel Exception -> " + e.getMessage(),e);
			
			result = new HashMap<String, String>();
			result.put("RespCd", 	 "9999");
			result.put("ScreenExpr", "통신사 제휴 : 통신사 시스템(기타) 에러");
		} finally {
			try {
				disconnect(sc);
			} catch (IOException e) {
				tcLogger.error("inquireForDiscountCancel disconnect IOException -> " + e.getMessage(),e);
			}
			tcLogger.info(logBuf.append("==== TelecommBroker [POINT_DISCOUT_CANCEL] END ").
					append (" [").append (System.currentTimeMillis () - startTime).
					append ("ms] ===============================").
					toString());			
			tcLogger.info("==== TelecommBroker [POINT_DISCOUT_CANCEL] END =================================");
		}
		
		return result;
	}
	
	/**
	 * VAN서버와의 연결을 시작한다. 
	 */
	private SocketChannel connect() throws IOException {
        SocketChannel sc        = null;
        long startTime          = System.currentTimeMillis();
        boolean isPossibleRetry = false;
        Exception excetion      = null;
        
        if (vanServerIp == null || vanServerPort == 0) {
        	throw new IOException();
        }
        
        try {
            sc = SocketChannel.open();
            sc.socket().connect(new InetSocketAddress(vanServerIp, vanServerPort), CONN_TIMEOUT);
            sc.configureBlocking(false);
        } catch (SocketTimeoutException ste) {
            isPossibleRetry = true;
            excetion = ste;
        } catch (ConnectException ce) {
            isPossibleRetry = true;
            excetion = ce;
        }
		
        // 재시도
        if (isPossibleRetry) {
        	connectRetryCnt++;
        	tcLogger.info(logBuf.delete(0, logBuf.length()).append("try connection : ").append(connectRetryCnt).
        					append(" [").append(System.currentTimeMillis() - startTime).append("ms]").toString());
            
            // (현재시도회수) < (재시도 가능 회수) + 기본 1회
            if (connectRetryCnt < CONN_RETRY_MAX_CNT + 1) {
                connect();
            } else {
                logBuf.delete(0, logBuf.length()).append("Connection Error");
                tcLogger.error(logBuf.toString());
                
                // 오류 SMS 발송을 위해 stderr에 출력.
                tcLogger.error(excetion.getMessage(), excetion);
            }
        }
        
		return sc;
	}
	
	/**
	 * KT-Vanilla 서버와의 연결을 시작한다. 
	 */
	private SocketChannel connectKtVanilla() throws IOException {
        SocketChannel sc        = null;
        long startTime          = System.currentTimeMillis();
        boolean isPossibleRetry = false;
        Exception excetion      = null;
        
        if (ktVanillaServerIp == null || ktVanillaServerPort == 0) {
        	throw new IOException();
        }
        
        try {
            sc = SocketChannel.open();
            sc.socket().connect(new InetSocketAddress(ktVanillaServerIp, ktVanillaServerPort), CONN_TIMEOUT);
            sc.configureBlocking(false);
        } catch (SocketTimeoutException ste) {
            isPossibleRetry = true;
            excetion = ste;
        } catch (ConnectException ce) {
            isPossibleRetry = true;
            excetion = ce;
        }
		
        // 재시도
        if (isPossibleRetry) {
        	connectRetryCntKtVanilla++;
        	tcLogger.info(
        			logBuf.delete(0, logBuf.length()).
        					append("try connection : ").
        					append(connectRetryCntKtVanilla).
        					append(" [").append(System.currentTimeMillis() - startTime).append("ms]"));
            
            // (현재시도회수) < (재시도 가능 회수) + 기본 1회
            if (connectRetryCntKtVanilla < CONN_RETRY_MAX_CNT + 1) {
            	connectKtVanilla();
            } else {
                logBuf.delete(0, logBuf.length()).append("Connection Error(KT-Vanilla)");
                tcLogger.error(logBuf.toString());
                
                // 오류 SMS 발송을 위해 stderr에 출력.
                tcLogger.error(excetion.getMessage(), excetion);
            }
        }
        
		return sc;
	}
	
	private void send(SocketChannel channel, ByteBuffer buffer, byte[] data) throws IOException {
		buffer.put(data);
		buffer.flip();
		while (buffer.hasRemaining()) {
			int writeCount = channel.write(buffer);
			tcLogger.info("Write count=" + writeCount);
		}
	}	
	
	private Map<String, String> sendMessage(
			SocketChannel sc,
			String textType,
			Map<String, byte[]> sendParamMap,
			String cardData,
			int amount,
			String orgApprovNo,
			String orgSaleDate,
			String termType,
			String yymmdd) {
		Map<String, String> result = null;
		byte[] sendMsg             = null;
		ByteBuffer tempWriteBuffer = null;
		ByteBuffer tempReadBuffer  = null;		
		Selector selector          = null;
		boolean isTimeoutReversal  = false;
		
		try {
//			sendMsg         = makeSendMessage(textType, sendParamMap, cardData, amount, orgApprovNo, orgSaleDate, termType, yymmdd);
			sendMsg         = makeSendMessage(textType, cardData, amount, orgApprovNo, orgSaleDate, termType, yymmdd);
			tempWriteBuffer = ByteBuffer.allocate(sendMsg.length);
			tempReadBuffer  = ByteBuffer.allocate(BUFFER_SIZE);
			
			//=====================================================================
			// 1. send request
			//=====================================================================
			tcLogger.info("sending...");
			send(sc, tempWriteBuffer, sendMsg);
			tcLogger.info("sendMsg byte[] :: " + new String(sendMsg));
			
			byte[] temp         = null;
			byte[] responseByte = null;
			int read            = 0;
			int loopCnt         = 0;			
			long s1Time         = System.currentTimeMillis();
			long s2Time         = 0L;
			
			selector = Selector.open();
			sc.register(selector, SelectionKey.OP_READ);
			
			//=====================================================================
			// 2. receive response
			//=====================================================================
			while (true) {				
				if (selector.select(1000) == 0) {
					s2Time = System.currentTimeMillis() - s1Time;
					this.tcLogger.info("timer :: " + s2Time);
					if (s2Time > READ_TIMEOUT) {
						isTimeoutReversal = true;
						break;
					}
					continue;
				}
				
				read = sc.read(tempReadBuffer);
				if (read == 0) {
					continue;
				}
				if (read == -1) {
					throw new Exception ("connection close");
				}
				
				this.tcLogger.info("timer :: " + s2Time);
				
				temp = new byte[read];
				System.arraycopy(tempReadBuffer.array(), 0, temp, 0, read);
				if (loopCnt == 0) {
					responseByte = new byte[read];
					System.arraycopy(temp, 0, responseByte, 0, read);
				} else {
					ByteUtil.addArray(responseByte, temp);
				}
				
				if (read < BUFFER_SIZE) {
					break;
				}
				loopCnt++;				
			}
			
			//=====================================================================
			// 3. parse response message (if timeout encounter, post process : time-out-reversal)
			//=====================================================================
			if (! isTimeoutReversal) {
				this.tcLogger.info("receiveMsg byte[] :: " + new String(responseByte,"euc-kr"));
				
				if (tcLogger.isDebugEnabled ()) {
				    this.tcLogger.debug("Time :: " + s2Time );
	                this.tcLogger.debug("sendMsg byte[] : " + new String(sendMsg));
	                this.tcLogger.debug("receiveMsg byte[] : " + new String(responseByte,"euc-kr"));
				}
				
				tcLogger.info("recvMsg byte[] :: " + new String(responseByte,"euc-kr"));
//				Message.parseResponse2("M6", responseByte);
				
				tcLogger.info("[parseResponse] START ===");
//				result = parseResponse(textType, responseByte);
				result = Message.parseResponse2(textType, responseByte);
				tcLogger.info("TELECOME parseResponse :: " + result);
				tcLogger.info("[parseResponse] END ===");
			} else {	/*전문 전달 후 응답을 받지 못한 경우 */
				tcLogger.info("[sendTimeoutReversalMessage] START ===");
//				result = sendTimeoutReversalMessage(sc, textType, sendParamMap);
				result = sendTimeoutReversalMessage(sc, sendParamMap);
				tcLogger.info("TELECOME sendTimeoutReversalMessage :: " + result);
				tcLogger.info("[sendTimeoutReversalMessage] END ===");
			}

		} catch (IOException e) {
			tcLogger.error("sendMessage IOException -> " + e.getMessage(), e);
		} catch (Exception e) {
			tcLogger.error("sendMessage Exception -> " + e.getMessage(), e);
		} finally {
			try {
				if (selector != null) {
					selector.close();
				}
			} catch (Exception e) {
				// do?!
				tcLogger.error(e.getMessage(), e);
			}
		}
		
		return result;
	}
	
	/**
	 * KT-Vanilla 서버 메시지 전송 및 응답 메시지 파싱
	 * 
	 * @param sc
	 * @param textType
	 * @param sendParamMap
	 * @param cardData
	 * @param amount
	 * @param orgApprovNo
	 * @param orgSaleDate
	 * @param termType
	 * @param yymmdd
	 * @return
	 */
	private Map<String, String> sendMessageKtVanilla(
			SocketChannel sc,
			String textType,
			Map<String, byte[]> sendParamMap,
			String cardData,
			int amount,
			String orgApprovNo,
			String orgSaleDate,
			String termType,
			String yymmdd) {
		Map<String, String> result = null;
		byte[] sendMsg             = null;
		ByteBuffer tempWriteBuffer = null;
		ByteBuffer tempReadBuffer  = null;		
		Selector selector          = null;
		boolean isTimeoutReversal  = false;
		
		try {
//			sendMsg         = makeSendMessageKtVanilla(textType, sendParamMap, cardData, amount, orgApprovNo, orgSaleDate, termType, yymmdd);
			sendMsg         = makeSendMessageKtVanilla(textType, cardData, amount, orgApprovNo, orgSaleDate, termType, yymmdd);
			tempWriteBuffer = ByteBuffer.allocate(sendMsg.length);
			tempReadBuffer  = ByteBuffer.allocate(BUFFER_SIZE);
			
			//=====================================================================
			// 1. send request
			//=====================================================================
			tcLogger.info("sending...");
			send(sc, tempWriteBuffer, sendMsg);
			tcLogger.info("sendMsg byte[] :: " + new String(sendMsg));
			
			byte[] temp         = null;
			byte[] responseByte = null;
			int read            = 0;
			int loopCnt         = 0;			
			long s1Time         = System.currentTimeMillis();
			long s2Time         = 0L;
			
			selector = Selector.open();
			sc.register(selector, SelectionKey.OP_READ);
			
			//=====================================================================
			// 2. receive response
			//=====================================================================
			while (true) {
				if (selector.select(1000) == 0) {
					s2Time = System.currentTimeMillis() - s1Time;
					this.tcLogger.info("timer :: " + s2Time);
					if (s2Time > READ_TIMEOUT) {
						isTimeoutReversal = true;
						break;
					}
					continue;
				}
				
				read = sc.read(tempReadBuffer);
				if (read == 0) {
					continue;
				}
				if (read == -1) {
					throw new Exception ("connection close");
				}
				
				this.tcLogger.info("timer :: " + s2Time);
				
				temp = new byte[read];
				System.arraycopy(tempReadBuffer.array(), 0, temp, 0, read);
				if (loopCnt == 0) {
					responseByte = new byte[read];
					System.arraycopy(temp, 0, responseByte, 0, read);
				} else {
					ByteUtil.addArray(responseByte, temp);
				}
				
				if (read < BUFFER_SIZE) {
					break;
				}
				loopCnt++;				
			}
			
			//=====================================================================
			// 3. parse response message (if timeout encounter, post process : time-out-reversal)
			//=====================================================================
			if (! isTimeoutReversal) {
				this.tcLogger.info("receiveMsg byte[] :: " + new String(responseByte,"euc-kr"));
				
				if (tcLogger.isDebugEnabled ()) {
				    this.tcLogger.debug("Time :: " + s2Time );
	                this.tcLogger.debug("sendMsg byte[] : " + new String(sendMsg));
	                this.tcLogger.debug("receiveMsg byte[] : " + new String(responseByte,"euc-kr"));
				}
				
				tcLogger.info("recvMsg byte[] :: " + new String(responseByte,"euc-kr"));
				
				tcLogger.info("[parseResponse] START ===");
				
				result = Message.parseResponseKtVanilla(textType, responseByte);	// for KT-Vanilla
				
				tcLogger.info("TELECOME parseResponse :: " + result);
				tcLogger.info("[parseResponse] END ===");
			} else {	/*전문 전달 후 응답을 받지 못한 경우 */
				tcLogger.info("[sendTimeoutReversalMessage] START ===");
//				result = sendTimeoutReversalMessage(sc, textType, sendParamMap);
				result = sendTimeoutReversalMessage(sc, sendParamMap);
				tcLogger.info("TELECOME sendTimeoutReversalMessage :: " + result);
				tcLogger.info("[sendTimeoutReversalMessage] END ===");
			}

		} catch (IOException e) {
			tcLogger.error("sendMessage IOException -> " + e.getMessage(), e);
		} catch (Exception e) {
			tcLogger.error("sendMessage Exception -> " + e.getMessage(), e);
		} finally {
			try {
				if (selector != null) {
					selector.close();
				}
			} catch (Exception e) {
				// do?!
				tcLogger.error(e.getMessage(), e);
			}
		}
		
		return result;
	}
	
	/**
	 * FD 연동 후 연동 결과 수신 - 원 요청 취소
	 * @param trCodeOri - 원 요청 연동 코드
	 * @param sendParamMap - 원 요청 연동 인자
	 * @return 연동 결과를 담은 map
	 */
//	private Map<String, String> sendTimeoutReversalMessage(SocketChannel sc, String trCodeOri, Map<String, byte[]> sendParamMap) {
	private Map<String, String> sendTimeoutReversalMessage(SocketChannel sc, Map<String, byte[]> sendParamMap) {
		
		String timeoutReversalCode = "";
		
//		String timeoutReversalCode = FDCode.TRCODE.TIMEOUT_REVERSAL;
//		if("0110".equals(trCodeOri) || "0810".equals(trCodeOri)) {	// bulk는 0705 코드 사용
//			timeoutReversalCode = FDCode.TRCODE.TIMEOUT_REVERSAL_BULK;
//		}
		
		Map<String, String> result = null;
//		byte[] sendMsg             = makeTimeoutReversalMessage(trCodeOri, sendParamMap);
		byte[] sendMsg             = makeTimeoutReversalMessage(sendParamMap);
		this.tcLogger.info("sendMsg byte[] :: " + new String(sendMsg));		
		boolean isTimeoutReversal  = false;
		ByteBuffer tempWriteBuffer = ByteBuffer.allocate(sendMsg.length);
		ByteBuffer tempReadBuffer  = ByteBuffer.allocate(BUFFER_SIZE);		
		SocketChannel inSc = sc;
		
		try {
			
			if (!inSc.isConnected()) {
				inSc = connect();
			}
			
			send(inSc, tempWriteBuffer, sendMsg);

			byte[] temp         = null;
			byte[] responseByte = null;
			int read            = 0;
			int loopCnt         = 0;			
			long s1Time         = System.currentTimeMillis();
			long s2Time         = 0L;
			
			Selector selector = Selector.open();
			inSc.register(selector, SelectionKey.OP_READ);
			
			while (true) {
				if (selector.select(1000) == 0) {
					s2Time = System.currentTimeMillis() - s1Time;
					this.tcLogger.info("timer2 :: " + s2Time);
					if (s2Time > READ_TIMEOUT) {
						isTimeoutReversal = true;
						break;
					}
					continue;
				}
				
				read = inSc.read(tempReadBuffer);
				
				if (read == 0) {
					continue;
				}
				if (read == -1) {
				    this.tcLogger.info("Time TR :: IOException <- read = -1");
					throw new Exception ("connection close");
				}
				this.tcLogger.info("timer :: " + s2Time);	

				temp = new byte[read];
				System.arraycopy(tempReadBuffer.array(), 0, temp, 0, read);
				if (loopCnt == 0) {
					responseByte = new byte[read];
					System.arraycopy(temp, 0, responseByte, 0, read);
				} else {
					ByteUtil.addArray(responseByte, temp);
				}
				
//				result = parseResponse(timeoutReversalCode, responseByte);
//				
//				//응답이 timeout reversal이면,
//				if (timeoutReversalCode.equals(result.get("TRCODE"))) {
//					this.fdLogger.info("receiveMsg byte[] :: " + new String(responseByte);
//					if (read < bufferSize) {
//						break;
//					}
//				} else {//응답이 timeout reserval이 아니면 
//					this.fdLogger.info("receiveMsg byte[] :: " + new String(responseByte));
//					read = 0;
//					continue;
//				}
				
				if (read < BUFFER_SIZE) {
					break;
				}
				loopCnt++;
			}
			
			if (! isTimeoutReversal) {
			    this.tcLogger.info("Time TR :: " + s2Time );
			    this.tcLogger.info("receiveMsg byte[] TR :: " + new String(responseByte,"euc-kr"));
//				result = parseResponse(timeoutReversalCode, responseByte);
				result = parseResponse(responseByte);
			} else {	/*전문 전달 후 응답을 받지 못한 경우 */
				this.tcLogger.info ("Time TR :: IOException");
				throw new IOException();
			}
			 
			selector.close();
			
		} catch (IOException e) {
			tcLogger.error("sendTimeoutReversalMessage IOException -> " + e.getMessage(), e);
		} catch (Exception e) {
			tcLogger.error("sendTimeoutReversalMessage Exception -> " + e.getMessage(), e);
		} 
		
		return result;
	}
	
//	private byte[] makeSendMessage(String textType, Map<String, byte[]> sendParamMap, String cardData, int amount,
	private byte[] makeSendMessage(String textType, String cardData, int amount,
			String orgApprovNo,
			String orgSaleDate,
			String termType,
			String yymmdd) {
		
		MessageProvider messageProvider = new MessageProvider(this.merchantNumber, this.telecommUtil);
		
		byte[] message   = new byte[0];
		
		message = messageProvider.setHeader(textType, cardData, message, termType);		
		message = messageProvider.setBody(textType, cardData, message, amount, orgApprovNo, orgSaleDate, termType, yymmdd);
		message = messageProvider.wrap(message);
		
		return message;
	}
	
	/**
	 * KT-Vanilla 전송 메시지 생성
	 * 
	 * @param textType
	 * @param sendParamMap
	 * @param cardData
	 * @param amount
	 * @param orgApprovNo
	 * @param orgSaleDate
	 * @param termType
	 * @param yymmdd
	 * @return
	 */
//	private byte[] makeSendMessageKtVanilla(String textType, Map<String, byte[]> sendParamMap, String cardData, int amount,
	private byte[] makeSendMessageKtVanilla(String textType, String cardData, int amount,
					
			String orgApprovNo,
			String orgSaleDate,
			String termType,
			String yymmdd) {
		
		MessageProvider messageProvider = new MessageProvider(this.merchantNumber, this.telecommUtil);
		
		byte[] message   = new byte[0];
		
		// KT-Vanilla 서버 통신시 카드번호 암호화. 20180810
		String encCardData = "";
		
		try {
			AES256Cipher a256 = AES256Cipher.getInstance();
			encCardData = a256.AES_Encode(cardData);
		} catch(Exception e) {
			tcLogger.error("sendTimeoutReversalMessage Exception -> " + e.getMessage(), e);
			//throw new TelecomException("TelecomException::cardData[" + cardData + "] encryption failed!");
		}
		
		message = messageProvider.setHeader(textType, encCardData, message, termType);		
		
		message = messageProvider.setBodyKtVanilla(textType, encCardData, message, amount, orgApprovNo, orgSaleDate, termType, yymmdd);
		
		message = messageProvider.wrapKtVanilla(message);
		
		return message;
	}
	
	/**
	 * FD 연동 메시지 구성 - 원 요청 취소
	 * 연동 코드는 0704이고, 원 요청 전문에 원 요청 연동 코드를 추가 하여 전송
	 * @param trCodeOri - 원 요청 연동 코드
	 * @param sendParamMapOri - 원 요청 연동 인자
	 * @return - 취소 연동 MSG
	 */
//	private byte[] makeTimeoutReversalMessage(String trCodeOri, Map<String, byte[]> sendParamMapOri) {
	private byte[] makeTimeoutReversalMessage(Map<String, byte[]> sendParamMapOri) {
		
		String timeoutReversalCode = "";
		
//		String timeoutReversalCode = FDCode.TRCODE.TIMEOUT_REVERSAL;
//		if("0110".equals(trCodeOri) || "0810".equals(trCodeOri)) {	// bulk는 0705 코드 사용
//			timeoutReversalCode = FDCode.TRCODE.TIMEOUT_REVERSAL_BULK;
//		}
		
		MessageProvider messageProvider = new MessageProvider(this.merchantNumber, this.telecommUtil);
		
		byte[] msg = null;
		byte[] msgPrefix = null;
		byte[] msgBody = null;
		byte[] msgSuffix = null;
		
		//F6필드에 원 전문 번호를 등록
//		sendParamMapOri.put(FDCode.FILED.ORIGINAL_TRANSACTION_REQUEST, trCodeOri.getBytes());
		
		msgBody   = messageProvider.getBody(timeoutReversalCode, sendParamMapOri);
		msgPrefix = messageProvider.getPrefix(msgBody.length);
		msgSuffix = messageProvider.getSuffix();
		
		//prefix + body
		msg = ByteUtil.addArray(msgPrefix, msgBody);
		
		//(prefix + body) + suffix
		msg = ByteUtil.addArray(msg, msgSuffix);
		
		return msg;
	}	
	
	private static final char cSTX = 2;
	private static final char cETX = 3;
	private static final char cFS  = 28;
	private static final char cSI  = 15;
	
//	private Map<String, String> parseResponse(String textType, byte[] response) {
	private Map<String, String> parseResponse(byte[] response) {
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
	
	/**
	 * VAN서버와의 연결을 종료한다. 
	 */
	private void disconnect(SocketChannel sc) throws IOException {
		if (sc != null && sc.isOpen()) {
			sc.close();
			tcLogger.info("sc close!!");
		}
	}
	
	public static void main(String[] args) {		
		/* cardData */
//		String termType    = "K";
//		String cardData    = "1111222233334441";
//		int amount         = 1000;
//		String orgApprovNo = "";
//		String orgSaleDate = "";
//		String pluCd       = "KT0002";
//		
//		Map<String, String> result1 = null;
//		Map<String, String> result2 = null;
//		Map<String, String> result3 = null;
////		
//		TelecommBroker telecomBroker = new TelecommBroker();
//		result1 = telecomBroker.inquireForPoint(termType, cardData);
//		System.out.println("response.resultCode="    + result1.get("RespCd"));
//		System.out.println("response.resultMessage=" + result1.get("ScreenExpr"));		//resultMessage
//		System.out.println("==================================================================================");
//		System.out.println("");
//		result2 = telecomBroker.inquireForDiscount(termType, cardData, amount, pluCd);
//		System.out.println("response.resultCode="    + result2.get("RespCd"));
//		System.out.println("response.resultMessage=" + result2.get("ScreenExpr"));
//		System.out.println("response.ApprovDT="      + result2.get("ApprovDT"));
//		System.out.println("response.ApprovNo="      + result2.get("ApprovNo"));
//		System.out.println("==================================================================================");
//		System.out.println("");
//		result3 = telecomBroker.inquireForDiscountCancel(termType, cardData, amount, orgApprovNo, orgSaleDate);
//		System.out.println("response.resultCode="    + result3.get("RespCd"));
//		System.out.println("response.resultMessage=" + result3.get("ScreenExpr"));
//		System.out.println("==================================================================================");
//		System.out.println("");
//		System.out.println("Completed!");
	}
}
