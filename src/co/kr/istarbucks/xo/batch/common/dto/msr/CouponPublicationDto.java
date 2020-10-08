package co.kr.istarbucks.xo.batch.common.dto.msr;


/**
 * 비회원(준/웹) 리워드 쿠폰관련 Model
 */
public class CouponPublicationDto {

	private String di;								// 본인인증 고유값 DI
	private String userNumber;						// 사용자 번호
	private String transferUserNumber;				// 이관 사용자 번호
	private String orderNo;							// 사이렌오더 주문번호
	private String businessDate;					// 영업일{YYYYMMDD}
	private String oriBusinessDate;					// 원거래 영업일{YYYYMMDD}
	private String oriBranchCode;					// 원거래 점포코드
	private String oriPosNumber;					// 원거래 POS번호
	private String oriPosTrdNumber;					// 원거래 거래번호
	private String couponNumber;					// 쿠폰번호
	
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
