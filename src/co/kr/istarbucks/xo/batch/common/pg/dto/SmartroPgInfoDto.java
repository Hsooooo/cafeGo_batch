package co.kr.istarbucks.xo.batch.common.pg.dto;

/**
 * 스마트로 PG 정보 Dto
 * @author hw.Jang
 *
 */
public class SmartroPgInfoDto {
	private String pgcmShopId;				//PG사 상점 ID
	private String pgcmShopSgngKey;		//PG사 상점 서명 키
	private String pgcmShopCncltPwd;		//PG사 상점 취소 비밀번호
	private String smpyMbsId;				//간편결제가맹정 ID(SspMallID)
	private String logFilePath;					//로그 파일 경로
	private String intrlUrl;						//연동 URL
	private String ipads;							//IP 주소
	private String stlmnPortNo;				//결제 포트 번호
	private String cncltPortNo;				//취소 포트 번호
	private String tmtMscnt;					// 타임아웃밀리초수
	
	
	/**
	 * PG사 상점 ID
	 * @return
	 */
	public String getPgcmShopId() {
		return pgcmShopId;
	}

	/**
	 * PG사 상점 ID
	 * @param pgcmShopId
	 */
	public void setPgcmShopId(String pgcmShopId) {
		this.pgcmShopId = pgcmShopId;
	}

	/**
	 * PG사 상점 서명 키
	 * @return
	 */
	public String getPgcmShopSgngKey() {
		return pgcmShopSgngKey;
	}

	/**
	 * PG사 상점 서명 키
	 * @param pgcmShopSgngKey
	 */
	public void setPgcmShopSgngKey(String pgcmShopSgngKey) {
		this.pgcmShopSgngKey = pgcmShopSgngKey;
	}

	/**
	 * PG사 상점 취소 비밀번호
	 * @return
	 */
	public String getPgcmShopCncltPwd() {
		return pgcmShopCncltPwd;
	}

	/**
	 * PG사 상점 취소 비밀번호
	 * @param pgcmShopCncltPwd
	 */
	public void setPgcmShopCncltPwd(String pgcmShopCncltPwd) {
		this.pgcmShopCncltPwd = pgcmShopCncltPwd;
	}

	/**
	 * 간편결제가맹정 ID(SspMallID)
	 * @return
	 */
	public String getSmpyMbsId() {
		return smpyMbsId;
	}

	/**
	 * 간편결제가맹정 ID(SspMallID)
	 * @param smpyMbsId
	 */
	public void setSmpyMbsId(String smpyMbsId) {
		this.smpyMbsId = smpyMbsId;
	}

	/**
	 * 로그 파일 경로
	 * @return
	 */
	public String getLogFilePath() {
		return logFilePath;
	}

	/**
	 * 로그 파일 경로
	 * @param logFilePath
	 */
	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}

	/**
	 * 연동 URL
	 * @return
	 */
	public String getIntrlUrl() {
		return intrlUrl;
	}

	/**
	 * 연동 URL
	 * @param intrlUrl
	 */
	public void setIntrlUrl(String intrlUrl) {
		this.intrlUrl = intrlUrl;
	}

	/**
	 * IP 주소
	 * @return
	 */
	public String getIpads() {
		return ipads;
	}

	/**
	 * IP 주소
	 * @param ipads
	 */
	public void setIpads(String ipads) {
		this.ipads = ipads;
	}

	/**
	 * 결제 포트 번호
	 * @return
	 */
	public String getStlmnPortNo() {
		return stlmnPortNo;
	}

	/**
	 * 결제 포트 번호
	 * @param stlmnPortNo
	 */
	public void setStlmnPortNo(String stlmnPortNo) {
		this.stlmnPortNo = stlmnPortNo;
	}
	
	/**
	 * 취소 포트 번호
	 * @return
	 */
	public String getCncltPortNo() {
		return cncltPortNo;
	}

	/**
	 * 취소 포트 번호
	 * @param cncltPortNo
	 */
	public void setCncltPortNo(String cncltPortNo) {
		this.cncltPortNo = cncltPortNo;
	}

	/**
	 * 타임아웃밀리초수
	 * @return
	 */
	public String getTmtMscnt() {
		return tmtMscnt;
	}

	/**
	 * 타임아웃밀리초수
	 * @param tmtMscnt
	 */
	public void setTmtMscnt(String tmtMscnt) {
		this.tmtMscnt = tmtMscnt;
	}

}
