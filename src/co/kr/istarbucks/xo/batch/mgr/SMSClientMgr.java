package co.kr.istarbucks.xo.batch.mgr;

import java.util.List;
import java.util.Map;

import co.kr.istarbucks.xo.batch.common.dto.xo.SmtTranDto;
import co.kr.istarbucks.xo.batch.dao.SMSClientDao;

public class SMSClientMgr {
	
	private final SMSClientDao scd;
	
	public SMSClientMgr() {
		scd = new SMSClientDao();
	}
	
	public void insertSMS(Map<String, Object> param) throws Exception {
		
		scd.insertSMS(param);
	}
	
	public Map<String, String> getLastStats(String couponCode) throws Exception {
		
		return scd.getLastStats(couponCode);
	}
	
	public List<Map<String, Object>> getCouponStatSMSInfo(Map<String, String> param) throws Exception {
		
		return scd.getCouponStatSMSInfo(param);
	}
	
	public void updateCouponStatSMSInfo(int seq, String msg, String result) throws Exception {
		
		scd.updateCouponStatSMSInfo(seq, msg, result);
	}
}
