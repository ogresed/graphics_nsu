package ru.nsu.fit.g16207.melnikov.mf.property;

import ru.nsu.fit.g16207.melnikov.Configuration;
import ru.nsu.fit.g16207.melnikov.function.Function;
import ru.nsu.fit.g16207.melnikov.view.MainPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static javax.swing.JOptionPane.showMessageDialog;

public class Property extends JFrame {
    private final static String WRONG_VALUES = "Wrong values";
    private final static String CORRECT_VALUES = "Correct values";
    private ChangerValue left;
    private ChangerValue right;
    private ChangerValue lower;
    private ChangerValue heigh;
    private ChangerValue xSize;
    private ChangerValue ySize;
    private JPanel panel;
    private JLabel infoLabel;
    private MainPanel mainPanel;
    private Configuration configuration;
    public Property(MainPanel mainPanel, Configuration configuration) {
        this.mainPanel = mainPanel;
        this.configuration = configuration;
        int height = 650;
        int width = 400;
        panel = new JPanel();
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
        Function function = configuration.getFunction();
        left = makePanelAndCreateChanger("Left border",
                (int)function.getLeftBorder(), -1000, 1000, areaFunctionsPanel);
        right = makePanelAndCreateChanger("Right border",
                (int)function.getRightBorder(), -1000, 1000, areaFunctionsPanel);
        lower = makePanelAndCreateChanger("Lower border",
                (int)function.getLowerBorder(), -1000, 1000, areaFunctionsPanel);
        heigh = makePanelAndCreateChanger("Height border",
                (int)function.getHighBorder(), -1000, 1000, areaFunctionsPanel);
        //create changing grid parameters
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(2, 1));
        panel.add(gridPanel);
        xSize = makePanelAndCreateChanger("Horizontally dots", function.getNumberHorizontallyDotes(),
                Configuration.getMinGridSize(), Configuration.getMaxGridSize(), gridPanel);
        ySize = makePanelAndCreateChanger("Vertically dots", function.getNumberVerticallyDotes(),
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
        JSlider slider = new JSlider(min, max, value);
        JTextField field = new JTextField(String.valueOf(value), 5);
        ChangerValue changerValue = new ChangerValue(min, max, value,
                slider,field);
        changerValue.setSlider(slider);
        changerValue.setField(field);
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
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
            changing.getField().setText(String.valueOf(value));
            changing.setCorrect(true);
            changing.setNewValue(value);
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
        if(heigh.getNewValue() <= lower.getNewValue() ||
                right.getNewValue() <= left.getNewValue() ||
                !allIsCorrect()) {
            showMessageDialog(this, WRONG_VALUES,
                    "WARNING", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int leftValue = left.getNewValue(), rightValue = right.getNewValue(),
                low = lower.getNewValue(), height =heigh.getNewValue();
        if(configuration.getFunction().needUpdate(leftValue, rightValue, low, height)) {
            Function function = new Function(left.getNewValue(), right.getNewValue(),
                    lower.getNewValue(), heigh.getNewValue());
            configuration.setFunction(function);
        }
        configuration.setGrid(xSize.getNewValue(), ySize.getNewValue());
        mainPanel.createAndShowImage();
        setVisible(false);
    }

    private boolean allIsCorrect() {
        return left.isCorrect() && right.isCorrect() &&
                lower.isCorrect() && heigh.isCorrect() &&
                xSize.isCorrect() && ySize.isCorrect();
    }
}
