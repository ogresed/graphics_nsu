package ru.nsu.fit.g16207.melnikov.view.frame;
import ru.nsu.fit.g16207.melnikov.controller.FilterController;
import ru.nsu.fit.g16207.melnikov.view.file_utils.FileUtils;
import ru.nsu.fit.g16207.melnikov.view.panels.MainPanel;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class MainFrame extends JFrame {
    private static final int START_WIDTH = 1170;
    private static final int START_HEIGHT = 550;
    private MainPanel imagePanel;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    public MainFrame() {
        super();
        setSize(START_WIDTH, START_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Filter");
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        toolBar = new JToolBar("Toolbar");
        add(toolBar, BorderLayout.PAGE_START);
        toolBar.setRollover(true);
        createMenu();
        createToolbar();
        FilterController filterController = new FilterController();
        imagePanel = new MainPanel(getBounds().width, getBounds().height);
        imagePanel.setFilterController(filterController);
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        scrollPane.updateUI();
        add(scrollPane);
        setVisible(true);
        revalidate();
    }
    private ActionListener newDocumentListener = e-> imagePanel.newDocument();
    private ActionListener openListener = e-> onOpen();
    private ActionListener saveListener = e-> onSave();
    private ActionListener makeGrayListener = e-> imagePanel.makeGray();
    private ActionListener makeNegativeListener = e-> imagePanel.makeNegative();
    private ActionListener FSDitheringListener = e-> imagePanel.makeFloydSteinbergDithering();
    private ActionListener ODitheringListener = e-> imagePanel.makeOrderedDithering();
    private ActionListener makeSharpeningListener = e->imagePanel.makeSharpening();
    private ActionListener makeEmbossingListener = e->imagePanel.makeEmbossing();
    private ActionListener makeWaterColouringListener = e->imagePanel.makeWaterColoring();
    private ActionListener makeMedianListener = e->imagePanel.makeMedian();
    private ActionListener makeBlurListener = e->imagePanel.makeBlur();
    private ActionListener gammaCorrectionListener = e->imagePanel.makeGamma();
    private ActionListener makeRotationListener = e->imagePanel.makeRotation();
    private ActionListener makeDoublingListener = e->imagePanel.makeDoubling();
    private ActionListener makeRobertsListener = e->imagePanel.makeRoberts();
    private ActionListener makeSobelListener = e->imagePanel.makeSobel();
    private ActionListener selectRightListener = e->imagePanel.makeR2Bmp();
    private void createMenu() {
        JMenu file =  makeMenu("File", 'F');
        makeMenuItem(file, "New Document", newDocumentListener, 'N', "New Document");
        makeMenuItem(file, "Open", openListener, 'O', "Open");
        makeMenuItem(file, "Save", saveListener, 'S', "Save");
        JMenu image = makeMenu("Image", 'I');
        makeMenuItem(image, "Gray Scale", makeGrayListener, '0', "Make image monochrome");
        makeMenuItem(image, "Negative", makeNegativeListener, '1', "Make image negative");
        makeMenuItem(image, "Floyd-Steinberg Dithering", FSDitheringListener, '2', "Floyd-Steinberg Dithering");
        makeMenuItem(image, "Ordered Dithering", ODitheringListener, '3', "Ordered Dithering");
        makeMenuItem(image, "Sharpening", makeSharpeningListener, '4', "Image Sharpening");
        makeMenuItem(image, "Embossing", makeEmbossingListener, '5', "Image embossing");
        makeMenuItem(image, "Watercoloring", makeWaterColouringListener, '6', "Image watercoloring");
        makeMenuItem(image, "Median", makeMedianListener, '7', "Apply median filter on image");
        makeMenuItem(image, "Blur", makeBlurListener, '8', "Blur image");
        makeMenuItem(image, "Gamma Correction", gammaCorrectionListener, '9', "Apply Gamma Correction");
        makeMenuItem(image, "Rotation", makeRotationListener, 'R', "Rotate image");
        makeMenuItem(image, "Doubling", makeDoublingListener, 'D', "Double central part of image");
        makeMenuItem(image, "Roberts", makeRobertsListener, 'P', "Roberts diff");
        makeMenuItem(image, "Sobel", makeSobelListener, 'W', "Sobel diff");
        makeMenuItem(image, "Select right", selectRightListener, 'Z', "Select filtered image to make operations with it");
    }
    private void createToolbar() {
        makeButton("pictures/newDoc.jpg",newDocumentListener,'N', "New Document");
        makeButton("pictures/open.jpg",openListener,'O', "Open file");
        makeButton("pictures/save.jpg",saveListener, 'S', "Save file");
        toolBar.addSeparator();
        makeButton("pictures/cmyk.png", makeGrayListener,'0', "Make image monochrome");
        makeButton("pictures/contrast.png", makeNegativeListener, '1', "Make image negative");
        toolBar.addSeparator();
        makeButton("pictures/rgb.png",FSDitheringListener, '2', "Floyd-Steinberg Dithering");
        makeButton("pictures/traffic.png",ODitheringListener, '3', "Ordered Dithering");
        toolBar.addSeparator();
        makeButton("pictures/barchart.png",makeSharpeningListener, '4', "Image Sharpening");
        makeButton("pictures/tripod.png",  makeMedianListener, '7', "Apply median filter on image");
        toolBar.addSeparator();
        makeButton("pictures/embossing.png",makeEmbossingListener, '5', "Image embossing");
        makeButton("pictures/art.png", makeWaterColouringListener, '6', "Image watercoloring");
        makeButton("pictures/water.png", makeBlurListener, '8', "Blur image");
        makeButton("pictures/crop.png", gammaCorrectionListener, '9', "Apply Gamma Correction");
        toolBar.addSeparator();
        makeButton("pictures/bikewheel.png", makeRotationListener, 'R', "Rotate image");
        makeButton("pictures/zoomin.png", makeDoublingListener, 'D', "Double central part of image");
        makeButton("pictures/trends.png", makeRobertsListener, 'P', "Roberts diff");
        makeButton("pictures/merge.png", makeSobelListener, 'W', "Sobel diff");
        toolBar.addSeparator();

        makeButton("pictures/upload.png", selectRightListener, 'Z', "Select filtered image to make operations with it");
    }
    private JMenu makeMenu(String file, char f) {
        JMenu menu = new JMenu(file);
        menu.setMnemonic(f);
        menuBar.add(menu);
        return  menu;
    }
    private void makeMenuItem(JMenu menu, String name, ActionListener l, char f, String description) {
        JMenuItem item = menu.add(new JMenuItem(name));
        item.addActionListener(l);
        item.setMnemonic(f);
        item.setToolTipText(description);
    }
    private void makeButton(String icon, ActionListener l, char f, String description) {
        Icon ICon = new ImageIcon(icon);
        JButton button = new JButton(ICon);
        toolBar.add(button);
        button.addActionListener(l);
        button.setMnemonic(f);
        button.setToolTipText(description);
    }
    private void onOpen() {
        File imageFile = FileUtils.getOpenFileName(this, "png", "Images");
        if (imageFile == null) {
            return;
        }

        try {
            BufferedImage image = ImageIO.read(imageFile);
            imagePanel.drawOriginalImage(image);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(this, "Bad image");
        }
    }
    private void onSave() {
        File imageFile = FileUtils.getSaveFileName(this, "bmp", "Images");
        if (imageFile == null) {
            return;
        }

        try {
            imagePanel.save(imageFile);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(this, "Error in saving image");
        }
    }
}
