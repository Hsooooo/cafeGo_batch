/*
 * @(#) $Id: PushMgr.java,v 1.2 2017/05/10 02:13:54 namgu1 Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */
package co.kr.istarbucks.xo.batch.mgr;

import java.sql.SQLException;
import java.util.Map;

import co.kr.istarbucks.xo.batch.common.dto.push.ReservationPushDto;
import co.kr.istarbucks.xo.batch.dao.PushDao;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * PUSH �߼�  PushMgr.
 * 
 * @author eZEN ksy
 * @since 2016. 11. 03.
 * @version $Revision: 1.2 $
 */
public class PushMgr {

	private final PushDao pushDao;
	
	public PushMgr() {
		this.pushDao = new PushDao();
	}	
	
	/**
	 * PUSH ��뷮 �߼� ���
	 * @param sqlMap
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int insertTblReservationPush(SqlMapClient sqlMap, ReservationPushDto params) throws SQLException {
		return this.pushDao.insertTblReservationPush(sqlMap, params);
	}

	/**
	 * PUSH �ַ�� DB ���̺� TMS_PERSON_QUEUE(����ȭ ��뷮 : TPQ)�� ������ ����
	 * @param sqlMap
	 * @param dbMap
	 * @return
	 * @throws SQLException
	 */
	public boolean insertTmsPersonQueue(SqlMapClient sqlMap,Map<String, Object> dbMap) throws SQLException {
		return this.pushDao.insertTmsPersonQueue(sqlMap, dbMap);
	}
	
	/**
	 * PUSH �ַ�� DB ���̺� TMS_QUEUE�� ������ ����
	 * @param sqlMap
	 * @param dbMap
	 * @return
	 * @throws SQLException
	 */
	public boolean insertTmsQueue(SqlMapClient sqlMap, Map<String, Object> dbMap) throws SQLException {
		return this.pushDao.insertTmsQueue(sqlMap, dbMap);
		
	}
}
