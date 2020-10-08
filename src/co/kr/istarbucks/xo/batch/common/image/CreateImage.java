/*
 * @(#) $Id: CreateImage.java,v 1.3 2016/11/23 07:25:36 dev99 Exp $
 * 
 * Starbucks Service
 * 
 * Copyright 2016 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.common.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.krysalis.barcode4j.impl.pdf417.PDF417Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.MimeTypes;
import org.krysalis.barcode4j.tools.UnitConv;

import co.kr.istarbucks.xo.batch.common.util.OnmUtil;
import co.kr.istarbucks.xo.batch.common.util.XOUtil;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 이미지 생성 - CreateImage.
 *
 * @author ksy
 * @since 2016. 11. 07
 * @version $Revision: 1.3 $
 */
public class CreateImage {
	private final Log          logger;
	OnmUtil              ou     = new OnmUtil();
	private final StringBuffer logSb  = new StringBuffer();	
	
	// 이미지 출력 형태
	public static final int OUTPUT_IMAGE        = 0;
	public static final int OUTPUT_BASE64       = 1;
	public static final int OUTPUT_IMAGE_BASE64 = 2;
	
	// 이미지 크기 
	private static int imgW  = 640;
	private static int imgH  = 1136;

	// 컨텐츠 이미지 크기
	private static int cImgW = 642;  // 480
	private static int cImgH = 642;  // 390
	private static int cImgX = 0;    // 20
	private static int cImgY = 79; 	 // 120
	
	// 텍스트 타이틀 위치
	private static int TextX = 36;   // 20
	private static int TextY = 720;  // 20
	
	// 색 설정
	private static final Color subTitleColor  = new Color(68,68,68);
	private static final Color contentColor   = new Color(102,102,102);
	
	// 글씨 크키
	private static final int   titleFontSize    = 34;
	private static final int   subTitleFontSize = 36;
	private static final int   contentsFontSize = 24;
	private static final int   barcodeFontSize  = 26;
	
	
	// 글씨 설정
	private static       String FONT_NAME     = "Malgun Gothic";
//	private static final Font   titleFont     = new Font(FONT_NAME,     Font.TRUETYPE_FONT,             34);
//	private static final Font   barcodeFont   = new Font(FONT_NAME,     Font.TRUETYPE_FONT,             26);
//	private static final Font   subTitleFont  = new Font(FONT_NAME,     Font.TRUETYPE_FONT,             36);
//	private static final Font   contentFont   = new Font(FONT_NAME,     Font.TRUETYPE_FONT,             24);
	
	// 바코드 설정
	private static int barcodeW = 342;		// Barcode 너비 
	private static int barcodeH = 130;		// Barcode 높이
	
	/**
	 * constructor
	 */
	public CreateImage(Log logger) {
		this.logger = logger;
	}
	
	/**
	 * 신규 이미지 경로
	 */
	private String getNewImgPath(String basePath) {
		String           path       = ou.concatString("yyyyMMdd");
		SimpleDateFormat dateFormat = new SimpleDateFormat(path, Locale.KOREAN);
		String           newImgPath = basePath + File.separator + dateFormat.format(Calendar.getInstance().getTime()) + File.separator;
		
		return newImgPath;
	}
	
	/**
	 * 기존 이미지 경로
	 * @param sentMMSImgpath
	 */
	private String getOldImgPath(String oldMMSImgPath) {
		String oldImgPath = oldMMSImgPath.substring(0, oldMMSImgPath.lastIndexOf(File.separator)) + File.separator;
		return oldImgPath;
	}	
	
	/**
	 * MMS 이미지 생성
	 * @param vo
	 * @param logTitle
	 * @return
	 * @throws Exception
	 */
	public MMSImageVO getImageForMMS(MMSImageVO vo, String logStr) throws Exception{
		ImageFileOutputStream out          = null;
		MMSImageVO            mmsImageVO   = vo;
		String                logTitle     = ou.concatString(logStr, "[mmsImage][", vo.getBarcode(), "]");
		String                processStep  = "";
		int                   processOrder = 0;

		if(logger.isDebugEnabled()){
			logger.debug(ou.concatString(logSb, logTitle, "getImageForMMS START =============================="));
		}	
		try	{
			processStep = " 배경이미지/내용이미지/생성될 이미지 경로 확인";	//===========================
			// 생성될 이미지 경로 생성
			String mmsSavePath = "";
			if(vo.getIsRetry() && StringUtils.isNotEmpty(vo.getOrgImgPath())){
				File orgMmsImg  = FileUtils.getFile(ou.concatString(vo.getOrgImgPath(), vo.getImgName()));
				if(orgMmsImg.exists()){
					mmsSavePath = getOldImgPath(vo.getOrgImgPath());	// 재발송 & 기존이미지 있는 경우 기존 이미지 경로로
				} else {
					mmsSavePath = getNewImgPath(vo.getImgBasePath());	// 신규 이미지 경로(년/월/일)
				}
			} else {
				mmsSavePath = getNewImgPath(vo.getImgBasePath());		// 신규 이미지 경로(년/월/일)
			}
			mmsImageVO.setImgPath(mmsSavePath);
			File   mmsSaveDir  = FileUtils.getFile(mmsSavePath);				// MMS 저장 경로
			if(!mmsSaveDir.exists()) {
				mmsSaveDir.mkdirs();
			}
			String mmsImgPath  = ou.concatString(mmsSavePath, vo.getImgName());	// 생성될 이미지 경로
			File   mmsImg      = FileUtils.getFile(mmsImgPath);				// 생성될 이미지 경로								// 생성될 이미지
			File   backImg     = FileUtils.getFile(vo.getBackImgPath());		// 배경이미지
			File   contentsImg = FileUtils.getFile(vo.getContentsImgPath());	// 내용이미지
			if(logger.isDebugEnabled()){
				logger.debug(ou.concatString(logSb, logTitle, "backImg     : ", backImg    , ", ", backImg.exists()));
				logger.debug(ou.concatString(logSb, logTitle, "contentsImg : ", contentsImg, ", ", contentsImg.exists()));
				logger.debug(ou.concatString(logSb, logTitle, "mmsImgPath  : ", mmsImgPath));
				logger.debug(ou.concatString(logSb, logTitle, processOrder++,  processStep));
			}
			
			processStep = " 이미지 버퍼 생성";	//====================================================
			BufferedImage bufferedImage = new BufferedImage(imgW, imgH, BufferedImage.TYPE_INT_RGB);
			if(logger.isDebugEnabled()){logger.debug(ou.concatString(logSb, logTitle, processOrder++,  processStep));}
			
			processStep = " 그래픽 셍성 및 초기화";	//====================================================
			Graphics2D g = bufferedImage.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,   RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_RENDERING,         RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);		// 글씨 외곽 부드럽게
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_DEFAULT);		// 그림 외과 부드럽게
			g.setRenderingHint(RenderingHints.KEY_DITHERING,         RenderingHints.VALUE_DITHER_DISABLE);
			g.setColor(new Color(209,210,212));	
			g.fillRect(0, 0, imgW, imgH);
			if(logger.isDebugEnabled()){logger.debug(ou.concatString(logSb, logTitle, processOrder++,  processStep));}
			
			processStep = ou.concatString(" 배경 이미지 생성 (", backImg,") "); //====================================================
			BufferedImage bufImg = ImageIO.read(backImg);
			if(bufImg.getWidth() != imgW) {
				Image nimg = bufImg.getScaledInstance(imgW, imgH, Image.SCALE_SMOOTH);
				g.drawImage(nimg, 0, 0, null);
			} else {
				g.drawImage(bufImg, null, 0, 0);
			}
			if(logger.isDebugEnabled()){logger.debug(ou.concatString(logSb, logTitle, processOrder++,  processStep));}
			
			processStep = " 배경 이미지에 내용 이미지 출력";
			try {
				int           contentsX      = cImgX;							// 컨텐츠 이미지 위치 - 왼쪽부터
				int           contentsY      = cImgY;							// 컨텐츠 미미지 위치 - 위부터
				int           contentsW      = cImgW;							// 컨텐츠 이미지 너비
				int           contentsH      = cImgH;							// 컨텐츠 이미지 높이
				float         cRate          = (cImgW * 1000) / cImgH;			// 컨텐츠 이미지 영역 가로*세로 비율
				BufferedImage contentsBufImg = ImageIO.read(contentsImg);
				int           w              = contentsBufImg.getWidth();		// 실제 컨텐츠 이미지 너비
				int           h              = contentsBufImg.getHeight();		// 실제 컨텐츠 이미지 높이
				float         contentsRate   = (w * 1000) / h;					// 실제 컨텐츠이미지 가로*세로 비율
				if(contentsRate > cRate ) {
					// 가로 비율 기준으로 
					contentsH = ((cImgW*1000/w)*h)/1000;
					contentsY = cImgY +  (cImgY - contentsH) / 2;
				} else {
					// 세로 비율 기준으로
					contentsW = ((cImgH*1000/h)*w)/1000;
					contentsX = (imgW - contentsW) / 2;
				}
				// MMS 이미지 크기에 맞게 조절된 
				Image img = contentsBufImg.getScaledInstance(contentsW, contentsH, Image.SCALE_AREA_AVERAGING);
				g.drawImage(img, contentsX, contentsY, null);
				g.setColor (null);
				g.drawRect (contentsX-1, contentsY, contentsW, contentsH);
			} catch (Exception e) {
				if(logger.isInfoEnabled()){
					logger.info(ou.concatString(logSb, logTitle, processOrder, " 내용이미지 없음", e));
				}
			}
			if(logger.isDebugEnabled()){logger.debug(ou.concatString(logSb, logTitle, processOrder++,  processStep));}
			
			processStep = " MMS 제목 쓰기";	//====================================================
			Font titleFont = new Font(FONT_NAME, Font.TRUETYPE_FONT+Font.BOLD, titleFontSize); 
			g.setColor  (Color.WHITE);
			g.setFont   (titleFont);
			g.drawString(vo.getImgTitle(), TextX, 19 + titleFontSize );
			if(logger.isDebugEnabled()){logger.debug(ou.concatString(logSb, logTitle, processOrder++,  processStep));}
			
			processStep = " MMS 내용 쓰기";	//====================================================
			// MMS 내용 상단.
			int tY = TextY; 
			Font subTitleFont = new Font(FONT_NAME, Font.TRUETYPE_FONT+Font.BOLD, subTitleFontSize);
			g.setColor  (subTitleColor);
			g.setFont   (subTitleFont);
			g.drawString(vo.getImgSubTitle(), TextX, (tY += 32 + subTitleFontSize - 6));
			// 본문 타이틀 문구 텍스트 쓰기
			Font contentsFont = new Font(FONT_NAME, Font.TRUETYPE_FONT, contentsFontSize);
			g.setColor  (contentColor);
			g.setFont   (contentsFont);
			tY += 56;
			for(String str : vo.getImgContents()){
				String arr[] = StringUtils.split(str, "//|");
				if(arr.length != 2) continue;
				g.drawString(arr[0]+" : "+arr[1] , TextX, tY += contentsFontSize + 10);
			}
			if(logger.isDebugEnabled()){logger.debug(ou.concatString(logSb, logTitle, processOrder++,  processStep));}
			
			processStep = " Barcode 이미지 생성";//==================================================== 
			File   tmpBarcode = FileUtils.getFile(ou.concatString(mmsSavePath, File.separator, "barcode_", vo.getBarcode(), ".jpg"));
			double mod_width  = 0.28D;
			double height 	  = 2.60D;
			int    dpi 		  = 300;
			
			PDF417Bean bean  = new PDF417Bean();
			bean.setFontSize(UnitConv.mm2in(1)*10);
			bean.setFontName(FONT_NAME);
			bean.setModuleWidth(mod_width);
			bean.setHeight(height);
			bean.doQuietZone(false);
			OutputStream os = new FileOutputStream(tmpBarcode);
			int imageType   = BufferedImage.TYPE_BYTE_BINARY;
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(os, MimeTypes.MIME_JPEG , dpi, imageType, true, 0);
			bean.generateBarcode(canvas, vo.getBarcode());
			canvas.finish();
			os.close();
			
			BufferedImage barcodeBufImg = ImageIO.read(tmpBarcode);
			int barcodeX = (imgW - barcodeW) / 2;
			int barcodeY = (tY += 66);
			Image barcodeImg = barcodeBufImg.getScaledInstance(barcodeW, barcodeH, Image.SCALE_FAST);
			g.drawImage(barcodeImg, barcodeX, barcodeY, null);
			g.setColor(Color.WHITE);
			g.drawRect(barcodeX, barcodeY, barcodeW, barcodeH);
			// 바코드 번호 쓰기
			Font barcodeFont = new Font(FONT_NAME, Font.TRUETYPE_FONT, barcodeFontSize);
			int barcodeTextX = (imgW-vo.getBarcode().length()*13)/2;
			int barcodeTextY = tY + barcodeH + (20-6) + barcodeFontSize;
			g.setColor  (subTitleColor);
			g.setFont   (barcodeFont);
			g.drawString(vo.getBarcode(), barcodeTextX, barcodeTextY);
			
			// 바코드 이미지 삭제
			try{
				if(tmpBarcode!=null){
					tmpBarcode.delete();
				}
			}catch(Exception ex){
				logger.error(ex.getMessage(), ex);
			}
			if(logger.isDebugEnabled()){logger.debug(ou.concatString(logSb, logTitle, processOrder++,  processStep));}			
			
			processStep = " 생성한 이미지를 파일로 저장";	//====================================================
			//    - .jpg 파일로 저장
			//    - .jpg.base64 파일로 base64 변환 되어 저장				
			out = new ImageFileOutputStream(mmsImg, OUTPUT_IMAGE);
			JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam parm = enc.getDefaultJPEGEncodeParam(bufferedImage);
			parm.setQuality(1.0f, true);
			enc.encode(bufferedImage, parm);
			out.flush();
			out.close();
			out = null;
			if(logger.isDebugEnabled()){logger.debug(ou.concatString(logSb, logTitle, processOrder++,  processStep));}
			
			mmsImageVO.setIsSuccess(true);
		} catch(Exception ex){
			mmsImageVO.setIsSuccess(false);
			if(logger.isErrorEnabled()){
				logger.error(ou.concatString(logSb, logTitle, processOrder++, processStep, ex.getMessage()), ex);
			}
		} finally {
			if (out != null) {
				out.close();
			}
			
		}
		if(logger.isDebugEnabled()){
			logger.debug(ou.concatString(logSb, logTitle, "getImageForMMS END =============================="));
		}	
		
		return mmsImageVO;
	}
	
	public static void main(String[] args) throws Exception {
		
		String   couponStartDate = "2016-12-24";
		String   couponEndDate   = "2016-12-30";
		String   propContents    = XOUtil.getPropertiesString("coupon.wholecake.img.contents", new String[]{couponStartDate, couponEndDate});
		String[] contents        = StringUtils.split(propContents, ";");
		String   imgName         = XOUtil.getPropertiesString("coupon.wholecake.img.name",     new String[]{"order_no", "couponNumber"});
		String   imgTitle        = XOUtil.getPropertiesString("coupon.wholecake.img.title");
		String   rootPath        = XOUtil.getPropertiesString("coupon.root.path");
		String   backImgPath     = XOUtil.getPropertiesString("coupon.wholecake.back.img.path",    new String[]{rootPath});
		String   contentsImgPath = XOUtil.getPropertiesString("coupon.wholecake.default.img.path", new String[]{rootPath});
		String   imgSavePath     = XOUtil.getPropertiesString("coupon.wholecake.img.save.path",    new String[]{rootPath});
		
		// VO 담기
		MMSImageVO imgVO = new MMSImageVO();
		imgVO.setImgName         (imgName);									// 이미지 파일 명 (예약번호_쿠폰번호.jpg)
		imgVO.setImgTitle        (imgTitle);								// 이미지 제목
		imgVO.setImgSubTitle     ("홀케익 무료 음료 쿠폰");							// 쿠폰명
		imgVO.setImgContents     (contents);								// 이미지 항목 내용
		imgVO.setBackImgPath     (backImgPath); 							// 배경 이미지 경로
		imgVO.setImgBasePath     (imgSavePath);  							// MMS 이미지 저장 경로
		imgVO.setContentsImgPath (contentsImgPath);           				// 쿠폰 이미지 경로
		imgVO.setBarcode         ("9111610089727");		// 쿠폰선물전송번호
		CreateImage cImage = new CreateImage(LogFactory.getLog("WHOLECAKE_ORDER_COUPON"));
		cImage.getImageForMMS(imgVO, "");
	}
}
