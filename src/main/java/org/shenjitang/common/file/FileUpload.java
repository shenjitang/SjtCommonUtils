package org.shenjitang.common.file;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.shenjitang.common.util.StringUtilEx;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

/**
 * User: gang.xie
 * Date: 14-4-15
 * Time: 下午3:11
 */

public class FileUpload {
    private Map<String, String> params = new HashMap<String, String>();

    private List<File> files = new ArrayList<File>();

    private List<File> needDelete = new ArrayList<File>();

    private static final Log logger = LogFactory.getLog(FileUpload.class);

    private static DiskFileItemFactory factory = new DiskFileItemFactory();

    /**
     * @param request
     * @param tempDirPath
     * @param fileName    文件名称可以为NULL，如果是NULL则用文件原有名称,不要带后缀名
     * @return
     */
    public static FileUpload parse(HttpServletRequest request, String tempDirPath, String fileName) {
        ServletFileUpload upload = new ServletFileUpload(factory);
        FileUpload result = new FileUpload();
        Enumeration<String> keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String value = request.getParameter(key);
            if (!result.params.containsKey(key)) {
                result.params.put(key, value);
            }
        }

        try {
            List<FileItem> fileItems = upload.parseRequest(request);

            for (FileItem item : fileItems) {

                if (item.isFormField()) {
                    String key = item.getFieldName();
                    String value = item.getString("UTF-8");
                    if (!result.params.containsKey(key)) {
                        result.params.put(key, value);
                    }
                    continue;
                }
                String filename = item.getName();
                if (StringUtils.isNotBlank(fileName)) {
                    filename = fileName + filename.substring(filename.lastIndexOf("."), filename.length());
                }
                File tempDir = getTempDir(tempDirPath);
                File tempFile = result.getTempFile(tempDir, filename);
                item.write(tempFile);
            }
        } catch (Exception e) {
            String url = null;
            try {
                url = request.getRequestURL().toString();
            } catch (Exception ignore) {
            }
            logger.error("上载文件异常 url = " + url, e);
        }
        return result;
    }

    /**
     * @param request
     * @param tempDirPath
     * @return
     */
    public static FileUpload parse(HttpServletRequest request, String tempDirPath) {
        ServletFileUpload upload = new ServletFileUpload(factory);
        FileUpload result = new FileUpload();
        Enumeration<String> keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String value = request.getParameter(key);
            if (!result.params.containsKey(key)) {
                result.params.put(key, value);
            }
        }

        try {
            List<FileItem> fileItems = upload.parseRequest(request);

            for (FileItem item : fileItems) {

                if (item.isFormField()) {
                    String key = item.getFieldName();
                    String value = item.getString("UTF-8");
                    if (!result.params.containsKey(key)) {
                        result.params.put(key, value);
                    }
                    continue;
                }
                String filename = item.getName();
                File tempDir = getTempDir(tempDirPath);
                File tempFile = result.getTempFile(tempDir, filename);
                item.write(tempFile);
            }
        } catch (Exception e) {
            String url = null;
            try {
                url = request.getRequestURL().toString();
            } catch (Exception ignore) {
            }
            logger.error("上载文件异常 url = " + url, e);
        }
        return result;
    }

    private static File getTempDir(String tempDirPath) {
        File result = new File(tempDirPath);
        if (!result.exists())
            result.mkdirs();
        return result;
    }

    private File getTempFile(File tempDir, String filename) {
        File result = new File(tempDir, filename);
        if (!result.exists()) {
            files.add(result);
            needDelete.add(result);
            return result;
        }
        File temp = null;
        do {
            String ram = StringUtilEx.randomAlphabetic(10);
            temp = new File(tempDir, ram);
        } while (!temp.exists());
        temp.mkdirs();
        result = new File(temp, filename);
        files.add(result);
        needDelete.add(temp);
        return result;
    }

    public String getParameter(String key) {
        return params.get(key);
    }

    public Map<String, String> getParameters() {
        return params;
    }

    public List<File> getUploadFiles() {
        return files;
    }

}
