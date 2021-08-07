package demi.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * http相关工具类
 */
public class HttpUtils {

    public static long getHttpFileContentLength(String url) throws Exception {

        int contentLength = 0;
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = getHttpURLConnection(url);
            contentLength = httpURLConnection.getContentLength();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return contentLength;
    }

    /**
     * 分块下载请求
     * @param url   下载地址
     * @param startPos  开始位置
     * @param endPos    结束位置
     * @return  返回下载请求值
     * @throws IOException
     */
    public static HttpURLConnection getHttpURLConnection(String url, long startPos, long endPos) throws Exception {
        // 获取文件链接请求
        HttpURLConnection httpURLConnection = getHttpURLConnection(url);
        // 下载提示信息
        LogUtils.info("下载的区间是：{}-{}", startPos, endPos);
        // 如果下载没结束
        if (endPos != 0) {
            // 请求获取的字节区间
            httpURLConnection.setRequestProperty("RANGE", "bytes=" + startPos + "-" + endPos);
        } else {
            httpURLConnection.setRequestProperty("RANGE", "bytes=" + startPos + "-");
        }
        return httpURLConnection;
    }


    /**
     * 获取HTTPURLConnection链接对象
     * @param url 文件下载地址
     * @return
     * @throws Exception
     */
    public static HttpURLConnection getHttpURLConnection(String url) throws Exception {
        URL httpUrl = new URL(url);
        // 将访问地址强转换成HttpURLConnection对象
        HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
        // 向文件所在服务器发送用户访问标识信息
        httpURLConnection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36");
        return httpURLConnection;
    }

    public static String getFileName(String url) {
        int index = url.lastIndexOf("/");
        String fileName = url.substring(index + 1);
        return fileName;
    }
}
