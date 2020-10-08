
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
 * Ȧ���� ���� Ȯ�� ���� ��� ��ġ
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
	private int totalQtyCount = 0;     // ���ֵ�� �� �Ǽ�
	private int failTotalQtyCount = 0; // ���� ��� ���� �� �Ǽ�
	private int failQtyCount = 0; 
	
	private Configuration conf = null;
	
	public WholecakeOrderConfirm () {
		this.scksaWholecakeOrderMgr = new ScksaWholecakeOrderMgr();
		this.xoWholecakeOrderMgr = new XoWholecakeOrderMgr();
		this.mmsMgr = new MMSMgr();
		
		this.loggerTitle = "[WholecakeOrderConfirm] ";
		this.logSb = new StringBuffer (); // log�� StringBuffer
	}
	
	/**
	 * 1. ���� Ȯ�����ڰ� ������ Ȧ���� �������� ��ȸ
	 * 2. �������� ���(���̷����� ���� -> ��������)
	 * 3. ������ ���� �������� ó��(�񱳴���� �����Ƿ� ������ ���� ���������� ���� ó��)
	 * 4. ��� �α� ���
	 */
	public void start (String salesOrderDate) {
		long startTime = System.currentTimeMillis ();
		
		StringBuffer failInfo = new StringBuffer ();
		
		int successCnt = 0;  // ����ī����
		int failCnt    = 0;  // ����ī����
		String userId  = "";
		String orderNo = "";
		
		
		logSb.delete(0, logSb.length ()).append (this.loggerTitle).append ("START");
		wLogger.info (logSb.toString ());
		
		// 1. ���� Ȯ�����ڰ� ������ Ȧ���� �������� ��ȸ
		//    ���� Ȯ������ ��¥�� �ִٸ� �ش� ��¥�� �������� ��ȸ
		Map<String, Object> dbMap = new HashMap<String, Object> ();
		dbMap.put("salesOrderDate", salesOrderDate);
		
        this.getTodayWholecakeOrderList(dbMap);

        if(wholecakeOrderDtoList != null && wholecakeOrderDtoList.size() > 0) {
        	
	        for(WholecakeOrderDto dto : wholecakeOrderDtoList){
	            boolean isWholecakeOrder = false;
	
	        	// orderNoTemp�� �ʱⰪ�� dto.getOrder_no ������ �ִ´�.
	            if(StringUtils.isBlank(orderNoTemp)) {
	        		orderNoTemp = dto.getOrder_no();
	        	}
	        	
	        	// ���� order_no�� orderDtoList�� add ó��
	    		if(orderNoTemp.equals(dto.getOrder_no())) {
	    			orderDtoList.add(dto);
	    		} else {
	    			
	    			try {

		    			// 2. �������� ���(���̷����� ���� -> ��������)
		    			// 2. Ȧ���� �������� ������Ʈ(���̷����� ����)	    				
		    			// 2. �ٸ� �ֹ���ȣ�� ��� ������ orderDtoList ���� �������� ���
	    				isWholecakeOrder = setWholecakeOrder(orderDtoList);
		    			userId      = orderDtoList.get(0).getUser_id();
		    			orderNo     = orderDtoList.get(0).getOrder_no();
	                } catch ( Exception e ) {
	                	logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Error : ").append (userId).append ("|").append (orderNo).append ("|").append("\n").append("Exception : ").append (e.getMessage()).append("\n");
	                	wLogger.info (logSb.toString ());
	                	wErrorLogger.error (logSb.toString());
	                }
	
	    			// �������� ���� ���� �� Ȧ���� �������� ������Ʈ ����
	    			if(isWholecakeOrder) {
	    				successCnt++;
	    				
	    			} else {
	    			// �������� ���� ���� �� Ȧ���� �������� ������Ʈ ����
	                    failCnt++;
	                    if ( failInfo.length () > 0 ) {
	                    	failInfo.append ("\n\t\t\t\t\t     ");
	                    }
	                    failInfo.append (userId)
	                            .append ("|").append (orderNo)
	                            .append (";");
	    			}
	    			
	    			// ó�� �� orderDtoList �ʱ�ȭ �� ���� �ֹ���ȣ�� ���������� orderDtoList�� add ó��
	    			// orderNoTemp�� ���� ���� �ֹ���ȣ�� �ʱ�ȭ
	    			orderDtoList.clear();   // �ʱ�ȭ
	    			orderDtoList.add(dto);  // ��Ͽ� �߰�
	    			orderNoTemp = dto.getOrder_no(); // ���� �ֹ����� �ӽð��� ����
	    		}
	        }
	        

	        // 3. ������ ���� �������� ó��(�񱳴���� �����Ƿ� ������ ���� ���������� ���� ó��)
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
	            
				// �������� ���� ���� �� Ȧ���� �������� ������Ʈ ����
				if(isWholecakeOrder) {
					successCnt++;
					
				} else {
				// ����
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
        
        // 4. ��� �α� ���
        logSb.delete(0, logSb.length ()).insert (0, "=================================================")
                                     .append ("\r\n\t\t\t    ").append ("����          : ").append (successCnt).append ("��")
                                     .append ("\r\n\t\t\t    ").append ("����          : ").append (failCnt).append ("��")
                                     .append ("\r\n\t\t\t    ").append ("���� ����   : ").append (new String (failInfo))
                                     .append ("\r\n\t\t\t    =================================================");
		wLogger.info (logSb.toString ());
		
		// 5. ��� SMS �߼�
		this.smsSend(successCnt, failCnt, salesOrderDate);
		
		logSb.delete(0, logSb.length ()).append (this.loggerTitle).append ("END : ").append (System.currentTimeMillis () - startTime).append ("ms");
		wLogger.info (logSb.toString ());
	}


	/**
	 * �������� ���(���̷����� ���� -> ��������)
	 * Ȧ���� �������� ������Ʈ(���̷����� ����)
	 * @param orderDtoList 
	 * @return 
	 * @throws SQLException
	 */
	private boolean setWholecakeOrder(List<WholecakeOrderDto> orderDtoList) throws Exception {
		
		SqlMapClient scksaSqlMap = IBatisSqlConfig.getScksasqlMapInstance(); // ��������(SCKSA) Ʈ������
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();        // ���̷����� Ʈ������
		
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
			// Ʈ������ ���۱���
			sqlMap.startTransaction();
			scksaSqlMap.startTransaction();
			
			for(WholecakeOrderDto dto : orderDtoList){

				// �������� ���(���̷����� ���� -> ��������) 
				// orderDtoList�� ���� �ϳ��� �������� + �������� �󼼸� ��ģ ������ �̹Ƿ� ��Ͻ� �����ؾ��Ѵ�.
				// �������� ��Ͻ� �ߺ��� ���� ���� �������� ù ��ȸ���� ������ ��� ó���Ѵ�.
				// XO_ORDER_WHOLECAKE, XO_ORDER_HISTORY_WHOLECAKE�� �ߺ� ������Ʈ �� �����丮 ����� �������� ù ��ȸ���� ������ ó���Ѵ�.

				if(isFirst) {
					
					userId = dto.getUser_id();
					orderNo = dto.getOrder_no();
					
					// �������� �α�
					logSb.delete(0, logSb.length()).append(this.loggerTitle).append("���� Ȯ������ : ").append(dto.getSales_order_date()).append(", �������� : ").append(dto.getOrder_date());
					wLogger.info (logSb.toString ());
					
					// �������� �α�
					logSb.delete(0, logSb.length()).append(this.loggerTitle).append("���̵� : ").append(userId).append(", �����ȣ : ").append(orderNo).append(", �������� : ").append(dto.getReceive_date()).append(", ���ɸ��� : ").append(dto.getReceive_store_cd()).append(", ����Ǽ� : ").append(dto.getTotal_qty());
					wLogger.info (logSb.toString ());
					
					// ���� �� �Ǽ�
					totalQtyCount += dto.getTotal_qty();
					// ���� ��� ���н� totalQty ī���� ���� �뵵
					failQtyCount = dto.getTotal_qty();
							
					// 1. �������� ���(���̷����� ���� -> ��������)
					dbTable = "Insert ICS_XO_ORDER_WCAKE ";
					errorMessage = "�������� ���(���̷����� ���� -> ��������) ����";
					isOk = this.scksaWholecakeOrderMgr.insertScksaWholecakeOrder(scksaSqlMap, dto);		
					if(isOk) {
						logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Insert ICS_XO_ORDER_WCAKE : ").append (isOk);
						wLogger.info (logSb.toString ());
					}else {
						// ��� �� ������Ʈ ���� �ѹ� ó��
						throw new Exception();
					}
				}
				
				// 2. �������� ������ ���(���̷����� ���� -> ��������)
				dbTable = "Insert ICS_XO_ORDER_DETAIL_WCAKE ";
				errorMessage = "�������� ������ ���(���̷����� ���� -> ��������) ����";
				isOk = this.scksaWholecakeOrderMgr.insertScksaWholecakeOrderDetail(scksaSqlMap, dto);
				
				if(!isOk){
					// ��� �� ������Ʈ ���� �ѹ� ó��
					throw new Exception();
				}
				
				//�������� ������ ��� ��� ������ �α׸� ����
				//�������� ó�� �� ���̷����� ���°� ���� �� �����丮 ���
				if(dtoSize == detailCount && isOk) {
					logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Insert ICS_XO_ORDER_DETAIL_WCAKE : ").append (isOk);				
					wLogger.info (logSb.toString ());
					
					
					// 3. ���̷����� XO_ORDER_WHOLECAKE�� STATUS�� O20���� ������Ʈ
					dbTable = "update XO_WHOLECAKE_ORDER# ";
					errorMessage = "���̷����� XO_ORDER_WHOLECAKE�� STATUS�� O20���� ������Ʈ ����";
					isOk = this.xoWholecakeOrderMgr.updateWholecakeOrder(sqlMap, dto);
					if(isOk) {
						logSb.delete(0, logSb.length()).append(this.loggerTitle).append("update XO_WHOLECAKE_ORDER# : ").append (isOk);
						wLogger.info (logSb.toString ());
					} else {
						// ��� �� ������Ʈ ���� �ѹ� ó��
						throw new Exception();
					}
					
					// 4. ���̷����� XO_ORDER_HISTORY_WHOLECAKE�� STATUS�� O20���� �����丮 ��� 
					dbTable = "Insert XO_WHOLECAKE_ORDER_HISTORY# ";
					errorMessage = "���̷����� XO_ORDER_HISTORY_WHOLECAKE�� STATUS�� O20���� �����丮 ��� ����";
					isOk = this.xoWholecakeOrderMgr.insertWholecakeOrderHistory(sqlMap, dto);
					if(isOk) {
						logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Insert XO_WHOLECAKE_ORDER_HISTORY# : ").append (isOk).append("\n");
						wLogger.info (logSb.toString ());
					} else {
						// ��� �� ������Ʈ ���� �ѹ� ó��
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
			
			// ���ֵ�� �� �Ǽ� ����
			totalQtyCount = totalQtyCount - failQtyCount;
			// ���� ��� ���� �� �Ǽ� ����
			failTotalQtyCount += failQtyCount;
			
			// �ʱ�ȭ
			failQtyCount = 0;
			
			// ��������(SCKSA) ����� ���������� ���� ���� ��� ROLLBACK ó�� 
			if(scksaSqlMap != null) {
				scksaSqlMap.endTransaction();
			}
			
			// ���̷������� ������Ʈ �� �����丮 ����� ���������� ���� ���� ��� ROLLBACK ó��
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
	 * ���� Ȯ�����ڰ� ������ Ȧ���� �������� ��ȸ
	 */
	private void getTodayWholecakeOrderList(Map<String, Object> dbMap) {
        
		try {
			// ���� Ȯ�����ڰ� ������ Ȧ���� �������� ��ȸ
			wholecakeOrderDtoList = this.xoWholecakeOrderMgr.getWholecakeOrderList(dbMap);
		} catch ( Exception e ) {
        	logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Select TodayWholecakeOrderList Error").append("\n").append("Exception : ").append (e.getMessage()).append("\n");
        	wLogger.info (logSb.toString ());
        	wErrorLogger.error (logSb.toString());
		}
	}
	
	// Ȧ���� ���� Ȯ�� ���� ��� SMS �߼�
	private void smsSend(int successCnt, int failCnt, String salesOrderDate) {
		
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();        // ���̷����� Ʈ������
		
		try {
			
			sqlMap.startTransaction();
			
			Map<String, Object> dbMap = new HashMap<String, Object> ();
			dbMap.put("salesOrderDate", salesOrderDate);
			
			// ���� Ȯ�������� ���� ���� Ȯ�� ī���� ��ȸ
			Map<String, Object> returnMap = this.xoWholecakeOrderMgr.getYesterdayWholecakeOrderConfirm(dbMap);
			
			conf = CommPropertiesConfiguration.getConfiguration("sms.properties");
			int totalCnt = successCnt + failCnt; // �� ����Ǽ�(���� ���� �Ǽ� + ���� ���� �Ǽ�)
			int totalQtyCnt = totalQtyCount + failTotalQtyCount; // �� ���� ���ͼ�(���ֵ�� �� �Ǽ� + ���� ��� ���� �� �Ǽ�)

			String content = "[WholecakeOrderConfirm]\n"
							 // ���� ����:�����Ǽ�(successCnt)/���� ���� ���ͼ�(totalQtyCount) (����:�� ����Ǽ�(totalCnt)/�� ���� ���ͼ�(totalQtyCnt))
			                 + "���� ����:" + successCnt + "/" + totalQtyCount + " (����:" + totalCnt + "/" + totalQtyCnt + ")\n"
					         //+ "���� ����:�����Ǽ�/���� ���� ���ͼ�
			                 + "���� ����:" + returnMap.get("count") + "/" + returnMap.get("totalQty");
			String callback = conf.getString("wholecake.sms.callback");
			String recipientNum = conf.getString("wholecake.sms.receive.info");
			String recipientArr[] = recipientNum.split("\\|");

			logSb.delete(0, logSb.length ()).append (this.loggerTitle).append ("���� ���� ���� : ").append (successCnt).append (" / ���� ���� ���ͼ� : ").append (totalQtyCount).append (", ���� ����Ǽ� : ").append (totalCnt).append (" / ���� ���ͼ� : ").append (totalQtyCnt);
			wLogger.info (logSb.toString ());
			
			logSb.delete(0, logSb.length ()).append (this.loggerTitle).append ("���� ���� ���� : ").append (returnMap.get("count")).append (" / ���� ���� ���ͼ� : ").append (returnMap.get("totalQty"));
			wLogger.info (logSb.toString ());
			
	        // SMS �߼� ��û 
			for(int i = 0; i < recipientArr.length; i++) {
		        SmtTranDto smtTranDto = new SmtTranDto ();
		        smtTranDto.setPriority ("S"); //���� �켱 ����{VF-Very Fast, F-Fast, S-Slow}
		        smtTranDto.setContent (content);
		        smtTranDto.setCallback (callback);
		        smtTranDto.setRecipient_num (recipientArr[i]);

		        this.mmsMgr.insertSmtTran (sqlMap, smtTranDto);
		        sqlMap.commitTransaction (); // commit
			}
	        
		} catch (Exception e) {
			logSb.delete(0, logSb.length ()).append (this.loggerTitle).append ("SMS �߼� ���� : ").append (e.getMessage());
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
