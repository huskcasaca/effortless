package dev.huskuraft.effortless.api.math;

public final class MathUtils {

    public static final double E = Math.E;

    public static final double PI = Math.PI;

    private MathUtils() {
    }

    public static int max(int a, int b) {
        return Math.max(a, b);
    }

    public static long max(long a, long b) {
        return Math.max(a, b);
    }

    public static float max(float a, float b) {
        return Math.max(a, b);
    }

    public static double max(double a, double b) {
        return Math.max(a, b);
    }

    public static int min(int a, int b) {
        return Math.min(a, b);
    }

    public static long min(long a, long b) {
        return Math.min(a, b);
    }

    public static float min(float a, float b) {
        return Math.min(a, b);
    }

    public static double min(double a, double b) {
        return Math.min(a, b);
    }

    public static int abs(int a) {
        return Math.abs(a);
    }

    public static long abs(long a) {
        return Math.abs(a);
    }

    public static float abs(float a) {
        return Math.abs(a);
    }

    public static double abs(double a) {
        return Math.abs(a);
    }

    public static int clamp(int a, int b, int c) {
        return Math.min(Math.max(a, b), c);
    }

    public static long clamp(long a, long b, long c) {
        return Math.min(Math.max(a, b), c);
    }

    public static float clamp(float a, float b, float c) {
        if (a < b) return b;
        return Math.min(a, c);
    }

    public static double clamp(double a, double b, double c) {
        if (a < b) return b;
        return Math.min(a, c);
    }

    // FIXME: 17/10/23
    public static double floor(double a) {
        return Math.floor(a);
    }

    // FIXME: 17/10/23
    public static double ceil(double a) {
        return Math.ceil(a);
    }

    public static int round(float a) {
        return Math.round(a);
    }

    public static long round(double a) {
        return Math.round(a);
    }

    public static float sign(float a) {
        return Math.signum(a);
    }

    public static double sign(double a) {
        return Math.signum(a);
    }

    public static float sqrt(float a) {
        return (float) Math.sqrt(a);
    }

    public static double sqrt(double a) {
        return Math.sqrt(a);
    }


    public static float sin(float a) {
        return (float) Math.sin(a);
    }

    public static double sin(double a) {
        return Math.sin(a);
    }

    public static float cos(float a) {
        return (float) Math.cos(a);
    }

    public static double cos(double a) {
        return Math.cos(a);
    }

    public static double acos(double a) {
        return Math.acos(a);
    }

    public static double atan(double a) {
        return Math.atan(a);
    }

    public static double atan2(double a, double b) {
        return Math.atan2(a, b);
    }

    public static double rad(double a) {
        return Math.toRadians(a);
    }

    public static double deg(double a) {
        return Math.toDegrees(a);
    }

    public static boolean equal(float a, float b) {
        return Math.abs(b - a) < 1.0E-5F;
    }

    public static boolean equal(double a, double b) {
        return Math.abs(b - a) < 9.999999747378752E-6;
    }

    public static float lerp(float a, float b, float c) {
        return b + a * (c - b);
    }

    public static double lerp(double a, double b, double c) {
        return b + a * (c - b);
    }

    public static double lerp2(double d, double e, double f, double g, double h, double i) {
        return lerp(e, lerp(d, f, g), lerp(d, h, i));
    }

    public static double lerp3(double d, double e, double f, double g, double h, double i, double j, double k, double l, double m, double n) {
        return lerp(f, lerp2(d, e, g, h, i, j), lerp2(d, e, k, l, m, n));
    }

}
