package top.wcpe.wcpelib.common.utils.math;

import java.text.DecimalFormat;

public class Decimal {

    /**
     * 舍去小数保留两位
     *
     * @param db
     * @return
     * @author WCPE
     * @date 2021年4月5日 下午4:36:20
     */
    public static Double decimalFormat(double db) {
        return decimalFormat(db, 2);
    }

    /**
     * 保留几位小数
     *
     * @param db
     * @param i
     * @return {@link Double}
     */
    public static Double decimalFormat(double db, int i) {
        StringBuilder s = new StringBuilder("#.");
        for (; i > 0; i--) {
            s.append("0");
        }
        return Double.parseDouble(new DecimalFormat(s.toString()).format(db));
    }
}
