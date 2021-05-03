package top.wcpe.wcpelib.common.utils.datatime;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 其他时间工具
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-05-03 17:24
 */
public class OtherUtil {
    private static Timer timer = new Timer();

    public static void putCountDownTask(CountDownTask task, int m) {
        long time = System.currentTimeMillis() + m * 1000;
        timer.schedule(new TimerTask() {
            int times = m;

            @Override
            public void run() {
                if (System.currentTimeMillis() > time) {
                    cancel();
                }
                task.run(times--);

            }
        }, 0, 1000);
    }

    @FunctionalInterface
    public interface CountDownTask {
        void run(int time);
    }

    public static HashMap<String, Long> coolTimeMap = new HashMap<>();

    public static void putCoolTime(String key, long l) {
        coolTimeMap.put(key, System.currentTimeMillis() + l * 1000);
    }

    public static boolean isCoolFinish(String key) {
        Long aLong = coolTimeMap.get(key);
        if (aLong != null && aLong > System.currentTimeMillis()) return false;
        return true;
    }
}
