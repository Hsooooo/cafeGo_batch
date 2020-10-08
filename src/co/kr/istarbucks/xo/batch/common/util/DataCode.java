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
	
	// PG사 확대 =====================================================
	/** 스마트로 PG사 코드 : 004 */
	public static final String SMARTRO_PGCM_CODE = "004";
	/** 이니시스 PG사 코드 : 001 */
	public static final String INISYS_PGCM_CODE  = "001";
	/** 나이스 PG사 코드 : 002 */
	public static final String NICE_PGCM_CODE    = "002";
	
	/** PG사 타입(스마트로) */
	public static final String PG_TYPE_SMARTRO = "S";
	
	/** 채널 구분 코드(XOBATCH) */
	public static final String PG_CHANNEL_XOBATCH = "XB"; 
	
	/** PG사 서비스 구분 코드(사이렌오더) */
	public static final String PG_SERVICE_SIRENORDER = "04";
	
	/** 스마트로 결제 수단(신용카드) */
	public static final String SMARTRO_PAYMETHOD_CARD = "CARD";
	
	/** 스마트로 암호화 플래그(N: 평문, A2: 암호화(기본값)) */
	public static final String SMARTRO_ENC_FLAG = "A2";
	
	/** 스마트로 서비스 모드(PY0:결제, CL0:취소) */
	public static final String SMARTRO_CANCEL_MODE = "CL0";
	
	/** 스마트로 통화 구분 */
	public static final String SMARTRO_PAYMENT_CURRENCY = "KRW";
	
	/** 스마트로 캐릭터셋 */
	public static final String SMARTRO_CHARACTER_SET = "euc-kr";
	
	/** 스마트로 인코딩 타입 */
	public static final String SMARTRO_ENCODING_TYPE = "utf8";
	
	/** 스마트로 APP_LOG{1:enable} */
	public static final String SMARTRO_APP_LOG = "1";
	
	/** 스마트로 EVENT_LOG{1:enable} */
	public static final String SMARTRO_EVENT_LOG = "1";
	
	/** 스마트로 로그 출력명 */
	public static final String SMARTRO_LOG_TITLE = "SMARTRO";
	
	/** 스마트로 카드 결제 성공 */
	public static final String SMARTRO_CARD_SUCCESS = "3001";
	
	/** 스마트로 실시간 계좌이체 성공 */
	public static final String SMARTRO_BANK_SUCCESS = "4000";
	
	/** 스마트로 카드 결제 취소 성공 */
	public static final String SMARTRO_CARD_CANCEL = "2001";
	
	/** 스마트로 실시간 계좌이체 취소 성공 */
	public static final String SMARTRO_BANK_CANCEL = "2211";

	/** 비정상 승인건 취소 정상 처리 : 01 */
	public static final String PRCSG_ST_SUCCESS = "01";
	/** 비정상 승인건 취소 비정 정상 처리 : 02 */
	public static final String PRCSG_ST_FAIL    = "02";	
	/** 비정상 승인건 취소 예외 처리 : 03 */
	public static final String PRCSG_ST_ETC     = "03";
	
	/** 나이스 결제 취소 성공 : 2001 */
	public static final String NICE_CANCEL_SUCCESS = "2001";

	/** 취소 처리 사유 : PG 승인 대사 배치 결제 취소 */
	public static final String  PG_TRAD_CANCEL_REASONS = "PG 승인 대사 배치 결제 취소";

	/** 스마트로 PG 결제 취소 오류 : Result NULL */
	public static final String  PG_TRAD_SMARTRO_CANCEL_ERR = "스마트로 PG 결제 취소 오류 : Result NULL";
	
	/** 스마트로 PG 결제 취소 오류 : PG사 정보 조회 실패 */
	public static final String PG_INFO_SEARCH_ERR = "PG사 정보 조회 실패";
	
	/** PG사 서비스 구분 코드(일반충전) */
	public static final String PG_SERVICE_CHARGE     = "01";	
	/** PG사 서비스 구분 코드(자동충전) */
	public static final String PG_SERVICE_AUTOCHARGE = "02";	
	/** PG사 서비스 구분 코드(e-Gift Card) */
	public static final String PG_SERVICE_EGIFT_CARD = "03";
	/** PG사 서비스 구분 코드(홀케이크) */
	public static final String PG_SERVICE_WHOLECAKE  = "05";
	/** PG사 서비스 구분 코드(e-Gift Item) */
	public static final String PG_SERVICE_EGIFT_ITEM = "06";
	// PG사 확대 =====================================================
	
	/** 결과 성공 코드(공통) */
	public static final String RESULT_SUCCESS_CODE = "00";
	
	/** 결과 기타 에러 코드(공통) */
	public static final String RESULT_ETC_ERROR_CODE = "99";
	
	/**
     * 공통 메시지 아이디
     * 
     * @return String[][] DVSN_MSG_ID
     */
    public static final String[][] DVSN_MSG_ID = { {"SOR20", "XP12_0020"}
                                                 , {"SOR21", "XP20_0021"}
                                                 , {"SOR22", "XP20_0022"}
                                                 , {"B", "XP20_0023"}
                                                 };
}
