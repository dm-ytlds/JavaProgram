package demi.learn;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleTest {
    public static void main(String[] args) {
        schedule02();
    }
    public static void schedule() {
        // 获取对象
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        // 延时执行任务。这里设置的是延迟2秒执行当前线程。
        ses.schedule(() -> System.out.println(Thread.currentThread().getName()), 2, TimeUnit.SECONDS);
        // 手动关闭
        ses.shutdown();
    }

    public static void schedule02() {
        // 获取对象
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        // 延时2秒后开始执行任务，每间隔3秒在执行任务。
        ses.scheduleAtFixedRate(() -> {
            System.out.println(System.currentTimeMillis());
        }, 2, 3, TimeUnit.SECONDS);
    }
}
