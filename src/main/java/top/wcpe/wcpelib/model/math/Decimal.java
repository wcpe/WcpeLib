package top.wcpe.wcpelib.model.math;

import java.text.DecimalFormat;

public class Decimal {
	private static final DecimalFormat df = new DecimalFormat("#.00");

	/**
	 * 舍去小数保留两位
	 * 
	 * @param db
	 * @return
	 * @author WCPE
	 * @date 2021年4月5日 下午4:36:20
	 */
	public static Double decimalFormat(double db) {
		return Double.parseDouble(df.format(db));
	}
}
