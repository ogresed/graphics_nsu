package ru.nsu.fit.g16207.melnikov.model_loader;

import ru.nsu.fit.g16207.melnikov.bspline.BSpline;
import ru.nsu.fit.g16207.melnikov.Model;
import ru.nsu.fit.g16207.melnikov.Point;
import ru.nsu.fit.g16207.melnikov.matrix.Matrix;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public class ModelSaver {
    private ModelSaver() {

    }
    public static void saveModel(File file, Model model) throws IOException {
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file),
                Charset.forName("UTF-8"))) {
            outputStreamWriter.write(model.getN() + " " + model.getM() + " " + model.getK() + " " +
                    model.getA() + " " + model.getB() + " " + model.getC() + " " + model.getD());
            outputStreamWriter.write('\n');
            outputStreamWriter.write(model.getZn() + " " + model.getZf() + " " + model.getSw() + " " +
                    model.getSh());
            outputStreamWriter.write('\n');
            Matrix roundMatrix = model.getRoundMatrix();
            for (int i = 0; i < 3; ++i) {
                outputStreamWriter.write(roundMatrix.get(i, 0) + " " + roundMatrix.get(i, 1) + " " + roundMatrix.get(i, 2));
                outputStreamWriter.write('\n');
            }

            Color backgroundColor = model.getBackgroundColor();
            outputStreamWriter.write(backgroundColor.getRed() + " " + backgroundColor.getGreen() + " "
                    + backgroundColor.getBlue());

            outputStreamWriter.write('\n');
            outputStreamWriter.write(model.getbSplines().size() + "");
            outputStreamWriter.write('\n');

            for (BSpline bSpline : model.getbSplines()) {
                Color color = bSpline.getColor();
                outputStreamWriter.write(color.getRed() + " " + color.getGreen() + " " + color.getBlue());
                outputStreamWriter.write('\n');
                outputStreamWriter.write(bSpline.getCx() + " " + bSpline.getCy() + " " + bSpline.getCz());
                outputStreamWriter.write('\n');

                Matrix shapeMatrix = bSpline.getRoundMatrix();
                for (int i = 0; i < 3; ++i) {
                    outputStreamWriter.write(shapeMatrix.get(i, 0) + " " + shapeMatrix.get(i, 1) + " " + shapeMatrix.get(i, 2));
                    outputStreamWriter.write('\n');
                }

                outputStreamWriter.write(bSpline.getPoints().size() + "");
                outputStreamWriter.write('\n');

                for (Point<Double, Double> point : bSpline.getPoints()) {
                    outputStreamWriter.write(point.getX() + " " + point.getY());
                    outputStreamWriter.write('\n');
                }
            }
        }
    }
}
