package co.kr.cafego.api.batch.mgr;

import java.sql.SQLException;
import java.util.List;

import co.kr.cafego.api.batch.common.dto.WithdrawalMemberDto;
import co.kr.cafego.api.batch.dao.WithdrawalDao;

public class WithdrawalMgr {
	private final WithdrawalDao withdrawalDao;
	
	public WithdrawalMgr() {
		this.withdrawalDao = new WithdrawalDao();
	}
	
	
	/**
	 * Ż�� �����Ⱓ �ʰ� ȸ������ ����
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public String withdrawalBatch(String date) throws SQLException {
		StringBuffer logSb = new StringBuffer();
		List<WithdrawalMemberDto> withdrawalMemberList = withdrawalDao.getWithdrawalMemberList(date);
		
		logSb.append("[").append(date).append("]").append("[Ż����Ѹ��� ȸ�� ��][")
		.append(withdrawalMemberList.size()).append("]");
		
		
		
		return 0;
		
	}
}
