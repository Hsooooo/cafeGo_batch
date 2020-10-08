package co.kr.istarbucks.xo.batch.mgr;

import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;

import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;
import co.kr.istarbucks.xo.batch.dao.EzOrderDao; 
public class EzOrderMgr {
	private final EzOrderDao ezorderDao;
	
	/**
	 * DB 오늘 날짜 가져오기
	 * @return
	 * @throws Exception
	 */ 
	public Map<String, String> getToday () throws Exception {
		return this.ezorderDao.getToday ();
	}
	
	public EzOrderMgr () {
		this.ezorderDao = new EzOrderDao ();
	}
	/**
	 * 이지오더 일통계 
	 * @return
	 * @throws Exception
	 */
	public int ezOrder() throws Exception {
		return this.ezorderDao.ezOrder ();
	}
}
