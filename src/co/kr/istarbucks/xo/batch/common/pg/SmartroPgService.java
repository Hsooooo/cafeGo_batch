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
 * 스마트로 공통 Service
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
		//PG사 연동 정보 조회
		Map<String, Object> dbMap = new HashMap<String, Object>();
		dbMap.put("channelCode", DataCode.PG_CHANNEL_XOBATCH);
		dbMap.put("serviceCode", serviceCode);
		dbMap.put("pgCode", DataCode.SMARTRO_PGCM_CODE);
		try {
			pgInfoDto = paymentCancelMsrMgr.getSmartroPgInfo(dbMap);
			if(pgInfoDto == null) {
				pgLogger.error("[" + DataCode.SMARTRO_LOG_TITLE + "] 연동 정보 조회 실패");
			}
		} catch (Exception e) {
			pgLogger.error("[" + DataCode.SMARTRO_LOG_TITLE + "] 연동 정보 조회 실패");
		}
		return pgInfoDto;
	}

	/**
	 * PG 승인 취소 요청(스마트로)
	 * @param request
	 * @return
	 */
	public Map<String, String> pgCancelProcessSmartro(String tid, String orderNo, String cancelReason, String logTitle, String amt, SmartroPgInfoDto pgInfoDto){
		StringBuffer buf = new StringBuffer();
		Map<String, String> resultMap = new HashMap<String, String>();
		pgLogger.info("============================ [" + DataCode.SMARTRO_LOG_TITLE + "] 취소 요청 START ===============================");
		
		try {
			SmilePayClient payClient = new SmilePayClient();
			
			payClient.setParam("SMILEPAY_DOMAIN_NAME", pgInfoDto.getIpads());
			payClient.setParam("SMILEPAY_ADAPTOR_LISTEN_PORT", pgInfoDto.getCncltPortNo());
			// LogHome - Path만 설정 가능(Path/PG_yyyyMMdd.log) 
			// 파일명을 변경하려면 스마트로 모듈 수정 필요
			payClient.setParam("SMILEPAY_LOG_HOME", pgInfoDto.getLogFilePath());
			// TimedOut - PG사결제정보관리(msr_pgcm_stlmn_infrm_m#.tmt_mscnt) 테이블에서 관리
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
	            pgLogger.info("============================ [" + DataCode.SMARTRO_LOG_TITLE + "] 취소 요청 END ===============================");
				return resultMap;
			}
			
			String resultCode = resultDto.getParameter("ResultCode"); // 결과코드 (정상 :2001(신용카드), 2211(실시간계좌이체), 그 외 에러)
			String resultMsg = resultDto.getParameter("ResultMsg");
			
			if(!StringUtils.equals(resultCode, DataCode.SMARTRO_CARD_CANCEL) && !StringUtils.equals(resultCode, DataCode.SMARTRO_BANK_CANCEL)) {
				resultMap.put("resultCode", DataCode.RESULT_ETC_ERROR_CODE);
				resultMap.put("resultMsg", StringUtils.defaultIfEmpty(resultMsg,"resultMsg is null"));
				buf.delete (0, buf.length ()).append("["+DataCode.SMARTRO_LOG_TITLE+"]")
				.append(logTitle).append("resultCode = " + resultCode + " | return false");
				pgLogger.error (buf.toString ().replaceAll("\r\n", ""));
	            pgLogger.info("============================ [" + DataCode.SMARTRO_LOG_TITLE + "] 취소 요청 END ===============================");
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
		
		pgLogger.info("============================ [" + DataCode.SMARTRO_LOG_TITLE + "] 취소 요청 END ===============================");
		return resultMap;
	}


}
