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
 * �̹��� ���� - CreateImage.
 *
 * @author ksy
 * @since 2016. 11. 07
 * @version $Revision: 1.3 $
 */
public class CreateImage {
	private final Log          logger;
	OnmUtil              ou     = new OnmUtil();
	private final StringBuffer logSb  = new StringBuffer();	
	
	// �̹��� ��� ����
	public static final int OUTPUT_IMAGE        = 0;
	public static final int OUTPUT_BASE64       = 1;
	public static final int OUTPUT_IMAGE_BASE64 = 2;
	
	// �̹��� ũ�� 
	private static int imgW  = 640;
	private static int imgH  = 1136;

	// ������ �̹��� ũ��
	private static int cImgW = 642;  // 480
	private static int cImgH = 642;  // 390
	private static int cImgX = 0;    // 20
	private static int cImgY = 79; 	 // 120
	
	// �ؽ�Ʈ Ÿ��Ʋ ��ġ
	private static int TextX = 36;   // 20
	private static int TextY = 720;  // 20
	
	// �� ����
	private static final Color subTitleColor  = new Color(68,68,68);
	private static final Color contentColor   = new Color(102,102,102);
	
	// �۾� ũŰ
	private static final int   titleFontSize    = 34;
	private static final int   subTitleFontSize = 36;
	private static final int   contentsFontSize = 24;
	private static final int   barcodeFontSize  = 26;
	
	
	// �۾� ����
	private static       String FONT_NAME     = "Malgun Gothic";
//	private static final Font   titleFont     = new Font(FONT_NAME,     Font.TRUETYPE_FONT,             34);
//	private static final Font   barcodeFont   = new Font(FONT_NAME,     Font.TRUETYPE_FONT,             26);
//	private static final Font   subTitleFont  = new Font(FONT_NAME,     Font.TRUETYPE_FONT,             36);
//	private static final Font   contentFont   = new Font(FONT_NAME,     Font.TRUETYPE_FONT,             24);
	
	// ���ڵ� ����
	private static int barcodeW = 342;		// Barcode �ʺ� 
	private static int barcodeH = 130;		// Barcode ����
	
	/**
	 * constructor
	 */
	public CreateImage(Log logger) {
		this.logger = logger;
	}
	
	/**
	 * �ű� �̹��� ���
	 */
	private String getNewImgPath(String basePath) {
		String           path       = ou.concatString("yyyyMMdd");
		SimpleDateFormat dateFormat = new SimpleDateFormat(path, Locale.KOREAN);
		String           newImgPath = basePath + File.separator + dateFormat.format(Calendar.getInstance().getTime()) + File.separator;
		
		return newImgPath;
	}
	
	/**
	 * ���� �̹��� ���
	 * @param sentMMSImgpath
	 */
	private String getOldImgPath(String oldMMSImgPath) {
		String oldImgPath = oldMMSImgPath.substring(0, oldMMSImgPath.lastIndexOf(File.separator)) + File.separator;
		return oldImgPath;
	}	
	
	/**
	 * MMS �̹��� ����
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
			processStep = " ����̹���/�����̹���/������ �̹��� ��� Ȯ��";	//===========================
			// ������ �̹��� ��� ����
			String mmsSavePath = "";
			if(vo.getIsRetry() && StringUtils.isNotEmpty(vo.getOrgImgPath())){
				File orgMmsImg  = FileUtils.getFile(ou.concatString(vo.getOrgImgPath(), vo.getImgName()));
				if(orgMmsImg.exists()){
					mmsSavePath = getOldImgPath(vo.getOrgImgPath());	// ��߼� & �����̹��� �ִ� ��� ���� �̹��� ��η�
				} else {
					mmsSavePath = getNewImgPath(vo.getImgBasePath());	// �ű� �̹��� ���(��/��/��)
				}
			} else {
				mmsSavePath = getNewImgPath(vo.getImgBasePath());		// �ű� �̹��� ���(��/��/��)
			}
			mmsImageVO.setImgPath(mmsSavePath);
			File   mmsSaveDir  = FileUtils.getFile(mmsSavePath);				// MMS ���� ���
			if(!mmsSaveDir.exists()) {
				mmsSaveDir.mkdirs();
			}
			String mmsImgPath  = ou.concatString(mmsSavePath, vo.getImgName());	// ������ �̹��� ���
			File   mmsImg      = FileUtils.getFile(mmsImgPath);				// ������ �̹��� ���								// ������ �̹���
			File   backImg     = FileUtils.getFile(vo.getBackImgPath());		// ����̹���
			File   contentsImg = FileUtils.getFile(vo.getContentsImgPath());	// �����̹���
			if(logger.isDebugEnabled()){
				logger.debug(ou.concatString(logSb, logTitle, "backImg     : ", backImg    , ", ", backImg.exists()));
				logger.debug(ou.concatString(logSb, logTitle, "contentsImg : ", contentsImg, ", ", contentsImg.exists()));
				logger.debug(ou.concatString(logSb, logTitle, "mmsImgPath  : ", mmsImgPath));
				logger.debug(ou.concatString(logSb, logTitle, processOrder++,  processStep));
			}
			
			processStep = " �̹��� ���� ����";	//====================================================
			BufferedImage bufferedImage = new BufferedImage(imgW, imgH, BufferedImage.TYPE_INT_RGB);
			if(logger.isDebugEnabled()){logger.debug(ou.concatString(logSb, logTitle, processOrder++,  processStep));}
			
			processStep = " �׷��� �ļ� �� �ʱ�ȭ";	//====================================================
			Graphics2D g = bufferedImage.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,   RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_RENDERING,         RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);		// �۾� �ܰ� �ε巴��
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_DEFAULT);		// �׸� �ܰ� �ε巴��
			g.setRenderingHint(RenderingHints.KEY_DITHERING,         RenderingHints.VALUE_DITHER_DISABLE);
			g.setColor(new Color(209,210,212));	
			g.fillRect(0, 0, imgW, imgH);
			if(logger.isDebugEnabled()){logger.debug(ou.concatString(logSb, logTitle, processOrder++,  processStep));}
			
			processStep = ou.concatString(" ��� �̹��� ���� (", backImg,") "); //====================================================
			BufferedImage bufImg = ImageIO.read(backImg);
			if(bufImg.getWidth() != imgW) {
				Image nimg = bufImg.getScaledInstance(imgW, imgH, Image.SCALE_SMOOTH);
				g.drawImage(nimg, 0, 0, null);
			} else {
				g.drawImage(bufImg, null, 0, 0);
			}
			if(logger.isDebugEnabled()){logger.debug(ou.concatString(logSb, logTitle, processOrder++,  processStep));}
			
			processStep = " ��� �̹����� ���� �̹��� ���";
			try {
				int           contentsX      = cImgX;							// ������ �̹��� ��ġ - ���ʺ���
				int           contentsY      = cImgY;							// ������ �̹��� ��ġ - ������
				int           contentsW      = cImgW;							// ������ �̹��� �ʺ�
				int           contentsH      = cImgH;							// ������ �̹��� ����
				float         cRate          = (cImgW * 1000) / cImgH;			// ������ �̹��� ���� ����*���� ����
				BufferedImage contentsBufImg = ImageIO.read(contentsImg);
				int           w              = contentsBufImg.getWidth();		// ���� ������ �̹��� �ʺ�
				int           h              = contentsBufImg.getHeight();		// ���� ������ �̹��� ����
				float         contentsRate   = (w * 1000) / h;					// ���� �������̹��� ����*���� ����
				if(contentsRate > cRate ) {
					// ���� ���� �������� 
					contentsH = ((cImgW*1000/w)*h)/1000;
					contentsY = cImgY +  (cImgY - contentsH) / 2;
				} else {
					// ���� ���� ��������
					contentsW = ((cImgH*1000/h)*w)/1000;
					contentsX = (imgW - contentsW) / 2;
				}
				// MMS �̹��� ũ�⿡ �°� ������ 
				Image img = contentsBufImg.getScaledInstance(contentsW, contentsH, Image.SCALE_AREA_AVERAGING);
				g.drawImage(img, contentsX, contentsY, null);
				g.setColor (null);
				g.drawRect (contentsX-1, contentsY, contentsW, contentsH);
			} catch (Exception e) {
				if(logger.isInfoEnabled()){
					logger.info(ou.concatString(logSb, logTitle, processOrder, " �����̹��� ����", e));
				}
			}
			if(logger.isDebugEnabled()){logger.debug(ou.concatString(logSb, logTitle, processOrder++,  processStep));}
			
			processStep = " MMS ���� ����";	//====================================================
			Font titleFont = new Font(FONT_NAME, Font.TRUETYPE_FONT+Font.BOLD, titleFontSize); 
			g.setColor  (Color.WHITE);
			g.setFont   (titleFont);
			g.drawString(vo.getImgTitle(), TextX, 19 + titleFontSize );
			if(logger.isDebugEnabled()){logger.debug(ou.concatString(logSb, logTitle, processOrder++,  processStep));}
			
			processStep = " MMS ���� ����";	//====================================================
			// MMS ���� ���.
			int tY = TextY; 
			Font subTitleFont = new Font(FONT_NAME, Font.TRUETYPE_FONT+Font.BOLD, subTitleFontSize);
			g.setColor  (subTitleColor);
			g.setFont   (subTitleFont);
			g.drawString(vo.getImgSubTitle(), TextX, (tY += 32 + subTitleFontSize - 6));
			// ���� Ÿ��Ʋ ���� �ؽ�Ʈ ����
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
			
			processStep = " Barcode �̹��� ����";//==================================================== 
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
			// ���ڵ� ��ȣ ����
			Font barcodeFont = new Font(FONT_NAME, Font.TRUETYPE_FONT, barcodeFontSize);
			int barcodeTextX = (imgW-vo.getBarcode().length()*13)/2;
			int barcodeTextY = tY + barcodeH + (20-6) + barcodeFontSize;
			g.setColor  (subTitleColor);
			g.setFont   (barcodeFont);
			g.drawString(vo.getBarcode(), barcodeTextX, barcodeTextY);
			
			// ���ڵ� �̹��� ����
			try{
				if(tmpBarcode!=null){
					tmpBarcode.delete();
				}
			}catch(Exception ex){
				logger.error(ex.getMessage(), ex);
			}
			if(logger.isDebugEnabled()){logger.debug(ou.concatString(logSb, logTitle, processOrder++,  processStep));}			
			
			processStep = " ������ �̹����� ���Ϸ� ����";	//====================================================
			//    - .jpg ���Ϸ� ����
			//    - .jpg.base64 ���Ϸ� base64 ��ȯ �Ǿ� ����				
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
		
		// VO ���
		MMSImageVO imgVO = new MMSImageVO();
		imgVO.setImgName         (imgName);									// �̹��� ���� �� (�����ȣ_������ȣ.jpg)
		imgVO.setImgTitle        (imgTitle);								// �̹��� ����
		imgVO.setImgSubTitle     ("Ȧ���� ���� ���� ����");							// ������
		imgVO.setImgContents     (contents);								// �̹��� �׸� ����
		imgVO.setBackImgPath     (backImgPath); 							// ��� �̹��� ���
		imgVO.setImgBasePath     (imgSavePath);  							// MMS �̹��� ���� ���
		imgVO.setContentsImgPath (contentsImgPath);           				// ���� �̹��� ���
		imgVO.setBarcode         ("9111610089727");		// �����������۹�ȣ
		CreateImage cImage = new CreateImage(LogFactory.getLog("WHOLECAKE_ORDER_COUPON"));
		cImage.getImageForMMS(imgVO, "");
	}
}
