/*
 * @(#)ByteUtil.java     v1.01, 2013 12 16
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.common.util;

/**
 * ClassName:   ByteUtil.java
 * <p>
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 16 09:58:14
 */
public class ByteUtil {

    /**
     * ת��shortΪbyte
     * 
     * @param b
     * @param s
     *            ��Ҫת����short
     * @param index
     */
    /**
     * putShort:
     * <p>
     *
     * @param   b
     * @param   s
     * @param   index
     *
     * @since   v1.01
     */
    public static byte[] short2ByteArray(short s) {
        byte[] array = new byte[4];
        array[1] = (byte) (s >> 8);
        array[0] = (byte) (s >> 0);
        return array;
    }

    /**
     * ͨ��byte����ȡ��short
     * 
     * @param b
     * @param index
     *            �ڼ�λ��ʼȡ
     * @return
     */
    public static short byteArray2Short(byte[] b) {
        return (short) (((b[1] << 8) | b[0] & 0xff));
    }

    /**
     * ת��intΪbyte����
     * 
     * @param bb
     * @param x
     * @param index
     */
    public static byte[] int2ByteArray(int x) {
        byte[] array = new byte[4];
        array[3] = (byte) (x >> 24);
        array[2] = (byte) (x >> 16);
        array[1] = (byte) (x >> 8);
        array[0] = (byte) (x >> 0);
        return array;
    }

    /**
     * ͨ��byte����ȡ��int
     * 
     * @param bb
     * @param index
     *            �ڼ�λ��ʼ
     * @return
     */
    public static int getInt(byte[] bb, int index) {
        return (int) ((((bb[index + 3] & 0xff) << 24)
                | ((bb[index + 2] & 0xff) << 16)
                | ((bb[index + 1] & 0xff) << 8) | ((bb[index + 0] & 0xff) << 0)));
    }

    /**
     * ת��long��Ϊbyte����
     * 
     * @param bb
     * @param x
     * @param index
     */
    public static void putLong(byte[] bb, long x, int index) {
        bb[index + 7] = (byte) (x >> 56);
        bb[index + 6] = (byte) (x >> 48);
        bb[index + 5] = (byte) (x >> 40);
        bb[index + 4] = (byte) (x >> 32);
        bb[index + 3] = (byte) (x >> 24);
        bb[index + 2] = (byte) (x >> 16);
        bb[index + 1] = (byte) (x >> 8);
        bb[index + 0] = (byte) (x >> 0);
    }

    /**
     * ͨ��byte����ȡ��long
     * 
     * @param bb
     * @param index
     * @return
     */
    public static long getLong(byte[] bb, int index) {
        return ((((long) bb[index + 7] & 0xff) << 56)
                | (((long) bb[index + 6] & 0xff) << 48)
                | (((long) bb[index + 5] & 0xff) << 40)
                | (((long) bb[index + 4] & 0xff) << 32)
                | (((long) bb[index + 3] & 0xff) << 24)
                | (((long) bb[index + 2] & 0xff) << 16)
                | (((long) bb[index + 1] & 0xff) << 8) | (((long) bb[index + 0] & 0xff) << 0));
    }

    /**
     * �ַ����ֽ�ת��
     * 
     * @param ch
     * @return
     */
    public static void putChar(byte[] bb, char ch, int index) {
        int temp = (int) ch;
        // byte[] b = new byte[2];
        for (int i = 0; i < 2; i ++ ) {
            bb[index + i] = new Integer(temp & 0xff).byteValue(); // �����λ���������λ
            temp = temp >> 8; // ������8λ
        }
    }

    /**
     * �ֽڵ��ַ�ת��
     * 
     * @param b
     * @return
     */
    public static char getChar(byte[] b, int index) {
        int s = 0;
        if (b[index + 1] > 0)
            s += b[index + 1];
        else
            s += 256 + b[index + 0];
        s *= 256;
        if (b[index + 0] > 0)
            s += b[index + 1];
        else
            s += 256 + b[index + 0];
        char ch = (char) s;
        return ch;
    }

    /**
     * floatת��byte
     * 
     * @param bb
     * @param x
     * @param index
     */
    public static byte[] float2ByteArray(float x) {
        byte[] b = new byte[4];
        int l = Float.floatToIntBits(x);
        for (int i = 0; i < 4; i++) {
            b[i] = new Integer(l).byteValue();
            l = l >> 8;
        }
        return b;
    }

    /**
     * ͨ��byte����ȡ��float
     * 
     * @param bb
     * @param index
     * @return
     */
    public static float getFloat(byte[] b, int index) {
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

    /**
     * doubleת��byte
     * 
     * @param bb
     * @param x
     * @param index
     */
    public static void putDouble(byte[] bb, double x, int index) {
        // byte[] b = new byte[8];
        long l = Double.doubleToLongBits(x);
        for (int i = 0; i < 4; i++) {
            bb[index + i] = new Long(l).byteValue();
            l = l >> 8;
        }
    }

    /**
     * ͨ��byte����ȡ��float
     * 
     * @param bb
     * @param index
     * @return
     */
    public static double getDouble(byte[] b, int index) {
        long l;
        l = b[0];
        l &= 0xff;
        l |= ((long) b[1] << 8);
        l &= 0xffff;
        l |= ((long) b[2] << 16);
        l &= 0xffffff;
        l |= ((long) b[3] << 24);
        l &= 0xffffffffl;
        l |= ((long) b[4] << 32);
        l &= 0xffffffffffl;
        l |= ((long) b[5] << 40);
        l &= 0xffffffffffffl;
        l |= ((long) b[6] << 48);
        l &= 0xffffffffffffffl;
        l |= ((long) b[7] << 56);
        return Double.longBitsToDouble(l);
    }

}

