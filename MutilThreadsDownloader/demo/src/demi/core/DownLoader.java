package demi.core;

import demi.constant.Constant;
import demi.utils.FileUtils;
import demi.utils.HttpUtils;
import demi.utils.LogUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * ������������
 */
public class DownLoader {

    public ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    // �̳߳ض���
    ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(Constant.THREAD_NUM, Constant.THREAD_NUM, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(Constant.THREAD_NUM));

    // ����CountDownLatch����ʵ���̼߳���
    CountDownLatch countDownLatch = new CountDownLatch(Constant.THREAD_NUM);

    public void download(String url) {
        // ��ȡ�ļ���
        String fileName = HttpUtils.getFileName(url);
        // ���ļ�����·������ƴ��
        String httpFileName = Constant.FILE_PATH + fileName;

        // ��ȡ�����ļ��Ĵ�С
        long localFileContentLength = FileUtils.getFileContentLength(httpFileName);

        HttpURLConnection httpURLConnection = null;
        DownLoadInfoThread downLoadInfoThread = null;
        try {
            // ��ȡ���Ӷ���
            httpURLConnection = HttpUtils.getHttpURLConnection(url);
            // ��ȡ�����ļ����ܴ�С
            int contentLength = httpURLConnection.getContentLength();
            // �ļ��Ƿ���������
            if (localFileContentLength >= contentLength) {
                LogUtils.info("{}��������ϣ������ظ����أ�", httpFileName);
                return;
            }
            // ������ȡ������Ϣ���������
            downLoadInfoThread = new DownLoadInfoThread(contentLength);
            // �����񽻸��߳�ִ��
            scheduledExecutorService.scheduleAtFixedRate(downLoadInfoThread, 1, 1, TimeUnit.SECONDS);

            // �з�����
            ArrayList<Future> list = new ArrayList<>();
            split(url, list);
            /*// �õ�list�����е��ļ�
            list.forEach(future -> {
                try {
                    future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });*/
            // ������Ĵ���ĳ�countDownLatch����ʵ��
            countDownLatch.await();

            // �ϲ��꣬�����
            if (merge(httpFileName)) {
                clearTemp(httpFileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.print("\r");
            System.out.print("������ɣ�");
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            // �ر�ִ�н���
            scheduledExecutorService.shutdownNow();
            // �ر��̳߳�
            poolExecutor.shutdown();
        }
        // δ�ֿ�֮ǰ
        /*try (
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                FileOutputStream fos = new FileOutputStream(httpFileName);
                BufferedOutputStream bos = new BufferedOutputStream(fos)
        ) {
            // �����ļ����������ļ�����������ļ���ȡ����д��
            int readCount = -1;
            byte[] bytes = new byte[Constant.BYTE_SIZE];
            while ((readCount = bis.read(bytes)) != -1) {
                // ���±����ۻ������ļ���С
                downLoadInfoThread.downSize += readCount;
                bos.write(bytes, 0, readCount);
            }
        } catch (FileNotFoundException f) {
            LogUtils.error("�����ļ�������{}", url);
        } catch (Exception e) {
            LogUtils.error("����ʧ��");
        } finally {
            System.out.print("\r");
            System.out.print("������ɣ�");
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            // �ر��̳߳�
            scheduledExecutorService.shutdownNow();
        }
            */

    }

    /**
     * �ֿ�
     * @param url
     * @param futureArrayList
     */
    public void split(String url, ArrayList<Future> futureArrayList) {

        try {
            // ��ȡ�ļ������ش�С
            long fileContentLength = HttpUtils.getHttpFileContentLength(url);
            // �����зֺ��ļ���С
            long size = fileContentLength / Constant.THREAD_NUM;
            // ����ֿ�����
            for (int i = 0; i < Constant.THREAD_NUM; i++) {
                // �������ص���ʼλ��
                long startPos = i * size;
                // �������λ��
                long endPos;
                if (i == Constant.THREAD_NUM - 1) {
                    // �������һ��
                    endPos = 0;
                } else
                    endPos = startPos + size;
                // ������ǵ�һ��
                if (startPos != 0) {
                    startPos++;
                }
                DownLoaderTask downLoaderTask = new DownLoaderTask(url, startPos, endPos, i, countDownLatch);

                // �������ύ���̳߳���
                // submit֧��Runnable��Callable,executeֻ֧��Runnable
                Future<Boolean> future = poolExecutor.submit(downLoaderTask);
                futureArrayList.add(future);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * �ϲ��ļ�
     * @param fileName
     * @return
     */
    public boolean merge(String fileName) {
         LogUtils.info("��ʼ�ϲ��ļ�");
         // һ�ζ�ȡ�ļ��Ĵ�С
         byte[] bytes = new byte[Constant.BYTE_SIZE];
         int readCount = -1;
         try (RandomAccessFile accessFile = new RandomAccessFile(fileName, "rw")) {
             for (int i = 0; i < Constant.THREAD_NUM; i++) {
                 try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName + ".temp" + i))) {
                     while ((readCount = bis.read(bytes)) != -1) {
                         accessFile.write(bytes, 0, readCount);
                     }
                 }
             }
             LogUtils.info("�ļ��ϲ���ϣ�{}", fileName);
         } catch (Exception e) {
             e.printStackTrace();
             return false;
         }
    return true;
    }

    /**
     * ɾ����ʱ�ļ�
     * @param fileName
     * @return
     */
    public boolean clearTemp(String fileName) {
        for (int i = 0; i < Constant.THREAD_NUM; i++) {
           File file = new File(fileName + ".temp" + i);
           file.delete();
        }
        return true;
    }
}
