package com.xiyuan.pay.util;

import java.util.Random;

/**
 * Created by xiyuan_fengyu on 2016/8/25.
 */
public class RandomUtil {

    public static final char[] chars = "01234567890abcdefghigklmnopqrstuvwxwzABCDEFGHIGKLMNOPQRSTUVWXWZ".toCharArray();

    public static final int charsLen = chars.length;

    public static final Random random = new Random();

    public static String generateTimeNumber() {
        String randomCode = String.format("%04d", random.nextInt(10000));
        return DateUtil.nowToNoDividerStr() + randomCode;
    }

    public static String generateStr(int minLen, int maxLen) {
        if (minLen == maxLen) {
            return generateStr(minLen);
        }
        else {
            int min = Math.min(minLen, maxLen);
            int interval = Math.abs(maxLen - minLen);
            return generateStr(min + random.nextInt(interval));
        }
    }

    public static String generateStr(int length) {
        StringBuilder strBld = new StringBuilder();
        for (int i = 0; i < length; i++) {
            strBld.append(chars[random.nextInt(charsLen)]);
        }
        return strBld.toString();
    }

}
