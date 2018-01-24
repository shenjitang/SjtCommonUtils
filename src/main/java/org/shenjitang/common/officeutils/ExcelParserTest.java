/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shenjitang.common.officeutils;

import java.io.File;
import java.util.Map;

/**
 *
 * @author xiaolie
 */
public class ExcelParserTest {
    public static void main(String[] args) throws Exception {
        Map<String, String[][]> map = ExcelParser.parseExcelInMem(new File("D:\\Work\\smartindex\\excel\\P020170921312419186953.xlsx"), new String[] {"Sheet1"});
        print(map);
    }
    public static void print(Map<String, String[][]> map) {
        for (String sheet: map.keySet()) {
            System.out.println("=============" + sheet + "================");
            String[][] table = map.get(sheet);
            for (int i = 0; i < table.length; i++) {
                for (int j = 0; j < table[i].length; j++) {
                    System.out.print(table[i][j] + "|");
                }
                System.out.println();
            }
        }
    }
}
