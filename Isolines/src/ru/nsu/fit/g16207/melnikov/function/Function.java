package ru.nsu.fit.g16207.melnikov.function;

public class Function {
    private final double COEFFICIENT = 50.0;
    private double minFunction;// = -COEFFICIENT;
    private double maxFunction;// = COEFFICIENT;
    private double[][] values;
    private double[] keyValues;
    private int numberOfKeyValues;//n
    private int numberHorizontallyDotes;//k
    private int numberVerticallyDotes;//m
    //хорошие значения ***Border это -10 и 10
    private double leftBorder;//a
    private double rightBorder;//b
    private double lowerBorder;//c
    private double highBorder;//d
    private double offsetOfKeyValue;
    private double offsetOfHorizontally;
    private double offsetOfVertically;

    public Function(double leftBorder, double rightBorder, double lowerBorder, double highBorder) {
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
        this.lowerBorder = lowerBorder;
        this.highBorder = highBorder;

    }

    public void setValuesNumber(int valuesNumber) {
        this.numberOfKeyValues = valuesNumber;
        setOffsetOfKeyValue();
        createKeyValues();
    }

    public void setGrid(int xSize, int ySize) {
        this.numberHorizontallyDotes = xSize;
        this.numberVerticallyDotes = ySize;
        setOffsetOfHorizontally();
        setOffsetOfVertically();
        setMaxMinValue();
    }

    private void setMaxMinValue() {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double x = leftBorder;
        double y = lowerBorder;
        //т.к. учтены крайние значения то -1
        for(int i = 0; i < numberVerticallyDotes - 1; i++) {
            for(int j = 0; j < numberHorizontallyDotes - 1; j++) {
                double tmp = operation(x, y);
                if(tmp  < min) {
                    min = tmp;
                }
                if(tmp > max) {
                    max = tmp;
                }
                x+=offsetOfHorizontally;
            }
            x = leftBorder;
            y+=offsetOfVertically;
        }
        minFunction = min;// - 1.0;
        maxFunction = max;// + 1.0;
    }

    public void makeFunction() {
        values = new double[numberVerticallyDotes][numberHorizontallyDotes];
        double x = leftBorder;
        double y = lowerBorder;
        for(int i = 0; i < numberVerticallyDotes; i++) {
            for(int j = 0; j < numberHorizontallyDotes; j++) {
                values[i][j] = getNearestValue(operation(x, y));
                x+=offsetOfHorizontally;
            }
            x = leftBorder;
            y+=offsetOfVertically;
        }
    }

    private double getNearestValue(double v) {
        int indexWithMinimumDifference = 0;
        double difference = Math.abs(v - keyValues[indexWithMinimumDifference]);
        for(int i = 1; i < keyValues.length; i++) {
            double tmpDifference = Math.abs(v - keyValues[i]);
            if(tmpDifference < difference) {
                difference = tmpDifference;
                indexWithMinimumDifference = i;
            }
        }
        return keyValues[indexWithMinimumDifference];
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

    private void setOffsetOfHorizontally() {
        //так как крайние значения тоже рассматриваются, то - 1
        offsetOfHorizontally = Math.abs(rightBorder - leftBorder) / (double)(numberHorizontallyDotes - 1);
    }

    private void setOffsetOfVertically() {
        offsetOfVertically = Math.abs(highBorder - lowerBorder) / (double)(numberVerticallyDotes - 1);
    }

    private void setOffsetOfKeyValue() {
        double numberOfColors = (double)numberOfKeyValues + 1.0;
        offsetOfKeyValue = (maxFunction - minFunction) / numberOfColors;
    }

    public double operation(double x, double y) {
        return Math.sin(x) + x*y+ Math.cos(y);//(COEFFICIENT * Math.cos((x + y) / 3));
    }

    public double discreteFunction(double x, double y) {
        double value = operation(x, y);
        return getNearestValue(value);
    }

    public double[][] getValues() {
        return values;
    }

    public double[] getKeyValues() {
        return keyValues;
    }

    public int getNumberHorizontallyDotes() {
        return numberHorizontallyDotes;
    }

    public void setNumberHorizontallyDotes(int numberHorizontallyDotes) {
        this.numberHorizontallyDotes = numberHorizontallyDotes;
    }

    public int getNumberVerticallyDotes() {
        return numberVerticallyDotes;
    }

    public void setNumberVerticallyDotes(int numberVerticallyDotes) {
        this.numberVerticallyDotes = numberVerticallyDotes;
    }

    public int getNumberOfKeyValues() {
        return numberOfKeyValues;
    }

    public void setNumberOfKeyValues(int numberOfKeyValues) {
        this.numberOfKeyValues = numberOfKeyValues;
    }

    public double getRightBorder() {
        return rightBorder;
    }

    public void setRightBorder(int rightBorder) {
        this.rightBorder = rightBorder;
    }

    public double getLeftBorder() {
        return leftBorder;
    }

    public void setLeftBorder(int leftBorder) {
        this.leftBorder = leftBorder;
    }

    public double getHighBorder() {
        return highBorder;
    }

    public void setHighBorder(int highBorder) {
        this.highBorder = highBorder;
    }

    public double getLowerBorder() {
        return lowerBorder;
    }

    public void setLowerBorder(int lowerBorder) {
        this.lowerBorder = lowerBorder;
    }

    public double getOffsetOfHorizontally() {
        return offsetOfHorizontally;
    }

    public double getOffsetOfVertically() {
        return offsetOfVertically;
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
}
