package co.kr.istarbucks.xo.batch.common.telecomm;

public class TelecomResultCode {

	/** ��Ż� ����/��� ���� */
	public static final String SUCCESS = "0000";
	
	/** ��Ż� ��� ���� */
	public static final String FAIL = "99";
	
	public class KT {
		/** ��Ż� KT 1�� ���Ƚ�� �ѵ� �ʰ� */
		public static final String ONE_DAY_USE_CNT_OVER_LIMIT = "1024";
		
		/** �̵�� ī�� */
		public static final String NON_REG_CARD = "1013";
		
		/** KT��Ż� ������� ���� �����ΰ��		 */
		public static final String FAIL_AUTH_BIRTH = "1009";
	}
	
	public class UPLUS {
		/** ��Ż� UPLUS 1�� ���Ƚ�� �ѵ� �ʰ� */
		public static final String ONE_DAY_USE_CNT_OVER_LIMIT = "0043";
		
		/** �̵�� ī�� */
		public static final String NON_REG_CARD = "0027";
	}	
}
