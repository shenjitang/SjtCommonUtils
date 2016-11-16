package org.shenjitang.common.util;

import org.apache.commons.lang.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtils {

	private UrlUtils() {

	}

	private static String RELATIVE = "\\.\\./";

	private static final List<String> domainList = Collections.unmodifiableList(Arrays.asList(new String[] { "com",
			"gov", "net", "edu", "org", "mil", "cn" }));

	public static String getUrlPath(String url) {
		if (StringUtils.isEmpty(url)) {
			throw new IllegalArgumentException(FileUtils.INVALID_PARAMETER);
		}
		String path = "";
		try {
			URL u = new URL(url);
			path = u.getPath();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return path;
	}

	public static String getUrlDir(String url) {
		String path = getUrlPath(url);
		return path.substring(0, path.lastIndexOf("/") + 1);
	}

	public static String getHostUrl(String url) {
		if (StringUtils.isEmpty(url)) {
			throw new IllegalArgumentException(FileUtils.INVALID_PARAMETER);
		}
		String hostUrl = "";
		try {
			URL u = new URL(url);
			hostUrl = u.getProtocol() + "://" + u.getHost();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return hostUrl;
	}

	public static String getHost(String url) {
		if (StringUtils.isEmpty(url)) {
			throw new IllegalArgumentException(FileUtils.INVALID_PARAMETER);
		}
		String host = "";
		try {
			URL u = new URL(url);
			host = u.getHost();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return host;
	}

	public static String getProtocol(String url) {
		if (StringUtils.isEmpty(url)) {
			throw new IllegalArgumentException(FileUtils.INVALID_PARAMETER);
		}
		String protocol = "";
		try {
			URL u = new URL(url);
			protocol = u.getProtocol();
		} catch (MalformedURLException e) {
			throw new RuntimeException("URL协议错误");
		}
		return protocol;
	}

	public static String getTopDomain(String url) {
		if (StringUtils.isEmpty(url)) {
			throw new IllegalArgumentException(FileUtils.INVALID_PARAMETER);
		}
		String topDomain = "";
		String host = getHost(url);
		for (String domain : domainList) {
			int i = host.indexOf(domain);
			if (i > 0) {
				topDomain = host.substring(0, i - 1);
				break;
			}
		}
		String[] domains = topDomain.split("\\.");
		topDomain = domains.length == 2 ? domains[1] : domains[0];
		return topDomain;
	}

	public static String getUrlPattern(String url) {
		return ".*" + getTopDomain(url) + ".*";
	}

	public static String getPosition(String subUrl, String url) throws MalformedURLException {
		URL uri = new URL(url);
		String tou = subUrl.substring(0, 5);
		if (tou.equalsIgnoreCase("http:")) {
			return subUrl;
		}
		if ((subUrl.startsWith("\\")) && (subUrl.endsWith("\""))) {
			subUrl = subUrl.substring(2, subUrl.length() - 2);
		}
		if (subUrl.indexOf("../") == 0) {
			if (url.endsWith("/")) {
				String u = url.substring(0, url.lastIndexOf("/"));
				String sub = (u.substring(0, u.lastIndexOf("/")) + "/" + subUrl).replace("/../", "/");
				return sub;
			}
		}
		// 拼接不以/开头的链接
		if ((!subUrl.contains(uri.getHost())) && subUrl.indexOf("/") != 0) {
			String sub = url.substring(0, url.lastIndexOf("/")) + "/" + subUrl;
			return sub;
		} else if (!subUrl.contains(uri.getHost())) {
			String subUrl2 = uri.getProtocol() + "://" + uri.getHost() + subUrl;
			return subUrl2;
		}
		return subUrl;
	}

	public static String getAbsoluteUrl(String refUrl, String targetUrl) {
		String prifix = "";
		if (refUrl.startsWith("http://")) {
			prifix = "http://";
		} else if (refUrl.startsWith("https://")) {
			prifix = "https://";
		}
		refUrl = refUrl.replaceAll("http://", "").replaceAll("https://", "").replaceAll("\\?.*$", "");
		String[] paths = refUrl.split("/");
		if (StringUtils.isEmpty(targetUrl)) {
			return prifix + refUrl;
		}
		if (targetUrl.startsWith("?")) {
			return prifix + refUrl + targetUrl;
		}
		if (targetUrl.startsWith("http")) {
			return targetUrl;
		}
		if (targetUrl.startsWith("/")) {
			return prifix + paths[0] + targetUrl;
		}
		if(targetUrl.startsWith("./")){
			if(refUrl.contains("stats.paj.gr.jp")){
				//该网站url比较特殊
				return prifix+refUrl.replace(paths[paths.length-1], "")+targetUrl;
			}
			return prifix+refUrl+targetUrl.substring(2);
		}
		if (targetUrl.startsWith("../")) {
			String currentUrl = "";
			Pattern p = Pattern.compile(RELATIVE);
			Matcher m = p.matcher(targetUrl);
			int length = 0;
			while (m.find()) {
				length++;
			}
			if (paths.length < length + 2) {
				// throw new RuntimeException("参照地址不符合获取绝对路径算法");
				length = 1;// 异常情况下特殊处理(针对中国金属网，这样处理后url可以正确拼接)
			}
			for (int i = 0; i < paths.length - length - 1; i++) {
				currentUrl += paths[i] + "/";
			}
			targetUrl = targetUrl.replaceAll(RELATIVE, "");
			return prifix + currentUrl + targetUrl;
		} else {
			String currentUrl = "";
			if(paths.length > 1){
                if (refUrl.endsWith("/")) {
                    for(int i = 0;i < paths.length;i++){
                        currentUrl += paths[i] + "/";
                    }
                } else {
                    for(int i = 0;i < paths.length - 1;i++){
                        currentUrl += paths[i] + "/";
                    }
                }
				return prifix + currentUrl + targetUrl;
			} else {
				return prifix + paths[0] + "/" + targetUrl;
			}
		}
	}

	public static void main(String[] args) throws Exception {
		// System.out.println(getPosition("/default.aspx",
		// "http://www.baidu.com"));
		System.out.println("../201".startsWith("./"));
//		System.out.println("tourism.hainan.gov.cn/goverment/lvyoutongji/tongjihuizong/2012/".split("/").length);
//		System.out.println(getAbsoluteUrl("http://tourism.hainan.gov.cn/goverment/lvyoutongji/tongjihuizong/2012/", "./201209/t20120919_34785.html"));
	}
}
