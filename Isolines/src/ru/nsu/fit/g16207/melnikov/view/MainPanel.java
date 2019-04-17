package ru.nsu.fit.g16207.melnikov.view;

import ru.nsu.fit.g16207.melnikov.Configuration;
import ru.nsu.fit.g16207.melnikov.Main;
import ru.nsu.fit.g16207.melnikov.function.Function;
import ru.nsu.fit.g16207.melnikov.mf.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainPanel extends JPanel {
    private final int OFFSET_FROM_IMAGE = 10;
    private MainFrame frame;
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
    private boolean grid;
    private boolean isolines;
    private boolean interpolation;
    public MainPanel(MainFrame frame, Configuration configuration) {
        this.frame = frame;
        this.configuration = configuration;
        grid = false;
        isolines = false;
        interpolation = false;
        showedImage = null;
        widthOfImage = Main.multiplyByFraction(7, 9, this.frame.getWidth());
        heightOfImage = Main.multiplyByFraction(7, 9, this.frame.getHeight());
        legendOffset = widthOfImage + OFFSET_FROM_IMAGE;
        widthForLegend = frame.getWidth() - 2*OFFSET_FROM_IMAGE - widthOfImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(showedImage != null) {
            g.drawImage(showedImage, 0, 0, null);
        }
        if(showedLegend != null) {
            g.drawImage(showedLegend,  legendOffset, 0, null);
            writeKeys();
        }
        if(grid) {
            drawGrid(g);
        }
    }

    private void writeKeys() {
        double keys[] = configuration.getFunction().getKeyValues();
        for(int i = 1; i < keys.length - 2; i++) {
            
        }
    }

    private void drawGrid(Graphics g) {
        //set color
        Color previousColor = g.getColor();
        g.setColor(Color.white);
        //calculate values
        Function function = configuration.getFunction();
        //- 1 потому что учитываются крайние значения
        int cells = function.getNumberHorizontallyDotes()- 1;
        int XPixelOffset = (widthOfImage + cells -1)/ (cells);
        cells = (function.getNumberVerticallyDotes()-1);
        int YPixelOffset = (heightOfImage + cells -1)/ (cells);
        //drawing
        int offset = 0;
        for(int i = 0; i  < configuration.getXSize(); i++) {
            g.drawLine(offset, 0, offset, heightOfImage - 1);
            offset+=XPixelOffset;
        }
        offset = 0;
        for(int i = 0; i  < configuration.getYSize(); i++) {
            g.drawLine(0, offset, widthOfImage - 1, offset);
            offset+=YPixelOffset;
        }
        g.setColor(previousColor);
    }

    public interface CalculateColor {

        int getColor(double x, double y);

    }
    public void createAndShowImage() {
        colorsImage = createImage(this::getFunctionColor);
        interpolationImage = createImage(this::getInterpolationColor);
        showedImage = colorsImage;
        repaint();
    }
    public void createLegend() {
        BufferedImage colorL = new BufferedImage(widthForLegend, heightOfImage, BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage interpolationL = new BufferedImage(widthForLegend, heightOfImage, BufferedImage.TYPE_3BYTE_BGR);
        Function function = configuration.getFunction();
        double offsetByKeyValuesScale = Math.abs(function.getMaxFunction() - function.getMinFunction())
                / (1.0 * heightOfImage);
        int interpolImageColor;
        int colorImageColor;
        double val = function.getMinFunction();
        Color colors[] = configuration.getAllColors();
        for(int i = 0; i < heightOfImage; i++) {
            // calc two colors to images
            colorImageColor = colors[configuration.getIndexOfColorByValue(val)].getRGB();
            interpolImageColor = getInterpolColor(val);
            //set colors
            for(int j = 0; j < widthForLegend; j++) {
                colorL.setRGB(j, i, colorImageColor);
                interpolationL.setRGB(j, i, interpolImageColor);
            }
            val+=offsetByKeyValuesScale;
        }
        colorsLegend = colorL;
        interpolationLegend = interpolationL;
        showedLegend = colorsLegend;
    }

    /**
     * return color of pixel by coordinates in functions area
     * */
    private int getFunctionColor(double x, double y) {
        double value = configuration.getFunction().operation(x, y);
        int index = configuration.getIndexOfColorByValue(value);
        return configuration.getAllColors()[index].getRGB();
    }

    private int getInterpolationColor(double x, double y) {
        double value = configuration.getFunction().operation(x, y);
        return getInterpolColor(value);
    }

    private int getInterpolColor(double value) {
        double[] keyValues = configuration.getFunction().getKeyValues();
        Color [] colors = configuration.getAllColors();
        int index = configuration.getIndexOfColorByValue(value);
        Color color = colors[index];
        double keysOffset = configuration.getFunction().getOffsetOfKeyValue();
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

    private BufferedImage createImage(CalculateColor calculate) {
        BufferedImage image = new BufferedImage(widthOfImage, heightOfImage, BufferedImage.TYPE_3BYTE_BGR);
        Function function = configuration.getFunction();
        double x, y;
        int color;
        for(int i = 0; i < heightOfImage; i++) {
            y = getValueFromRasterToArea(i, heightOfImage, function.getLowerBorder(), function.getHighBorder());
            for(int j = 0; j < widthOfImage; j++) {
                x = getValueFromRasterToArea(j, widthOfImage, function.getLeftBorder(), function.getRightBorder());
                color =  calculate.getColor(x, y);
                image.setRGB(j, i, color);
            }
        }
        return image;
    }

    private double getValueFromRasterToArea(int coordinate, int sizeOfRaster, double minVal, double maxVal) {
        double sizeArea = Math.abs(maxVal * 1.0 - minVal * 1.0);
        double attitude = (double)coordinate / (double)sizeOfRaster;
        return sizeArea * attitude + minVal;
    }
    public void setInterpolation () {
        interpolation = !interpolation;
        showedImage = interpolation ? interpolationImage : colorsImage;
        showedLegend = interpolation ? interpolationLegend :  colorsLegend;
        repaint();
    }

    public boolean getInterpolation() {
        return interpolation;
    }

    public void setGrid() {
        grid = !grid;
        repaint();
    }
}
