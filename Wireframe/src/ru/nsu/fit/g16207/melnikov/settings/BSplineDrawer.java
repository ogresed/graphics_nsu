package ru.nsu.fit.g16207.melnikov.settings;

import ru.nsu.fit.g16207.melnikov.pair.Pair;

import java.util.LinkedList;

class BSplineDrawer {
    private final double matrix[][] = {
            {-1, 3, -3, 1},
            {3, -6, 3, 0},
            {-3, 0, 3, 0},
            {1, 4, 1, 0}};
    private final double k = 1.0 / 6.0;
    private LinkedList<Pair<Double>> mainDots;
    private LinkedList<Pair<Double>> splineDots;

    BSplineDrawer(LinkedList<Pair<Double>> dots) {
        mainDots = dots;
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                matrix[i][j]*=k;
            }
        }
    }


    public LinkedList<Pair<Double>> createSpline() {
        double T[] = new double[4];
        double X[] = new double[4];
        double Y[] = new double[4];
        if(mainDots.size() < 4) {
            return null;
        }
        splineDots = new LinkedList<>();
        for(int i = 1; i < mainDots.size() - 2; i++) {
            for(double t = 0; t < 1; t+=0.1) { // может быть  начинать не с ноля
                double temp = 1.0;
                for(int j = 0; j < 4; j++) { // можеть быть обратный порядок
                    T[3-j] = temp;
                    temp*=t;

                    X[j] = mainDots.get(i-1 + j).getFirst();//мб изменить порядок
                    Y[j] = mainDots.get(i-1 + j).getSecond();//мб изменить порядок
                }
                //double result[] = multiplicationMatrix(T, matrix, X);

            }
        }
        return splineDots;
    }
}
