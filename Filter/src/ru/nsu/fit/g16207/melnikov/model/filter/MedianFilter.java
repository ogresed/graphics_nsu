package ru.nsu.fit.g16207.melnikov.model.filter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;


public class MedianFilter implements Filter {
    private static final int NEIGHBORS_COUNT = 25;

    @Override
    public BufferedImage process(BufferedImage bufferedImage) {
        BufferedImage result = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        int[][] coefficients = {{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {0, 0}, {0, 1}, {-1, 1}, {0, 1}, {1, 1},
                {-2, -2}, {-1, -2}, {0, -2}, {1, -2}, {2, -2}, {-2, -1}, {2, -1},
                {-2, 2}, {-1, 2}, {0, 2}, {1, 2}, {2, 2}, {-2, 1}, {2, 1},
                {-2, 0}, {2, 0}};

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int[] redNeighbors = new int[NEIGHBORS_COUNT];
                int[] greenNeighbors = new int[NEIGHBORS_COUNT];
                int[] blueNeighbors = new int[NEIGHBORS_COUNT];

                for (int k = 0; k < NEIGHBORS_COUNT; k++) {
                    if (i + coefficients[k][0] < 0 || i + coefficients[k][0] >= width
                            || j + coefficients[k][1] < 0 || j + coefficients[k][1] >= height) {
                        continue;
                    }
                    Color oldColor = new Color(bufferedImage.getRGB(i + coefficients[k][0], j + coefficients[k][1]));
                    redNeighbors[k] = oldColor.getRed();
                    greenNeighbors[k] = oldColor.getGreen();
                    blueNeighbors[k] = oldColor.getBlue();
                }
                Arrays.sort(redNeighbors);
                Arrays.sort(greenNeighbors);
                Arrays.sort(blueNeighbors);

                int medianIndex = NEIGHBORS_COUNT / 2 + 1;
                Color newColor = new Color(redNeighbors[medianIndex], greenNeighbors[medianIndex], blueNeighbors[medianIndex]);
                result.setRGB(i, j, newColor.getRGB());
            }
        }

        return result;
    }
}
