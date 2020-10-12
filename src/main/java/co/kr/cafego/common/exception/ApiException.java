package co.kr.cafego.common.exception;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

import co.kr.cafego.common.util.MessageUtil;

@SuppressWarnings("serial")
public class ApiException extends RuntimeException {
	/** 오류코드 */
	private String resultCode;
	
	/** 오류메시지 */
	private String resultMessage;
	
	/** 오류메시지 데이터 */
	private String[] resultData;
	
	/**
	 * API Exception
	 * @param resultCode
	 */
	public ApiException ( String resultCode ) {
		super("["+resultCode+":"+ MessageUtil.getMessage(resultCode)+"]");
		this.resultCode = resultCode;
	}
	
	
	public ApiException(String resultCode, String[] resultData) {
		super("["+resultCode+":"+ MessageUtil.getMessage(resultCode)+"]");
		this.resultCode = resultCode;
//		this.resultData = resultData;
		
		if (resultData == null) {
			this.resultData = null;
		} else {
			this.resultData = new String[resultData.length];
			System.arraycopy(resultData, 0, this.resultData, 0, resultData.length);
		}
	}

	/**
	 * API Exception
	 * @param resultCode
	 * @param resultMessage
	 */
	public ApiException ( String resultCode, String resultMessage ) {
		super("["+resultCode+":"+ MessageUtil.getMessage(resultCode)+"]" +"/"+ resultMessage);
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
	}
	
	/**
	 * API Exception
	 * @param resultCode
	 * @param resultMessage
	 * @param resultMsgArr
	 */
	public ApiException ( String resultCode, String resultMessage, String[] resultData ) {
		super("["+resultCode+":"+ MessageUtil.getMessage(resultCode)+"]" +"/"+ resultMessage);
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
//		this.resultData = resultData;
		
		if (resultData == null) {
			this.resultData = null;
		} else {
			this.resultData = new String[resultData.length];
			System.arraycopy(resultData, 0, this.resultData, 0, resultData.length);
		}
	}
	
	/**
	 * 오류 코드
	 * @return resultCode
	 */
	public String getResultCode () {
		return resultCode;
	}
	
	/**
	 * 오류 코드
	 * @param resultCode
	 */
	public void setResultCode ( String resultCode ) {
		this.resultCode = resultCode;
	}
	
	/**
	 * 결과 메시지
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
	 * 결과 메시지
	 * @param resultMessage
	 */
	public void setResultMessage ( String resultMessage ) {
		this.resultMessage = resultMessage;
	}
	
	/**
	 * 결과 데이타
	 * @return
	 */
	public String[] getResultData () {
		final String[] ret = resultData == null ? null : Arrays.copyOf(resultData, resultData.length);
		return ret;
	}
	
	/**
	 * 결과 데이타
	 * @param resultData
	 */
	public void setResultData ( String[] resultData ) {
//		this.resultData = resultData;
		
		if (resultData == null) {
			this.resultData = null;
		} else {
			this.resultData = new String[resultData.length];
			System.arraycopy(resultData, 0, this.resultData, 0, resultData.length);
		}
	}
}
