package ru.nsu.fit.g16207.melnikov.model.filter;

import java.awt.image.BufferedImage;

public interface Filter {
    int IMAGE_SIZE = 350;

    BufferedImage process(BufferedImage bufferedImage);
}
