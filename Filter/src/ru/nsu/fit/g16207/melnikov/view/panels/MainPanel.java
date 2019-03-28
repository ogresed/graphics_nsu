package ru.nsu.fit.g16207.melnikov.view.panels;

import ru.nsu.fit.g16207.melnikov.controller.FilterController;
import ru.nsu.fit.g16207.melnikov.model.event.FilterApplied;
import ru.nsu.fit.g16207.melnikov.model.filter.*;
import ru.nsu.fit.g16207.melnikov.view.ValueChangedHandler;
import ru.nsu.fit.g16207.melnikov.view.frame.forms.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
public class MainPanel extends JPanel {
    private static final int DEFAULT_WIDTH = 1170;
    private static final int DEFAULT_HEIGHT = 450;

    private static final int IMAGE_SIZE = 350;
    private static final int MARGIN = 10;
    private static final int INDENT = 50;

    private BufferedImage originalImage;
    private BufferedImage displayedOriginalImage;
    private BufferedImage selectedImage;
    private BufferedImage filteredImage;

    private int[] squareCoordinates;

    private double widthResizeCoefficient;
    private double heightResizeCoefficient;

    private FilterController filterController;

    public MainPanel(int width, int height) {
        setBounds(0, 0, width, height);

        ImageListener imageListener = new ImageListener();
        addMouseMotionListener(imageListener);
        addMouseListener(imageListener);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.drawRect(INDENT, INDENT, IMAGE_SIZE, IMAGE_SIZE);
        graphics.drawRect(IMAGE_SIZE + INDENT + MARGIN, INDENT, IMAGE_SIZE, IMAGE_SIZE);
        graphics.drawRect(2 * IMAGE_SIZE + INDENT + 2 * MARGIN, INDENT, IMAGE_SIZE, IMAGE_SIZE);

        if (displayedOriginalImage != null) {
            ((Graphics2D) graphics).drawImage(displayedOriginalImage, null, INDENT, INDENT);
        }
        if (selectedImage != null) {
            ((Graphics2D) graphics).drawImage(selectedImage, null, IMAGE_SIZE + INDENT + MARGIN, INDENT);
        }
        if (selectedImage != null) {
            ((Graphics2D) graphics).drawImage(filteredImage, null, 2 * IMAGE_SIZE + INDENT + 2 * MARGIN, INDENT);
        }
        if (squareCoordinates != null) {
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
            graphics2D.setStroke(stroke);
            graphics2D.drawRect(squareCoordinates[0], squareCoordinates[1], squareCoordinates[2], squareCoordinates[3]);
            graphics2D.dispose();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public void drawOriginalImage(BufferedImage image) {
        originalImage = image;
        if ((image.getWidth() > IMAGE_SIZE) || (image.getHeight() > IMAGE_SIZE)) {
            resizeImage();
            repaint();
            return;
        }
        widthResizeCoefficient = 1;
        heightResizeCoefficient = 1;
        displayedOriginalImage = image;
        repaint();
    }

    private void resizeImage() {
        double attitude = originalImage.getWidth() * 1.0 / originalImage.getHeight();
        int newWidth;
        int newHeight;
        if (attitude == 1.0) {
            widthResizeCoefficient = IMAGE_SIZE * 1.0 / originalImage.getWidth();
            heightResizeCoefficient = IMAGE_SIZE * 1.0 / originalImage.getHeight();
            newWidth = IMAGE_SIZE;
            newHeight = IMAGE_SIZE;
        } else if (attitude < 1.0) {
            heightResizeCoefficient = IMAGE_SIZE * 1.0 / originalImage.getHeight();
            widthResizeCoefficient = IMAGE_SIZE * 1.0 / originalImage.getHeight();
            newHeight = IMAGE_SIZE;
            newWidth = (int) (originalImage.getWidth() * widthResizeCoefficient);
        } else {
            heightResizeCoefficient = IMAGE_SIZE * 1.0 / originalImage.getWidth();
            widthResizeCoefficient = IMAGE_SIZE * 1.0 / originalImage.getWidth();
            newWidth = IMAGE_SIZE;
            newHeight = (int) (originalImage.getHeight() * heightResizeCoefficient);
        }

        displayedOriginalImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D resizedImageGraphics = displayedOriginalImage.createGraphics();
        resizedImageGraphics.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        resizedImageGraphics.dispose();
    }
    private void drawSelectedImage(int startX, int startY) {
        int width = IMAGE_SIZE;
        int height = IMAGE_SIZE;
        if (startX + width >= originalImage.getWidth()) {
            width -= ((startX + width) - originalImage.getWidth());
        }
        if (startY + height >= originalImage.getHeight()) {
            height -= ((startY + height) - originalImage.getHeight());
        }
        selectedImage = originalImage.getSubimage(startX, startY, width, height);
    }

    private void drawSquare(int startX, int startY, int width, int height) {
        squareCoordinates = new int[4];
        squareCoordinates[0] = startX;
        squareCoordinates[1] = startY;
        squareCoordinates[2] = width;
        squareCoordinates[3] = height;
    }

    public void makeGray() {
        if (checkIfImagePartIsSelected()) {
            return;
        }
        filterController.makeFilter(new GrayScaleFilter(), selectedImage);
    }

    public void makeNegative() {
        if (checkIfImagePartIsSelected()) {
            return;
        }
        filterController.makeFilter(new NegativeFilter(), selectedImage);
    }

    public void makeFloydSteinbergDithering() {
        if (checkIfImagePartIsSelected()) {
            return;
        }
        new FloydSteinbergForm(filterController, selectedImage);
    }
    public void makeOrderedDithering() {
        if (checkIfImagePartIsSelected()) {
            return;
        }
        int matrixSize = 16;
        filterController.makeFilter(new OrderedDitheringFilter(matrixSize), selectedImage);
    }
    public void makeSharpening() {
        if (checkIfImagePartIsSelected()) {
            return;
        }
        filterController.makeFilter(new SharpeningFilter(), selectedImage);
    }
    public void makeEmbossing() {
        if (checkIfImagePartIsSelected()) {
            return;
        }
        filterController.makeFilter(new EmbossingFilter(), selectedImage);
    }
    public void makeWaterColoring() {
        if (checkIfImagePartIsSelected()) {
            System.out.println(123456);
            return;
        }
        filterController.makeFilter(new WatercoloringFilter(), selectedImage);
    }
    public void makeMedian() {
        if (checkIfImagePartIsSelected()) {
            return;
        }
        filterController.makeFilter(new MedianFilter(), selectedImage);
    }
    public void makeBlur() {
        if (checkIfImagePartIsSelected()) {
            return;
        }
        filterController.makeFilter(new BlurFilter(), selectedImage);
    }
    public void makeGamma() {
        if (checkIfImagePartIsSelected()) {
            return;
        }
        new GammaForm(filterController, selectedImage);
    }
    public void makeRotation() {
        if (checkIfImagePartIsSelected()) {
            return;
        }
        new RotationForm(filterController, selectedImage);
    }
    public void makeDoubling() {
        if (checkIfImagePartIsSelected()) {
            return;
        }
        int startX = selectedImage.getWidth() / 4;
        int startY = selectedImage.getHeight() / 4;
        int newWidth = selectedImage.getWidth() / 2;
        int newHeight = selectedImage.getHeight() / 2;
        BufferedImage subImage = selectedImage.getSubimage(startX, startY, newWidth, newHeight);
        filterController.makeFilter(new DoublingFilter(), subImage);
    }
    public void makeRoberts() {
        if (checkIfImagePartIsSelected()) {
            return;
        }
        new RobertsForm(filterController, selectedImage);
    }
    public void makeSobel() {
        if (checkIfImagePartIsSelected()) {
            return;
        }
        new SobelForm(filterController, selectedImage);
    }
    public void save(File file) throws IOException {
        filterController.saveImage(file, filteredImage);
    }
    public void makeR2Bmp() {
        ColorModel colorModel = filteredImage.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster raster = filteredImage.copyData(null);
        selectedImage = new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
        repaint();
    }
    public void newDocument() {
        originalImage = null;
        displayedOriginalImage = null;
        selectedImage = null;
        filteredImage = null;
        repaint();
    }
    public void setFilterController(FilterController filterController) {
        this.filterController = filterController;
        filterController.setHandler(new FilterAppliedHandler());
    }
    private boolean checkIfImagePartIsSelected() {
        if (selectedImage == null) {
            JOptionPane.showMessageDialog(MainPanel.this, "Please, select a part of image first");
            return true;
        }
        return false;
    }
    private boolean checkIfImageIsChosen() {
        if (originalImage == null) {
            JOptionPane.showMessageDialog(MainPanel.this, "Please, choose image in a gallery first");
            return false;
        }
        return true;
    }
    private class ImageListener extends MouseAdapter {
        private int startX;
        private int startY;
        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            super.mousePressed(mouseEvent);
            if (!checkIfImageIsChosen()) {
                return;
            }
            if (checkCoordinates(mouseEvent.getX(), mouseEvent.getY())) {
                startX = transformX(mouseEvent.getX());
                startY = transformY(mouseEvent.getY());
            }
        }
        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
            super.mouseReleased(mouseEvent);
            squareCoordinates = null;
            repaint();
        }
        @Override
        public void mouseDragged(MouseEvent mouseEvent) {
            super.mouseDragged(mouseEvent);
            if (checkCoordinates(mouseEvent.getX(), mouseEvent.getY())) {
                startX = transformX(mouseEvent.getX());
                startY = transformY(mouseEvent.getY());
                drawSelectedImage(startX, startY);
                drawSquare(mouseEvent.getX(), mouseEvent.getY(),
                        (int) (IMAGE_SIZE * widthResizeCoefficient), (int) (IMAGE_SIZE * heightResizeCoefficient));
                repaint();
            }
        }
        private int transformX(int x) {
            int imageX = x - INDENT;
            return (int) (imageX / widthResizeCoefficient);
        }
        private int transformY(int y) {
            int imageY = y - INDENT;
            return (int) (imageY / heightResizeCoefficient);
        }
        private boolean checkCoordinates(int x, int y) {
            return x > INDENT && x < IMAGE_SIZE + INDENT && y > INDENT && y < IMAGE_SIZE + INDENT;
        }
    }
    private class FilterAppliedHandler implements ValueChangedHandler {
        @Override
        public void handle(Object value) {
            if (!(value instanceof FilterApplied)) {
                return;
            }
            FilterApplied filterApplied = (FilterApplied) value;
            filteredImage = filterApplied.getNewImage();
            repaint();
        }
    }
}
