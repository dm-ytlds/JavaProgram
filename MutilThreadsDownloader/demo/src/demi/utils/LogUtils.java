package demi.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 日志工具类
 */
public class LogUtils {
    public static void info(String msg, Object... args) {
        print(msg, "-info-", args);
    }
    public static void error(String msg, Object... args) {
        print(msg, "-error-", args);
    }
    /**
     * 输出日志文件
     * @param msg   日志信息
     * @param level 日志类型
     * @param args  传入的参数，这里不确定有哪些参数，所以用的是可变参数 Object... args
     */
    private static void print(String msg, String level, Object... args) {
        if (args != null && args.length > 0) {
            // 将{}替换成文本字符串
            msg = String.format(msg.replace("{}", "%s"), args);
        }
        // 当前线程的名称
        String threadName = Thread.currentThread().getName();
        // 输出日志信息
        System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")) + " " + threadName + level + msg);

    }
}
