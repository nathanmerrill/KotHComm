package com.nmerrill.kothcomm.utils;


public final class MathTools {
    private MathTools(){}

    public static boolean inRange(int num, int min, int max){
        return num < max && num >= min;
    }

    public static boolean inRange(double num, double min, double max){
        return num < max && num >= min;
    }

    public static int modulo(int num, int mod){
        return ((num%mod)+mod)%mod;
    }

    public static int clamp(int num, int min, int max){
        return num > max ? max :
               num < min ? min : num;
    }

    public static double clamp(double num, double min, double max){
        return num > max ? max :
                num < min ? min : num;
    }

}
