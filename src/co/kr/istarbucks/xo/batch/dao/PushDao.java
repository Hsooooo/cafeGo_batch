/*
 * @(#) $Id: PushDao.java,v 1.2 2017/05/10 02:13:55 namgu1 Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */
package co.kr.istarbucks.xo.batch.dao;

import java.sql.SQLException;
import java.util.Map;

import co.kr.istarbucks.xo.batch.common.dto.push.ReservationPushDto;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * PUSH �߼�  PushDao.
 * 
 * @author eZEN ksy
 * @since 2016. 11. 03.
 * @version $Revision: 1.2 $
 */
public class PushDao {

	/**
	 * PUSH ��뷮 �߼� ���
	 * @param sqlMap
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int insertTblReservationPush(SqlMapClient sqlMap, ReservationPushDto params) throws SQLException {
		return (Integer) sqlMap.insert("push.insertTblReservationPush", params);
	}

	/**
	 * PUSH �ַ�� DB ���̺� TMS_PERSON_QUEUE(����ȭ ��뷮 : TPQ)�� ������ ����
	 * @param sqlMap
	 * @param dbMap
	 * @return
	 * @throws SQLException
	 */
	public boolean insertTmsPersonQueue(SqlMapClient sqlMap, Map<String, Object> dbMap) throws SQLException {
		String sqlId = "push.insertTmsPersonQueue";
		return sqlMap.update(sqlId, dbMap) == 1 ? true : false;
	}
	
	/**
	 * PUSH �߼� ���
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public boolean insertTmsQueue(SqlMapClient sqlMap, Map<String, Object> dbMap)  throws SQLException {
		String sqlId = "push.insertTmsQueue";
		return sqlMap.update(sqlId, dbMap) == 1 ? true : false;
	}
}
