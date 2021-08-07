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
 * 分块下载文件任务处理
 */
public class DownLoaderTask implements Callable<Boolean> {
    // 文件下载地址
    private String url;
    // 文件下载的起始位置
    private long startPos;
    // 文件下载的结束位置
    private long endPos;
    // 文件下载的区间值
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
        // 获取下载的文件名称
        String fileName = HttpUtils.getFileName(url);
        // 拼接分块下载的文件名
        fileName = fileName + ".temp" + part;
        // 文件下载路径
        fileName = Constant.FILE_PATH + fileName;
        // 获取分块下载的链接
        HttpURLConnection httpURLConnection = HttpUtils.getHttpURLConnection(url, startPos, endPos);

        try(
                // 获取链接文件的流
                InputStream inputStream = httpURLConnection.getInputStream();
                // 利用转换输入流读取文件
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                // 利用随机存取文件类存储文件
                RandomAccessFile accessFile = new RandomAccessFile(fileName, "rw")
        ) {
            // 读 写 文件
            int readCount = -1;
            byte[] bytes = new byte[Constant.BYTE_SIZE];
            while ((readCount = bis.read(bytes)) != -1) {
                // 一秒内下载文件数据之和，通过原子类进行操作
                DownLoadInfoThread.downSize.add(readCount);
                accessFile.write(bytes, 0, readCount);
            }
        } catch (FileNotFoundException f) {
            LogUtils.error("待下载的文件不存在：{}", url);
            return false;
        } catch (Exception e) {
            LogUtils.error("下载文件出现异常！！！");
            return false;
        } finally {
            httpURLConnection.disconnect();
            //
            countDownLatch.countDown();
        }
        return true;
    }
}
