package co.kr.istarbucks.xo.batch.common.dto.xo;

public class MsrvInfrmMasterDto {

	/** 마이스타벅스리뷰 순번 */
	private long msrvSrnm;
	/** 마이스타벅스리뷰 제목명 */
	private String msrvTitleName;
	/** 마이스타벅스리뷰 구분코드 */
	private String msrvDvsnCode;
	/** 참여대상자제한여부 */
	private String prtcnTrprRstrcYn;
	/** 참여대상자제한인원수 */
	private long prtcnTrprRstrcPrcnt;
	/** 참여인원수(현재) */
	private long prtcnPrcnt;
	/** 참여인원수제한여부{Y:인원수over_불가능, N:인원수미만_가능} */
	private String prcntLimitYn;
	/** 성별코드{M:남성, F:여성, A:모두} */
	private String gnrCode;
	/** MSR등급 코드내역 */
	private String msrGradeCodeDtls;
	/** 출생년도적용여부 */
	private String birthYrwnAplctYn;
	/** 주문시간적용여부 */
	private String orderTmeAplctYn;
	/** 매장속성적용여부 */
	private String storeAtrbtAplctYn;
	/** 상품정보적용여부 */
	private String goodsInfrmAplctYn;
	/** 상품정보페어링여부 */
	private String goodsInfrmPrngYn;
	/** 파일럿여부 */
	private String pilotYn;
	/** 마이스타벅스리뷰 회원구분코드{N:일반대상자군, F:파일럿대상자군_타겟팅X, P:파일럿대상자군_타겟팅O} */
	private String msrvMbbrDvsnCode;
	/** 현재주문일{YYYYMMDDHH24MISS} */
	private String currOrderDate;
	/** 사이렌오더 주문번호 */
	private String orderNo;
	/** 사용자ID */
	private String userId;
	
	
	/**
	 * 마이스타벅스리뷰 순번
	 * @return
	 */
	public long getMsrvSrnm() {
		return msrvSrnm;
	}
	/**
	 * 마이스타벅스리뷰 순번
	 * @param msrvSrnm
	 */
	public void setMsrvSrnm(long msrvSrnm) {
		this.msrvSrnm = msrvSrnm;
	}
	
	/**
	 * 마이스타벅스리뷰 제목명
	 * @return
	 */
	public String getMsrvTitleName() {
		return msrvTitleName;
	}
	/**
	 * 마이스타벅스리뷰 제목명
	 * @param msrvTitleName
	 */
	public void setMsrvTitleName(String msrvTitleName) {
		this.msrvTitleName = msrvTitleName;
	}
	
	/**
	 * 마이스타벅스리뷰 구분코드
	 * @return
	 */
	public String getMsrvDvsnCode() {
		return msrvDvsnCode;
	}
	/**
	 * 마이스타벅스리뷰 구분코드
	 * @param msrvDvsnCode
	 */
	public void setMsrvDvsnCode(String msrvDvsnCode) {
		this.msrvDvsnCode = msrvDvsnCode;
	}
	
	/**
	 * 참여대상자제한여부
	 * @return
	 */
	public String getPrtcnTrprRstrcYn() {
		return prtcnTrprRstrcYn;
	}
	/**
	 * 참여대상자제한여부
	 * @param prtcnTrprRstrcYn
	 */
	public void setPrtcnTrprRstrcYn(String prtcnTrprRstrcYn) {
		this.prtcnTrprRstrcYn = prtcnTrprRstrcYn;
	}
	
	/**
	 * 참여대상자제한인원수
	 * @return
	 */
	public long getPrtcnTrprRstrcPrcnt() {
		return prtcnTrprRstrcPrcnt;
	}
	/**
	 * 참여대상자제한인원수
	 * @param prtcnTrprRstrcPrcnt
	 */
	public void setPrtcnTrprRstrcPrcnt(long prtcnTrprRstrcPrcnt) {
		this.prtcnTrprRstrcPrcnt = prtcnTrprRstrcPrcnt;
	}
	
	/**
	 * 참여인원수(현재)
	 * @return
	 */
	public long getPrtcnPrcnt() {
		return prtcnPrcnt;
	}
	/**
	 * 참여인원수(현재)
	 * @param prtcnPrcnt
	 */
	public void setPrtcnPrcnt(long prtcnPrcnt) {
		this.prtcnPrcnt = prtcnPrcnt;
	}
	
	/**
	 * 성별코드{M:남성, F:여성, A:모두}
	 * @return
	 */
	public String getGnrCode() {
		return gnrCode;
	}
	/**
	 * 성별코드{M:남성, F:여성, A:모두}
	 * @param gnrCode
	 */
	public void setGnrCode(String gnrCode) {
		this.gnrCode = gnrCode;
	}
	
	/**
	 * MSR등급 코드내역
	 * @return
	 */
	public String getMsrGradeCodeDtls() {
		return msrGradeCodeDtls;
	}
	/**
	 * MSR등급 코드내역
	 * @param msrGradeCodeDtls
	 */
	public void setMsrGradeCodeDtls(String msrGradeCodeDtls) {
		this.msrGradeCodeDtls = msrGradeCodeDtls;
	}
	
	/**
	 * 출생년도적용여부
	 * @return
	 */
	public String getBirthYrwnAplctYn() {
		return birthYrwnAplctYn;
	}
	/**
	 * 출생년도적용여부
	 * @param birthYrwnAplctYn
	 */
	public void setBirthYrwnAplctYn(String birthYrwnAplctYn) {
		this.birthYrwnAplctYn = birthYrwnAplctYn;
	}
	
	/**
	 * 주문시간적용여부
	 * @return
	 */
	public String getOrderTmeAplctYn() {
		return orderTmeAplctYn;
	}
	/**
	 * 주문시간적용여부
	 * @param orderTmeAplctYn
	 */
	public void setOrderTmeAplctYn(String orderTmeAplctYn) {
		this.orderTmeAplctYn = orderTmeAplctYn;
	}
	
	/**
	 * 매장속성적용여부
	 * @return
	 */
	public String getStoreAtrbtAplctYn() {
		return storeAtrbtAplctYn;
	}
	/**
	 * 매장속성적용여부
	 * @param storeAtrbtAplctYn
	 */
	public void setStoreAtrbtAplctYn(String storeAtrbtAplctYn) {
		this.storeAtrbtAplctYn = storeAtrbtAplctYn;
	}
	
	/**
	 * 상품정보적용여부
	 * @return
	 */
	public String getGoodsInfrmAplctYn() {
		return goodsInfrmAplctYn;
	}
	/**
	 * 상품정보적용여부
	 * @param goodsInfrmAplctYn
	 */
	public void setGoodsInfrmAplctYn(String goodsInfrmAplctYn) {
		this.goodsInfrmAplctYn = goodsInfrmAplctYn;
	}
	
	/**
	 * 상품정보페어링여부
	 * @return
	 */
	public String getGoodsInfrmPrngYn() {
		return goodsInfrmPrngYn;
	}
	/**
	 * 상품정보페어링여부
	 * @param goodsInfrmPrngYn
	 */
	public void setGoodsInfrmPrngYn(String goodsInfrmPrngYn) {
		this.goodsInfrmPrngYn = goodsInfrmPrngYn;
	}
	
	/**
	 * 파일럿여부
	 * @return
	 */
	public String getPilotYn() {
		return pilotYn;
	}
	/**
	 * 파일럿여부
	 * @param pilotYn
	 */
	public void setPilotYn(String pilotYn) {
		this.pilotYn = pilotYn;
	}
	
	/**
	 * 마이스타벅스리뷰 회원구분코드
	 * 	{N:일반대상자군, F:파일럿대상자군_타겟팅X, P:파일럿대상자군_타겟팅O}
	 * @return
	 */
	public String getMsrvMbbrDvsnCode() {
		return msrvMbbrDvsnCode;
	}
	/**
	 * 마이스타벅스리뷰 회원구분코드
	 * 	{N:일반대상자군, F:파일럿대상자군_타겟팅X, P:파일럿대상자군_타겟팅O}
	 * @param msrvMbbrDvsnCode
	 */
	public void setMsrvMbbrDvsnCode(String msrvMbbrDvsnCode) {
		this.msrvMbbrDvsnCode = msrvMbbrDvsnCode;
	}
	
	/**
	 * 현재주문일{YYYYMMDDHH24MISS}
	 * @return
	 */
	public String getCurrOrderDate() {
		return currOrderDate;
	}
	/**
	 * 현재주문일{YYYYMMDDHH24MISS}
	 * @param currOrderDate
	 */
	public void setCurrOrderDate(String currOrderDate) {
		this.currOrderDate = currOrderDate;
	}
	
	/**
	 * 참여인원수제한여부{Y:인원수over_불가능, N:인원수미만_가능}
	 * @return
	 */
	public String getPrcntLimitYn() {
		return prcntLimitYn;
	}
	/**
	 * 참여인원수제한여부{Y:인원수over_불가능, N:인원수미만_가능}
	 * @param prcntLimitYn
	 */
	public void setPrcntLimitYn(String prcntLimitYn) {
		this.prcntLimitYn = prcntLimitYn;
	}
	
	/**
	 * 사이렌오더 주문번호
	 * @return
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * 사이렌오더 주문번호
	 * @param orderNo
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * 사용자ID
	 * @return
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 사용자ID
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	@Override
	public String toString() {
		return "MsrvInfrmMasterDto [orderNo=" + orderNo + ", msrvSrnm=" + msrvSrnm
				+ ", msrvDvsnCode=" + msrvDvsnCode + ", prtcnTrprRstrcYn="
				+ prtcnTrprRstrcYn + ", prtcnTrprRstrcPrcnt=" + prtcnTrprRstrcPrcnt + ", prtcnPrcnt=" + prtcnPrcnt
				+ ", prcntLimitYn=" + prcntLimitYn + ", gnrCode=" + gnrCode + ", msrGradeCodeDtls=" + msrGradeCodeDtls
				+ ", birthYrwnAplctYn=" + birthYrwnAplctYn + ", orderTmeAplctYn=" + orderTmeAplctYn
				+ ", storeAtrbtAplctYn=" + storeAtrbtAplctYn + ", goodsInfrmAplctYn=" + goodsInfrmAplctYn
				+ ", goodsInfrmPrngYn=" + goodsInfrmPrngYn + ", pilotYn=" + pilotYn + ", msrvMbbrDvsnCode="
				+ msrvMbbrDvsnCode + ", currOrderDate=" + currOrderDate + "]";
	}
}
