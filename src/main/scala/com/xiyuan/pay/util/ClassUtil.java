package com.xiyuan.pay.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiyuan_fengyu on 2016/8/26.
 */
public class ClassUtil {

    private static Map<Class<?>, ClassAndMethod> classNameAndMethod = new HashMap<>();

    private static class ClassAndMethod {
        private Class<?> clazz;
        private Method method;

        public ClassAndMethod(Class<?> clazz, Method method) {
            this.clazz = clazz;
            this.method = method;
        }
    }

    private static void init() {
        try {
            {
                Method method = Byte.class.getMethod("valueOf", String.class);
                ClassAndMethod classAndMethod = new ClassAndMethod(Byte.class, method);
                classNameAndMethod.put(Byte.class, classAndMethod);
                classNameAndMethod.put(byte.class, classAndMethod);
            }

            {
                Method method = Boolean.class.getMethod("valueOf", String.class);
                ClassAndMethod classAndMethod = new ClassAndMethod(Boolean.class, method);
                classNameAndMethod.put(Boolean.class, classAndMethod);
                classNameAndMethod.put(boolean.class, classAndMethod);
            }

            {
                Method method = Short.class.getMethod("valueOf", String.class);
                ClassAndMethod classAndMethod = new ClassAndMethod(Short.class, method);
                classNameAndMethod.put(Short.class, classAndMethod);
                classNameAndMethod.put(short.class, classAndMethod);
            }

            {
                Method method = Integer.class.getMethod("valueOf", String.class);
                ClassAndMethod classAndMethod = new ClassAndMethod(Integer.class, method);
                classNameAndMethod.put(Integer.class, classAndMethod);
                classNameAndMethod.put(int.class, classAndMethod);
            }

            {
                Method method = Long.class.getMethod("valueOf", String.class);
                ClassAndMethod classAndMethod = new ClassAndMethod(Long.class, method);
                classNameAndMethod.put(Long.class, classAndMethod);
                classNameAndMethod.put(long.class, classAndMethod);
            }

            {
                Method method = Float.class.getMethod("valueOf", String.class);
                ClassAndMethod classAndMethod = new ClassAndMethod(Float.class, method);
                classNameAndMethod.put(Float.class, classAndMethod);
                classNameAndMethod.put(float.class, classAndMethod);
            }

            {
                Method method = Double.class.getMethod("valueOf", String.class);
                ClassAndMethod classAndMethod = new ClassAndMethod(Double.class, method);
                classNameAndMethod.put(Double.class, classAndMethod);
                classNameAndMethod.put(double.class, classAndMethod);
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static <T> void setValue(Class<?> clazz, T instance, String fieldName, String value) {
        if (instance == null || instance.getClass() != clazz) {
            return;
        }

        if (classNameAndMethod.isEmpty()) {
            init();
        }

        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
        }

        if (field != null) {
            try {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                if (fieldType == String.class) {
                    field.set(instance, value);
                }
                else if (fieldType == char.class || fieldType == Character.class) {
                    field.set(instance, value.charAt(0));
                }
                else  {
                    ClassAndMethod classAndMethod = classNameAndMethod.get(fieldType);
                    if (classAndMethod != null) {
                        field.set(instance, classAndMethod.method.invoke(classAndMethod.clazz, value));
                    }
                }

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

        }

    }

//    public static void main(String[] args) {
//        A a = new A();
//        setValue(A.class, a, "aByte", "1");
//        setValue(A.class, a, "aBoolean", "true");
//        setValue(A.class, a, "aChar", "c");
//        setValue(A.class, a, "aShort", "2");
//        setValue(A.class, a, "aInt", "123");
//        setValue(A.class, a, "aLong", "123");
//        setValue(A.class, a, "aFloat", "123.0");
//        setValue(A.class, a, "aDouble", "123.001");
//        setValue(A.class, a, "aString", "dsfsd");
//        XYLog.d(a);
//    }
//
//    private static class A {
//
//        public byte aByte;
//
//        public boolean aBoolean;
//
//        public char aChar;
//
//        public short aShort;
//
//        public int aInt;
//
//        public long aLong;
//
//        public float aFloat;
//
//        public double aDouble;
//
//        public String aString;
//
//    }

}
