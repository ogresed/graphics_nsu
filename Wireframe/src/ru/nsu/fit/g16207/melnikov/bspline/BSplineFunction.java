package ru.nsu.fit.g16207.melnikov.bspline;

import ru.nsu.fit.g16207.melnikov.Point;
import ru.nsu.fit.g16207.melnikov.matrix.Matrix;
import ru.nsu.fit.g16207.melnikov.matrix.MatrixUtil;

import java.util.ArrayList;

public class BSplineFunction {
    private final ArrayList<Point<Double, Double>> points;
    private final Matrix matrix = new Matrix(new double[][] { {-1, 3, -3, 1},
            {3, -6, 3, 0},
            {-3, 0, 3, 0},
            {1, 4, 1, 0}});
    private static final double k = 1.0/6.0;

    public BSplineFunction(ArrayList<Point<Double, Double>> points) {
        this.points = points;
    }

    public Point<Double, Double> getValue(int i, double t) {
        Matrix giMatrixX = new Matrix(new double[][] {{points.get(i - 1).getX()},
                {points.get(i).getX()}, {points.get(i + 1).getX()}, {points.get(i + 2).getX()}});
        Matrix giMatrixY = new Matrix(new double[][]{{points.get(i - 1).getY()}, {points.get(i).getY()},
                {points.get(i + 1).getY()}, {points.get(i + 2).getY()}});

        Matrix tMatrix = new Matrix(new double[][] {{t * t * t, t * t, t, 1}});

        Matrix tempMatrix = MatrixUtil.multiply(tMatrix, MatrixUtil.multiply(matrix, k));

        Matrix xResultMatrix = MatrixUtil.multiply(tempMatrix, giMatrixX);
        Matrix yResultMatrix = MatrixUtil.multiply(tempMatrix, giMatrixY);

        return new Point<>(xResultMatrix.get(0, 0), yResultMatrix.get(0, 0));
    }

    public Point<Double, Double> getValue(double percentOfSplineLength) {
        Point<Integer, Double> iAndTPoint = getIAndT(percentOfSplineLength);

        if (iAndTPoint.getX() < getMinI() || iAndTPoint.getX() >= getMaxI()) {
            return null;
        }

        return getValue(iAndTPoint.getX(), iAndTPoint.getY());
    }

    public double getLength() {
        double totalLength = 0;
        for (int i = getMinI(); i < getMaxI(); ++i) {
            double localLength = 0;

            Point<Double, Double> prevPoint = null;
            for (double t = 0; t < 1; t = t + 0.01) {
                Point<Double, Double> p = getValue(i, t);
                if (t > 0) {
                    localLength += Math.sqrt(Math.pow(p.getX() - prevPoint.getX(), 2)
                            + Math.pow(p.getY() - prevPoint.getY(), 2));
                }
                prevPoint = p;
            }
            totalLength += localLength;
        }

        return totalLength;
    }

    public Point<Integer, Double> getIAndT(double u) {
        double appropriateLength = getLength() * u;

        double totalLength = 0;
        for (int i = getMinI(); i < getMaxI(); ++i) {

            Point<Double, Double> prevPoint = null;
            for (double t = 0; t < 1; t = t + 0.01f) {
                Point<Double, Double> p = getValue(i, t);
                if (t > 0) {
                    totalLength += Math.sqrt(Math.pow(p.getX() - prevPoint.getX(), 2)
                            + Math.pow(p.getY() - prevPoint.getY(), 2));

                    if (totalLength > appropriateLength) {
                        return new Point<>(i, t);
                    }
                }
                prevPoint = p;
            }
        }

        return new Point<>(getMaxI(), 0.0);
    }

    public int getMinI() {
        return 1;
    }

    public int getMaxI() {
        return points.size() - 2;
    }

}
