package ru.nsu.fit.g16207.melnikov;

import ru.nsu.fit.g16207.melnikov.mf.MainFrame;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        /*Function function = new Function(4, 9, 9,
                -10, 10, -10, 10);
        System.out.println(Arrays.toString(function.getKeyValues()));*/
        new MainFrame().openFile(new File("FIT_16207_Melnikov_Isolines_Data/config.txt"));
    }
    public static int multiplyByFraction (int numerator, int denominator, int value) {
        return (value + denominator - 1) / denominator * numerator;
    }
}