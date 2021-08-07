package demi.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * http��ع�����
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
     * �ֿ���������
     * @param url   ���ص�ַ
     * @param startPos  ��ʼλ��
     * @param endPos    ����λ��
     * @return  ������������ֵ
     * @throws IOException
     */
    public static HttpURLConnection getHttpURLConnection(String url, long startPos, long endPos) throws Exception {
        // ��ȡ�ļ���������
        HttpURLConnection httpURLConnection = getHttpURLConnection(url);
        // ������ʾ��Ϣ
        LogUtils.info("���ص������ǣ�{}-{}", startPos, endPos);
        // �������û����
        if (endPos != 0) {
            // �����ȡ���ֽ�����
            httpURLConnection.setRequestProperty("RANGE", "bytes=" + startPos + "-" + endPos);
        } else {
            httpURLConnection.setRequestProperty("RANGE", "bytes=" + startPos + "-");
        }
        return httpURLConnection;
    }


    /**
     * ��ȡHTTPURLConnection���Ӷ���
     * @param url �ļ����ص�ַ
     * @return
     * @throws Exception
     */
    public static HttpURLConnection getHttpURLConnection(String url) throws Exception {
        URL httpUrl = new URL(url);
        // �����ʵ�ַǿת����HttpURLConnection����
        HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
        // ���ļ����ڷ����������û����ʱ�ʶ��Ϣ
        httpURLConnection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36");
        return httpURLConnection;
    }

    public static String getFileName(String url) {
        int index = url.lastIndexOf("/");
        String fileName = url.substring(index + 1);
        return fileName;
    }
}
