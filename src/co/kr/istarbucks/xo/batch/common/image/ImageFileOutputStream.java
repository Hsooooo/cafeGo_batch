/*
 * @(#) $Id: ImageFileOutputStream.java,v 1.1 2016/11/10 00:53:43 dev99 Exp $
 * 
 * Starbucks Service
 * 
 * Copyright 2012 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

/**
 * 
 */
package co.kr.istarbucks.xo.batch.common.image;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;



public class ImageFileOutputStream extends OutputStream {
	private BufferedOutputStream imageOutput = null; // NOPMD
	private Base64OutputStream base64Output = null; // NOPMD
	
	
	public ImageFileOutputStream(ByteArrayOutputStream out, File file, int writeType) throws Exception {
		switch(writeType) {
			case CreateImage.OUTPUT_IMAGE : {
				imageOutput = new BufferedOutputStream(new FileOutputStream(file));
				break;
			} case CreateImage.OUTPUT_BASE64 : {
				base64Output = new Base64OutputStream(out);
				break;
			} case CreateImage.OUTPUT_IMAGE_BASE64 : {
				imageOutput = new BufferedOutputStream(new FileOutputStream(file));
				base64Output = new Base64OutputStream(out);
				break;
			} default : {
				throw new Exception("Invalid WriteType");
			}
		}
	}
		
	public ImageFileOutputStream(File file, int writeType) throws Exception {
		switch(writeType) {
			case CreateImage.OUTPUT_IMAGE: {
				imageOutput = new BufferedOutputStream(new FileOutputStream(file));
				break;
			}
			case CreateImage.OUTPUT_BASE64: {
				this.base64Output = new Base64OutputStream(new FileOutputStream(FileUtils.getFile(new String[]{file.toString() + ".base64"})));
	            return;
			}
			case CreateImage.OUTPUT_IMAGE_BASE64: {
				imageOutput = new BufferedOutputStream(new FileOutputStream(file));
				this.base64Output = new Base64OutputStream(new FileOutputStream(FileUtils.getFile(new String[]{file.toString() + ".base64"})));

				break;
			}
			default: {
				throw new Exception("Invalid WriteType");
			}
		}
	}

	public void write(byte[] b, int off, int len) throws IOException {
		if(imageOutput != null) {
			imageOutput.write(b, off, len);
		}
		if(base64Output != null) {
			base64Output.write(b, off, len);
		}
	}

	public void write(byte[] b) throws IOException {
		if(imageOutput != null) {
			imageOutput.write(b);
		}
		if(base64Output != null) {
			base64Output.write(b);
		}
	}

	public void write(int b) throws IOException {
		if(imageOutput != null) {
			imageOutput.write(b);
		}
		if(base64Output != null) {
			base64Output.write(b);
		}
	}

	public void close() throws IOException {
		if(imageOutput != null) {
			imageOutput.close();
		}
		if(base64Output != null) {
			base64Output.close();
		}
	}

	public void flush() throws IOException {
		if(imageOutput != null) {
			imageOutput.flush();
		}
		if(base64Output != null) {
			base64Output.flush();
		}
	}
}
