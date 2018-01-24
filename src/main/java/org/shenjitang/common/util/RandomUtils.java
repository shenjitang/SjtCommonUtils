package org.shenjitang.common.util;

import java.util.Random;

/**
 * <p>
 * 该类用于生成伪随机数，可生成布尔值、整数、小数，每个线程使用单独的种子
 * </p>
 *
 * @author 雷钦
 */
public class RandomUtils {

    private static ThreadLocal<Random> randoms = new ThreadLocal<Random>() {

        @Override
        protected Random initialValue() {
            return new Random();
        }
    };

    private RandomUtils() {
    }

    /**
     * <p>
     * 设置该线程的伪随机数生成器的种子
     * </p>
     *
     * @param seed 随机数生成器的种子
     */
    public static void setSeed(long seed) {
        randoms.get().setSeed(seed);
    }

    /**
     * <p>
     * 生成一个随机的布尔值
     * </p>
     *
     * @return 随机的布尔值
     */
    public static boolean nextBoolean() {
        return randoms.get().nextBoolean();
    }

    /**
     * <p>
     * 生成一个随机的小数 <code>[0,1)</code>
     * </p>
     *
     * @return 随机的小数
     */
    public static float nextFloat() {
        return randoms.get().nextFloat();
    }

    /**
     * <p>
     * 生成一个随机的小数 <code>[0,1)</code>
     * </p>
     *
     * @return 随机的小数
     */
    public static double nextDouble() {
        return randoms.get().nextDouble();
    }

    /**
     * <p>
     * 生成一个随机的正整数
     * </p>
     *
     * @return 随机的正整数
     */
    public static int nextInt() {
        return randoms.get().nextInt();
    }

    /**
     * <p>
     * 生成一个随机的正整数 <code>[0,n)</code>
     * </p>
     *
     * @return 随机的正整数
     */
    public static int nextInt(int n) {
        if (n <= 0)
            throw new IllegalArgumentException(n + "");
        return randoms.get().nextInt(n);
    }

    /**
     * <p>
     * 生成一个随机的正整数
     * </p>
     *
     * @return 随机的正整数
     */
    public static long nextLong() {
        return randoms.get().nextLong();
    }

    /**
     * <p>
     * 生成随机字节并将其置于用户提供的字节数组中
     * </p>
     *
     * @param bytes 放入随机字节的字节数组
     */
    public static void nextBytes(byte[] bytes) {
        randoms.get().nextBytes(bytes);
    }

    /**
     * <p>
     * 生成一个随机的 double 值，其平均值是 0.0，标准偏差是 1.0。
     * </p>
     *
     * @return 随机的 double 值
     */
    public static double nextGaussian() {
        return randoms.get().nextGaussian();
    }

    /**
     * <p>
     * 返回范围[floor,ceiling)内的整数
     * </p>
     *
     * @param floor   下限，包含
     * @param ceiling 上限，不包含
     * @return 随机的 int 值
     */
    public static int randomBetween(int floor, int ceiling) {
        if (floor >= ceiling) {
            throw new IllegalArgumentException("floor: " + floor
                    + " 大于或等于 ceiling:" + ceiling);
        }
        return floor + nextInt(ceiling - floor);
    }

    /**
     * <p>
     * 返回范围[floor,ceiling)内的小数
     * </p>
     *
     * @param floor   下限，包含
     * @param ceiling 上限，不包含
     * @return 随机的 double 值
     */
    public static double randomBetween(double floor, double ceiling) {
        if (floor >= ceiling) {
            throw new IllegalArgumentException("floor: " + floor
                    + " 大于或等于 ceiling:" + ceiling);
        }
        return floor + (ceiling - floor) * nextDouble();
    }
}
