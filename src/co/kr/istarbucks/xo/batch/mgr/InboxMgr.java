/*
 * @(#) $Id: InboxMgr.java,v 1.1 2017/08/07 09:02:38 shinepop Exp $
 * 
 * Starbucks Service
 * 
 * Copyright 2017³âµµ eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.mgr;

import com.ibatis.sqlmap.client.SqlMapClient;

import co.kr.istarbucks.xo.batch.common.dto.inbox.UserInboxDto;
import co.kr.istarbucks.xo.batch.dao.InboxDao;

public class InboxMgr { 
	
	private final InboxDao inboxDao;
	
	public InboxMgr () {
		this.inboxDao = new InboxDao();
	}
	
	public void insertUserInBoxMsg(SqlMapClient homeSqlMap, UserInboxDto dto) throws Exception {
		this.inboxDao.insertUserInBoxMsg(homeSqlMap, dto);
	}
}
