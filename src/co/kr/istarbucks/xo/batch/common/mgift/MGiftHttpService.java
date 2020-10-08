package co.kr.istarbucks.xo.batch.common.mgift;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.xwork.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.ibatis.sqlmap.client.SqlMapClient;

import co.kr.istarbucks.xo.batch.common.dto.xo.PaymentDto;
import co.kr.istarbucks.xo.batch.common.util.CryptoUtils;
import co.kr.istarbucks.xo.batch.mgr.PaymentCancelXoMgr;

public class MGiftHttpService {
	private final Logger pcLogger = Logger.getLogger ("PPC_INFO");
	private final Logger mLogger = Logger.getLogger("PC_MGIFT");
   
	
	private final PaymentCancelXoMgr paymentCancelXoMgr;
	
	
	public Logger getPcLogger() {
		return pcLogger;
	}
	
	public Logger getMLogger() {
		return mLogger;
	}

	public PaymentCancelXoMgr getPaymentCancelXoMgr() {
		return paymentCancelXoMgr;
	}

	public MGiftHttpService() {
		paymentCancelXoMgr = new PaymentCancelXoMgr();
	}



	
	/**
	 * 모바일상품권 정보 조회
	 * @param couponNo
	 * @return
	 * @throws Exception 
	 */
	public Map<String, String> giftCertificationInfo(String couponNo, String orderNo){
		StringBuffer buf = new StringBuffer ();
		buf.delete (0, buf.length ()).append ("------------------------------Gift Certificate Info Start------------------------------");
		this.mLogger.info (buf.toString ());
		Map<String, String> infoMap = new HashMap<String, String>();
		infoMap.put("couponNo", couponNo);
		infoMap.put("orderNo", orderNo);
		infoMap.put("transType", MGiftCode.SEND_INFO);
		boolean serverError = false;
		Map<String, String> resultMap = null;
		String prefix = couponNo.substring(0, 2);
		
		try {
			if(couponNo.length() != 12) {
				resultMap = new HashMap<String, String>();
				this.mLogger.info ("쿠폰 자리수 오류!");
				resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_VALID_ERROR);
				buf.delete (0, buf.length ()).append ("------------------------------Gift Certificate Info End------------------------------");
				this.mLogger.info (buf.toString ());
				return resultMap;
			} else if(!MGiftFunction.checkValidGfcrPrefix(prefix, MGiftCode.MOBILE_GIFT)) {
				resultMap = new HashMap<String, String>();
				this.mLogger.info ("유효하지 않은 상품권!!!! - 다른 타입의 상품권");
				buf.delete (0, buf.length ()).append ("------------------------------Gift Certificate Info End------------------------------");
				this.mLogger.info (buf.toString ());
				return resultMap;
			} else {
				this.mLogger.info("SendInfo Start");
				resultMap = setUrlPostSend(MGiftCode.SEND_INFO, "GIFT", infoMap);
			}
			
			
			//쿠폰 타입 체크
			//금액형 상품권은 유효하지 않은 상품권으로 오류 전달
			if(StringUtils.equals(MGiftCode.XO_MGIFT_SUCCESS, resultMap.get("RESULT_CODE"))) {
				String prcmType = MGiftFunction.getPrcmType(prefix);
				if(StringUtils.equals(prcmType, MGiftCode.MONEYCON_PRCM) && StringUtils.equals(resultMap.get("COUPON_TYPE"), "11")) {
					resultMap.clear();
					resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_VALID_ERROR);
					mLogger.error("잘못된 쿠폰 타입!!!!!!!!!!!!!");
				} else if(StringUtils.equals(prcmType, MGiftCode.COOP_PRCM) && StringUtils.equals(resultMap.get("COUPON_TYPE"), "01")) {
					resultMap.clear();
					resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_VALID_ERROR);
					mLogger.error("잘못된 쿠폰 타입!!!!!!!!!!!!!");
				}
			}
			
			if(MapUtils.isEmpty(resultMap)) {
				serverError = true;
			}
		}  catch (Exception e) {
			serverError = true;
			mLogger.info("[Exception][".replaceAll("\r\n", "")+"]["+ couponNo.replaceAll("\r\n", "")+"]["+ e.toString().replaceAll("\r\n", ""));
		} finally {
			if(serverError) {
				if(MapUtils.isEmpty(resultMap)) {
					resultMap = new HashMap<String, String>();
				}
				resultMap.clear();
				resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_SERVER_ERROR);
			}
		}
		this.mLogger.info("------------------------------Gift Certificate Info End------------------------------");
		return resultMap;
	}
	
	/**
	 * 모바일 상품권 사용
	 * @param couponNo
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("static-access")
	public Map<String, String> giftUse(String couponNo, String orderNo) {
		StringBuffer buf = new StringBuffer ();
		
		Map<String, String> infoMap = new HashMap<String, String>();
		
		infoMap.put("couponNo", couponNo);
		infoMap.put("orderNo", orderNo);
		infoMap.put("transType", MGiftCode.SEND_USE);
		
		boolean serverError = false;
		String skuCode = "";
		Map<String, String> resultMap = null;
		
		try {
		// 사용 전 쿠폰 상태 체크하기 위해 조회
			resultMap = giftCertificationInfo(couponNo, orderNo);
			mLogger.info("["+  couponNo.replaceAll("\r\n", "")+"]"+"["+  orderNo.replaceAll("\r\n", "")+"]"+ "giftUse".replaceAll("\r\n", "")+"]");
			String prefix = couponNo.substring(0, 2);
			
			//제휴사 연동(조회)가 정상이고 결과 코드가 00인 경우 쿠폰 상태 체크
			if(StringUtils.equals(MGiftCode.XO_MGIFT_SUCCESS, resultMap.get("RESULT_CODE"))) {
				skuCode = resultMap.get("SKU_CODE");
				
				String prcmType = MGiftFunction.getPrcmType(prefix);
				// 쿠폰 타입이 머니콘 '10', 기프티콘 '07' 카운트 상품으로 설정  
				if((StringUtils.equals(prcmType, MGiftCode.MONEYCON_PRCM) && StringUtils.equals(resultMap.get("COUPON_TYPE"), "10")) 
						|| (StringUtils.equals(prcmType, MGiftCode.GIFTICON_PRCM) && StringUtils.equals(resultMap.get("COUPON_TYPE"), "07"))) {
						infoMap.put("cntGiftYn", "Y");
				} else {
					infoMap.put("cntGiftYn", "N");
				}
				// 사용금액 등 사용 처리에 필요한 데이터 셋팅
				infoMap.put("productPrice", resultMap.get("PRICE"));
				infoMap.put("skuCode", resultMap.get("SKU_CODE"));
				
				resultMap = setUrlPostSend(MGiftCode.SEND_USE, "GIFT", infoMap);

			}  else if(StringUtils.equals(MGiftCode.XO_MGIFT_USED_ERROR, resultMap.get("RESULT_CODE"))) {
				mLogger.error("사용이 완료된 쿠폰 에러!!!!!");
			} else if(StringUtils.equals(MGiftCode.XO_MGIFT_VALID_ERROR, resultMap.get("RESULT_CODE"))) {
				mLogger.error("유효하지 않은 쿠폰 에러!!!!!");
			} else {
				mLogger.error("서버 기타 에러!!!!");
			}
			
			//SKU_CODE가 비어 있는 경우 조회한 SKU_CODE 값 설정
			if(StringUtils.isEmpty(resultMap.get("SKU_CODE"))) {
				resultMap.put("SKU_CODE", skuCode);
			}
			
		} catch (Exception e) {
			serverError = true;
			this.mLogger.info("[Exception][".replaceAll("\r\n", "")+"]["+ couponNo.replaceAll("\r\n", "")+"]["+ e.toString().replaceAll("\r\n", "")+e);		} finally {
			if(serverError) {
				if(MapUtils.isEmpty(resultMap)) {
					resultMap = new HashMap<String, String>();
				}
				resultMap.clear();
				resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_SERVER_ERROR);
			}
		}
		
		this.mLogger.info (buf.toString ().replaceAll("\r\n", ""));
		return resultMap;
	}
		/**
	 * 모바일 상품권 사용 취소
	 * @param couponNo
	 * @param approvNo
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("static-access")
	public Map<String, String> giftUseCancel(String couponNo, String approvNo, String orderNo) {
		StringBuffer buf = new StringBuffer ();
		Map<String, String> infoMap = new HashMap<String, String>();
		infoMap.put("couponNo", couponNo);
		infoMap.put("orderNo", orderNo);
		infoMap.put("approvNo", approvNo);
		infoMap.put("transType", MGiftCode.SEND_CANCEL);
		boolean serverError = false;
		Map<String, String> resultMap = null;
		
		try {
			if(couponNo.length() != 12) {
				resultMap = new HashMap<String, String>();
				resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_VALID_ERROR);
				mLogger.info("쿠폰 자리수 에러");
				return resultMap;
			}
						
			mLogger.info("["+  couponNo.replaceAll("\r\n", "")+"]["+ "giftUseCancel".replaceAll("\r\n", "")+"]");
			resultMap = setUrlPostSend(MGiftCode.SEND_CANCEL, "GIFT", infoMap);
			if(MapUtils.isEmpty(resultMap)) {
				serverError = true;
			}
		} catch (Exception e) {
			serverError = true;
			mLogger.info("[Exception][".replaceAll("\r\n", "")+"]["+  couponNo.replaceAll("\r\n", "")+"]["+ e.toString().replaceAll("\r\n", ""));
		} finally {
			if(serverError) {
				if(MapUtils.isEmpty(resultMap)) {
					resultMap = new HashMap<String, String>();
				}
				resultMap.clear();
				resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_SERVER_ERROR);
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 금액형 상품권 정보 조회
	 * @param couponNo
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("static-access")
	public Map<String, String> cardCertificateInfo(String couponNo) {
		mLogger.info("------------------------------Card Certificate Info Start------------------------------");
		Map<String, String> infoMap = new HashMap<String, String>();
		
		infoMap.put("couponNo", couponNo);
		infoMap.put("transType", MGiftCode.SEND_INFO);
		boolean serverError = false;
		String prefix = couponNo.substring(0, 2);
		
		Map<String, String> resultMap = null;
		try {
			if(!(couponNo.length() == 12)) {
				resultMap = new HashMap<String, String>();
				resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_VALID_ERROR);
				mLogger.info("쿠폰 자리수 에러!!!!");
				mLogger.info("------------------------------Card Certificate Info End------------------------------");
				return resultMap;
			} else if(!MGiftFunction.checkValidGfcrPrefix(prefix, MGiftCode.CARD_EXCHANGE)) {
				resultMap = new HashMap<String, String>();
				resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_VALID_ERROR);
				mLogger.info("유효하지 않은 상품권!!!! - 다른 타입의 상품권");
				mLogger.info("------------------------------Card Certificate Info End------------------------------");
				return resultMap;
			} else {
				resultMap = setUrlPostSend(MGiftCode.SEND_INFO, "CARD",infoMap);
			}
			mLogger.info("["+  couponNo.replaceAll("\r\n", "")+"]["+ "cardCertificateInfo".replaceAll("\r\n", "")+"]");
			
			if(MapUtils.isEmpty(resultMap)) {
				serverError = true;
			}
		} catch (Exception e) {
			serverError = true;
			mLogger.info("[Exception][".replaceAll("\r\n", "")+"]["+  couponNo.replaceAll("\r\n", "")+"]["+ e.toString().replaceAll("\r\n", ""));
		} finally {
			if(serverError) {
				if(MapUtils.isEmpty(resultMap)) {
					resultMap = new HashMap<String, String>();
				}
				resultMap.clear();
				resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_SERVER_ERROR);
			}
		}
		mLogger.info("------------------------------Card Certificate Info End------------------------------");
		return resultMap;
	}
	
	/**
	 * 금액형 상품권 사용
	 * @param couponNo
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("static-access")
	public Map<String, String> cardUse(String couponNo) {
		mLogger.info("------------------------------Card Use Start------------------------------");
		Map<String, String> infoMap = new HashMap<String, String>();
		infoMap.put("couponNo", couponNo);
		infoMap.put("transType", MGiftCode.SEND_USE);
		boolean serverError = false;
		Map<String, String> resultMap = null;
		try {
			
			resultMap = cardCertificateInfo(couponNo);
			mLogger.info("["+ couponNo.replaceAll("\r\n", "")+"]["+ "cardUse".replaceAll("\r\n", ""));
			
			if(StringUtils.equals(resultMap.get("RESULT_CODE"), MGiftCode.XO_MGIFT_SUCCESS)) {
				infoMap.put("productPrice", resultMap.get("PRICE"));
				String prefix = couponNo.substring(0, 2);
				String prcmType = MGiftFunction.getPrcmType(prefix);
				if(StringUtils.equals(prcmType, MGiftCode.MONEYCON_PRCM)) {
					infoMap.put("prcGiftYn", "Y");
				} 
				resultMap = setUrlPostSend(MGiftCode.SEND_USE, "CARD", infoMap);
			} else if(StringUtils.equals(MGiftCode.XO_MGIFT_USED_ERROR, resultMap.get("RESULT_CODE"))) {
				mLogger.error("사용이 완료된 쿠폰 에러!!!!!");
			} else if(StringUtils.equals(MGiftCode.XO_MGIFT_VALID_ERROR, resultMap.get("RESULT_CODE"))) {
				mLogger.error("유효하지 않은 쿠폰 에러!!!!!");
			} else {
				mLogger.error("서버 기타 에러!!!!");
			}
			
			if(MapUtils.isEmpty(resultMap)) {
				serverError = true;
			}
		} catch (Exception e) {
			serverError = true;
			mLogger.info("[Exception][".replaceAll("\r\n", "")+"]["+ couponNo.replaceAll("\r\n", "")+"]["+ e.toString().replaceAll("\r\n", ""));
		} finally {
			if(serverError) {
				if(MapUtils.isEmpty(resultMap)) {
					resultMap = new HashMap<String, String>();
				}
				resultMap.clear();
				resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_SERVER_ERROR);
			}
		}
		
		mLogger.info("------------------------------Card Use End------------------------------");
		return resultMap;
	}
	
	/**
	 * 금액형 상품권 사용 취소
	 * @param couponNo
	 * @param approvNo
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("static-access")
	public Map<String, String> cardUseCancel(String couponNo, String approvNo) {
		mLogger.info("------------------------------Card Use Cancel Start------------------------------");
		Map<String, String> infoMap = new HashMap<String, String>();
		infoMap.put("couponNo", couponNo);
		infoMap.put("approvNo", approvNo);
		infoMap.put("transType", MGiftCode.SEND_CANCEL);
		
		boolean serverError = false;
		Map<String, String> resultMap = null;
		try {
			if(couponNo.length() != 12) {
				resultMap = new HashMap<String, String>();
				resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_VALID_ERROR);
				mLogger.info("------------------------------Card Use Cancel End------------------------------");
				return resultMap;
			} else {
				resultMap = setUrlPostSend(MGiftCode.SEND_CANCEL, "CARD", infoMap);
			}
			
			if(MapUtils.isEmpty(resultMap)) {
				serverError = true;
			}
		} catch (Exception e) {
			serverError = true;
			mLogger.info("[Exception][".replaceAll("\r\n", "")+"]["+ couponNo.replaceAll("\r\n", "")+"]["+ e.toString().replaceAll("\r\n", ""));
		} finally {
			if(serverError) {
				if(MapUtils.isEmpty(resultMap)) {
					resultMap = new HashMap<String, String>();
				}
				resultMap.clear();
				resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_SERVER_ERROR);
			}
		}
		
		mLogger.info("------------------------------Card Use Cancel End------------------------------");
		return resultMap;
	}
	
	/**
	 * 모바일 상품권 망거래 취소
	 * @param couponNo
	 * @param approvNo
	 * @return
	 */
	public Map<String, String> tradeCancel(Map<String, String> infoMap) {
		mLogger.info("------------------------------Trade Cancel Start------------------------------");
		Map<String, String> resultMap = null;
		String couponNo = infoMap.get("couponNo");
		String orderNo = infoMap.get("orderNo");
		String cntGiftYn = "";
		String productPrice = "";
		if(StringUtils.isNotEmpty(infoMap.get("cntGiftYn"))) {
			cntGiftYn = infoMap.get("cntGiftYn");
		}
		if(StringUtils.isNotEmpty(infoMap.get("productPrice"))){
			productPrice = infoMap.get("productPrice");
		}
		infoMap.clear();
		
		infoMap.put("orderNo", orderNo);
		infoMap.put("couponNo", couponNo);
		infoMap.put("transType", MGiftCode.SEND_TRADE_CANCEL);
		infoMap.put("cntGiftYn", cntGiftYn);
		infoMap.put("productPrice", productPrice);
		
		try {
			resultMap = setUrlPostSend(MGiftCode.SEND_TRADE_CANCEL, null, infoMap);
		} catch (Exception e) {
			mLogger.info("[Exception][".replaceAll("\r\n", "")+"]["+  infoMap.get("couponNo").replaceAll("\r\n", "")+"]["+ e.toString().replaceAll("\r\n", ""));
		} 
		
		mLogger.info("------------------------------Trade Cancel End------------------------------");
		return resultMap;
	}
	
	/**
	 * Post Send
	 * @param couponNo
	 * @param type - 조회, 사용, 취소
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> setUrlPostSend(String type, String giftType, Map<String, String> infoMap) throws Exception{
		//1. DB 조회 (제휴사 마스터)
		//2. sendData 생성
		//3. send
		//4. receive
		//5. prefix에 맞춰 데이터 파싱
		//6. 파싱 후 호출한 곳으로 resultMap<String, String> return
		StringBuffer buf = new StringBuffer ();
		String couponNo = infoMap.get("couponNo");
		String orderNo = infoMap.get("orderNo");
		Map<String, String> resultMap = null;
		String prefix = couponNo.substring(0, 2);
		Map<String, String> dbMap = new HashMap<String, String>();
		dbMap.put("prefix", prefix);
		
		if(StringUtils.equals(giftType, "GIFT")) {
			dbMap.put("giftYn", "Y");
		} else if(StringUtils.equals(giftType, "CARD")) {
			dbMap.put("cardYn", "Y");
		}
		
		Map<String, String> dbResult = paymentCancelXoMgr.getMblGfcrPrcmInfo(dbMap);
		
		if(dbResult == null) {
			mLogger.error("제휴사 연동 정보가 존재하지 않습니다. ");
			resultMap = new HashMap<String, String>();
			resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_VALID_ERROR);
			return resultMap;
		}
		
		URL url = null;
		HttpURLConnection conn = null;
		OutputStream os = null;
		PrintWriter out = null;
		String urlEncodingValue = null;
		String strUrl = dbResult.get("INTRL_URL");
		String prcmType = MGiftFunction.getPrcmType(prefix);
		if(StringUtils.equals(MGiftCode.GIFTICON_PRCM, prcmType)) {
			strUrl = makeSendUrl(strUrl, type);
		}
		Hashtable<String, String> data = null;
		
		try {
			url = new URL(strUrl);
			data = makeSendData(dbResult, couponNo, type, infoMap);
			if(MapUtils.isEmpty(data)) {
				resultMap = new HashMap<String, String>();
				resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_ETC_ERROR);
				return resultMap;
			}
			
			if(data != null) {
				conn = (HttpURLConnection)url.openConnection();
				conn.setConnectTimeout(Integer.parseInt(dbResult.get("CNCTN_TMT_TME")));
				conn.setReadTimeout(Integer.parseInt(dbResult.get("RSPNS_TMT_TME")));
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				conn.setDoInput(true);
				
				if(StringUtils.equals(MGiftCode.GIFTISHOW_PRCM, prcmType)) {
					String lineEnd = "\r\n";
					String twoHyphens = "--";
					String boundary = "STARBUCKSHTTP";
					DataOutputStream dos = null ;
					
					conn.setRequestProperty("api_code", "0459");
					conn.setRequestProperty("custom_auth_code", dbResult.get("USR_ATHN_CODE"));
					conn.setRequestProperty("custom_auth_token", CryptoUtils.encryptAES256ECB(dbResult.get("CRPTG_KEY"), dbResult.get("USR_ATHN_CODE"), dbResult.get("CHST_NAME"),false));
					conn.setRequestProperty("custom_enc_flag", "Y");
					conn.setRequestProperty("Content-Type", "multipart/form-data;charset=utf-8;boundary=" + boundary);
					
					Enumeration<String> e = data.keys();
					String key = null;
					String value = null;
					StringBuilder logValue = new StringBuilder();		//Log용
					
					try {
						dos = new DataOutputStream(conn.getOutputStream());
						String eq = "=";		//pmd로 인한 변수
						String an = "&";		//pmd로 인한 변수
						while(e.hasMoreElements()) {
							key = (String) e.nextElement();
							value = (String) data.get(key);
							dos.writeBytes(twoHyphens + boundary + lineEnd);
							dos.writeBytes("Content-Disposition:form-data;name=\"" + key + "\"" + lineEnd);
							dos.writeBytes(lineEnd);
							dos.writeBytes(value + lineEnd);
							logValue.append(key).append(eq).append(value).append(an); 
						}
						dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
						buf.delete (0, buf.length ()).append ("[").append(orderNo).append("]").append ("[").append(couponNo).append("]").append(strUrl).append("/").append(logValue);
						this.mLogger.info (buf.toString ().replaceAll("\r\n", ""));
						
					} catch(Exception ex) {
						buf.delete (0, buf.length ()).append ("setUrlPostSend(GiftiShow) Fail   ------------> ").append(e);
			            this.mLogger.info (buf.toString ().replaceAll("\r\n", ""));
					}
					//findbugs 수정
					if(dos != null) {
						dos.close();
					}
				} else if (StringUtils.equals(MGiftCode.MONEYCON_PRCM, prcmType) || StringUtils.equals(MGiftCode.COOP_PRCM, prcmType)) {
					os = conn.getOutputStream();
					out = new PrintWriter(os);
					urlEncodingValue = urlEncoding(data);
					//Log
					mLogger.info("["+orderNo.replaceAll("\r\n", "")+"]["+ couponNo.replaceAll("\r\n", "")+"]["+ urlEncodingValue.replaceAll("\r\n", ""));
					out.print(urlEncodingValue);
					out.flush();
				} else if(StringUtils.equals(MGiftCode.GIFTICON_PRCM, prcmType)) {
					JsonObject json = new JsonObject();
					Enumeration<String> e = data.keys();
					String key = null;
					String value = null;

					while(e.hasMoreElements()) {
						key = (String)e.nextElement();
						value = (String)data.get(key);
						if(value.length() >= 2) {
							value = java.net.URLEncoder.encode(value, "utf-8");
						}
						json.addProperty(key, value);
					}
					
					os = conn.getOutputStream();
					out = new PrintWriter(os);
					//Log
					buf.delete (0, buf.length ()).append("[").append(orderNo).append("]").append("[").append(couponNo).append("] -").append(json);
					this.mLogger.info (buf.toString ().replaceAll("\r\n", ""));
					out.print(json);
					out.flush();
				} else {
					os = conn.getOutputStream();
					out = new PrintWriter(os);
					urlEncodingValue = urlEncoding(data);
					out.print(urlEncodingValue);
					out.flush();
					buf.delete (0, buf.length ()).append ("[").append(orderNo).append("]").append ("[").append(couponNo).append("]").append(urlEncodingValue);
					this.mLogger.info (buf.toString ().replaceAll("\r\n", ""));
				}
			} 

			resultMap = getUrlPostReceive(conn, prefix, dbResult.get("CHST_NAME"), type, infoMap);
			if(MapUtils.isNotEmpty(resultMap)) {
				resultMap.put("PAY_METHOD_NAME", dbResult.get("PRCM_NAME"));
				resultMap.put("PAY_METHOD", dbResult.get("SROR_STLMN_DVSN_CODE"));
			}
		} catch(Exception e) {
			buf.delete (0, buf.length ()).append ("setUrlPostSend Fail   ------------> ").append(e);
            this.mLogger.info (buf.toString ().replaceAll("\r\n", ""));
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch(Exception ex) {
					buf.delete (0, buf.length ()).append ("PrintWriter Close Fail   ------------> ").append(ex);
		            this.mLogger.info (buf.toString ().replaceAll("\r\n", ""));
				}
			}
			if(os!=null) {
				try {
					os.close();
				} catch(Exception ex) {
					buf.delete (0, buf.length ()).append ("OutputStream Close Fail   ------------> ").append(ex);
		            this.mLogger.info (buf.toString ().replaceAll("\r\n", ""));
				}
			}
		}
		return resultMap;
	}
	
	/**
	 * PMD 높은 복잡도로 인한 제휴사 URL 생성 메소드 분리
	 * @param type
	 * @return
	 */
	public String makeSendUrl(String strUrl, String type){
		String subUrl = "";
		String returnUrl = "";
		if(StringUtils.equals(MGiftCode.SEND_INFO, type)) {
			subUrl = "authCheckCoupon";
		} else if(StringUtils.equals(MGiftCode.SEND_USE, type)) {
			subUrl = "authAprvCoupon";
		} else if(StringUtils.equals(MGiftCode.SEND_CANCEL, type)) {
			subUrl = "authAprvCnclCoupon";
		} else if(StringUtils.equals(MGiftCode.SEND_TRADE_CANCEL, type)) {
			subUrl = "authForceCnclCoupon";
		}
		returnUrl = strUrl + "/" + subUrl;
		return returnUrl;
	}
	
	
	/**
	 * URL Encoding
	 * @param hash
	 * @return
	 * @throws Exception
	 */
	private String urlEncoding(Hashtable<String, String> hash) throws Exception{
		boolean isFirst = true;
		String key = null;
		String value = null;
		StringBuffer buf = new StringBuffer();
		if(hash == null) {
			return null;
		}
		Enumeration<String> e = hash.keys();
		while(e.hasMoreElements()) {
			if(isFirst) {
				isFirst = false;
			} else {
				buf.append('&');
			}
			key = (String)e.nextElement();
			value = (String)hash.get(key);
			buf.append(key);
			buf.append('=');
			buf.append(java.net.URLEncoder.encode(value,"utf-8"));
		}
		return buf.toString();
	}
	/**
	 * MultiPart
	 * @param conn
	 * @param hash
	 * @return
	 */
	public DataOutputStream httpPostMultipart(HttpURLConnection conn, Hashtable<String, String> hash, Map<String, String> infoMap){ 
		StringBuffer buf = new StringBuffer ();
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "STARBUCKSHTTP";
		DataOutputStream dos = null ;
		String key = null;
		String value = null;
		if(hash == null) {
			return null;
		}
		Enumeration<String> e = hash.keys();
		try {
			dos = new DataOutputStream(conn.getOutputStream());
			while(e.hasMoreElements()) {
				key = (String)e.nextElement();
				value = (String)hash.get(key);
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition:form-data;name=\"" + key + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(value+lineEnd);
			}
			
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			
		} catch(Exception ex) {
			mLogger.error("[Exception-httpPostMultipart][".replaceAll("\r\n", "\t")+"]["+ infoMap.get("couponNo").replaceAll("\r\n", "\t")+"]["+ ex.toString().replaceAll("\r\n", "\t"));
			buf.delete (0, buf.length ()).append ("httpPostMultipart Fail   ------------> ").append(e);
            this.mLogger.info (buf.toString ().replaceAll("\r\n", ""));
		}
		return dos;

	}
	
	/**
	 * Data Receive
	 * @param conn
	 * @param prefix
	 * @param charSet
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getUrlPostReceive(HttpURLConnection conn, String prefix, String charSet, String type, Map<String, String> infoMap) throws Exception{
		 StringBuffer buf = new StringBuffer ();
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		String temp = "";
		StringBuilder sb = new StringBuilder();
		String result = "";
		boolean exceptionYn = false;
		
		try {
			is = conn.getInputStream();
			isr = new InputStreamReader(is, charSet);
			br = new BufferedReader(isr);
			
			//PMD 조치
			while(true){
				temp = br.readLine();
				if(StringUtils.isEmpty(temp)) {
					break;
				}
				sb.append(temp);
			}
			result = sb.toString();
			
			mLogger.info("["+ infoMap.get("couponNo").replaceAll("\r\n","")+"]["+infoMap.get("orderNo").replaceAll("\r\n","")+"]["+result.replaceAll("\r\n","")+"]");
			
			
			buf.delete(0, buf.length()).append(result);
			this.mLogger.info (buf.toString ().replaceAll("\r\n", ""));
		} catch(SocketTimeoutException ste) {
			exceptionYn = true;
			mLogger.info("[SocketTimeoutException][".replaceAll("\r\n","")+"]["+ infoMap.get("couponNo").replaceAll("\r\n", "")+"]["+ ste.toString().replaceAll("\r\n", ""));
			if(StringUtils.equals(ste.toString(), "java.net.SocketTimeoutException: Read timed out")
					&&(StringUtils.equals(infoMap.get("transType"), MGiftCode.SEND_USE) || StringUtils.equals(infoMap.get("transType"), MGiftCode.SEND_CANCEL))
					&&MGiftFunction.checkTradeCancel(prefix)) {
				//사용 또는 취소 처리 중 ReadTimedOut이 발생하면 망거래 취소 요청
				tradeCancel(infoMap);
			}
		} catch(Exception e) {
			buf.delete (0, buf.length ()).append ("getUrlPostReceive Fail   ------------> ").append(e);
            this.mLogger.info (buf.toString ().replaceAll("\r\n", ""));
            
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch(IOException ioEx) {
					buf.delete (0, buf.length ()).append ("BufferedReader Close Fail   ------------> ").append(ioEx);
		            this.mLogger.info (buf.toString ().replaceAll("\r\n", ""));
				}
			}
			if(isr != null) {
				try {
					isr.close();
				} catch(IOException ioEx) {
					buf.delete (0, buf.length ()).append ("InputStreamReader Close Fail   ------------> ").append(ioEx);
		            this.mLogger.info (buf.toString ().replaceAll("\r\n", ""));
				}
			}
			if(is != null) {
				try {
					is.close();
				} catch(IOException ioEx) {
					buf.delete (0, buf.length ()).append ("InputStream Close Fail   ------------> ").append(ioEx);
		            this.mLogger.info (buf.toString ().replaceAll("\r\n", ""));
				}
			}
			if(conn!= null) {
				try {
					conn.disconnect();
				} catch(Exception ex) {
					buf.delete (0, buf.length ()).append ("Connection DisConnect Fail   ------------> ").append(ex);
		            this.mLogger.info (buf.toString ().replaceAll("\r\n", ""));
				}
			}
		}
		
		Map<String, String> resultMap = null;
		if(exceptionYn) {
			resultMap = new HashMap<String, String>();
			resultMap.put("RESULT_CODE", MGiftCode.XO_MGIFT_SERVER_ERROR);
		} else {
			String prcmType = MGiftFunction.getPrcmType(prefix);
			MGiftHttpParser mGiftHttpParser = new MGiftHttpParser();
			if(StringUtils.equals(MGiftCode.MONEYCON_PRCM, prcmType)) {
				resultMap = mGiftHttpParser.getResultMapMoneyCon(result, infoMap.get("couponNo"));
			} else if(StringUtils.equals(MGiftCode.GIFTISHOW_PRCM, prcmType)) {
				resultMap = mGiftHttpParser.getResultMapGiftiShow(result, type, infoMap.get("couponNo"));
			} else if(StringUtils.equals(MGiftCode.COOP_PRCM, prcmType)){
				resultMap = mGiftHttpParser.getResultMapCoup(result, infoMap.get("couponNo"));
			} else if(StringUtils.equals(MGiftCode.GIFTICON_PRCM, prcmType)) {
				resultMap = mGiftHttpParser.getResultMapGiftiCon(result, type, infoMap.get("couponNo"));
			} 
		}
		
		return resultMap;
	}
	
	/**
	 * Send Data Create
	 * @param baseInfo
	 * @param couponNo
	 * @param type
	 * @return
	 * @throws Exception 
	 */
	public Hashtable<String, String> makeSendData(Map<String, String> baseInfo,String couponNo, String type, Map<String, String> infoMap) throws Exception{
		 StringBuffer buf = new StringBuffer ();
		Hashtable<String, String> returnData = new Hashtable<String, String>();
		String prefix = "";
		String comCode = "";
		String authCode = "";
		String branchCode = "";
		String branchName = "";
		String posNo = "";
		String crptgKey = "";
		String charSet = "";
		String iv = "";
		
		//prefix 추출
		for(String key : baseInfo.keySet()) {
			if(StringUtils.equals("PRCM_FRST_CODE",key)) {
				prefix = baseInfo.get(key);
			} else if(StringUtils.equals("XCHNG_CMPY_CODE",key)) {
				comCode = baseInfo.get(key);
			} else if(StringUtils.equals("USR_ATHN_CODE",key)) {
				authCode = baseInfo.get(key);
			} else if(StringUtils.equals("MBL_GFCR_STORE_CODE",key)) {
				branchCode = baseInfo.get(key);
			} else if(StringUtils.equals("MBL_GFCR_STORE_NAME",key)) {
				branchName = baseInfo.get(key);
			} else if(StringUtils.equals("MBL_GFCR_POS_NO",key)) {
				posNo = baseInfo.get(key);
			} else if(StringUtils.equals("CRPTG_KEY",key)) {
				crptgKey = baseInfo.get(key);
			} else if(StringUtils.equals("CHST_NAME",key)) {
				charSet = baseInfo.get(key);
			} else if(StringUtils.equals("INTVC_KEY",key)) {
				iv = baseInfo.get(key);
			}
		}
		String prcmType = MGiftFunction.getPrcmType(prefix);
		//데이터 생성
		if(StringUtils.equals(MGiftCode.MONEYCON_PRCM, prcmType)) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
			SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss", Locale.KOREA);
			
			returnData.put("ExchangeId", comCode);
			if(StringUtils.equals(MGiftCode.SEND_INFO, type)) {
				returnData.put("MsgSubCode", "100");
			} else if(StringUtils.equals(MGiftCode.SEND_USE, type)) {
				returnData.put("MsgSubCode", "101");
				returnData.put("Payment",infoMap.get("productPrice"));
				if(StringUtils.equals(infoMap.get("cntGiftYn"),"Y")) {
					returnData.put("Use", "1");
				} else {
					if(StringUtils.equals(infoMap.get("prcGiftYn"), "Y")) {
						returnData.put("Use", infoMap.get("productPrice"));
					} else {
						returnData.put("Use", "0");
					}
				}
			} else {
				returnData.put("MsgSubCode", "102");
				returnData.put("AdmitNum", infoMap.get("approvNo"));
			}
			
			try {
				returnData.put("CouponNum", CryptoUtils.encryptAES256CBC(crptgKey, iv, couponNo, charSet, false));
			} catch (Exception e) {
				buf.delete (0, buf.length ()).append ("[Exception-makeSendData]["+ couponNo +"] ------------> ").append(e);
	            this.mLogger.info (buf.toString ().replaceAll("\r\n", ""));
	            throw new Exception("");
			}
			
			returnData.put("BranchCode", branchCode);
			returnData.put("BranchName", branchName);
			returnData.put("PosCode", "560");
			returnData.put("PosDate", sdf.format(date));
			returnData.put("PosTime", sdf2.format(date));
		} else if(StringUtils.equals(MGiftCode.COOP_PRCM, prcmType)) {
			returnData.put("PASS", authCode);
			returnData.put("COUPON_TYPE", "");
			returnData.put("COUPON_NUMBER", CryptoUtils.encryptAES256CBC(crptgKey, iv, couponNo, charSet, false));
			returnData.put("BRANCH_CODE", branchCode);
			returnData.put("USE_PRICE", "");
			returnData.put("CancelAuthCode", "");
			returnData.put("POSCode", posNo);
			returnData.put("COMP_CODE", comCode);
			
			// 주문번호가 없는 경우 거래 추적용 키 생성
			// 카드교환권이 없으므로 주석 처리 - 모바일상품권 거래에서는 orderNo가 없을수 없음
//			if(StringUtils.isEmpty(infoMap.get("orderNo"))) {
//				infoMap.put("orderNo", MGiftFunction.getTraceKey(infoMap.get("userId")));
//			}
			
			if(StringUtils.equals(MGiftCode.SEND_INFO, type)) {
				returnData.put("COMMAND", "L0");
				returnData.put("L3_SEQNUMBER", "");
			} else if(StringUtils.equals(MGiftCode.SEND_USE, type)) {
				returnData.put("COMMAND", "L1");
				returnData.put("L3_SEQNUMBER", infoMap.get("orderNo"));
			} else if(StringUtils.equals(MGiftCode.SEND_CANCEL, type)) {
				returnData.put("COMMAND", "L2");
				returnData.put("L3_SEQNUMBER", infoMap.get("orderNo"));
				returnData.put("CancelAuthCode", infoMap.get("approvNo"));
			} else {
				returnData.put("COMMAND", "L3");
				returnData.put("L3_SEQNUMBER", infoMap.get("orderNo"));
			}
		} else if(StringUtils.equals(MGiftCode.GIFTISHOW_PRCM, prcmType)) {
			returnData.put("pos_ver_type", "0004");
				returnData.put("pos_ver_type", "0004");
			if(StringUtils.equals(MGiftCode.SEND_INFO, type)) {
				returnData.put("pos_fun_type", "01");
			} else if(StringUtils.equals(MGiftCode.SEND_USE, type)) {
				returnData.put("pos_fun_type", "02");
			} else {
				returnData.put("pos_fun_type", "03");
				returnData.put("approvement_no", infoMap.get("approvNo"));
			} 
			buf.delete(0,buf.length()).append(infoMap.get("approvNo"));
			this.mLogger.info (buf.toString ().replaceAll("\r\n", ""));
			returnData.put("exchplc_cd", comCode);
			returnData.put("receiver_chk_type", "2");
			try {
				returnData.put("pin_no", CryptoUtils.encryptAES256ECB(crptgKey, couponNo, charSet, false));
			} catch (Exception e) {
				mLogger.info("[Exception-makeSendData][".replaceAll("\r\n", "")+"]["+couponNo.replaceAll("\r\n", "")+"]["+ e.toString().replaceAll("\r\n", ""));
				buf.delete (0, buf.length ()).append ("[Exception-makeSendData]["+ couponNo +"] ------------> ").append(e);
	            this.mLogger.info (buf.toString ().replaceAll("\r\n", ""));
	            throw  e;
			}
			returnData.put("num_input_type", "1");
			} else if(StringUtils.equals(MGiftCode.GIFTICON_PRCM, prcmType)) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
			SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss", Locale.KOREA);
			
			returnData.put("TRANS_DATE", sdf.format(date));
			returnData.put("TRANS_TIME", sdf2.format(date));
			returnData.put("CPCO_ID", comCode);
			returnData.put("FRANCHISE_ID",branchCode);
			returnData.put("FRANCHISE_NAME", branchName);
			returnData.put("POS_ID", posNo);
			returnData.put("POS_REQUEST_DATE", sdf.format(date));
			returnData.put("POS_REQUEST_TIME", sdf2.format(date));
			returnData.put("COUPON_NUMBER", CryptoUtils.encryptAES256ECB(crptgKey, couponNo, charSet, false));	
			
			returnData.put("SECURE_MODE", "1");
			returnData.put("BARCODE_SCAN", "1");
			if(StringUtils.equals(MGiftCode.SEND_USE, type)) {
				returnData.put("TRACE_NUMBER", infoMap.get("orderNo").substring(10, infoMap.get("orderNo").length()));	
				if(StringUtils.equals(infoMap.get("cntGiftYn"),"Y")) {
					returnData.put("DEAL_COST", "1");	
				} 
			} else if(StringUtils.equals(MGiftCode.SEND_CANCEL, type)) {
				returnData.put("TRACE_NUMBER", infoMap.get("orderNo").substring(10, infoMap.get("orderNo").length()));	
				returnData.put("APPROVAL_NUMBER", infoMap.get("approvNo"));	
			} else if(StringUtils.equals(MGiftCode.SEND_TRADE_CANCEL, type)) {
				returnData.put("TRACE_NUMBER", infoMap.get("orderNo").substring(10, infoMap.get("orderNo").length()));
				if(StringUtils.equals(infoMap.get("cntGiftYn"), "Y")) {
					returnData.put("DEAL_COST", "1");
				} else {
					returnData.put("DEAL_COST", infoMap.get("productPrice"));
				}
			}
		} else {
			returnData = null;
		}
		return returnData;
	}
	

	 /**
     * 제휴사 취소 처리(모바일 상품권)
     */
    public boolean setMGiftOrderCancelProcess(List<PaymentDto> paymentList, String logTitle, StringBuffer logBuf){
    	 StringBuffer buf = new StringBuffer ();
    	int mGiftCount = 0;
    	Map<String, String> resultMap=null;
    	
    	if(paymentList != null && paymentList.size() > 0){
    		for(PaymentDto payDto : paymentList){
    			if(StringUtils.isNotEmpty(payDto.getPrcm_frst_code())){
    				mGiftCount++;
    				try{
    					buf.delete (0, buf.length ()).append ("----------------------------Gift Use Cancel Start----------------------------");
    					this.mLogger.info (buf.toString ());
    					resultMap = this.giftUseCancel(payDto.getMbl_gfcr_no(), payDto.getMbl_gfcr_use_cnsnt_no(), payDto.getOrder_no());
    					if(StringUtils.equals(MGiftCode.XO_MGIFT_SUCCESS,resultMap.get("RESULT_CODE"))) {
    						payDto.setCancel_result_code("00");
    						payDto.setMbl_gfcr_cnclt_cnsnt_no(resultMap.get("APPROV_NO").replace("\"", ""));
    						payDto.setResult_msg("결제 취소 성공");
    						payDto.setStatus("C");
    						mLogger.info("MGift cancel Success! ResultCode : "+ resultMap.get("RESULT_CODE").replace("\r\n", "")+"/ approvNo :"+payDto.getMbl_gfcr_use_cnsnt_no().replace("\r\n", ""));
						} else {
    						//Log 추가
    						mLogger.error("MGift cancel Error! ResultCode : "+ resultMap.get("RESULT_CODE").replace("\r\n", ""));
    						pcLogger.info("MGift cancel Error! ResultCode : "+ resultMap.get("RESULT_CODE").replace("\r\n", ""));
    						
    						logBuf.delete(0, buf.length()).append (logTitle)
    						.append("|").append("METHOD=").append(payDto.getPay_method())
    						.append("|").append("APPROV_NO=").append(payDto.getMbl_gfcr_cnclt_cnsnt_no())
    						.append("|").append("RESULT=").append(resultMap.get("RESULT_CODE"))
    						.append("|").append("RESULT_MSG=").append(resultMap.get("RESULT_MESSAGE"))
    						.append("|").append("COUPON_NUMBER=").append(resultMap.get("COUPON_NUMBER"));
    						pcLogger.info(logBuf.toString().replaceAll("\r\n", ""));		//INFO 로그
    						mLogger.error(logBuf.toString().replaceAll("\r\n", ""));
    						throw new Exception("Mobile Gift Cancel Error!");
    					}
    				} catch(Exception e){
    					// 취소 실패 시 다시 사용 처리
    					/*for(PaymentDto payCancelDto : paymentList){
    						if(StringUtils.equals(payCancelDto.getCancel_result_code(), "00")) {
    							logBuf.delete(0, logBuf.length()).append("사용 취소 실패!  - "+resultMap.get("RESULT_MESSAGE"));
    							mLogger.error(logBuf.toString().replaceAll("\r\n", ""));
    							//사용  처리
    							giftUse(payCancelDto.getMbl_gfcr_no(), payCancelDto.getOrder_no());
    							logBuf.delete(0, logBuf.length()).append("rollBack 성공");
    						}
    					}*/
    					mLogger.error(e.getMessage());
    					return false;
    				}finally {
    					buf.delete (0, buf.length ()).append ("----------------------------Gift Use Cancel End----------------------------");
    					this.mLogger.info (buf.toString ());
					}
    			}
    		}
    	}
    	
    	//상품권 결제가 없는 경우 무조건 true 리턴
    	if(mGiftCount == 0){
    		return true;
    	}
    	return true;
    }
    
    /**
     * 제휴사 취소 처리 롤백(모바일 상품권)
     */
    public int setMGiftOrderCancelRollback(SqlMapClient xoSqlMap, List<PaymentDto> paymentList, String logTitle, StringBuffer logBuf){
    	 StringBuffer buf = new StringBuffer ();
    	 int criCnt = 0;
    	if(paymentList != null && paymentList.size() > 0){
    		for(PaymentDto payDto : paymentList){
    			if(StringUtils.isNotEmpty(payDto.getPrcm_frst_code())){
    				try {
    					//정상 취소 된 건만 롤백처리
    					if ("00".equals(payDto.getCancel_result_code())) {
    						Map<String, String> resultMap;
    						buf.delete (0, buf.length ()).append ("----------------------------Gift Use Start----------------------------");
    						this.mLogger.info (buf.toString ());
    						//모바일 상품권 사용처리
    						resultMap = this.giftUse(payDto.getMbl_gfcr_no(), payDto.getOrder_no());
    						
    						if (StringUtils.equals("00",resultMap.get("RESULT_CODE"))) {
    							payDto.setCancel_result_code("");
    							payDto.setMbl_gfcr_use_cnsnt_no(resultMap.get("APPROV_NO").replace("\"", ""));
    							payDto.setResult_msg(resultMap.get("RESULT_MESSAGE"));
    							payDto.setMbl_gfcr_cnclt_cnsnt_no("");
    							payDto.setStatus("P");
    							mLogger.info("MGift cancel Rollback Success! ResultCode : "+ resultMap.get("RESULT_CODE").replace("\r\n", "")+"/approvNo :"+payDto.getMbl_gfcr_use_cnsnt_no().replace("\r\n", ""));
    						
    							//성공 approvNo update
    							paymentCancelXoMgr.updatePayment(xoSqlMap, payDto);
    							xoSqlMap.commitTransaction();
    						
    						
    						} else {
    							// Log 추가
    							mLogger.error("MGift cancel Rollback Error! ResultCode : "+ resultMap.get("RESULT_CODE").replace("\r\n", ""));
    							pcLogger.info("MGift cancel Rollback Error! ResultCode : "+ resultMap.get("RESULT_CODE").replace("\r\n", ""));
    							logBuf.delete(0, buf.length()).append(logTitle)
    							.append("|").append("METHOD=").append(payDto.getPay_method()).append("|")
    							.append("CARD_NUMBER=").append(payDto.getSbc_card_no()).append("|")
    							.append("APPROV_NO=").append(payDto.getMbl_gfcr_cnclt_cnsnt_no()).append("|")
    							.append("RESULT=").append(resultMap.get("RESULT_CODE")).append("|")
    							.append("RESULT_MSG=").append(resultMap.get("RESULT_MESSAGE")).append("|")
    							.append("COUPON_NUMBER=").append(resultMap.get("COUPON_NUMBER"));
    							pcLogger.info(logBuf.toString().replaceAll("\r\n", "")); // INFO 로그
    							mLogger.error(logBuf.toString().replaceAll("\r\n", ""));
    							criCnt ++;
    							//throw new Exception("Mobile Gift Rollback Error!");
    						}
    					}
    					
    				} catch (Exception e) {
    					return ++criCnt;
    				}finally {
    					buf.delete (0, buf.length ()).append ("----------------------------Gift Use End----------------------------");
    					this.mLogger.info (buf.toString ());
    				}
    				
    			}
    		}
    	}
    	
    	return criCnt;
    }
	
}
