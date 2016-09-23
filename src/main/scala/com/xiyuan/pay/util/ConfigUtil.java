package com.xiyuan.pay.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by xiyuan_fengyu on 2016/8/25.
 */
public class ConfigUtil {

    public static void main(String[] args) {
        propertiesToClass("pay/AlipayCfg.properties", "com.xiyuan.pay.ali");
        propertiesToClass("pay/WeixinpayCfg.properties", "com.xiyuan.pay.weixin");
    }

    private static final String fileTag = "file:";

    private static final String sourceDirctory = "./src/main/scala/";
    private static final String rLongOrInt = "-[0-9]{1,19}|[+]{0,1}[0-9]{1,19}";
    private static final String rDouble = "[-+]{0,1}[0-9]+\\.[0-9]+";
    private static final String rBoolean = "true|false";

    private static String classRoot = "";

    static {
        Class<?> clazz = ConfigUtil.class;
        String classPath = "/" + clazz.getPackage().getName().replaceAll("\\.", "/") + "/" + clazz.getSimpleName() + ".class";
        String path = clazz.getResource(clazz.getSimpleName() + ".class").getPath();
        if (path.startsWith(fileTag)) {
            String jarPath = path.substring(fileTag.length(), path.indexOf(classPath));
            classRoot = jarPath.substring(0, jarPath.lastIndexOf("/")) + "/";
        }
        else {
            classRoot = path.substring(0, path.indexOf(classPath)) + "/";
        }

    }

    public static Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        try {
            properties.load(ConfigUtil.class.getClassLoader().getResourceAsStream(fileName));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    private static void propertiesToClass(String fileName, String packageStr) {
        Properties properties = new Properties();
        try {
            properties.load(ConfigUtil.class.getClassLoader().getResourceAsStream(fileName));

            StringBuilder strBld = new StringBuilder();
            Iterator<Object> keyIt = properties.keySet().iterator();
            while (keyIt.hasNext()) {
                String key = (String) keyIt.next();
                String value = properties.getProperty(key);
                String keyInJava = key.replaceAll("\\.", "_");

                if (value.matches(rBoolean)) {
                    strBld.append("\tpublic static final boolean " + keyInJava + " = Boolean.parseBoolean(properties.getProperty(\"" + key + "\"));\n\n");
                }
                else if (value.matches(rDouble)) {
                    strBld.append("\tpublic static final double " + keyInJava + " = Double.parseDouble(properties.getProperty(\"" + key + "\"));\n\n");
                }
                else if (value.matches(rLongOrInt)) {
                    try {
                        long tempL = Long.parseLong(value);
                        if (tempL >= Integer.MIN_VALUE && tempL <= Integer.MAX_VALUE) {
                            strBld.append("\tpublic static final int " + keyInJava + " = Integer.parseInt(properties.getProperty(\"" + key + "\"));\n\n");
                        }
                        else {
                            strBld.append("\tpublic static final long " + keyInJava + " = Long.parseLong(properties.getProperty(\"" + key + "\"));\n\n");
                        }
                    }
                    catch (Exception e) {
                        strBld.append("\tpublic static final String " + keyInJava + " = properties.getProperty(\"" + key + "\");\n\n");
                    }
                }
                else {
                    strBld.append("\tpublic static final String " + keyInJava + " = properties.getProperty(\"" + key + "\");\n\n");
                }
            }

            int lastSperator = fileName.replaceAll("\\\\", "/").lastIndexOf("/") + 1;
            String className = fileName.substring(lastSperator).split("\\.")[0];
            char[] cArr = className.toCharArray();
            if (cArr.length > 0 && cArr[0] >= 'a' && cArr[0] <= 'z') {
                cArr[0] = (char) (cArr[0] + 'A' - 'a');
                className = new String(cArr);
            }

            String classStr = "package " + packageStr + ";\n\n" + "import java.util.Properties;\n\nimport " + ConfigUtil.class.getName() + ";\n\n" + "public class " + className + " {\n\n" + "\tprivate static final Properties properties = " + ConfigUtil.class.getSimpleName() + ".loadProperties(\"" + fileName + "\");\n\n" + strBld.toString() + "}";

            File dir = new File(sourceDirctory + packageStr.replaceAll("\\.", "/"));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File classFile = new File(sourceDirctory + packageStr.replaceAll("\\.", "/") + "/" + className + ".java");
            try (FileOutputStream out = new FileOutputStream(classFile)) {
                out.write(classStr.getBytes("utf-8"));
                out.flush();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
