package ru.nsu.fit.g16207.melnikov.model.event;

import java.awt.image.BufferedImage;

public class FilterApplied {
    private BufferedImage newImage;

    public FilterApplied(BufferedImage newImage) {
        this.newImage = newImage;
    }

    public BufferedImage getNewImage() {
        return newImage;
    }
}
