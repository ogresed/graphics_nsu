package ru.nsu.fit.g16207.melnikov;

import ru.nsu.fit.g16207.melnikov.mf.MainFrame;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        new MainFrame().openFile(new File("FIT_16207_Melnikov_Isolines_Data/conf.txt"));
    }
    public static int multiplyByFraction (int numerator, int denominator, int value) {
        return (value + denominator - 1) / denominator * numerator;
    }
    public static int toPixel(double coordinate, int size, double min, double max) {
        return (int) Math.round (1.0 *size * (coordinate - min) / (max - min));
    }
    public static double getValueFromRasterToArea(int coordinate, int sizeOfRaster, double minVal, double maxVal) {
        double sizeArea = Math.abs(maxVal - minVal);
        double attitude = (double)coordinate / (double)sizeOfRaster;
        return sizeArea * attitude + minVal;
    }
}