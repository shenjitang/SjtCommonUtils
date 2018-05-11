package org.shenjitang.common.util;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Iterator;
import java.util.List;

public class XmlUtils {

    private static XmlUtils xmlTools;

    private XmlUtils() {

    }

    public static XmlUtils getXmlTools() {
        if (xmlTools == null) {
            xmlTools = new XmlUtils();
        }
        return xmlTools;
    }

    public String getTagValue(String xml, String locateTagName, String locateTagValue, String tagName) throws Exception {
        Document doc = DocumentHelper.parseText(xml);
        Element elem = doc.getRootElement();
        StringBuffer sb = new StringBuffer();
        getTagValue(elem, locateTagName, locateTagValue, tagName, sb);
        return sb.toString();
    }

    public String getTagValue(String xml, String locateTagName) throws Exception {
        Document doc = DocumentHelper.parseText(xml);
        Element elem = doc.getRootElement();
        StringBuffer sb = new StringBuffer();
        getTagValue(elem, locateTagName, sb);
        return sb.toString();
    }

    /**
     * 递归遍历方法
     *
     * @param element
     */
    @SuppressWarnings("unchecked")
    public void getTagValue(Element element, String locateTagName, String locateTagValue, String tagName, StringBuffer result) {
        if (!"".equals(result.toString())) {
            return;
        }
        List elements = element.elements();
        if (elements.size() == 0) {
            if (element.getName().contains(locateTagName) && element.getTextTrim().contains(locateTagValue)) {
                result.append(element.getParent().element(tagName)
                        .getTextTrim());
            }
        } else {
            // 有子元素
            for (Iterator it = elements.iterator(); it.hasNext(); ) {
                Element elem = (Element) it.next();
                if (element.getName().contains(locateTagName) && elem.getTextTrim().contains(locateTagValue)) {
                    result.append(elem.element(tagName).getTextTrim());
                }
                getTagValue(elem, locateTagName, locateTagValue, tagName,
                        result);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void getTagValue(Element element, String locateTagName, StringBuffer result) {
        System.out.println(element.getName());
        if (locateTagName.equals(element.getName())) {
            System.out.println("****************************" + element.getStringValue());
            result.append(element.getTextTrim());
        }
        if (!"".equals(result.toString())) {
            return;
        }
        List elements = element.elements();
        if (elements.size() == 0) {
        } else {
            // 有子元素
            for (Iterator it = elements.iterator(); it.hasNext(); ) {
                Element elem = (Element) it.next();
                getTagValue(elem, locateTagName, result);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        XmlUtils tools = XmlUtils.getXmlTools();
        tools.getTagValue("", "", "", "");
    }

}
