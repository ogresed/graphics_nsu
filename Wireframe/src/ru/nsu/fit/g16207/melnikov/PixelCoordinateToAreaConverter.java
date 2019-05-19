package ru.nsu.fit.g16207.melnikov;

public class PixelCoordinateToAreaConverter {
    private final double startX;
    private final double startY;
    private final double endX;
    private final double endY;

    private final int pixelFieldWidth;
    private final int pixelFieldHeight;

    public PixelCoordinateToAreaConverter(double startX, double startY, double endX, double endY, int pixelFieldWidth, int pixelFieldHeight) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.pixelFieldWidth = pixelFieldWidth;
        this.pixelFieldHeight = pixelFieldHeight;
    }

    public double toRealX(int u) {
        return (((double) u) / pixelFieldWidth) * (endX - startX) + startX;
    }

    public double toRealY(int v) {
        return ((double) v / pixelFieldHeight) * (endY - startY) + startY;
    }

    public int toPixelX(double imX) {
        return (int) (pixelFieldWidth / 2 + (imX / (endX - startX)) * pixelFieldWidth);
    }

    public int toPixelY(double imY) {
        return (int) (pixelFieldHeight / 2 - (imY / (endY - startY)) * pixelFieldHeight);
    }

    public int getPixelFieldWidth() {
        return pixelFieldWidth;
    }

    public int getPixelFieldHeight() {
        return pixelFieldHeight;
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public double getEndX() {
        return endX;
    }

    public double getEndY() {
        return endY;
    }
}
