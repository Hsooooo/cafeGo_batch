package co.kr.cafego.core.view;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

public class FileDownloadView extends AbstractView{
	public static final String MODEL_NAME_DOWNLOAD_FILE = "downloadFile";
	public static final String MODEL_NAME_DOWNLOAD_FILE_NAME = "downloadFileName";

	public FileDownloadView() {
		setContentType("application/download; charset=utf-8");
	}

	protected void renderMergedOutputModel(Map<String, Object> model, 
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		Object obj = model.get("downloadFile");
		String filename = (String)model.get("downloadFileName");

		if ((!(obj instanceof File)) && (!(obj instanceof String))) {
			throw new IllegalArgumentException("The target object to be downloaded must be String or File.");
		}
		InputStream in;
		int length;
		
		if ((obj instanceof File)) {
			File file = (File)obj;

			if (!file.exists()) {
				throw new FileNotFoundException(file.getPath());
			}

			if (filename == null) {
				filename = file.getName();
			}
			length = (int)file.length();
			in = new FileInputStream(file);
		} else {
			String str = (String)obj;

			Assert.notNull(str);
			Assert.hasText(filename);

			length = str.length();
			in = new ByteArrayInputStream(str.getBytes());
		}

		response.setContentType(getContentType());
		response.setContentLength(length);

		response.setHeader("Content-Disposition","attachment; filename=\"" + reviseFilename(request, filename) + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");

		OutputStream out = response.getOutputStream();

		FileCopyUtils.copy(in, out);

		out.flush();
	}

	private String reviseFilename(HttpServletRequest request, String filename) throws Exception {
		String userAgent = request.getHeader("User-Agent");
		if (userAgent != null) {
			if ((userAgent.contains("MSIE")) || (userAgent.contains("Trident"))) {
				return URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
			}
			
			return new String(filename.getBytes("UTF-8"), "ISO-8859-1");
		}

		return filename;
	}
}