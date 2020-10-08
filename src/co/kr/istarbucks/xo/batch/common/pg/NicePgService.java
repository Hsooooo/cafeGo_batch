/*
 * @(#) $Id: NicePgService.java,v 1.1 2015/11/24 03:58:54 soonwoo Exp $
 * 
 * Starbucks Service
 * 
 * Copyright 2015 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, Digitalro 306, Guro-gu,
 * Seoul, Korea
 */

/**
 * 
 */
package co.kr.istarbucks.xo.batch.common.pg;

import java.net.InetAddress;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import kr.co.nicevan.nicepay.adapter.etc.HttpServletRequestMock;
import kr.co.nicevan.nicepay.adapter.etc.HttpServletResponseMock;
import kr.co.nicevan.nicepay.adapter.web.NicePayHttpServletRequestWrapper;
import kr.co.nicevan.nicepay.adapter.web.NicePayWEB;
import kr.co.nicevan.nicepay.adapter.web.dto.WebMessageDTO;
import co.kr.istarbucks.xo.batch.common.pg.dto.NiceResultDto;
import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;
import co.kr.istarbucks.xo.batch.exception.XOException;


/**
 * 나이스(nice) 공통 Service
 *
 * @author sw.Lee
 * @version $Revision: 1.1 $
 */
public class NicePgService{
	private final Configuration conf;
	private static Logger niceLogger = Logger.getLogger ("PG_NICE");
	private static Logger pcLogger   = Logger.getLogger ("PPC_INFO");
    private static Logger eLogger    = Logger.getLogger ("PPC_ERROR");

    private final String NICEPAY_LOG_HOME;
    private final String APP_LOG;
    private final String EVENT_LOG;
	
	//constructor
	public NicePgService(){
		this.conf = CommPropertiesConfiguration.getConfiguration("xoBatch.properties");
		
		this.NICEPAY_LOG_HOME = conf.getString("NICEPAY_LOG_HOME");
		this.APP_LOG 		  = conf.getString("APP_LOG");
		this.EVENT_LOG 		  = conf.getString("EVENT_LOG");
	}


	/**
	 * NICE 취소요청
	 * @param pgDto
	 * @return
	 */
	public NiceResultDto niceCancelProcess(String pMoid, String pTid, String pCancelAmt, String logTitle){
		StringBuffer buf = new StringBuffer();
		
		NiceResultDto niceResultDto = new NiceResultDto();
		niceResultDto.setServiceMode("CL0");	//서비스모드(PY0:결제, CL0:취소)
		niceResultDto.setMoid(pMoid);
		
		try{
			HttpServletRequestMock request   = new HttpServletRequestMock();
			HttpServletResponseMock response = new HttpServletResponseMock();
			
			/** 1. Request Wrapper 클래스를 등록 */ 
			NicePayHttpServletRequestWrapper httpRequestWrapper = new NicePayHttpServletRequestWrapper(request);

			//취소 파라미터 설정
			httpRequestWrapper.addParameter("Moid", 			 pMoid);
			httpRequestWrapper.addParameter("MID", 				 conf.getString("xo.ssg.nice.mid"));
			httpRequestWrapper.addParameter("TID", 				 pTid);
			httpRequestWrapper.addParameter("CancelAmt", 		 pCancelAmt);
			httpRequestWrapper.addParameter("CancelMsg", 		 "사이렌오더 BATCH 결제 취소");
			httpRequestWrapper.addParameter("CancelPwd", 		 conf.getString("xo.ssg.nice.cancelPassWord"));
			httpRequestWrapper.addParameter("PartialCancelCode", "0");
			try{
				httpRequestWrapper.addParameter("CancelIP", 	 InetAddress.getLocalHost().getHostAddress());
			}catch(Exception e){
				httpRequestWrapper.addParameter("CancelIP", 	 "");
			}

			/** 2. 소켓 어댑터와 연동하는 Web 인터페이스 객체를 생성 */
			NicePayWEB nicepayWEB = new NicePayWEB();

			/** 2-1. 로그 디렉토리 설정 */
			nicepayWEB.setParam("NICEPAY_LOG_HOME", NICEPAY_LOG_HOME);

			/** 2-2. 이벤트로그 모드 설정(0:DISABLE, 1:ENABLE) */
			nicepayWEB.setParam("APP_LOG", APP_LOG);

			/** 2-3. 어플리케이션로그 모드 설정(0:DISABLE, 1:ENABLE) */
			nicepayWEB.setParam("EVENT_LOG", EVENT_LOG);

			/** 2-4. 암호화플래그 설정(N:평문, S:암호화) */
			nicepayWEB.setParam("EncFlag", "S");

			/** 2-5. 서비스모드 설정(결제 서비스:PY0, 취소 서비스:CL0) */
			nicepayWEB.setParam("SERVICE_MODE", "CL0");

			/** 3. 결제취소 요청 */
			WebMessageDTO responseDTO = nicepayWEB.doService(httpRequestWrapper, response);

			if(responseDTO == null) {
				throw new XOException("payment Cancel Error");
			}
			
			/** 4. 취소결과 */
			String mid 		  = responseDTO.getParameter("MID");			//상점ID
			String tid 		  = responseDTO.getParameter("TID");			//TID
			String payMethod  = responseDTO.getParameter("PayMethod");   	//취소 결제수단
			String resultCode = responseDTO.getParameter("ResultCode"); 	//결과코드 {2001:취소성공, 2002:취소진행중, 그 외 에러}
			String resultMsg  = responseDTO.getParameter("ResultMsg");   	//결과메시지
			String cancelAmt  = responseDTO.getParameter("CancelAmt");   	//취소금액
			String cancelDate = responseDTO.getParameter("CancelDate");     //취소일
			String cancelTime = responseDTO.getParameter("CancelTime");   	//취소시간
			String cancelNum  = responseDTO.getParameter("CancelNum");   	//취소번호
			
			niceResultDto.setMid(mid);
			niceResultDto.setTid(tid);
			niceResultDto.setPayMethod(payMethod);
			niceResultDto.setResultCode(resultCode);
			niceResultDto.setResultMsg(resultMsg);
			niceResultDto.setCancelAmt(cancelAmt);
			niceResultDto.setCancelDate(cancelDate);
			niceResultDto.setCancelTime(cancelTime);
			niceResultDto.setCancelNum(cancelNum);
			niceLogger.info("ORDER_NO:" + pMoid.replaceAll("\n|\r", "") + ", SSG.PAY_CANCEL_RESULT -> " + niceResultDto.toCancelString().replaceAll("\n|\r", ""));
			
		}catch(Exception ex){
			niceLogger.error("ORDER_NO:" + pMoid.replaceAll("\n|\r", "") + ", Exception -> " + ex.toString().replaceAll("\n|\r", ""));
			
			niceResultDto.setResultCode("E999");
			niceResultDto.setResultMsg("취소:BATCH 서버 시스템 에러");
			
            buf.delete (0, buf.length ()).append (logTitle).append (ex.getMessage ());
            this.pcLogger.error (buf.toString ().replaceAll("\n|\r", ""), ex);
            this.eLogger.error (buf.toString ().replaceAll("\n|\r", ""), ex);
		}
		
		return niceResultDto;
	}


	/**
	 * NICE 취소요청(PG 승인대사 배치)
	 * @param pMoid
	 * @param pMid
	 * @param pTid
	 * @param pCancelAmt
	 * @return
	 */
	public NiceResultDto niceTradeCancelProcess(String pMoid, String pMid, String pCancelPwd, String pTid, String pCancelAmt) {
		StringBuffer buf = new StringBuffer();
		
		NiceResultDto niceResultDto = new NiceResultDto();
		niceResultDto.setServiceMode("CL0");	//서비스모드(PY0:결제, CL0:취소)
		niceResultDto.setMoid(pMoid);
		
		try{
			HttpServletRequestMock request   = new HttpServletRequestMock();
			HttpServletResponseMock response = new HttpServletResponseMock();
			
			/** 1. Request Wrapper 클래스를 등록 */ 
			NicePayHttpServletRequestWrapper httpRequestWrapper = new NicePayHttpServletRequestWrapper(request);

			//취소 파라미터 설정
			httpRequestWrapper.addParameter("Moid", 			 pMoid);
			httpRequestWrapper.addParameter("MID", 				 pMid);
			httpRequestWrapper.addParameter("TID", 				 pTid);
			httpRequestWrapper.addParameter("CancelAmt", 		 pCancelAmt);
			httpRequestWrapper.addParameter("CancelMsg", 		 "PG 승인 대사 배치 결제 취소");
			httpRequestWrapper.addParameter("CancelPwd", 		 pCancelPwd);
			httpRequestWrapper.addParameter("PartialCancelCode", "0");
			try{
				httpRequestWrapper.addParameter("CancelIP", 	 InetAddress.getLocalHost().getHostAddress());
			}catch(Exception e){
				httpRequestWrapper.addParameter("CancelIP", 	 "");
			}

			/** 2. 소켓 어댑터와 연동하는 Web 인터페이스 객체를 생성 */
			NicePayWEB nicepayWEB = new NicePayWEB();

			/** 2-1. 로그 디렉토리 설정 */
			nicepayWEB.setParam("NICEPAY_LOG_HOME", NICEPAY_LOG_HOME);

			/** 2-2. 이벤트로그 모드 설정(0:DISABLE, 1:ENABLE) */
			nicepayWEB.setParam("APP_LOG", APP_LOG);

			/** 2-3. 어플리케이션로그 모드 설정(0:DISABLE, 1:ENABLE) */
			nicepayWEB.setParam("EVENT_LOG", EVENT_LOG);

			/** 2-4. 암호화플래그 설정(N:평문, S:암호화) */
			nicepayWEB.setParam("EncFlag", "S");

			/** 2-5. 서비스모드 설정(결제 서비스:PY0, 취소 서비스:CL0) */
			nicepayWEB.setParam("SERVICE_MODE", "CL0");

			/** 3. 결제취소 요청 */
			WebMessageDTO responseDTO = nicepayWEB.doService(httpRequestWrapper, response);

			if(responseDTO == null) {
				throw new XOException("payment Cancel Error");
			}
			
			/** 4. 취소결과 */
			String mid 		  = responseDTO.getParameter("MID");			//상점ID
			String tid 		  = responseDTO.getParameter("TID");			//TID
			String payMethod  = responseDTO.getParameter("PayMethod");   	//취소 결제수단
			String resultCode = responseDTO.getParameter("ResultCode"); 	//결과코드 {2001:취소성공, 2002:취소진행중, 그 외 에러}
			String resultMsg  = responseDTO.getParameter("ResultMsg");   	//결과메시지
			String cancelAmt  = responseDTO.getParameter("CancelAmt");   	//취소금액
			String cancelDate = responseDTO.getParameter("CancelDate");     //취소일
			String cancelTime = responseDTO.getParameter("CancelTime");   	//취소시간
			String cancelNum  = responseDTO.getParameter("CancelNum");   	//취소번호
			
			niceResultDto.setMid(mid);
			niceResultDto.setTid(tid);
			niceResultDto.setPayMethod(payMethod);
			niceResultDto.setResultCode(resultCode);
			niceResultDto.setResultMsg(resultMsg);
			niceResultDto.setCancelAmt(cancelAmt);
			niceResultDto.setCancelDate(cancelDate);
			niceResultDto.setCancelTime(cancelTime);
			niceResultDto.setCancelNum(cancelNum);
			niceLogger.info("ORDER_NO:" + pMoid.replaceAll("\n|\r", "") + ", SSG.PAY_CANCEL_RESULT -> " + niceResultDto.toCancelString().replaceAll("\n|\r", ""));
		}catch(Exception ex){
			niceLogger.error("ORDER_NO:" + pMoid.replaceAll("\n|\r", "") + ", Exception -> " + ex.toString().replaceAll("\n|\r", ""));
			
			niceResultDto.setResultCode("E999");
			niceResultDto.setResultMsg("취소:BATCH 서버 시스템 에러");
			
           // buf.delete (0, buf.length ()).append (logTitle).append (ex.getMessage ());
            this.pcLogger.error (buf.toString (), ex);
            this.eLogger.error (buf.toString (), ex);
		}
		
		return niceResultDto;
	}


}
