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

    public static void putCountDownTask(CountDownPerSecondTask perSecondTask, CountDownFinishTask finishTask, int m) {
        long time = System.currentTimeMillis() + m * 1000;
        new Timer().schedule(new TimerTask() {
            int times = m;

            @Override
            public void run() {
                if (System.currentTimeMillis() >= time) {
                    finishTask.onRun();
                    cancel();
                    return;
                }
                perSecondTask.onRun(times--);

            }
        }, 0, 1000);
    }

    @FunctionalInterface
    public interface CountDownFinishTask {
        void onRun();
    }

    @FunctionalInterface
    public interface CountDownPerSecondTask {
        void onRun(int time);
    }

    private static HashMap<String, Long> coolTimeMap = new HashMap<>();

    public static void putCoolTime(String key, long l) {
        coolTimeMap.put(key, System.currentTimeMillis() + l * 1000);
    }

    public static long isCoolFinish(String key) {
        Long aLong = coolTimeMap.get(key);
        if (aLong != null && aLong > System.currentTimeMillis()) return aLong - System.currentTimeMillis();
        return -1;
    }
}
