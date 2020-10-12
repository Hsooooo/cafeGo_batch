package co.kr.cafego.common.util;

public class ResultCode {
	/** 응답코드 : [000] 정상 처리되었습니다. */
	public static final String SUCCESS           = "000";
	
	/** 응답코드 : [001] 액세스 토큰이 유효하지 않습니다. */
	public static final String NO_TOKEN          = "001";
	
	/** 응답코드 : [002] 액세스 토큰의 기간이 만료되었습니다. */
	public static final String NO_SIRN_TOKEN     = "002";
	
	/** 응답코드 : [003] 요청 파라미터의 값이 잘못되었습니다. */
	public static final String INVALID_PARAMETER = "003";
	
	/** 응답코드 : [004] 잘못된 URL입니다. */
	public static final String INVALID_URL       = "004";
	
	/** 응답코드 : [005] 잘못된 요청입니다. */
	public static final String INVALID_REQUEST   = "005";
	
	/** 응답코드 : [006] 프로토콜 오류입니다. (HTTPS) */
	public static final String INVALID_PROTOCOL  = "006";
	
	/** 응답코드 : [007] 허용되지 않은 HTTP 메소드 입니다. (허용 메소드 : POST, GET, DELETE, PATCH) */
	public static final String INVALID_METHOD    = "007";
	
	/** 응답코드 : [008] DB 오류입니다. */
	public static final String DB_ERROR          = "008";
	
	/** 응답코드 : [009] 조회된 데이터가 없습니다. */
	public static final String NO_DATA           = "009";

	/** 응답코드 : [010] 서버 작업 중 입니다. */
	public static final String SERVER_WORKING    = "010";
	
	/** 응답코드 : [011] json data가 유효하지 않습니다. */
	public static final String JSON_DATA_ERROR   = "011";
	
	/** 응답코드 : [012] 세션 정보가 존재하지 않습니다. */
	public static final String NO_SESSION	     = "012";
	
	/** 응답코드 : [099] 기타 오류입니다. */
	public static final String SERVER_ERROR      = "099";
	
	//===========================[1. 회원]======================================//
	/** Message : 조회된 회원 정보 없음*/
	public static final String MEM_01	= "101";
	
	/** Message : 이미 등록된 회원정보가 있습니다. */
	public static final String MEM_02	= "102";
	
	/** Message : 패스워드가 일치하지 않습니다. */
	public static final String MEM_03	= "103";
	
	/** Message : 최대 로그인 실패 횟수를 초과했습니다. */
	public static final String MEM_04	= "104";
	
	//===========================[6. 결제]=======================================//
	
	/** Message : 사용 가능한 포인트 한도 초과*/
	public static final String PAY_01	= "601";
	
	/** Message : 주문 가능한 카트 정보 아님*/
	public static final String PAY_02	= "602";
	
}