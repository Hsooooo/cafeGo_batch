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
 * 이미지 VO - MMSImageVO.
 *
 * @author ksy
 * @since 2016. 11. 07
 * @version $Revision: 1.1 $
 */
public class MMSImageVO {

	private String   imgName;			// 이미지 파일 명
	private String   imgPath;           // 이미지 경로
	private String   imgBasePath;	    // 이미지 기본 경로
	private String   imgTitle;			// 이미지 제목
	private String   imgSubTitle;		// 이미지 부제목
	private String[] imgContents;       // 이미지 내용 
	private String   backImgPath;		// 배경이미지 경로
	private String   contentsImgPath;	// 내용이미지 경로
	private String   barcode;			// 바코드
	private boolean  isRetry = false;	// 재발송 이미지 여부
	private String   orgImgPath;		// 기존에 생성한 MMS 경로
	private boolean  isSuccess = false; // 이미지 생성 결과 
	
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
	 * 이미지 파일 명
	 * @return imgName
	 */
	public String getImgName() {
		return imgName;
	}

	/**
	 * 이미지 파일 명
	 * @param imgName
	 */
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	
	/**
	 * 이미지 경로
	 * @return imgPath
	 */
	public String getImgPath() {
		return imgPath;
	}

	/**
	 * 이미지 경로
	 * @param imgPath
	 */
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
	/**
	 * 이미지 기본 경로
	 * @return imgBasePath
	 */
	public String getImgBasePath() {
		return imgBasePath;
	}

	/**
	 * 이미지 기본 경로
	 * @param imgBasePath
	 */
	public void setImgBasePath(String imgBasePath) {
		this.imgBasePath = imgBasePath;
	}
	
	/**
	 * 이미지 제목
	 * @return imgTitle
	 */
	public String getImgTitle() {
		return imgTitle;
	}

	/**
	 * 이미지 제목
	 * @param imgTitle
	 */
	public void setImgTitle(String imgTitle) {
		this.imgTitle = imgTitle;
	}
	
	/**
	 * 이미지 부제목
	 * @return imgSubTitle
	 */
	public String getImgSubTitle() {
		return imgSubTitle;
	}

	/**
	 * 이미지 내용 항목명
	 * @param imgSubTitle
	 */
	public void setImgSubTitle(String imgSubTitle) {
		this.imgSubTitle = imgSubTitle;
	}
	
	/**
	 * 이미지 내용
	 * @return imgContents
	 */
	public String[] getImgContents() {
	    String [] arrImgContents = Arrays.copyOf(imgContents, imgContents.length);
	    return arrImgContents;
	}

	/**
	 * 이미지 내용
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
	 * 배경이미지 경로
	 * @return backImgPath
	 */
	public String getBackImgPath() {
		return backImgPath;
	}

	/**
	 * 배경이미지 경로
	 * @param backImgPath
	 */
	public void setBackImgPath(String backImgPath) {
		this.backImgPath = backImgPath;
	}
	
	/**
	 * 내용이미지 경로
	 * @return contentsImgPath
	 */
	public String getContentsImgPath() {
		return contentsImgPath;
	}

	/**
	 * 내용이미지 경로
	 * @param contentsImgPath
	 */
	public void setContentsImgPath(String contentsImgPath) {
		this.contentsImgPath = contentsImgPath;
	}
	
	/**
	 * 바코드
	 * @return barcode
	 */
	public String getBarcode() {
		return barcode;
	}

	/**
	 * 바코드
	 * @param barcode
	 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	/**
	 * 재발송 이미지 여부
	 * @return isRetry
	 */
	public boolean getIsRetry() {
		return isRetry;
	}

	/**
	 * 재발송 이미지 여부
	 * @param isRetry
	 */
	public void setIsRetry(boolean isRetry) {
		this.isRetry = isRetry;
	}
	
	/**
	 * 기존에 생성한 MMS 경로
	 * @return orgImgPath
	 */
	public String getOrgImgPath() {
		return orgImgPath;
	}

	/**
	 * 기존에 생성한 MMS 경로
	 * @param orgImgPath
	 */
	public void setOrgImgPath(String orgImgPath) {
		this.orgImgPath = orgImgPath;
	}
	
	/**
	 * 이미지 생성 결과 
	 * @return isSucces
	 */
	public boolean getIsSuccess() {
		return isSuccess;
	}

	/**
	 * 이미지 생성 결과 
	 * @param isSucces
	 */
	public void setIsSuccess(boolean isSucces) {
		this.isSuccess = isSucces;
	}
}
