package co.kr.istarbucks.xo.batch.common.dto.inbox;

@SuppressWarnings("PMD.AvoidConstantsInterface")
public interface InboxType {

	/**
	 * ���Ű���
	 * @author sinjw
	 *
	 */
	public interface PURCHASE {
		
		/**
		 *  ��Ÿ���� ���� �湮 �� ���� �̷�
		 */
//		public Object[] VISIT = {"A-1", false, false };
		
		/**
		 * �Ϲ� ī�� ��� �Ϸ�
		 */
//		public Object[] CARD_REGISTER = {"A-2", false, false };
		
		/**
		 * ī�� �ڵ�����(������)
		 */
		public Object[] CARD_AUTOCHARGE_DAY = {"A-3", true, true };

		/**
		 * ī�� �ڵ�����(��������)
		 */
		public Object[] CARD_AUTOCHARGE_LOWEST_AMT = {"A-4", true, true };
		
		/**
		 * ��Ÿ���� ī�� ȯ�� �Ϸ�
		 */
		public Object[] CARD_REFUND = {"A-5", true, true };
		
		/**
		 * eGift ����
		 */
		public Object[] EGIFT_SEND = {"A-6", true, true };
		
		/**
		 * eGift ���
		 */
		public Object[] EGIFT_CANCEL = {"A-7", true, true };		
	}
	
	/**
	 * ���ȸ��
	 * @author sinjw
	 *
	 */
	public interface VIP_MEMBER {
		
		/**
		 * ����� ����Ʈ ��û �ȳ�
		 */
		public Object[] GIFT_APPLICATION  = {"B-1", true, true };
		
		/**
		 * ����� ����Ʈ ����
		 */
		public Object[] GIFT_ARRIVAL  = {"B-2", true, true };
	}
	
	/**
	 * �̺�Ʈ
	 * @author sinjw
	 *
	 */
	public interface EVENT {
		
		/**
		 * ���� ���θ�� ���� �˸�
		 */
		public Object[] PROMOTION_START = {"C-1", true, true };
	}
	
	/**
	 * �������� ī�װ�
	 */
	public interface MEMBER_INFO {
		
		/**
		 * MSR ���� �����
		 */
		public Object[] FIRST_CARD_REGISTER = {"E-2", true, true };
	}
	
	/**
	 * Ȧ����
	 * @author sinjw
	 *
	 */
	public interface WHOLE_CAKE  {
		
		/**
		 * Ȧ ���� �ֹ� Ȯ�� 
		 */
		public Object[] ORDER_CONFIRM = { "G-1", true, true };

		/**
		 * Ȧ ���� ���� ����
		 */
		public Object[] ARRIVAL = { "G-2", true, true };
	}
	
	/**
	 * ���ī��
	 * @author sinjw
	 *
	 */
	public interface GOLD_CARD {
		
		/**
		 * ���ī�� ��� �Ϸ�
		 */
		public Object[] REGISTER = {"H-1", false, false };
		
		/**
		 * ���ī�� ����
		 */
		public Object[] ARRIVAL = {"H-2", true, true };
	}
	
	/**
	 * ���� ���� �˸�
	 * @author sinjw
	 *
	 */
	public interface LEVEL {
		/**
		 * ����->�׸�<br>
		 * �±�
		 */
		public Object[] WelcomeToGreen_MODIFY = {"J-1", true, true };
		/**
		 * ����->���<br>
		 * �׸�->���<br>
		 * �±�
		 */
		public Object[] WGToGold_MODIFY = {"J-2", true, true };
		/**
		 * ���->�׸�<br>
		 * ����
		 */
		public Object[] GoldToGreen_MODIFY = {"J-3", true, true };
		/**
		 * ���->���<br>
		 * ����
		 */
		public Object[] GoldToGold_MODIFY = {"J-4", true, true };
	}
	
	/**
	 * ���� 
	 * @author sinjw
	 *
	 */
	public interface COUPON {
		
		/**
		 * e-���� ���� �ȳ� 
		 */
		public Object[] EXPIRE = {"K-1", true, true };
	}

	/**
	 * ��ǰ�� GIFT
	 * @author sinjw
	 *
	 */
	public interface E_GIFT_ITEM {
		
		/**
		 * ������ ��ȿ�Ⱓ����
		 */
		public Object[] RECEIVE_NOTICE = {"L-20", true, true };
		
		/**
		 * ������ ��ȿ�Ⱓ����
		 */
		public Object[] SEND_NOTICE = {"L-30", true, true };
		
		/**
		 * ���� ��������(����)
		 */
		public Object[] RESERVE_NOTICE = {"L-6", true, true };
	}
}
