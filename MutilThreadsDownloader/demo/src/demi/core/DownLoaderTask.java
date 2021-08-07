package demi.core;

import demi.constant.Constant;
import demi.utils.HttpUtils;
import demi.utils.LogUtils;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * �ֿ������ļ�������
 */
public class DownLoaderTask implements Callable<Boolean> {
    // �ļ����ص�ַ
    private String url;
    // �ļ����ص���ʼλ��
    private long startPos;
    // �ļ����صĽ���λ��
    private long endPos;
    // �ļ����ص�����ֵ
    private int part;

    private CountDownLatch countDownLatch;

    public DownLoaderTask(String url, long startPos, long endPos, int part, CountDownLatch countDownLatch) {
        this.url = url;
        this.startPos = startPos;
        this.endPos = endPos;
        this.part = part;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public Boolean call() throws Exception {
        // ��ȡ���ص��ļ�����
        String fileName = HttpUtils.getFileName(url);
        // ƴ�ӷֿ����ص��ļ���
        fileName = fileName + ".temp" + part;
        // �ļ�����·��
        fileName = Constant.FILE_PATH + fileName;
        // ��ȡ�ֿ����ص�����
        HttpURLConnection httpURLConnection = HttpUtils.getHttpURLConnection(url, startPos, endPos);

        try(
                // ��ȡ�����ļ�����
                InputStream inputStream = httpURLConnection.getInputStream();
                // ����ת����������ȡ�ļ�
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                // ���������ȡ�ļ���洢�ļ�
                RandomAccessFile accessFile = new RandomAccessFile(fileName, "rw")
        ) {
            // �� д �ļ�
            int readCount = -1;
            byte[] bytes = new byte[Constant.BYTE_SIZE];
            while ((readCount = bis.read(bytes)) != -1) {
                // һ���������ļ�����֮�ͣ�ͨ��ԭ������в���
                DownLoadInfoThread.downSize.add(readCount);
                accessFile.write(bytes, 0, readCount);
            }
        } catch (FileNotFoundException f) {
            LogUtils.error("�����ص��ļ������ڣ�{}", url);
            return false;
        } catch (Exception e) {
            LogUtils.error("�����ļ������쳣������");
            return false;
        } finally {
            httpURLConnection.disconnect();
            //
            countDownLatch.countDown();
        }
        return true;
    }
}
