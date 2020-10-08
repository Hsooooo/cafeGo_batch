/*
 * @(#) $Id: MMSImageVO.java,v 1.1 2016/11/10 00:53:43 dev99 Exp $
 * 
 * Starbucks Service
 * 
 * Copyright 2016 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */
package co.kr.istarbucks.xo.batch.common.image;

import java.util.Arrays;

/**
 * �̹��� VO - MMSImageVO.
 *
 * @author ksy
 * @since 2016. 11. 07
 * @version $Revision: 1.1 $
 */
public class MMSImageVO {

	private String   imgName;			// �̹��� ���� ��
	private String   imgPath;           // �̹��� ���
	private String   imgBasePath;	    // �̹��� �⺻ ���
	private String   imgTitle;			// �̹��� ����
	private String   imgSubTitle;		// �̹��� ������
	private String[] imgContents;       // �̹��� ���� 
	private String   backImgPath;		// ����̹��� ���
	private String   contentsImgPath;	// �����̹��� ���
	private String   barcode;			// ���ڵ�
	private boolean  isRetry = false;	// ��߼� �̹��� ����
	private String   orgImgPath;		// ������ ������ MMS ���
	private boolean  isSuccess = false; // �̹��� ���� ��� 
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [");
		sb.append("isSuccess="         ).append(this.isSuccess);
		sb.append(", imgPath="         ).append(this.imgPath);
		sb.append(", imgName="         ).append(this.imgName);
		sb.append(", imgPath="         ).append(this.imgPath);
		sb.append(", imgBasePath="     ).append(this.imgBasePath);
		sb.append(", imgTitle="        ).append(this.imgTitle);
		sb.append(", imgContentsTitle=").append(this.imgSubTitle);
		sb.append(", imgContents="     ).append(this.imgContents);
		sb.append(", backImgPath="     ).append(this.backImgPath);
		sb.append(", contentsImgPath=" ).append(this.contentsImgPath);
		sb.append(", barcode="         ).append(this.barcode);
		sb.append(", isRetry="         ).append(this.isRetry);
		sb.append(", orgImgPath="      ).append(this.orgImgPath);
		sb.append(", isSuccess="       ).append(this.isSuccess);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * �̹��� ���� ��
	 * @return imgName
	 */
	public String getImgName() {
		return imgName;
	}

	/**
	 * �̹��� ���� ��
	 * @param imgName
	 */
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	
	/**
	 * �̹��� ���
	 * @return imgPath
	 */
	public String getImgPath() {
		return imgPath;
	}

	/**
	 * �̹��� ���
	 * @param imgPath
	 */
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
	/**
	 * �̹��� �⺻ ���
	 * @return imgBasePath
	 */
	public String getImgBasePath() {
		return imgBasePath;
	}

	/**
	 * �̹��� �⺻ ���
	 * @param imgBasePath
	 */
	public void setImgBasePath(String imgBasePath) {
		this.imgBasePath = imgBasePath;
	}
	
	/**
	 * �̹��� ����
	 * @return imgTitle
	 */
	public String getImgTitle() {
		return imgTitle;
	}

	/**
	 * �̹��� ����
	 * @param imgTitle
	 */
	public void setImgTitle(String imgTitle) {
		this.imgTitle = imgTitle;
	}
	
	/**
	 * �̹��� ������
	 * @return imgSubTitle
	 */
	public String getImgSubTitle() {
		return imgSubTitle;
	}

	/**
	 * �̹��� ���� �׸��
	 * @param imgSubTitle
	 */
	public void setImgSubTitle(String imgSubTitle) {
		this.imgSubTitle = imgSubTitle;
	}
	
	/**
	 * �̹��� ����
	 * @return imgContents
	 */
	public String[] getImgContents() {
	    String [] arrImgContents = Arrays.copyOf(imgContents, imgContents.length);
	    return arrImgContents;
	}

	/**
	 * �̹��� ����
	 * @param imgContents
	 */
	public void setImgContents(String[] imgContents) {
        if (imgContents == null) {
            this.imgContents = null;
        } else {
            this.imgContents = new String[imgContents.length];
            System.arraycopy(imgContents, 0, this.imgContents, 0,imgContents.length);
        }
	}
	
	/**
	 * ����̹��� ���
	 * @return backImgPath
	 */
	public String getBackImgPath() {
		return backImgPath;
	}

	/**
	 * ����̹��� ���
	 * @param backImgPath
	 */
	public void setBackImgPath(String backImgPath) {
		this.backImgPath = backImgPath;
	}
	
	/**
	 * �����̹��� ���
	 * @return contentsImgPath
	 */
	public String getContentsImgPath() {
		return contentsImgPath;
	}

	/**
	 * �����̹��� ���
	 * @param contentsImgPath
	 */
	public void setContentsImgPath(String contentsImgPath) {
		this.contentsImgPath = contentsImgPath;
	}
	
	/**
	 * ���ڵ�
	 * @return barcode
	 */
	public String getBarcode() {
		return barcode;
	}

	/**
	 * ���ڵ�
	 * @param barcode
	 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	/**
	 * ��߼� �̹��� ����
	 * @return isRetry
	 */
	public boolean getIsRetry() {
		return isRetry;
	}

	/**
	 * ��߼� �̹��� ����
	 * @param isRetry
	 */
	public void setIsRetry(boolean isRetry) {
		this.isRetry = isRetry;
	}
	
	/**
	 * ������ ������ MMS ���
	 * @return orgImgPath
	 */
	public String getOrgImgPath() {
		return orgImgPath;
	}

	/**
	 * ������ ������ MMS ���
	 * @param orgImgPath
	 */
	public void setOrgImgPath(String orgImgPath) {
		this.orgImgPath = orgImgPath;
	}
	
	/**
	 * �̹��� ���� ��� 
	 * @return isSucces
	 */
	public boolean getIsSuccess() {
		return isSuccess;
	}

	/**
	 * �̹��� ���� ��� 
	 * @param isSucces
	 */
	public void setIsSuccess(boolean isSucces) {
		this.isSuccess = isSucces;
	}
}
