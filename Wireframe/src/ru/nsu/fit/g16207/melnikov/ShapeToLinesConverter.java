package ru.nsu.fit.g16207.melnikov;

import ru.nsu.fit.g16207.melnikov.bspline.BSplineFunction;

import java.util.ArrayList;

public class ShapeToLinesConverter {
    private ShapeToLinesConverter() {

    }
    public static ArrayList<Line<Point3D<Double, Double, Double>>> toLines(BSplineFunction bSplineFunction, int n, int m, int k,
                                                                           double a, double b, double d, double c) {
        ArrayList<Line<Point3D<Double, Double, Double>>> lines = new ArrayList<>();

        double stepSplinePace = 1.0 / ((double) (n) * (double) k);
        double stepRotate = 360.0 / ((double) (m));

        Point<Double, Double> currentPoint = bSplineFunction.getValue(a);
        Point<Double, Double> nextPoint = bSplineFunction.getValue(a + stepSplinePace);

        if (null == currentPoint || nextPoint == null) {
            return lines;
        }
        int counter = 0;
        for (double i = a; i < b; i += stepSplinePace) {
            for (double j = c; j <= d; j += stepRotate) {
                double currentAngleInRadians = Math.toRadians(j);
                double nextAngleInRadians = Math.toRadians(j + stepRotate);
                Point3D<Double, Double, Double> startPoint = new Point3D<>(
                        Math.abs(currentPoint.getY()) * Math.cos(currentAngleInRadians),
                        Math.abs(currentPoint.getY()) * Math.sin(currentAngleInRadians),
                        currentPoint.getX()
                );

                if (j + stepRotate <= d + 0.1 && counter%k==0) {
                    Point3D<Double, Double, Double> rotateEndPoint = new Point3D<>(
                            Math.abs(currentPoint.getY()) * Math.cos(nextAngleInRadians),
                            Math.abs(currentPoint.getY()) * Math.sin(nextAngleInRadians),
                            currentPoint.getX()
                    );
                    lines.add(new Line<>(startPoint, rotateEndPoint));
                }

                if (null != nextPoint && i + stepSplinePace < b) {
                    Point3D<Double, Double, Double> lengthEndPoint = new Point3D<>(
                            Math.abs(nextPoint.getY()) * Math.cos(currentAngleInRadians),
                                    Math.abs(nextPoint.getY()) * Math.sin(currentAngleInRadians),
                            nextPoint.getX()
                    );
                    lines.add(new Line<>(startPoint, lengthEndPoint));
                }
            }
            currentPoint = nextPoint;
            nextPoint = bSplineFunction.getValue(i +  stepSplinePace);
            counter++;
        }

        //==================
        double offset = ((b*100) -5.0) %10 == 0 ? stepSplinePace : stepSplinePace*2;
        currentPoint = bSplineFunction.getValue(b-offset);
        nextPoint = bSplineFunction.getValue(b);
        for (double j = c; j <= d - stepRotate; j += stepRotate) {
            double currentAngleInRadians = Math.toRadians(j);
            double nextAngleInRadians = Math.toRadians(j + stepRotate);
            Point3D<Double, Double, Double> startPoint = new Point3D<>(
                    Math.abs(currentPoint.getY()) * Math.cos(currentAngleInRadians),
                    Math.abs(currentPoint.getY()) * Math.sin(currentAngleInRadians),
                    currentPoint.getX()
            );
            Point3D<Double, Double, Double> rotateEndPoint = new Point3D<>(
                    Math.abs(currentPoint.getY()) * Math.cos(nextAngleInRadians),
                    Math.abs(currentPoint.getY()) * Math.sin(nextAngleInRadians),
                    currentPoint.getX()
            );
            lines.add(new Line<>(startPoint, rotateEndPoint));
            Point3D<Double, Double, Double> lengthEndPoint = new Point3D<>(
                    Math.abs(nextPoint.getY()) * Math.cos(currentAngleInRadians),
                    Math.abs(nextPoint.getY()) * Math.sin(currentAngleInRadians),
                    nextPoint.getX()
            );
            //lines.add(new Line<>(startPoint, lengthEndPoint));
        }
        //==================

        return lines;
    }
}
