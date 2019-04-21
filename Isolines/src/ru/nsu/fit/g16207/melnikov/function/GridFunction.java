package ru.nsu.fit.g16207.melnikov.function;

public class GridFunction {
    private double minFunction;
    private double maxFunction;
    private double[][] values;
    private double[] keyValues;
    private int numberOfKeyValues;//n

    private double offsetOfKeyValue;
    private double offsetOfHorizontal;
    private double offsetOfVertical;
    //changeable parameters
    private int numberHorizontalDotes = 10;//k
    private int numberVerticalDotes = 10;//m
    private double leftBorder;//a
    private double rightBorder;//b
    private double lowerBorder;//c
    private double highBorder;//d
    private Function function;

    public GridFunction(double leftBorder, double rightBorder, double lowerBorder, double highBorder) {
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
        this.lowerBorder = lowerBorder;
        this.highBorder = highBorder;
        function =  new Function(leftBorder, rightBorder, lowerBorder, highBorder) {
            @Override
            public double function(double x, double y) {
                return x*y;
            }
        };
    }

    public void setValuesNumber(int valuesNumber) {
        this.numberOfKeyValues = valuesNumber;
        setOffsetOfKeyValue();
        createKeyValues();
    }

    public void setGrid(int xSize, int ySize, int maxGridSize) {
        this.numberHorizontalDotes = xSize;
        this.numberVerticalDotes = ySize;
        setOffsetOfHorizontal();
        setOffsetOfVertical();
        setMaxMinValue(maxGridSize);
    }

    private void setMaxMinValue(int maxGridSize) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double x = leftBorder;
        double y = lowerBorder;
        double verticalOffset = Math.abs(highBorder- lowerBorder) / (double) (maxGridSize);
        double horizontalOffset = Math.abs(rightBorder - leftBorder) / (double) (maxGridSize);
        //т.к. учтены крайние значения то -1
        for(int i = 0; i < maxGridSize; i++) {
            for(int j = 0; j < maxGridSize; j++) {
                double tmp = function(x, y);
                if(tmp  < min) {
                    min = tmp;
                }
                if(tmp > max) {
                    max = tmp;
                }
                x+= horizontalOffset;
            }
            x = leftBorder;
            y+= verticalOffset;
        }
        minFunction = min;
        maxFunction = max;
    }

    public void makeFunction() {
        values = new double[numberVerticalDotes][numberHorizontalDotes];
        double x = leftBorder;
        double y = lowerBorder;
        for(int i = 0; i < numberVerticalDotes; i++) {
            for(int j = 0; j < numberHorizontalDotes; j++) {
                values[i][j] = function(x, y);
                x+= offsetOfHorizontal;
            }
            x = leftBorder;
            y+= offsetOfVertical;
        }
    }

    private void createKeyValues() {
        // + 2 потому что прибавляем крайние значения функции
        int numberOfValuesOfFunction = numberOfKeyValues+2;
        keyValues = new double[numberOfValuesOfFunction];
        keyValues[0] = minFunction;
        keyValues[numberOfValuesOfFunction - 1] = maxFunction;
        double previousValue = minFunction;
        for(int i = 1; i < numberOfValuesOfFunction - 1; i++) {
            previousValue += offsetOfKeyValue;
            keyValues[i] = previousValue;
        }
    }

    private void setOffsetOfHorizontal() {
        offsetOfHorizontal = Math.abs(rightBorder - leftBorder) / (double)(numberHorizontalDotes - 1);
    }

    private void setOffsetOfVertical() {
        offsetOfVertical = Math.abs(highBorder - lowerBorder) / (double)(numberVerticalDotes - 1);
    }

    private void setOffsetOfKeyValue() {
        double numberOfColors = (double)numberOfKeyValues + 1.0;
        offsetOfKeyValue = (maxFunction - minFunction) / numberOfColors;
    }

    public double[][] getValues() {
        return values;
    }

    public double[] getKeyValues() {
        return keyValues;
    }

    public int getNumberHorizontalDotes() {
        return numberHorizontalDotes;
    }

    public int getNumberVerticalDotes() {
        return numberVerticalDotes;
    }

    public int getNumberOfKeyValues() {
        return numberOfKeyValues;
    }

    public double getRightBorder() {
        return rightBorder;
    }

    public double getLeftBorder() {
        return leftBorder;
    }

    public double getHighBorder() {
        return highBorder;
    }

    public double getLowerBorder() {
        return lowerBorder;
    }

    public double getOffsetOfHorizontal() {
        return offsetOfHorizontal;
    }

    public double getOffsetOfVertical() {
        return offsetOfVertical;
    }

    public double getMinFunction() {
        return minFunction;
    }

    public double getMaxFunction() {
        return maxFunction;
    }

    public double getOffsetOfKeyValue() {
        return offsetOfKeyValue;
    }

    public boolean needUpdate(int left, int right, int lower, int height) {
        return !(left == leftBorder && right == rightBorder &&
                lower == lowerBorder && height == highBorder);
    }

    private double function(double x, double y) {
        return function.function(x, y);
    }

    public Function getFunction() {
        return function;
    }
}
