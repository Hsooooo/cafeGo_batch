package co.kr.istarbucks.xo.batch.common.dto.msr;


/**
 * ��ȸ��(��/��) ������ �������� Model
 */
public class CouponPublicationDto {

	private String di;								// �������� ������ DI
	private String userNumber;						// ����� ��ȣ
	private String transferUserNumber;				// �̰� ����� ��ȣ
	private String orderNo;							// ���̷����� �ֹ���ȣ
	private String businessDate;					// ������{YYYYMMDD}
	private String oriBusinessDate;					// ���ŷ� ������{YYYYMMDD}
	private String oriBranchCode;					// ���ŷ� �����ڵ�
	private String oriPosNumber;					// ���ŷ� POS��ȣ
	private String oriPosTrdNumber;					// ���ŷ� �ŷ���ȣ
	private String couponNumber;					// ������ȣ
	
	/**
	 * @return the di
	 */
	public String getDi() {
		return di;
	}
	/**
	 * @param di the di to set
	 */
	public void setDi(String di) {
		this.di = di;
	}
	/**
	 * @return the userNumber
	 */
	public String getUserNumber() {
		return userNumber;
	}
	/**
	 * @param userNumber the userNumber to set
	 */
	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}
	/**
	 * @return the transferUserNumber
	 */
	public String getTransferUserNumber() {
		return transferUserNumber;
	}
	/**
	 * @param transferUserNumber the transferUserNumber to set
	 */
	public void setTransferUserNumber(String transferUserNumber) {
		this.transferUserNumber = transferUserNumber;
	}
	/**
	 * @return the orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * @return the businessDate
	 */
	public String getBusinessDate() {
		return businessDate;
	}
	/**
	 * @param businessDate the businessDate to set
	 */
	public void setBusinessDate(String businessDate) {
		this.businessDate = businessDate;
	}
	/**
	 * @return the oriBusinessDate
	 */
	public String getOriBusinessDate() {
		return oriBusinessDate;
	}
	/**
	 * @param oriBusinessDate the oriBusinessDate to set
	 */
	public void setOriBusinessDate(String oriBusinessDate) {
		this.oriBusinessDate = oriBusinessDate;
	}
	/**
	 * @return the oriBranchCode
	 */
	public String getOriBranchCode() {
		return oriBranchCode;
	}
	/**
	 * @param oriBranchCode the oriBranchCode to set
	 */
	public void setOriBranchCode(String oriBranchCode) {
		this.oriBranchCode = oriBranchCode;
	}
	/**
	 * @return the oriPosNumber
	 */
	public String getOriPosNumber() {
		return oriPosNumber;
	}
	/**
	 * @param oriPosNumber the oriPosNumber to set
	 */
	public void setOriPosNumber(String oriPosNumber) {
		this.oriPosNumber = oriPosNumber;
	}
	/**
	 * @return the oriPosTrdNumber
	 */
	public String getOriPosTrdNumber() {
		return oriPosTrdNumber;
	}
	/**
	 * @param oriPosTrdNumber the oriPosTrdNumber to set
	 */
	public void setOriPosTrdNumber(String oriPosTrdNumber) {
		this.oriPosTrdNumber = oriPosTrdNumber;
	}
	/**
	 * @return the couponNumber
	 */
	public String getCouponNumber() {
		return couponNumber;
	}
	/**
	 * @param couponNumber the couponNumber to set
	 */
	public void setCouponNumber(String couponNumber) {
		this.couponNumber = couponNumber;
	}
}
