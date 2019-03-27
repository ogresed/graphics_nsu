package ru.nsu.fit.g16207.melnikov.model.filter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DoublingFilter implements Filter {
    private static final int ZOOM = 2;

    @Override
    public BufferedImage process(BufferedImage bufferedImage) {
        BufferedImage result = new BufferedImage(bufferedImage.getWidth() * 2, bufferedImage.getHeight() * 2,
                BufferedImage.TYPE_INT_ARGB);

        int width = result.getWidth();
        int height = result.getHeight();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double resizedX = i * 1.0 / ZOOM;
                double resizedY = j * 1.0 / ZOOM;

                int intResizedX = (int) resizedX;
                int intResizedY = (int) resizedY;

                double deltaX = resizedX - intResizedX;
                double deltaY = resizedY - intResizedY;

                int sourceRed = (int) (new Color(bufferedImage.getRGB(intResizedX, intResizedY)).getRed() * (1 - deltaX) * (1 - deltaY));
                int sourceGreen = (int) (new Color(bufferedImage.getRGB(intResizedX, intResizedY)).getGreen() * (1 - deltaX) * (1 - deltaY));
                int sourceBlue = (int) (new Color(bufferedImage.getRGB(intResizedX, intResizedY)).getBlue() * (1 - deltaX) * (1 - deltaY));

                if (intResizedX + 1 < bufferedImage.getWidth()) {
                    sourceRed += (int) (new Color(bufferedImage.getRGB(intResizedX + 1, intResizedY)).getRed() * deltaX * (1 - deltaY));
                    sourceGreen += (int) (new Color(bufferedImage.getRGB(intResizedX + 1, intResizedY)).getGreen() * deltaX * (1 - deltaY));
                    sourceBlue += (int) (new Color(bufferedImage.getRGB(intResizedX + 1, intResizedY)).getBlue() * deltaX * (1 - deltaY));
                }
                if (intResizedY + 1 < bufferedImage.getHeight()) {
                    sourceRed += (int) (new Color(bufferedImage.getRGB(intResizedX, intResizedY + 1)).getRed() * (1 - deltaX) * deltaY);
                    sourceGreen += (int) (new Color(bufferedImage.getRGB(intResizedX, intResizedY + 1)).getGreen() * (1 - deltaX) * deltaY);
                    sourceBlue += (int) (new Color(bufferedImage.getRGB(intResizedX, intResizedY + 1)).getBlue() * (1 - deltaX) * deltaY);
                }
                if (intResizedX + 1 < bufferedImage.getWidth() && intResizedY + 1 < bufferedImage.getHeight()) {
                    sourceRed += (int) (new Color(bufferedImage.getRGB(intResizedX + 1, intResizedY + 1)).getRed() * deltaX * deltaY);
                    sourceGreen += (int) (new Color(bufferedImage.getRGB(intResizedX + 1, intResizedY + 1)).getGreen() * deltaX * deltaY);
                    sourceBlue += (int) (new Color(bufferedImage.getRGB(intResizedX + 1, intResizedY + 1)).getBlue() * deltaX * deltaY);
                }

                int sourceRGB = new Color(sourceRed, sourceGreen, sourceBlue).getRGB();
                result.setRGB(i, j, sourceRGB);
            }
        }

        return result;
    }
}
