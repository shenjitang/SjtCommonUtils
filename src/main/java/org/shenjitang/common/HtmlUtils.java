/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.shenjitang.common;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class HtmlUtils {
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
