/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.shenjitang.common;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author admin
 */
public class HtmlUtils {

    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    private static final String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符

    /**
     * 删除Html标签
     *
     * @param htmlStr
     * @return
     */
    public static String delHTMLTag(String htmlStr) {
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
        return htmlStr.trim(); // 返回文本字符串
    }

    public static String getTextFromHtml(String htmlStr) {
        if(StringUtils.isBlank(htmlStr)){
            return "";
        }
        htmlStr = delHTMLTag(htmlStr);
        htmlStr = htmlStr.replaceAll(" ", "");
        htmlStr = htmlStr.replaceAll("　", "");
        htmlStr = htmlStr.replaceAll("&nbsp;", "");
        return htmlStr;
    }

    public List<String[]> find(String tag, List<String> pNameList, String htmlDoc) {
        List<String[]> resList = new ArrayList();
        boolean inTag = false;
        boolean inTagName = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < htmlDoc.length(); i++) {
            char c = htmlDoc.charAt(i);
            String aTag = null;
            switch (c) {
                case '<':
                    inTagName = true;
                    break;
                case ' ':
                    if (inTagName) {
                        inTag = true;
                        aTag = sb.toString();
                        sb.delete(0, sb.length());
                    }
                default:
                    sb.append(c);
                    break;
            }
        }
        return resList;
    }
}
