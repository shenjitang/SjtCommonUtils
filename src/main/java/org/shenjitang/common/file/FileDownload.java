package org.shenjitang.common.file;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * User: gang.xie
 * Date: 14-4-15
 * Time: 下午3:20
 */
public class FileDownload {

    public static void download(HttpServletRequest request, HttpServletResponse response,String filePath,String fileName) throws Exception{
        File file = new File(filePath + "/" + fileName);
            if (file.exists()) {
                InputStream inStream = new FileInputStream(file);
                try {
                    ServletOutputStream streamOut = response.getOutputStream();
                    try {
                        response.reset();
                        String agent = request.getHeader("USER-AGENT");
                        response.setContentType("application/x-msdownload;charset=UTF-8");
                        if (null != agent && -1 != agent.indexOf("MSIE")) {//java.net.URLEncoder.encode(
                            fileName = java.net.URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
                            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
                        } else if (null != agent && -1 != agent.indexOf("Mozilla")) {
                            response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes(), "iso-8859-1") + "\"");
                        }
                        int bytesRead = 0;
                        byte[] buffer = new byte[1024];
                        while ((bytesRead = inStream.read(buffer, 0, 1024)) > 0) {
                            streamOut.write(buffer, 0, bytesRead);
                        }
                        streamOut.flush();
                    } finally {
                        streamOut.close();
                    }
                } finally {
                    inStream.close();
                }
            }
    }
}
