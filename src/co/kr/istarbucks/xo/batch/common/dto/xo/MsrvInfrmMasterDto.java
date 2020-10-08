package co.kr.istarbucks.xo.batch.common.dto.xo;

public class MsrvInfrmMasterDto {

	/** ���̽�Ÿ�������� ���� */
	private long msrvSrnm;
	/** ���̽�Ÿ�������� ����� */
	private String msrvTitleName;
	/** ���̽�Ÿ�������� �����ڵ� */
	private String msrvDvsnCode;
	/** ������������ѿ��� */
	private String prtcnTrprRstrcYn;
	/** ��������������ο��� */
	private long prtcnTrprRstrcPrcnt;
	/** �����ο���(����) */
	private long prtcnPrcnt;
	/** �����ο������ѿ���{Y:�ο���over_�Ұ���, N:�ο����̸�_����} */
	private String prcntLimitYn;
	/** �����ڵ�{M:����, F:����, A:���} */
	private String gnrCode;
	/** MSR��� �ڵ峻�� */
	private String msrGradeCodeDtls;
	/** ����⵵���뿩�� */
	private String birthYrwnAplctYn;
	/** �ֹ��ð����뿩�� */
	private String orderTmeAplctYn;
	/** ����Ӽ����뿩�� */
	private String storeAtrbtAplctYn;
	/** ��ǰ�������뿩�� */
	private String goodsInfrmAplctYn;
	/** ��ǰ���������� */
	private String goodsInfrmPrngYn;
	/** ���Ϸ����� */
	private String pilotYn;
	/** ���̽�Ÿ�������� ȸ�������ڵ�{N:�Ϲݴ���ڱ�, F:���Ϸ�����ڱ�_Ÿ����X, P:���Ϸ�����ڱ�_Ÿ����O} */
	private String msrvMbbrDvsnCode;
	/** �����ֹ���{YYYYMMDDHH24MISS} */
	private String currOrderDate;
	/** ���̷����� �ֹ���ȣ */
	private String orderNo;
	/** �����ID */
	private String userId;
	
	
	/**
	 * ���̽�Ÿ�������� ����
	 * @return
	 */
	public long getMsrvSrnm() {
		return msrvSrnm;
	}
	/**
	 * ���̽�Ÿ�������� ����
	 * @param msrvSrnm
	 */
	public void setMsrvSrnm(long msrvSrnm) {
		this.msrvSrnm = msrvSrnm;
	}
	
	/**
	 * ���̽�Ÿ�������� �����
	 * @return
	 */
	public String getMsrvTitleName() {
		return msrvTitleName;
	}
	/**
	 * ���̽�Ÿ�������� �����
	 * @param msrvTitleName
	 */
	public void setMsrvTitleName(String msrvTitleName) {
		this.msrvTitleName = msrvTitleName;
	}
	
	/**
	 * ���̽�Ÿ�������� �����ڵ�
	 * @return
	 */
	public String getMsrvDvsnCode() {
		return msrvDvsnCode;
	}
	/**
	 * ���̽�Ÿ�������� �����ڵ�
	 * @param msrvDvsnCode
	 */
	public void setMsrvDvsnCode(String msrvDvsnCode) {
		this.msrvDvsnCode = msrvDvsnCode;
	}
	
	/**
	 * ������������ѿ���
	 * @return
	 */
	public String getPrtcnTrprRstrcYn() {
		return prtcnTrprRstrcYn;
	}
	/**
	 * ������������ѿ���
	 * @param prtcnTrprRstrcYn
	 */
	public void setPrtcnTrprRstrcYn(String prtcnTrprRstrcYn) {
		this.prtcnTrprRstrcYn = prtcnTrprRstrcYn;
	}
	
	/**
	 * ��������������ο���
	 * @return
	 */
	public long getPrtcnTrprRstrcPrcnt() {
		return prtcnTrprRstrcPrcnt;
	}
	/**
	 * ��������������ο���
	 * @param prtcnTrprRstrcPrcnt
	 */
	public void setPrtcnTrprRstrcPrcnt(long prtcnTrprRstrcPrcnt) {
		this.prtcnTrprRstrcPrcnt = prtcnTrprRstrcPrcnt;
	}
	
	/**
	 * �����ο���(����)
	 * @return
	 */
	public long getPrtcnPrcnt() {
		return prtcnPrcnt;
	}
	/**
	 * �����ο���(����)
	 * @param prtcnPrcnt
	 */
	public void setPrtcnPrcnt(long prtcnPrcnt) {
		this.prtcnPrcnt = prtcnPrcnt;
	}
	
	/**
	 * �����ڵ�{M:����, F:����, A:���}
	 * @return
	 */
	public String getGnrCode() {
		return gnrCode;
	}
	/**
	 * �����ڵ�{M:����, F:����, A:���}
	 * @param gnrCode
	 */
	public void setGnrCode(String gnrCode) {
		this.gnrCode = gnrCode;
	}
	
	/**
	 * MSR��� �ڵ峻��
	 * @return
	 */
	public String getMsrGradeCodeDtls() {
		return msrGradeCodeDtls;
	}
	/**
	 * MSR��� �ڵ峻��
	 * @param msrGradeCodeDtls
	 */
	public void setMsrGradeCodeDtls(String msrGradeCodeDtls) {
		this.msrGradeCodeDtls = msrGradeCodeDtls;
	}
	
	/**
	 * ����⵵���뿩��
	 * @return
	 */
	public String getBirthYrwnAplctYn() {
		return birthYrwnAplctYn;
	}
	/**
	 * ����⵵���뿩��
	 * @param birthYrwnAplctYn
	 */
	public void setBirthYrwnAplctYn(String birthYrwnAplctYn) {
		this.birthYrwnAplctYn = birthYrwnAplctYn;
	}
	
	/**
	 * �ֹ��ð����뿩��
	 * @return
	 */
	public String getOrderTmeAplctYn() {
		return orderTmeAplctYn;
	}
	/**
	 * �ֹ��ð����뿩��
	 * @param orderTmeAplctYn
	 */
	public void setOrderTmeAplctYn(String orderTmeAplctYn) {
		this.orderTmeAplctYn = orderTmeAplctYn;
	}
	
	/**
	 * ����Ӽ����뿩��
	 * @return
	 */
	public String getStoreAtrbtAplctYn() {
		return storeAtrbtAplctYn;
	}
	/**
	 * ����Ӽ����뿩��
	 * @param storeAtrbtAplctYn
	 */
	public void setStoreAtrbtAplctYn(String storeAtrbtAplctYn) {
		this.storeAtrbtAplctYn = storeAtrbtAplctYn;
	}
	
	/**
	 * ��ǰ�������뿩��
	 * @return
	 */
	public String getGoodsInfrmAplctYn() {
		return goodsInfrmAplctYn;
	}
	/**
	 * ��ǰ�������뿩��
	 * @param goodsInfrmAplctYn
	 */
	public void setGoodsInfrmAplctYn(String goodsInfrmAplctYn) {
		this.goodsInfrmAplctYn = goodsInfrmAplctYn;
	}
	
	/**
	 * ��ǰ����������
	 * @return
	 */
	public String getGoodsInfrmPrngYn() {
		return goodsInfrmPrngYn;
	}
	/**
	 * ��ǰ����������
	 * @param goodsInfrmPrngYn
	 */
	public void setGoodsInfrmPrngYn(String goodsInfrmPrngYn) {
		this.goodsInfrmPrngYn = goodsInfrmPrngYn;
	}
	
	/**
	 * ���Ϸ�����
	 * @return
	 */
	public String getPilotYn() {
		return pilotYn;
	}
	/**
	 * ���Ϸ�����
	 * @param pilotYn
	 */
	public void setPilotYn(String pilotYn) {
		this.pilotYn = pilotYn;
	}
	
	/**
	 * ���̽�Ÿ�������� ȸ�������ڵ�
	 * 	{N:�Ϲݴ���ڱ�, F:���Ϸ�����ڱ�_Ÿ����X, P:���Ϸ�����ڱ�_Ÿ����O}
	 * @return
	 */
	public String getMsrvMbbrDvsnCode() {
		return msrvMbbrDvsnCode;
	}
	/**
	 * ���̽�Ÿ�������� ȸ�������ڵ�
	 * 	{N:�Ϲݴ���ڱ�, F:���Ϸ�����ڱ�_Ÿ����X, P:���Ϸ�����ڱ�_Ÿ����O}
	 * @param msrvMbbrDvsnCode
	 */
	public void setMsrvMbbrDvsnCode(String msrvMbbrDvsnCode) {
		this.msrvMbbrDvsnCode = msrvMbbrDvsnCode;
	}
	
	/**
	 * �����ֹ���{YYYYMMDDHH24MISS}
	 * @return
	 */
	public String getCurrOrderDate() {
		return currOrderDate;
	}
	/**
	 * �����ֹ���{YYYYMMDDHH24MISS}
	 * @param currOrderDate
	 */
	public void setCurrOrderDate(String currOrderDate) {
		this.currOrderDate = currOrderDate;
	}
	
	/**
	 * �����ο������ѿ���{Y:�ο���over_�Ұ���, N:�ο����̸�_����}
	 * @return
	 */
	public String getPrcntLimitYn() {
		return prcntLimitYn;
	}
	/**
	 * �����ο������ѿ���{Y:�ο���over_�Ұ���, N:�ο����̸�_����}
	 * @param prcntLimitYn
	 */
	public void setPrcntLimitYn(String prcntLimitYn) {
		this.prcntLimitYn = prcntLimitYn;
	}
	
	/**
	 * ���̷����� �ֹ���ȣ
	 * @return
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * ���̷����� �ֹ���ȣ
	 * @param orderNo
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * �����ID
	 * @return
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * �����ID
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
