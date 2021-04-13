package kr.or.ddit.groupware.contoller;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.or.ddit.groupware.model.AttFileVo;
import kr.or.ddit.groupware.service.AttFileService;

@RequestMapping("file")
@Controller
public class FileController {
	
	@Resource(name="attFileService")
	private AttFileService attFileService;
	
	// 파일 다운로드
	@RequestMapping("fileDownload")
	public void fileDownload(int file_no, HttpServletResponse resp, HttpServletRequest req) throws IOException {
		
		AttFileVo attFileVo = attFileService.selectFile(file_no);
		
		
		String fileName = attFileVo.getFile_nm();
		
		
		String path = attFileVo.getFile_route();
		
		String browser = getBrowser(req);
		resp.setContentType("application/octet-stream; charset=UTF-8");
		resp.setHeader("Content-Description", "file download");
		resp.setHeader("Content-Disposition", "attachment; filename=\"".concat(getFileNm(browser, fileName)).concat("\""));
		resp.setHeader("Content-Transfer-Encoding", "binary");
		
		


		FileInputStream fis = new FileInputStream(path);
		ServletOutputStream sos = resp.getOutputStream();
		
		byte[] buff = new byte[512];
		while(fis.read(buff)!=-1) {
			
			sos.write(buff);
			
		}
		
		
		fis.close();
		sos.flush();
		sos.close();
	}

	public String getFileNm(String browser, String fileNm) {
		String reFileNm = null;
		try {
			if (browser.equals("MSIE") || browser.equals("Trident") || browser.equals("Edge")) {
				reFileNm = URLEncoder.encode(fileNm, "UTF-8").replaceAll("\\+", "%20");
			} else {
				if (browser.equals("Chrome")) {
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < fileNm.length(); i++) {
						char c = fileNm.charAt(i);
						if (c > '~') {
							sb.append(URLEncoder.encode(Character.toString(c), "UTF-8"));
						} else {
							sb.append(c);
						}
					}
					reFileNm = sb.toString();
				} else {
					reFileNm = new String(fileNm.getBytes("UTF-8"), "ISO-8859-1");
				}
				if (browser.equals("Safari") || browser.equals("Firefox"))
					reFileNm = URLDecoder.decode(reFileNm);
			}
		} catch (Exception e) {
		}
		return reFileNm;
	}
	
	public String getBrowser(HttpServletRequest req) {
		String userAgent = req.getHeader("User-Agent");
		if(userAgent.indexOf("MSIE") > -1
			|| userAgent.indexOf("Trident") > -1 //IE11
			|| userAgent.indexOf("Edge") > -1) {
			return "MSIE";
		} else if(userAgent.indexOf("Chrome") > -1) {
			return "Chrome";
		} else if(userAgent.indexOf("Opera") > -1) {
			return "Opera";
		} else if(userAgent.indexOf("Safari") > -1) {
			return "Safari";
		} else if(userAgent.indexOf("Firefox") > -1){
			return "Firefox";
		} else{
			return null;
		}
	}

	
}
