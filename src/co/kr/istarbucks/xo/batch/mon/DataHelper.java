package co.kr.istarbucks.xo.batch.mon;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import co.kr.istarbucks.xo.batch.batchResult.BatchResult;
import co.kr.istarbucks.xo.batch.common.exception.CustomException;
import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;
import co.kr.istarbucks.xo.batch.exception.XOException;


public class DataHelper {
	
	private static Log log   = LogFactory.getLog("batchData");
	
	transient BatchResult batchResult;
	transient int btjbSrnmVal; // 배치순번(Default:1)
	
	public DataHelper(){
	
		batchResult = new BatchResult();
	
	}
	
	protected void validateStr(String str) {
		if (str==null||"".equals(str)) {
			throw new CustomException("로그 파라미터가 없습니다.");
		}
	}
	
	/**
	 * 배치 모니터링 로그 insert(start)
	 * @throws MsrException
	 */
	public int startMonitor(String baseDate, int btjbSrnm, String BTJB_ID, String BTJB_WORK_DSCRT ) throws XOException {
		int btjbSrnmForInsert = btjbSrnm;
		try {
			
			Map<String, Object> paramMap = new HashMap<String, Object>();

			//Configuration conf = co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration.getConfiguration("batch.monitor.properties");
			Configuration conf = CommPropertiesConfiguration.getConfiguration("batch.monitor.properties");
			String str_service = conf.getString(BTJB_ID);	
			String str_service_det = conf.getString(BTJB_WORK_DSCRT);
			
			log.info(str_service+","+str_service_det);
			
			
			paramMap.put("baseDate"		, baseDate);       //20200513
			paramMap.put("btjbId"		, str_service);        //LPRDataDelete
			paramMap.put("btjbStCode"	, "I");            //S
			paramMap.put("btjbWorkDscrt", str_service_det);//LRP 데이터 삭제 배치
			paramMap.put("pushYn"		, "Y");
			
			Map<String, Object> pkMap = batchResult.insert(paramMap); //helper.startMonitor(today, 2, "ExpireCouponUser", "ExpireCouponUser PUSH");
			btjbSrnmForInsert = (Integer) pkMap.get("btjbSrnm");
			
			log.info("* 배치 모니터링 시작 : 기준일 ["+baseDate.replaceAll("[\r\n]", "")+"]"
					+ ", 배치작업ID ["+BTJB_ID.replaceAll("[\r\n]", "")+"]"
					+ ", 배치순번 [" +btjbSrnm +"]"
					+ ", 배치작업상태코드 [I]"
					+ ", 푸시여부 [Y]");
		} catch (XOException e) {
			throw e; 
		}
		return btjbSrnmForInsert;
	}
	

	
	
	// 배치 모니터링 로그 종료용 update(end)
		public void endMonitor(String baseDate, int btjbSrnm, String BTJB_ID, String BTJB_WORK_DSCRT,int wrkngTrgCscnt, int wrkngScsCscnt, int wrkngFlrCscnt, String ErrorCntnt ) throws XOException {
			log.info("");
			String exceptionMsg = "배치 모니터링  종료 처리 실패";
			Map<String, Object> paramMap = new HashMap<String, Object>();
			
			Configuration conf = CommPropertiesConfiguration.getConfiguration("batch.monitor.properties");
			String str_service = conf.getString(BTJB_ID);	

			
			try {

				String tempBaseDate = baseDate;
				int tempBtjbSrnm = btjbSrnm; 
				// 집계		helper.endMonitor(today, 2, "ExpireCouponUser", "ExpireCouponUser PUSH",  memberList.size(), 100, 101);
				paramMap.put("baseDate", tempBaseDate); //today
				paramMap.put("btjbId"  , str_service); // ExpireCouponUser
				paramMap.put("btjbSrnm", tempBtjbSrnm); // 2
				paramMap.put("btjbStCode"   , "S");
				paramMap.put("pushYn"		, "N");
				paramMap.put("wrkngTrgCscnt", wrkngTrgCscnt);
				paramMap.put("wrkngScsCscnt", wrkngScsCscnt);
				paramMap.put("wrkngFlrCscnt", wrkngFlrCscnt);			
				paramMap.put("errorCntnt"	, ErrorCntnt);
				
				log.info("* 배치 모니터링 종료 : 기준일 ["+baseDate.replaceAll("[\r\n]", "")+"]"+ ", 배치작업ID ["+str_service.replaceAll("[\r\n]", "")+"]"
						+ ", 배치순번 [" +btjbSrnm +"]"+ ", 배치작업상태코드 [S]"
						+ ", 작업 대상 건수 ["+wrkngTrgCscnt+"]"
						+ ", 작업 성공 건수 ["+wrkngScsCscnt+"]"+ ", 작업 실패 건수 ["+wrkngFlrCscnt+"]"+ ", 푸시여부 [Y]");
				boolean updateResult = batchResult.update(paramMap);
				if (!updateResult) {
					throw new XOException(exceptionMsg);
				}
			} catch (XOException e) {
				throw e;
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new XOException(exceptionMsg);
			} catch (StringIndexOutOfBoundsException e) {
				throw new XOException(exceptionMsg);
			}
		}

		public BatchResult getBatchResult() {return batchResult;}
		public void setBatchResult(BatchResult batchResult) {this.batchResult = batchResult;}
	
}

