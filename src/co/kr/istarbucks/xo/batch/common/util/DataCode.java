package co.kr.istarbucks.xo.batch.common.util;

public class DataCode {
	
	public static String getDvsnMsgId(String msgTpCd) {
		String dvsnMsgId = "";
		
		String[][] arr = DVSN_MSG_ID;
		
		for( int i = 0; i < arr.length; i++ ) {
            if( msgTpCd.equals(arr[i][0]) ) {
            	dvsnMsgId = arr[i][1];
                break;
            }
        }
        return dvsnMsgId;
	}
	
	// PG�� Ȯ�� =====================================================
	/** ����Ʈ�� PG�� �ڵ� : 004 */
	public static final String SMARTRO_PGCM_CODE = "004";
	/** �̴Ͻý� PG�� �ڵ� : 001 */
	public static final String INISYS_PGCM_CODE  = "001";
	/** ���̽� PG�� �ڵ� : 002 */
	public static final String NICE_PGCM_CODE    = "002";
	
	/** PG�� Ÿ��(����Ʈ��) */
	public static final String PG_TYPE_SMARTRO = "S";
	
	/** ä�� ���� �ڵ�(XOBATCH) */
	public static final String PG_CHANNEL_XOBATCH = "XB"; 
	
	/** PG�� ���� ���� �ڵ�(���̷�����) */
	public static final String PG_SERVICE_SIRENORDER = "04";
	
	/** ����Ʈ�� ���� ����(�ſ�ī��) */
	public static final String SMARTRO_PAYMETHOD_CARD = "CARD";
	
	/** ����Ʈ�� ��ȣȭ �÷���(N: ��, A2: ��ȣȭ(�⺻��)) */
	public static final String SMARTRO_ENC_FLAG = "A2";
	
	/** ����Ʈ�� ���� ���(PY0:����, CL0:���) */
	public static final String SMARTRO_CANCEL_MODE = "CL0";
	
	/** ����Ʈ�� ��ȭ ���� */
	public static final String SMARTRO_PAYMENT_CURRENCY = "KRW";
	
	/** ����Ʈ�� ĳ���ͼ� */
	public static final String SMARTRO_CHARACTER_SET = "euc-kr";
	
	/** ����Ʈ�� ���ڵ� Ÿ�� */
	public static final String SMARTRO_ENCODING_TYPE = "utf8";
	
	/** ����Ʈ�� APP_LOG{1:enable} */
	public static final String SMARTRO_APP_LOG = "1";
	
	/** ����Ʈ�� EVENT_LOG{1:enable} */
	public static final String SMARTRO_EVENT_LOG = "1";
	
	/** ����Ʈ�� �α� ��¸� */
	public static final String SMARTRO_LOG_TITLE = "SMARTRO";
	
	/** ����Ʈ�� ī�� ���� ���� */
	public static final String SMARTRO_CARD_SUCCESS = "3001";
	
	/** ����Ʈ�� �ǽð� ������ü ���� */
	public static final String SMARTRO_BANK_SUCCESS = "4000";
	
	/** ����Ʈ�� ī�� ���� ��� ���� */
	public static final String SMARTRO_CARD_CANCEL = "2001";
	
	/** ����Ʈ�� �ǽð� ������ü ��� ���� */
	public static final String SMARTRO_BANK_CANCEL = "2211";

	/** ������ ���ΰ� ��� ���� ó�� : 01 */
	public static final String PRCSG_ST_SUCCESS = "01";
	/** ������ ���ΰ� ��� ���� ���� ó�� : 02 */
	public static final String PRCSG_ST_FAIL    = "02";	
	/** ������ ���ΰ� ��� ���� ó�� : 03 */
	public static final String PRCSG_ST_ETC     = "03";
	
	/** ���̽� ���� ��� ���� : 2001 */
	public static final String NICE_CANCEL_SUCCESS = "2001";

	/** ��� ó�� ���� : PG ���� ��� ��ġ ���� ��� */
	public static final String  PG_TRAD_CANCEL_REASONS = "PG ���� ��� ��ġ ���� ���";

	/** ����Ʈ�� PG ���� ��� ���� : Result NULL */
	public static final String  PG_TRAD_SMARTRO_CANCEL_ERR = "����Ʈ�� PG ���� ��� ���� : Result NULL";
	
	/** ����Ʈ�� PG ���� ��� ���� : PG�� ���� ��ȸ ���� */
	public static final String PG_INFO_SEARCH_ERR = "PG�� ���� ��ȸ ����";
	
	/** PG�� ���� ���� �ڵ�(�Ϲ�����) */
	public static final String PG_SERVICE_CHARGE     = "01";	
	/** PG�� ���� ���� �ڵ�(�ڵ�����) */
	public static final String PG_SERVICE_AUTOCHARGE = "02";	
	/** PG�� ���� ���� �ڵ�(e-Gift Card) */
	public static final String PG_SERVICE_EGIFT_CARD = "03";
	/** PG�� ���� ���� �ڵ�(Ȧ����ũ) */
	public static final String PG_SERVICE_WHOLECAKE  = "05";
	/** PG�� ���� ���� �ڵ�(e-Gift Item) */
	public static final String PG_SERVICE_EGIFT_ITEM = "06";
	// PG�� Ȯ�� =====================================================
	
	/** ��� ���� �ڵ�(����) */
	public static final String RESULT_SUCCESS_CODE = "00";
	
	/** ��� ��Ÿ ���� �ڵ�(����) */
	public static final String RESULT_ETC_ERROR_CODE = "99";
	
	/**
     * ���� �޽��� ���̵�
     * 
     * @return String[][] DVSN_MSG_ID
     */
    public static final String[][] DVSN_MSG_ID = { {"SOR20", "XP12_0020"}
                                                 , {"SOR21", "XP20_0021"}
                                                 , {"SOR22", "XP20_0022"}
                                                 , {"B", "XP20_0023"}
                                                 };
}
