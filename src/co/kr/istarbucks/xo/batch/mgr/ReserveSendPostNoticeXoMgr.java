/*
 * @(#) $Id: ReserveSendPostNoticeXoMgr.java,v 1.1 2017/08/07 09:02:38 shinepop Exp $
 * 
 * Starbucks Service
 * 
 * Copyright 2017³âµµ eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.mgr;

import java.util.List;
import java.util.Map;

import co.kr.istarbucks.xo.batch.dao.ReserveSendPostNoticeXoDao;

public class ReserveSendPostNoticeXoMgr { 
	
	private final ReserveSendPostNoticeXoDao reserveSendPostNoticeXoDao;
	
	public ReserveSendPostNoticeXoMgr(){
		this.reserveSendPostNoticeXoDao = new ReserveSendPostNoticeXoDao();
	}

	public List<Map<String, String>> getReserveSendPostNoticeTarget(Map<String, Object> dbMap) throws Exception {
		return reserveSendPostNoticeXoDao.getReserveSendPostNoticeTarget(dbMap);
	}
}
