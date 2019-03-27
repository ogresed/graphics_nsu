package ru.nsu.fit.g16207.melnikov.model.filter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GammaFilter implements Filter {
    private static final int GAMMA_COEFFICIENT = 1;
    private static final double NORMALIZING_COEFFICIENT = 256.0;
    private double gamma;

    public GammaFilter(double gamma) {
        this.gamma = gamma;
    }

    @Override
    public BufferedImage process(BufferedImage bufferedImage) {
        BufferedImage result = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = bufferedImage.getRGB(i, j);

                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                float newRed = (float) (GAMMA_COEFFICIENT * Math.pow(red / NORMALIZING_COEFFICIENT, gamma));
                float newGreen = (float) (GAMMA_COEFFICIENT * Math.pow(green / NORMALIZING_COEFFICIENT, gamma));
                float newBlue = (float) (GAMMA_COEFFICIENT * Math.pow(blue / NORMALIZING_COEFFICIENT, gamma));

                Color newColor = new Color(newRed, newGreen, newBlue);
                result.setRGB(i, j, newColor.getRGB());
            }
        }

        return result;
    }
}
