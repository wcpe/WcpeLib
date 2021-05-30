package top.wcpe.wcpelib.common.utils.math;

import java.util.Random;

/**
 * 随机数
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-05-24 12:07
 */
public class RandomUtil {
    private final static Random random = new Random();

    /**
     * 生成 [min, max] 区间随机值
     * @param min
     * @param max
     * @return int
     */
    public static int nextRandom(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
