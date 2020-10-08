package co.kr.istarbucks.xo.batch.common.telecomm;

import java.util.Map;


/**
 * ��Ż� ���������� ���� VAN ���� ����
 */
public class TelecommService {
	
	/**
	 * ��Ż��� ����� ���θ� ��ȸ�Ѵ�.
	 * @param telecomType ��Ż� ����. KT�� ��� 'K', U+�� ��� 'U'
	 * @param cardNumber  ����� ī���ȣ. 16�ڸ� ����Ÿ��
	 * @return ��ȯ Map��ü�� Ű"RespCd"�� ���� "0000"�� ���� ȸ��, �ƴϸ� ȸ���ƴ�
	 */
	public static Map<String, String> inquireMembershipPoint(String telecomType, String cardNumber) {
		TelecommBroker tcBroker = new TelecommBroker();
		return tcBroker.inquireForPoint(telecomType, cardNumber);		
	}
	
	/**
	 * KT ��Ż��� ����� ���θ� ��ȸ�Ѵ�.
	 * @param telecomType ��Ż� ����. KT�� ��� 'K'
	 * @param cardNumber  ����� ī���ȣ. 16�ڸ� ����Ÿ��
	 * @param yymmdd �������
	 * @return ��ȯ Map��ü�� Ű"RespCd"�� ���� "0000"�� ���� ȸ��, �ƴϸ� ȸ���ƴ�
	 */
	public static Map<String, String> inquireMembershipPoint(String telecomType, String cardNumber, String yymmdd) {
		TelecommBroker tcBroker = new TelecommBroker();
		return tcBroker.inquireForPoint(telecomType, cardNumber, yymmdd);		
	}

	/**
	 * ��Ż� �������ο� ���� ����Ʈ ������ ��û�Ѵ�.
	 * @param telecomType  ��Ż� ����. KT�� ��� 'K', U+�� ��� 'U'
	 * @param cardNumber   ����� ī���ȣ. 16�ڸ� ����Ÿ��
	 * @param amount       ������û�ݾ�. �ݾװ� ������ ����Ʈ�� ������û�Ѵ�.(1���� 1����Ʈ)
	 * @param discountType ��Ż簡 U+�� ���� null, KT�� ���� �Ʒ��� ����.<br />
	 *                     ��������׷��̵��� ��� 'KT0002'
	 * @return             ��ȯ Map��ü�� Ű"RespCd"�� ���� "0000"�� ���� ��������, �ƴϸ� ��������<br />
	 *                     ������ ��� ������Ҹ� ���� �Ʒ��� ���� �����ؾ� �Ѵ�.<br />
	 *                     &nbsp;1) ApprovDT (�����Ͻ�, YYMMDDhhmmssN (N: 0~6, 0�Ͽ���))
	 *                     &nbsp;2) ApprovNo (���ι�ȣ)
	 */
	public static Map<String, String> requestMembershipSale(String telecomType,
			String cardNumber,
			int amount) {
		TelecommBroker tcBroker = new TelecommBroker();
		return tcBroker.inquireForDiscount(telecomType, cardNumber, amount);
	}
	
	/**
	 * ��Ż� �������ο� ���� ����Ʈ ������ ��û�Ѵ�.
	 * @param telecomType  ��Ż� ����. KT�� ��� 'K', U+�� ��� 'U'
	 * @param cardNumber   ����� ī���ȣ. 16�ڸ� ����Ÿ��
	 * @param amount       ������û�ݾ�. �ݾװ� ������ ����Ʈ�� ������û�Ѵ�.(1���� 1����Ʈ)
	 * @param discountType ��Ż簡 U+�� ���� null, KT�� ���� �Ʒ��� ����.<br />
	 *                     ��������׷��̵��� ��� 'KT0002'
	 *                     vvip ���� ���� ���忡���� ��� 'KT0001'
	 * @return             ��ȯ Map��ü�� Ű"RespCd"�� ���� "0000"�� ���� ��������, �ƴϸ� ��������<br />
	 *                     ������ ��� ������Ҹ� ���� �Ʒ��� ���� �����ؾ� �Ѵ�.<br />
	 *                     &nbsp;1) ApprovDT (�����Ͻ�, YYMMDDhhmmssN (N: 0~6, 0�Ͽ���))
	 *                     &nbsp;2) ApprovNo (���ι�ȣ)
	 */
	public static Map<String, String> requestMembershipSale(String telecomType,
			String cardNumber,
			int amount,
			String yymmdd) {
		TelecommBroker tcBroker = new TelecommBroker();
		return tcBroker.inquireForDiscount(telecomType, cardNumber, amount, yymmdd);
	}
	
	/**
	 * ��Ż� �������ο� ���� ����Ʈ ������Ҹ� ��û�Ѵ�.
	 * @param telecomType ��Ż� ����. KT�� ��� 'K', U+�� ��� 'U'
	 * @param cardNumber  ����� ī���ȣ. 16�ڸ� ����Ÿ��
	 * @param amount      ������ҿ�û�ݾ�. �ݾװ� ������ ����Ʈ�� ������ҿ�û�Ѵ�.(1���� 1����Ʈ)
	 * @param orgApprovNo ���ΰ��� ���ι�ȣ
	 * @param orgSaleDate ���ΰ��� �����Ͻ�
	 * @return            ��ȯ Map��ü�� Ű"RespCd"�� ���� "0000"�� ���� ������Ҽ���, �ƴϸ� ������ҽ���
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
	 * ��Ż� �������ο� ���� ����Ʈ ������Ҹ� ��û�Ѵ�.
	 * @param telecomType ��Ż� ����. KT�� ��� 'K', U+�� ��� 'U'
	 * @param cardNumber  ����� ī���ȣ. 16�ڸ� ����Ÿ��
	 * @param amount      ������ҿ�û�ݾ�. �ݾװ� ������ ����Ʈ�� ������ҿ�û�Ѵ�.(1���� 1����Ʈ)
	 * @param orgApprovNo ���ΰ��� ���ι�ȣ
	 * @param orgSaleDate ���ΰ��� �����Ͻ�
	 * @return            ��ȯ Map��ü�� Ű"RespCd"�� ���� "0000"�� ���� ������Ҽ���, �ƴϸ� ������ҽ���
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
