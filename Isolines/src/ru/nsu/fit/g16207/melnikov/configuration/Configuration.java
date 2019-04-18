package ru.nsu.fit.g16207.melnikov.configuration;

import ru.nsu.fit.g16207.melnikov.function.GridFunction;
import ru.nsu.fit.g16207.melnikov.mf.WrongValueException;

import java.awt.*;

public class Configuration {
    private static final int MAX_GRID_SIZE = 100;
    private static final int MIN_GRID_SIZE = 4;
    private static final int MAX_VALUES_NUMBER = 100;
    private static final int MIN_VALUES_NUMBER = 1;
    private Color colors[];
    private int xSize;
    private int ySize;
    private int valuesNumber;
    private GridFunction function;
    public Configuration(GridFunction function) {
        this.function = function;
    }

    public Color[] getAllColors() {
        return colors;
    }

    private void setColors(Color[] colors) {
        this.colors = colors;
    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }

    public int getValuesNumber() {
        return valuesNumber;
    }

    public GridFunction getGridFunction() {
        return function;
    }

    private void setValuesNumber(int valuesNumber) {
        this.valuesNumber = valuesNumber;
        function.setValuesNumber(valuesNumber);
        function.makeFunction();
    }

    private void setGridSize(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        int gridParameter = MAX_GRID_SIZE * 10;
        function.setGrid(xSize, ySize, gridParameter);
    }

    public void setConfiguration(Color[] colors, int xSize, int ySize, int valuesNumber) throws WrongValueException {
        if(xSize < MIN_GRID_SIZE || xSize > MAX_GRID_SIZE ||
                ySize < MIN_GRID_SIZE || ySize > MAX_GRID_SIZE ||
                valuesNumber < MIN_VALUES_NUMBER || valuesNumber > MAX_VALUES_NUMBER) {
            throw new WrongValueException();
        }
        setColors(colors);
        createValues(xSize, ySize, valuesNumber);
    }

    private void createValues(int xSize, int ySize, int valuesNumber) {
        setGridSize(xSize, ySize);
        setValuesNumber(valuesNumber);
    }

    public int getIndexOfColorByValue(double value) {
        if (value < function.getMinFunction()) {
            return 0;
        }
        // - 2 потому что в массиве цветов хранится еще цвет изолинии
        if (value > function.getMaxFunction()) {
            return colors.length - 2;
        }
        int index = 0;
        double keyValues[] = function.getKeyValues();
        while (value > keyValues[index + 1]) {
            index++;
        }
        return index;
    }

    public static int getMaxGridSize() {
        return MAX_GRID_SIZE;
    }

    public static int getMinGridSize() {
        return MIN_GRID_SIZE;
    }

    public void setFunction(GridFunction function) {
        this.function = function;
    }
    public void setGrid(int xS, int yS) {
        createValues(xS, yS, valuesNumber);
    }
}
