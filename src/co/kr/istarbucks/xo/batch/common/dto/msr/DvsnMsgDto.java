package co.kr.istarbucks.xo.batch.common.dto.msr;

/**
 * ���� �޽���
 * 
 */
public class DvsnMsgDto {

	private String msgId;				// �޽��� ���̵�
	private String msgCntnt;			// �޽���
	private String sbMsgCntnt;			// ���� �޽���
	private String storeAdclWordsCntnt;	// ���� �ΰ� ����
	
	/**
	 * �޽��� ���̵�
	 * @return the msgId
	 */
	public String getMsgId() {
		return msgId;
	}
	/**
	 * �޽��� ���̵�
	 * @param msgId the msgId to set
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	/**
	 * �޽���
	 * @return the msgCntnt
	 */
	public String getMsgCntnt() {
		return msgCntnt;
	}
	/**
	 * �޽���
	 * @param msgCntnt the msgCntnt to set
	 */
	public void setMsgCntnt(String msgCntnt) {
		this.msgCntnt = msgCntnt;
	}
	/**
	 * ���� �޽���
	 * @return the sbMsgCntnt
	 */
	public String getSbMsgCntnt() {
		return sbMsgCntnt;
	}
	/**
	 * ���� �޽���
	 * @param sbMsgCntnt the sbMsgCntnt to set
	 */
	public void setSbMsgCntnt(String sbMsgCntnt) {
		this.sbMsgCntnt = sbMsgCntnt;
	}
	/**
	 * ���� �ΰ� ����
	 * @return the storeAdclWordsCntnt
	 */
	public String getStoreAdclWordsCntnt() {
		return storeAdclWordsCntnt;
	}
	/**
	 * ���� �ΰ� ����
	 * @param storeAdclWordsCntnt the storeAdclWordsCntnt to set
	 */
	public void setStoreAdclWordsCntnt(String storeAdclWordsCntnt) {
		this.storeAdclWordsCntnt = storeAdclWordsCntnt;
	}
	
	
}
