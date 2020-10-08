/*
 * @(#) $Id: SsgInfoMgr.java,v 1.2 2015/10/13 13:20:25 leeminjung Exp $
 * 
 * Starbucks XO
 * 
 * Copyright 2013 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.mgr;

import java.util.List;
import java.util.Map;

import co.kr.istarbucks.xo.batch.dao.SsgInfoDao;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * ������ ���� ����ȭ
 *
 * @author leeminjung
 * @version $Revision: 1.2 $
 */
public class SsgInfoMgr {

	private final SsgInfoDao ssgInfoDao;
	
	public SsgInfoMgr() {
		this.ssgInfoDao = new SsgInfoDao();
	}
	
	/**
	 * ��Ÿ���� ���� ���� ��ȸ
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getSbcInfo() throws Exception {
		return this.ssgInfoDao.getSbcInfo();
	}
	
	/**
	 * �ż��� ���� ���� ��ȸ
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getSsgInfo() throws Exception {
		return this.ssgInfoDao.getSsgInfo();
	}
	
	/**
	 * ���� ���� ��ü ����
	 * 
	 * @param sqlMap
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Integer deleteInfo(SqlMapClient sqlMap, Map<String, String> paramMap) throws Exception {
		return this.ssgInfoDao.deleteInfo(sqlMap, paramMap);
	}
	
	/**
	 * ���� ���� ����
	 * 
	 * @param sqlMap
	 * @param posInfoList
	 * @return
	 * @throws Exception
	 */
	public String insertInfo(SqlMapClient sqlMap, List<Map<String, String>> infoList) throws Exception {
		return this.ssgInfoDao.insertInfo(sqlMap, infoList);
	}
	
	/**
	 * ����� ������ ���� ���� ����
	 * 
	 * @return
	 * @throws Exception
	 */
	public Integer deleteUserEmpMapping () throws Exception {
		return this.ssgInfoDao.deleteUserEmpMapping();
	}
}
