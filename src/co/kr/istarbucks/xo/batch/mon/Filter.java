package co.kr.istarbucks.xo.batch.mon;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Filter {
    
    private static volatile Filter g_instance = null;
    public Hashtable localBufferFilter = new Hashtable();
    
    //public long stableTimeInterval = 0;
    //stableTimeInterval = Integer.parseInt(this.getProperty("TimeInterval"));
    public long stableTimeInterval = Integer.parseInt(this.getProperty("monitor.sms.interval"));
    private static Log logger = LogFactory.getLog(Filter.class);
    
    public Filter () {
    }

    public Hashtable filtering (String strValue) {
        
        Hashtable resultFilter = new Hashtable();
        String strRet = "";
        long currentTime = System.currentTimeMillis();
        Enumeration tKey = localBufferFilter.keys();
        while(tKey.hasMoreElements()) {
          String tID = (String)tKey.nextElement();
          FilterObject fo = (FilterObject) localBufferFilter.get(tID);
          if(fo.rTime <= currentTime - stableTimeInterval) {
              localBufferFilter.remove(tID);
//              System.out.println("Remove:"+tID);
          }
        }

      if (!localBufferFilter.containsKey(strValue)){
//          System.out.println("no");
        if( stableTimeInterval > 0 ) {
        	FilterObject fo = new FilterObject();
        	fo.rCount = 1;
        	fo.rTime = currentTime;
        	localBufferFilter.put(strValue,fo);
        	resultFilter.put(strValue,fo);
        }/* else {
          
        }*/
      }
      
      for(int i = 0;i < resultFilter.size();i++) {
          tKey = resultFilter.keys();
          while(tKey.hasMoreElements()) {
              strRet = (String)tKey.nextElement();
//              System.out.println("strRet:" + strRet);
          }
      }

      return resultFilter;
    }
    
    
    /**
     * getInstance
     * singletone°´Ã¼¸¦ ÃëµæÇÑ´Ù.
     * @return Filter
     */
    public static Filter getInstance() {
     
      if(g_instance == null){
          synchronized(Filter.class){
           if(g_instance == null){
               g_instance = new Filter();
           }
          }
      }
       return g_instance;
    }      
    
    
    public String getProperty(String sKey)
    {
        String sRet = "";
        try {
            String s = "/xop_app/xo/batch/conf/monitor.properties";
            Properties prop = new Properties();
            prop.load(new BufferedInputStream(new FileInputStream(s)));
            sRet = prop.getProperty(sKey);
//            System.out.println("KeyValue:"+sRet);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return sRet;
    }
    
    public static void main(String[] args) {
    	
//    	Filter filter = new Filter();
//    	filter.getProperty("monitor.sms.interval");
//    	filter.filtering(strValue)
    }
}
