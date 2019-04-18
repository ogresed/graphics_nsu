package ru.nsu.fit.g16207.melnikov.view;

import ru.nsu.fit.g16207.melnikov.configuration.Configuration;
import ru.nsu.fit.g16207.melnikov.function.Function;
import ru.nsu.fit.g16207.melnikov.segment.Lines;
import ru.nsu.fit.g16207.melnikov.Main;
import ru.nsu.fit.g16207.melnikov.function.GridFunction;
import ru.nsu.fit.g16207.melnikov.mf.MainFrame;
import ru.nsu.fit.g16207.melnikov.segment.Segment;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static ru.nsu.fit.g16207.melnikov.Main.getValueFromRasterToArea;
import static ru.nsu.fit.g16207.melnikov.Main.toPixel;

public class MainPanel extends JPanel {
    private final int OFFSET_FROM_IMAGE = 10;
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
    private Lines lines;
    private boolean grid;
    private boolean isolines;
    private boolean interpolation;
    public MainPanel(MainFrame mainFrame, Configuration configuration) {
        this.configuration = configuration;
        grid = false;
        isolines = false;
        interpolation = false;
        showedImage = null;
        widthOfImage = Main.multiplyByFraction(7, 9, mainFrame.getWidth());
        heightOfImage = Main.multiplyByFraction(7, 9, mainFrame.getHeight());
        legendOffset = widthOfImage + OFFSET_FROM_IMAGE;
        widthForLegend = (mainFrame.getWidth() - 2*OFFSET_FROM_IMAGE - widthOfImage) / 5;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
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
            drawAllIsolines(g);
        }
    }

    private void drawAllIsolines(Graphics g) {
        Color previousColor = g.getColor();
        g.setColor(configuration.getAllColors()[configuration.getValuesNumber() + 1]);
        for(double level: levels) {
            drawIsoline(level, g);
        }
        g.setColor(previousColor);
    }

    private void drawIsoline(double level, Graphics g) {
        GridFunction function = configuration.getGridFunction();
        Segment verticalLines[][] = lines.getVerticalLines();
        Segment horizontalLines[][] = lines.getHorizontalLines();
        // T L Ш З
        Segment cell[] = new Segment[4];
        for(int j = 0; j < function.getNumberHorizontalDotes() - 1; j++) {
            horizontalLines[0][j].indicateCrossDot(level);
        }
        for(int i = 0; i < function.getNumberHorizontalDotes() - 1; i++) {
            verticalLines[i][0].indicateCrossDot(level);
            for(int j = 0; j < function.getNumberVerticalDotes() - 1; j++) {
                verticalLines[i][j+1].indicateCrossDot(level);
                horizontalLines[i+1][j].indicateCrossDot(level);
                cell[0] = horizontalLines[i][j];
                cell[1] = verticalLines[i][j];
                cell[2] = horizontalLines[i+1][j];
                cell[3] = verticalLines[i][j+1];
                drawLine(cell, g);
            }
        }
    }

    private void drawLine(Segment[] cell, Graphics g) {
        GridFunction function = configuration.getGridFunction();
        int x1 = 0, x2 = 100, y1 = 0, y2 = 100;
        if(countCrosses(cell) == 2) {
            for(int i = 0; i < 3; i++) {
                if(cell[i].isCrossed()) {
                    x1 = toPixel(cell[i].getCrossAbscissa(), widthOfImage,
                            function.getLeftBorder(), function.getRightBorder());
                    y1 = toPixel(cell[i].getCrossOrdinate(), heightOfImage,
                            function.getLowerBorder(), function.getHighBorder());
                }
            }
            for(int i = 1; i < 4; i++) {
                if(cell[i].isCrossed()) {
                    x2 = toPixel(cell[i].getCrossAbscissa(), widthOfImage,
                            function.getLeftBorder(), function.getRightBorder());
                    y2 = toPixel(cell[i].getCrossOrdinate(), heightOfImage,
                            function.getLowerBorder(), function.getHighBorder());
                }
            }
            g.drawLine(x1, y1, x2, y2);
        }
    }

    private int countCrosses(Segment[] cell) {
        int i = 0;
        for (int j = 0; j < 4; j++) {
            if(cell[j].isCrossed()) {
                i++;
            }
        }
        return i;
    }

    private void writeKeys(Graphics g) {
        double keys[] = configuration.getGridFunction().getKeyValues();
        int offset = Main.multiplyByFraction(1, keys.length - 1, heightOfImage);
        int alignment = 3;
        int y = offset;
        int len = keys.length;
        for(int i = 1; i < len - 1; i++) {
            g.drawString(String.format("%.2f", keys[len - 1 - i]),
                    legendOffset + widthForLegend + OFFSET_FROM_IMAGE, y + alignment);
            y+=offset;
        }
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
            int pixelY = toPixel(y, heightOfImage, function.getLowerBorder(), function.getRightBorder());
            g.drawLine(0, pixelY, widthOfImage - 1, pixelY);
            y+=yOffset;
        }
        g.setColor(previousColor);
    }

    public void setLevelsAndLines(GridFunction function) {
        lines = new Lines(function);
        levels = new ArrayList<>();
        for(double val : function.getKeyValues()) {
            levels.add(val);
        }
    }

    public void createImage() {
        Function function = configuration.getGridFunction().getFunction();
        colorsImage = createImage(widthOfImage, heightOfImage, function, this::getFunctionColor);
        interpolationImage = createImage(widthOfImage, heightOfImage, function, this::getInterpolColor);
        showedImage = interpolation ? interpolationImage : colorsImage;
        repaint();
    }

    private BufferedImage createImage(int width, int height, Function function, CalculateColor calculate) {
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
    }

    public void createLegend() {
        GridFunction gridFunction = configuration.getGridFunction();
        Function function = new Function() {
            @Override
            public double function(double x, double y) {
                return -y;
            }
        };
        function.setYmin(gridFunction.getMinFunction());
        function.setYmax(gridFunction.getMaxFunction());
        colorsLegend = createImage(widthForLegend, heightOfImage, function, this::getFunctionColor);
        interpolationLegend = createImage(widthForLegend, heightOfImage, function, this::getInterpolColor);
        showedLegend = interpolation ? interpolationLegend : colorsLegend;
        repaint();
    }
    /**
     * return color of pixel by coordinates in functions area
     * */
    private int getFunctionColor(double value) {
        int index = configuration.getIndexOfColorByValue(value);
        return configuration.getAllColors()[index].getRGB();
    }

    private int getInterpolColor(double value) {
        double[] keyValues = configuration.getGridFunction().getKeyValues();
        Color [] colors = configuration.getAllColors();
        int index = configuration.getIndexOfColorByValue(value);
        Color color = colors[index];
        double keysOffset = configuration.getGridFunction().getOffsetOfKeyValue();
        double attitude = Math.abs((value - keyValues[index]) / keysOffset);
        double multiply = 1 - attitude;
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        if(index != 0) {
            red = Math.abs((int) (red * attitude + colors[index - 1].getRed() * multiply));
            green = Math.abs((int) (green * attitude + colors[index - 1].getGreen() * multiply));
            blue = Math.abs((int) (blue * attitude + colors[index - 1].getBlue() * multiply));
        }
        return (((red & 0xFF) << 16) |
                ((green & 0xFF) << 8)  |
                ((blue & 0xFF)));
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
}
