package demi.utils;

import java.io.File;

/**
 * ��ȡ�����ļ���С
 */
public class FileUtils {
    public static long getFileContentLength(String path) {
        File file = new File(path);
        return file.exists() && file.isFile() ? file.length() : 0;
    }
}
