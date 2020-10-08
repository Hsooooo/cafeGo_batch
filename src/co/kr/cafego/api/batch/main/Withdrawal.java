package co.kr.cafego.api.batch.main;

import org.apache.log4j.Logger;

import co.kr.cafego.api.batch.dao.WithdrawalDao;

/**
 * Ż�� �����Ⱓ ���� ��ġ
 * <p> Ż�� ���ڰ� ��å�� ������ �Ⱓ���� Ŭ ��� ȸ������ ����</p> 
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
