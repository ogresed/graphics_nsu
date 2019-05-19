package ru.nsu.fit.g16207.melnikov.bspline;

import ru.nsu.fit.g16207.melnikov.Point;
import ru.nsu.fit.g16207.melnikov.ShapeBuildingException;
import ru.nsu.fit.g16207.melnikov.matrix.Matrix;

import java.awt.*;
import java.util.ArrayList;

public class BSplineBuilder {
    private Color color = null;
    private Double cx = null;
    private Double cy = null;
    private Double cz = null;
    private Matrix roundMatrix = null;
    private final ArrayList<ru.nsu.fit.g16207.melnikov.Point<Double, Double>> points = new ArrayList<>();

    public BSplineBuilder() {

    }

    public BSplineBuilder withColor(Color color) {
        this.color = color;
        return this;
    }

    public BSplineBuilder withCx(Double cx) {
        this.cx = cx;
        return this;
    }

    public BSplineBuilder withCy(Double cy) {
        this.cy = cy;
        return this;
    }

    public BSplineBuilder withCz(Double cz) {
        this.cz = cz;
        return this;
    }

    public BSplineBuilder withRoundMatrix(Matrix roundMatrix) {
        this.roundMatrix = roundMatrix;
        return this;
    }

    public BSplineBuilder addPoint(double x, double y) {
        points.add(new Point<>(x, y));
        return this;
    }

    public BSpline build() throws ShapeBuildingException {
        if (null == color || null == cz || null == cy || null == cx
                || null == roundMatrix) {
            throw new ShapeBuildingException("Can not build object: not all necessary parameters are set");
        }

        return new BSpline(color, cx, cy, cz, roundMatrix, points);
    }

}
