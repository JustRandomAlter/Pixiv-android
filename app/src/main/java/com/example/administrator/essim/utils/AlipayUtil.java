package com.example.administrator.essim.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.net.URISyntaxException;

public class AlipayUtil {

    public static final String MY_ACCOUNT = "FKX06500UXERPRXFYBV102";
    public static final String ACCOUNT_2 = "FKX00207SI2YQ0GCDHAU73";
    public static final String ACCOUNT_3 = "FKX03258JYIGPFVZAN15F3";
    public static final String ACCOUNT_4 = "FKX05738OA8WPOW0FTXOF5";
    // 支付宝包名
    private static final String ALIPAY_PACKAGE_NAME = "com.eg.android.AlipayGphone";

    // 旧版支付宝二维码通用 Intent Scheme Url 格式
    private static final String INTENT_URL_FORMAT = "intent://platformapi/startapp?saId=10000007&" +
            "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2F{urlCode}%3F_s" +
            "%3Dweb-other&_t=1472443966571#Intent;" +
            "scheme=alipayqr;package=com.eg.android.AlipayGphone;end";

    /**
     * 打开转账窗口
     * 旧版支付宝二维码方法，需要使用 https://fama.alipay.com/qrcode/index.htm 网站生成的二维码
     * 这个方法最好，但在 2016 年 8 月发现新用户可能无法使用
     *
     * @param activity Parent Activity
     * @param urlCode  手动解析二维码获得地址中的参数，例如 https://qr.alipay.com/aehvyvf4taua18zo6e 最后那段
     * @return 是否成功调用
     */
    public static boolean startAlipayClient(Activity activity, String urlCode) {
        if (hasInstalledAlipayClient(activity)) {
            Common.showToast(activity, "打开支付宝");
            return startIntentUrl(activity, INTENT_URL_FORMAT.replace("{urlCode}", urlCode));
        } else {
            Common.showToast(activity, "你好像没有安装支付宝");
            return false;
        }
    }

    /**
     * 打开 Intent Scheme Url
     *
     * @param activity      Parent Activity
     * @param intentFullUrl Intent 跳转地址
     * @return 是否成功调用
     */
    public static boolean startIntentUrl(Activity activity, String intentFullUrl) {
        try {
            Intent intent = Intent.parseUri(
                    intentFullUrl,
                    Intent.URI_INTENT_SCHEME
            );
            activity.startActivity(intent);
            return true;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断支付宝客户端是否已安装，建议调用转账前检查
     *
     * @param context Context
     * @return 支付宝客户端是否已安装
     */
    public static boolean hasInstalledAlipayClient(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(ALIPAY_PACKAGE_NAME, 0);
            return info != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
