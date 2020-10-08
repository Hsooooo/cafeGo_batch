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
	 * 탈퇴 유예기간 초과 회원정보 삭제
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public int withdrawalBatch(String date) throws SQLException {
		List<WithdrawalMemberDto> withdrawalMemberList = withdrawalDao.getWithdrawalMemberList(date);
		
		
		return 0;
		
	}
}
