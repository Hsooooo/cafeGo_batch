package co.kr.istarbucks.xo.batch.mgr;

import co.kr.istarbucks.xo.batch.common.dto.xo.WholecakeOrderDto;
import co.kr.istarbucks.xo.batch.dao.ScksaWholecakeOrderDao;

import com.ibatis.sqlmap.client.SqlMapClient;

public class ScksaWholecakeOrderMgr {
	private final ScksaWholecakeOrderDao scksaWholecakeOrderDao;
	
	public ScksaWholecakeOrderMgr() {
		scksaWholecakeOrderDao  = new ScksaWholecakeOrderDao();
	}

	/**
	 * 발주정보 등록(사이렌오더 서버 -> 영업정보)
	 * @param scksaSqlMap
	 * @param dto
	 * @return
	 */
	public boolean insertScksaWholecakeOrder(SqlMapClient scksaSqlMap, WholecakeOrderDto dto) throws Exception {
		return this.scksaWholecakeOrderDao.insertScksaWholecakeOrder (scksaSqlMap, dto);
	}

	/**
	 * 발주정보 상세정보 등록(사이렌오더 서버 -> 영업정보)
	 * @param scksaSqlMap
	 * @param dto
	 * @return
	 */
	public boolean insertScksaWholecakeOrderDetail(SqlMapClient scksaSqlMap, WholecakeOrderDto dto) throws Exception {
		return this.scksaWholecakeOrderDao.insertScksaWholecakeOrderDetail (scksaSqlMap, dto);
	}
}
