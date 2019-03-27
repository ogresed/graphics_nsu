package ru.nsu.fit.g16207.melnikov.model.filter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FloydSteinbergDitheringFilter implements Filter {
    private int nRed;
    private int nGreen;
    private int nBlue;

    public FloydSteinbergDitheringFilter(int nRed, int nGreen, int nBlue) {
        this.nRed = nRed;
        this.nGreen = nGreen;
        this.nBlue = nBlue;
    }

    @Override
    public BufferedImage process(BufferedImage bufferedImage) {
        BufferedImage result = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        BufferedImage copy = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        copyImage(bufferedImage, copy);

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int oldColor = copy.getRGB(i, j);

                int alpha = (oldColor >> 24) & 0xff;
                int red = (oldColor >> 16) & 0xff;
                int green = (oldColor >> 8) & 0xff;
                int blue = (oldColor) & 0xff;

                int newRed = findClosestPaletteColor(red, nRed);
                int newGreen = findClosestPaletteColor(green, nGreen);
                int newBlue = findClosestPaletteColor(blue, nBlue);

                int color = 0;
                color |= (alpha << 24);
                color |= (newRed << 16);
                color |= (newGreen << 8);
                color |= newBlue;

                result.setRGB(i, j, color);

                int redError = red - newRed;
                int greenError = green - newGreen;
                int blueError = blue - newBlue;

                addErrors((int) (7.0 / 16 * redError), (int) (7.0 / 16 * greenError), (int) (7.0 / 16 * blueError), copy, i + 1, j);
                addErrors((int) (3.0 / 16 * redError), (int) (3.0 / 16 * greenError), (int) (3.0 / 16 * blueError), copy, i - 1, j + 1);
                addErrors((int) (5.0 / 16 * redError), (int) (5.0 / 16 * greenError), (int) (5.0 / 16 * blueError), copy, i, j + 1);
                addErrors((int) (1.0 / 16 * redError), (int) (1.0 / 16 * greenError), (int) (1.0 / 16 * blueError), copy, i + 1, j + 1);
            }
        }

        return result;
    }

    private int findClosestPaletteColor(int color, int colorsCount) {
        double intervalSize = 256.0 / colorsCount;
        int index = (int) (color / intervalSize);
        double position = 255.0 / (colorsCount - 1);
        return (int) (index * position);
    }

    private void addErrors(int redError, int greenError, int blueError, BufferedImage image, int x, int y) {
        if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) {
            return;
        }

        Color oldColor = new Color(image.getRGB(x, y));
        int newRed = addSingleError(oldColor.getRed(), redError);
        int newGreen = addSingleError(oldColor.getGreen(), greenError);
        int newBlue = addSingleError(oldColor.getBlue(), blueError);

        Color newColor = new Color(newRed, newGreen, newBlue);
        image.setRGB(x, y, newColor.getRGB());
    }

    private int addSingleError(int oldColor, int error) {
        int result = oldColor + error;
        if (result > 255) {
            result = 255;
        }
        if (result < 0) {
            result = 0;
        }
        return result;
    }

    private void copyImage(BufferedImage source, BufferedImage destination) {
        for (int i = 0; i < source.getWidth(); i++) {
            for (int j = 0; j < source.getHeight(); j++) {
                int rgb = source.getRGB(i, j);
                destination.setRGB(i, j, rgb);
            }
        }
    }
}
