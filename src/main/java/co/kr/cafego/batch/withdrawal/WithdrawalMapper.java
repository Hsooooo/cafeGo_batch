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

	/**
	 * 포인트 정보 삭제
	 * 	 * @param expireWdList
	 * @return
	 */
	public int delPointInfo(List<WithdrawalMemberDto> expireWdList);

	/**
	 * 회원 정보 삭제
	 * @param expireWdList
	 * @return
	 */
	public int delMemberInfo(List<WithdrawalMemberDto> expireWdList);
}
