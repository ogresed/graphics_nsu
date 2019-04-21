package ru.nsu.fit.g16207.melnikov.view;

import ru.nsu.fit.g16207.melnikov.configuration.Configuration;
import ru.nsu.fit.g16207.melnikov.function.Function;
import ru.nsu.fit.g16207.melnikov.mf.StatusBar;
import ru.nsu.fit.g16207.melnikov.segment.Lines;
import ru.nsu.fit.g16207.melnikov.Main;
import ru.nsu.fit.g16207.melnikov.function.GridFunction;
import ru.nsu.fit.g16207.melnikov.segment.PixelSegment;
import ru.nsu.fit.g16207.melnikov.segment.Segment;
import ru.nsu.fit.g16207.melnikov.calculate.CalculateColor;
import ru.nsu.fit.g16207.melnikov.calculate.FunctionColor;
import ru.nsu.fit.g16207.melnikov.calculate.InterpolationColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static ru.nsu.fit.g16207.melnikov.Main.countCrosses;
import static ru.nsu.fit.g16207.melnikov.Main.getValueFromRasterToArea;
import static ru.nsu.fit.g16207.melnikov.Main.toPixel;

public class MainPanel extends JPanel {
    private final int OFFSET_FROM_IMAGE = 10;
    private final StatusBar statusBar;
    private BufferedImage colorsImage;
    private BufferedImage interpolationImage;
    private BufferedImage showedImage;
    private BufferedImage colorsLegend;
    private BufferedImage interpolationLegend;
    private BufferedImage showedLegend;
    private int widthOfImage;
    private int heightOfImage;
    private int legendOffset;
    private int widthForLegend;
    private Configuration configuration;
    private ArrayList<Double> levels;
    private ArrayList<PixelSegment> pixelSegments;
    private Lines lines;
    private boolean grid;
    private boolean isolines;
    private boolean interpolation;
    private boolean selected;
    private boolean enteringDot;

    public MainPanel(Configuration configuration, StatusBar statusBar) {
        this.configuration = configuration;
        this.statusBar = statusBar;
        functionColor = new FunctionColor(configuration);
        interpolationColor = new InterpolationColor(configuration);
        grid = false;
        isolines = false;
        interpolation = false;
        showedImage = null;
        MyMouseAdapter mouseAdapter = new MyMouseAdapter();
        addMouseMotionListener(mouseAdapter);
        addMouseListener(mouseAdapter);
        createSizes();
    }

    private void createSizes() {
        widthOfImage = Main.multiplyByFraction(7, 9, this.getWidth());
        heightOfImage = this.getHeight();
        legendOffset = widthOfImage + OFFSET_FROM_IMAGE;
        widthForLegend = (this.getWidth() - 2*OFFSET_FROM_IMAGE - widthOfImage) / 5;
    }

    class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            event(e.getX(), e.getY());
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            if(!(x > widthOfImage || y > heightOfImage)) {
                GridFunction gridFunction = configuration.getGridFunction();
                double xF = getValueFromRasterToArea(
                        x, widthOfImage, gridFunction.getLeftBorder(), gridFunction.getRightBorder());
                double yF = getValueFromRasterToArea(
                        y, heightOfImage, gridFunction.getLowerBorder(), gridFunction.getHighBorder());
                statusBar.setMessage(xF, yF, gridFunction.getFunction().function(xF, yF));
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            event(e.getX(), e.getY());
        }

        private void event(int x, int y) {
            if(levels != null && isolines && !(x > widthOfImage || y > heightOfImage)) {
                GridFunction gridFunction = configuration.getGridFunction();
                double xF = getValueFromRasterToArea(
                        x, widthOfImage, gridFunction.getLeftBorder(), gridFunction.getRightBorder());
                double yF = getValueFromRasterToArea(
                        y, heightOfImage, gridFunction.getLowerBorder(), gridFunction.getHighBorder());
                levels.add(gridFunction.getFunction().function(xF, yF));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(selected) {
            createSizes();
            createImage();
            createLegend();
        }
        if(showedImage != null) {
            g.drawImage(showedImage, 0, 0, null);
        }
        if(showedLegend != null) {
            g.drawImage(showedLegend,  legendOffset, 0, null);
            writeKeys(g);
        }
        if(grid) {
            drawGrid(g);
        }
        if(isolines) {
            createPixelSegments();
            Color previousColor = g.getColor();
            g.setColor(configuration.getAllColors()[configuration.getValuesNumber() + 1]);
            for(PixelSegment segment : pixelSegments) {
                g.drawLine(segment.x1, segment.y1, segment.x2, segment.y2);
            }
            g.setColor(previousColor);
            repaint();
        }
        if(enteringDot) {
            for(PixelSegment segment : pixelSegments) {
                drawEndsOfSegment(segment, g);
            }
        }
    }

    private CalculateColor functionColor;
    private CalculateColor interpolationColor;

    public void createImage() {
        Function function = configuration.getGridFunction().getFunction();
        colorsImage = createImage(widthOfImage, heightOfImage, function, functionColor);
        interpolationImage = createImage(widthOfImage, heightOfImage, function, interpolationColor);
        showedImage = interpolation ? interpolationImage : colorsImage;
        repaint();
    }

    public void createLegend() {
        GridFunction gridFunction = configuration.getGridFunction();
        Function function = new Function() {
            @Override
            public double function(double x, double y) {
                return y;
            }
        };
        function.setYmin(gridFunction.getMinFunction());
        function.setYmax(gridFunction.getMaxFunction());
        colorsLegend = createImage(widthForLegend, heightOfImage, function, functionColor);
        interpolationLegend = createImage(widthForLegend, heightOfImage, function, interpolationColor);
        showedLegend = interpolation ? interpolationLegend : colorsLegend;
        repaint();
    }

    private BufferedImage createImage(int width, int height, Function function, CalculateColor calculate) {
        try {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            double x, y;
            int color;
            for(int i = 0; i < height; i++) {
                y = getValueFromRasterToArea(i, height, function.getYmin(), function.getYmax());
                for(int j = 0; j < width; j++) {
                    x = getValueFromRasterToArea(j, width, function.getXmin(), function.getXmax());
                    color =  calculate.getColor(function.function(x, y));
                    image.setRGB(j, i, color);
                }
            }
            return image;
        } catch (IllegalArgumentException | NegativeArraySizeException ignore) {
        }
        return null;
    }

    private void drawGrid(Graphics g) {
        //set color
        Color previousColor = g.getColor();
        g.setColor(Color.white);
        //get values
        GridFunction function = configuration.getGridFunction();
        double x = function.getLeftBorder();
        double y = function.getLowerBorder();
        double xOffset = function.getOffsetOfHorizontal();
        double yOffset = function.getOffsetOfVertical();
        //drawing
        int xSize = function.getNumberHorizontalDotes();
        int ySize = function.getNumberVerticalDotes();
        for(int i = 0; i  < xSize; i++) {
            int pixelX = toPixel(x, widthOfImage, function.getLeftBorder(), function.getRightBorder());
            g.drawLine(pixelX, 0, pixelX, heightOfImage - 1);
            x+=xOffset;
        }
        for(int i = 0; i  < ySize; i++) {
            int pixelY = toPixel(y, heightOfImage, function.getLowerBorder(), function.getHighBorder());
            g.drawLine(0, pixelY, widthOfImage - 1, pixelY);
            y+=yOffset;
        }
        g.setColor(previousColor);
    }

    private void writeKeys(Graphics g) {
        double keys[] = configuration.getGridFunction().getKeyValues();
        int offset = Main.multiplyByFraction(1, keys.length - 1, heightOfImage);
        int alignment = 3;
        int y = offset;
        int len = keys.length;
        for(int i = 1; i < len - 1; i++) {
            g.drawString(String.format("%.2f", keys[i]),
                    legendOffset + widthForLegend + OFFSET_FROM_IMAGE, y + alignment);
            y+=offset;
        }
    }

    private void drawEndsOfSegment(PixelSegment segment, Graphics g) {
        int sizeOfOval = 4;
        drawPaintingOval(segment.x1, segment.y1,sizeOfOval, g);
        drawPaintingOval(segment.x2, segment.y2,sizeOfOval, g);
    }

    private void drawPaintingOval(int x, int y, int size, Graphics g) {
        int offset = size / 2;
        g.drawOval(x - offset, y - offset, size, size);
        g.fillOval(x - offset, y - offset, size, size);
    }

    public void createAllIsolines(GridFunction function) {
        createSizes();
        lines = new Lines(function);
        createBaseIsolines();
    }

    private void createBaseIsolines() {
        levels = new ArrayList<>();
        double keys[] = configuration.getGridFunction().getKeyValues();
        for(int i = 1; i <= configuration.getValuesNumber(); i++) {
            levels.add(keys[i]);
        }
        createPixelSegments();
    }

    private void createPixelSegments() {
        pixelSegments = new ArrayList<>();
        for(double level: levels) {
            createIsoline(level);
        }
    }

    private void createIsoline(double level) {
        GridFunction function = configuration.getGridFunction();
        Segment verticalLines[][] = lines.getVerticalLines();
        Segment horizontalLines[][] = lines.getHorizontalLines();
        // T L Ш З
        Segment cell[] = new Segment[4];
        for(int j = 0; j < function.getNumberHorizontalDotes() - 1; j++) {
            horizontalLines[0][j].indicateCrossDot(level);
        }
        for(int i = 0; i < function.getNumberVerticalDotes() - 1; i++) {
            cell[0] = horizontalLines[i][0];
            cell[1] = verticalLines[0][i].indicateCrossDot(level);
            for(int j = 0; j < function.getNumberHorizontalDotes() - 1; j++) {
                cell[2] = horizontalLines[i+1][j].indicateCrossDot(level);
                cell[3] = verticalLines[j+1][i].indicateCrossDot(level);
                createPartOfIsoline(cell, level);
                cell[1] = cell[3];
                if(j+1 != function.getNumberHorizontalDotes() - 1) {
                    cell[0] = horizontalLines[i][j + 1];
                }
            }
        }
    }

    private void createPartOfIsoline(Segment[] cell, double level) {
        int number = countCrosses(cell);
        int firstIndex ,secondIndex;
        if(number == 2) {
            for(firstIndex = 0; firstIndex < 3; firstIndex++) {
                if(cell[firstIndex].isCrossed()) {
                    break;
                }
            }
            for(secondIndex = 1; secondIndex < 4; secondIndex++) {
                if(firstIndex != secondIndex && cell[secondIndex].isCrossed()) {
                    break;
                }
            }
            savePart(cell[firstIndex], cell[secondIndex]);
        }
        else if (number == 3) {
            createIsoline(level+ 0.1);
        }
        else if(number == 4) {
            double f = (cell[0].getF1() + cell[0].getF2() + cell[2].getF1() + cell[2].getF2()) / 4.0;
            if(cell[0].crosses(f) && cell[1].crosses(f)) {
                savePart(cell[0], cell[1]);
                savePart(cell[2], cell[3]);
            }
            else {
                savePart(cell[0], cell[3]);
                savePart(cell[1], cell[2]);
            }
        }
    }

    private void savePart(Segment first, Segment second) {
        double firstX = first.getCrossAbscissa();
        double firstY = first.getCrossOrdinate();
        double secondX = second.getCrossAbscissa();
        double secondY = second.getCrossOrdinate();
        int x1 = toPixel(firstX, widthOfImage, configuration.getGridFunction().getLeftBorder(), configuration.getGridFunction().getRightBorder());
        int x2 = toPixel(secondX, widthOfImage, configuration.getGridFunction().getLeftBorder(), configuration.getGridFunction().getRightBorder());
        int y1 = toPixel(firstY, heightOfImage, configuration.getGridFunction().getLowerBorder(), configuration.getGridFunction().getHighBorder());
        int y2 = toPixel(secondY, heightOfImage, configuration.getGridFunction().getLowerBorder(), configuration.getGridFunction().getHighBorder());
        pixelSegments.add(new PixelSegment(x1, x2, y1, y2));
    }

    public void clearExcessLines() {
        if(levels.size() > configuration.getValuesNumber()) {
            createBaseIsolines();
        }
    }

    public void setInterpolation () {
        interpolation = !interpolation;
        showedImage = interpolation ? interpolationImage : colorsImage;
        showedLegend = interpolation ? interpolationLegend :  colorsLegend;
        repaint();
    }

    public void setIsolines() {
        isolines = !isolines;
        repaint();
    }

    public void setGrid() {
        grid = !grid;
        repaint();
    }

    public void setEnteringDot() {
        this.enteringDot = !enteringDot;
        repaint();
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
