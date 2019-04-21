package ru.nsu.fit.g16207.melnikov.mf.properties;

import ru.nsu.fit.g16207.melnikov.configuration.Configuration;
import ru.nsu.fit.g16207.melnikov.function.GridFunction;
import ru.nsu.fit.g16207.melnikov.view.MainPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static javax.swing.JOptionPane.showMessageDialog;

public class Properties extends JFrame {
    private final static char ARROW = (char) 65535;
    private final static String WRONG_VALUES = "Wrong values";
    private final static String CORRECT_VALUES = "Correct values";
    private final static int MAX_AMPLITUDE = 400;
    private ChangerValue left;
    private ChangerValue right;
    private ChangerValue lower;
    private ChangerValue high;
    private ChangerValue xSize;
    private ChangerValue ySize;
    private JLabel infoLabel;
    private MainPanel mainPanel;
    private Configuration configuration;
    public Properties(MainPanel mainPanel, Configuration configuration) {
        this.mainPanel = mainPanel;
        this.configuration = configuration;
        int height = 650;
        int width = 400;
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        add(panel);
        //set base options
        setVisible(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        setBounds((dimension.width - width) / 2, (dimension.height - height) / 2, width, height);
        //create changing size of area of function
        JPanel areaFunctionsPanel = new JPanel();
        areaFunctionsPanel.setLayout(new GridLayout(2, 2));
        panel.add(areaFunctionsPanel);
        GridFunction function = configuration.getGridFunction();
        left = makePanelAndCreateChanger("Left border",
                (int)function.getLeftBorder(), -MAX_AMPLITUDE, MAX_AMPLITUDE, areaFunctionsPanel);
        right = makePanelAndCreateChanger("Right border",
                (int)function.getRightBorder(), -MAX_AMPLITUDE, MAX_AMPLITUDE, areaFunctionsPanel);
        lower = makePanelAndCreateChanger("Lower border",
                (int)function.getLowerBorder(), -MAX_AMPLITUDE, MAX_AMPLITUDE, areaFunctionsPanel);
        high = makePanelAndCreateChanger("Height border",
                (int)function.getHighBorder(), -MAX_AMPLITUDE, MAX_AMPLITUDE, areaFunctionsPanel);
        //create changing grid parameters
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(2, 1));
        panel.add(gridPanel);
        xSize = makePanelAndCreateChanger("Horizontal dots", configuration.getXSize(),
                Configuration.getMinGridSize(), Configuration.getMaxGridSize(), gridPanel);
        ySize = makePanelAndCreateChanger("Vertical dots", configuration.getYSize(),
                Configuration.getMinGridSize(), Configuration.getMaxGridSize(), gridPanel);
        //add info label
        JPanel infoPanel = new JPanel();
        panel.add(infoPanel);
        infoLabel = new JLabel("");
        infoPanel.add(infoLabel);
        //add OK and Cancel Buttons
        JPanel okCancelPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        okCancelPanel.add(okButton);
        okCancelPanel.add(cancelButton);
        panel.add(okCancelPanel);
        okButton.addActionListener(e -> onOk());
        cancelButton.addActionListener(e -> setVisible(false));
    }
    private ChangerValue makePanelAndCreateChanger (
            String name,
            int value,
            int min,
            int max,
            JPanel parentPanel
        ) {
        if(value < min || value > max) {
            value = (min + max)/2;
        }
        JSlider slider = new JSlider(min, max, value);
        JTextField field = new JTextField(String.valueOf(value), 5);
        ChangerValue changerValue = new ChangerValue(min, max, value,
                slider,field);
        changerValue.setSlider(slider);
        changerValue.setField(field);
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == ARROW) {
                    return;
                }
                checkValueInField(changerValue);
            }
        });
        slider.addChangeListener(e-> changerValue.changeSliderValue());
        JPanel jPanel = new JPanel();
        Border horizontallyPanelBorder = BorderFactory.createTitledBorder(name);
        jPanel.setBorder(horizontallyPanelBorder);
        parentPanel.add(jPanel);
        jPanel.add(slider);
        jPanel.add(field);
        slider.setPaintTicks(true);
        return changerValue;
    }
    private void checkValueInField(ChangerValue changing) {
        int value = getParsedInteger(changing.getField().getText(), changing.min, changing.max);
        if(value < changing.min) {
            changing.setCorrect(false);
            infoLabel.setText(WRONG_VALUES);
        }
        else {
            changing.getSlider().setValue(value);
            changing.setNewValue(value);
            changing.setCorrect(true);
            infoLabel.setText(CORRECT_VALUES);
        }
    }
    private int getParsedInteger(String text, int min, int max) {
        int ret = min - 1;
        try {
            int tmp = Integer.parseInt(text);
            if(tmp > max || tmp < min) {
                return ret;
            }
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return ret;
        }
    }
    private void onOk() {
        if(high.getNewValue() <= lower.getNewValue() ||
                right.getNewValue() <= left.getNewValue() ||
                !allIsCorrect()) {
            showMessageDialog(this, WRONG_VALUES,
                    "WARNING", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int leftValue = left.getNewValue(),
                rightValue = right.getNewValue(),
                low = lower.getNewValue(),
                height = high.getNewValue();
        if(configuration.getGridFunction().needUpdate(leftValue, rightValue, low, height)) {
            GridFunction function = new GridFunction(
                    left.getNewValue(),
                    right.getNewValue(),
                    lower.getNewValue(),
                    high.getNewValue());
            configuration.setFunction(function);
            mainPanel.createAllIsolines(function);
        }
        else {
            configuration.setGrid(xSize.getNewValue(), ySize.getNewValue());
            mainPanel.createAllIsolines(configuration.getGridFunction());
            mainPanel.createImage();
        }
        setVisible(false);
    }

    private boolean allIsCorrect() {
        return left.isCorrect() && right.isCorrect() &&
                lower.isCorrect() && high.isCorrect() &&
                xSize.isCorrect() && ySize.isCorrect();
    }
    public void setGrid(int xSizeVal, int ySizeVal) {
        xSize.setValue(xSizeVal);
        ySize.setValue(ySizeVal);
    }
}
