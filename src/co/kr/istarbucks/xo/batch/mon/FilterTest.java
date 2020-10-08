package co.kr.istarbucks.xo.batch.mon;

import java.util.Enumeration;
import java.util.Hashtable;

public class FilterTest {
	public static void main(String[] args) {
		String strRet = "";
		String msg="test11";
		Hashtable htRet = Filter.getInstance().filtering(msg.toString().trim());
									
		for(int ii = 0;ii < htRet.size();ii++) {
		    Enumeration tKey  = htRet.keys();
		    while(tKey.hasMoreElements()) {
		        strRet = (String)tKey.nextElement();
		        //Filter Debug
		        //System.out.println("Filter Debug:" + "[" + getProperty(lf.agentName) + "]" + strRet);
		        /*
		         * SMS List 보내기
		         * */
		        System.out.println("strRet==" + strRet);
		        try {
		        Thread.sleep(1000);
		        } catch (Exception e) {
		        	e.printStackTrace();
		        }
		    }// SMS 대상 리스트
		} //CONTAINER Check
	} //Container For
			                     

}
