package co.kr.istarbucks.xo.batch.common.pg.dto;

/**
 * ����Ʈ�� PG ���� Dto
 * @author hw.Jang
 *
 */
public class SmartroPgInfoDto {
	private String pgcmShopId;				//PG�� ���� ID
	private String pgcmShopSgngKey;		//PG�� ���� ���� Ű
	private String pgcmShopCncltPwd;		//PG�� ���� ��� ��й�ȣ
	private String smpyMbsId;				//������������� ID(SspMallID)
	private String logFilePath;					//�α� ���� ���
	private String intrlUrl;						//���� URL
	private String ipads;							//IP �ּ�
	private String stlmnPortNo;				//���� ��Ʈ ��ȣ
	private String cncltPortNo;				//��� ��Ʈ ��ȣ
	private String tmtMscnt;					// Ÿ�Ӿƿ��и��ʼ�
	
	
	/**
	 * PG�� ���� ID
	 * @return
	 */
	public String getPgcmShopId() {
		return pgcmShopId;
	}

	/**
	 * PG�� ���� ID
	 * @param pgcmShopId
	 */
	public void setPgcmShopId(String pgcmShopId) {
		this.pgcmShopId = pgcmShopId;
	}

	/**
	 * PG�� ���� ���� Ű
	 * @return
	 */
	public String getPgcmShopSgngKey() {
		return pgcmShopSgngKey;
	}

	/**
	 * PG�� ���� ���� Ű
	 * @param pgcmShopSgngKey
	 */
	public void setPgcmShopSgngKey(String pgcmShopSgngKey) {
		this.pgcmShopSgngKey = pgcmShopSgngKey;
	}

	/**
	 * PG�� ���� ��� ��й�ȣ
	 * @return
	 */
	public String getPgcmShopCncltPwd() {
		return pgcmShopCncltPwd;
	}

	/**
	 * PG�� ���� ��� ��й�ȣ
	 * @param pgcmShopCncltPwd
	 */
	public void setPgcmShopCncltPwd(String pgcmShopCncltPwd) {
		this.pgcmShopCncltPwd = pgcmShopCncltPwd;
	}

	/**
	 * ������������� ID(SspMallID)
	 * @return
	 */
	public String getSmpyMbsId() {
		return smpyMbsId;
	}

	/**
	 * ������������� ID(SspMallID)
	 * @param smpyMbsId
	 */
	public void setSmpyMbsId(String smpyMbsId) {
		this.smpyMbsId = smpyMbsId;
	}

	/**
	 * �α� ���� ���
	 * @return
	 */
	public String getLogFilePath() {
		return logFilePath;
	}

	/**
	 * �α� ���� ���
	 * @param logFilePath
	 */
	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}

	/**
	 * ���� URL
	 * @return
	 */
	public String getIntrlUrl() {
		return intrlUrl;
	}

	/**
	 * ���� URL
	 * @param intrlUrl
	 */
	public void setIntrlUrl(String intrlUrl) {
		this.intrlUrl = intrlUrl;
	}

	/**
	 * IP �ּ�
	 * @return
	 */
	public String getIpads() {
		return ipads;
	}

	/**
	 * IP �ּ�
	 * @param ipads
	 */
	public void setIpads(String ipads) {
		this.ipads = ipads;
	}

	/**
	 * ���� ��Ʈ ��ȣ
	 * @return
	 */
	public String getStlmnPortNo() {
		return stlmnPortNo;
	}

	/**
	 * ���� ��Ʈ ��ȣ
	 * @param stlmnPortNo
	 */
	public void setStlmnPortNo(String stlmnPortNo) {
		this.stlmnPortNo = stlmnPortNo;
	}
	
	/**
	 * ��� ��Ʈ ��ȣ
	 * @return
	 */
	public String getCncltPortNo() {
		return cncltPortNo;
	}

	/**
	 * ��� ��Ʈ ��ȣ
	 * @param cncltPortNo
	 */
	public void setCncltPortNo(String cncltPortNo) {
		this.cncltPortNo = cncltPortNo;
	}

	/**
	 * Ÿ�Ӿƿ��и��ʼ�
	 * @return
	 */
	public String getTmtMscnt() {
		return tmtMscnt;
	}

	/**
	 * Ÿ�Ӿƿ��и��ʼ�
	 * @param tmtMscnt
	 */
	public void setTmtMscnt(String tmtMscnt) {
		this.tmtMscnt = tmtMscnt;
	}

}
