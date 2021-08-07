package demi.learn;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleTest {
    public static void main(String[] args) {
        schedule02();
    }
    public static void schedule() {
        // ��ȡ����
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        // ��ʱִ�������������õ����ӳ�2��ִ�е�ǰ�̡߳�
        ses.schedule(() -> System.out.println(Thread.currentThread().getName()), 2, TimeUnit.SECONDS);
        // �ֶ��ر�
        ses.shutdown();
    }

    public static void schedule02() {
        // ��ȡ����
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        // ��ʱ2���ʼִ������ÿ���3����ִ������
        ses.scheduleAtFixedRate(() -> {
            System.out.println(System.currentTimeMillis());
        }, 2, 3, TimeUnit.SECONDS);
    }
}
