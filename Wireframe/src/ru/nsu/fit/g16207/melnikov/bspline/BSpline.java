package ru.nsu.fit.g16207.melnikov.bspline;

import ru.nsu.fit.g16207.melnikov.Point;
import ru.nsu.fit.g16207.melnikov.matrix.Matrix;

import java.awt.*;
import java.util.ArrayList;

public class BSpline {
    private Color color;
    private double cx;
    private double cy;
    private double cz;
    private Matrix roundMatrix;
    private final ArrayList<ru.nsu.fit.g16207.melnikov.Point<Double, Double>> points;

    BSpline(Color color, double cx, double cy, double cz, Matrix roundMatrix, ArrayList<ru.nsu.fit.g16207.melnikov.Point<Double, Double>> points) {
        this.color = color;
        this.cx = cx;
        this.cy = cy;
        this.cz = cz;
        this.roundMatrix = roundMatrix;
        this.points = points;
    }

    public void addPoint(ru.nsu.fit.g16207.melnikov.Point<Double, Double> point) {
        points.add(point);
    }

    public Color getColor() {
        return color;
    }

    public double getCx() {
        return cx;
    }

    public double getCy() {
        return cy;
    }

    public double getCz() {
        return cz;
    }

    public Matrix getRoundMatrix() {
        return roundMatrix;
    }

    public ArrayList<Point<Double, Double>> getPoints() {
        return points;
    }

    public boolean isEmpty() {
        return getPoints().isEmpty();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setRoundMatrix(Matrix roundMatrix) {
        this.roundMatrix = roundMatrix;
    }

    public void setCx(double cx) {
        this.cx = cx;
    }

    public void setCy(double cy) {
        this.cy = cy;
    }

    public void setCz(double cz) {
        this.cz = cz;
    }
}
