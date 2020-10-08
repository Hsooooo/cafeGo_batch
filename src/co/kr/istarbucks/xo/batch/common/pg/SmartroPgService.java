/**
 * 
 */
package co.kr.istarbucks.xo.batch.common.pg;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.log4j.Logger;

import co.kr.istarbucks.xo.batch.common.pg.dto.SmartroPgInfoDto;
import co.kr.istarbucks.xo.batch.common.util.DataCode;
import co.kr.istarbucks.xo.batch.mgr.PaymentCancelMsrMgr;
import kr.co.smartro.adapter.server.client.SmilePayClient;
import kr.co.smartro.adapter.server.client.dto.WebMessageDTO;


/**
 * ����Ʈ�� ���� Service
 *
 * @author hw.Jang
 */
public class SmartroPgService{
	private static Logger pgLogger   = Logger.getLogger ("PG_SMARTRO");
    
    private final PaymentCancelMsrMgr paymentCancelMsrMgr;
    
    public PaymentCancelMsrMgr getPaymentCancelMsrMgr() {
		return paymentCancelMsrMgr;
	}

	public SmartroPgService() {
		paymentCancelMsrMgr = new PaymentCancelMsrMgr();
	}
	
	
	public SmartroPgInfoDto getPgInfoDto(String serviceCode) {
		SmartroPgInfoDto pgInfoDto = null;
		//PG�� ���� ���� ��ȸ
		Map<String, Object> dbMap = new HashMap<String, Object>();
		dbMap.put("channelCode", DataCode.PG_CHANNEL_XOBATCH);
		dbMap.put("serviceCode", serviceCode);
		dbMap.put("pgCode", DataCode.SMARTRO_PGCM_CODE);
		try {
			pgInfoDto = paymentCancelMsrMgr.getSmartroPgInfo(dbMap);
			if(pgInfoDto == null) {
				pgLogger.error("[" + DataCode.SMARTRO_LOG_TITLE + "] ���� ���� ��ȸ ����");
			}
		} catch (Exception e) {
			pgLogger.error("[" + DataCode.SMARTRO_LOG_TITLE + "] ���� ���� ��ȸ ����");
		}
		return pgInfoDto;
	}

	/**
	 * PG ���� ��� ��û(����Ʈ��)
	 * @param request
	 * @return
	 */
	public Map<String, String> pgCancelProcessSmartro(String tid, String orderNo, String cancelReason, String logTitle, String amt, SmartroPgInfoDto pgInfoDto){
		StringBuffer buf = new StringBuffer();
		Map<String, String> resultMap = new HashMap<String, String>();
		pgLogger.info("============================ [" + DataCode.SMARTRO_LOG_TITLE + "] ��� ��û START ===============================");
		
		try {
			SmilePayClient payClient = new SmilePayClient();
			
			payClient.setParam("SMILEPAY_DOMAIN_NAME", pgInfoDto.getIpads());
			payClient.setParam("SMILEPAY_ADAPTOR_LISTEN_PORT", pgInfoDto.getCncltPortNo());
			// LogHome - Path�� ���� ����(Path/PG_yyyyMMdd.log) 
			// ���ϸ��� �����Ϸ��� ����Ʈ�� ��� ���� �ʿ�
			payClient.setParam("SMILEPAY_LOG_HOME", pgInfoDto.getLogFilePath());
			// TimedOut - PG�������������(msr_pgcm_stlmn_infrm_m#.tmt_mscnt) ���̺��� ����
			payClient.setParam("SOCKET_SO_TIMEOUT", pgInfoDto.getTmtMscnt());
			payClient.setParam("APP_LOG", DataCode.SMARTRO_APP_LOG);
			payClient.setParam("EVENT_LOG", DataCode.SMARTRO_EVENT_LOG);
			payClient.setParam("EncFlag", DataCode.SMARTRO_ENC_FLAG);
			payClient.setParam("SERVICE_MODE", DataCode.SMARTRO_CANCEL_MODE);
			payClient.setParam("Currency", DataCode.SMARTRO_PAYMENT_CURRENCY);
			payClient.setParam("CharSet", DataCode.SMARTRO_CHARACTER_SET);
			
			payClient.setParam("CancelAmt", amt);
			payClient.setParam("CancelPwd", pgInfoDto.getPgcmShopCncltPwd());
			payClient.setParam("CancelMsg", cancelReason);
			payClient.setParam("MID", pgInfoDto.getPgcmShopId());
			payClient.setParam("TID", tid);
			
			WebMessageDTO resultDto = payClient.doService();
			
			if(resultDto == null) {
				resultMap.put("resultCode", DataCode.RESULT_ETC_ERROR_CODE);
				resultMap.put("resultMsg", "resultDto is null");
				buf.delete (0, buf.length ()).append("["+DataCode.SMARTRO_LOG_TITLE+"]") 
				.append(logTitle).append(" resultDto == null | return false");
				pgLogger.error (buf.toString ().replaceAll("\r\n", ""));
	            pgLogger.info("============================ [" + DataCode.SMARTRO_LOG_TITLE + "] ��� ��û END ===============================");
				return resultMap;
			}
			
			String resultCode = resultDto.getParameter("ResultCode"); // ����ڵ� (���� :2001(�ſ�ī��), 2211(�ǽð�������ü), �� �� ����)
			String resultMsg = resultDto.getParameter("ResultMsg");
			
			if(!StringUtils.equals(resultCode, DataCode.SMARTRO_CARD_CANCEL) && !StringUtils.equals(resultCode, DataCode.SMARTRO_BANK_CANCEL)) {
				resultMap.put("resultCode", DataCode.RESULT_ETC_ERROR_CODE);
				resultMap.put("resultMsg", StringUtils.defaultIfEmpty(resultMsg,"resultMsg is null"));
				buf.delete (0, buf.length ()).append("["+DataCode.SMARTRO_LOG_TITLE+"]")
				.append(logTitle).append("resultCode = " + resultCode + " | return false");
				pgLogger.error (buf.toString ().replaceAll("\r\n", ""));
	            pgLogger.info("============================ [" + DataCode.SMARTRO_LOG_TITLE + "] ��� ��û END ===============================");
				return resultMap;
			} else {
				resultMap.put("resultCode", DataCode.RESULT_SUCCESS_CODE);
				resultMap.put("resultMsg", resultMsg);
			}
		} catch(Exception e) {
			buf.delete (0, buf.length ()).append("["+DataCode.SMARTRO_LOG_TITLE+"]") 
			.append(logTitle).append(" Exception - " + e.getMessage());
			pgLogger.error (buf.toString ().replaceAll("\r\n", ""));
            resultMap.put("resultCode", DataCode.RESULT_ETC_ERROR_CODE);
            resultMap.put("resultMsg", "Exception Error");
		}
		
		pgLogger.info("============================ [" + DataCode.SMARTRO_LOG_TITLE + "] ��� ��û END ===============================");
		return resultMap;
	}


}
