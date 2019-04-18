package ru.nsu.fit.g16207.melnikov.segment;

public class Segment {
    private double x1, x2, y1, y2, f1, f2;
    private boolean crossed;
    private double crossAbscissa, crossOrdinate;
    Segment(double x1, double x2, double y1, double y2, double f1, double f2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.f1 = f1;
        this.f2 = f2;
        crossed = false;
    }
    public double getCrossAbscissa() {
        return crossAbscissa;
    }
    public double getCrossOrdinate() {
        return crossOrdinate;
    }
    /**
     * this function return true if level crosses line
     * */
    public void indicateCrossDot(double level) {
        // true if don't crosses
        boolean returnValue = level > f1 && level > f2 ||
                level < f1 && level < f2;
        if(!returnValue) {
            double small = f1 < f2 ? f1 : f2;
            double attitude = (level - small) / Math.abs(f2 - f1);
            if(x1 == x2) {
                crossOrdinate = Math.abs(y2 -y1) * attitude;
                crossAbscissa = x1;
            }
            else if (y1 == y2) {
                crossAbscissa = Math.abs(x2 -x1) * attitude;
                crossOrdinate = y1;
            }
        }
        crossed = returnValue;
    }
    public boolean isCrossed() {
        return crossed;
    }
}
