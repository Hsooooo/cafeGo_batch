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
	 * NICE ��ҿ�û
	 * @param pgDto
	 * @return
	 */
	public NiceResultDto niceCancelProcess(String pMoid, String pTid, String pCancelAmt, String logTitle){
		StringBuffer buf = new StringBuffer();
		
		NiceResultDto niceResultDto = new NiceResultDto();
		niceResultDto.setServiceMode("CL0");	//���񽺸��(PY0:����, CL0:���)
		niceResultDto.setMoid(pMoid);
		
		try{
			HttpServletRequestMock request   = new HttpServletRequestMock();
			HttpServletResponseMock response = new HttpServletResponseMock();
			
			/** 1. Request Wrapper Ŭ������ ��� */ 
			NicePayHttpServletRequestWrapper httpRequestWrapper = new NicePayHttpServletRequestWrapper(request);

			//��� �Ķ���� ����
			httpRequestWrapper.addParameter("Moid", 			 pMoid);
			httpRequestWrapper.addParameter("MID", 				 "dreamtestm");
			httpRequestWrapper.addParameter("TID", 				 pTid);
			httpRequestWrapper.addParameter("CancelAmt", 		 pCancelAmt);
			httpRequestWrapper.addParameter("CancelMsg", 		 "���̷����� BATCH ���� ���");
			httpRequestWrapper.addParameter("CancelPwd", 		 "123456");
			httpRequestWrapper.addParameter("PartialCancelCode", "0");
			try{
				httpRequestWrapper.addParameter("CancelIP", 	 (InetAddress.getLocalHost()).getHostAddress());
			}catch(Exception e){
				httpRequestWrapper.addParameter("CancelIP", 	 "");
			}

			/** 2. ���� ����Ϳ� �����ϴ� Web �������̽� ��ü�� ���� */
			NicePayWEB nicepayWEB = new NicePayWEB();

			/** 2-1. �α� ���丮 ���� */
			nicepayWEB.setParam("NICEPAY_LOG_HOME", NICEPAY_LOG_HOME);

			/** 2-2. �̺�Ʈ�α� ��� ����(0:DISABLE, 1:ENABLE) */
			nicepayWEB.setParam("APP_LOG", APP_LOG);

			/** 2-3. ���ø����̼Ƿα� ��� ����(0:DISABLE, 1:ENABLE) */
			nicepayWEB.setParam("EVENT_LOG", EVENT_LOG);

			/** 2-4. ��ȣȭ�÷��� ����(N:��, S:��ȣȭ) */
			nicepayWEB.setParam("EncFlag", "S");

			/** 2-5. ���񽺸�� ����(���� ����:PY0, ��� ����:CL0) */
			nicepayWEB.setParam("SERVICE_MODE", "CL0");

			/** 3. ������� ��û */
			WebMessageDTO responseDTO = nicepayWEB.doService(httpRequestWrapper, response);

			/** 4. ��Ұ�� */
			String mid 		  = responseDTO.getParameter("MID");			//����ID
			String tid 		  = responseDTO.getParameter("TID");			//TID
			String payMethod  = responseDTO.getParameter("PayMethod");   	//��� ��������
			String resultCode = responseDTO.getParameter("ResultCode"); 	//����ڵ� {2001:��Ҽ���, 2002:���������, �� �� ����}
			String resultMsg  = responseDTO.getParameter("ResultMsg");   	//����޽���
			String cancelAmt  = responseDTO.getParameter("CancelAmt");   	//��ұݾ�
			String cancelDate = responseDTO.getParameter("CancelDate");     //�����
			String cancelTime = responseDTO.getParameter("CancelTime");   	//��ҽð�
			String cancelNum  = responseDTO.getParameter("CancelNum");   	//��ҹ�ȣ
			
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
			niceResultDto.setResultMsg("���:BATCH ���� �ý��� ����");
			
            buf.delete (0, buf.length ()).append (logTitle).append (ex.getMessage ());
		}
		
		return niceResultDto;
	}
	
	public void cancel(String orderNo, String tid, String amount) {
		NiceResultDto niceDto = this.niceCancelProcess(orderNo, tid, amount, "�׽�Ʈ");
		System.out.println(niceDto.toCancelString());
	}
	
	public static void main(String[] args) {
		TestSSGPayMultiCancel cancel = new TestSSGPayMultiCancel();
		cancel.cancel("32016060919332106748", "dreamtestm21071606091933162658", "600");
	}
}
