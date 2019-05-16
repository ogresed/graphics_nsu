package ru.nsu.fit.g16207.melnikov.settings;

import ru.nsu.fit.g16207.melnikov.mf.StatusBar;
import ru.nsu.fit.g16207.melnikov.pair.Pair;
import ru.nsu.fit.g16207.melnikov.utils.MathUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;

class BSplinePanel extends JPanel {
    BSplineDrawer splineDrawer;
    /*private final Color showingColor = Color.WHITE;
    private final Color invisibleColor = Color.GRAY;*/
    private final Color axisColor = Color.YELLOW;
    private final int sizeofCircle = 12;
    private final int offset = 5;
    private double coefficient = 1.0;
    private LinkedList<Pair<Double>> dots = new LinkedList<>();
    private int areaWidth;
    private int areaHeight;
    private int verticalAxis;
    private int horizontalAxis;
    private Zoom zoom;
    private StatusBar statusBar;

    BSplinePanel(Settings settings, StatusBar bar) {
        splineDrawer = new BSplineDrawer(dots);
        statusBar = bar;
        zoom = new Zoom(this, 19, 41, 0.05);
        areaWidth = MathUtil.multiplyByFraction(2, 3, settings.getWidth());
        areaHeight = MathUtil.multiplyByFraction(2, 3, settings.getHeight());
        horizontalAxis = areaHeight/2 + offset;
        verticalAxis = areaWidth/2 + offset;
        setSize(areaWidth, areaHeight);
        MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
        addMouseListener(myMouseAdapter);
        addMouseMotionListener(myMouseAdapter);
        addMouseWheelListener(new MyWheelListener());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.fillRect(offset, offset, areaWidth, areaHeight);
        drawAxises(g);
        drawCircles(g);
    }

    private void drawAxises(Graphics g) {
        Color previousColor = g.getColor();
        g.setColor(axisColor);
        //horizontal
        g.drawLine(offset, horizontalAxis,
                offset + areaWidth, horizontalAxis);
        //vertical
        g.drawLine(verticalAxis, offset,
                verticalAxis, offset + areaHeight);
        g.setColor(previousColor);
    }

    private void drawCircles(Graphics g) {
        Color previousColor = g.getColor();
        g.setColor(Color.WHITE);
        if(dots.size() != 0) {
            drawOval(fromDoubleToInt(dots.getFirst().getFirst())- sizeofCircle / 2,
                    fromDoubleToInt(dots.getFirst().getSecond() - sizeofCircle / 2), g);
            /*g.drawOval(dots.getFirst().getFirst() - sizeofCircle / 2, dots.getFirst().getSecond() - sizeofCircle / 2,
                    sizeofCircle, sizeofCircle);*/
        }
        for(int i = 1; i < dots.size(); i++) {
            int x1 = fromDoubleToInt(dots.get(i-1).getFirst());
            int y1 = fromDoubleToInt(dots.get(i-1).getSecond());
            int x2 = fromDoubleToInt(dots.get(i).getFirst());
            int y2 = fromDoubleToInt(dots.get(i).getSecond());
            g.drawLine(x1, y1, x2, y2);
            drawOval(x1 - sizeofCircle/2, y1 - sizeofCircle/2, g);
            if(i == dots.size() - 1) {
                drawOval(x2 - sizeofCircle/2, y2 - sizeofCircle/2, g);
            }
        }
        g.setColor(previousColor);
    }

    private int fromDoubleToInt (double val) {
        return  (int) Math.round(val);
    }

    private void drawOval(int x, int y, Graphics g) {
        if(!isOutside(x, y)) {
            g.drawOval(x, y, sizeofCircle, sizeofCircle);
        }
    }

    private boolean isOutside(int x, int y) {
        return !(x > offset && x < offset + areaWidth &&
                y > offset && y < offset + areaHeight);
    }

    private class MyWheelListener implements MouseWheelListener {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if(e.getWheelRotation() == 1) {
                zoom.negative();
            }
            else if(e.getWheelRotation() == -1) {
                zoom.positive();
            }
            statusBar.setMessage(String.format("%.2f X      CMB - clear spline", zoom.currentValue()));
        }
    }

    class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            if(x >= offset && x <= areaWidth &&
                    y >= offset && y <= areaHeight) {
                Pair dot = nearestDot(x, y);
                //add dot in left mouse press
                if (SwingUtilities.isLeftMouseButton(e) && dot == null) {
                    dots.add(new Pair<>((double)e.getX(), (double)e.getY()));
                    repaint();
                //remove dot in right mouse press
                } else if(dot != null && SwingUtilities.isRightMouseButton(e)) {
                    dots.remove(dot);
                    repaint();
                } else if(SwingUtilities.isMiddleMouseButton(e)) {
                    dots.clear();
                    repaint();
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            double x = e.getX();
            double y = e.getY();
            Pair dot = nearestDot(x, y);
            if (dot != null &&
                    x >= offset && x <= areaWidth &&
                    y >=offset && y <= areaHeight &&
                    SwingUtilities.isLeftMouseButton(e)) {
                dot.setFirst(x);
                dot.setSecond(y);
                repaint();
            }
        }
        private Pair nearestDot(double x, double y) {
            for(Pair pair : dots) {
                double d = Math.sqrt(Math.pow((double)pair.getFirst() - x, 2) +
                        Math.pow((double)pair.getSecond() - y, 2));
                if(d <= sizeofCircle) {
                    return pair;
                }
            }
            return null;
        }

    }

    void setCoefficient(double newCoefficient) {
        double workCoefficient = newCoefficient / coefficient;
        for(Pair dot : dots) {
            double  widthLength = verticalAxis - (workCoefficient * (verticalAxis - (double) dot.getFirst()));
            double heightLength = horizontalAxis - (workCoefficient * (horizontalAxis - (double) dot.getSecond()));
            dot.setFirst(widthLength);
            dot.setSecond(heightLength);
        }
        this.coefficient = newCoefficient;
        repaint();
    }
}
