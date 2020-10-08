package co.kr.cafego.api.batch.main;

import org.apache.log4j.Logger;

import co.kr.cafego.api.batch.dao.WithdrawalDao;

/**
 * 탈퇴 유예기간 만료 배치
 * <p> 탈퇴 일자가 정책에 정해진 기간보다 클 경우 회원정보 삭제</p> 
 * @author Hsooooo
 *
 */
public class Withdrawal {
	private static final Logger logger = Logger.getLogger("WITHDRAWAL");
	
	private final WithdrawalDao dao;
	
	Withdrawal(){
		this.dao = new WithdrawalDao();
	}
	
	void run(String date) {
		logger.info("\n\n" + "========================================\n" +
                "Withdrawal Batch Start! date:" + date + "\n" +
                "========================================\n" );
	}
	
}
