/*
 * @(#) $Id: SsgInfo.java,v 1.3 2015/10/13 13:40:11 leeminjung Exp $
 * 
 * Starbucks XO
 * 
 * Copyright 2013 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;
import co.kr.istarbucks.xo.batch.mgr.SsgInfoMgr;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * ������ ���� ����ȭ
 *
 * @author leeminjung
 * @version $Revision: 1.3 $
 */
public class SsgInfo {

	private final Log logger = LogFactory.getLog("SSGINFO");
	
	private final SsgInfoMgr infoMgr;
	private List<Map<String, String>> sbcInfoList;
	private List<Map<String, String>> ssgInfoList;
	private final String loggerTitle;
	private final StringBuffer logSb;
	
	public SsgInfo () {
		this.infoMgr = new SsgInfoMgr ();
		this.loggerTitle = "[ssginfo] ";
		this.logSb = new StringBuffer(); // log�� StringBuffer
	}
	
	public void start() {
		long startTime = System.currentTimeMillis ();
		
		logSb.delete(0, logSb.length()).append(" ").append(this.loggerTitle).append("START");
		logger.info(logSb.toString());
		
		// ��Ÿ���� ���� ���� ����ȭ
		syncSbcInfo();
		
		// �ż��� ���� ���� ����ȭ
		syncSsgInfo();
		
		// ����� ������ ���� ���� ����ȭ
		syncUserEmpMapping();
		
		logSb.delete(0, logSb.length()).append(" ").append(this.loggerTitle).append("END : ").append(System.currentTimeMillis () - startTime).append("ms");
		logger.info(logSb.toString());
	}
	
	/**
	 * ��Ÿ���� ���� ���� ����ȭ
	 */
	private void syncSbcInfo() {
		
		// ��Ÿ���� ���� ���� ��ȸ
		try {
			this.sbcInfoList = this.infoMgr.getSbcInfo();

			logSb.delete(0, logSb.length()).append(" ").append(this.loggerTitle).append("[SBC] Get : ").append(sbcInfoList.size()).append("��");
			logger.info(logSb.toString());
			
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		// ��ȸ�� ������ ������ ó�� ����
		if(this.sbcInfoList != null && this.sbcInfoList.size() > 0) {
			SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
			Map<String, String> paramMap = new HashMap<String, String>();
			
			try {
				sqlMap.startTransaction();
				
				// ������ ����
				paramMap.put("sbc_flag", "Y");
				int deleteCount = this.infoMgr.deleteInfo(sqlMap, paramMap);
				
				logSb.delete(0, logSb.length()).append(" ").append(this.loggerTitle).append("[SBC] Delete : ").append(deleteCount).append("��");
				logger.info(logSb.toString());
				
				// ������ ����
				String insertResult = this.infoMgr.insertInfo(sqlMap, this.sbcInfoList);
				String[] count = insertResult.split(",");				
				
				logSb.delete(0, logSb.length()).append(this.loggerTitle).append("[SBC] Insert : ").append(count[0]).append("��, Fail : ").append(count[1]).append("��");
				logger.info(logSb.toString());
				
				// commit
				sqlMap.commitTransaction();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				paramMap.clear();
				
				try {
					sqlMap.endTransaction();
				} catch (Exception ee) {
					logger.error(ee.getMessage(), ee);
				}
			}
		}
	}
	
	/**
	 * �ż��� ���� ���� ����ȭ
	 */
	private void syncSsgInfo() {
		
		// �ż��� ���� ���� ��ȸ
		try {
			this.ssgInfoList = this.infoMgr.getSsgInfo();

			logSb.delete(0, logSb.length()).append(this.loggerTitle).append("[SSG] Get : ").append(ssgInfoList.size()).append("��");
			logger.info(logSb.toString());
			
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		// ��ȸ�� ������ ������ ó�� ����
		if(this.ssgInfoList != null && this.ssgInfoList.size() > 0) {
			SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
			Map<String, String> paramMap = new HashMap<String, String>();
			
			try {
				sqlMap.startTransaction();
				
				// ������ ����
				paramMap.put("sbc_flag", "N");
				int deleteCount = this.infoMgr.deleteInfo(sqlMap, paramMap);
				
				logSb.delete(0, logSb.length()).append(this.loggerTitle).append("[SSG] Delete : ").append(deleteCount).append("��");
				logger.info(logSb.toString());
				
				// ������ ����
				String insertResult = this.infoMgr.insertInfo(sqlMap, this.ssgInfoList);
				String[] count = insertResult.split(",");				
				
				logSb.delete(0, logSb.length()).append(this.loggerTitle).append("[SSG] Insert : ").append(count[0]).append("��, Fail : ").append(count[1]).append("��");
				logger.info(logSb.toString());
				
				// commit
				sqlMap.commitTransaction();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				paramMap.clear();
				try {
					sqlMap.endTransaction();
				} catch (Exception ee) {
					logger.error(ee.getMessage(), ee);
				}
			}
		}
	}
	
	/**
	 * ����� ������ ���� ���� ����ȭ
	 */
	private void syncUserEmpMapping() {
		try {
			int deleteCount = this.infoMgr.deleteUserEmpMapping();
			
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append("[UserEmpMapping] Delete : ").append(deleteCount).append("��");
			logger.info(logSb.toString());
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public static void main(String[] args) {
		SsgInfo info = new SsgInfo();
		info.start();
	}
}
