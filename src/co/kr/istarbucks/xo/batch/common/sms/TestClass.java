package co.kr.istarbucks.xo.batch.common.sms;

import co.kr.istarbucks.xo.batch.main.DaemonLog;

public class TestClass {
	
	private DaemonLog daemonLog;
	
	public TestClass() {
		daemonLog = new DaemonLog("TestClass"); //자기 자신의 class 명
	}
	
	public void TestStart() {
		
		daemonLog.setStartLog();	//데몬/배치의 시작 로그 생성
		try{
			/**
			 * 
			 * 데몬/배치 로직 
			 * 
			 */
			
		} catch(Exception e) {
			daemonLog.setErrorLogAndSMS(e, true);	//데몬/배치의 에러 로그 및 SMS 생성. false 일 때, SMS는 발송하지 않음.
		}
		
		daemonLog.setEndLogAndSMS(true);	//데몬/배치의 종료 로그 및 SMS 생성. false 일 때, SMS는 발송하지 않음.
	}

}
