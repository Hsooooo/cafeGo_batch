package co.kr.cafego.common.util;

import java.util.Random;

import org.springframework.core.env.Environment;

public class AppFunction {
	
	private final Environment dConf;
	
	public AppFunction (Environment conf) {
		this.dConf = conf;
	}
	
	
	/**
	 * 회원 카드 번호 채번(DB 중복체크 필요)
	 * @return
	 */
	public static String getMemberCardNum() {
		String cardNum = "";
		
		Random rand = new Random();
		
		for(int i=0; i<16; i++) {
			String ranNum = Integer.toString(rand.nextInt(10));
			
			cardNum += ranNum;
		}
		
		return cardNum;
	}
	

	public Environment getDConf() {
		return dConf;
	}
	
}
