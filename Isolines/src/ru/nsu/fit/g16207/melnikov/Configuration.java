package ru.nsu.fit.g16207.melnikov;

import ru.nsu.fit.g16207.melnikov.function.Function;

import java.awt.*;

public class Configuration {
    private static final int MAX_GRID_SIZE = 300;
    private static final int MIN_GRID_SIZE = 4;
    private static final int MAX_VALUES_NUMBER = 500;
    private static final int MIN_VALUES_NUMBER = 1;
    private Color colors[];
    private int xSize;
    private int ySize;
    private int valuesNumber;
    private Function function;
    //private Map<Double, Color> map;
    public Configuration(Function function) {
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

    public Function getFunction() {
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
        function.setGrid(xSize, ySize);
    }

    /*private void createMap() {
        map = new HashMap<>();
        double keyValues[] = function.getKeyValues();
        for(int i = 0; i < valuesNumber; i++) {
            //+1 потому что двум крайним значения с обоих концов соотв. одни цвета
            map.put(keyValues[i+1], colors[i]);
        }
        //добавление тех самых концов
        map.put(keyValues[0], colors[0]);
        map.put(keyValues[valuesNumber + 1], colors[colors.length - 2]);
    }
*/
    /*public Color getColor(double value) {
        return map.get(value);
    }
*/
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
        //createMap();
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
}
