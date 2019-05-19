package ru.nsu.fit.g16207.melnikov.matrix;

import java.util.Arrays;

public class Matrix {
    private final double data[][];

    public Matrix(double[][] data) {
        this.data = data;
    }

    int getHeight() {
        return data.length;
    }

    int getWidth() {
        return data[0].length;
    }

    public double get(int h, int w) {
        return data[h][w];
    }

    public void add(int h, int w, double value) {
        data[h][w] = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Matrix matrix = (Matrix) obj;
        return Arrays.deepEquals(data, matrix.data);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(data);
    }
}
