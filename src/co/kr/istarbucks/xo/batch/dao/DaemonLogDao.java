package co.kr.istarbucks.xo.batch.dao;

import java.util.HashMap;
import java.util.Map;

import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;

import com.ibatis.sqlmap.client.SqlMapClient;

public class DaemonLogDao {
	
	public void insertLog(String sh, String name, String status, String desc) throws Exception {		
		SqlMapClient sqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
		
		Map<String, String> param = new HashMap<String, String>();
		
		param.put("sh", sh);
		param.put("name", name);
		param.put("status", status);
		param.put("desc", desc);
		
		
		sqlMap.insert("log.insertStartLog", param);
	}
	
	public void insertErrorLog(String name, String sh, String status, String desc) throws Exception {
		SqlMapClient sqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
		
		Map<String, String> param = new HashMap<String, String>();
		
		param.put("name", name);
		param.put("sh", sh);
		param.put("status", status);
		param.put("desc", desc);
		
		sqlMap.insert("log.insertErrorLog", param);
	}
	
	public void insertEndLog(String name, String sh, String status, String desc) throws Exception {
		SqlMapClient sqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
		
		Map<String, String> param = new HashMap<String, String>();
		
		param.put("name", name);
		param.put("sh", sh);
		param.put("status", status);
		param.put("desc", desc);
		
		sqlMap.insert("log.insertEndLog", param);
	}

}
