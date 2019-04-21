package ru.nsu.fit.g16207.melnikov.calculate;

import ru.nsu.fit.g16207.melnikov.configuration.Configuration;

public class FunctionColor implements CalculateColor {
    private Configuration configuration;

    public FunctionColor(Configuration configuration) {
        this.configuration = configuration;
    }
    /**
     * return color of pixel by coordinates in functions area
     * */
    @Override
    public int getColor(double value) {
        int index = configuration.getIndexOfColorByValue(value);
        return configuration.getAllColors()[index].getRGB();
    }
}
