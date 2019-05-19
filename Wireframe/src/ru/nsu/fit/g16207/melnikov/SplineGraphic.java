package ru.nsu.fit.g16207.melnikov;

import ru.nsu.fit.g16207.melnikov.bspline.BSpline;
import ru.nsu.fit.g16207.melnikov.bspline.BSplineFunction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public class SplineGraphic extends JPanel {
    private static final int ovalRadius1 = 4;
    private static final int ovalRadius2 = 7;
    private static final Color POINTS_COLOR = Color.blue;
    private static final Color SELECTED_POINTS_COLOR = Color.green;
    private static final Color EXTRA_SPLINE_PART_COLOR = Color.GRAY;
    private static final Color SYSTEM_COORDINATE_COLOR = Color.WHITE;
    private static final Color MAIN_SPLINE_COLOR = Color.WHITE;

    private final int width;
    private final int height;

    private double startLength;
    private double endLength;

    private final BufferedImage bufferedImage;
    private PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter;
    private double maxWidth = 10;
    private ru.nsu.fit.g16207.melnikov.bspline.BSpline BSpline;

    private Point<Double, Double> selectedPoint = null;

    public SplineGraphic(int width, int height, double startLength, double endLength) {
        super(true);
        this.startLength = startLength;
        this.endLength = endLength;

        this.height = height;
        this.width = width;
        setMaxWidth(maxWidth);
        this.bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        setPreferredSize(new Dimension(width, height));

        drawSpline();

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                onMouseDragged(e);
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (SwingUtilities.isRightMouseButton(e)) {
                    if (onMouseRightButtonClick(e)) return;
                }

                onMouseDoubleClick(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                if (SwingUtilities.isRightMouseButton(e)) {
                    return;
                }

                onMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                onMouseReleased();
            }
        });
    }

    private void onMouseReleased() {
        if (null != BSpline && !BSpline.isEmpty()) {
            selectedPoint = null;
            drawSpline();
        }
    }

    private void onMousePressed(MouseEvent e) {
        if (null != BSpline && !BSpline.isEmpty()) {
            int counter = 0;
            for (Point<Double, Double> point : BSpline.getPoints()) {
                int realX = pixelCoordinateToAreaConverter.toPixelX(point.getX());
                int realY = pixelCoordinateToAreaConverter.toPixelY(point.getY());
                int radius = (0 == counter % 2) ? ovalRadius1 : ovalRadius2;

                if (e.getX() >= realX - radius && e.getX() <= realX + radius) {
                    if (e.getY() >= realY - radius && e.getY() <= realY + radius) {
                        selectedPoint = point;
                        drawSpline();
                        break;
                    }
                }
                counter++;
            }
        }
    }

    private void onMouseDoubleClick(MouseEvent e) {
        if (e.getClickCount() == 2 && !e.isConsumed()) {
            e.consume();

            if (null != BSpline) {
                int counter = 0;
                for (Point<Double, Double> point : BSpline.getPoints()) {
                    int realX = pixelCoordinateToAreaConverter.toPixelX(point.getX());
                    int realY = pixelCoordinateToAreaConverter.toPixelY(point.getY());
                    int radius = (0 == counter % 2) ? ovalRadius1 : ovalRadius2;

                    if (e.getX() >= realX - radius && e.getX() <= realX + radius) {
                        if (e.getY() >= realY - radius && e.getY() <= realY + radius) {
                            return;
                        }
                    }
                    counter++;
                }

                double fieldX = pixelCoordinateToAreaConverter.toRealX(e.getX());
                double fieldY = pixelCoordinateToAreaConverter.toRealY(height - e.getY());

                BSpline.addPoint(new Point<>(fieldX, fieldY));

                drawSpline();
            }
        }
    }

    private boolean onMouseRightButtonClick(MouseEvent e) {
        if (null != BSpline) {
            int counter = 0;

            for (Point<Double, Double> point : BSpline.getPoints()) {
                int realX = pixelCoordinateToAreaConverter.toPixelX(point.getX());
                int realY = pixelCoordinateToAreaConverter.toPixelY(point.getY());
                int radius = (0 == counter % 2) ? ovalRadius1 : ovalRadius2;

                if (e.getX() >= realX - radius && e.getX() <= realX + radius) {
                    if (e.getY() >= realY - radius && e.getY() <= realY + radius) {
                        JPopupMenu jPopupMenu = new JPopupMenu();
                        JMenuItem itemRemove = new JMenuItem("Delete");
                        itemRemove.addActionListener(e1 -> {
                            BSpline.getPoints().remove(point);
                            drawSpline();
                        });

                        jPopupMenu.add(itemRemove);
                        jPopupMenu.show(SplineGraphic.this, e.getX(), e.getY());
                        return true;
                    }
                }
                counter++;
            }
        }
        return false;
    }

    private void drawPoints(ArrayList<Point<Double, Double>> points) {
        Graphics2D graphics2D = bufferedImage.createGraphics();

        graphics2D.setColor(POINTS_COLOR);

        int counter = 0;
        int prevRealX = 0;
        int prevRealY = 0;
        for (Point<Double, Double> p : points) {
            int realX = pixelCoordinateToAreaConverter.toPixelX(p.getX());
            int realY = pixelCoordinateToAreaConverter.toPixelY(p.getY());
            int ovarRadius = (0 == counter % 2) ? ovalRadius1 : ovalRadius2;

            if (selectedPoint == p) {
                graphics2D.setColor(SELECTED_POINTS_COLOR);
            }

            graphics2D.drawOval(realX - ovarRadius, realY - ovarRadius, ovarRadius * 2,
                    ovarRadius * 2);

            if (selectedPoint == p) {
                graphics2D.setColor(POINTS_COLOR);
            }
            if (counter++ > 0) {
                graphics2D.drawLine(realX, realY, prevRealX, prevRealY);
            }

            prevRealX = realX;
            prevRealY = realY;

        }

        graphics2D.dispose();
    }

    public void setBSpline(BSpline BSpline) {
        this.BSpline = BSpline;
        drawSpline();
    }

    public void setNewStartLength(double newStartLength) {
        this.startLength = newStartLength;
        drawSpline();
    }

    public void setNewEndLength(double newEndLength) {
        this.endLength = newEndLength;
        drawSpline();
    }

    private void drawSpline() {
        clearImage();
        drawCoordinateSystem();

        if (null == BSpline || BSpline.getPoints().isEmpty()) {
            repaint();
            return;
        }

        drawPoints(BSpline.getPoints());

        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setColor(MAIN_SPLINE_COLOR);

        BSplineFunction bSplineFunction = new BSplineFunction(BSpline.getPoints());

        Point<Integer, Double> startPoint = bSplineFunction.getIAndT(startLength);
        Point<Integer, Double> endPoint = bSplineFunction.getIAndT(endLength);

        graphics2D.setColor(EXTRA_SPLINE_PART_COLOR);
        for (int i = 1; i < BSpline.getPoints().size() - 2; ++i) {
            for (double t = 0; t < 1; t += 0.01) {
                if (i == startPoint.getX() && t > startPoint.getY()) {
                    graphics2D.setColor(MAIN_SPLINE_COLOR);
                }

                if (i == endPoint.getX() && t > endPoint.getY()) {
                    graphics2D.setColor(EXTRA_SPLINE_PART_COLOR);
                }
                Point<Double, Double> point = bSplineFunction.getValue(i, t);

                int realX = pixelCoordinateToAreaConverter.toPixelX(point.getX());
                int realY = pixelCoordinateToAreaConverter.toPixelY(point.getY());
                graphics2D.drawLine(realX, realY, realX, realY);
            }
        }

        graphics2D.dispose();

        repaint();
    }

    private void clearImage() {
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setColor(Color.BLACK);
        graphics2D.fillRect(0, 0, width, height);

        graphics2D.dispose();
    }

    private void drawCoordinateSystem() {
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setColor(SYSTEM_COORDINATE_COLOR);
        graphics2D.drawLine(0, height / 2, width, height / 2);
        graphics2D.drawLine(width / 2, 0, width / 2, height);

        double heightStep = (((double) height / 2f) / getMaxWidth());
        double widthStep = (((double) width / 2f) / getMaxWidth());

        for (int k = 0; k < getMaxWidth(); ++k) {
            graphics2D.drawLine((int) (width / 2f) - 5, (int) ((height / 2f) - k * heightStep),
                    (int) (width / 2f + 5), (int) ((height / 2f) - k * heightStep));
            graphics2D.drawLine((int) ((width / 2f) - 5), (int) ((height / 2f) + k * heightStep),
                    (int) (width / 2f + 5), (int) ((height / 2f) + k * heightStep));
            graphics2D.drawLine((int) ((width / 2f) - k * widthStep), (int) (height / 2f) - 5,
                    (int) ((width / 2f) - k * widthStep), (int) (height / 2f) + 5);
            graphics2D.drawLine((int) ((width / 2f) + k * widthStep), (int) (height / 2f) - 5,
                    (int) ((width / 2f) + k * widthStep), (int) (height / 2f) + 5);
        }

        graphics2D.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bufferedImage, 0, 0, null);
    }

    public void setMaxWidth(double maxWidth) {
        this.pixelCoordinateToAreaConverter = new PixelCoordinateToAreaConverter(-maxWidth, -maxWidth,
                maxWidth, maxWidth, width, height);

        this.maxWidth = maxWidth;
    }

    public double getMaxWidth() {
        return maxWidth;
    }

    public void onMouseDragged(MouseEvent e) {
        if (null != BSpline && null != selectedPoint) {
            double oldMax = pixelCoordinateToAreaConverter.getEndX();
            double newX;
            double newY;

            if (e.getX() > width) {
                double gapX = e.getX() - width;
                double addPartX = (gapX / (double) width) * 2 * oldMax;
                newX = oldMax + addPartX;
                selectedPoint.setX(newX);
            } else {
                newX = pixelCoordinateToAreaConverter.toRealX(e.getX());
                selectedPoint.setX(newX);
            }

            if (e.getY() > height) {
                int gapY = e.getY() - height;
                double addPartY = (gapY / (double) height) * 2 * oldMax;
                newY = -oldMax - addPartY;
                selectedPoint.setY(newY);
            } else {
                newY = pixelCoordinateToAreaConverter.toRealY(height - e.getY());
                selectedPoint.setY(newY);
            }

            double newMax = Math.max(Math.abs(newX), Math.abs(newY));
            if (newMax > getMaxWidth()) {
                setMaxWidth(newMax);
            }

            drawSpline();
        }
    }

    private double getMax(ArrayList<Point<Double, Double>> list) {
        Optional<Double> maxValue = list
                .stream()
                .flatMap(point ->
                        ListUtil.asList(point.getX(), point.getY()).stream())
                .max(Comparator.comparingDouble(Math::abs));
        return maxValue.map(Math::abs).orElse(0.0);
    }

    public void scaleField(double scaleRate) {
        double newMaxField = getMaxWidth() * scaleRate;
        this.maxWidth = newMaxField;
        setMaxWidth(newMaxField);
        drawSpline();
    }

    public void autosizeField() {
        if (null != BSpline) {
            setMaxWidth(getMax(BSpline.getPoints()));
        }

        drawSpline();
    }
}
