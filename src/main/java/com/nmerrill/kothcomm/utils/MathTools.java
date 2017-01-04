package com.nmerrill.kothcomm.utils;

import org.eclipse.collections.api.DoubleIterable;
import org.eclipse.collections.api.IntIterable;
import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.MutableIntList;


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
    public static MutableList<Integer> distributeInt(int total, RichIterable<Integer> weights){
        return weights.collect(new Distribute(weights.sumOfDouble(Integer::doubleValue), total)::distribute).toList();
    }

    public static MutableList<Integer> distribute(int total, RichIterable<Double> weights){
        return weights.collect(new Distribute(weights.sumOfDouble(Double::doubleValue), total)::distribute).toList();
    }

    public static MutableIntList distribute(int total, IntIterable weights){
        return weights.asLazy().collectInt(new Distribute(weights.sum(), total)::distribute).toList();
    }

    public static MutableIntList distribute(int total, DoubleIterable weights){
        return weights.asLazy().collectInt(new Distribute(weights.sum(), total)::distribute).toList();
    }

    private static class Distribute {
        private double remainingWeight;
        private int remainingDistribution;
        public Distribute(double totalWeight, int totalDistribution){
            this.remainingWeight = totalWeight;
            this.remainingDistribution = totalDistribution;
        }

        public int distribute(double weight){
            int distribution = (int) (weight * remainingDistribution/remainingWeight);
            remainingWeight -= weight;
            remainingDistribution -= distribution;
            return distribution;
        }
    }



}
