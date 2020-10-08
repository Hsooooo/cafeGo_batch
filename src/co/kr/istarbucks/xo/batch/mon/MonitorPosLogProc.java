package co.kr.istarbucks.xo.batch.mon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import co.kr.istarbucks.xo.batch.common.sms.SMSClient;
import co.kr.istarbucks.xo.batch.mgr.MonitorLogMgr;
import co.kr.istarbucks.xo.batch.main.DaemonLogDefine;

public class MonitorPosLogProc implements Runnable {
	private static Log logger = LogFactory.getLog("MONITOR");
	
	private final MonitorLogMgr mlm;
	private final SMSClient smsc;
	private final DaemonLogDefine define;
	
	private final String args;
	private final String path;
	private final String posErrMsg;
	
	public MonitorPosLogProc(String args, String path, String posMsg) {
		
		mlm		= new MonitorLogMgr();
		smsc	= new SMSClient();
		define	= new DaemonLogDefine();
		
		this.args = args;
		this.path = path + args;
		this.posErrMsg = posMsg;
	}
	
	public void run() {
		BufferedReader br = null;
		String msgList = null;
		String msg = null;
		boolean sms 		= false;
		
		Map<String, String> param = new HashMap<String, String>();
		logger.info("["+Thread.currentThread().getName()+"] Monitor POSLog Thread Start ======================");
		
		try {
			br = new BufferedReader(new FileReader(FileUtils.getFile(new String[]{this.path})));

			String[] ss = null;
			String[] s1 = null;
			String errDate = null;
			String errTime = null;
			msgList=br.readLine();
			while ( msgList != null ) {
				
				String result = null;
				ss = msgList.split("\\(");
				
				if (ss[0].indexOf("ERROR") > 0) {
					s1 = msgList.split(" ");
					errDate = s1[0];
					errTime = s1[1];
				}
				
				if (msgList.indexOf("SQLException") > 0) {
					if (msgList.indexOf(".SQLException") > 0) {
						result ="["+errDate+" "+errTime+"] " + msgList;
					}
				}else {
					if (ss[0].indexOf("ERROR") > -1) {
						result= null;
					}else {
						result ="["+errDate+" "+errTime+"] " + msgList;
					}
				}
				
				if (result != null && result.length() > 0) {
					Map<String, String> pmap = getParam(result);
					
					param.put("logFile_name", this.args);
					param.put("service_name", define.getSh(this.args));
					param.put("error_date", pmap.get("errDate"));
					param.put("log_desc", pmap.get("errDesc"));
					
					logger.info("["+Thread.currentThread().getName()+"] DB INSERT Param : logFile_name=" +this.args
							+", service_name="+param.get("service_name")+", error_date="+param.get("error_date"));
					
					try {
						mlm.insertMonitorLog(param);
						logger.info("["+Thread.currentThread().getName()+"] DB INSERT END");
					} catch (Exception e) {
						logger.error(e, e);
					}
					
					//error sms ��ȿ�� �˻�
//					sms = filterErrMsg(args, pmap.get("errDesc"));
					sms = filterErrMsg(pmap.get("errDesc"));
					logger.info("["+Thread.currentThread().getName()+"] SMS SEND STATUS = " + sms);
					
					//sms �߼�. false �� �� �߼�
					if (!sms) {
						//���� SMS ���� �˻�. ������ �޼����� 10 �� ���� �߼����� �ʴ´�.
						if (getSendSMSStatus(pmap.get("errDesc"))) {
				        	smsc.sendErrorSMS(define.getSh(this.args), pmap.get("errDesc"));
				        	logger.info("["+Thread.currentThread().getName()+"] SMS SEND END");
						}
					}
					
					param.clear();
				}
				msgList=br.readLine();
			}
			logger.info("["+Thread.currentThread().getName()+"] Monitor POSLog Thread End ======================");
		} catch (IOException ioe) {
			logger.error(ioe.getMessage(), ioe);
		} finally {
			try {
				br.close();
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
		}
	}

	/**
	 * �α� �Ľ�
	 * ���Ͽ��� �о� �鿩�� �α׸� �Ľ��Ѵ�.
	 * @param args
	 * @return
	 */
	private Map<String, String> getParam(String args) {
		
		Map<String, String> paramMap = new HashMap<String, String>();
		
		String paramDate = null;
		String paramMsg = null;
		String[] paramList = null;
		
		paramList = args.split("]");
		
		paramDate = paramList[0].replace("[", "");
		paramMsg = paramList[1];
		
		if (paramDate != null && paramDate.length() > 0) {
			if (paramDate.indexOf(".") > 0) {
				paramMap.put("errDate", paramDate.trim().substring(0, paramDate.lastIndexOf(".")));
			} else {
				paramMap.put("errDate", paramDate);
			}
			
		}
		if (paramMsg != null && paramMsg.length() > 0) {
			paramMap.put("errDesc", paramMsg);
		}
		
		return paramMap;
		
	}
	
	/**
	 * SMS ���� ����
	 * ������ �ð� ���� �޼����� Ȧ�� ��Ŵ.
	 * @param param
	 * @return
	 */
	private boolean getSendSMSStatus (String param) {
		String strRet 	= null;
		boolean result	= false;
		
		Hashtable htRet = Filter.getInstance().filtering(param.trim());
		
		for(int ii = 0;ii < htRet.size();ii++) {
		    Enumeration tKey  = htRet.keys();
		    
		    while(tKey.hasMoreElements()) {
		        strRet = (String)tKey.nextElement();
		        
		        if (strRet != null && strRet.length() > 0) {
		        	result = true;
		        }
		        
		        try {
		        	Thread.sleep(1000);
		        } catch (Exception e) {
		        	logger.error(e.getMessage(), e);
		        }
		    }
		}
		
		return result;
	}
	
	/**
	 * ERROR SMS ��ȿ�� �˻�
	 * 
	 * @param args
	 * @param errMsg
	 * @return
	 */
//	private boolean filterErrMsg(String args, String errMsg) {
	private boolean filterErrMsg(String errMsg) {
		boolean result = false;
		
		String[] paramList = this.posErrMsg.split("\\|");		
				
		for (int i=0; i < paramList.length; i++) {			
			if(errMsg.toUpperCase().indexOf(paramList[i].trim()) > -1) {
				result = true;
			}			
		}
		
		return result;
	}

}