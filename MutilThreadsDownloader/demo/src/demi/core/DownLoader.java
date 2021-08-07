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
 * 下载器定义类
 */
public class DownLoader {

    public ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    // 线程池对象
    ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(Constant.THREAD_NUM, Constant.THREAD_NUM, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(Constant.THREAD_NUM));

    // 创建CountDownLatch对象，实现线程计数
    CountDownLatch countDownLatch = new CountDownLatch(Constant.THREAD_NUM);

    public void download(String url) {
        // 获取文件名
        String fileName = HttpUtils.getFileName(url);
        // 将文件名与路径名做拼接
        String httpFileName = Constant.FILE_PATH + fileName;

        // 获取本地文件的大小
        long localFileContentLength = FileUtils.getFileContentLength(httpFileName);

        HttpURLConnection httpURLConnection = null;
        DownLoadInfoThread downLoadInfoThread = null;
        try {
            // 获取连接对象
            httpURLConnection = HttpUtils.getHttpURLConnection(url);
            // 获取下载文件的总大小
            int contentLength = httpURLConnection.getContentLength();
            // 文件是否已下载了
            if (localFileContentLength >= contentLength) {
                LogUtils.info("{}已下载完毕，无需重复下载！", httpFileName);
                return;
            }
            // 创建获取下载信息的任务对象
            downLoadInfoThread = new DownLoadInfoThread(contentLength);
            // 将任务交给线程执行
            scheduledExecutorService.scheduleAtFixedRate(downLoadInfoThread, 1, 1, TimeUnit.SECONDS);

            // 切分任务
            ArrayList<Future> list = new ArrayList<>();
            split(url, list);
            /*// 拿到list集合中的文件
            list.forEach(future -> {
                try {
                    future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });*/
            // 将上面的代码改成countDownLatch代码实现
            countDownLatch.await();

            // 合并完，在清除
            if (merge(httpFileName)) {
                clearTemp(httpFileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.print("\r");
            System.out.print("下载完成！");
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            // 关闭执行进程
            scheduledExecutorService.shutdownNow();
            // 关闭线程池
            poolExecutor.shutdown();
        }
        // 未分块之前
        /*try (
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                FileOutputStream fos = new FileOutputStream(httpFileName);
                BufferedOutputStream bos = new BufferedOutputStream(fos)
        ) {
            // 利用文件输入流和文件输出流，将文件读取，并写入
            int readCount = -1;
            byte[] bytes = new byte[Constant.BYTE_SIZE];
            while ((readCount = bis.read(bytes)) != -1) {
                // 更新本次累积下载文件大小
                downLoadInfoThread.downSize += readCount;
                bos.write(bytes, 0, readCount);
            }
        } catch (FileNotFoundException f) {
            LogUtils.error("下载文件不存在{}", url);
        } catch (Exception e) {
            LogUtils.error("下载失败");
        } finally {
            System.out.print("\r");
            System.out.print("下载完成！");
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            // 关闭线程池
            scheduledExecutorService.shutdownNow();
        }
            */

    }

    /**
     * 分块
     * @param url
     * @param futureArrayList
     */
    public void split(String url, ArrayList<Future> futureArrayList) {

        try {
            // 获取文件的下载大小
            long fileContentLength = HttpUtils.getHttpFileContentLength(url);
            // 计算切分后文件大小
            long size = fileContentLength / Constant.THREAD_NUM;
            // 计算分块数量
            for (int i = 0; i < Constant.THREAD_NUM; i++) {
                // 计算下载的起始位置
                long startPos = i * size;
                // 计算结束位置
                long endPos;
                if (i == Constant.THREAD_NUM - 1) {
                    // 下载最后一块
                    endPos = 0;
                } else
                    endPos = startPos + size;
                // 如果不是第一块
                if (startPos != 0) {
                    startPos++;
                }
                DownLoaderTask downLoaderTask = new DownLoaderTask(url, startPos, endPos, i, countDownLatch);

                // 将任务提交到线程池中
                // submit支持Runnable和Callable,execute只支持Runnable
                Future<Boolean> future = poolExecutor.submit(downLoaderTask);
                futureArrayList.add(future);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 合并文件
     * @param fileName
     * @return
     */
    public boolean merge(String fileName) {
         LogUtils.info("开始合并文件");
         // 一次读取文件的大小
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
             LogUtils.info("文件合并完毕：{}", fileName);
         } catch (Exception e) {
             e.printStackTrace();
             return false;
         }
    return true;
    }

    /**
     * 删除临时文件
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
