package ru.nsu.fit.g16207.melnikov.model.filter;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class ConvolutionFilter implements Filter {
    protected static final int NEIGHBORS_COUNT = 9;
    protected int[] matrix;
    protected double matrixCoefficient;

    protected int[] addNeighbors(int pixelX, int pixelY, int width, int height, BufferedImage image) {
        int[] result = new int[NEIGHBORS_COUNT];
        int[][] coefficients = {{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {0, 0}, {0, 1}, {-1, 1}, {0, 1}, {1, 1}};
        for (int i = 0; i < NEIGHBORS_COUNT; i++) {
            if (pixelX + coefficients[i][0] < 0 || pixelX + coefficients[i][0] >= width
                    || pixelY + coefficients[i][1] < 0 || pixelY + coefficients[i][1] >= height) {
                continue;
            }
            result[i] = image.getRGB(pixelX + coefficients[i][0], pixelY + coefficients[i][1]);
        }
        return result;
    }

    protected int applyMatrix(int[] neighbors) {
        int resultRed = 0;
        int resultGreen = 0;
        int resultBlue = 0;

        for (int i = 0; i < NEIGHBORS_COUNT; i++) {
            Color color = new Color(neighbors[i]);
            resultRed += matrixCoefficient * matrix[i] * color.getRed();
            resultGreen += matrixCoefficient * matrix[i] * color.getGreen();
            resultBlue += matrixCoefficient * matrix[i] * color.getBlue();
        }

        resultRed = normalizeColorComponent(resultRed);
        resultGreen = normalizeColorComponent(resultGreen);
        resultBlue = normalizeColorComponent(resultBlue);

        return new Color(resultRed, resultGreen, resultBlue).getRGB();
    }

    protected int normalizeColorComponent(int colorComponent) {
        if (colorComponent > 255) {
            colorComponent = 255;
        }
        if (colorComponent < 0) {
            colorComponent = 0;
        }
        return colorComponent;
    }
}
