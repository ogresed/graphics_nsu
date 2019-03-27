package ru.nsu.fit.g16207.melnikov.model.filter;

import java.awt.image.BufferedImage;

public class GrayScaleFilter implements Filter {
    @Override
    public BufferedImage process(BufferedImage bufferedImage) {
        BufferedImage result = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = bufferedImage.getRGB(i, j);

                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                int average = (red + green + blue) / 3;

                int color = 0;
                color |= (alpha << 24);
                color |= (average << 16);
                color |= (average << 8);
                color |= average;

                result.setRGB(i, j, color);
            }
        }

        return result;
    }
}
