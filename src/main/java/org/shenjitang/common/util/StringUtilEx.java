/*
 * StringUtils.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.shenjitang.common.util;

import org.apache.commons.lang.StringUtils;
import org.shenjitang.common.StringNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author xiaolie
 */
public class StringUtilEx {

    private static String[] source = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private static Map<String, Pattern> map = new ConcurrentHashMap<String, Pattern>();
    public static String REGEX_EMAIL = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
    public static String IS_REGEX_MOBILE = "^1\\d{10}$";
    public static String IS_REGEX_EMAIL = "^[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?$";

    public static String trim(String input) {
        if (StringUtils.isNotEmpty(input)) {
            return replace(RegularExpressionUtils.RegularExpression.WHITE_SPACE, input, "");
        }
        return "";
    }

    public static String replace(RegularExpressionUtils.RegularExpression reg, String input, String value) {
        if (StringUtils.isNotEmpty(input)) {
            return input.replaceAll(reg.getPattern(), value).trim();
        }
        return "";
    }

    public static String[] split(RegularExpressionUtils.RegularExpression exp, String input) {
        if (StringUtils.isNotEmpty(input)) {
            return input.split(exp.getPattern());
        }
        return null;
    }

    /**
     * Creates a new instance of StringUtils
     */
    public StringUtilEx() {
    }

    public static void parseHttpParms(String line, Map<String, String> map) {
        if (line != null) {
            line = line.trim();
            String[] strArray = line.split("&");
            for (int i = 0; i < strArray.length; i++) {
                int idx = strArray[i].indexOf('=');
                if (idx > 0) {
                    String key = strArray[i].substring(0, idx);
                    String value = strArray[i].substring(idx + 1);
                    map.put(key, value);
                }
            }
        }
    }

    public static StringNumber splitStringNumber(String inStr) {
        StringBuilder sb = new StringBuilder();
        int i = inStr.length();
        for (; i > 0; i--) {
            char c = inStr.charAt(i - 1);
            if (c >= '0' && c <= '9') {
                sb.insert(0, c);
            } else {
                break;
            }
        }
        if (sb.length() > 0) {
            int num = Integer.parseInt(sb.toString());
            String str = inStr.substring(0, i);
            return new StringNumber(str, num);
        } else {
            return new StringNumber(inStr, 0);
        }
    }

    public static List<String> getFieldList(String cvsRecord, char fieldSeparator) throws Exception {
        return getFieldList(cvsRecord, String.valueOf(fieldSeparator));
    }

    public static List<String> getFieldList(String cvsRecord, String fieldSeparator) throws Exception {
        return getFieldList(cvsRecord, fieldSeparator, '"');
    }

    public static List<String> getFieldList(String cvsRecord, String fieldSeparator, char transf) throws Exception {
        boolean inQuotation = false;
        ArrayList<String> values = new ArrayList<String>();
        StringBuilder word = new StringBuilder(cvsRecord.length());
        char c = ' ';
        for (int i = 0; i < cvsRecord.length(); i++) {
            c = cvsRecord.charAt(i);
            if (!inQuotation) {
                if (cvsRecord.indexOf(fieldSeparator, i) == i) {
                    //if (c == FieldSeparator) {
                    i += fieldSeparator.length() - 1;
                    values.add(word.toString());
                    word.delete(0, word.length());
                } else if (c == transf) {
                    inQuotation = true;
                } else {
                    word.append(c);
                }
            } else { // in a quotation
                if (c == transf) {
                    if (++i < cvsRecord.length()) {
                        c = cvsRecord.charAt(i);
                        if (c == transf) {
                            word.append(c);
                        } else if (cvsRecord.indexOf(fieldSeparator, i) == i) {
                            //} else if ( c == FieldSeparator) {
                            i += fieldSeparator.length() - 1;
                            values.add(word.toString());
                            word.delete(0, word.length());
                            inQuotation = false;
                        } else {
                            //word.append(transf);
                            inQuotation = false;
                            word.append(c);
                        }
                    }
                } else {
                    word.append(c);
                }
            }
        }
        values.add(word.toString());
        return values;
    }

    public static List<String> getTrimedFieldList(String cvsRecord, String fieldSeparator) throws Exception {
        return getTrimedFieldList(cvsRecord, fieldSeparator, '"');
    }

    public static List<String> getTrimedFieldList(String cvsRecord, String fieldSeparator, char transf) throws Exception {
        boolean inQuotation = false;
        ArrayList<String> values = new ArrayList<String>();
        StringBuilder word = new StringBuilder(cvsRecord.length());
        char c = ' ';
        for (int i = 0; i < cvsRecord.length(); i++) {
            c = cvsRecord.charAt(i);
            if (!inQuotation) {
                if (cvsRecord.indexOf(fieldSeparator, i) == i) {
                    //if (c == FieldSeparator) {
                    i += fieldSeparator.length() - 1;
                    values.add(word.toString().trim());
                    word.delete(0, word.length());
                } else if (c == transf) {
                    inQuotation = true;
                } else {
                    word.append(c);
                }
            } else { // in a quotation
                if (c == transf) {
                    if (++i < cvsRecord.length()) {
                        c = cvsRecord.charAt(i);
                        if (c == transf) {
                            word.append(c);
                        } else if (cvsRecord.indexOf(fieldSeparator, i) == i) {
                            //} else if ( c == FieldSeparator) {
                            i += fieldSeparator.length() - 1;
                            values.add(word.toString().trim());
                            word.delete(0, word.length());
                            inQuotation = false;
                        } else {
                            //word.append(transf);
                            inQuotation = false;
                            word.append(c);
                        }
                    }
                } else {
                    word.append(c);
                }
            }
        }
        values.add(word.toString().trim());
        return values;
    }

    public static List<String> getFieldListSDQuotation(String cvsRecord, String fieldSeparator) {
        boolean inQuotation = false;
        ArrayList<String> values = new ArrayList<String>();
        StringBuilder word = new StringBuilder(cvsRecord.length());
        char c = ' ';
        char transf = '\"';
        for (int i = 0; i < cvsRecord.length(); i++) {
            c = cvsRecord.charAt(i);
            if (!inQuotation) {
                if (cvsRecord.indexOf(fieldSeparator, i) == i) {
                    //if (c == FieldSeparator) {
                    i += fieldSeparator.length() - 1;
                    values.add(word.toString());
                    word.delete(0, word.length());
                } else if (c == '\'') {
                    transf = '\'';
                    inQuotation = true;
                } else if (c == '\"') {
                    transf = '\"';
                    inQuotation = true;
                } else {
                    word.append(c);
                }
            } else { // in a quotation
                if (c == transf) {
                    if (++i < cvsRecord.length()) {
                        c = cvsRecord.charAt(i);
                        if (c == transf) {
                            word.append(c);
                        } else if (cvsRecord.indexOf(fieldSeparator, i) == i) {
                            //} else if ( c == FieldSeparator) {
                            i += fieldSeparator.length() - 1;
                            values.add(word.toString());
                            word.delete(0, word.length());
                            inQuotation = false;
                        } else {
                            //word.append(transf);
                            inQuotation = false;
                            word.append(c);
                        }
                    }
                } else {
                    word.append(c);
                }
            }
        }
        values.add(word.toString());
        return values;

    }

    public static String firstUpcase(String inStr) {
        char firstChar = inStr.charAt(0);
        if (firstChar <= 'z' && firstChar >= 'a') {
            firstChar += ('A' - 'a');
        }
        return new StringBuilder().append(
                firstChar).append(
                inStr.substring(1)).toString();
    }

    public static String firstLowCase(String inStr) {
        char firstChar = inStr.charAt(0);
        if (firstChar <= 'Z' && firstChar >= 'A') {
            firstChar += ('a' - 'A');
        }
        return new StringBuilder().append(
                firstChar).append(
                inStr.substring(1)).toString();

    }

    /**
     * <p>
     * 测试字符串source是否能完全匹配正则表达式regex
     * </p>
     *
     * @param source 待匹配的字符串
     * @param regex  正则表达式
     * @return 匹配的结果
     */
    public static boolean matches(String source, String regex) {
        if (source == null || regex == null)
            return false;
        Pattern pattern = getPattern(regex);
        return pattern.matcher(source).matches();
    }

    public static Pattern getPattern(String regex) {
        Pattern result = map.get(regex);
        if (result == null) {
            result = Pattern.compile(regex);
            map.put(regex, result);
        }
        return result;
    }

    /*
    public static List<String> getVariableList(String str) {
        List res = new ArrayList();
        str.split("${", -2);
        return res;
    }
    
    public static void main(String[] args) {
        String str = "aa${a},dfasdf${b}sssss${cd}sdfsd${姓名}哈哈";
        List<String>res = getVariableList(str);
        for (String s : res) {
            System.out.println(s);
        }
    }
    */
    public static boolean canEncode(String str) {
        return canEncode(str, SystemUtils.systemCharset());
    }

    /**
     * 生成一个由字母组成长度为length的字符串
     *
     * @param length 字符串的长度
     * @return 生成的字符串
     */
    public static String randomAlphabetic(int length) {
        if (length <= 0)
            throw new IllegalArgumentException(length + "");
        String str = "";
        for (int i = 0; i < length; i++) {
            int index = RandomUtils.nextInt(52) + 10;
            str += source[index];
        }
        return str;
    }

    public static boolean canEncode(String str, String charset) {
        return java.nio.charset.Charset.forName(charset).newEncoder()
                .canEncode(str);
    }

    public static boolean notCanEncode(String str, String charset) {
        return !canEncode(str, charset);
    }

    public static boolean notCanEncode(String str) {
        return !canEncode(str);
    }

    public static boolean isMobile(String name) {
        return Pattern.matches(IS_REGEX_MOBILE, name);
    }

    public static boolean isEmail(String name) {
        return Pattern.matches(IS_REGEX_EMAIL, name);
    }
}
