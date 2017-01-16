/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.shenjitang.common;

/**
 *
 * @author admin
 */
public class DoubleConvert {

    
    public static void main(String[] args) {
        System.out.println("begin...");
        String str = "abcd";
        double db = toDouble(str);
        //System.out.println(db);
        String res = toString(db);
        System.out.println(res);
        System.out.println("end...");
    }

    private static double toDouble(String str) {
        long lo = 0L;
        for (int i = 0; i < str.length() && i < 4; i++) {
            long c = (long)str.charAt(i);
            c = c << 16 * i;
            lo |= c;
        }
        return Double.longBitsToDouble(lo);
    }

    private static String toString(double db) {
        long lo = Double.doubleToLongBits(db);
        char[] cs = new char[4];
        cs[0] = (char)(lo & 0xffffL);
        cs[1] = (char)((lo >> 16) & 0xffffL);
        cs[2] = (char)((lo & 0xffff00000000L) >> 32);
        cs[3] = (char)((lo & 0xffff000000000000L) >> 48);
        return new String(cs);
    }
}
