package ru.nsu.fit.g16207.melnikov.calculate;

import ru.nsu.fit.g16207.melnikov.configuration.Configuration;

import java.awt.*;

public class InterpolationColor implements CalculateColor {
    private Configuration configuration;

    public InterpolationColor(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public int getColor(double value) {
        double[] keyValues = configuration.getGridFunction().getKeyValues();
        Color[] colors = configuration.getAllColors();
        int index = configuration.getIndexOfColorByValue(value);
        Color color = colors[index];
        double keysOffset = configuration.getGridFunction().getOffsetOfKeyValue();
        double attitude = Math.abs((value - keyValues[index]) / keysOffset);
        double multiply = 1 - attitude;
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        if(index != 0) {
            red = Math.abs((int) (red * attitude + colors[index - 1].getRed() * multiply));
            green = Math.abs((int) (green * attitude + colors[index - 1].getGreen() * multiply));
            blue = Math.abs((int) (blue * attitude + colors[index - 1].getBlue() * multiply));
        }
        return (((red & 0xFF) << 16) |
                ((green & 0xFF) << 8)  |
                ((blue & 0xFF)));
    }
}
