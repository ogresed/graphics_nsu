package ru.nsu.fit.g16207.melnikov.function;

public abstract class Function {
    private double Xmin;
    private double Xmax;
    private double Ymin;
    private double Ymax;

    public Function(double xmin, double xmax, double ymin, double ymax) {
        Xmin = xmin;
        Xmax = xmax;
        Ymin = ymin;
        Ymax = ymax;
    }

    public Function() {

    }

    public double getXmin() {
        return Xmin;
    }

    public void setXmin(double xmin) {
        Xmin = xmin;
    }

    public double getXmax() {
        return Xmax;
    }

    public void setXmax(double xmax) {
        Xmax = xmax;
    }

    public double getYmin() {
        return Ymin;
    }

    public void setYmin(double ymin) {
        Ymin = ymin;
    }

    public double getYmax() {
        return Ymax;
    }

    public void setYmax(double ymax) {
        Ymax = ymax;
    }

    public abstract double function(double x, double y);
}
