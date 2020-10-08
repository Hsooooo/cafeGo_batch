package co.kr.istarbucks.xo.batch.mgr;

import java.util.Map;

import co.kr.istarbucks.xo.batch.dao.DaemonLogDao;

public class DaemonLogMgr {
	
	private final DaemonLogDao dlo;
	
	public DaemonLogMgr() {
		dlo = new DaemonLogDao();
	}
	
	public void insertLog(String sh, String name, String status, String desc) throws Exception {
		
		dlo.insertLog(sh, name, status, desc);
	}
	
	public void insertErrorLog(String name, String sh, String status, String desc) throws Exception {
		
		dlo.insertErrorLog(name, sh, status, desc);
	}
	
	public void insertEndLog(String name, String sh, String status, String desc) throws Exception {
		
		dlo.insertEndLog(name, sh, status, desc);
	}
	
}
