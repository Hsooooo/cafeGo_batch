/*
 * @(#) $Id: TestSSGPayMultiCancel.java,v 1.1 2016/06/09 10:38:37 sinjw Exp $
 * 
 * Starbucks Service
 * 
 * Copyright 2013 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.main;

import java.net.InetAddress;

import kr.co.nicevan.nicepay.adapter.etc.HttpServletRequestMock;
import kr.co.nicevan.nicepay.adapter.etc.HttpServletResponseMock;
import kr.co.nicevan.nicepay.adapter.web.NicePayHttpServletRequestWrapper;
import kr.co.nicevan.nicepay.adapter.web.NicePayWEB;
import kr.co.nicevan.nicepay.adapter.web.dto.WebMessageDTO;
import co.kr.istarbucks.xo.batch.common.pg.dto.NiceResultDto;

public class TestSSGPayMultiCancel {
	
	private String NICEPAY_LOG_HOME = "/xop_app/xo/batch/logs/paymentpgcancel/pg/nice";
	private String APP_LOG 			= "1";
	private String EVENT_LOG 		= "1";
	
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
			httpRequestWrapper.addParameter("MID", 				 "dreamtestm");
			httpRequestWrapper.addParameter("TID", 				 pTid);
			httpRequestWrapper.addParameter("CancelAmt", 		 pCancelAmt);
			httpRequestWrapper.addParameter("CancelMsg", 		 "사이렌오더 BATCH 결제 취소");
			httpRequestWrapper.addParameter("CancelPwd", 		 "123456");
			httpRequestWrapper.addParameter("PartialCancelCode", "0");
			try{
				httpRequestWrapper.addParameter("CancelIP", 	 (InetAddress.getLocalHost()).getHostAddress());
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
			niceResultDto.setPayMethod(payMethod);;
			niceResultDto.setResultCode(resultCode);
			niceResultDto.setResultMsg(resultMsg);
			niceResultDto.setCancelAmt(cancelAmt);
			niceResultDto.setCancelDate(cancelDate);
			niceResultDto.setCancelTime(cancelTime);
			niceResultDto.setCancelNum(cancelNum);
			System.out.println("ORDER_NO:" + pMoid + ", SSG.PAY_CANCEL_RESULT -> " + niceResultDto.toCancelString());
		}catch(Exception ex){
			ex.printStackTrace();
			System.err.println("ORDER_NO:" + pMoid + ", Exception -> " + ex.toString());
			
			niceResultDto.setResultCode("E999");
			niceResultDto.setResultMsg("취소:BATCH 서버 시스템 에러");
			
            buf.delete (0, buf.length ()).append (logTitle).append (ex.getMessage ());
		}
		
		return niceResultDto;
	}
	
	public void cancel(String orderNo, String tid, String amount) {
		NiceResultDto niceDto = this.niceCancelProcess(orderNo, tid, amount, "테스트");
		System.out.println(niceDto.toCancelString());
	}
	
	public static void main(String[] args) {
		TestSSGPayMultiCancel cancel = new TestSSGPayMultiCancel();
		cancel.cancel("32016060919332106748", "dreamtestm21071606091933162658", "600");
	}
}
