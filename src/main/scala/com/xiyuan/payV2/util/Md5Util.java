package com.xiyuan.payV2.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;

/**
 * Created by xiyuan_fengyu on 2016/8/26.
 */
public class Md5Util {

    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    private static String byteArrayToHexString(byte bs[]) {
        StringBuilder resultSb = new StringBuilder();
        for (byte b: bs) {
            resultSb.append(byteToHexString(b));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String get(String origin) {
        return get(origin, StandardCharsets.UTF_8);
    }

    public static String get(String origin, Charset charsets) {
        if (charsets == null) {
            charsets = StandardCharsets.UTF_8;
        }
        return get(origin.getBytes(charsets));
    }

    public static String get(byte[] origin) {
        String resultString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(origin));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    public static String get(InputStream in) {
        if (in == null) return "";

        String resultString = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            updateDigest(md, in);
            resultString = byteArrayToHexString(md.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    private static void updateDigest(MessageDigest md, InputStream in) throws IOException {
        byte[] buffer = new byte[2048];
        int len;
        try {
            while ((len = in.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String get(File file) {
        if (file != null && file.exists()) {
            if (file.isFile()) {
                return getSingleFile(file);
            }
            else {
                ArrayList<File> subs = new ArrayList<>();
                subFiles(file, subs);
                try {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    for (File sub : subs) {
                        md.update(getRelativePath(sub, file).getBytes(StandardCharsets.UTF_8));
                        updateDigest(md, new FileInputStream(sub));
                    }
                    return byteArrayToHexString(md.digest());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    private static String getRelativePath(File cur, File root) {
        String path = cur.getAbsolutePath().substring(root.getAbsolutePath().length()).replace('\\', '/');
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        return path;
    }

    private static String getSingleFile(File file) {
        try {
            return get(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void subFiles(File root, ArrayList<File> result) {
        File[] files = root.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    result.add(file);
                }
                else {
                    subFiles(file, result);
                }
            }
        }
    }

}