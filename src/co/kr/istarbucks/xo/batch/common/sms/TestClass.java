package co.kr.istarbucks.xo.batch.common.sms;

import co.kr.istarbucks.xo.batch.main.DaemonLog;

public class TestClass {
	
	private DaemonLog daemonLog;
	
	public TestClass() {
		daemonLog = new DaemonLog("TestClass"); //�ڱ� �ڽ��� class ��
	}
	
	public void TestStart() {
		
		daemonLog.setStartLog();	//����/��ġ�� ���� �α� ����
		try{
			/**
			 * 
			 * ����/��ġ ���� 
			 * 
			 */
			
		} catch(Exception e) {
			daemonLog.setErrorLogAndSMS(e, true);	//����/��ġ�� ���� �α� �� SMS ����. false �� ��, SMS�� �߼����� ����.
		}
		
		daemonLog.setEndLogAndSMS(true);	//����/��ġ�� ���� �α� �� SMS ����. false �� ��, SMS�� �߼����� ����.
	}

}
