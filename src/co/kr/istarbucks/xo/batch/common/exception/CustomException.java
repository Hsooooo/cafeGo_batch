package co.kr.istarbucks.xo.batch.common.exception;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;


@SuppressWarnings("serial")
public class CustomException extends RuntimeException {

	/**
	 * PMD 0.5 Generic Exception ó���� ���� Ŭ����
	 */
	
	/** �����ڵ� */
	private String resultCode;
	
	/** �����޽��� */
	private String resultMessage;
	
	/** �����޽��� ������ */
	private String[] resultData;
	
	public CustomException( String resultCode ) {
		super("["+resultCode+":"+ MessageUtil.getMessage(resultCode)+"]");
		this.resultCode = resultCode;
	}

	public CustomException(String resultCode, String[] resultData) {
		super("["+resultCode+":"+ MessageUtil.getMessage(resultCode)+"]");
		this.resultCode = resultCode;
		String[] arrRtrnResultData = resultData;
		this.resultData = arrRtrnResultData;
	}
	
	/**
	 * API Exception
	 * @param resultCode
	 * @param resultMessage
	 */
	public CustomException ( String resultCode, String resultMessage ) {
		super(MessageUtil.getMessage(resultCode)+"/"+ resultMessage);
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
	}
	
	/**
	 * API Exception
	 * @param resultCode
	 * @param resultMessage
	 * @param resultMsgArr
	 */
	public CustomException ( String resultCode, String resultMessage, String[] resultData ) {
		super("["+resultCode+":"+ MessageUtil.getMessage(resultCode)+"]" +"/"+ resultMessage);
		this.resultCode = resultCode;
		String[] arrRtrnResultData = resultData;
		this.resultData = arrRtrnResultData;
	}
	
	/**
	 * ���� �ڵ�
	 * @return resultCode
	 */
	public String getResultCode () {
		return resultCode;
	}
	
	/**
	 * ���� �ڵ�
	 * @param resultCode
	 */
	public void setResultCode ( String resultCode ) {
		this.resultCode = resultCode;
	}
	
	/**
	 * ��� �޽���
	 * @return resultMessage
	 */
	public String getResultMessage () {
		if(this.resultData != null && this.resultData.length > 0){
			String resultMsg = MessageUtil.getMessage(this.resultCode);
			StringBuffer buf = new StringBuffer();
			int cnt = 0;
			for(String str : this.resultData){
				buf.delete(0, buf.length()).append("\\{").append(cnt).append("\\}");
				resultMsg = resultMsg.replaceAll(buf.toString(), StringUtils.defaultString(str));
				cnt++;
			}
			
			resultMessage = resultMsg;
		}
		
		return resultMessage;
	}
	
	/**
	 * ��� �޽���
	 * @param resultMessage
	 */
	public void setResultMessage ( String resultMessage ) {
		this.resultMessage = resultMessage;
	}
	
	/**
	 * ��� ����Ÿ
	 * @return
	 */
	public String[] getResultData() {
		String[] arrRtrnResultData = null;
		if(resultData == null)
			arrRtrnResultData = null;
		else
			arrRtrnResultData = Arrays.copyOf(resultData, resultData.length);
		return arrRtrnResultData;
	}
	
	/**
	 * ��� ������
	 * @param resultData
	 */
	public void setResultData ( String[] resultData ) {
		String[] arrRtrnResultData = resultData;
		this.resultData = arrRtrnResultData;
	}
	
}
