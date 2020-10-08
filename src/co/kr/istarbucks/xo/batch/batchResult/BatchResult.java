package co.kr.istarbucks.xo.batch.batchResult;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.*;

import co.kr.istarbucks.xo.batch.exception.XOException;
import co.kr.istarbucks.xo.batch.mgr.batchResult.BatchResultMgr;

/**
 * MSR_BTJB_RESULT 테이블에 BATCH 실행 모니터링 내용 및 결과 INSERT/UPDATE
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
			throw new XOException("기준일 파라미터가 잘못되었습니다.");
		}
		
		if (StringUtils.isEmpty(btjbId) || "".equals(btjbId)) {
			throw new XOException("배치작업ID 파라미터가 없습니다.");
		}
		
		if (StringUtils.isEmpty(btjbStCode) || "".equals(btjbStCode) || btjbStCode.length()>2) {
			throw new XOException("배치작업상태코드 파라미터가 잘못되었습니다.");
		}
		
		if (StringUtils.isEmpty(btjbWorkDscrt) || "".equals(btjbWorkDscrt)) {
			throw new XOException("배치작업업무설명 파라미터가 없습니다.");
		}
		
		if (StringUtils.isEmpty(pushYn) || "".equals(pushYn) || pushYn.length() != 1) {
			throw new XOException("푸시여부 파라미터가 잘못되었습니다.");
		}
	}
	
	public void validateEmptyForUpd(String baseDate, String btjbId, String btjbStCode
									, String pushYn) throws XOException {
		
		// 1. 필수값 VALIDATE
		if (StringUtils.isEmpty(baseDate) || "".equals(baseDate) || baseDate.length()<8) {
			throw new XOException("기준일자가 잘못되었습니다.");
		}
		
		if (StringUtils.isEmpty(btjbId) || "".equals(btjbId)) {
			throw new XOException("배치작업ID가 없습니다.");
		}
		
		if (StringUtils.isEmpty(btjbStCode) || "".equals(btjbStCode) || btjbStCode.length()>2) {
			throw new XOException("배치작업상태코드가 잘못되었습니다.");
		}
		
		if (StringUtils.isEmpty(pushYn) || "".equals(pushYn) || pushYn.length() != 1) {
			throw new XOException("푸시여부가 잘못되었습니다.");
		}
	}

	/**
	 * MSR_BTJB_RESULT batch 시작 insert
	 * @return insert 성공여부
	 */
	public Map<String, Object> insert(Map<String, Object> paramMap) throws XOException {
		
		int btjbSrnm = 1;
		String exceptionMsg = "배치 작업 결과 등록 실패";
		Map<String, Object> pkMap = new HashMap<String, Object>();
		
		try {
			
			// 1. 필수값 Validation 
			String baseDate 	 = (String) paramMap.get("baseDate");
			String btjbId 		 = (String) paramMap.get("btjbId");
			String btjbStCode    = (String) paramMap.get("btjbStCode");
			String btjbWorkDscrt = (String) paramMap.get("btjbWorkDscrt");
			String pushYn 		 = (String) paramMap.get("pushYn");
			
			this.validateEmpty(baseDate, btjbId, btjbStCode, btjbWorkDscrt, pushYn); // 2020.06 PMD 높은 복잡도로 인해 수정
			/*if (StringUtils.isEmpty(baseDate) || "".equals(baseDate) || baseDate.length()<8) {
				throw new XOException("기준일 파라미터가 잘못되었습니다.");
			}
			
			if (StringUtils.isEmpty(btjbId) || "".equals(btjbId)) {
				throw new XOException("배치작업ID 파라미터가 없습니다.");
			}
			
			if (StringUtils.isEmpty(btjbStCode) || "".equals(btjbStCode) || btjbStCode.length()>2) {
				throw new XOException("배치작업상태코드 파라미터가 잘못되었습니다.");
			}
			
			if (StringUtils.isEmpty(btjbWorkDscrt) || "".equals(btjbWorkDscrt)) {
				throw new XOException("배치작업업무설명 파라미터가 없습니다.");
			}
			
			if (StringUtils.isEmpty(pushYn) || "".equals(pushYn) || pushYn.length() != 1) {
				throw new XOException("푸시여부 파라미터가 잘못되었습니다.");
			}
			*/
			// 2. 배치순번 조회
			btjbSrnm = this.getBtjbSrnm(paramMap); 
			if (btjbSrnm<1) {
				throw new XOException("다음배치작업순번이 잘못되었습니다.");
			}
			paramMap.put("btjbSrnm", btjbSrnm);
			
			// 2. insert
			batchResultMgr.insert(paramMap);
		
			// 3. pk 리턴
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
	 * 다음 배치작업순번 조회
	 */
	public int getBtjbSrnm(Map<String, Object> paramMap) throws SQLException, XOException {
		
		int returnInt = 0;
		returnInt = batchResultMgr.getBtjbSrnm(paramMap);
		
		if (returnInt<1) {
			throw new XOException("배치작업순번이 잘못되었습니다.");
		}
		
		return returnInt;
	}
	
	
	/**
	 * MSR_BTJB_RESULT 배치 실행 모니터링 결과 update
	 */
	public boolean update(Map<String, Object> paramMap) throws XOException {
		
		boolean returnBoolean = true;
		String exceptionMsg = "배치 작업 결과 업데이트 실패";
		
		try {
			
			String baseDate   = (String) paramMap.get("baseDate");
			String btjbId     = (String) paramMap.get("btjbId");
			String btjbStCode = (String) paramMap.get("btjbStCode");
			String pushYn 	  = (String) paramMap.get("pushYn");
			
			// 1. 필수값 VALIDATE
			this.validateEmptyForUpd(baseDate, btjbId, btjbStCode, pushYn); // 2020.06 PMD 높은 복잡도로 인해 수정
			/*if (StringUtils.isEmpty(baseDate) || "".equals(baseDate) || baseDate.length()<8) {
				throw new XOException("기준일자가 잘못되었습니다.");
			}
			
			if (StringUtils.isEmpty(btjbId) || "".equals(btjbId)) {
				throw new XOException("배치작업ID가 없습니다.");
			}
			
			if (StringUtils.isEmpty(btjbStCode) || "".equals(btjbStCode) || btjbStCode.length()>2) {
				throw new XOException("배치작업상태코드가 잘못되었습니다.");
			}
			
			if (StringUtils.isEmpty(pushYn) || "".equals(pushYn) || pushYn.length() != 1) {
				throw new XOException("푸시여부가 잘못되었습니다.");
			}
			*/
			// 2. update : btjbStCode = 'S' (상태코드가 성공이면 종료일시 SYSDATE로 업데이트) 
			int updateResult = batchResultMgr.update(paramMap);
			if (updateResult!=1) {
				throw new XOException("배치작업 업데이트에 실패하였습니다.");
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
