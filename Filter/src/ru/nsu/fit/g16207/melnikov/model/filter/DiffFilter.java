package ru.nsu.fit.g16207.melnikov.model.filter;

import ru.nsu.fit.g16207.melnikov.model.Colors;

import java.awt.*;

abstract class DiffFilter implements Filter {

    private int threshold;

    DiffFilter(int threshold) {
        this.threshold = threshold;
    }

    int applyOperator(int[] operatorColors) {
        Color color = operatorColors[Colors.RED] > threshold ? Color.WHITE : Color.BLACK;

        return color.getRGB();
    }
}
