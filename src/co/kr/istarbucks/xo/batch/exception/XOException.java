/*
 * @(#) $Id: XOException.java,v 1.1 2014/03/03 04:50:55 alcyone Exp $
 * Starbucks ExpressOrder
 * Copyright 2013 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.exception;

/**
 * ���� ���� XOException.
 * @author eZEN ksy
 * @version $Revision: 1.1 $
 */
@SuppressWarnings ( "serial" )
public class XOException extends Exception {
	private String message; // ���� �޽���
	
	public XOException () {
		super ();
	}
	
	public XOException ( String resultMsg ) {
		this.message = resultMsg;
	}
	
	/**
	 * ���� �޽���
	 * @return resultMessage
	 */
	public String getMessage () {
		return message;
	}
	
	/**
	 * ���� �޽���
	 * @param message
	 */
	public void setMessage ( String message ) {
		this.message = message;
	}
}
