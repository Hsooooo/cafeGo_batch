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
	 * �������� ���(���̷����� ���� -> ��������)
	 * @param scksaSqlMap
	 * @param dto
	 * @return
	 */
	public boolean insertScksaWholecakeOrder(SqlMapClient scksaSqlMap, WholecakeOrderDto dto) throws Exception {
		return this.scksaWholecakeOrderDao.insertScksaWholecakeOrder (scksaSqlMap, dto);
	}

	/**
	 * �������� ������ ���(���̷����� ���� -> ��������)
	 * @param scksaSqlMap
	 * @param dto
	 * @return
	 */
	public boolean insertScksaWholecakeOrderDetail(SqlMapClient scksaSqlMap, WholecakeOrderDto dto) throws Exception {
		return this.scksaWholecakeOrderDao.insertScksaWholecakeOrderDetail (scksaSqlMap, dto);
	}
}
