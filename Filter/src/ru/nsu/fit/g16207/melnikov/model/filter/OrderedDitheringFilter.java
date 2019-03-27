package ru.nsu.fit.g16207.melnikov.model.filter;

import ru.nsu.fit.g16207.melnikov.model.Colors;

import java.awt.image.BufferedImage;

public class OrderedDitheringFilter implements Filter {
    private int matrixSize;

    public OrderedDitheringFilter(int matrixSize) {
        this.matrixSize = matrixSize;
    }

    @Override
    public BufferedImage process(BufferedImage bufferedImage) {
        int[] matrix = createMatrix();
        BufferedImage result = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int oldPixelColor = bufferedImage.getRGB(x, y);

                int redComponent = (oldPixelColor >> 16) & 0xff;
                int greenComponent = (oldPixelColor >> 8) & 0xff;
                int blueComponent = (oldPixelColor) & 0xff;

                int newPixelColor = oldPixelColor;

                int i = x % matrixSize;
                int j = y % matrixSize;
                int matrixElement = matrix[i * matrixSize + j];

                int newRedComponent = createNewComponent(redComponent, matrixElement);
                int newGreenComponent = createNewComponent(greenComponent, matrixElement);
                int newBlueComponent = createNewComponent(blueComponent, matrixElement);

                newPixelColor |= (newRedComponent << 16);
                newPixelColor |= (newGreenComponent << 8);
                newPixelColor |= newBlueComponent;

                result.setRGB(x, y, newPixelColor);
            }
        }

        return result;
    }

    private int createNewComponent(int component, int matrixElement) {
        int resultComponent;

        if (component > matrixElement) {
            resultComponent = Colors.MAX_COLOR;
        } else {
            resultComponent = Colors.MIN_COLOR;
        }

        return resultComponent;
    }

    private int[] createMatrix() {
        int[] result = new int[matrixSize * matrixSize];
        result[0] = 0;
        result[1] = 2;
        result[2] = 3;
        result[3] = 1;

        int smallMatrixSize = 2;
        int[] matrixCoefficients = {0, 2, 3, 1};

        while (smallMatrixSize < matrixSize) {

            int[] previous = new int[smallMatrixSize * smallMatrixSize];
            System.arraycopy(result, 0, previous, 0, smallMatrixSize * smallMatrixSize);

            for (int i = 0; i < 4; i++) {
                int matrixIndex = i / 2 == 0 ? 0 : 2 * smallMatrixSize * smallMatrixSize;
                if (i % 2 != 0) {
                    matrixIndex += smallMatrixSize;
                }

                for (int j = 0; j < smallMatrixSize; j++) {
                    for (int k = 0; k < smallMatrixSize; k++) {
                        result[matrixIndex + k] = 4 * previous[(j * smallMatrixSize + k)] + matrixCoefficients[i];
                    }
                    matrixIndex += 2 * smallMatrixSize;
                }
            }

            smallMatrixSize *= 2;
        }

        return result;
    }
}
