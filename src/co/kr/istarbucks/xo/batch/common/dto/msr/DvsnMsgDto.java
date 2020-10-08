package co.kr.istarbucks.xo.batch.common.dto.msr;

/**
 * 공통 메시지
 * 
 */
public class DvsnMsgDto {

	private String msgId;				// 메시지 아이디
	private String msgCntnt;			// 메시지
	private String sbMsgCntnt;			// 서브 메시지
	private String storeAdclWordsCntnt;	// 매장 부가 문구
	
	/**
	 * 메시지 아이디
	 * @return the msgId
	 */
	public String getMsgId() {
		return msgId;
	}
	/**
	 * 메시지 아이디
	 * @param msgId the msgId to set
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	/**
	 * 메시지
	 * @return the msgCntnt
	 */
	public String getMsgCntnt() {
		return msgCntnt;
	}
	/**
	 * 메시지
	 * @param msgCntnt the msgCntnt to set
	 */
	public void setMsgCntnt(String msgCntnt) {
		this.msgCntnt = msgCntnt;
	}
	/**
	 * 서브 메시지
	 * @return the sbMsgCntnt
	 */
	public String getSbMsgCntnt() {
		return sbMsgCntnt;
	}
	/**
	 * 서브 메시지
	 * @param sbMsgCntnt the sbMsgCntnt to set
	 */
	public void setSbMsgCntnt(String sbMsgCntnt) {
		this.sbMsgCntnt = sbMsgCntnt;
	}
	/**
	 * 매장 부가 문구
	 * @return the storeAdclWordsCntnt
	 */
	public String getStoreAdclWordsCntnt() {
		return storeAdclWordsCntnt;
	}
	/**
	 * 매장 부가 문구
	 * @param storeAdclWordsCntnt the storeAdclWordsCntnt to set
	 */
	public void setStoreAdclWordsCntnt(String storeAdclWordsCntnt) {
		this.storeAdclWordsCntnt = storeAdclWordsCntnt;
	}
	
	
}
