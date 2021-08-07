package demi;

import demi.core.DownLoader;
import demi.utils.LogUtils;

import java.util.Scanner;

/**
 * 用来获取下载地址的主类
 */
public class Main {
    public static void main(String[] args) {
        // 获取下载地址
        String url;
        if (args == null || args.length == 0) {
            for (; ;) {
                LogUtils.info("请输入下载地址：");
                Scanner sc = new Scanner(System.in);
                url = sc.next();
                if (url != null) {
                    break;
                }
            }
        } else
            url = args[0];
        // 调用下载器下载文件
        DownLoader downloader = new DownLoader();
        downloader.download(url);
    }
}
