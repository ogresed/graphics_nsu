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
    public Segment indicateCrossDot(double level) {
        boolean returnValue = crosses(level);
        if(returnValue) {
            double attitude = Math.abs((level - f1) / (f2 - f1));
            if(x1 == x2) {
                crossOrdinate = (y2 -y1) * attitude + y1;
                crossAbscissa = x1;
            }
            else if (y1 == y2) {
                crossAbscissa = (x2 -x1) * attitude + x1;
                crossOrdinate = y1;
            }
        }
        crossed = returnValue;
        return this;
    }
    // true if crosses
    public boolean crosses(double level) {
        return !(level > f1 && level > f2 ||
                level < f1 && level < f2);
    }

    public double getF1() {
        return f1;
    }

    public double getF2() {
        return f2;
    }

    public boolean isCrossed() {
        return crossed;
    }
}
