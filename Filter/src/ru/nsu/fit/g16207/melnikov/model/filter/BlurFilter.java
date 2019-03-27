package ru.nsu.fit.g16207.melnikov.model.filter;

import java.awt.image.BufferedImage;

public class BlurFilter extends ConvolutionFilter {
    public BlurFilter() {
        matrix = new int[]{0, 1, 0, 1, 2, 1, 0, 1, 0};
        matrixCoefficient = 1.0 / 6;
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
                result.setRGB(i, j, newPixelColor);
            }
        }

        return result;
    }
}
