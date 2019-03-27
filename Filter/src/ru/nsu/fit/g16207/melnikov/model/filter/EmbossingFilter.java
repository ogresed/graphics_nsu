package ru.nsu.fit.g16207.melnikov.model.filter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EmbossingFilter extends ConvolutionFilter {
    private static final int brightnessShift = 128;

    public EmbossingFilter() {
        matrix = new int[]{0, 1, 0, 1, 0, -1, 0, -1, 0};
        matrixCoefficient = 1;
    }

    @Override
    public BufferedImage process(BufferedImage bufferedImage) {
        BufferedImage result = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int[] neighbors = addNeighbors(i, j, width, height, bufferedImage);
                int newPixelColor = applyMatrix(neighbors);
                newPixelColor = applyBrightnessShift(newPixelColor);
                result.setRGB(i, j, newPixelColor);
            }
        }

        return result;
    }

    private int applyBrightnessShift(int pixelColor) {
        Color color = new Color(pixelColor);

        int red = color.getRed() + brightnessShift;
        int green = color.getGreen() + brightnessShift;
        int blue = color.getBlue() + brightnessShift;

        int newRed = normalizeColorComponent(red);
        int newGreen = normalizeColorComponent(green);
        int newBlue = normalizeColorComponent(blue);

        return new Color(newRed, newGreen, newBlue).getRGB();
    }
}
