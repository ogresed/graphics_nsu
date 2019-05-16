package ru.nsu.fit.g16207.melnikov.settings;

import java.util.Arrays;

class Zoom {
    private double array[];
    private int index;
    private BSplinePanel splinePanel;

    Zoom(BSplinePanel setSplinePanel, int negativeNumber, int positiveNumber, double step) {
        splinePanel = setSplinePanel;
        array = createArray(negativeNumber, positiveNumber, step);
        System.out.println(Arrays.toString(array));
    }

    private double[] createArray(int negativeNumber, int positiveNumber, double step) {
        double array[] = new double[negativeNumber + positiveNumber];
        double offset = 1.0 / (negativeNumber + 1);
        double current = offset;
        int i;
        for(i = 0; i < negativeNumber; i++) {
            array[i] = current;
            current += offset;
        }
        array[i] = 1.0;
        index = i;
        current = step <= 1 ? step + 1 : step;
        for(i = i + 1; i < negativeNumber + positiveNumber; i++) {
            array[i] = current;
            current +=step;
        }
        return array;
    }

    public void positive() {
        if(index == array.length - 1) {
            return;
        }
        index++;
        splinePanel.setCoefficient(array[index]);
    }

    public void negative() {
        if(index == 0) {
            return;
        }
        index--;
        splinePanel.setCoefficient(array[index]);
    }

    public double currentValue() {
        return array[index];
    }
}
