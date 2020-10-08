package co.kr.istarbucks.xo.batch.main;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;
import co.kr.istarbucks.xo.batch.mgr.OrderMsgDeleteMgr;
import co.kr.istarbucks.xo.batch.mgr.PreOrderDeleteMgr;

/**
 * �ֹ� �޽��� ������ ����
 *
 */
public class OrderMsgDelete {

   	private static Log logger = LogFactory.getLog("OrderMsgDelete");

    private Configuration conf = CommPropertiesConfiguration.getConfiguration("xoBatch.properties");

    private String logTitle = "";
    
    private final String loggerTitle;
    private final OrderMsgDeleteMgr orderMsgDeleteMgr;
  
    public OrderMsgDelete () {
        this.orderMsgDeleteMgr = new OrderMsgDeleteMgr ();
        this.loggerTitle = "[PreOrderDelete] ";
    }
    
    /**
     * �ֹ� �޽��� ������ ����
     * @param deleteDay
     */
    public void start (String thatDay) {
    	long startTimeTotal = System.currentTimeMillis ();
    	
    	boolean paramDaysCheck = NumberUtils.isNumber(thatDay) && Integer.parseInt(thatDay) > 0;
    	
    	String deleteDay = paramDaysCheck ? thatDay : "7";
    	
        
    	logger.info(this.loggerTitle.replaceAll("\n|\r", "") + "START");
        
    	logger.info(this.loggerTitle.replaceAll("\n|\r", "") + "���ú��� " + deleteDay + "�� �� �������� ����");

        // 1. XO_ORDER_MSG_M ���̺� ������ ����
        logger.info(this.loggerTitle.replaceAll("\n|\r", "") + "1. XO_ORDER_MSG_M ���̺� ������ ���� START");
        this.deleteOrderMsg(deleteDay);
        logger.info(this.loggerTitle.replaceAll("\n|\r", "") + "1. XO_ORDER_MSG_M ���̺� ������ ���� END");

        logger.info(this.loggerTitle.replaceAll("\n|\r", "") + "END :" + ( System.currentTimeMillis () - startTimeTotal ) + "ms");

    }

	/**
     * XO_ORDER_MSG_M ���̺� ������ ����
     * @param deleteDay
     */
    private void deleteOrderMsg(String deleteDay) {
    	
        try {
            Map<String, Object> dbMap = new HashMap<String, Object>();
            dbMap.put("deleteDay", deleteDay);
            
            // XO_ORDER_MSG_M ���̺� ������ ����
            int deleteDataCnt = this.orderMsgDeleteMgr.deleteOrderMsg(dbMap);
            logger.info(this.loggerTitle.replaceAll("\n|\r", "") + "XO_ORDER_MSG_M ���̺� ������ ���� : " + deleteDataCnt + "��");

        } catch (SQLException e ) {
            logger.error (e.toString().replaceAll("\n|\r", ""), e);
        }

	}


	public static void main( String[] args) {
        OrderMsgDelete preOrderDelete = new OrderMsgDelete();        
        
        if(args.length > 0) {
        	preOrderDelete.start(args[0]);
        } else {
        	preOrderDelete.start("");
        }        
    }

	public Configuration getConf() {
		return conf;
	}

	public void setConf(Configuration conf) {
		this.conf = conf;
	}

	public String getLogTitle() {
		return logTitle;
	}

	public void setLogTitle(String logTitle) {
		this.logTitle = logTitle;
	}

	public String getLoggerTitle() {
		return loggerTitle;
	}

	public OrderMsgDeleteMgr getOrderMsgDeleteMgr() {
		return orderMsgDeleteMgr;
	}
	

}
