package top.wcpe.wcpelib.common.utils.math;

import java.util.*;

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
     *
     * @param min
     * @param max
     * @return int
     */
    public static int nextRandom(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * 生成 [min, max] 区间随机值
     * @param min
     * @param max
     * @return
     */
    public static double nextRandom(double min, double max) {
        return min + Math.random() * max;
    }

    /**
     * 区间定位随机返回 List 中一个索引值
     *
     * @param mapList
     * @return int
     */
    public static int randomIndexToListChance(List<Double> mapList) {
        List<Double> totalChance = new ArrayList<>(mapList.size());
        for (Double aDouble : mapList) {
            totalChance.add(aDouble < 0d ? 0d : aDouble);
        }
        if (totalChance == null || totalChance.isEmpty()) {
            return -1;
        }
        int size = totalChance.size();
        double sumRate = 0d;
        for (double rate : totalChance) {
            sumRate += rate;
        }
        List<Double> sortOriginalRates = new ArrayList<>(size);
        Double tempSumRate = 0d;
        for (double rate : totalChance) {
            tempSumRate += rate;
            sortOriginalRates.add(tempSumRate / sumRate);
        }
        double nextDouble = Math.random();
        sortOriginalRates.add(nextDouble);
        Collections.sort(sortOriginalRates);
        return sortOriginalRates.indexOf(nextDouble);
    }

    /**
     * 判断概率
     *
     * @param chance
     * @return
     * @author WCPE
     * @date 2021年4月5日 下午2:48:10
     */
    public static boolean probability(double chance) {
        return chance > 0 && chance / 100D > random.nextDouble();
    }

}
