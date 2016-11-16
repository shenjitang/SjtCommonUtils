package org.shenjitang.common.util;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.*;

/**
 * 
 * 通用工具类
 * @author 冯炎
 * @version 1.0 2011-04-19
 */
public final class SystemUtils {

	private SystemUtils() {

	}

	public static String toHexString(int dec) {
		StringBuilder sb = new StringBuilder();
		sb.append("0x");
		for (int i = 0; i < 8; i++) {
			int tmp = (dec >> (7 - i % 8) * 4) & 0x0f;
			if (tmp < 10) {
				sb.append(tmp);
			} else {
				sb.append((char) ('A' + (tmp - 10)));
			}
		}
		return sb.toString();
	}

	public static boolean atChina() {
		return Locale.getDefault().toString().equalsIgnoreCase("zh_CN") ? true
				: false;
	}

	public static String getProperties(String file, String key) {
		ResourceBundle resource = ResourceBundle.getBundle(file, Locale
				.getDefault());
		return resource.getString(key);
	}

	public static Map<String, String> getProperties(String file) {
		ResourceBundle resource = ResourceBundle.getBundle(file, Locale
				.getDefault());
		Enumeration<String> keys = resource.getKeys();
		Map<String, String> map = new HashMap<String, String>();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			map.put(key, resource.getString(key));
		}
		return map;
	}

	public static boolean isNotEmpty(Object arg) {
		return !isEmpty(arg);
	}

	@SuppressWarnings("unchecked")
	public static boolean isEmpty(Object arg) {
		if (arg == null) {
			return true;
		}
		if (arg instanceof Object[]) {
			return ((Object[]) arg).length == 0;
		} else if (arg instanceof Collection) {
			return ((Collection) arg).isEmpty();
		} else if (arg instanceof Map) {
			return ((Map) arg).isEmpty();
		}
		return false;
	}

	public static boolean isWindowsSystem() {
		boolean isWindows = false;
		if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1) {
			isWindows = true;
		}
		return isWindows;
	}

	public static String systemCharset() {
		if (SystemUtils.atChina() && SystemUtils.isWindowsSystem()) {
			return "GBK";
		} else {
			return "UTF-8";
		}
	}

	public static String[][] newArray(int row, int col) {
		String[][] array = new String[row][col];
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				array[i][j] = "";
			}
		}
		return array;
	}

	public static String[] newArray(int length) {
		String[] array = new String[length];
		for (int i = 0; i < array.length; i++) {
			array[i] = "";
		}
		return array;
	}

	public static boolean even(int value) {
		return value % 2 == 0;
	}

	public static boolean odd(int value) {
		return !even(value);
	}

	public static Map<String, String> splitParameterToMap(String arg,
			String split) {

		String[] tokens = StringUtils.splitPreserveAllTokens(arg, split);
		if (SystemUtils.isEmpty(tokens)) {
			return null;
		}
		if (odd(tokens.length)) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		for (int j = 0; j < tokens.length; j++) {
			if (odd(j)) {
				map.put(tokens[j - 1], tokens[j]);
			}
		}
		return map;
	}

	public static String[] getPackageAllClassName(String packageName) {
		String classPath = Thread.currentThread().getContextClassLoader()
				.getResource("").getPath();
		if (classPath.startsWith("/") || classPath.startsWith("\\")) {
			classPath = classPath.substring(1);
		}
		String[] packagePathSplit = packageName.split("[.]");
		int packageLength = packagePathSplit.length;
		for (int i = 0; i < packageLength; i++) {
			classPath = classPath + File.separator + packagePathSplit[i];
		}
		File packeageDir = new File(classPath);
		if (packeageDir.isDirectory()) {
			String[] allClassName = packeageDir.list();
			String[] results = new String[allClassName.length];
			for (int i = 0; i < results.length; i++) {
				results[i] = packageName
						+ "."
						+ allClassName[i].substring(0, allClassName[i]
								.indexOf("."));
			}
			return results;
		}
		return null;
	}

}
