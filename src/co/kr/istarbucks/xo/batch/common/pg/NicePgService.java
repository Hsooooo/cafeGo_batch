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
 * ���̽�(nice) ���� Service
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
			httpRequestWrapper.addParameter("MID", 				 conf.getString("xo.ssg.nice.mid"));
			httpRequestWrapper.addParameter("TID", 				 pTid);
			httpRequestWrapper.addParameter("CancelAmt", 		 pCancelAmt);
			httpRequestWrapper.addParameter("CancelMsg", 		 "���̷����� BATCH ���� ���");
			httpRequestWrapper.addParameter("CancelPwd", 		 conf.getString("xo.ssg.nice.cancelPassWord"));
			httpRequestWrapper.addParameter("PartialCancelCode", "0");
			try{
				httpRequestWrapper.addParameter("CancelIP", 	 InetAddress.getLocalHost().getHostAddress());
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

			if(responseDTO == null) {
				throw new XOException("payment Cancel Error");
			}
			
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
			niceResultDto.setResultMsg("���:BATCH ���� �ý��� ����");
			
            buf.delete (0, buf.length ()).append (logTitle).append (ex.getMessage ());
            this.pcLogger.error (buf.toString ().replaceAll("\n|\r", ""), ex);
            this.eLogger.error (buf.toString ().replaceAll("\n|\r", ""), ex);
		}
		
		return niceResultDto;
	}


	/**
	 * NICE ��ҿ�û(PG ���δ�� ��ġ)
	 * @param pMoid
	 * @param pMid
	 * @param pTid
	 * @param pCancelAmt
	 * @return
	 */
	public NiceResultDto niceTradeCancelProcess(String pMoid, String pMid, String pCancelPwd, String pTid, String pCancelAmt) {
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
			httpRequestWrapper.addParameter("MID", 				 pMid);
			httpRequestWrapper.addParameter("TID", 				 pTid);
			httpRequestWrapper.addParameter("CancelAmt", 		 pCancelAmt);
			httpRequestWrapper.addParameter("CancelMsg", 		 "PG ���� ��� ��ġ ���� ���");
			httpRequestWrapper.addParameter("CancelPwd", 		 pCancelPwd);
			httpRequestWrapper.addParameter("PartialCancelCode", "0");
			try{
				httpRequestWrapper.addParameter("CancelIP", 	 InetAddress.getLocalHost().getHostAddress());
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

			if(responseDTO == null) {
				throw new XOException("payment Cancel Error");
			}
			
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
			niceResultDto.setResultMsg("���:BATCH ���� �ý��� ����");
			
           // buf.delete (0, buf.length ()).append (logTitle).append (ex.getMessage ());
            this.pcLogger.error (buf.toString (), ex);
            this.eLogger.error (buf.toString (), ex);
		}
		
		return niceResultDto;
	}


}
