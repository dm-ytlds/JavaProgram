package demi.core;

import demi.constant.Constant;

import java.util.concurrent.atomic.LongAdder;

public class DownLoadInfoThread implements Runnable {
    // �����ļ��ܴ�С
    private long httpFileContentLength;
    // �����������ļ��Ĵ�С
    public static LongAdder finishedSize = new LongAdder();

    // �����ۼ����صĴ�С
    public static volatile LongAdder downSize = new LongAdder();
    // ǰһ�����صĴ�С
    public double prevSize;

    public DownLoadInfoThread(long httpFileContentLength) {
        this.httpFileContentLength = httpFileContentLength;
    }

    @Override
    public void run() {
        // �����ļ��ܴ�С
        String httpFileSize = String.format("%.2f", httpFileContentLength / Constant.MB);
        // ����ÿ�������ٶ�
        int speed = (int) ((downSize.doubleValue() - prevSize) / Constant.KB);
        prevSize = downSize.doubleValue();
        // ʣ���ļ���С
        double remainSize = httpFileContentLength - finishedSize.doubleValue() - downSize.doubleValue();
        // ����ʣ��ʱ��
        String remainTime = String.format("%.2f", remainSize / speed / Constant.KB);
        // ���ʱ����Ч��
        if ("Infinity".equalsIgnoreCase(remainTime)) {
            remainTime = "-";
        }
        // �����ش�С
        String currentFileSize = String.format("%.2f", (downSize.doubleValue() - finishedSize.doubleValue()) / Constant.MB);
        // �ַ���ƴ��
        String downInfo = String.format("������ %smb / %smb, �ٶ� %skb/s, ʣ��ʱ�� %ss", currentFileSize, httpFileSize, speed, remainTime);
        // �����Ϣ
        System.out.print("\r");
        System.out.print(downInfo);
    }
}
