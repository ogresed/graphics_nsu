package ru.nsu.fit.g16207.melnikov.model.filter;

import java.awt.image.BufferedImage;

public class WatercoloringFilter implements Filter {
    @Override
    public BufferedImage process(BufferedImage bufferedImage) {
        BufferedImage temp;

        MedianFilter medianFilter = new MedianFilter();
        temp = medianFilter.process(bufferedImage);

        SharpeningFilter sharpeningFilter = new SharpeningFilter();

        return sharpeningFilter.process(temp);
    }
}
