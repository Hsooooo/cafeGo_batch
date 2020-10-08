package co.kr.istarbucks.xo.batch.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;

import com.ibatis.sqlmap.client.SqlMapClient;

public class SMSClientDao {
//	private static Log logger = LogFactory.getLog("SMSCLIENT");
	
	public Map<String, String> getLastStats(String couponCode) throws Exception {
		SqlMapClient sqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
		
		return (Map<String, String>) sqlMap.queryForObject("sms.getLastStats", couponCode); 
	}
	
	public void insertSMS(Map<String, Object> param) throws Exception {
		SqlMapClient sqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
		
		sqlMap.insert("sms.insertSMS", param);
		
	}
	
	public List<Map<String, Object>> getCouponStatSMSInfo(Map<String, String> param) throws Exception {
		SqlMapClient sqlMap = IBatisSqlConfig.getMsrSqlMapInstance();		
		return  sqlMap.queryForList("sms.getCouponStatSMSInfo", param);
	}
	
	public void updateCouponStatSMSInfo(int seq, String msg, String result) throws Exception {
		SqlMapClient sqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("seq", seq);
		param.put("msg", msg);
		param.put("result", result);
		
		sqlMap.update("sms.updateCouponStatSMSInfo", param);
	}
}
