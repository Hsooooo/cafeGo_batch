package co.kr.istarbucks.xo.batch.mgr;

import java.util.Map;

import co.kr.istarbucks.xo.batch.dao.MonitorLogDao;

public class MonitorLogMgr {
	
	private final MonitorLogDao dao;
	
	public MonitorLogMgr() {
		dao = new MonitorLogDao();
	}
	
	public void insertMonitorLog(Map<String, String> param) throws Exception {
		dao.insertMonitorLog(param);
	}

}
