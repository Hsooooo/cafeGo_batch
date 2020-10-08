package co.kr.istarbucks.xo.batch.mgr;

import java.io.File;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;
import co.kr.istarbucks.xo.batch.common.util.DateUtil;

public class LogFileRemoverMgr {
	private static Log logger = LogFactory.getLog("logFileRemover");
	private static Configuration conf = CommPropertiesConfiguration.getConfiguration("logRemover.properties");
//	private final String BASE_DIR_PATH_RESIN    = File.separator + "xop_app" + File.separator + "resin" + File.separator +"log" + File.separator;
//	private final String BASE_DIR_PATH_XO_BATCH = File.separator + "xop_app" + File.separator + "xo" + File.separator + "batch" + File.separator +"logs" + File.separator;
//	private final String BASE_DIR_PATH_XO_POS   = File.separator + "xop_app" + File.separator + "xo" + File.separator + "pos" + File.separator +"logs" + File.separator;
	
	int totaldFileCount;
	int deletedFileCount;
	
	int totalFileSize;
	int deletedFileSize;
	
	String batchMode;
	boolean isRealMode;
	
	public void deleteLogFiles(String parentDirPath) {
		
	    logger.info("----------------------------------------------------------");
	    logger.info(" target directory=" + parentDirPath);
	    logger.info("----------------------------------------------------------");		
		
	    this.totaldFileCount  = 0;
		this.deletedFileCount = 0;
	    this.totalFileSize  = 0;
		this.deletedFileSize = 0;
		
		this.batchMode = StringUtils.defaultString(conf.getString("batch_mode"));
		isRealMode = false;
		if ("real".equals(this.batchMode)) {
			this.isRealMode = true;
		}
		
		File toDelete = FileUtils.getFile(FilenameUtils.getPath(parentDirPath));
	    String[] list = toDelete.list (); 
	    if(list.length != 0){            										 							// ���� ������ ������ �������
	    	for(int i=0;i<list.length;i++){     								 							// ������� ������ üũ
	    		//File delFile = new File(toDelete+File.separator+list[i]);		 						// ���丮
	    		File delFile =  FileUtils.getFile(FilenameUtils.getPath(toDelete+File.separator+list[i]));		 						// ���丮
	    		if(delFile.isDirectory()){     										 						// ���丮�̸� ������ �ѹ������������� �żҵ� ȣ��
	    			checkFile(toDelete+File.separator+list[i]);
	    		}else{              																		// ȭ���̸� ����
	    			deleteFile(delFile);
	    		}
	    	}
	    }
	    
	    logger.info("");
	    logger.info("----------------------------------------------------------");
	    logger.info(" summary");
	    if("BASE_DIR_PATH_RESIN".equals(parentDirPath)){
	    	logger.info(" 1/2 wasLog");
	    }
	    else if ("BASE_DIR_PATH_XO_BATCH".equals(parentDirPath)){
	    	logger.info(" 2/2 batchLog");
	    }
	    else if ("BASE_DIR_PATH_XO_POS".equals(parentDirPath)) {
	    	logger.info(" 3/2 posLog");
	    }
	    logger.info("----------------------------------------------------------");
	    logger.info(" " + "server mode=" + batchMode);
	    logger.info(" " + totaldFileCount  + " file(s) exists. [size="+totalFileSize+" byte(s)]");
	    logger.info(" " + deletedFileCount + " file(s) deleted. [size="+deletedFileSize+" byte(s)]");
	    logger.info("----------------------------------------------------------");
	    
//	    System.out.println("���� ����:" + toDelete);       											// �������� �ڱ��ڽ� ���� ����
	    //toDelete.delete();
	  }
	
	public void checkFile(String path){
        String[] list = FileUtils.getFile(new String[]{path}).list();
        if (list.length != 0) {
            for (String append : list) {
                File delFile = FileUtils.getFile(new String[]{new StringBuilder(String.valueOf(path)).append(File.separator).append(list).toString()});
                if (delFile.isDirectory()) {
                    checkFile(new StringBuilder(String.valueOf(path)).append(File.separator).append(append).toString());
                } else {
                    deleteFile(delFile);
                }
            }
        }
    }
	
	public void deleteFile(File file) {
		String fileName  = "";				
		String logMsg    = "";
		String logDescMsg = "";
		String resultMsg = "";
		
		try {
			fileName = replacePattern(file.getAbsolutePath());
			
			totaldFileCount++;			
			totalFileSize += file.length();

			// ���ϸ��� ������ ã�� ���Ϻ����� ����
			String term = conf.getString(fileName);	 // logRemover.properties���Ͽ��� �����ֱ⸦ ������
			int termDays = 0;
			if(term != null && term.contains("Y")){
				termDays = Integer.parseInt(term.replace("Y", "")) * 365;
			} else if(term != null && term.contains("W")){
				termDays = Integer.parseInt(term.replace("W", "")) * 7;
			} else if(term != null && term.contains("M")){
				termDays = Integer.parseInt(term.replace("M", "")) * 30;
			}
			String expiredDate  = DateUtil.getDifferDays("yyyyMMdd", termDays);	//�����ֱ⸦ ���ó�¥�� ����Ͽ� �����ֱ⸦ ����

			int fromDate = Integer.parseInt(expiredDate);
			int getDate = getDate(file.getAbsolutePath());
			
			logMsg     = file.getAbsolutePath();
			logDescMsg = "[policy=" + StringUtils.defaultString(term) + ", expired_date=" + fromDate +"]";
			
			//2014.01.07 CSH ���� - expiredDate���� �����ǵ��� ����
			//if((getDate < fromDate) && (termDays != 0) && (getDate != 0)){	//���ó�¥���� �����ֱ⸦ ���� �Ⱓ���� ������ ����
			if((getDate <= fromDate) && (termDays != 0) && (getDate != 0)){	//���ó�¥���� �����ֱ⸦ ���� �Ⱓ���� ������ ����
//				logger.info("[�������ϸ�] = "+fileName);
//				logger.info("[�����Ⱓ] = "+termDays);
//				logger.info("[����������] = "+fromDate);
//				logger.info("[���ϻ�����] = "+getDate);
//				logger.info("[�������ϸ�] = "+file.getAbsolutePath());

				resultMsg = "      ... deleted.";
				this.deletedFileCount++;
				deletedFileSize += file.length();
				
				if (isRealMode) {
					file.delete();
				}
				
			} else {
				resultMsg = "      ... not expired.";
			}
			
			if (StringUtils.isBlank(term)) {
				resultMsg = "      ... not determine policy.";
			}
		} catch (Exception e) {
			resultMsg = "      ... cannot deleted.";
			logDescMsg = "[file=" + file.getAbsolutePath() + "]";
			logger.info(e.getMessage(), e);
		} finally {
			logger.info(logMsg);
			logger.info(resultMsg + " " + logDescMsg);
		}
		

	}
	
	public static int getDate(String filePath){														//���ϸ��� ��¥���� ����
		int date = 0;
		int i=0;
		int j=0;
		//int k=0;
		String inFilePath = filePath;
		if(!"".equals(inFilePath)){
			inFilePath = inFilePath.replace("-", "");
			//** 2013.12.09 CSH ���� **//
			/*
			while(i<filePath.length()) {
			   if(filePath.charAt(i)>=48 &&  filePath.charAt(i)<=57) {
				   j++;
				   if(j == 7){					   					   
					   date = Integer.parseInt(filePath.substring(i-6, i+2));
					   break;
				   }
			   }
			   i++;
			}
			*/
			while(i<inFilePath.length()) {
			   if(inFilePath.charAt(i)>=48 && inFilePath.charAt(i)<=57) {
				   j++;				   
				   if(j == 7){		
					   date = Integer.parseInt(inFilePath.substring(i-6, i+2));
					   break;
				   } else if (j < 7){
					   if(!(inFilePath.charAt(i+1)>=48 && inFilePath.charAt(i+1)<=57)) {
						   j = 0;
					   }
				   }
			   }
			   i++;
			}
		}
		return date;
	}
	
	public static String replacePattern(String filePath){		
		String inFilePath = filePath;
		if(inFilePath.contains("log."+getDate(inFilePath))){												// xxxx.log.date
			inFilePath = inFilePath.replace("."+getDate(inFilePath), "");
			int nameEnd= inFilePath.indexOf(".log");
			StringBuffer sb= new StringBuffer();
			sb.append(inFilePath.substring(0, nameEnd));
			sb.append(".log");
			//** 2013.11.27 CSH ���� **//
			//String fileName = sb.toString();
			//String path      = fileName.substring(0, fileName.lastIndexOf("_"));
			//String extension = fileName.substring(fileName.lastIndexOf("."));
			//filePath = path + extension;
			inFilePath = sb.toString();
		}else if(inFilePath.contains(getDate(inFilePath)+".log")){										// xxxx_date.log	
			inFilePath = inFilePath.replace(String.valueOf(getDate(inFilePath)), "");
			String fileName = inFilePath;
			String path      = fileName.substring(0, fileName.lastIndexOf("_"));
			String extension = fileName.substring(fileName.lastIndexOf("."));
			inFilePath = path + extension;
		}
		return inFilePath;
	}
}
