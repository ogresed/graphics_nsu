package ru.nsu.fit.g16207.melnikov;

import ru.nsu.fit.g16207.melnikov.bspline.BSpline;
import ru.nsu.fit.g16207.melnikov.matrix.Matrix;

import java.awt.*;
import java.util.ArrayList;

public class Model {
    private int n;
    private int m;
    private int k;
    private double a;
    private double b;
    private int c;
    private int d;
    private double zn;
    private double zf;
    private double sw;
    private double sh;
    private Matrix roundMatrix;
    private Color backgroundColor;
    private final ArrayList<BSpline> bSplines = new ArrayList<>();

    public Model() {

    }

    public Model(int n, int m, int k, int a, int b, int c, int d,
                 int zn, int zf, int sw, int sh,
                 Matrix roundMatrix, Color backgroundColor) {
        this.n = n;
        this.m = m;
        this.k = k;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.zn = zn;
        this.zf = zf;
        this.sw = sw;
        this.sh = sh;
        this.roundMatrix = roundMatrix;
        this.backgroundColor = backgroundColor;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setM(int m) {
        this.m = m;
    }

    public void setK(int k) {
        this.k = k;
    }

    public void setA(double a) {
        this.a = a;
    }

    public void setB(double b) {
        this.b = b;
    }

    public void setC(int c) {
        this.c = c;
    }

    public void setD(int d) {
        this.d = d;
    }

    public void setZn(double zn) {
        this.zn = zn;
    }

    public void setZf(double zf) {
        this.zf = zf;
    }

    public void setSw(double sw) {
        this.sw = sw;
    }

    public void setSh(double sh) {
        this.sh = sh;
    }

    public void setRoundMatrix(Matrix roundMatrix) {
        this.roundMatrix = roundMatrix;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void addShape(BSpline BSpline) {
        bSplines.add(BSpline);
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public int getK() {
        return k;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public int getC() {
        return c;
    }

    public int getD() {
        return d;
    }

    public double getZn() {
        return zn;
    }

    public double getZf() {
        return zf;
    }

    public double getSw() {
        return sw;
    }

    public double getSh() {
        return sh;
    }

    public Matrix getRoundMatrix() {
        return roundMatrix;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public ArrayList<BSpline> getbSplines() {
        return bSplines;
    }

    public boolean isEmpty() {
        return getbSplines().isEmpty();
    }
}
