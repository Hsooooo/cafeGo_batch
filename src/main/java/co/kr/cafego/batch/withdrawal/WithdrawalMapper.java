package co.kr.cafego.batch.withdrawal;

import java.util.List;

import co.kr.cafego.batch.common.dto.WithdrawalMemberDto;

public interface WithdrawalMapper {
	
	/**
	 * 탈퇴 기한 만료 회원 목록 조회
	 * @param today
	 * @return
	 */
	public List<WithdrawalMemberDto> getExpireWdMemberList(String today);
}
