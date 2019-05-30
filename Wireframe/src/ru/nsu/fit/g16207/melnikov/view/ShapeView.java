package ru.nsu.fit.g16207.melnikov.view;

import ru.nsu.fit.g16207.melnikov.*;
import ru.nsu.fit.g16207.melnikov.Point;
import ru.nsu.fit.g16207.melnikov.bspline.BSpline;
import ru.nsu.fit.g16207.melnikov.bspline.BSplineFunction;
import ru.nsu.fit.g16207.melnikov.matrix.Matrix;
import ru.nsu.fit.g16207.melnikov.matrix.MatrixUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class ShapeView extends JPanel {
    private static final double ZOOM_PLUS_RATIO = 1.1f;
    private static final double ZOOM_MINUS_RATIO = 0.9f;
    private BufferedImage bufferedImage;
    private int height;
    private int width;

    private Model model;
    private Integer selectedShape = null;

    private Point<Integer, Integer> prevPointScene = new Point<>(0, 0);
    private Point<Integer, Integer> prevPointShape = new Point<>(0, 0);

    private Matrix sceneRotationMatrix = new Matrix(new double[][] {
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}
    });

    private Matrix shapeRotationMatrix;

    class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            onMouseDragged(e);
            prevPointScene = new ru.nsu.fit.g16207.melnikov.Point<>(e.getX(), e.getY());
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            if (SwingUtilities.isLeftMouseButton(e)) {
                prevPointScene = new ru.nsu.fit.g16207.melnikov.Point<>(e.getX(), e.getY());
            } else {
                prevPointShape = new Point<>(e.getX(), e.getY());
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            ShapeView.this.requestFocus();
        }

        private void onMouseDragged(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                int dx = (e.getX() - prevPointScene.getX());
                int dy = - (e.getY() - prevPointScene.getY());

                Matrix qzMatrix = calculateQzMatrix(((double) dy / (double) height) * 2 * Math.PI);
                Matrix qyMatrix = calculateQyMatrix(((double) dx / (double) width) * 2 * Math.PI);

                sceneRotationMatrix = MatrixUtil.multiply(qzMatrix,
                        MatrixUtil.multiply(qyMatrix, sceneRotationMatrix));

                model.setRoundMatrix(sceneRotationMatrix);
            } else {
                if (null != selectedShape && shapeRotationMatrix != null) {
                    int dx = (((e.getX() - prevPointShape.getX())));
                    int dy =  - (((e.getY() - prevPointShape.getY())));

                    Matrix qxMatrix = calculateQxMatrix(((double) dy / (double) height) * Math.PI / 5);
                    Matrix qyMatrix = calculateQyMatrix(((double) dx / (double) width) * Math.PI / 5);

                    shapeRotationMatrix = MatrixUtil.multiply(qyMatrix, MatrixUtil.multiply(qxMatrix, shapeRotationMatrix));
                    model.getbSpline().setRoundMatrix(shapeRotationMatrix);
                }
            }

            repaint();
        }
    }

    public ShapeView(int width, int height) {
        super(true);
        setPreferredSize(new Dimension(width, height));
        this.height = height;
        this.width = width;
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        MouseAdapter adapter = new MyMouseAdapter();
        addMouseMotionListener(adapter);
        addMouseListener(adapter);
        addMouseWheelListener(e -> {
            int notches = e.getWheelRotation();
            if (notches < 0) {
                if (model.getZf() == 0) {
                    model.setZf(0.1f);
                }
                model.setZf(model.getZf() * ZOOM_PLUS_RATIO);
                repaint();
            } else {
                model.setZf(model.getZf() * ZOOM_MINUS_RATIO);
                repaint();
            }
        });
    }

    public void setSceneRotationMatrix(Matrix sceneRotationMatrix) {
        this.sceneRotationMatrix = sceneRotationMatrix;
    }

    public void setModel(Model newModel) {
        this.model = newModel;
        sceneRotationMatrix = newModel.getRoundMatrix();
        if (!model.isEmpty()) {
            setSelectedShape(0);
        } else {
            selectedShape = null;
        }
        update();
    }

    public void update() {
        if (null != model) {
            double sw = model.getSw();
            double sh = model.getSh();

            double necessaryK = sw / sh;
            double currentK = (double) bufferedImage.getWidth() / (double) bufferedImage.getHeight();

            if (currentK < necessaryK) {
                height = (int) (bufferedImage.getWidth() * sh / sw);
                width = bufferedImage.getWidth();
            }

            if (necessaryK < currentK) {
                width = (int) (bufferedImage.getHeight() * sw / sh);
                height = bufferedImage.getHeight();
            }

            Graphics2D graphics2D = bufferedImage.createGraphics();
            graphics2D.setColor(Color.LIGHT_GRAY);
            graphics2D.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
            graphics2D.dispose();
        }

        repaint();
    }

    private Matrix calculateZoomMatrix(double scaleX, double scaleY, double scaleZ) {
        return new Matrix(new double[][]{
                {scaleX, 0, 0, 0},
                {0, scaleY, 0, 0},
                {0, 0, scaleZ, 0},
                {0, 0, 0, 1}
        });
    }

    private Matrix calculateShiftMatrix(double shiftX, double shiftY, double shiftZ) {
        return new Matrix(new double[][]{
                {1, 0, 0, shiftX},
                {0, 1, 0, shiftY},
                {0, 0, 1, shiftZ},
                {0, 0, 0, 1}
        });
    }

    private Matrix calculateQzMatrix(double angleZ) {
        return new Matrix(new double[][]{
                {cos(angleZ), -sin(angleZ), 0, 0},
                {sin(angleZ), cos(angleZ), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
    }

    private Matrix calculateQxMatrix(double angleX) {
        return new Matrix(new double[][]{
                {1, 0, 0, 0},
                {0, cos(angleX), -sin(angleX), 0},
                {0, sin(angleX), cos(angleX), 0},
                {0, 0, 0, 1}
        });
    }

    private Matrix calculateQyMatrix(double angleY) {
        return new Matrix(new double[][]{
                {cos(angleY), 0, sin(angleY), 0},
                {0, 1, 0, 0},
                {-sin(angleY), 0, cos(angleY), 0},
                {0, 0, 0, 1},
        });
    }

    private Point3D<Double, Double, Double> crossProduct(Point3D<Double, Double, Double> vector1,
                                                         Point3D<Double, Double, Double> vector2) {
        return new Point3D<>(vector1.getY() * vector2.getZ() - vector1.getZ() * vector2.getY(),
                - (vector1.getX() * vector2.getZ() - vector1.getZ() * vector2.getX()),
                vector1.getX() * vector2.getY() - vector1.getY() * vector2.getX());
    }

    private Matrix calculateCameraMatrix(Point3D<Double, Double, Double> cameraPosition,
                                         Point3D<Double, Double, Double> viewPoint,
                                         Point3D<Double, Double, Double> upVector) {
        double normalizeForK = Math.sqrt((cameraPosition.getX() - viewPoint.getX()) * (cameraPosition.getX() - viewPoint.getX())
                + (cameraPosition.getY() - viewPoint.getY()) * (cameraPosition.getY() - viewPoint.getY()) +
                (cameraPosition.getZ() - viewPoint.getZ()) * (cameraPosition.getZ() - viewPoint.getZ()));

        double kx = (viewPoint.getX() - cameraPosition.getX()) /
                normalizeForK;
        double ky = (viewPoint.getY() - cameraPosition.getY()) /
                normalizeForK;
        double kz = (viewPoint.getZ() - cameraPosition.getZ()) /
                normalizeForK;

        Point3D<Double, Double, Double> iVector = crossProduct(upVector, new Point3D<>(kx, ky, kz));

        double normalizeForI = Math.sqrt(iVector.getX() * iVector.getX() +
                iVector.getY() * iVector.getY() +
                iVector.getZ() * iVector.getZ());

        double ix = iVector.getX() / normalizeForI;
        double iy = iVector.getY() / normalizeForI;
        double iz = iVector.getZ() / normalizeForI;

        Point3D<Double, Double, Double> jVector = crossProduct(new Point3D<>(kx, ky, kz),
                new Point3D<>(ix, iy, iz));

        return MatrixUtil.multiply(new Matrix(new double[][]{
                {ix, iy, iz, 0},
                {jVector.getX(), jVector.getY(), jVector.getZ(), 0},
                {kx, ky, kz, 0},
                {0, 0, 0, 1}
        }), new Matrix(new double[][]{
                {1, 0, 0, -viewPoint.getX()},
                {0, 1, 0, -viewPoint.getY()},
                {0, 0, 1, -viewPoint.getZ()},
                {0, 0, 0, 1}
        }));
    }

    private Matrix calculateProjMatrix(double sw, double sh, double zf, double zn) {
        return new Matrix(new double[][]{
                {2 * zf / sw, 0, 0, 0},
                {0, 2 * zf / sh, 0, 0},
                {0, 0, zf / (zn - zf), zn * zf / (zn - zf)},
                {0, 0, -1, 0}
        });
    }

    private void drawLine(Point3D<Double, Double, Double> startPoint,
                          Point3D<Double, Double, Double> endPoint,
                          Color color) {
        drawLine(startPoint, endPoint, color, new Matrix(new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        }));
    }

    private void drawLine(Point3D<Double, Double, Double> startPoint,
                          Point3D<Double, Double, Double> endPoint,
                          Color color, Matrix shapeMatrix) {
        Matrix start = new Matrix(new double[][]{{startPoint.getX()},
                {startPoint.getY()}, {startPoint.getZ()}, {1}});

        Matrix end = new Matrix(new double[][]{{endPoint.getX()},
                {endPoint.getY()}, {endPoint.getZ()}, {1}});

        Matrix realStart = MatrixUtil.multiply(sceneRotationMatrix, MatrixUtil.multiply(shapeMatrix, start));
        Matrix realEnd = MatrixUtil.multiply(sceneRotationMatrix, MatrixUtil.multiply(shapeMatrix, end));

        Matrix cameraMatrix = calculateCameraMatrix(new Point3D<>(-10.0, 0.0, 0.0),
                new Point3D<>(10.0, 0.0, 0.0),
                new Point3D<>(0.0, 1.0, 0.0));

        Matrix projMatrix = calculateProjMatrix(model.getSw(),
                model.getSh(), model.getZf(), model.getZn());

        realStart = MatrixUtil.multiply(projMatrix,
                MatrixUtil.multiply(cameraMatrix, realStart));
        realEnd = MatrixUtil.multiply(projMatrix,
                MatrixUtil.multiply(cameraMatrix, realEnd));

        double x0 = realStart.get(0, 0) / realStart.get(3, 0);
        double y0 = realStart.get(1, 0) / realStart.get(3, 0);
        double z0 = realStart.get(2, 0) / realStart.get(3, 0);

        double x1 = realEnd.get(0, 0) / realEnd.get(3, 0);
        double y1 = realEnd.get(1, 0) / realEnd.get(3, 0);
        double z1 = realEnd.get(2, 0) / realEnd.get(3, 0);

        Clipper3D clipper3D = new Clipper3D(1, 1, 1, -1, -1, 0);

        Line<Point3D<Double, Double, Double>> clippedLine = clipper3D.getClippedLine(new Line<>(new Point3D<>(x0, y0, z0), new Point3D<>(x1, y1, z1)));

        if (null == clippedLine) {
            return;
        }

        PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter =
                new PixelCoordinateToAreaConverter(-1, -1, 1, 1, width, height);

        Graphics2D g = bufferedImage.createGraphics();
        g.setColor(color);
        g.drawLine(pixelCoordinateToAreaConverter.toPixelX(clippedLine.getStart().getX()),
                pixelCoordinateToAreaConverter.toPixelY(clippedLine.getStart().getY()),
                pixelCoordinateToAreaConverter.toPixelX(clippedLine.getEnd().getX()),
                pixelCoordinateToAreaConverter.toPixelY(clippedLine.getEnd().getY()));
        g.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = bufferedImage.createGraphics();

        if (null != model) {
            g2d.setColor(model.getBackgroundColor());

            g2d.fillRect(0, 0, width + 1, height + 1);
            g2d.dispose();

            drawCube();
            drawCoordinateSystem();
            double globalMax = Double.MIN_VALUE;
            BSpline bSpline = model.getbSpline();

            ArrayList<Line<Point3D<Double, Double, Double>>> splineLines = ShapeToLinesConverter
                    .toLines(new BSplineFunction(bSpline.getPoints()),
                    model.getN(), model.getM(), model.getK(), model.getA(), model.getB(), model.getD(), model.getC());

            Matrix roundMatrix = bSpline.getRoundMatrix();
            Matrix moveMatrix = calculateShiftMatrix(bSpline.getCx(),
                    bSpline.getCy(), bSpline.getCz());
            Matrix shapeMatrix = MatrixUtil.multiply(moveMatrix, roundMatrix);

            for (Line<Point3D<Double, Double, Double>> line : splineLines) {

                Matrix startPoint = MatrixUtil.multiply(shapeMatrix, new Matrix(new double[][]{{line.getStart().getX()},
                        {line.getStart().getY()}, {line.getStart().getZ()}, {1}}));

                Matrix endPoint = MatrixUtil.multiply(shapeMatrix, new Matrix(new double[][]{{line.getEnd().getX()},
                        {line.getEnd().getY()}, {line.getEnd().getZ()}, {1}}));

                double localMax = ListUtil.asList(startPoint.get(1, 0), startPoint.get(0, 0),
                        startPoint.get(2, 0),
                        endPoint.get(0, 0), endPoint.get(1, 0),
                        endPoint.get(2, 0))
                        .stream()
                        .map(Math::abs)
                        .max(Double::compareTo)
                        .orElse(0.0);

                if (localMax > globalMax) {
                    globalMax = localMax;
                }
            }

            Matrix scaleMatrix = calculateZoomMatrix(1 / globalMax, 1 / globalMax, 1 / globalMax);

            moveMatrix = calculateShiftMatrix(model.getbSpline().getCx(),
                    model.getbSpline().getCy(), model.getbSpline().getCz());

            bSpline = model.getbSpline();

            roundMatrix = bSpline.getRoundMatrix();
            Matrix shapeMatrixPrev = MatrixUtil.multiply(moveMatrix, roundMatrix);
            shapeMatrix = MatrixUtil.multiply(scaleMatrix, shapeMatrixPrev);

            for (Line<Point3D<Double, Double, Double>> splineLine : splineLines) {
                drawLine(splineLine.getStart(), splineLine.getEnd(), model.getbSpline().getColor(), shapeMatrix);
            }
        }

        g.drawImage(bufferedImage, 0, 0, null);
    }

    private void drawCoordinateSystem() {
        Point3D<Double, Double, Double> zero =  new Point3D<>(0.0, 0.0, 0.0);
        drawLine(zero, new Point3D<>(1.0, 0.0, 0.0), Color.RED);
        drawLine(zero, new Point3D<>(0.0, 1.0, 0.0), Color.GREEN);
        drawLine(zero, new Point3D<>(0.0, 0.0, 1.0), Color.BLUE);
    }

    private void drawCube() {
        ArrayList<Line<Point3D<Double, Double, Double>>> cubeLines = new ArrayList<>();

        cubeLines.add(new Line<>(new Point3D<>(-1.0, -1.0, -1.0),
                new Point3D<>(1.0, -1.0, -1.0)));
        cubeLines.add(new Line<>(new Point3D<>(-1.0, -1.0, -1.0), new Point3D<>(-1.0, 1.0, -1.0)));
        cubeLines.add(new Line<>(new Point3D<>(-1.0, -1.0, -1.0), new Point3D<>(-1.0, -1.0, 1.0)));

        cubeLines.add(new Line<>(new Point3D<>(1.0, 1.0, -1.0), new Point3D<>(1.0, -1.0, -1.0)));
        cubeLines.add(new Line<>(new Point3D<>(1.0, -1.0, 1.0), new Point3D<>(1.0, -1.0, -1.0)));
        cubeLines.add(new Line<>(new Point3D<>(-1.0, 1.0, -1.0), new Point3D<>(1.0, 1.0, -1.0)));
        cubeLines.add(new Line<>(new Point3D<>(-1.0, -1.0, 1.0), new Point3D<>(1.0, -1.0, 1.0)));

        cubeLines.add(new Line<>(new Point3D<>(1.0, 1.0, -1.0), new Point3D<>(1.0, 1.0, 1.0)));
        cubeLines.add(new Line<>(new Point3D<>(1.0, -1.0, 1.0), new Point3D<>(1.0, 1.0, 1.0)));
        cubeLines.add(new Line<>(new Point3D<>(-1.0, 1.0, 1.0), new Point3D<>(1.0, 1.0, 1.0)));

        cubeLines.add(new Line<>(new Point3D<>(-1.0, 1.0, 1.0), new Point3D<>(-1.0, 1.0, -1.0)));
        cubeLines.add(new Line<>(new Point3D<>(-1.0, 1.0, 1.0), new Point3D<>(-1.0, -1.0, 1.0)));

        for (Line<Point3D<Double, Double, Double>> line : cubeLines) {
            drawLine(line.getStart(), line.getEnd(), Color.WHITE);
        }
    }

    public Integer getSelectedShape() {
        return selectedShape;
    }

    public void setSelectedShape(Integer selectedShape) {
        this.selectedShape = selectedShape;
        if (null != selectedShape) {
            shapeRotationMatrix = model.getbSpline().getRoundMatrix();
        }
    }
}
