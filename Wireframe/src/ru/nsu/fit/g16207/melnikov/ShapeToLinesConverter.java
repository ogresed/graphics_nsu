package ru.nsu.fit.g16207.melnikov;
import ru.nsu.fit.g16207.melnikov.bspline.BSplineFunction;
import java.util.ArrayList;
public class ShapeToLinesConverter {
    private ShapeToLinesConverter() {

    }
    private static ArrayList<Line<Point3D<Double, Double, Double>>> lines;
    public static ArrayList<Line<Point3D<Double, Double, Double>>> toLines(BSplineFunction bSplineFunction, int n, int m, int k,
                                                                           double a, double b, double d, double c) {
        lines = new ArrayList<>();
        double stepRotate = (360.0 / ((double) m));
        double[] lenSteps = getSteps(a, b, n * k, 1.0);
        double[] rotateSteps = getSteps(c, d, m, 360.0);
        Point<Double, Double> currentPoint = bSplineFunction.getValue(lenSteps[0]);
        Point<Double, Double> nextPoint = bSplineFunction.getValue(lenSteps[1]);
        if (null == currentPoint || nextPoint == null) {
            return lines;
        }
        ArrayList<Integer> drawingIndexes = getDrawingIndexes(lenSteps, n);
        int drawingIndex = 0;
        for(int i = 0; i < lenSteps.length - 1; i++) {
            //double length = lenSteps[i];
            boolean bool = drawingIndexes.get(drawingIndex) == i;
            if(bool) {
                drawingIndex++;
            }
            drawOnCircle(rotateSteps, currentPoint, nextPoint, bool);
            currentPoint = nextPoint;
            if (i < lenSteps.length - 2) {
                nextPoint = bSplineFunction.getValue(lenSteps[i + 2]);
            }
        }

        //==========================================================
        drawOnCircle(rotateSteps, currentPoint, nextPoint, true);
        //==========================================================
        return lines;
    }

    private static void drawOnCircle(
            double[] rotateSteps,
            Point<Double, Double> currentPoint,
            Point<Double, Double> nextPoint,
            boolean bool
    ) {
        for(int i = 0; i < rotateSteps.length; i++) {
            double rotate = rotateSteps[i];
            double currentAngleInRadians = Math.toRadians(rotate);
            double nextAngleInRadians = Math.toRadians(rotateSteps[(i+1) % rotateSteps.length]);
            Point3D<Double, Double, Double> startPoint = new Point3D<>(
                    Math.abs(currentPoint.getY())* Math.cos(currentAngleInRadians),
                    Math.abs(currentPoint.getY())* Math.sin(currentAngleInRadians),
                    currentPoint.getX()
            );
            if (bool && i != rotateSteps.length - 1) {
                Point3D<Double, Double, Double> rotateEndPoint = new Point3D<>(
                        Math.abs(currentPoint.getY()) * Math.cos(nextAngleInRadians),
                        Math.abs(currentPoint.getY()) * Math.sin(nextAngleInRadians),
                        currentPoint.getX()
                );
                lines.add(new Line<>(startPoint, rotateEndPoint));
            }
            Point3D<Double, Double, Double> lengthEndPoint = new Point3D<>(
                    Math.abs(nextPoint.getY()) * Math.cos(currentAngleInRadians),
                    Math.abs(nextPoint.getY()) * Math.sin(currentAngleInRadians),
                    nextPoint.getX()
            );
            lines.add(new Line<>(startPoint, lengthEndPoint));
        }
    }

    private static ArrayList<Integer> getDrawingIndexes(double[] lenSteps, int n) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(0);
        double offset = 1.0 / n;
        double current = 0;
        while (current <= lenSteps[0]) {
            current += offset;
        }
        for(int i = 1; i < lenSteps.length; i++) {
            if((i != lenSteps.length - 1) && Math.abs(current - lenSteps[i]) < 0.0000001) {
                list.add(i);
                current+=offset;
            }
        }
        list.add(lenSteps.length - 1);
        return list;
    }

    private static double[] getSteps(double start, double end, int number, double whole) {
        double delenie = (whole / (number*1.0));
        double current = 0;
        while(current <= start) {
            current+=delenie;
        }
        int numberOfdelenie = 0;
        double tmp = current;
        while(tmp < end) {
            numberOfdelenie++;
            tmp+=delenie;
        }
        double[] array = new double[2 + numberOfdelenie];
        array[0] = start;
        array[numberOfdelenie + 1] = end;
        for(int i = 0; i<numberOfdelenie; i++) {
            array[i+1] = current;
            current+=delenie;
        }
        return array;
    }
}