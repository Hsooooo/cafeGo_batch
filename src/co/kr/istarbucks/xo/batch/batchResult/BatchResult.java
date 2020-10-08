package co.kr.istarbucks.xo.batch.batchResult;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.*;

import co.kr.istarbucks.xo.batch.exception.XOException;
import co.kr.istarbucks.xo.batch.mgr.batchResult.BatchResultMgr;

/**
 * MSR_BTJB_RESULT ���̺� BATCH ���� ����͸� ���� �� ��� INSERT/UPDATE
 * @author ezens
 */
public class BatchResult {

	transient private BatchResultMgr batchResultMgr;
	
	/* Getter|Setter */
	public BatchResultMgr getBrMgr() {return batchResultMgr;}
	public void setBatchResultMgr(BatchResultMgr batchResultMgr) {this.batchResultMgr = batchResultMgr;}

	public BatchResult() {
		batchResultMgr = new BatchResultMgr();
	}
	
	public void validateEmpty(String baseDate, String btjbId, String btjbStCode
			, String btjbWorkDscrt, String pushYn) throws XOException {
		if (StringUtils.isEmpty(baseDate) || "".equals(baseDate) || baseDate.length()<8) {
			throw new XOException("������ �Ķ���Ͱ� �߸��Ǿ����ϴ�.");
		}
		
		if (StringUtils.isEmpty(btjbId) || "".equals(btjbId)) {
			throw new XOException("��ġ�۾�ID �Ķ���Ͱ� �����ϴ�.");
		}
		
		if (StringUtils.isEmpty(btjbStCode) || "".equals(btjbStCode) || btjbStCode.length()>2) {
			throw new XOException("��ġ�۾������ڵ� �Ķ���Ͱ� �߸��Ǿ����ϴ�.");
		}
		
		if (StringUtils.isEmpty(btjbWorkDscrt) || "".equals(btjbWorkDscrt)) {
			throw new XOException("��ġ�۾��������� �Ķ���Ͱ� �����ϴ�.");
		}
		
		if (StringUtils.isEmpty(pushYn) || "".equals(pushYn) || pushYn.length() != 1) {
			throw new XOException("Ǫ�ÿ��� �Ķ���Ͱ� �߸��Ǿ����ϴ�.");
		}
	}
	
	public void validateEmptyForUpd(String baseDate, String btjbId, String btjbStCode
									, String pushYn) throws XOException {
		
		// 1. �ʼ��� VALIDATE
		if (StringUtils.isEmpty(baseDate) || "".equals(baseDate) || baseDate.length()<8) {
			throw new XOException("�������ڰ� �߸��Ǿ����ϴ�.");
		}
		
		if (StringUtils.isEmpty(btjbId) || "".equals(btjbId)) {
			throw new XOException("��ġ�۾�ID�� �����ϴ�.");
		}
		
		if (StringUtils.isEmpty(btjbStCode) || "".equals(btjbStCode) || btjbStCode.length()>2) {
			throw new XOException("��ġ�۾������ڵ尡 �߸��Ǿ����ϴ�.");
		}
		
		if (StringUtils.isEmpty(pushYn) || "".equals(pushYn) || pushYn.length() != 1) {
			throw new XOException("Ǫ�ÿ��ΰ� �߸��Ǿ����ϴ�.");
		}
	}

	/**
	 * MSR_BTJB_RESULT batch ���� insert
	 * @return insert ��������
	 */
	public Map<String, Object> insert(Map<String, Object> paramMap) throws XOException {
		
		int btjbSrnm = 1;
		String exceptionMsg = "��ġ �۾� ��� ��� ����";
		Map<String, Object> pkMap = new HashMap<String, Object>();
		
		try {
			
			// 1. �ʼ��� Validation 
			String baseDate 	 = (String) paramMap.get("baseDate");
			String btjbId 		 = (String) paramMap.get("btjbId");
			String btjbStCode    = (String) paramMap.get("btjbStCode");
			String btjbWorkDscrt = (String) paramMap.get("btjbWorkDscrt");
			String pushYn 		 = (String) paramMap.get("pushYn");
			
			this.validateEmpty(baseDate, btjbId, btjbStCode, btjbWorkDscrt, pushYn); // 2020.06 PMD ���� ���⵵�� ���� ����
			/*if (StringUtils.isEmpty(baseDate) || "".equals(baseDate) || baseDate.length()<8) {
				throw new XOException("������ �Ķ���Ͱ� �߸��Ǿ����ϴ�.");
			}
			
			if (StringUtils.isEmpty(btjbId) || "".equals(btjbId)) {
				throw new XOException("��ġ�۾�ID �Ķ���Ͱ� �����ϴ�.");
			}
			
			if (StringUtils.isEmpty(btjbStCode) || "".equals(btjbStCode) || btjbStCode.length()>2) {
				throw new XOException("��ġ�۾������ڵ� �Ķ���Ͱ� �߸��Ǿ����ϴ�.");
			}
			
			if (StringUtils.isEmpty(btjbWorkDscrt) || "".equals(btjbWorkDscrt)) {
				throw new XOException("��ġ�۾��������� �Ķ���Ͱ� �����ϴ�.");
			}
			
			if (StringUtils.isEmpty(pushYn) || "".equals(pushYn) || pushYn.length() != 1) {
				throw new XOException("Ǫ�ÿ��� �Ķ���Ͱ� �߸��Ǿ����ϴ�.");
			}
			*/
			// 2. ��ġ���� ��ȸ
			btjbSrnm = this.getBtjbSrnm(paramMap); 
			if (btjbSrnm<1) {
				throw new XOException("������ġ�۾������� �߸��Ǿ����ϴ�.");
			}
			paramMap.put("btjbSrnm", btjbSrnm);
			
			// 2. insert
			batchResultMgr.insert(paramMap);
		
			// 3. pk ����
			pkMap.put("baseDate", baseDate);
			pkMap.put("btjbId"  , btjbId);
			pkMap.put("btjbSrnm", btjbSrnm);

		} catch (XOException e) {
			throw e;
		} catch (ArrayIndexOutOfBoundsException e) {
			exceptionMsg = e.getMessage();
			throw new XOException(exceptionMsg);
		} catch (StringIndexOutOfBoundsException e) {
			exceptionMsg = e.getMessage();
			throw new XOException(exceptionMsg);
		} catch (SQLException e) {
			exceptionMsg = e.getMessage();
			throw new XOException(exceptionMsg);
		} catch (NumberFormatException e) {
			exceptionMsg = e.getMessage();
			throw new XOException(exceptionMsg);
		} 
		return pkMap;
	}
	
	
	/**
	 * ���� ��ġ�۾����� ��ȸ
	 */
	public int getBtjbSrnm(Map<String, Object> paramMap) throws SQLException, XOException {
		
		int returnInt = 0;
		returnInt = batchResultMgr.getBtjbSrnm(paramMap);
		
		if (returnInt<1) {
			throw new XOException("��ġ�۾������� �߸��Ǿ����ϴ�.");
		}
		
		return returnInt;
	}
	
	
	/**
	 * MSR_BTJB_RESULT ��ġ ���� ����͸� ��� update
	 */
	public boolean update(Map<String, Object> paramMap) throws XOException {
		
		boolean returnBoolean = true;
		String exceptionMsg = "��ġ �۾� ��� ������Ʈ ����";
		
		try {
			
			String baseDate   = (String) paramMap.get("baseDate");
			String btjbId     = (String) paramMap.get("btjbId");
			String btjbStCode = (String) paramMap.get("btjbStCode");
			String pushYn 	  = (String) paramMap.get("pushYn");
			
			// 1. �ʼ��� VALIDATE
			this.validateEmptyForUpd(baseDate, btjbId, btjbStCode, pushYn); // 2020.06 PMD ���� ���⵵�� ���� ����
			/*if (StringUtils.isEmpty(baseDate) || "".equals(baseDate) || baseDate.length()<8) {
				throw new XOException("�������ڰ� �߸��Ǿ����ϴ�.");
			}
			
			if (StringUtils.isEmpty(btjbId) || "".equals(btjbId)) {
				throw new XOException("��ġ�۾�ID�� �����ϴ�.");
			}
			
			if (StringUtils.isEmpty(btjbStCode) || "".equals(btjbStCode) || btjbStCode.length()>2) {
				throw new XOException("��ġ�۾������ڵ尡 �߸��Ǿ����ϴ�.");
			}
			
			if (StringUtils.isEmpty(pushYn) || "".equals(pushYn) || pushYn.length() != 1) {
				throw new XOException("Ǫ�ÿ��ΰ� �߸��Ǿ����ϴ�.");
			}
			*/
			// 2. update : btjbStCode = 'S' (�����ڵ尡 �����̸� �����Ͻ� SYSDATE�� ������Ʈ) 
			int updateResult = batchResultMgr.update(paramMap);
			if (updateResult!=1) {
				throw new XOException("��ġ�۾� ������Ʈ�� �����Ͽ����ϴ�.");
			}
			
		} catch (XOException e) {
			throw e;
		} catch (ArrayIndexOutOfBoundsException e) {
			exceptionMsg = e.getMessage();
			throw new XOException(exceptionMsg);
		} catch (StringIndexOutOfBoundsException e) {
			throw new XOException(exceptionMsg);
		} catch (SQLException e) {
			throw new XOException(exceptionMsg);
		} catch (NumberFormatException e) {
			throw new XOException(exceptionMsg);
		}
		return returnBoolean;
	}
	
}
