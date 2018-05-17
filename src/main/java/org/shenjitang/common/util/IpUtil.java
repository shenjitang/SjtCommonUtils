package org.shenjitang.common.util;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * IP地址工具类
 *
 * @author xudongdong
 */
public class IpUtil {

    private static final String addressesApi = "http://ip.taobao.com/service/getIpInfo.php";//调用淘宝API

    /**
     * 私有化构造器
     */
    private IpUtil() {
    }

    /**
     * 获取真实IP地址
     * <p>使用getRealIP代替该方法</p>
     *
     * @param request req
     * @return ip
     */
    @Deprecated
    public static String getClientIpByReq(HttpServletRequest request) {
        // 获取客户端ip地址
        String clientIp = request.getHeader("x-forwarded-for");

        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
        /*
         * 对于获取到多ip的情况下，找到公网ip.
         */
        String sIP = null;
        if (clientIp != null && !clientIp.contains("unknown") && clientIp.indexOf(",") > 0) {
            String[] ipsz = clientIp.split(",");
            for (String anIpsz : ipsz) {
                if (!isInnerIP(anIpsz.trim())) {
                    sIP = anIpsz.trim();
                    break;
                }
            }
            /*
             * 如果多ip都是内网ip，则取第一个ip.
             */
            if (null == sIP) {
                sIP = ipsz[0].trim();
            }
            clientIp = sIP;
        }
        if (clientIp != null && clientIp.contains("unknown")) {
            clientIp = clientIp.replaceAll("unknown,", "");
            clientIp = clientIp.trim();
        }
        if ("".equals(clientIp) || null == clientIp) {
            clientIp = "127.0.0.1";
        }
        return clientIp;
    }

    /**
     * 判断IP是否是内网地址
     *
     * @param ipAddress ip地址
     * @return 是否是内网地址
     */
    public static boolean isInnerIP(String ipAddress) {
        boolean isInnerIp;
        long ipNum = getIpNum(ipAddress);
        /**
         私有IP：A类  10.0.0.0-10.255.255.255
         B类  172.16.0.0-172.31.255.255
         C类  192.168.0.0-192.168.255.255
         当然，还有127这个网段是环回地址
         **/
        long aBegin = getIpNum("10.0.0.0");
        long aEnd = getIpNum("10.255.255.255");

        long bBegin = getIpNum("172.16.0.0");
        long bEnd = getIpNum("172.31.255.255");

        long cBegin = getIpNum("192.168.0.0");
        long cEnd = getIpNum("192.168.255.255");
        isInnerIp = isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd)
                || ipAddress.equals("127.0.0.1");
        return isInnerIp;
    }

    private static long getIpNum(String ipAddress) {
        String[] ip = ipAddress.split("\\.");
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);

        return a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
    }

    private static boolean isInner(long userIp, long begin, long end) {
        return (userIp >= begin) && (userIp <= end);
    }

    public static String getRealIP(HttpServletRequest request) {
        // 获取客户端ip地址
        String clientIp = request.getHeader("x-forwarded-for");

        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }

        String[] clientIps = clientIp.split(",");
        if (clientIps.length <= 1) return clientIp.trim();

        // 判断是否来自CDN
        if (isComeFromCDN(request)) {
            return clientIps[clientIps.length - 2].trim();
        }

        return clientIps[clientIps.length - 1].trim();
    }

    private static boolean isComeFromCDN(HttpServletRequest request) {
        String host = request.getHeader("host");
        return host.contains("www.189.cn") || host.contains("shouji.189.cn") || host.contains(
                "image2.chinatelecom-ec.com") || host.contains(
                "image1.chinatelecom-ec.com");
    }

    /**
     * 根据IP地址获取详细的地域信息
     * 淘宝API : http://ip.taobao.com/service/getIpInfo.php?ip=218.192.3.42
     * 新浪API : http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=218.192.3.42
     *
     * @author Cryhelyxx
     * @version 1.0
     * @File AddressUtils.java
     * @Package org.gditc.weicommunity.util
     * @Description TODO
     * @Copyright Copyright © 2014
     * @Site https://github.com/Cryhelyxx
     * @Blog http://blog.csdn.net/Cryhelyxx
     * @Email cryhelyxx@gmail.com
     * @Company GDITC
     * @Date 2014年11月6日 下午1:46:37
     */
    public static class AddressUtils {

        /**
         * @param content        请求的参数 格式为：name=xxx&pwd=xxx
         * @param encodingString 服务器端请求编码。如GBK,UTF-8等
         */
        public static String getAddresses(String content, String encodingString, Integer timeOut) {
            // 从http://whois.pconline.com.cn取得IP所在的省市区信息
            String returnStr = getResult(addressesApi, content, encodingString, timeOut);
            if (returnStr != null) {
                // 处理返回的省市区信息
//                System.out.println("(1) unicode转换成中文前的returnStr : " + returnStr);
                returnStr = decodeUnicode(returnStr);
//                System.out.println("(2) unicode转换成中文后的returnStr : " + returnStr);
                String[] temp = returnStr.split(",");
                if (temp.length < 3) {
                    return "0";//无效IP，局域网测试
                }
                return returnStr;
            }
            return null;
        }

        /**
         * @param urlStr   请求的地址
         * @param content  请求的参数 格式为：name=xxx&pwd=xxx
         * @param encoding 服务器端请求编码。如GBK,UTF-8等
         * @return
         */
        private static String getResult(String urlStr, String content, String encoding, Integer timeOut) {
            URL url = null;
            HttpURLConnection connection = null;
            try {
                url = new URL(urlStr);
                connection = (HttpURLConnection) url.openConnection();// 新建连接实例
                connection.setConnectTimeout(timeOut);// 设置连接超时时间，单位毫秒
                connection.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒
                connection.setDoOutput(true);// 是否打开输出流 true|false
                connection.setDoInput(true);// 是否打开输入流true|false
                connection.setRequestMethod("POST");// 提交方法POST|GET
                connection.setUseCaches(false);// 是否缓存true|false
                connection.connect();// 打开连接端口
                DataOutputStream out = new DataOutputStream(connection
                        .getOutputStream());// 打开输出流往对端服务器写数据
                out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
                out.flush();// 刷新
                out.close();// 关闭输出流
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据
                // ,以BufferedReader流来读取
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                reader.close();
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();// 关闭连接
                }
            }
            return null;
        }

        /**
         * unicode 转换成 中文
         *
         * @param theString
         * @return
         * @author fanhui 2007-3-15
         */
        public static String decodeUnicode(String theString) {
            char aChar;
            int len = theString.length();
            StringBuffer outBuffer = new StringBuffer(len);
            for (int x = 0; x < len; ) {
                aChar = theString.charAt(x++);
                if (aChar == '\\') {
                    aChar = theString.charAt(x++);
                    if (aChar == 'u') {
                        int value = 0;
                        for (int i = 0; i < 4; i++) {
                            aChar = theString.charAt(x++);
                            switch (aChar) {
                                case '0':
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                                    value = (value << 4) + aChar - '0';
                                    break;
                                case 'a':
                                case 'b':
                                case 'c':
                                case 'd':
                                case 'e':
                                case 'f':
                                    value = (value << 4) + 10 + aChar - 'a';
                                    break;
                                case 'A':
                                case 'B':
                                case 'C':
                                case 'D':
                                case 'E':
                                case 'F':
                                    value = (value << 4) + 10 + aChar - 'A';
                                    break;
                                default:
                                    throw new IllegalArgumentException(
                                            "Malformed      encoding.");
                            }
                        }
                        outBuffer.append((char) value);
                    } else {
                        if (aChar == 't') {
                            aChar = '\t';
                        } else if (aChar == 'r') {
                            aChar = '\r';
                        } else if (aChar == 'n') {
                            aChar = '\n';
                        } else if (aChar == 'f') {
                            aChar = '\f';
                        }
                        outBuffer.append(aChar);
                    }
                } else {
                    outBuffer.append(aChar);
                }
            }
            return outBuffer.toString();
        }

        // 测试
        public static void main(String[] args) {
            AddressUtils addressUtils = new AddressUtils();
            // 测试ip 219.136.134.157 中国=华南=广东省=广州市=越秀区=电信
            String ip = "122.49.20.247";
            String address = getAddresses("ip=" + ip, "utf-8", 2000);
            System.out.println(address);
            // 输出结果为：广东省,广州市,越秀区
            try {
                System.out.println(getMyIP());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getMyIP() throws IOException {
        String url = "http://ip.chinaz.com/getip.aspx";
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            String jsonText = sb.toString();
            jsonText = jsonText.replaceAll("'", "");
            jsonText = jsonText.substring(1, jsonText.length() - 1);
            jsonText = jsonText.replaceAll(",", "<br/>");
            return jsonText;
        } finally {
            is.close();
            // System.out.println("同时 从这里也能看出 即便return了，仍然会执行finally的！");
        }
    }
}
