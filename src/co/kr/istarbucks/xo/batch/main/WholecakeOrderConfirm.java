
package co.kr.istarbucks.xo.batch.main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import co.kr.istarbucks.xo.batch.common.dto.xo.SmtTranDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.WholecakeOrderDto;
import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;
import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;
import co.kr.istarbucks.xo.batch.mgr.MMSMgr;
import co.kr.istarbucks.xo.batch.mgr.ScksaWholecakeOrderMgr;
import co.kr.istarbucks.xo.batch.mgr.XoWholecakeOrderMgr;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 홀케익 발주 확정 정보 등록 배치
 * TODO Insert type comment for WholecakeOrderConfirm.
 *
 * @author namgu1
 * @version $Revision: 1.6 $
 */
public class WholecakeOrderConfirm {
    
private static Log wLogger = LogFactory.getLog ("WHOLECAKE_ORDER_CONFIRM");
private static Log wErrorLogger = LogFactory.getLog ("WHOLECAKE_ORDER_CONFIRM_ERROR");

	private final ScksaWholecakeOrderMgr scksaWholecakeOrderMgr;
	private final XoWholecakeOrderMgr xoWholecakeOrderMgr;
	private final MMSMgr mmsMgr;
	
	private final String loggerTitle;
	private List<WholecakeOrderDto> wholecakeOrderDtoList = new ArrayList<WholecakeOrderDto>();
	private final List<WholecakeOrderDto> orderDtoList = new ArrayList<WholecakeOrderDto>();
	private String orderNoTemp = ""; 
	private final StringBuffer logSb;
	private int totalQtyCount = 0;     // 발주등록 상세 건수
	private int failTotalQtyCount = 0; // 발주 등록 실패 상세 건수
	private int failQtyCount = 0; 
	
	private Configuration conf = null;
	
	public WholecakeOrderConfirm () {
		this.scksaWholecakeOrderMgr = new ScksaWholecakeOrderMgr();
		this.xoWholecakeOrderMgr = new XoWholecakeOrderMgr();
		this.mmsMgr = new MMSMgr();
		
		this.loggerTitle = "[WholecakeOrderConfirm] ";
		this.logSb = new StringBuffer (); // log용 StringBuffer
	}
	
	/**
	 * 1. 발주 확정일자가 오늘인 홀케익 예약정보 조회
	 * 2. 발주정보 등록(사이렌오더 서버 -> 영업정보)
	 * 3. 마지막 남은 예약정보 처리(비교대상이 없으므로 마지막 남은 예약정보를 별도 처리)
	 * 4. 결과 로그 출력
	 */
	public void start (String salesOrderDate) {
		long startTime = System.currentTimeMillis ();
		
		StringBuffer failInfo = new StringBuffer ();
		
		int successCnt = 0;  // 성공카운터
		int failCnt    = 0;  // 실패카운터
		String userId  = "";
		String orderNo = "";
		
		
		logSb.delete(0, logSb.length ()).append (this.loggerTitle).append ("START");
		wLogger.info (logSb.toString ());
		
		// 1. 발주 확정일자가 오늘인 홀케익 예약정보 조회
		//    발주 확정일자 날짜가 있다면 해당 날짜의 예약정보 조회
		Map<String, Object> dbMap = new HashMap<String, Object> ();
		dbMap.put("salesOrderDate", salesOrderDate);
		
        this.getTodayWholecakeOrderList(dbMap);

        if(wholecakeOrderDtoList != null && wholecakeOrderDtoList.size() > 0) {
        	
	        for(WholecakeOrderDto dto : wholecakeOrderDtoList){
	            boolean isWholecakeOrder = false;
	
	        	// orderNoTemp의 초기값은 dto.getOrder_no 값으로 넣는다.
	            if(StringUtils.isBlank(orderNoTemp)) {
	        		orderNoTemp = dto.getOrder_no();
	        	}
	        	
	        	// 같은 order_no만 orderDtoList에 add 처리
	    		if(orderNoTemp.equals(dto.getOrder_no())) {
	    			orderDtoList.add(dto);
	    		} else {
	    			
	    			try {

		    			// 2. 발주정보 등록(사이렌오더 서버 -> 영업정보)
		    			// 2. 홀케익 예약정보 업데이트(사이렌오더 서버)	    				
		    			// 2. 다른 주문번호인 경우 기존에 orderDtoList 값을 발주정보 등록
	    				isWholecakeOrder = setWholecakeOrder(orderDtoList);
		    			userId      = orderDtoList.get(0).getUser_id();
		    			orderNo     = orderDtoList.get(0).getOrder_no();
	                } catch ( Exception e ) {
	                	logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Error : ").append (userId).append ("|").append (orderNo).append ("|").append("\n").append("Exception : ").append (e.getMessage()).append("\n");
	                	wLogger.info (logSb.toString ());
	                	wErrorLogger.error (logSb.toString());
	                }
	
	    			// 영업정보 발주 정보 및 홀케익 예약정보 업데이트 성공
	    			if(isWholecakeOrder) {
	    				successCnt++;
	    				
	    			} else {
	    			// 영업정보 발주 정보 및 홀케익 예약정보 업데이트 실패
	                    failCnt++;
	                    if ( failInfo.length () > 0 ) {
	                    	failInfo.append ("\n\t\t\t\t\t     ");
	                    }
	                    failInfo.append (userId)
	                            .append ("|").append (orderNo)
	                            .append (";");
	    			}
	    			
	    			// 처리 후 orderDtoList 초기화 후 다음 주문번호의 예약정보를 orderDtoList에 add 처리
	    			// orderNoTemp에 값도 다음 주문번호로 초기화
	    			orderDtoList.clear();   // 초기화
	    			orderDtoList.add(dto);  // 목록에 추가
	    			orderNoTemp = dto.getOrder_no(); // 예약 주문정보 임시값에 저장
	    		}
	        }
	        

	        // 3. 마지막 남은 예약정보 처리(비교대상이 없으므로 마지막 남은 예약정보를 별도 처리)
			if(orderDtoList != null && orderDtoList.size() > 0) {
	            boolean isWholecakeOrder = false;
	            
	            try {
	            	isWholecakeOrder = setWholecakeOrder(orderDtoList);
	            	
	    			userId      = orderDtoList.get(0).getUser_id();
	    			orderNo     = orderDtoList.get(0).getOrder_no();

	            } catch ( Exception e ) {
                	logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Error : ").append (userId).append ("|").append (orderNo).append ("|").append("\n").append("Exception : ").append (e.getMessage()).append("\n");
                	wLogger.info (logSb.toString ());
                	wErrorLogger.error (logSb.toString());
	            }
	            
				// 영업정보 발주 정보 및 홀케익 예약정보 업데이트 성공
				if(isWholecakeOrder) {
					successCnt++;
					
				} else {
				// 실패
	                failCnt++;
                    if ( failInfo.length () > 0 ) {
                        failInfo.append ("\n\t\t\t\t\t     ");
                    }
	                failInfo.append (userId)
	                        .append ("|").append (orderNo)
	                        .append (";");
				}	
			}
        }
        
        // 4. 결과 로그 출력
        logSb.delete(0, logSb.length ()).insert (0, "=================================================")
                                     .append ("\r\n\t\t\t    ").append ("성공          : ").append (successCnt).append ("건")
                                     .append ("\r\n\t\t\t    ").append ("실패          : ").append (failCnt).append ("건")
                                     .append ("\r\n\t\t\t    ").append ("실패 정보   : ").append (new String (failInfo))
                                     .append ("\r\n\t\t\t    =================================================");
		wLogger.info (logSb.toString ());
		
		// 5. 결과 SMS 발송
		this.smsSend(successCnt, failCnt, salesOrderDate);
		
		logSb.delete(0, logSb.length ()).append (this.loggerTitle).append ("END : ").append (System.currentTimeMillis () - startTime).append ("ms");
		wLogger.info (logSb.toString ());
	}


	/**
	 * 발주정보 등록(사이렌오더 서버 -> 영업정보)
	 * 홀케익 예약정보 업데이트(사이렌오더 서버)
	 * @param orderDtoList 
	 * @return 
	 * @throws SQLException
	 */
	private boolean setWholecakeOrder(List<WholecakeOrderDto> orderDtoList) throws Exception {
		
		SqlMapClient scksaSqlMap = IBatisSqlConfig.getScksasqlMapInstance(); // 영업정보(SCKSA) 트렌젝션
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();        // 사이렌오더 트렌젝션
		
        boolean isWholecakeOrder = false;
        boolean isOk = false;
        boolean isFirst = true;
        String userId = "";
        String orderNo = "";
        int dtoSize = orderDtoList.size();
        int detailCount = 1; 
        String dbTable = "";
        String errorMessage = "";
        
		try {
			// 트렌젝션 시작구간
			sqlMap.startTransaction();
			scksaSqlMap.startTransaction();
			
			for(WholecakeOrderDto dto : orderDtoList){

				// 발주정보 등록(사이렌오더 서버 -> 영업정보) 
				// orderDtoList의 값은 하나의 예약정보 + 예약정보 상세를 합친 데이터 이므로 등록시 주의해야한다.
				// 발주정보 등록시 중복을 막기 위해 예약정보 첫 조회값만 가지고 등록 처리한다.
				// XO_ORDER_WHOLECAKE, XO_ORDER_HISTORY_WHOLECAKE의 중복 업데이트 및 히스토리 등록을 막기위해 첫 조회값만 가지고 처리한다.

				if(isFirst) {
					
					userId = dto.getUser_id();
					orderNo = dto.getOrder_no();
					
					// 발주일자 로그
					logSb.delete(0, logSb.length()).append(this.loggerTitle).append("발주 확정일자 : ").append(dto.getSales_order_date()).append(", 발주일자 : ").append(dto.getOrder_date());
					wLogger.info (logSb.toString ());
					
					// 예약정보 로그
					logSb.delete(0, logSb.length()).append(this.loggerTitle).append("아이디 : ").append(userId).append(", 예약번호 : ").append(orderNo).append(", 수령일자 : ").append(dto.getReceive_date()).append(", 수령매장 : ").append(dto.getReceive_store_cd()).append(", 예약건수 : ").append(dto.getTotal_qty());
					wLogger.info (logSb.toString ());
					
					// 발주 상세 건수
					totalQtyCount += dto.getTotal_qty();
					// 발주 등록 실패시 totalQty 카운터 삭제 용도
					failQtyCount = dto.getTotal_qty();
							
					// 1. 발주정보 등록(사이렌오더 서버 -> 영업정보)
					dbTable = "Insert ICS_XO_ORDER_WCAKE ";
					errorMessage = "발주정보 등록(사이렌오더 서버 -> 영업정보) 실패";
					isOk = this.scksaWholecakeOrderMgr.insertScksaWholecakeOrder(scksaSqlMap, dto);		
					if(isOk) {
						logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Insert ICS_XO_ORDER_WCAKE : ").append (isOk);
						wLogger.info (logSb.toString ());
					}else {
						// 등록 및 업데이트 실패 롤백 처리
						throw new Exception();
					}
				}
				
				// 2. 발주정보 상세정보 등록(사이렌오더 서버 -> 영업정보)
				dbTable = "Insert ICS_XO_ORDER_DETAIL_WCAKE ";
				errorMessage = "발주정보 상세정보 등록(사이렌오더 서버 -> 영업정보) 실패";
				isOk = this.scksaWholecakeOrderMgr.insertScksaWholecakeOrderDetail(scksaSqlMap, dto);
				
				if(!isOk){
					// 등록 및 업데이트 실패 롤백 처리
					throw new Exception();
				}
				
				//발주정보 상세정보 등록 모두 끝난뒤 로그를 남김
				//영업정보 처리 후 사이렌오더 상태값 변경 및 히스토리 등록
				if(dtoSize == detailCount && isOk) {
					logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Insert ICS_XO_ORDER_DETAIL_WCAKE : ").append (isOk);				
					wLogger.info (logSb.toString ());
					
					
					// 3. 사이렌오더 XO_ORDER_WHOLECAKE의 STATUS값 O20으로 업데이트
					dbTable = "update XO_WHOLECAKE_ORDER# ";
					errorMessage = "사이렌오더 XO_ORDER_WHOLECAKE의 STATUS값 O20으로 업데이트 실패";
					isOk = this.xoWholecakeOrderMgr.updateWholecakeOrder(sqlMap, dto);
					if(isOk) {
						logSb.delete(0, logSb.length()).append(this.loggerTitle).append("update XO_WHOLECAKE_ORDER# : ").append (isOk);
						wLogger.info (logSb.toString ());
					} else {
						// 등록 및 업데이트 실패 롤백 처리
						throw new Exception();
					}
					
					// 4. 사이렌오더 XO_ORDER_HISTORY_WHOLECAKE의 STATUS값 O20으로 히스토리 등록 
					dbTable = "Insert XO_WHOLECAKE_ORDER_HISTORY# ";
					errorMessage = "사이렌오더 XO_ORDER_HISTORY_WHOLECAKE의 STATUS값 O20으로 히스토리 등록 실패";
					isOk = this.xoWholecakeOrderMgr.insertWholecakeOrderHistory(sqlMap, dto);
					if(isOk) {
						logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Insert XO_WHOLECAKE_ORDER_HISTORY# : ").append (isOk).append("\n");
						wLogger.info (logSb.toString ());
					} else {
						// 등록 및 업데이트 실패 롤백 처리
						throw new Exception();
					}
				}
				
				isFirst = false;
				detailCount++;
			}
			
			isWholecakeOrder = true;
			scksaSqlMap.commitTransaction();
			sqlMap.commitTransaction();
			
		} catch (Exception e) {

			logSb.delete(0, logSb.length()).append(this.loggerTitle).append(dbTable).append("Error : ").append (userId).append ("|").append (orderNo).append ("|").append ("false");
			wLogger.info (logSb.toString ());

			logSb.delete(0, logSb.length()).append(this.loggerTitle).append(dbTable).append("Error : ").append (errorMessage).append("\n");
			wLogger.info (logSb.toString ());
			
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append(dbTable).append("Error : ").append (userId).append ("|").append (orderNo).append ("|").append("\n").append("Exception : ").append (e.getMessage()).append("\n");
			wErrorLogger.error (logSb.toString());
			
			// 발주등록 상세 건수 감소
			totalQtyCount = totalQtyCount - failQtyCount;
			// 발주 등록 실패 상세 건수 증가
			failTotalQtyCount += failQtyCount;
			
			// 초기화
			failQtyCount = 0;
			
			// 영업정보(SCKSA) 등록이 정상적으로 되지 않을 경우 ROLLBACK 처리 
			if(scksaSqlMap != null) {
				scksaSqlMap.endTransaction();
			}
			
			// 사이렌오더에 업데이트 및 히스토리 등록이 정상적으로 되지 않을 경우 ROLLBACK 처리
			if(sqlMap != null) {
				sqlMap.endTransaction();
			}

			wErrorLogger.error(e.getMessage(), e);
		} finally {
			
			scksaSqlMap.endTransaction();
			sqlMap.endTransaction();
		}
		
		return isWholecakeOrder;
	}

	/**
	 * 발주 확정일자가 오늘인 홀케익 예약정보 조회
	 */
	private void getTodayWholecakeOrderList(Map<String, Object> dbMap) {
        
		try {
			// 발주 확정일자가 오늘일 홀케익 예약정보 조회
			wholecakeOrderDtoList = this.xoWholecakeOrderMgr.getWholecakeOrderList(dbMap);
		} catch ( Exception e ) {
        	logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Select TodayWholecakeOrderList Error").append("\n").append("Exception : ").append (e.getMessage()).append("\n");
        	wLogger.info (logSb.toString ());
        	wErrorLogger.error (logSb.toString());
		}
	}
	
	// 홀케익 발주 확정 종료 결과 SMS 발송
	private void smsSend(int successCnt, int failCnt, String salesOrderDate) {
		
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();        // 사이렌오더 트렌젝션
		
		try {
			
			sqlMap.startTransaction();
			
			Map<String, Object> dbMap = new HashMap<String, Object> ();
			dbMap.put("salesOrderDate", salesOrderDate);
			
			// 발주 확정일자의 전일 발주 확정 카운터 조회
			Map<String, Object> returnMap = this.xoWholecakeOrderMgr.getYesterdayWholecakeOrderConfirm(dbMap);
			
			conf = CommPropertiesConfiguration.getConfiguration("sms.properties");
			int totalCnt = successCnt + failCnt; // 총 예약건수(발주 성공 건수 + 발주 실패 건수)
			int totalQtyCnt = totalQtyCount + failTotalQtyCount; // 총 예약 케익수(발주등록 상세 건수 + 발주 등록 실패 상세 건수)

			String content = "[WholecakeOrderConfirm]\n"
							 // 금일 발주:성공건수(successCnt)/발주 성공 케익수(totalQtyCount) (예약:총 예약건수(totalCnt)/총 예약 케익수(totalQtyCnt))
			                 + "금일 발주:" + successCnt + "/" + totalQtyCount + " (예약:" + totalCnt + "/" + totalQtyCnt + ")\n"
					         //+ "전일 발주:성공건수/발주 성공 케익수
			                 + "전일 발주:" + returnMap.get("count") + "/" + returnMap.get("totalQty");
			String callback = conf.getString("wholecake.sms.callback");
			String recipientNum = conf.getString("wholecake.sms.receive.info");
			String recipientArr[] = recipientNum.split("\\|");

			logSb.delete(0, logSb.length ()).append (this.loggerTitle).append ("금일 발주 성공 : ").append (successCnt).append (" / 발주 성공 케익수 : ").append (totalQtyCount).append (", 금일 예약건수 : ").append (totalCnt).append (" / 예약 케익수 : ").append (totalQtyCnt);
			wLogger.info (logSb.toString ());
			
			logSb.delete(0, logSb.length ()).append (this.loggerTitle).append ("전일 발주 성공 : ").append (returnMap.get("count")).append (" / 발주 성공 케익수 : ").append (returnMap.get("totalQty"));
			wLogger.info (logSb.toString ());
			
	        // SMS 발송 요청 
			for(int i = 0; i < recipientArr.length; i++) {
		        SmtTranDto smtTranDto = new SmtTranDto ();
		        smtTranDto.setPriority ("S"); //전송 우선 순위{VF-Very Fast, F-Fast, S-Slow}
		        smtTranDto.setContent (content);
		        smtTranDto.setCallback (callback);
		        smtTranDto.setRecipient_num (recipientArr[i]);

		        this.mmsMgr.insertSmtTran (sqlMap, smtTranDto);
		        sqlMap.commitTransaction (); // commit
			}
	        
		} catch (Exception e) {
			logSb.delete(0, logSb.length ()).append (this.loggerTitle).append ("SMS 발송 실패 : ").append (e.getMessage());
			wLogger.info (logSb.toString ());
			
			if(sqlMap != null) {
				try {
					sqlMap.endTransaction();
				} catch (SQLException e1) {
					wErrorLogger.error(e.getMessage(), e);
				}
			}
		} finally {
            try {
            	sqlMap.endTransaction ();
            } catch ( Exception ee ) {
            	wErrorLogger.error(ee.getMessage(), ee);
            }
		}
	}
	
	public static void main ( String[] args ) {
		WholecakeOrderConfirm wholecakeOrderConfirm = new WholecakeOrderConfirm ();
		
		if(args.length > 1) {
			wholecakeOrderConfirm.start (args[1]);
		} else {
			wholecakeOrderConfirm.start ("");
		}
		
	}
    
}
