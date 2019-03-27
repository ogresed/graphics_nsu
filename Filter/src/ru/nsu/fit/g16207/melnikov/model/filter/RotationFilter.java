package ru.nsu.fit.g16207.melnikov.model.filter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RotationFilter implements Filter {
    public static final int COEFFICIENTS_COUNT = 4;
    private double[] rotationMatrix;

    public RotationFilter(int angle) {
        double angleInRadians = angle * Math.PI / 180;
        rotationMatrix = new double[COEFFICIENTS_COUNT];
        rotationMatrix[0] = Math.cos(angleInRadians);
        rotationMatrix[1] = Math.sin(angleInRadians);
        rotationMatrix[2] = -Math.sin(angleInRadians);
        rotationMatrix[3] = Math.cos(angleInRadians);
    }

    @Override
    public BufferedImage process(BufferedImage bufferedImage) {
        BufferedImage result = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        int centerX = width / 2;
        int centerY = height / 2;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int sourcePixelX = centerX + (int) ((i - centerX) * rotationMatrix[0] + (j - centerY) * rotationMatrix[1]);
                int sourcePixelY = centerY + (int) ((i - centerX) * rotationMatrix[2] + (j - centerY) * rotationMatrix[3]);

                int sourceRGB;
                if (sourcePixelX < 0 || sourcePixelX > width - 1 || sourcePixelY < 0 || sourcePixelY > height - 1) {
                    sourceRGB = Color.WHITE.getRGB();
                } else {
                    sourceRGB = bufferedImage.getRGB(sourcePixelX, sourcePixelY);
                }

                result.setRGB(i, j, sourceRGB);
            }
        }

        return result;
    }
}
