package org.shenjitang.common.util;

import java.math.BigDecimal;

/**
 * 处理浮点型数据计算的工具类
 * 
 * @author 冯炎
 * @version 1.0 2011-04-01
 */
public class ArithUtils {

	private static final int DEFAULT_DIV_SCALE = 2;

	private ArithUtils() {

	}
	
	public static int stripTrailingZeros(String value) {
		return stripTrailingZeros(Double.valueOf(value));
	}

	public static int stripTrailingZeros(double value) {
		BigDecimal b = new BigDecimal(value);
		return b.stripTrailingZeros().intValue();
	}

	public static double add(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2).doubleValue();
	}

	public static double add(double v1, double v2) {
		return add(Double.toString(v1), Double.toString(v2));
	}

	public static double subtract(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.subtract(b2).doubleValue();
	}

	public static double subtract(double v1, double v2) {
		return subtract(Double.toString(v1), Double.toString(v2));
	}

	public static double multiply(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).doubleValue();
	}

	public static double multiply(double v1, double v2) {
		return multiply(Double.toString(v1), Double.toString(v2));
	}

	public static double divide(double v1, double v2) {
		return divide(v1, v2, DEFAULT_DIV_SCALE);
	}

	public static double divide(String v1, String v2) {
		return divide(v1, v2, DEFAULT_DIV_SCALE);
	}

	public static double divide(double v1, double v2, int scale) {
		return divide(Double.toString(v1), Double.toString(v2), scale);
	}

	public static double divide(String v1, String v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero.");
		}
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static double round(double v) {
		return round(v, DEFAULT_DIV_SCALE);
	}

	public static double round(String v) {
		return round(v, DEFAULT_DIV_SCALE);
	}

	public static double round(double v, int scale) {
		return round(Double.toString(v), scale);
	}

	public static double round(String v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero.");
		}
		BigDecimal b = new BigDecimal(v);
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static void main(String[] args) {
	}
}
