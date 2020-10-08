package co.kr.istarbucks.xo.batch.common.telecomm;

import java.util.Map;


/**
 * 통신사 제휴할인을 위한 VAN 연동 서비스
 */
public class TelecommService {
	
	/**
	 * 통신사의 멤버십 여부를 조회한다.
	 * @param telecomType 통신사 구분. KT인 경우 'K', U+인 경우 'U'
	 * @param cardNumber  멤버십 카드번호. 16자리 숫자타입
	 * @return 반환 Map객체의 키"RespCd"의 값이 "0000"인 경우는 회원, 아니면 회원아님
	 */
	public static Map<String, String> inquireMembershipPoint(String telecomType, String cardNumber) {
		TelecommBroker tcBroker = new TelecommBroker();
		return tcBroker.inquireForPoint(telecomType, cardNumber);		
	}
	
	/**
	 * KT 통신사의 멤버십 여부를 조회한다.
	 * @param telecomType 통신사 구분. KT인 경우 'K'
	 * @param cardNumber  멤버십 카드번호. 16자리 숫자타입
	 * @param yymmdd 생년월일
	 * @return 반환 Map객체의 키"RespCd"의 값이 "0000"인 경우는 회원, 아니면 회원아님
	 */
	public static Map<String, String> inquireMembershipPoint(String telecomType, String cardNumber, String yymmdd) {
		TelecommBroker tcBroker = new TelecommBroker();
		return tcBroker.inquireForPoint(telecomType, cardNumber, yymmdd);		
	}

	/**
	 * 통신사 제휴할인에 따른 포인트 결제를 요청한다.
	 * @param telecomType  통신사 구분. KT인 경우 'K', U+인 경우 'U'
	 * @param cardNumber   멤버십 카드번호. 16자리 숫자타입
	 * @param amount       결제요청금액. 금액과 동일한 포인트를 결제요청한다.(1원당 1포인트)
	 * @param discountType 통신사가 U+인 경우는 null, KT인 경우는 아래와 같다.<br />
	 *                     사이즈업그레이드인 경우 'KT0002'
	 * @return             반환 Map객체의 키"RespCd"의 값이 "0000"인 경우는 결제성공, 아니면 결제실패<br />
	 *                     성공인 경우 결제취소를 위해 아래의 값을 관리해야 한다.<br />
	 *                     &nbsp;1) ApprovDT (승인일시, YYMMDDhhmmssN (N: 0~6, 0일요일))
	 *                     &nbsp;2) ApprovNo (승인번호)
	 */
	public static Map<String, String> requestMembershipSale(String telecomType,
			String cardNumber,
			int amount) {
		TelecommBroker tcBroker = new TelecommBroker();
		return tcBroker.inquireForDiscount(telecomType, cardNumber, amount);
	}
	
	/**
	 * 통신사 제휴할인에 따른 포인트 결제를 요청한다.
	 * @param telecomType  통신사 구분. KT인 경우 'K', U+인 경우 'U'
	 * @param cardNumber   멤버십 카드번호. 16자리 숫자타입
	 * @param amount       결제요청금액. 금액과 동일한 포인트를 결제요청한다.(1원당 1포인트)
	 * @param discountType 통신사가 U+인 경우는 null, KT인 경우는 아래와 같다.<br />
	 *                     사이즈업그레이드인 경우 'KT0002'
	 *                     vvip 무료 음료 매장에서만 사용 'KT0001'
	 * @return             반환 Map객체의 키"RespCd"의 값이 "0000"인 경우는 결제성공, 아니면 결제실패<br />
	 *                     성공인 경우 결제취소를 위해 아래의 값을 관리해야 한다.<br />
	 *                     &nbsp;1) ApprovDT (승인일시, YYMMDDhhmmssN (N: 0~6, 0일요일))
	 *                     &nbsp;2) ApprovNo (승인번호)
	 */
	public static Map<String, String> requestMembershipSale(String telecomType,
			String cardNumber,
			int amount,
			String yymmdd) {
		TelecommBroker tcBroker = new TelecommBroker();
		return tcBroker.inquireForDiscount(telecomType, cardNumber, amount, yymmdd);
	}
	
	/**
	 * 통신사 제휴할인에 따른 포인트 결제취소를 요청한다.
	 * @param telecomType 통신사 구분. KT인 경우 'K', U+인 경우 'U'
	 * @param cardNumber  멤버십 카드번호. 16자리 숫자타입
	 * @param amount      결제취소요청금액. 금액과 동일한 포인트를 결제취소요청한다.(1원당 1포인트)
	 * @param orgApprovNo 원인결제 승인번호
	 * @param orgSaleDate 원인결제 승인일시
	 * @return            반환 Map객체의 키"RespCd"의 값이 "0000"인 경우는 결제취소성공, 아니면 결제취소실패
	 */
	public static Map<String, String> requestMembershipSaleCancel(String telecomType,
			String cardNumber,
			int amount,
			String orgApprovNo,
			String orgSaleDate) {
		TelecommBroker tcBroker = new TelecommBroker();
		return tcBroker.inquireForDiscountCancel(telecomType, cardNumber, amount, orgApprovNo, orgSaleDate);
	}
	
	/**
	 * 통신사 제휴할인에 따른 포인트 결제취소를 요청한다.
	 * @param telecomType 통신사 구분. KT인 경우 'K', U+인 경우 'U'
	 * @param cardNumber  멤버십 카드번호. 16자리 숫자타입
	 * @param amount      결제취소요청금액. 금액과 동일한 포인트를 결제취소요청한다.(1원당 1포인트)
	 * @param orgApprovNo 원인결제 승인번호
	 * @param orgSaleDate 원인결제 승인일시
	 * @return            반환 Map객체의 키"RespCd"의 값이 "0000"인 경우는 결제취소성공, 아니면 결제취소실패
	 */
	public static Map<String, String> requestMembershipSaleCancel(String telecomType,
			String cardNumber,
			int amount,
			String orgApprovNo,
			String orgSaleDate,
			String yymmdd) {
		TelecommBroker tcBroker = new TelecommBroker();
		return tcBroker.inquireForDiscountCancel(telecomType, cardNumber, amount, orgApprovNo, orgSaleDate, yymmdd);
	}
}
