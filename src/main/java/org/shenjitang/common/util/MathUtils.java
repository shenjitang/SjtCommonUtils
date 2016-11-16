package org.shenjitang.common.util;

import org.apache.commons.lang.math.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 操作数字的类，包括格式化数字，浮点数的四舍五入，浮点数的精确运算等
 * </p>
 * <p>
 * RoundingMode是舍入方式，详见Java API<br>
 * scale 精度，小数点后保留的位数，0表示整数，-1表示精确到10，-2表示精确到100<br>
 * precision 精度，有效数字的个数
 * </p>
 * 
 * @author 雷钦
 */
public class MathUtils {

	public static final int DEFAULT_SCALE = 15;

	private static Map<String, NumberFormat> map = new ConcurrentHashMap<String, NumberFormat>();

	private MathUtils() {}

	/**
	 * <p>
	 * 使用指定的模式pattern格式化整数number
	 * </p>
	 * 
	 * @param number
	 *            要格式化的整数
	 * @param pattern
	 *            模式
	 * @return 格式化后的字符串
	 */
	public static String format(long number, String pattern) {
		NumberFormat formater = getFormat(pattern);
		return formater.format(number);
	}

	/**
	 * <p>
	 * 使用指定的模式pattern格式化小数number
	 * </p>
	 * 
	 * @param number
	 *            要格式化的小数
	 * @param pattern
	 *            模式
	 * @return 格式化后的字符串
	 */
	public static String format(double number, String pattern) {
		NumberFormat formater = getFormat(pattern);
		return formater.format(number);
	}

	/**
	 * <p>
	 * 对指定的小数进行舍入，精度为scale，舍入方式为四舍五入
	 * </p>
	 * 
	 * @param number
	 *            要进行舍入的数
	 * @param scale
	 *            精度
	 * @return 舍入后的数
	 */
	public static double round(double number, int scale) {
		BigDecimal d = new BigDecimal(String.valueOf(number));
		d = d.setScale(scale, RoundingMode.HALF_UP);
		return d.doubleValue();
	}

	/**
	 * <p>
	 * 对指定的小数进行舍入，精度为scale，舍入方式为round
	 * </p>
	 * 
	 * @param number
	 *            要进行舍入的数
	 * @param scale
	 *            精度
	 * @param round
	 *            舍入方式
	 * @return 舍入后的数
	 */
	public static double round(double number, int scale, RoundingMode round) {
		BigDecimal d = new BigDecimal(String.valueOf(number));
		d = d.setScale(scale, round);
		return d.doubleValue();
	}

	/**
	 * <p>
	 * 对指定的小数进行舍入，精度为precision，舍入方式为round
	 * </p>
	 * 
	 * @param number
	 *            要进行舍入的数
	 * @param round
	 *            舍入方式
	 * @param precision
	 *            精度
	 * @return 舍入后的数
	 */
	public static double round(double number, RoundingMode round, int precision) {
		BigDecimal d = new BigDecimal(String.valueOf(number));
		MathContext context = new MathContext(precision, round);
		d = d.round(context);
		return d.doubleValue();
	}

	/**
	 * <p>
	 * 返回 num1 + num2 ，精度为 max(num1.scale,num2.scale)
	 * </p>
	 * 
	 * @param num1
	 *            被加数
	 * @param num2
	 *            加数
	 * @return 运算的结果
	 */
	public static double add(double num1, double num2) {
		BigDecimal d1 = new BigDecimal(String.valueOf(num1));
		BigDecimal d2 = new BigDecimal(String.valueOf(num2));
		BigDecimal result = d1.add(d2);
		return result.doubleValue();
	}

	/**
	 * <p>
	 * 返回 num1 + num2 ，精度为 scale，舍入方式为四舍五入
	 * </p>
	 * 
	 * @param num1
	 *            被加数
	 * @param num2
	 *            加数
	 * @param scale
	 *            精度
	 * @return 运算的结果
	 */
	public static double add(double num1, double num2, int scale) {
		BigDecimal d1 = new BigDecimal(String.valueOf(num1));
		BigDecimal d2 = new BigDecimal(String.valueOf(num2));
		BigDecimal result = d1.add(d2);
		result = result.setScale(scale, RoundingMode.HALF_UP);
		return result.doubleValue();
	}

	/**
	 * <p>
	 * 返回 num1 + num2 ，精度为 scale，舍入方式为 round
	 * </p>
	 * 
	 * @param num1
	 *            被加数
	 * @param num2
	 *            加数
	 * @param scale
	 *            精度
	 * @param round
	 *            舍入方式
	 * @return 运算的结果
	 */
	public static double add(double num1, double num2, int scale,
			RoundingMode round) {
		BigDecimal d1 = new BigDecimal(String.valueOf(num1));
		BigDecimal d2 = new BigDecimal(String.valueOf(num2));
		BigDecimal result = d1.add(d2);
		result = result.setScale(scale, round);
		return result.doubleValue();
	}

	/**
	 * <p>
	 * 返回 num1 + num2 ，精度为 precision，舍入方式为 round
	 * </p>
	 * 
	 * @param num1
	 *            被加数
	 * @param num2
	 *            加数
	 * @param round
	 *            舍入方式
	 * @param precision
	 *            精度
	 * @return 运算的结果
	 */
	public static double add(double num1, double num2, RoundingMode round,
			int precision) {
		BigDecimal d1 = new BigDecimal(String.valueOf(num1));
		BigDecimal d2 = new BigDecimal(String.valueOf(num2));
		MathContext context = new MathContext(precision, round);
		BigDecimal result = d1.add(d2, context);
		return result.doubleValue();
	}

	/**
	 * <p>
	 * 返回 num1 - num2 ，精度为 max(num1.scale,num2.scale)
	 * </p>
	 * 
	 * @param num1
	 *            被减数
	 * @param num2
	 *            减数
	 * @return 运算的结果
	 */
	public static double subtract(double num1, double num2) {
		BigDecimal d1 = new BigDecimal(String.valueOf(num1));
		BigDecimal d2 = new BigDecimal(String.valueOf(num2));
		BigDecimal result = d1.subtract(d2);
		return result.doubleValue();
	}

	/**
	 * <p>
	 * 返回 num1 - num2 ，精度为 scale，舍入方式为四舍五入
	 * </p>
	 * 
	 * @param num1
	 *            被减数
	 * @param num2
	 *            减数
	 * @param scale
	 *            精度
	 * @return 运算的结果
	 */
	public static double subtract(double num1, double num2, int scale) {
		BigDecimal d1 = new BigDecimal(String.valueOf(num1));
		BigDecimal d2 = new BigDecimal(String.valueOf(num2));
		BigDecimal result = d1.subtract(d2);
		result = result.setScale(scale, RoundingMode.HALF_UP);
		return result.doubleValue();
	}

	/**
	 * <p>
	 * 返回 num1 - num2 ，精度为 scale，舍入方式为 round
	 * </p>
	 * 
	 * @param num1
	 *            被减数
	 * @param num2
	 *            减数
	 * @param scale
	 *            精度
	 * @param round
	 *            舍入方式
	 * @return 运算的结果
	 */
	public static double subtract(double num1, double num2, int scale,
			RoundingMode round) {
		BigDecimal d1 = new BigDecimal(String.valueOf(num1));
		BigDecimal d2 = new BigDecimal(String.valueOf(num2));
		BigDecimal result = d1.subtract(d2);
		result = result.setScale(scale, round);
		return result.doubleValue();
	}

	/**
	 * <p>
	 * 返回 num1 - num2 ，精度为 precision，舍入方式为 round
	 * </p>
	 * 
	 * @param num1
	 *            被减数
	 * @param num2
	 *            减数
	 * @param round
	 *            舍入方式
	 * @param precision
	 *            精度
	 * @return 运算的结果
	 */
	public static double subtract(double num1, double num2, RoundingMode round,
			int precision) {
		BigDecimal d1 = new BigDecimal(String.valueOf(num1));
		BigDecimal d2 = new BigDecimal(String.valueOf(num2));
		MathContext context = new MathContext(precision, round);
		BigDecimal result = d1.subtract(d2, context);
		return result.doubleValue();
	}

	/**
	 * <p>
	 * 返回 num1 * num2 ，精度为 num1.scale + num2.scale
	 * </p>
	 * 
	 * @param num1
	 *            被乘数
	 * @param num2
	 *            乘数
	 * @return 运算的结果
	 */
	public static double multiply(double num1, double num2) {
		BigDecimal d1 = new BigDecimal(String.valueOf(num1));
		BigDecimal d2 = new BigDecimal(String.valueOf(num2));
		BigDecimal result = d1.multiply(d2);
		return result.doubleValue();
	}

	/**
	 * <p>
	 * 返回 num1 * num2 ，精度为 scale，舍入方式为四舍五入
	 * </p>
	 * 
	 * @param num1
	 *            被乘数
	 * @param num2
	 *            乘数
	 * @param scale
	 *            精度
	 * @return 运算的结果
	 */
	public static double multiply(double num1, double num2, int scale) {
		BigDecimal d1 = new BigDecimal(String.valueOf(num1));
		BigDecimal d2 = new BigDecimal(String.valueOf(num2));
		BigDecimal result = d1.multiply(d2);
		result = result.setScale(scale, RoundingMode.HALF_UP);
		return result.doubleValue();
	}

	/**
	 * <p>
	 * 返回 num1 * num2 ，精度为 scale，舍入方式为 round
	 * </p>
	 * 
	 * @param num1
	 *            被乘数
	 * @param num2
	 *            乘数
	 * @param scale
	 *            精度
	 * @param round
	 *            舍入方式
	 * @return 运算的结果
	 */
	public static double multiply(double num1, double num2, int scale,
			RoundingMode round) {
		BigDecimal d1 = new BigDecimal(String.valueOf(num1));
		BigDecimal d2 = new BigDecimal(String.valueOf(num2));
		BigDecimal result = d1.multiply(d2);
		result = result.setScale(scale, round);
		return result.doubleValue();
	}

	/**
	 * <p>
	 * 返回 num1 * num2 ，精度为 precision，舍入方式为 round
	 * </p>
	 * 
	 * @param num1
	 *            被乘数
	 * @param num2
	 *            乘数
	 * @param round
	 *            舍入方式
	 * @param precision
	 *            精度
	 * @return 运算的结果
	 */
	public static double multiply(double num1, double num2, RoundingMode round,
			int precision) {
		BigDecimal d1 = new BigDecimal(String.valueOf(num1));
		BigDecimal d2 = new BigDecimal(String.valueOf(num2));
		MathContext context = new MathContext(precision, round);
		BigDecimal result = d1.multiply(d2, context);
		return result.doubleValue();
	}

	/**
	 * <p>
	 * 返回 num1 / num2 ，精度为 num1.scale，舍入方式为四舍五入
	 * </p>
	 * 
	 * @param num1
	 *            被除数
	 * @param num2
	 *            除数
	 * @return 运算的结果
	 */
	public static double divide(double num1, double num2) {
		BigDecimal d1 = new BigDecimal(String.valueOf(num1));
		BigDecimal d2 = new BigDecimal(String.valueOf(num2));
		BigDecimal result = d1.divide(d2, RoundingMode.HALF_UP);
		return result.doubleValue();
	}

	/**
	 * <p>
	 * 返回 num1 / num2 ，精度为 scale，舍入方式为四舍五入
	 * </p>
	 * 
	 * @param num1
	 *            被除数
	 * @param num2
	 *            除数
	 * @param scale
	 *            精度
	 * @return 运算的结果
	 */
	public static double divide(double num1, double num2, int scale) {
		BigDecimal d1 = new BigDecimal(String.valueOf(num1));
		BigDecimal d2 = new BigDecimal(String.valueOf(num2));
		BigDecimal result = d1.divide(d2, scale, RoundingMode.HALF_UP);
		return result.doubleValue();
	}

	/**
	 * <p>
	 * 返回 num1 / num2 ，精度为 scale，舍入方式为 round
	 * </p>
	 * 
	 * @param num1
	 *            被除数
	 * @param num2
	 *            除数
	 * @param scale
	 *            精度
	 * @param round
	 *            舍入方式
	 * @return 运算的结果
	 */
	public static double divide(double num1, double num2, int scale,
			RoundingMode round) {
		BigDecimal d1 = new BigDecimal(String.valueOf(num1));
		BigDecimal d2 = new BigDecimal(String.valueOf(num2));
		BigDecimal result = d1.divide(d2, scale, round);
		return result.doubleValue();
	}

	/**
	 * <p>
	 * 返回 num1 / num2 ，精度为 precision，舍入方式为 round
	 * </p>
	 * 
	 * @param num1
	 *            被除数
	 * @param num2
	 *            除数
	 * @param round
	 *            舍入方式
	 * @param precision
	 *            精度
	 * @return 运算的结果
	 */
	public static double divide(double num1, double num2, RoundingMode round,
			int precision) {
		BigDecimal d1 = new BigDecimal(String.valueOf(num1));
		BigDecimal d2 = new BigDecimal(String.valueOf(num2));
		MathContext context = new MathContext(precision, round);
		BigDecimal result = d1.divide(d2, context);
		return result.doubleValue();
	}

	/**
	 * <p>
	 * 返回 num 的 n 次幂，精度为 num.scale*n
	 * </p>
	 * 
	 * @param num
	 *            底数
	 * @param n
	 *            幂
	 * @return 运算的结果
	 */
	public static double pow(double num, int n) {
		BigDecimal d = new BigDecimal(String.valueOf(num));
		BigDecimal result = d.pow(n);
		return result.doubleValue();
	}

	/**
	 * <p>
	 * 返回 num 的 n 次幂，精度为 scale，舍入方式为四舍五入
	 * </p>
	 * 
	 * @param num
	 *            底数
	 * @param n
	 *            幂
	 * @param scale
	 *            精度
	 * @return 运算的结果
	 */
	public static double pow(double num, int n, int scale) {
		BigDecimal d = new BigDecimal(String.valueOf(num));
		BigDecimal result = d.pow(n);
		result = result.setScale(scale, RoundingMode.HALF_UP);
		return result.doubleValue();
	}

	/**
	 * <p>
	 * 返回 num 的 n 次幂，精度为 scale，舍入方式为 round
	 * </p>
	 * 
	 * @param num
	 *            底数
	 * @param n
	 *            幂
	 * @param scale
	 *            精度
	 * @param round
	 *            舍入方式
	 * @return 运算的结果
	 */
	public static double pow(double num, int n, int scale, RoundingMode round) {
		BigDecimal d = new BigDecimal(String.valueOf(num));
		BigDecimal result = d.pow(n);
		result = result.setScale(scale, round);
		return result.doubleValue();
	}

	/**
	 * <p>
	 * 返回 num 的 n 次幂，精度为 precision，舍入方式为 round
	 * </p>
	 * 
	 * @param num
	 *            底数
	 * @param n
	 *            幂
	 * @param round
	 *            舍入方式
	 * @param precision
	 *            精度
	 * @return 运算的结果
	 */
	public static double pow(double num, int n, RoundingMode round,
			int precision) {
		BigDecimal d = new BigDecimal(String.valueOf(num));
		MathContext context = new MathContext(precision, round);
		BigDecimal result = d.pow(n, context);
		return result.doubleValue();
	}

	/**
	 * <p>
	 * 返回以自然对数e为底的value的对数
	 * </p>
	 * 
	 * @param value
	 *            一个值
	 * @return 计算的结果
	 */
	public static double ln(double value) {
		return Math.log(value);
	}

	/**
	 * <p>
	 * 返回以base为底的value的对数
	 * </p>
	 * 
	 * @param value
	 *            一个值
	 * @param base
	 *            底数
	 * @return 计算的结果
	 */
	public static double log(double value, double base) {
		return Math.log(value) / Math.log(base);
	}

	/**
	 * <p>
	 * 返回以2为底的value的对数
	 * </p>
	 * 
	 * @param value
	 *            一个值
	 * @return 计算的结果
	 */
	public static double log2(double value) {
		return Math.log(value) / Math.log(2);
	}

	/**
	 * <p>
	 * 返回以10为底的value的对数
	 * </p>
	 * 
	 * @param value
	 *            一个值
	 * @return 计算的结果
	 */
	public static double log10(double value) {
		return Math.log10(value);
	}

	/**
	 * <p>
	 * 将参数 value 沿绝对值增大的方向向上舍入，使其等于最接近的 significance 的倍数<br>
	 * value 与 significance 的符号必须一致
	 * </p>
	 * 
	 * @param value
	 *            一个值
	 * @param significance
	 *            另一个值
	 * @return 计算的结果
	 */
	public static double ceiling(double value, double significance) {
		if (value == 0 || significance == 0)
			return 0;
		if (value < 0 && significance > 0)
			throw new ArithmeticException("value 与 significance 的符号必须一致");
		if (value > 0 && significance < 0)
			throw new ArithmeticException("value 与 significance 的符号必须一致");
		int temp = (int) (value / significance);
		if (value % significance != 0)
			temp++;
		return temp * significance;
	}

	/**
	 * <p>
	 * 将参数 value 沿绝对值减小的方向向下舍入，使其等于最接近的 significance 的倍数<br>
	 * value 与 significance 的符号必须一致且不为0
	 * </p>
	 * 
	 * @param value
	 *            一个值
	 * @param significance
	 *            另一个值
	 * @return 计算的结果
	 */
	public static double floor(double value, double significance) {
		if (value == 0)
			return 0;
		if (significance == 0)
			throw new ArithmeticException("DIV 0");
		if (value < 0 && significance > 0)
			throw new ArithmeticException("value 与 significance 的符号必须一致");
		if (value > 0 && significance < 0)
			throw new ArithmeticException("value 与 significance 的符号必须一致");
		int temp = (int) (value / significance);
		return temp * significance;
	}

	/**
	 * <p>
	 * 返回fact的阶乘
	 * </p>
	 * 
	 * @param fact
	 *            值
	 * @return 计算的结果
	 */
	public static BigInteger factorial(int fact) {
		if (fact < 0)
			throw new ArithmeticException("fact 不能小于0");
		if (fact == 0)
			return BigInteger.ONE;
		BigInteger i = new BigInteger(String.valueOf(fact));
		BigInteger result = i;
		i = i.subtract(BigInteger.ONE);
		while (!BigInteger.ZERO.equals(i)) {
			result = result.multiply(i);
			i = i.subtract(BigInteger.ONE);
		}
		return result;
	}

	/**
	 * <p>
	 * 返回fact的半阶乘
	 * </p>
	 * 
	 * @param fact
	 *            值
	 * @return 计算的结果
	 */
	public static BigInteger factorialDouble(int fact) {
		if (fact < 0)
			throw new ArithmeticException("fact 不能小于0");
		if (fact == 0)
			return BigInteger.ONE;
		BigInteger i = new BigInteger(String.valueOf(fact));
		BigInteger result = i;
		i = i.subtract(BigInteger.ONE);
		i = i.subtract(BigInteger.ONE);
		while (BigInteger.ONE.compareTo(i) < 0) {
			result = result.multiply(i);
			i = i.subtract(BigInteger.ONE);
			i = i.subtract(BigInteger.ONE);
		}
		return result;
	}

	/**
	 * <p>
	 * 反双曲正弦函数ln(x+sqrt(x*x+1))
	 * </p>
	 * 
	 * @param value
	 *            值
	 * @return 计算的结果
	 */
	public static double asinh(double value) {
		return Math.log(value + Math.sqrt(value * value + 1));
	}

	/**
	 * <p>
	 * 反双曲余弦函数ln(x+sqrt(x*x-1))
	 * </p>
	 * 
	 * @param value
	 *            值
	 * @return 计算的结果
	 */
	public static double acosh(double value) {
		if (value <= 1)
			throw new IllegalArgumentException("value 必须大于 1");
		return Math.log(value + Math.sqrt(value * value - 1));
	}

	/**
	 * <p>
	 * 反双曲正切函数(1/2)*ln((1+x)/(1-x))
	 * </p>
	 * 
	 * @param value
	 *            值
	 * @return 计算的结果
	 */
	public static double atanh(double value) {
		if (value <= -1 || value >= 1)
			throw new IllegalArgumentException("value 必须在 (-1,1) 之间");
		return 0.5 * Math.log((1 + value) / (1 - value));
	}

	/**
	 * <p>
	 * 最大公约数
	 * </p>
	 * 
	 * @param nums
	 * @return
	 */
	public static int gcd(List<Integer> numbers) {
		if (numbers.size() == 0)
			throw new IllegalArgumentException();
		List<Integer> nums = new ArrayList<Integer>(numbers);
		Collections.sort(nums);
		for (int i = 0; i < nums.size(); i++) {
			int temp = nums.get(i);
			if (temp < 0)
				throw new IllegalArgumentException("参数不能小于 0");
			if (temp == 0) {
				nums.remove(i);
				i--;
			}
		}
		if (nums.size() == 0)
			return 0;
		int result = nums.get(0);
		while (result != 1) {
			boolean flag = true;
			for (int num : nums) {
				if (num % result != 0) {
					flag = false;
					break;
				}
			}
			if (flag)
				break;
			result--;
		}
		return result;
	}

	/**
	 * <p>
	 * 最小公倍数
	 * </p>
	 * 
	 * @param nums
	 * @return
	 */
	public static int lcm(List<Integer> numbers) {
		if (numbers.size() == 0)
			throw new IllegalArgumentException();
		List<Integer> nums = new ArrayList<Integer>(numbers);
		Collections.sort(nums);
		for (int i = 0; i < nums.size(); i++) {
			int temp = nums.get(i);
			if (temp < 0)
				throw new IllegalArgumentException("参数不能小于 0");
			if (temp == 0)
				return 0;
		}
		int result = nums.get(nums.size() - 1);
		while (true) {
			boolean flag = true;
			for (int num : nums) {
				if (result % num != 0) {
					flag = false;
					break;
				}
			}
			if (flag)
				break;
			result++;
		}
		return result;
	}

	/**
	 * <p>
	 * 返回一组数据与其均值的绝对偏差的平均值
	 * </p>
	 * 
	 * @param nums
	 * @return
	 */
	public static BigDecimal avedev(List<BigDecimal> nums) {
		return avedev(nums, DEFAULT_SCALE);
	}

	/**
	 * <p>
	 * 返回一组数据与其均值的绝对偏差的平均值
	 * </p>
	 * 
	 * @param nums
	 * @param scale
	 * @return
	 */
	public static BigDecimal avedev(List<BigDecimal> nums, int scale) {
		if (nums.size() == 0)
			return BigDecimal.ZERO;
		BigDecimal avg = average(nums, scale);
		BigDecimal sum = BigDecimal.ZERO;
		for (BigDecimal num : nums) {
			BigDecimal temp = num.subtract(avg);
			temp = temp.abs();
			sum = sum.add(temp);
		}
		return sum.divide(BigDecimal.valueOf(nums.size()), scale,
				RoundingMode.HALF_UP).stripTrailingZeros();
	}

	/**
	 * <p>
	 * 返回一组数据与其均值的绝对偏差的平均值
	 * </p>
	 * 
	 * @param nums
	 * @return
	 */
	public static double avedevDouble(List<Double> nums) {
		if (nums.size() == 0)
			return 0d;
		double avg = averageDouble(nums);
		double sum = 0d;
		for (double num : nums) {
			double temp = num - avg;
			temp = Math.abs(temp);
			sum += temp;
		}
		return sum / nums.size();
	}

	/**
	 * <p>
	 * 平均数
	 * </p>
	 * 
	 * @param nums
	 * @return
	 */
	public static BigDecimal average(List<BigDecimal> nums) {
		return average(nums, DEFAULT_SCALE);
	}

	/**
	 * <p>
	 * 平均数
	 * </p>
	 * 
	 * @param nums
	 * @param scale
	 * @return
	 */
	public static BigDecimal average(List<BigDecimal> nums, int scale) {
		if (nums.size() == 0)
			return BigDecimal.ZERO;
		BigDecimal sum = BigDecimal.ZERO;
		for (BigDecimal num : nums) {
			sum = sum.add(num);
		}
		return sum.divide(BigDecimal.valueOf(nums.size()), scale,
				RoundingMode.HALF_UP).stripTrailingZeros();
	}

	/**
	 * <p>
	 * 平均数
	 * </p>
	 * 
	 * @param nums
	 * @param scale
	 * @return
	 */
	public static double averageDouble(List<Double> nums) {
		if (nums.size() == 0)
			return 0d;
		double sum = 0d;
		for (double num : nums) {
			sum += num;
		}
		return sum / nums.size();
	}
	
	

	/**
	 * <p>
	 * 矩阵行列式
	 * </p>
	 * 
	 * @param matrix
	 * @return
	 */
	public static BigDecimal mdeterm(BigDecimal[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			if (matrix.length != matrix[i].length)
				throw new IllegalArgumentException("必须为方阵");
		}
		int[] row = new int[matrix.length];
		int[] col = new int[matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			row[i] = i;
			col[i] = i;
		}
		return mdeterm(matrix, row, col);
	}

	private static BigDecimal mdeterm(BigDecimal[][] matrix, int[] row,
			int[] col) {
		if (row.length == 1) {
			return matrix[row[0]][col[0]];
		}
		BigDecimal result = BigDecimal.ZERO;
		for (int i = 0; i < row.length; i++) {
			int[] sub_row = new int[row.length - 1];
			int[] sub_col = new int[col.length - 1];
			for (int j = 0, row_index = 0, col_index = 0; j < sub_col.length; j++, row_index++, col_index++) {
				sub_row[j] = row[row_index + 1];

				if (col_index == i)
					col_index++;
				sub_col[j] = col[col_index];
			}
			BigDecimal temp = matrix[row[0]][col[i]].multiply(mdeterm(matrix,
					sub_row, sub_col));
			if ((i % 2) != 0)
				temp = temp.negate();
			result = result.add(temp);
		}
		return result;
	}

	/**
	 * <p>
	 * 判断一个字符串是否表示一个数字，使用common-lang里的NumberUtils.isNumber()<br>
	 * </p>
	 * 
	 * @param str
	 *            要判断的字符串
	 * @return 判断的结果
	 */
	public static boolean isNumber(String str) {
		return NumberUtils.isNumber(str);
	}

	/**
	 * <p>
	 * 返回与isNumber()相反的值
	 * </p>
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotNumber(String str) {
		return !isNumber(str);
	}

	private static NumberFormat getFormat(String pattern) {
		NumberFormat result = map.get(pattern);
		if (result == null) {
			result = new DecimalFormat(pattern);
			map.put(pattern, result);
		}
		return result;
	}

}
