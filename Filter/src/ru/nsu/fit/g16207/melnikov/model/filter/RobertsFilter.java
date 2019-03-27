package ru.nsu.fit.g16207.melnikov.model.filter;

import ru.nsu.fit.g16207.melnikov.model.Colors;

import java.awt.image.BufferedImage;

public class RobertsFilter extends DiffFilter {
    public RobertsFilter(int threshold) {
        super(threshold);
    }

    @Override
    public BufferedImage process(BufferedImage bufferedImage) {
        BufferedImage result = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int[] colors = robertsOperator(bufferedImage, i, j);
                int rgb = applyOperator(colors);
                result.setRGB(i, j, rgb);
            }
        }
        return result;
    }

    private int[] robertsOperator(BufferedImage image, int pixelX, int pixelY) {
        int gxRed = 0;
        int gxGreen = 0;
        int gxBlue = 0;
        if (pixelX + 1 < image.getWidth() && pixelY + 1 < image.getHeight()) {
            gxRed = ((image.getRGB(pixelX + 1, pixelY + 1) >> 16) & 0xff) - ((image.getRGB(pixelX, pixelY) >> 16) & 0xff);
            gxGreen = ((image.getRGB(pixelX + 1, pixelY + 1) >> 8) & 0xff) - ((image.getRGB(pixelX, pixelY) >> 8) & 0xff);
            gxBlue = ((image.getRGB(pixelX + 1, pixelY + 1)) & 0xff) - ((image.getRGB(pixelX, pixelY)) & 0xff);
        }

        int gyRed = 0;
        int gyGreen = 0;
        int gyBlue = 0;
        if (pixelX + 1 < image.getWidth() && pixelY + 1 < image.getHeight()) {
            gyRed = ((image.getRGB(pixelX, pixelY + 1) >> 16) & 0xff) - ((image.getRGB(pixelX + 1, pixelY) >> 16) & 0xff);
            gyGreen = ((image.getRGB(pixelX, pixelY + 1) >> 8) & 0xff) - ((image.getRGB(pixelX + 1, pixelY) >> 8) & 0xff);
            gyBlue = ((image.getRGB(pixelX, pixelY + 1)) & 0xff) - ((image.getRGB(pixelX, pixelY + 1)) & 0xff);
        }

        int[] result = new int[Colors.COUNT];
        result[Colors.RED] = Math.abs(gxRed) + Math.abs(gyRed);
        result[Colors.GREEN] = Math.abs(gxGreen) + Math.abs(gyGreen);
        result[Colors.BLUE] = Math.abs(gxBlue) + Math.abs(gyBlue);

        return result;
    }
}
