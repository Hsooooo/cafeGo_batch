package co.kr.istarbucks.xo.batch.main;

import java.util.HashMap;

public class DaemonLogDefine {
	
	private static final HashMap<String, String> scriptTable = new HashMap<String, String>(); 
//	private static final HashMap<String, String> serviceTable = new HashMap<String, String>();
	
	static {
		/*
		 * key=class ��
		 * value=���� ��ũ��Ʈ ��
		 */
		scriptTable.put("SMSClient", "SMSClient");
		
		/**
		 * �α� ���� ����
		 * key=���ϸ�, value=���񽺸�
		 */
		scriptTable.put("stderr-app1", "MSR APP S1");
		scriptTable.put("stderr-app2", "MSR APP S2");
		scriptTable.put("xo_error1", "XO APP S1");
		scriptTable.put("xo_error2", "XO APP S2");
		scriptTable.put("POSServer1", "XO POSServer1");
		scriptTable.put("POSServer2", "XO POSServer2");
		scriptTable.put("POSServerRelay", "XO POS RELAY");
		scriptTable.put("JeusServer1", "WEB MANGO2");
		scriptTable.put("JeusServer2", "WEB MANGO3");
		scriptTable.put("JeusServer3", "WEB MANGO5");
		scriptTable.put("JeusServer4", "WEB MANGO6");
	}
	
	public String getSh(String key) {		
		String sh = scriptTable.get(key);	
		
		return sh;
	}
}
