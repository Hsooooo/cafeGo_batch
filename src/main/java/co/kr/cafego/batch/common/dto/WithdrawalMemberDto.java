package co.kr.cafego.batch.common.dto;

public class WithdrawalMemberDto {
	
	/** ȸ�� ��ȣ */
	private String memberNum;
	/** Ż�� ���� */
	private String wdDesc;
	/** Ż����(YYYYMMDD) */
	private String wdDate;
	
	/**
	 * ȸ�� ��ȣ
	 * @return
	 */
	public String getMemberNum() {
		return memberNum;
	}
	/**
	 * ȸ�� ��ȣ
	 * @param memberNum
	 */
	public void setMemberNum(String memberNum) {
		this.memberNum = memberNum;
	}
	/**
	 * Ż�� ����
	 * @return
	 */
	public String getWdDesc() {
		return wdDesc;
	}
	/**
	 * Ż�� ����
	 * @param wdDesc
	 */
	public void setWdDesc(String wdDesc) {
		this.wdDesc = wdDesc;
	}
	/**
	 * Ż����
	 * @return
	 */
	public String getWdDate() {
		return wdDate;
	}
	/**
	 * Ż����
	 * @param wdDate
	 */
	public void setWdDate(String wdDate) {
		this.wdDate = wdDate;
	}
	
	
}
