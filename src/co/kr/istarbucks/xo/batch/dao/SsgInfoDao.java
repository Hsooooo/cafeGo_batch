/*
 * @(#) $Id: SsgInfoDao.java,v 1.2 2015/10/13 13:20:25 leeminjung Exp $
 * Starbucks XO
 * Copyright 2013 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.dao;

import java.util.List;
import java.util.Map;

import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 임직원 정보 동기화
 * 
 * @author leeminjung
 * @version $Revision: 1.2 $
 */
public class SsgInfoDao {
	
	/**
	 * 스타벅스 직원 정보 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings ( "unchecked" )
	public List<Map<String, String>> getSbcInfo () throws Exception {
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return (List<Map<String, String>>) sqlMap.queryForList ("ssg.getSbcInfo");
	}
	
	/**
	 * 신세계 직원 정보 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings ( "unchecked" )
	public List<Map<String, String>> getSsgInfo () throws Exception {
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return (List<Map<String, String>>) sqlMap.queryForList ("ssg.getSsgInfo");
	}
	
	/**
	 * 직원 정보 삭제
	 * 
	 * @param sqlMap
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Integer deleteInfo ( SqlMapClient sqlMap, Map<String, String> paramMap ) throws Exception {
		return sqlMap.delete ("ssg.deleteInfo", paramMap);
	}
	
	/**
	 * 직원 정보 저장
	 * 
	 * @param sqlMap
	 * @param posInfoList
	 * @return
	 * @throws Exception
	 */
	public String insertInfo ( SqlMapClient sqlMap, List<Map<String, String>> infoList ) throws Exception {
		int count = 0;
		int failCount = 0;
		
		for ( Map<String, String> info : infoList ) {
			if ( info.get ("ssg_cd") != null && info.get ("emp_num") != null ) {
				count += (Integer) sqlMap.update ("ssg.insertInfo", info);
			} else {
				failCount += 1;
			}
		}
		StringBuffer buf = new StringBuffer ();
		buf.append (count).append (",").append (failCount);
		return buf.toString ();
	}
	
	/**
	 * 사용자 임직원 매핑 정보 삭제
	 * 
	 * @return
	 * @throws Exception
	 */
	public Integer deleteUserEmpMapping () throws Exception {
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.delete ("ssg.deleteUserEmpMappingInfo");
	}
}
