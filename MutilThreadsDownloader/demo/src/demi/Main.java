package demi;

import demi.core.DownLoader;
import demi.utils.LogUtils;

import java.util.Scanner;

/**
 * ������ȡ���ص�ַ������
 */
public class Main {
    public static void main(String[] args) {
        // ��ȡ���ص�ַ
        String url;
        if (args == null || args.length == 0) {
            for (; ;) {
                LogUtils.info("���������ص�ַ��");
                Scanner sc = new Scanner(System.in);
                url = sc.next();
                if (url != null) {
                    break;
                }
            }
        } else
            url = args[0];
        // ���������������ļ�
        DownLoader downloader = new DownLoader();
        downloader.download(url);
    }
}
