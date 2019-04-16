package ru.nsu.fit.g16207.melnikov.view;

import ru.nsu.fit.g16207.melnikov.Configuration;
import ru.nsu.fit.g16207.melnikov.Main;
import ru.nsu.fit.g16207.melnikov.function.Function;
import ru.nsu.fit.g16207.melnikov.mf.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainPanel extends JPanel {
    private MainFrame frame;
    private BufferedImage colorsImage;
    private BufferedImage interpolationImage;
    private BufferedImage showedImage;
    private int widthOfImage;
    private int heightOfImage;
    private Configuration configuration;
    boolean grid;
    boolean isolines;
    boolean interpolation;
    public MainPanel(MainFrame frame, Configuration configuration) {
        this.frame = frame;
        this.configuration = configuration;
        grid = false;
        isolines = false;
        interpolation = false;
        showedImage = null;
        widthOfImage = Main.multiplyByFraction(7, 9, this.frame.getWidth());
        heightOfImage = Main.multiplyByFraction(7, 9, this.frame.getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(showedImage != null) {
            g.drawImage(showedImage, 0, 0, null);
        }
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
            red = Math.abs((int) (color.getRed() * attitude + colors[index - 1].getRed() * multiply));
            green = Math.abs((int) (color.getGreen() * attitude + colors[index - 1].getGreen() * multiply));
            blue = Math.abs((int) (color.getBlue() * attitude + colors[index - 1].getBlue() * multiply));
        }
        return (((red & 0xFF) << 16) |
                ((green & 0xFF) << 8)  |
                ((blue & 0xFF)));
    }

    private BufferedImage createImage(CalculateColor calculate) {
        BufferedImage image = new BufferedImage(widthOfImage, heightOfImage, BufferedImage.TYPE_3BYTE_BGR);
        Function function = configuration.getFunction();
        for(int i = 0; i < heightOfImage; i++) {
            double y = getValueFromRasterToArea(i, heightOfImage, function.getLowerBorder(), function.getHighBorder());
            for(int j = 0; j < widthOfImage; j++) {
                double x = getValueFromRasterToArea(j, widthOfImage, function.getLeftBorder(), function.getRightBorder());
                int color =  calculate.getColor(x, y);
                image.setRGB(j, i, color);
            }
        }
        return image;
    }

    private double getValueFromRasterToArea(int coordinate, int sizeOfRaster, double minVal, double maxVal) {
        double sizeArea = Math.abs(maxVal - minVal);
        double attitude = (double)coordinate / (double)sizeOfRaster;
        return sizeArea * attitude + minVal;
    }
    public void setInterpolation (boolean val) {
        interpolation = val;
        showedImage = interpolation ? interpolationImage : colorsImage;
        repaint();
    }

    public boolean getInterpolation() {
        return interpolation;
    }
}
