package com.anshuman.imageprocessing.imageid.fingerprint;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.*;
import java.nio.ByteBuffer;
import java.math.BigInteger;

/**
 * .
 * User: anshu
 * Date: Dec 16, 2010
 * Time: 4:17:21 PM
 */
public class Arr {

    public static double stddev(double x[]) {
        double avg = 0;
        double avg2 = 0;
        for (int i = 0; i < x.length; ++i) {
            avg += x[i];
            avg2 += x[i] * x[i];
        }
        return Math.sqrt(avg2 / x.length - avg * avg / x.length / x.length);
    }

    public static double[] abs(final double[] r, final double[] im) {
        final double t[] = new double[r.length];
        for (int i = 0; i < t.length; ++i) {
            t[i] = Math.sqrt(r[i] * r[i] + im[i] * im[i]);
        }
        return t;
    }

    public static double[][] abs(final double[][] r, final double[][] im) {
        final double t[][] = new double[r.length][r[0].length];
        for (int i = 0; i < t.length; ++i) {
            for (int j = 0; j < t[i].length; ++j) {
                t[i][j] = Math.sqrt(r[i][j] * r[i][j] + im[i][j] * im[i][j]);
            }
        }
        return t;
    }

    public static double[] abssq(final double[] r, final double[] im) {
        final double t[] = new double[r.length];
        for (int i = 0; i < t.length; ++i) {
            t[i] = r[i] * r[i] + im[i] * im[i];
        }
        return t;
    }

    public static byte[] add(final byte a[], final byte b[]) {
        final int length = a.length;
        final byte[] c = new byte[length];
        for (int i = 0; i < length; i++) {
            c[i] = (byte) (a[i] + b[i]);
        }
        return c;
    }

    public static byte[][] add(final byte a[][], final byte b[][]) {
        final int length = a.length;
        final byte[][] c = new byte[length][];
        for (int i = 0; i < length; i++) {
            c[i] = new byte[a[i].length];
            c[i] = add(a[i], b[i]);
        }
        return c;
    }

    public static double[] add(final double a[], final double b) {
        final int length = a.length;
        final double[] c = new double[length];
        for (int i = 0; i < length; i++) {
            c[i] = a[i] + b;
        }
        return c;
    }

    public static double[][] add(final double a[][], final double b) {
        final int length = a.length;
        final double[][] c = new double[length][];
        for (int i = 0; i < length; i++) {
            c[i] = add(a[i], b);
        }
        return c;
    }

    public static double[][][] add(final double a[][][], final double b) {
        final int length = a.length;
        final double[][][] c = new double[length][][];
        for (int i = 0; i < length; i++) {
            c[i] = add(a[i], b);
        }
        return c;
    }

    public static double[] add(final double a[], final double b[]) {
        final int length = a.length;
        final double[] c = new double[length];
        for (int i = 0; i < length; i++) {
            c[i] = a[i] + b[i];
        }
        return c;
    }

    public static double[][] add(final double a[][], final double b[][]) {
        final int length = a.length;
        final double[][] c = new double[length][];
        for (int i = 0; i < length; i++) {
            c[i] = new double[a[i].length];
            c[i] = add(a[i], b[i]);
        }
        return c;
    }

    public static double[][][] add(final double a[][][], final double b[][][]) {
        final int length = a.length;
        final double[][][] c = new double[length][][];
        for (int i = 0; i < length; i++) {
            c[i] = new double[a[i].length][];
            c[i] = add(a[i], b[i]);
        }
        return c;
    }

    public static int[] add(final int a[], final int b[]) {
        final int length = a.length;
        final int[] c = new int[length];
        for (int i = 0; i < length; i++) {
            c[i] = a[i] + b[i];
        }
        return c;
    }

    public static int[] add(final int a[], final int b) {
        final int length = a.length;
        final int[] c = new int[length];
        for (int i = 0; i < length; i++) {
            c[i] = a[i] + b;
        }
        return c;
    }

    public static int[][] add(final int a[][], final int b[][]) {
        final int length = a.length;
        final int[][] c = new int[length][];
        for (int i = 0; i < length; i++) {
            c[i] = new int[a[i].length];
            c[i] = add(a[i], b[i]);
        }
        return c;
    }

    public static int[][] add(final int a[][], final int b) {
        final int length = a.length;
        final int[][] c = new int[length][];
        for (int i = 0; i < length; i++) {
            c[i] = new int[a[i].length];
            c[i] = add(a[i], b);
        }
        return c;
    }

    public static double avg(final double a[][]) {
        double sum = 0;
        int count = 0;
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a[i].length; ++j) {
                ++count;
                sum += a[i][j];
            }
        }
        return sum / count;
    }

    public static double avg(final int a[][]) {
        double sum = 0;
        int count = 0;
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a[i].length; ++j) {
                ++count;
                sum += a[i][j];
            }
        }
        return sum / count;
    }

    public static byte[] castb(final Byte a[]) {
        final byte y[] = new byte[a.length];
        for (int i = 0; i < a.length; ++i) {
            y[i] = a[i];
        }
        return y;
    }

    public static byte[] castb(final double a[]) {
        final byte y[] = new byte[a.length];
        for (int i = 0; i < a.length; ++i) {
            y[i] = (byte) a[i];
        }
        return y;
    }

    public static byte[] castb(final int a[]) {
        final byte y[] = new byte[a.length];
        for (int i = 0; i < a.length; ++i) {
            y[i] = (byte) a[i];
        }
        return y;
    }

    public static double[] castd(final byte a[]) {
        final double y[] = new double[a.length];
        for (int i = 0; i < a.length; ++i) {
            y[i] = a[i];
        }
        return y;
    }

    public static double[][] castd(final byte a[][]) {
        final double y[][] = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a[0].length; ++j) {
                y[i][j] = a[i][j];
            }
        }
        return y;
    }

    public static double[] castd(final Double a[]) {
        final double y[] = new double[a.length];
        for (int i = 0; i < a.length; ++i) {
            y[i] = a[i];
        }
        return y;
    }

    public static double[] castd(final float a[]) {
        final double y[] = new double[a.length];
        for (int i = 0; i < a.length; ++i) {
            y[i] = a[i];
        }
        return y;
    }

    public static double[] castd(final int a[]) {
        final double y[] = new double[a.length];
        for (int i = 0; i < a.length; ++i) {
            y[i] = a[i];
        }
        return y;
    }

    public static double[][] castd(final int a[][]) {
        final double y[][] = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a[0].length; ++j) {
                y[i][j] = a[i][j];
            }
        }
        return y;
    }

    public static double[][][] castd(final int a[][][]) {
        final double y[][][] = new double[a.length][a[0].length][a[0][0].length];
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a[0].length; ++j) {
                for (int k = 0; k < a[0][0].length; ++k) {
                    y[i][j][k] = a[i][j][k];
                }
            }
        }
        return y;
    }

    public static double[] castd(final long a[]) {
        final double y[] = new double[a.length];
        for (int i = 0; i < a.length; ++i) {
            y[i] = a[i];
        }
        return y;
    }

    public static float[] castf(final Float a[]) {
        final float y[] = new float[a.length];
        for (int i = 0; i < a.length; ++i) {
            y[i] = a[i];
        }
        return y;
    }

    public static int[] casti(final byte a[]) {
        final int y[] = new int[a.length];
        for (int i = 0; i < a.length; ++i) {
            y[i] = a[i];
        }
        return y;
    }

    public static int[][] casti(final byte a[][]) {
        final int y[][] = new int[a.length][a[0].length];
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a[0].length; ++j) {
                y[i][j] = a[i][j];
            }
        }
        return y;
    }

    public static int[][] casti(final double a[][]) {
        final int y[][] = new int[a.length][a[0].length];
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a[0].length; ++j) {
                y[i][j] = (int) a[i][j];
            }
        }
        return y;
    }

    public static int[][][] casti(final double a[][][]) {
        final int y[][][] = new int[a.length][a[0].length][a[0][0].length];
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a[0].length; ++j) {
                for (int k = 0; k < a[0][0].length; ++k) {
                    y[i][j][k] = (int) a[i][j][k];
                }
            }
        }
        return y;
    }

    public static int[] casti(final double a[]) {
        final int y[] = new int[a.length];
        for (int i = 0; i < a.length; ++i) {
            y[i] = (int) Math.round(a[i]);
        }
        return y;
    }

    public static int[] casti(final Integer a[]) {
        final int y[] = new int[a.length];
        for (int i = 0; i < a.length; ++i) {
            y[i] = a[i];
        }
        return y;
    }

    public static int[] casti(final long a[]) {
        final int y[] = new int[a.length];
        for (int i = 0; i < a.length; ++i) {
            y[i] = (int) a[i];
        }
        return y;
    }

    public static long[] castl(final int a[]) {
        final long y[] = new long[a.length];
        for (int i = 0; i < a.length; ++i) {
            y[i] = a[i];
        }
        return y;
    }

    public static long[] castl(final Long a[]) {
        final long y[] = new long[a.length];
        for (int i = 0; i < a.length; ++i) {
            y[i] = a[i];
        }
        return y;
    }

    public static Byte[] casto(final byte a[]) {
        final Byte y[] = new Byte[a.length];
        for (int i = 0; i < a.length; ++i) {
            y[i] = a[i];
        }
        return y;
    }

    public static Double[] casto(final double a[]) {
        final Double y[] = new Double[a.length];
        for (int i = 0; i < a.length; ++i) {
            y[i] = a[i];
        }
        return y;
    }

    public static Float[] casto(final float a[]) {
        final Float y[] = new Float[a.length];
        for (int i = 0; i < a.length; ++i) {
            y[i] = a[i];
        }
        return y;
    }

    public static Integer[] casto(final int a[]) {
        final Integer y[] = new Integer[a.length];
        for (int i = 0; i < a.length; ++i) {
            y[i] = a[i];
        }
        return y;
    }

    public static Long[] casto(final long a[]) {
        final Long y[] = new Long[a.length];
        for (int i = 0; i < a.length; ++i) {
            y[i] = a[i];
        }
        return y;
    }

    public static boolean[] cat(final boolean a[], final boolean b[]) {
        if (a == null || a.length == 0) {
            return b;
        }
        if (b == null || b.length == 0) {
            return a;
        }
        final boolean r[] = new boolean[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    public static byte[] cat(final byte a[][]) {
        int length = 0;
        for (int i = 0; i < a.length; ++i) {
            length += a[i].length;
        }
        final byte r[] = new byte[length];
        int pos = 0;
        for (int i = 0; i < a.length; ++i) {
            System.arraycopy(a[i], 0, r, pos, a[i].length);
            pos += a[i].length;
        }
        return r;
    }

    public static byte[] cat(final byte a[], final byte b[]) {
        if (a == null || a.length == 0) {
            return b;
        }
        if (b == null || b.length == 0) {
            return a;
        }
        final byte r[] = new byte[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] cat(final Class<T> type, final T a[], final T b[]) {
        if (a == null || a.length == 0) {
            return b;
        }
        if (b == null || b.length == 0) {
            return a;
        }
        final T r[] = (T[]) Array.newInstance(type, a.length + b.length);
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    public static double[] cat(final double a[], final double b[]) {
        if (a == null || a.length == 0) {
            return b;
        }
        if (b == null || b.length == 0) {
            return a;
        }
        final double r[] = new double[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    public static float[] cat(final float a[], final float b[]) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        final float r[] = new float[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    public static int[] cat(final int a[], final int b[]) {
        if (a == null || a.length == 0) {
            return b;
        }
        if (b == null || b.length == 0) {
            return a;
        }
        final int r[] = new int[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    public static long[] cat(final long a[], final long b[]) {
        if (a == null || a.length == 0) {
            return b;
        }
        if (b == null || b.length == 0) {
            return a;
        }
        final long r[] = new long[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    public static short[] cat(final short a[], final short b[]) {
        if (a == null || a.length == 0) {
            return b;
        }
        if (b == null || b.length == 0) {
            return a;
        }
        final short r[] = new short[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    public static String[] cat(final String a[], final String b[]) {
        if (a == null || a.length == 0) {
            return b;
        }
        if (b == null || b.length == 0) {
            return a;
        }
        final String r[] = new String[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    public static <T> T[] convert(final Object[] arr, final Class<T> cl) throws Exception {
        final T r[] = createArray(cl, arr.length);
        final Constructor<T> con = cl.getConstructor(arr[0].getClass());
        for (int i = 0; i < arr.length; ++i) {
            r[i] = con.newInstance(arr[i]);
        }
        return r;
    }

    public static boolean[] copy(final boolean a[]) {
        final boolean r[] = new boolean[a.length];
        System.arraycopy(a, 0, r, 0, a.length);
        return r;
    }

    public static byte[] copy(final byte a[]) {
        final byte r[] = new byte[a.length];
        System.arraycopy(a, 0, r, 0, a.length);
        return r;
    }

    public static byte[][] copy(final byte a[][]) {
        final byte r[][] = new byte[a.length][];
        for (int i = 0; i < a.length; ++i) {
            r[i] = new byte[a[i].length];
            System.arraycopy(a[i], 0, r[i], 0, a[i].length);
        }
        return r;
    }

    public static double[] copy(final double a[]) {
        final double r[] = new double[a.length];
        System.arraycopy(a, 0, r, 0, a.length);
        return r;
    }

    public static double[][] copy(final double a[][]) {
        final double r[][] = new double[a.length][];
        for (int i = 0; i < a.length; ++i) {
            r[i] = new double[a[i].length];
            System.arraycopy(a[i], 0, r[i], 0, a[i].length);
        }
        return r;
    }

    public static double[][][] copy(final double a[][][]) {
        final double r[][][] = new double[a.length][][];
        for (int i = 0; i < a.length; ++i) {
            r[i] = new double[a[i].length][];
            for (int j = 0; j < a[0].length; ++j) {
                r[i][j] = new double[a[i][j].length];
                System.arraycopy(a[i][j], 0, r[i][j], 0, a[i][j].length);
            }
        }
        return r;
    }

    public static float[] copy(final float a[]) {
        final float r[] = new float[a.length];
        System.arraycopy(a, 0, r, 0, a.length);
        return r;
    }

    public static int[][] copy(final int a[][]) {
        final int r[][] = new int[a.length][];
        for (int i = 0; i < a.length; ++i) {
            r[i] = new int[a[i].length];
            System.arraycopy(a[i], 0, r[i], 0, a[i].length);
        }
        return r;
    }

    public static int[] copy(final int a[]) {
        final int r[] = new int[a.length];
        System.arraycopy(a, 0, r, 0, a.length);
        return r;
    }

    public static long[] copy(final long a[]) {
        final long r[] = new long[a.length];
        System.arraycopy(a, 0, r, 0, a.length);
        return r;
    }

    public static short[] copy(final short a[]) {
        final short r[] = new short[a.length];
        System.arraycopy(a, 0, r, 0, a.length);
        return r;
    }

    public static String[] copy(final String a[]) {
        final String r[] = new String[a.length];
        System.arraycopy(a, 0, r, 0, a.length);
        return r;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] copy(final T a[]) {
        final T r[] = (T[]) Array.newInstance(a[0].getClass(), a.length);
        System.arraycopy(a, 0, r, 0, a.length);
        return r;
    }

    public static double[] cor(final double a[], final double b[]) {
        final int length = a.length;
        final double c[] = new double[length];
        for (int i = 0; i < length; ++i) {
            double r = 0;
            for (int j = i; j < length; ++j) {
                r += a[j] * b[j - i];
            }
            c[i] = r;
        }
        return c;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] createArray(final Class<T> cl, final int n) {
        return (T[]) Array.newInstance(cl, n);
    }

    public static double[] derivative(final double a[], final int gap) {
        final double r[] = new double[a.length - 2 * gap];
        for (int i = gap; i < a.length - gap; ++i) {
            r[i - gap] = (a[i + gap] - a[i - gap]) / 2.;
        }
        return r;
    }

    public static double determinant(final double[][] mat) {
        double result = 0;
        if (mat.length == 1) {
            result = mat[0][0];
            return result;
        }
        if (mat.length == 2) {
            result = mat[0][0] * mat[1][1] - mat[0][1] * mat[1][0];
            return result;
        }
        for (int i = 0; i < mat[0].length; i++) {
            final double temp[][] = new double[mat.length - 1][mat[0].length - 1];
            for (int j = 1; j < mat.length; j++) {
                for (int k = 0; k < mat[0].length; k++) {
                    if (k < i) {
                        temp[j - 1][k] = mat[j][k];
                    } else if (k > i) {
                        temp[j - 1][k - 1] = mat[j][k];
                    }
                }
            }
            result += mat[0][i] * Math.pow(-1, i) * determinant(temp);
        }
        return result;
    }

    public static int determinant(final int[][] mat) {
        int result = 0;
        if (mat.length == 1) {
            result = mat[0][0];
            return result;
        }
        if (mat.length == 2) {
            result = mat[0][0] * mat[1][1] - mat[0][1] * mat[1][0];
            return result;
        }
        for (int i = 0; i < mat[0].length; i++) {
            final int temp[][] = new int[mat.length - 1][mat[0].length - 1];
            for (int j = 1; j < mat.length; j++) {
                for (int k = 0; k < mat[0].length; k++) {
                    if (k < i) {
                        temp[j - 1][k] = mat[j][k];
                    } else if (k > i) {
                        temp[j - 1][k - 1] = mat[j][k];
                    }
                }
            }
            result += mat[0][i] * Math.pow(-1, i) * determinant(temp);
        }
        return result;
    }

    public static long determinant(final long[][] mat) {
        long result = 0;
        if (mat.length == 1) {
            result = mat[0][0];
            return result;
        }
        if (mat.length == 2) {
            result = mat[0][0] * mat[1][1] - mat[0][1] * mat[1][0];
            return result;
        }
        for (int i = 0; i < mat[0].length; i++) {
            final long temp[][] = new long[mat.length - 1][mat[0].length - 1];
            for (int j = 1; j < mat.length; j++) {
                for (int k = 0; k < mat[0].length; k++) {
                    if (k < i) {
                        temp[j - 1][k] = mat[j][k];
                    } else if (k > i) {
                        temp[j - 1][k - 1] = mat[j][k];
                    }
                }
            }
            result += mat[0][i] * Math.pow(-1, i) * determinant(temp);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] difference(final T a[], final T b[]) {
        if (a.length == 0 || b.length == 0) {
            return copy(a);
        }
        final ArrayList<T> iv = new ArrayList<T>();
        for (int i = 0; i < a.length; ++i) {
            final T obj = a[i];
            boolean found = false;
            for (int j = 0; j < b.length; ++j) {
                if (b[j].equals(obj)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                iv.add(a[i]);
            }
        }
        return iv.toArray((T[]) Array.newInstance(a[0].getClass(), 0));
    }

    public static double dot(final double a[], final double b[]) {
        final int ar = a.length;
        double c = 0;
        for (int i = 0; i < ar; i++) {
            c += a[i] * b[i];
        }
        return c;
    }

    public static int dot(final int a[], final int b[]) {
        final int ar = a.length;
        int c = 0;
        for (int i = 0; i < ar; i++) {
            c += a[i] * b[i];
        }
        return c;
    }

    public static boolean equals(final byte[] a, final byte[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; ++i) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(final int[] a, final int[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; ++i) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(final long[] a, final long[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; ++i) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    public static <T> T find(final T keys[], final T value[], final T key) {
        for (int i = 0; i < keys.length; ++i) {
            if (keys[i].equals(key)) {
                return value[i];
            }
        }
        return null;
    }

    public static double[] gendIndex(final int length) {
        final double arr[] = new double[length];
        for (int i = 0; i < length; ++i) {
            arr[i] = i;
        }
        return arr;
    }

    public static int[] genIndex(final int length) {
        return genIndex(0, length);
    }

    public static int[] genIndex(final int low, final int length) {
        final int arr[] = new int[length];
        for (int i = 0; i < length; ++i) {
            arr[i] = low + i;
        }
        return arr;
    }

    public static int[] genMixer(final long r[]) {
        final int arr[] = new int[r.length / 2];
        final LinkedList<Integer> list = new LinkedList<Integer>();
        for (int i = 0; i < arr.length; ++i) {
            list.add(i);
        }
        for (int i = 0; i < arr.length; ++i) {
            final double d = (((long) next(r[2 * i], 26) << 27) + next(r[2 * i + 1], 27))
                    / (double) (1L << 53);
            arr[i] = list.remove((int) (d * list.size()));
        }
        return arr;
    }

    public static byte[] genRandom(final int length) {
        final Random r = new Random();
        final byte arr[] = new byte[length];
        r.nextBytes(arr);
        return arr;
    }

    public static byte[] genRandomByteArray(final long[][] mix, final long vec[], final long prime,
                                            final int length) {
        long arr[] = vec;
        final byte b[] = new byte[length];
        for (int i = 0; i < vec.length; ++i) {
            arr = Arr.modTimes(mix, arr, prime);
            b[i] = (byte) (arr[0] >>> 16);
        }
        System.arraycopy(arr, 0, vec, 0, length);
        return b;
    }

    public static long[] genRandomLongArray(final long[][] mix, final long vec[], final long prime,
                                            final int length) {
        long arr[] = vec;
        final long b[] = new long[length];
        for (int i = 0; i < length; ++i) {
            arr = Arr.modTimes(mix, arr, prime);
            b[i] = arr[0] >>> 16;
        }
        System.arraycopy(arr, 0, vec, 0, arr.length);
        return b;
    }

    public static int[] getColumn(final int[][] r, final int i) {
        final int c[] = new int[r.length];
        for (int j = 0; j < c.length; ++j) {
            c[j] = r[j][i];
        }
        return c;
    }

    public static double[] getColumn(final double[][] r, final int i) {
        final double c[] = new double[r.length];
        for (int j = 0; j < c.length; ++j) {
            c[j] = r[j][i];
        }
        return c;
    }

    public static byte getNibble(final byte arr[], final int pos) {
        final int ind = pos / 2;
        final int offset = pos % 2;
        final int val = arr[ind] & 0xFF;
        if (offset == 0) {
            return (byte) (val >> 4);
        }
        return (byte) (val & 0xF);
    }

    public static SortedMap<Comparable<?>, Integer> histogram(final Comparable<?> c[]) {
        final SortedMap<Comparable<?>, Integer> map = new TreeMap<Comparable<?>, Integer>();
        for (final Comparable<?> x : c) {
            Integer count = map.get(x);
            if (count == null) {
                count = new Integer(0);
            }
            map.put(x, count + 1);
        }
        return map;
    }

    public static double[] histogram(final double a[], final int n, double min, double max) {
        double binSize = (max - min) / n;
        double h[] = new double[n + 1];
        for (int i = 0; i < a.length; ++i) {
            int ind = (int) ((a[i] - min) / binSize);
            if (ind > 0 && ind < h.length) {
                ++h[ind];
            }
        }
        return h;
    }

    public static double[][] identity(final int n) {
        final double c[][] = new double[n][n];
        for (int i = 0; i < n; ++i) {
            c[i][i] = 1;
        }
        return c;
    }

    public static int[][] iidentity(final int n) {
        final int c[][] = new int[n][n];
        for (int i = 0; i < n; ++i) {
            c[i][i] = 1;
        }
        return c;
    }

    public static int indexOf(final byte[] source, final int sourceOffset, final int sourceCount,
                              final byte[] target, final int targetOffset, final int targetCount, int fromIndex) {
        if (fromIndex >= sourceCount) {
            return targetCount == 0 ? sourceCount : -1;
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (targetCount == 0) {
            return fromIndex;
        }
        final byte first = target[targetOffset];
        final int max = sourceOffset + sourceCount - targetCount;
        for (int i = sourceOffset + fromIndex; i <= max; i++) {
            /* Look for first character. */
            if (source[i] != first) {
                while (++i <= max && source[i] != first) {
                    ;
                }
            }
            /* Found first character, now look at the rest of v2 */
            if (i <= max) {
                int j = i + 1;
                final int end = j + targetCount - 1;
                for (int k = targetOffset + 1; j < end && source[j] == target[k]; j++, k++) {
                    ;
                }
                if (j == end) {
                    /* Found whole string. */
                    return i - sourceOffset;
                }
            }
        }
        return -1;
    }

    public static int indexOf(final ByteBuffer source, final int sourceOffset,
                              final int sourceCount, final byte[] target, final int targetOffset, final int targetCount,
                              int fromIndex) {
        if (fromIndex >= sourceCount) {
            return targetCount == 0 ? sourceCount : -1;
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (targetCount == 0) {
            return fromIndex;
        }
        final byte first = target[targetOffset];
        final int max = sourceOffset + sourceCount - targetCount;
        for (int i = sourceOffset + fromIndex; i <= max; i++) {
            /* Look for first character. */
            if (source.get(i) != first) {
                while (++i <= max && source.get(i) != first) {
                    ;
                }
            }
            /* Found first character, now look at the rest of v2 */
            if (i <= max) {
                int j = i + 1;
                final int end = j + targetCount - 1;
                for (int k = targetOffset + 1; j < end && source.get(j) == target[k]; j++, k++) {
                    ;
                }
                if (j == end) {
                    /* Found whole string. */
                    return i - sourceOffset;
                }
            }
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] insert(final T[] arr, final T obj, final int pos) {
        final T r[] = (T[]) Array.newInstance(arr[0].getClass(), arr.length + 1);
        System.arraycopy(arr, 0, r, 0, pos);
        r[pos] = obj;
        System.arraycopy(arr, pos, r, pos + 1, arr.length - pos);
        return r;
    }

    public static int[] insert(final int[] arr, final int obj, final int pos) {
        final int r[] = new int[arr.length + 1];
        System.arraycopy(arr, 0, r, 0, pos);
        r[pos] = obj;
        System.arraycopy(arr, pos, r, pos + 1, arr.length - pos);
        return r;
    }

    public static char[] insert(final char[] arr, final char obj, final int pos) {
        final char r[] = new char[arr.length + 1];
        System.arraycopy(arr, 0, r, 0, pos);
        r[pos] = obj;
        System.arraycopy(arr, pos, r, pos + 1, arr.length - pos);
        return r;
    }

    public static long[] insert(final long[] arr, final long obj, final int pos) {
        final long r[] = new long[arr.length + 1];
        System.arraycopy(arr, 0, r, 0, pos);
        r[pos] = obj;
        System.arraycopy(arr, pos, r, pos + 1, arr.length - pos);
        return r;
    }

    public static double[] insert(final double[] arr, final double obj, final int pos) {
        final double r[] = new double[arr.length + 1];
        System.arraycopy(arr, 0, r, 0, pos);
        r[pos] = obj;
        System.arraycopy(arr, pos, r, pos + 1, arr.length - pos);
        return r;
    }

    public static double[] integrate(final double a[]) {
        final double r[] = new double[a.length];
        for (int i = 1; i < a.length; ++i) {
            r[i] = r[i - 1] + a[i];
        }
        return r;
    }

    public static int[] invertIndex(final int ind[]) {
        final int b[] = new int[ind.length];
        for (int i = 0; i < ind.length; ++i) {
            b[ind[i]] = i;
        }
        return b;
    }

    public static <T extends Comparable<T>> boolean isSorted(final T a[]) {
        for (int i = 1; i < a.length; ++i) {
            if (a[i].compareTo(a[i - 1]) < 0) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean isSorted(final T a[], final Comparator<T> c) {
        for (int i = 1; i < a.length; ++i) {
            if (c.compare(a[i], a[i - 1]) < 0) {
                return false;
            }
        }
        return true;
    }

    public static int levenshtein(CharSequence a, CharSequence b) {
        final int[][] d = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) {
            d[i][0] = i;
        }
        for (int j = 0; j <= b.length(); j++) {
            d[0][j] = j;
        }
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                d[i][j] = minimum(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1]
                        + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1));
            }
        }
        return d[a.length()][b.length()];
    }

    public static long[][] lidentity(final int n) {
        final long c[][] = new long[n][n];
        for (int i = 0; i < n; ++i) {
            c[i][i] = 1;
        }
        return c;
    }

    public static double max(final double arr[]) {
        double max = -Double.MAX_VALUE;
        for (int i = 0; i < arr.length; ++i) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    public static double max(final double[][] arr) {
        double max = -Double.MAX_VALUE;
        for (int i = 0; i < arr.length; ++i) {
            final double r = max(arr[i]);
            if (r > max) {
                max = r;
            }
        }
        return max;
    }

    public static double max(final double[][][] a) {
        double max = -Double.MAX_VALUE;
        for (int i = 0; i < a.length; ++i) {
            final double r = max(a[i]);
            if (r > max) {
                max = r;
            }
        }
        return max;
    }

    public static int max(final int arr[]) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; ++i) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    public static int max(final int[][] arr) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; ++i) {
            final int r = max(arr[i]);
            if (r > max) {
                max = r;
            }
        }
        return max;
    }

    private static int[] merge(final int[] a, final int[] b, final double[] src) {
        final int ind[] = new int[a.length + b.length];
        int pos1 = 0;
        int pos2 = 0;
        int pos = 0;
        while (pos1 < a.length && pos2 < b.length) {
            if (src[a[pos1]] < src[b[pos2]]) {
                ind[pos] = a[pos1];
                ++pos;
                ++pos1;
            } else if (src[a[pos1]] > src[b[pos2]]) {
                ind[pos] = b[pos2];
                ++pos;
                ++pos2;
            } else {
                ind[pos] = a[pos1];
                ++pos;
                ++pos1;
                ind[pos] = b[pos2];
                ++pos;
                ++pos2;
            }
        }
        while (pos1 < a.length) {
            ind[pos] = a[pos1];
            ++pos;
            ++pos1;
        }
        while (pos2 < b.length) {
            ind[pos] = b[pos2];
            ++pos;
            ++pos2;
        }
        return ind;
    }

    private static <T> int[] merge(final int[] a, final int[] b, final T[] src, final Comparator<T> c) {
        final int ind[] = new int[a.length + b.length];
        int pos1 = 0;
        int pos2 = 0;
        int pos = 0;
        while (pos1 < a.length && pos2 < b.length) {
            final int comp = c.compare(src[a[pos1]], src[b[pos2]]);
            if (comp < 0) {
                ind[pos] = a[pos1];
                ++pos;
                ++pos1;
            } else if (comp > 0) {
                ind[pos] = b[pos2];
                ++pos;
                ++pos2;
            } else {
                ind[pos] = a[pos1];
                ++pos;
                ++pos1;
                ind[pos] = b[pos2];
                ++pos;
                ++pos2;
            }
        }
        while (pos1 < a.length) {
            ind[pos] = a[pos1];
            ++pos;
            ++pos1;
        }
        while (pos2 < b.length) {
            ind[pos] = b[pos2];
            ++pos;
            ++pos2;
        }
        return ind;
    }

    private static int[] mergeSort(final double[] src, final int low, final int high) {
        final int length = high - low;
        if (length < 7) {
            final int ind[] = genIndex(low, length);
            for (int i = 0; i < length; ++i) {
                for (int j = i + 1; j < length; ++j) {
                    if (src[ind[j]] < src[ind[i]]) {
                        swap(ind, i, j);
                    }
                }
            }
            return ind;
        }
        final int mid = low + high >> 1;
        final int[] ind1 = mergeSort(src, low, mid);
        final int[] ind2 = mergeSort(src, mid, high);
        return merge(ind1, ind2, src);
    }

    private static <T> int[] mergeSort(final T[] src, final int low, final int high,
                                       final Comparator<T> c) {
        final int length = high - low;
        if (length < 7) {
            final int ind[] = genIndex(low, length);
            for (int i = 0; i < length; ++i) {
                for (int j = i + 1; j < length; ++j) {
                    if (c.compare(src[ind[j]], src[ind[i]]) < 0) {
                        swap(ind, i, j);
                    }
                }
            }
            return ind;
        }
        final int mid = low + high >> 1;
        final int[] ind1 = mergeSort(src, low, mid, c);
        final int[] ind2 = mergeSort(src, mid, high, c);
        return merge(ind1, ind2, src, c);
    }

    public static double min(final double arr[]) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < arr.length; ++i) {
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return min;
    }

    public static double min(final double[][] a) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < a.length; ++i) {
            final double r = min(a[i]);
            if (r < min) {
                min = r;
            }
        }
        return min;
    }

    public static double min(final double[][][] a) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < a.length; ++i) {
            final double r = min(a[i]);
            if (r < min) {
                min = r;
            }
        }
        return min;
    }

    public static int min(final int arr[]) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; ++i) {
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return min;
    }

    public static int min(final int[][] a) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < a.length; ++i) {
            final int r = min(a[i]);
            if (r < min) {
                min = r;
            }
        }
        return min;
    }

    private static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    public static byte mix(final int mixer[], final byte in) {
        return (byte) mixer[in];
    }

    public static int[] mod(final int[] arr, final int m) {
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = arr[i] % m;
        }
        return arr;
    }

    public static long[] mod(final long[] arr, final long m) {
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = arr[i] % m;
        }
        return arr;
    }

    public static long[][] mod(final long[][] arr, final long m) {
        for (int i = 0; i < arr.length; ++i) {
            final long[] vec = arr[i];
            for (int j = 0; j < arr.length; ++j) {
                vec[j] = vec[j] % m;
            }
        }
        return arr;
    }

    public static long[][] modInverse(final long[][] arr, final long prime) {
        return Arr.modPower(arr, BigInteger.valueOf(prime).pow(arr.length).subtract(
                BigInteger.valueOf(2)), prime);
    }

    public static long[][] modPower(final long[][] arr, final BigInteger pow, final long mod) {
        final BigInteger TWO = BigInteger.valueOf(2L);
        if (pow.equals(BigInteger.ZERO)) {
            return Arr.lidentity(arr.length);
        }
        if (pow.mod(TWO).equals(BigInteger.valueOf(0))) {
            return modPower(Arr.modTimes(arr, arr, mod), pow.divide(TWO), mod);
        }
        return Arr.modTimes(arr, modPower(Arr.modTimes(arr, arr, mod), pow.divide(TWO), mod), mod);
    }

    public static long[][] modPower(final long[][] arr, final long pow, final long mod) {
        return modPower(arr, BigInteger.valueOf(pow), mod);
    }

    public static long[][] modTimes(final long a[][], final long b[][], final long mod) {
        final int ar = a.length;
        final int ac = a[0].length;
        final int bc = b[0].length;
        final long[][] c = new long[ar][bc];
        final long[] bcolj = new long[ac];
        for (int j = 0; j < bc; j++) {
            for (int k = 0; k < ac; k++) {
                bcolj[k] = b[k][j];
            }
            for (int i = 0; i < ar; i++) {
                final long[] arowi = a[i];
                long s = 0;
                for (int k = 0; k < ac; k++) {
                    s = (s + arowi[k] * bcolj[k]) % mod;
                }
                c[i][j] = s;
            }
        }
        return c;
    }

    public static long[] modTimes(final long a[][], final long b[], final long mod) {
        final int ar = a.length;
        final int ac = a[0].length;
        final long[] c = new long[ar];
        for (int i = 0; i < ar; i++) {
            final long[] arowi = a[i];
            long s = 0;
            for (int k = 0; k < ac; k++) {
                s = (s + arowi[k] * b[k]) % mod;
            }
            c[i] = s;
        }
        return c;
    }

    static int next(long seed, final int bits) {
        final long multiplier = 0x5DEECE66DL;
        final long addend = 0xBL;
        final long mask = (1L << 48) - 1;
        seed = seed * multiplier + addend & mask;
        return (int) (seed >>> 48 - bits);
    }

    public static void normalize(final double[] d) {
        final double max = max(d);
        final double min = min(d);
        final double r = max - min;
        for (int i = 0; i < d.length; ++i) {
            d[i] = (d[i] - min) / r;
        }
    }

    public static void normalize(final double[][] d) {
        final double max = max(d);
        final double min = min(d);
        final double r = max - min;
        for (int i = 0; i < d.length; ++i) {
            for (int j = 0; j < d[0].length; ++j) {
                d[i][j] = (d[i][j] - min) / r;
            }
        }
    }

    public static void normalize(final double[][] d, final double factor) {
        final double max = max(d);
        final double min = min(d);
        final double r = max - min;
        for (int i = 0; i < d.length; ++i) {
            for (int j = 0; j < d[0].length; ++j) {
                d[i][j] = (int) ((d[i][j] - min) * factor / r);
            }
        }
    }

    public static void normalize(final double[][][] d) {
        final double max = max(d);
        final double min = min(d);
        final double r = max - min;
        for (int k = 0; k < d.length; ++k) {
            for (int i = 0; i < d[0].length; ++i) {
                for (int j = 0; j < d[0][0].length; ++j) {
                    d[k][i][j] = (int) ((d[k][i][j] - min) / r);
                }
            }
        }
    }

    public static void normalize(final double[][][] d, final double factor) {
        final double max = max(d);
        final double min = min(d);
        final double r = max - min;
        for (int k = 0; k < d.length; ++k) {
            for (int i = 0; i < d[0].length; ++i) {
                for (int j = 0; j < d[0][0].length; ++j) {
                    d[k][i][j] = (int) ((d[k][i][j] - min) * factor / r);
                }
            }
        }
    }

    public static void normalize(final int norm, final int[] d) {
        final double max = max(d);
        final double min = min(d);
        final double r = max - min;
        for (int i = 0; i < d.length; ++i) {
            d[i] = (int) (norm * (d[i] - min) / r);
        }
    }

    public static double[][] phase(final double[][] r, final double[][] im) {
        final double t[][] = new double[r.length][r[0].length];
        for (int i = 0; i < t.length; ++i) {
            for (int j = 0; j < t[i].length; ++j) {
                t[i][j] = Math.atan2(im[i][j], r[i][j]);
            }
        }
        return t;
    }

    public static double[][] power(final double a[][], final BigInteger b) {
        final BigInteger TWO = BigInteger.valueOf(2L);
        if (b.equals(BigInteger.ZERO)) {
            return Arr.identity(a.length);
        }
        if (b.mod(TWO).equals(BigInteger.valueOf(0))) {
            return power(Arr.times(a, a), b.divide(TWO));
        }
        return Arr.times(a, power(Arr.times(a, a), b.divide(TWO)));
    }

    public static double[][] power(final double a[][], final long b) {
        return power(a, BigInteger.valueOf(b));
    }

    public static int[][] power(final int a[][], final BigInteger b) {
        final BigInteger TWO = BigInteger.valueOf(2L);
        if (b.equals(BigInteger.ZERO)) {
            return Arr.iidentity(a.length);
        }
        if (b.mod(TWO).equals(BigInteger.valueOf(0))) {
            return power(Arr.times(a, a), b.divide(TWO));
        }
        return Arr.times(a, power(Arr.times(a, a), b.divide(TWO)));
    }

    public static int[][] power(final int a[][], final long b) {
        return power(a, BigInteger.valueOf(b));
    }

    public static long[][] power(final long a[][], final BigInteger b) {
        final BigInteger TWO = BigInteger.valueOf(2L);
        if (b.equals(BigInteger.ZERO)) {
            return Arr.lidentity(a.length);
        }
        if (b.mod(TWO).equals(BigInteger.valueOf(0))) {
            return power(Arr.times(a, a), b.divide(TWO));
        }
        return Arr.times(a, power(Arr.times(a, a), b.divide(TWO)));
    }

    public static long[][] power(final long a[][], final long b) {
        return power(a, BigInteger.valueOf(b));
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] remove(final T[] arr, final int pos) {
        final T r[] = (T[]) Array.newInstance(arr[0].getClass(), arr.length - 1);
        System.arraycopy(arr, 0, r, 0, pos);
        System.arraycopy(arr, pos + 1, r, pos, arr.length - pos - 1);
        return r;
    }

    public static int[] reorder(final int arr[], final int ind[]) {
        final int a[] = new int[ind.length];
        for (int i = 0; i < ind.length; ++i) {
            a[i] = arr[ind[i]];
        }
        return a;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] reorder(final T arr[], final int ind[]) {
        final T[] a = (T[]) createArray(arr[0].getClass(), ind.length);
        for (int i = 0; i < ind.length; ++i) {
            a[i] = arr[ind[i]];
        }
        return a;
    }

    public static double[][] resample(final double x[], final double d[], final int n) {
        final double[] nx = new double[n];
        final double[] nd = new double[n];
        final double min = min(x);
        final double max = max(x);
        final double delta = (max - min) / n;
        for (int i = 0; i < n; ++i) {
            nx[i] = i * delta;
            int ind = Arrays.binarySearch(x, nx[i]);
            if (ind < 0) {
                ind = -ind - 1;
                if (ind < 0) {
                    nd[i] = d[ind];
                }
                if (ind >= d.length) {
                    nd[i] = d[d.length - 1];
                } else {
                    nd[i] = (d[ind] - d[ind - 1]) / (x[ind] - x[ind - 1]) * (nx[i] - x[ind - 1]);
                }
            } else {
                nd[i] = d[ind];
            }
        }
        return new double[][]{nx, nd};
    }

    public static byte[] resize(final byte[] b, final int length) {
        final byte r[] = new byte[length];
        if (length > b.length) {
            System.arraycopy(b, 0, r, 0, b.length);
        } else {
            System.arraycopy(b, 0, r, 0, length);
        }
        return r;
    }

    public static int[] resize(final int[] b, final int length) {
        final int r[] = new int[length];
        if (length > b.length) {
            System.arraycopy(b, 0, r, 0, b.length);
        } else {
            System.arraycopy(b, 0, r, 0, length);
        }
        return r;
    }

    public static void reverse(final byte[] b) {
        int left = 0;
        int right = b.length - 1;
        while (left < right) {
            final byte temp = b[left];
            b[left] = b[right];
            b[right] = temp;
            left++;
            right--;
        }
    }

    public static <T> void reverse(final T[] b) {
        int left = 0;
        int right = b.length - 1;
        while (left < right) {
            final T temp = b[left];
            b[left] = b[right];
            b[right] = temp;
            left++;
            right--;
        }
    }

    public static void reverse(final double[] b) {
        int left = 0;
        int right = b.length - 1;
        while (left < right) {
            final double temp = b[left];
            b[left] = b[right];
            b[right] = temp;
            left++;
            right--;
        }
    }

    public static void reverse(final int[] b) {
        int left = 0;
        int right = b.length - 1;
        while (left < right) {
            final int temp = b[left];
            b[left] = b[right];
            b[right] = temp;
            left++;
            right--;
        }
    }

    public static byte[] setAll(final byte arr[], final byte v) {
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = v;
        }
        return arr;
    }

    public static double[] setAll(final double arr[], final double v) {
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = v;
        }
        return arr;
    }

    public static float[] setAll(final float arr[], final float v) {
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = v;
        }
        return arr;
    }

    public static char[] setAll(final char arr[], final char v) {
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = v;
        }
        return arr;
    }

    public static int[] setAll(final int arr[], final int v) {
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = v;
        }
        return arr;
    }

    public static long[] setAll(final long arr[], final long v) {
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = v;
        }
        return arr;
    }

    public static <T> T[] setAll(final T[] arr, final T v) {
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = v;
        }
        return arr;
    }

    public static byte[] shift(final byte a[], int p) {
        final byte b[] = new byte[a.length];
        if (p < 0) {
            p = a.length + p;
        }
        int c = a.length - p;
        System.arraycopy(a, 0, b, p, c);
        System.arraycopy(a, c, b, 0, p);
        return b;
    }

    public static double[] shift(final double a[], int p) {
        final double b[] = new double[a.length];
        if (p < 0) {
            p = a.length + p;
        }
        int c = a.length - p;
        System.arraycopy(a, 0, b, p, c);
        System.arraycopy(a, c, b, 0, p);
        return b;
    }

    public static double[][] shift(final double[][] a, final int x, final int y) {
        final double[][] r = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; ++i) {
            int xoff = i + x;
            if (xoff < 0) {
                xoff = a.length + xoff;
            }
            if (xoff > a.length - 1) {
                xoff = xoff - a.length;
            }
            for (int j = 0; j < a[0].length; ++j) {
                int yoff = j + y;
                if (yoff < 0) {
                    yoff = a[0].length + yoff;
                }
                if (yoff > a[0].length - 1) {
                    yoff = yoff - a[0].length;
                }
                r[i][j] = a[xoff][yoff];
            }
        }
        return r;
    }

    public static float[] shift(final float a[], int p) {
        final float b[] = new float[a.length];
        if (p < 0) {
            p = a.length + p;
        }
        int c = a.length - p;
        System.arraycopy(a, 0, b, p, c);
        System.arraycopy(a, c, b, 0, p);
        return b;
    }

    public static int[] shift(final int a[], int p) {
        final int b[] = new int[a.length];
        if (p < 0) {
            p = a.length + p;
        }
        int c = a.length - p;
        System.arraycopy(a, 0, b, p, c);
        System.arraycopy(a, c, b, 0, p);
        return b;
    }

    public static long[] shift(final long a[], int p) {
        final long b[] = new long[a.length];
        if (p < 0) {
            p = a.length + p;
        }
        int c = a.length - p;
        System.arraycopy(a, 0, b, p, c);
        System.arraycopy(a, c, b, 0, p);
        return b;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] shift(final T a[], int p) {
        final T b[] = (T[]) Arr.createArray(a[0].getClass(), a.length);
        if (p < 0) {
            p = a.length + p;
        }
        int c = a.length - p;
        System.arraycopy(a, 0, b, p, c);
        System.arraycopy(a, c, b, 0, p);
        return b;
    }

    public static double[] smooth(final double x[], final int ws) {
        final double[] r = new double[x.length];
        for (int i = 0; i < r.length; ++i) {
            for (int j = i - ws / 2; j < i + ws / 2; ++j) {
                if (j > 0 && j < r.length) {
                    r[i] += x[j];
                }
            }
            r[i] /= ws;
        }
        return r;
    }

    public static long[] sortByIndex(final long[] a, final int[] ind) {
        final long arr[] = new long[a.length];
        for (int i = 0; i < a.length; ++i) {
            arr[i] = a[ind[i]];
        }
        return arr;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] sortedDifference(final T a[], final T b[], final Comparator<T> c) {
        if (a.length == 0 || b.length == 0) {
            return copy(a);
        }
        final ArrayList<T> iv = new ArrayList<T>();
        for (int i = 0; i < a.length; ++i) {
            if (Arrays.binarySearch(b, a[i], c) < 0) {
                iv.add(a[i]);
            }
        }
        return iv.toArray((T[]) Array.newInstance(a[0].getClass(), 0));
    }

    public static int[] sortedIndex(final double a[]) {
        return mergeSort(a, 0, a.length);
    }

    public static <T extends Comparable<T>> int[] sortedIndex(final T a[]) {
        return sortedIndex(a, new Comparator<T>() {
            public int compare(final T o1, final T o2) {
                return o1.compareTo(o2);
            }
        });
    }

    public static <T> int[] sortedIndex(final T a[], final Comparator<T> c) {
        return mergeSort(a, 0, a.length, c);
    }

    public static Object[] sortedIntersection(final Object arr[][]) {
        Object r[] = arr[0];
        for (int i = 1; i < arr.length; ++i) {
            r = sortedIntersection(r, arr[i]);
        }
        return r;
    }

    public static Object[] sortedIntersection(final Object a[], final Object b[]) {
        final ArrayList<Object> iv = new ArrayList<Object>();
        if (a.length < b.length) {
            for (int i = 0; i < a.length; ++i) {
                if (Arrays.binarySearch(b, a[i]) > -1) {
                    iv.add(a[i]);
                }
            }
        } else {
            for (int i = 0; i < b.length; ++i) {
                if (Arrays.binarySearch(a, b[i]) > -1) {
                    iv.add(b[i]);
                }
            }
        }
        return iv.toArray();
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] sortedIntersection(final T a[], final T b[], final Comparator<T> c) {
        final ArrayList<T> iv = new ArrayList<T>();
        if (a.length < b.length) {
            for (int i = 0; i < a.length; ++i) {
                if (Arrays.binarySearch(b, a[i], c) > -1) {
                    iv.add(a[i]);
                }
            }
        } else {
            for (int i = 0; i < b.length; ++i) {
                if (Arrays.binarySearch(a, b[i], c) > -1) {
                    iv.add(b[i]);
                }
            }
        }
        return (T[]) iv.toArray();
    }


    @SuppressWarnings("unchecked")
    public static <T> T[] sortedUnion(final T a[], final T b[], final Comparator<T> c) {
        final ArrayList<T> iv = new ArrayList<T>();
        int pos1 = 0;
        int pos2 = 0;
        while (pos1 < a.length && pos2 < b.length) {
            if (c.compare(a[pos1], b[pos2]) < 0) {
                iv.add(a[pos1]);
                ++pos1;
            } else if (c.compare(a[pos1], b[pos2]) > 0) {
                iv.add(b[pos2]);
                ++pos2;
            } else {
                iv.add(a[pos1]);
                ++pos1;
                ++pos2;
            }
        }
        while (pos1 < a.length) {
            iv.add(a[pos1]);
            ++pos1;
        }
        while (pos2 < b.length) {
            iv.add(b[pos2]);
            ++pos2;
        }
        return (T[]) iv.toArray();
    }

    public static byte[] subset(final byte a[], final int start, final int length) {
        final byte b[] = new byte[length];
        System.arraycopy(a, start, b, 0, length);
        return b;
    }

    public static char[] subset(final char a[], final int start, final int length) {
        final char b[] = new char[length];
        System.arraycopy(a, start, b, 0, length);
        return b;
    }

    public static double[] subset(final double a[], final int start, final int length) {
        final double b[] = new double[length];
        System.arraycopy(a, start, b, 0, length);
        return b;
    }

    public static double[][] subset(final double[][] a, final int xoff, final int xlen,
                                    final int yoff, final int ylen) {
        final double b[][] = new double[xlen][];
        for (int i = 0; i < xlen; ++i) {
            b[i] = new double[ylen];
            System.arraycopy(a[i + xoff], yoff, b[i], 0, ylen);
        }
        return b;
    }

    public static int[] subset(final int a[], final int start, final int length) {
        final int b[] = new int[length];
        System.arraycopy(a, start, b, 0, length);
        return b;
    }

    public static int[][] subset(final int[][] a, final int xoff, final int xlen, final int yoff,
                                 final int ylen) {
        final int b[][] = new int[xlen][];
        for (int i = 0; i < xlen; ++i) {
            b[i] = new int[ylen];
            System.arraycopy(a[i + xoff], yoff, b[i], 0, ylen);
        }
        return b;
    }

    public static long[] subset(final long a[], final int start, final int length) {
        final long b[] = new long[length];
        System.arraycopy(a, start, b, 0, length);
        return b;
    }

    public static byte[] subtract(final byte a[], final byte b[]) {
        final int length = a.length;
        final byte[] c = new byte[length];
        for (int i = 0; i < length; i++) {
            c[i] = (byte) (a[i] - b[i]);
        }
        return c;
    }

    public static byte[][] subtract(final byte a[][], final byte b[][]) {
        final int length = a.length;
        final byte[][] c = new byte[length][];
        for (int i = 0; i < length; i++) {
            c[i] = subtract(a[i], b[i]);
        }
        return c;
    }

    public static double[][] subtract(final double a[][], final double b[][]) {
        final int length = a.length;
        final double[][] c = new double[length][];
        for (int i = 0; i < length; i++) {
            c[i] = subtract(a[i], b[i]);
        }
        return c;
    }

    public static double[][][] subtract(final double a[][][], final double b[][][]) {
        final int length = a.length;
        final double[][][] c = new double[length][][];
        for (int i = 0; i < length; i++) {
            c[i] = subtract(a[i], b[i]);
        }
        return c;
    }

    public static double[] subtract(final double a[], final double b[]) {
        final int length = a.length;
        final double[] c = new double[length];
        for (int i = 0; i < length; i++) {
            c[i] = a[i] - b[i];
        }
        return c;
    }

    public static double[][][] subtract(final double a[][][], final double b) {
        final int length = a.length;
        final double[][][] c = new double[length][][];
        for (int i = 0; i < length; i++) {
            c[i] = subtract(a[i], b);
        }
        return c;
    }

    public static double[][] subtract(final double a[][], final double b) {
        final int length = a.length;
        final double[][] c = new double[length][];
        for (int i = 0; i < length; i++) {
            c[i] = subtract(a[i], b);
        }
        return c;
    }

    public static double[] subtract(final double[] a, final double b) {
        final int length = a.length;
        final double[] c = new double[length];
        for (int i = 0; i < length; i++) {
            c[i] = a[i] - b;
        }
        return c;
    }

    public static int[][] subtract(final int a[][], final int b) {
        final int length = a.length;
        final int[][] c = new int[length][];
        for (int i = 0; i < length; i++) {
            c[i] = subtract(a[i], b);
        }
        return c;
    }

    public static int[] subtract(final int a[], final int b[]) {
        final int length = a.length;
        final int[] c = new int[length];
        for (int i = 0; i < length; i++) {
            c[i] = a[i] - b[i];
        }
        return c;
    }

    public static int[][] subtract(final int a[][], final int b[][]) {
        final int length = a.length;
        final int[][] c = new int[length][];
        for (int i = 0; i < length; i++) {
            c[i] = subtract(a[i], b[i]);
        }
        return c;
    }

    public static int[] subtract(final int[] a, final int b) {
        final int length = a.length;
        final int[] c = new int[length];
        for (int i = 0; i < length; i++) {
            c[i] = a[i] - b;
        }
        return c;
    }

    public static double sum2(final double[][][] a) {
        double sum = 0;
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a[i].length; ++j) {
                for (int k = 0; k < a[i][j].length; ++k) {
                    sum += a[i][j][k] * a[i][j][k];
                }
            }
        }
        return sum;
    }

    public static double sum(final double[][][] a) {
        double sum = 0;
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a[i].length; ++j) {
                for (int k = 0; k < a[i][j].length; ++k) {
                    sum += a[i][j][k];
                }
            }
        }
        return sum;
    }

    public static double sum2(final double[][] a) {
        double sum = 0;
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a[i].length; ++j) {
                sum += a[i][j] * a[i][j];
            }
        }
        return sum;
    }

    public static double sum(final double[][] a) {
        double sum = 0;
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a[i].length; ++j) {
                sum += a[i][j];
            }
        }
        return sum;
    }

    public static double sum(final double[] a) {
        double sum = 0;
        for (int i = 0; i < a.length; ++i) {
            sum += a[i];
        }
        return sum;
    }

    public static void swap(final int x[], final int a, final int b) {
        final int t = x[a];
        x[a] = x[b];
        x[b] = t;
    }

    public static double[] times(final double a[], final double b[]) {
        final int ar = a.length;
        final double[] c = new double[ar];
        for (int i = 0; i < ar; i++) {
            c[i] = a[i] * b[i];
        }
        return c;
    }

    public static double[] times(final double a[], final double b) {
        final int ar = a.length;
        final double[] c = new double[ar];
        for (int i = 0; i < ar; i++) {
            c[i] = a[i] * b;
        }
        return c;
    }

    public static double[][] times(final double a[][], final double b) {
        final int ar = a.length;
        final int ac = a[0].length;
        final double[][] c = new double[ar][ac];
        for (int i = 0; i < ar; i++) {
            for (int k = 0; k < ac; k++) {
                c[i][k] = a[i][k] * b;
            }
        }
        return c;
    }

    public static double[] times(final double a[][], final double b[]) {
        final int ar = a.length;
        final int ac = a[0].length;
        final double[] c = new double[ar];
        for (int i = 0; i < ar; i++) {
            final double[] arowi = a[i];
            double s = 0;
            for (int k = 0; k < ac; k++) {
                s += arowi[k] * b[k];
            }
            c[i] = s;
        }
        return c;
    }

    public static double[][] times(final double a[][], final double b[][]) {
        final int ar = a.length;
        final int ac = a[0].length;
        final int bc = b[0].length;
        final double[][] c = new double[ar][bc];
        final double[] bcolj = new double[ac];
        for (int j = 0; j < bc; j++) {
            for (int k = 0; k < ac; k++) {
                bcolj[k] = b[k][j];
            }
            for (int i = 0; i < ar; i++) {
                final double[] arowi = a[i];
                double s = 0;
                for (int k = 0; k < ac; k++) {
                    s += arowi[k] * bcolj[k];
                }
                c[i][j] = s;
            }
        }
        return c;
    }

    public static int[] times(final int a[], final int b) {
        final int ar = a.length;
        final int[] c = new int[ar];
        for (int i = 0; i < ar; i++) {
            c[i] = a[i] * b;
        }
        return c;
    }

    public static int[][] times(final int a[][], final int b) {
        final int ar = a.length;
        final int ac = a[0].length;
        final int[][] c = new int[ar][ac];
        for (int i = 0; i < ar; i++) {
            for (int k = 0; k < ac; k++) {
                c[i][k] = a[i][k] * b;
            }
        }
        return c;
    }

    public static int[] times(final int a[][], final int b[]) {
        final int ar = a.length;
        final int ac = a[0].length;
        final int[] c = new int[ar];
        for (int i = 0; i < ar; i++) {
            final int[] arowi = a[i];
            int s = 0;
            for (int k = 0; k < ac; k++) {
                s += arowi[k] * b[k];
            }
            c[i] = s;
        }
        return c;
    }

    public static int[][] times(final int a[][], final int b[][]) {
        final int ar = a.length;
        final int ac = a[0].length;
        final int bc = b[0].length;
        final int[][] c = new int[ar][bc];
        final int[] bcolj = new int[ac];
        for (int j = 0; j < bc; j++) {
            for (int k = 0; k < ac; k++) {
                bcolj[k] = b[k][j];
            }
            for (int i = 0; i < ar; i++) {
                final int[] arowi = a[i];
                int s = 0;
                for (int k = 0; k < ac; k++) {
                    s += arowi[k] * bcolj[k];
                }
                c[i][j] = s;
            }
        }
        return c;
    }

    public static long[] times(final long a[], final long b) {
        final int ar = a.length;
        final long[] c = new long[ar];
        for (int i = 0; i < ar; i++) {
            c[i] = a[i] * b;
        }
        return c;
    }

    public static long[][] times(final long a[][], final long b) {
        final int ar = a.length;
        final int ac = a[0].length;
        final long[][] c = new long[ar][ac];
        for (int i = 0; i < ar; i++) {
            for (int k = 0; k < ac; k++) {
                c[i][k] = a[i][k] * b;
            }
        }
        return c;
    }

    public static long[] times(final long a[][], final long b[]) {
        final int ar = a.length;
        final int ac = a[0].length;
        final long[] c = new long[ar];
        for (int i = 0; i < ar; i++) {
            final long[] arowi = a[i];
            long s = 0;
            for (int k = 0; k < ac; k++) {
                s += arowi[k] * b[k];
            }
            c[i] = s;
        }
        return c;
    }

    public static long[][] times(final long a[][], final long b[][]) {
        final int ar = a.length;
        final int ac = a[0].length;
        final int bc = b[0].length;
        final long[][] c = new long[ar][bc];
        final long[] bcolj = new long[ac];
        for (int j = 0; j < bc; j++) {
            for (int k = 0; k < ac; k++) {
                bcolj[k] = b[k][j];
            }
            for (int i = 0; i < ar; i++) {
                final long[] arowi = a[i];
                long s = 0;
                for (int k = 0; k < ac; k++) {
                    s += arowi[k] * bcolj[k];
                }
                c[i][j] = s;
            }
        }
        return c;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(final Iterator<T> iter, final Class<T> cl) {
        final ArrayList<T> vec = new ArrayList<T>();
        while (iter.hasNext()) {
            vec.add(iter.next());
        }
        return vec.toArray((T[]) Array.newInstance(cl, vec.size()));
    }

    public static String iteratorToString(final Iterator<Object> arr) {
        final StringBuffer buf = new StringBuffer();
        buf.append('{');
        int last = 0;
        while (arr.hasNext()) {
            Object obj = arr.next();
            if (obj != null && obj.getClass().isArray()) {
                obj = arrayToString(obj);
            }
            buf.append(obj);
            buf.append(',');
            final int length = buf.length();
            if (length - last > 100) {
                buf.append('\n');
                last = length;
            }
        }
        buf.append("}");
        return buf.toString();
    }

    public static String arrayToString(final Object arr) {
        final StringBuffer buf = new StringBuffer();
        final int l = Array.getLength(arr);
        buf.append('{');
        int last = 0;
        for (int i = 0; i < l; ++i) {
            Object obj = Array.get(arr, i);
            if (obj != null && obj.getClass().isArray()) {
                obj = arrayToString(obj);
            }
            buf.append(obj);
            if (i < l - 1) {
                buf.append(',');
            }
            final int length = buf.length();
            if (length - last > 100) {
                buf.append('\n');
                last = length;
            }
        }
        buf.append("}");
        return buf.toString();
    }

    public static double[] vecNormalize(final double d[]) {
        final double v = Math.sqrt(dot(d, d));
        return times(d, 1 / v);
    }

    public static int[] xor(final int[] a, final int[] b) {
        final int xor[] = new int[a.length];
        for (int i = 0; i < xor.length; ++i) {
            xor[i] = a[i] ^ b[i];
        }
        return xor;
    }

    public double[] solve(final double m[][], final double y[]) {
        final int N = y.length;
        // Gaussian elimination with partial pivoting
        for (int i = 0; i < N; i++) {
            // find pivot row and swap
            int max = i;
            for (int j = i + 1; j < N; j++) {
                if (Math.abs(m[j][i]) > Math.abs(m[max][i])) {
                    max = j;
                }
            }
            // TODO !!!!!!
            // A.swap(i, max);
            // b.swap(i, max);
            // singular
            if (m[i][i] == 0.0) {
                throw new RuntimeException("Matrix is singular.");
            }
            // pivot within b
            for (int j = i + 1; j < N; j++) {
                y[j] -= y[i] * m[j][i] / m[i][i];
            }
            // pivot within A
            for (int j = i + 1; j < N; j++) {
                final double r = m[j][i] / m[i][i];
                for (int k = i + 1; k < N; k++) {
                    m[j][k] -= m[i][k] * r;
                }
                m[j][i] = 0.0;
            }
        }
        // back substitution
        final double x[] = new double[N];
        for (int j = N - 1; j >= 0; j--) {
            double t = 0.0;
            for (int k = j + 1; k < N; k++) {
                t += m[j][k] * x[k];
            }
            x[j] = (y[j] - t) / m[j][j];
        }
        return x;
    }

    public static double[][] maxdif(double[][] a, double[][] b) {
        double r[][] = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a[i].length; ++j) {
                r[i][j] = Math.abs(a[i][j]) > Math.abs(b[i][j]) ? a[i][j] : b[i][j];
            }
        }
        return r;
    }

    public static double[][] variance(double[][] a) {
        double r[][] = new double[a.length][a[0].length];
        for (int i = 1; i < a.length - 1; ++i) {
            for (int j = 1; j < a[i].length - 1; ++j) {
                r[i][j] = calcVariance(a, i, j);
            }
        }
        return r;
    }

    public static int[][] variance(int[][] a) {
        int r[][] = new int[a.length][a[0].length];
        for (int i = 1; i < a.length - 1; ++i) {
            for (int j = 1; j < a[i].length - 1; ++j) {
                r[i][j] = (int) calcVariance(a, i, j);
            }
        }
        return r;
    }

    private static double calcVariance(int[][] a, int i, int j) {
        double g = (a[i - 1][j - 1] + a[i][j - 1] + a[i + 1][j - 1] + a[i - 1][j] + a[i][j]
                + a[i + 1][j] + a[i - 1][j + 1] + a[i][j + 1] + a[i + 1][j + 1]) / 9.;
        double h = (a[i - 1][j - 1] * a[i - 1][j - 1] + a[i][j - 1] * a[i][j - 1] + a[i + 1][j - 1]
                * a[i + 1][j - 1] + a[i - 1][j] * a[i - 1][j] + a[i][j] * a[i][j] + a[i + 1][j]
                * a[i + 1][j] + a[i - 1][j + 1] * a[i - 1][j + 1] + a[i][j + 1] * a[i][j + 1] + a[i + 1][j + 1]
                * a[i + 1][j + 1]) / 9.;
        return h - (g * g);
    }

    private static double calcVariance(double[][] a, int i, int j) {
        double g = (a[i - 1][j - 1] + a[i][j - 1] + a[i + 1][j - 1] + a[i - 1][j] + a[i][j]
                + a[i + 1][j] + a[i - 1][j + 1] + a[i][j + 1] + a[i + 1][j + 1]) / 9.;
        double h = (a[i - 1][j - 1] * a[i - 1][j - 1] + a[i][j - 1] * a[i][j - 1] + a[i + 1][j - 1]
                * a[i + 1][j - 1] + a[i - 1][j] * a[i - 1][j] + a[i][j] * a[i][j] + a[i + 1][j]
                * a[i + 1][j] + a[i - 1][j + 1] * a[i - 1][j + 1] + a[i][j + 1] * a[i][j + 1] + a[i + 1][j + 1]
                * a[i + 1][j + 1]) / 9.;
        return h - (g * g);
    }

}
