package co.kr.cafego.api.batch.common.dto;

public class WithdrawalMemberDto {
	
	/** È¸¿ø ¹øÈ£ */
	private String memberNum;
	/** Å»Åğ »çÀ¯ */
	private String wdDesc;
	/** Å»ÅğÀÏ(YYYYMMDD) */
	private String wdDate;
	
	/**
	 * È¸¿ø ¹øÈ£
	 * @return
	 */
	public String getMemberNum() {
		return memberNum;
	}
	/**
	 * È¸¿ø ¹øÈ£
	 * @param memberNum
	 */
	public void setMemberNum(String memberNum) {
		this.memberNum = memberNum;
	}
	/**
	 * Å»Åğ »çÀ¯
	 * @return
	 */
	public String getWdDesc() {
		return wdDesc;
	}
	/**
	 * Å»Åğ »çÀ¯
	 * @param wdDesc
	 */
	public void setWdDesc(String wdDesc) {
		this.wdDesc = wdDesc;
	}
	/**
	 * Å»ÅğÀÏ
	 * @return
	 */
	public String getWdDate() {
		return wdDate;
	}
	/**
	 * Å»ÅğÀÏ
	 * @param wdDate
	 */
	public void setWdDate(String wdDate) {
		this.wdDate = wdDate;
	}
	
	
}
