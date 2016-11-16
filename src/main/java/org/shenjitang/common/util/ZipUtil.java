package org.shenjitang.common.util;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.util.Enumeration;

/**
 * 使用java原生代码（org.apache.tools.zip，脱离系统的压缩工具）实现压缩、解压的工具类
 * @author mz.yyam
 * @date 2012-12-17 11:21:49
 */
public class ZipUtil {
	public static boolean doZip(String filesDirPath, String zipFilePath) {
		return doZip(new File(filesDirPath), zipFilePath);
	}

	public static boolean doZip(File inputFile, String zipFileName) {
		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(new FileOutputStream(zipFileName));
			boolean result = doZip(out, inputFile, "");

			return result;
		} catch (FileNotFoundException ex) {
			// ex.printStackTrace();
			return false;
		} catch (IOException ex) {
			// ex.printStackTrace();
			return false;
		} finally {
			try {
				out.close();
			} catch (IOException ex) {
				// ex.printStackTrace();
				return false;
			}
		}
	}

	private static boolean doZip(ZipOutputStream out, File f, String base) {
		try {
			if (f.isDirectory()) {
				File[] fl = f.listFiles();
				out.putNextEntry(new ZipEntry(base + "/"));
				base = StringUtils.isBlank(base) ? "" : base + "/";
				for (int i = 0; i < fl.length; i++) {
					doZip(out, fl[i], base + fl[i].getName());
				}
			} else {
				out.putNextEntry(new ZipEntry(base));
				FileInputStream in = new FileInputStream(f);
				int b;
				while ((b = in.read()) != -1) {
					out.write(b);
				}
				in.close();
			}
			return true;
		} catch (IOException ex) {
			// ex.printStackTrace();
			return false;
		}
	}

	public static boolean unZip(String srcFile, String dest, boolean deleteFile) {
		try {
			File file = new File(srcFile);
			if (!file.exists()) {
				// throw new RuntimeException("解压文件不存在!");
				return false;
			}
			ZipFile zipFile = new ZipFile(file);
			Enumeration e = zipFile.getEntries();
			while (e.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) e.nextElement();
				if (zipEntry.isDirectory()) {
					String name = zipEntry.getName();
					name = name.substring(0, name.length() - 1);
					File f = new File(dest, name);
					f.mkdirs();
				} else {
					File f = new File(dest, zipEntry.getName());
					f.getParentFile().mkdirs();
					f.createNewFile();
					InputStream is = zipFile.getInputStream(zipEntry);
					FileOutputStream fos = new FileOutputStream(f);
					int length = 0;
					byte[] b = new byte[1024];
					while ((length = is.read(b, 0, 1024)) != -1) {
						fos.write(b, 0, length);
					}
					is.close();
					fos.close();
				}
			}

			if (zipFile != null) {
				zipFile.close();
			}

			if (deleteFile) {
				file.deleteOnExit();
			}

			return true;
		} catch (IOException ex) {
			return false;
		}
	}
    /*
	public static void main(String[] args) throws Exception {
		// 压缩文件夹
		//boolean resultOfZip = ZipUtil.doZip("E:/ziptest", "E:/test.youarestupid");

		// 解压缩
		boolean resultOfUnZip = ZipUtil.unZip("E:/test.youarestupid", "E:/ziptest/", false);

		//System.out.println("压缩结果：" + resultOfZip + "\n解压缩结果：" + resultOfUnZip);
	}
    */
}
