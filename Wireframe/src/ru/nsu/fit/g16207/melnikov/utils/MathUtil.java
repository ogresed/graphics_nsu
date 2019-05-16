package ru.nsu.fit.g16207.melnikov.utils;

public class MathUtil {
    public static int multiplyByFraction (int numerator, int denominator, int value) {
        return (value + denominator - 1) / denominator * numerator;
    }
}
