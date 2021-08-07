package demi.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * ��־������
 */
public class LogUtils {
    public static void info(String msg, Object... args) {
        print(msg, "-info-", args);
    }
    public static void error(String msg, Object... args) {
        print(msg, "-error-", args);
    }
    /**
     * �����־�ļ�
     * @param msg   ��־��Ϣ
     * @param level ��־����
     * @param args  ����Ĳ��������ﲻȷ������Щ�����������õ��ǿɱ���� Object... args
     */
    private static void print(String msg, String level, Object... args) {
        if (args != null && args.length > 0) {
            // ��{}�滻���ı��ַ���
            msg = String.format(msg.replace("{}", "%s"), args);
        }
        // ��ǰ�̵߳�����
        String threadName = Thread.currentThread().getName();
        // �����־��Ϣ
        System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")) + " " + threadName + level + msg);

    }
}
