package co.kr.istarbucks.xo.batch.dao;

import java.sql.SQLException;
import java.util.Map;

import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;

import com.ibatis.sqlmap.client.SqlMapClient;

public class MonitorLogDao {
	
	public void insertMonitorLog(Map<String, String> param) throws SQLException {
		SqlMapClient sqlMap = IBatisSqlConfig.getMsrSqlMapInstance();
		sqlMap.insert("monitor.insertMonitorLog", param);
	}

}
