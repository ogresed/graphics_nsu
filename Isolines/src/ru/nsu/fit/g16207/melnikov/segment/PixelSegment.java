package ru.nsu.fit.g16207.melnikov.segment;

public class PixelSegment {
    public PixelSegment(int x1, int x2, int y1, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public int x1, x2, y1, y2;
}
