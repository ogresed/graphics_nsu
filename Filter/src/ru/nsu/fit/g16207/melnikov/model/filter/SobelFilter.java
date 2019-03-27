package ru.nsu.fit.g16207.melnikov.model.filter;

import ru.nsu.fit.g16207.melnikov.model.Colors;

import java.awt.image.BufferedImage;

public class SobelFilter extends DiffFilter {
    private static final int SIDE_COEFFICIENTS_COUNT = 3;

    public SobelFilter(int threshold) {
        super(threshold);
    }

    @Override
    public BufferedImage process(BufferedImage bufferedImage) {
        BufferedImage result = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        int[] sx;
        int[] sy;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                sx = countSX(i, j, width, height, bufferedImage);
                sy = countSY(i, j, width, height, bufferedImage);

                int sRed = Math.abs(sx[Colors.RED]) + Math.abs(sy[Colors.RED]);
                int sGreen = Math.abs(sx[Colors.GREEN]) + Math.abs(sy[Colors.GREEN]);
                int sBlue = Math.abs(sx[Colors.BLUE]) + Math.abs(sy[Colors.BLUE]);

                int[] colors = {sRed, sGreen, sBlue};
                int rgb = applyOperator(colors);
                result.setRGB(i, j, rgb);
            }
        }

        return result;
    }

    private boolean checkWidth(int pixelX, int width) {
        return pixelX > 0 && pixelX < width;
    }

    private boolean checkHeight(int pixelY, int height) {
        return pixelY > 0 && pixelY < height;
    }

    private int[] countSX(int pixelX, int pixelY, int width, int height, BufferedImage image) {
        int[] sx = new int[SIDE_COEFFICIENTS_COUNT];

        int[][] rightCoefficients = {{1, -1}, {1, 0}, {1, 1}};
        int[][] leftCoefficients = {{-1, -1}, {-1, 0}, {-1, 1}};

        if (checkWidth(pixelX + rightCoefficients[0][0], width)) {
            for (int k = 0; k < SIDE_COEFFICIENTS_COUNT; k++) {
                if (checkHeight(pixelY + rightCoefficients[k][1], height)) {
                    sx[Colors.RED] += (image.getRGB(pixelX + rightCoefficients[k][0], pixelY + rightCoefficients[k][1]) >> 16) & 0xff;
                    sx[Colors.GREEN] += (image.getRGB(pixelX + rightCoefficients[k][0], pixelY + rightCoefficients[k][1]) >> 8) & 0xff;
                    sx[Colors.BLUE] += (image.getRGB(pixelX + rightCoefficients[k][0], pixelY + rightCoefficients[k][1])) & 0xff;
                }
            }
            sx[Colors.RED] += (image.getRGB(pixelX + rightCoefficients[1][0], pixelY + rightCoefficients[1][1]) >> 16) & 0xff;
            sx[Colors.GREEN] += (image.getRGB(pixelX + rightCoefficients[1][0], pixelY + rightCoefficients[1][1]) >> 8) & 0xff;
            sx[Colors.BLUE] += (image.getRGB(pixelX + rightCoefficients[1][0], pixelY + rightCoefficients[1][1])) & 0xff;
        }

        if (checkWidth(pixelX + leftCoefficients[0][0], width)) {
            for (int k = 0; k < SIDE_COEFFICIENTS_COUNT; k++) {
                if (checkHeight(pixelY + leftCoefficients[k][1], height)) {
                    sx[Colors.RED] -= (image.getRGB(pixelX + leftCoefficients[k][0], pixelY + leftCoefficients[k][1]) >> 16) & 0xff;
                    sx[Colors.GREEN] -= (image.getRGB(pixelX + leftCoefficients[k][0], pixelY + leftCoefficients[k][1]) >> 8) & 0xff;
                    sx[Colors.BLUE] -= (image.getRGB(pixelX + leftCoefficients[k][0], pixelY + leftCoefficients[k][1])) & 0xff;
                }
            }
            sx[Colors.RED] -= (image.getRGB(pixelX + leftCoefficients[1][0], pixelY + leftCoefficients[1][1]) >> 16) & 0xff;
            sx[Colors.GREEN] -= (image.getRGB(pixelX + leftCoefficients[1][0], pixelY + leftCoefficients[1][1]) >> 8) & 0xff;
            sx[Colors.BLUE] -= (image.getRGB(pixelX + leftCoefficients[1][0], pixelY + leftCoefficients[1][1])) & 0xff;
        }

        return sx;
    }

    private int[] countSY(int pixelX, int pixelY, int width, int height, BufferedImage image) {
        int[] sy = new int[SIDE_COEFFICIENTS_COUNT];

        int[][] upCoefficients = {{-1, -1}, {0, -1}, {1, -1}};
        int[][] downCoefficients = {{-1, 1}, {0, 1}, {1, 1}};

        if (checkHeight(pixelY + upCoefficients[0][1], height)) {
            for (int k = 0; k < SIDE_COEFFICIENTS_COUNT; k++) {
                if (checkWidth(pixelX + upCoefficients[k][0], width)) {
                    sy[Colors.RED] += (image.getRGB(pixelX + upCoefficients[k][0], pixelY + upCoefficients[k][1]) >> 16) & 0xff;
                    sy[Colors.GREEN] += (image.getRGB(pixelX + upCoefficients[k][0], pixelY + upCoefficients[k][1]) >> 8) & 0xff;
                    sy[Colors.BLUE] += (image.getRGB(pixelX + upCoefficients[k][0], pixelY + upCoefficients[k][1])) & 0xff;
                }
            }
            sy[Colors.RED] += (image.getRGB(pixelX + upCoefficients[1][0], pixelY + upCoefficients[1][1]) >> 16) & 0xff;
            sy[Colors.GREEN] += (image.getRGB(pixelX + upCoefficients[1][0], pixelY + upCoefficients[1][1]) >> 8) & 0xff;
            sy[Colors.BLUE] += (image.getRGB(pixelX + upCoefficients[1][0], pixelY + upCoefficients[1][1])) & 0xff;
        }

        if (checkHeight(pixelY + downCoefficients[0][1], height)) {
            for (int k = 0; k < SIDE_COEFFICIENTS_COUNT; k++) {
                if (checkWidth(pixelX + downCoefficients[k][0], width)) {
                    sy[Colors.RED] -= (image.getRGB(pixelX + downCoefficients[k][0], pixelY + downCoefficients[k][1]) >> 16) & 0xff;
                    sy[Colors.GREEN] -= (image.getRGB(pixelX + downCoefficients[k][0], pixelY + downCoefficients[k][1]) >> 8) & 0xff;
                    sy[Colors.BLUE] -= (image.getRGB(pixelX + downCoefficients[k][0], pixelY + downCoefficients[k][1])) & 0xff;
                }
            }
            sy[Colors.RED] -= (image.getRGB(pixelX + downCoefficients[1][0], pixelY + downCoefficients[1][1]) >> 16) & 0xff;
            sy[Colors.GREEN] -= (image.getRGB(pixelX + downCoefficients[1][0], pixelY + downCoefficients[1][1]) >> 8) & 0xff;
            sy[Colors.BLUE] -= (image.getRGB(pixelX + downCoefficients[1][0], pixelY + downCoefficients[1][1])) & 0xff;
        }

        return sy;
    }
}
