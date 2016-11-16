/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shenjitang.common;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author admin
 */
public class AliasMng {

    private Map<String, String> keyAliasMap;
    private Map<String, String> aliasKeyMap;
    private String mapFile;

    public String getMapFile() {
        return mapFile;
    }

    public void setMapFile(String mapFile) throws IOException {
        this.mapFile = mapFile;
        keyAliasMap = new HashMap();
        aliasKeyMap = new HashMap();
        List<String> lines = FileUtils.readLines(new File(mapFile), "UTF8");
        for (String line : lines) {
            String[] kv = line.split(":");
            if (kv.length == 2) {
                keyAliasMap.put(kv[0], kv[1]);
                aliasKeyMap.put(kv[1], kv[0]);
            }
        }
    }

    public Map<String, String> getAliasMap() {
        return keyAliasMap;
    }

    public void setAliasMap(Map<String, String> aliasMap) {
        this.keyAliasMap = aliasMap;
    }

    public String jsonConvert(String inStr) {
        return convert(inStr, keyAliasMap);
    }

    public String jsonRestore(String inStr) {
        return convert(inStr, aliasKeyMap);
    }
    
    public String xmlConvert(String inStr) {
        return xConvert(inStr, keyAliasMap);
    }
    
    public String xmlRestore(String inStr) {
        return xConvert(inStr, aliasKeyMap);
    }
    
    private String convert(String inStr, Map map) {
        StringBuilder sb = new StringBuilder(inStr.length());
        StringBuilder keySb = new StringBuilder(100);
        boolean inKey = false;
        boolean inValue = false;
        char preChar = 0;
        for (int i = 0; i < inStr.length(); i++) {
            char c = inStr.charAt(i);
            if (c == '"' && preChar != '\\') {  //key value边界判断
                if (inValue) { // value end
                    sb.append(c);
                    inValue = false;
                } else {
                    if (inKey) { //key end
                        appendKey(c, keySb, sb, map);
                        sb.append(c);
                        inKey = false;
                    } else {
                        if (preChar == ':') { // value begin
                            inValue = true;
                            inKey = false;
                        } else { //key begin
                            inKey = true;
                            inValue = false;
                        //keySb.delete(0, keySb.length());
                        }
                        sb.append(c);
                    }
                }
            } else {
                if (inKey) {
                    keySb.append(c);
                } else {
                    sb.append(c);
                }
            }
            preChar = c;
        }
        return sb.toString();
    }
    
    private void appendKey (char c, StringBuilder keySb, StringBuilder sb, Map map) {
        String key = keySb.toString();
        if (map.containsKey(key)) {
            sb.append(map.get(key));
        } else {
            sb.append(key);
        }
        keySb.delete(0, keySb.length());
    }

    private String xConvert(String inStr, Map map) {
        StringBuilder sb = new StringBuilder(inStr.length());
        StringBuilder keySb = new StringBuilder(100);
        boolean inTag = false;
        boolean inKey = false;
        boolean inValue = false;
        char preChar = 0;
        for (int i = 0; i < inStr.length(); i++) {
            char c = inStr.charAt(i);
            if (inTag) {
                if (inValue) {
                    sb.append(c);
                    if (c == '"' && preChar != '\\') {  //end value
                        inValue = false;
                    }
                } else { // not in vlaue
                    if (c == '/' && preChar == '<' && inKey) {
                        sb.append(c);
                    } else if (c == '\"') { //begin value
                        inValue = true;
                        sb.append(c);
                    } else if (c == '>') { //end tag
                        if (inKey) { // end key
                            inKey = false;
                            appendKey(c, keySb, sb, map);
                        }
                        inTag = false;
                        sb.append(c);
                    } else if (c == ' ') {
                        if (inKey) {
                            appendKey(c, keySb, sb, map);
                        } else {
                            inKey = true;
                        }
                        sb.append(c);
                    } else if (c == '=') {
                        if (inKey) { // end key
                            inKey = false;
                            appendKey(c, keySb, sb, map);
                        }
                        sb.append(c);
                    } else { // 非分割字符
                        if (inKey) {
                            keySb.append(c);
                        } else {
                            sb.append(c);
                        }
                    }
                }
            } else { //not in tag
                if (c == '<') { //begin tag
                    inTag = true;
                    inKey = true;
                }
                sb.append(c);
            }
            preChar = c;
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        String jsonStr = FileUtils.readFileToString(new File("D:\\Workspace\\anaplatform\\temp\\protocolSample.txt"), "UTF8");
        System.out.println(jsonStr);
        AliasMng mng = new AliasMng();
        mng.setMapFile("D:\\Workspace\\anaplatform\\web\\WEB-INF\\alias.txt");
        String resStr = mng.jsonConvert(jsonStr);
        System.out.println(resStr);
        resStr = mng.jsonRestore(resStr);
        System.out.println(resStr);
        System.out.println(resStr.equals(jsonStr) ? "success" : "faile");
        String xmlStr =  FileUtils.readFileToString(new File("D:\\Workspace\\anaplatform\\temp\\xmlTest.txt"), "UTF8");
        System.out.println("--------source-------------");
        System.out.println(xmlStr);
        //AliasMng mng = new AliasMng();
        mng.setMapFile("D:\\Workspace\\anaplatform\\web\\WEB-INF\\xAlias.txt");
        resStr = mng.xmlConvert(xmlStr);
        System.out.println("--------convert-------------");
        System.out.println(resStr);
        resStr = mng.xmlRestore(resStr);
        System.out.println("--------restore-------------");
        System.out.println(resStr);
        System.out.println(resStr.equals(xmlStr) ? "success" : "faile");
    }
}
