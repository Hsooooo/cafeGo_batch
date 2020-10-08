/*
 * @(#) $Id: ReserveSendPostNoticeXoDao.java,v 1.1 2017/08/07 09:02:39 shinepop Exp $
 * 
 * Starbucks Service
 * 
 * Copyright 2017³âµµ eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.dao;

import java.util.List;
import java.util.Map;

import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;

import com.ibatis.sqlmap.client.SqlMapClient;

public class ReserveSendPostNoticeXoDao { 
	
	@SuppressWarnings ( "unchecked" )
	public List<Map<String, String>> getReserveSendPostNoticeTarget(Map<String, Object> dbMap) throws Exception {
		String sqlId = "reserveSendNoticeXo.getReserveSendPostNoticeTarget";
		SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return xoSqlMap.queryForList (sqlId, dbMap);
	}
}
