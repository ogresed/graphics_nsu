package ru.nsu.fit.g16207.melnikov.mf;

import ru.nsu.fit.g16207.melnikov.view.View;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class Property extends JFrame {
    private View view;
    private MainFrame frame;

    JRadioButton xor;
    JRadioButton replace;
    JCheckBox showImpacts;

    private int newHorizontally;
    private int newVertically;
    private int newRadius;
    private int newThickness;
    private int newPeriod;

    Property(MainFrame frame) {
        SetImpacts setImpacts = new SetImpacts(this);
        int height = 700;
        int width = 500;
        this.frame = frame;
        view = frame.view;
        setValues();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(8, 1));
        add(mainPanel);
            //set base options
        setVisible(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        setBounds((dimension.width - width) / 2, (dimension.height - height) / 2, width, height);
            //add choice modes
        xor = new JRadioButton("XOR");
        replace = new JRadioButton("Replace");
        JPanel xorModePanel = new JPanel();
        xorModePanel.add(xor);
        xorModePanel.add(replace);
        Border xorModePanelBorder = BorderFactory.createTitledBorder("xor mode");
        xorModePanel.setBorder(xorModePanelBorder);
        ButtonGroup group = new ButtonGroup();
        group.add(xor);
        group.add(replace);
        if (!view.isXOR()) {
            replace.setSelected(true);
        } else {
            xor.setSelected(true);
        }
        //add show impacts button
        showImpacts = new JCheckBox("show impacts ");
        //adding panels
        JPanel modesPanel = new JPanel();
        mainPanel.add(modesPanel);
        modesPanel.add(xorModePanel);
        modesPanel.add(showImpacts);
            //add choice horizontally

        JPanel horizontallyPanel = new JPanel();
        Border horizontallyPanelBorder = BorderFactory.createTitledBorder("horizontally");
        horizontallyPanel.setBorder(horizontallyPanelBorder);
        mainPanel.add(horizontallyPanel);
        JSlider horizontallySlider = new JSlider(1, 51, newHorizontally);
        JTextField horizontallyField = new JTextField(String.valueOf(newHorizontally), 4);
        horizontallyPanel.add(horizontallySlider);
        horizontallyPanel.add(horizontallyField);
        horizontallySlider.setMajorTickSpacing(10);
        horizontallySlider.setPaintTicks(true);
        horizontallySlider.setPaintLabels(true);

        horizontallyField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int value = getParsedInteger(horizontallyField.getText());
                if(value < 1) value = 1; else if(value > 51) value = 51;
                horizontallySlider.setValue(value);
                newHorizontally = value;
            }
        });
        horizontallySlider.addChangeListener(e-> {
            int value = horizontallySlider.getValue();
            horizontallyField.setText(String.valueOf(value));
            newHorizontally = value;
        });
            //add choice vertically
        JPanel verticallyPanel = new JPanel();
        Border verticallyPanelBorder = BorderFactory.createTitledBorder("vertically");
        verticallyPanel.setBorder(verticallyPanelBorder);
        mainPanel.add(verticallyPanel);
        JSlider verticallySlider = new JSlider(1, 51, newVertically);
        JTextField verticallyField = new JTextField(String.valueOf(newVertically), 4);
        verticallyPanel.add(verticallySlider);
        verticallyPanel.add(verticallyField);
        verticallySlider.setMajorTickSpacing(10);
        verticallySlider.setPaintTicks(true);
        verticallySlider.setPaintLabels(true);

        verticallyField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int value = getParsedInteger(verticallyField.getText());
                if(value < 1) value = 1; else if(value > 51) value = 51;
                verticallySlider.setValue(value);
                newVertically = value;
            }
        });
        verticallySlider.addChangeListener(e-> {
            int value = verticallySlider.getValue();
            verticallyField.setText(String.valueOf(value));
            newVertically = value;
        });
            //add choice radius
        JPanel radiusPanel = new JPanel();
        Border radiusPanelBorder = BorderFactory.createTitledBorder("radius");
        radiusPanel.setBorder(radiusPanelBorder);
        mainPanel.add(radiusPanel);
        JSlider radiusSlider = new JSlider(1, 51, newRadius);
        JTextField radiusField = new JTextField(String.valueOf(newRadius), 4);
        radiusPanel.add(radiusSlider);
        radiusPanel.add(radiusField);
        radiusSlider.setMajorTickSpacing(10);
        radiusSlider.setPaintTicks(true);
        radiusSlider.setPaintLabels(true);

        radiusField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int value = getParsedInteger(radiusField.getText());
                if(value < 1) value = 1; else if(value > 51) value = 51;
                radiusSlider.setValue(value);
                newRadius = value;
            }
        });
        radiusSlider.addChangeListener(e-> {
            int value = radiusSlider.getValue();
            radiusField.setText(String.valueOf(value));
            newRadius = value;
        });
            //add choice thickness
        JPanel thicknessPanel = new JPanel();
        Border thicknessPanelBorder = BorderFactory.createTitledBorder("thickness");
        thicknessPanel.setBorder(thicknessPanelBorder);
        mainPanel.add(thicknessPanel);
        JSlider thicknessSlider = new JSlider(1, 51, newThickness);
        JTextField thicknessField = new JTextField(String.valueOf(newThickness), 4);
        thicknessPanel.add(thicknessSlider);
        thicknessPanel.add(thicknessField);
        thicknessSlider.setMajorTickSpacing(10);
        thicknessSlider.setPaintTicks(true);
        thicknessSlider.setPaintLabels(true);

        thicknessField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int value = getParsedInteger(thicknessField.getText());
                if(value < 1) value = 1; else if(value > 51) value = 51;
                thicknessSlider.setValue(value);
                newThickness = value;
            }
        });
        thicknessSlider.addChangeListener(e-> {
            int value = thicknessSlider.getValue();
            thicknessField.setText(String.valueOf(value));
            newThickness = value;
        });
        //add choice period
        newPeriod = frame.PERIOD_OF_GAME;
        JPanel periodPanel = new JPanel();
        Border periodPanelBorder = BorderFactory.createTitledBorder("period");
        periodPanel.setBorder(periodPanelBorder);
        mainPanel.add(periodPanel);
        JSlider periodSlider = new JSlider(200, 3000, newPeriod);
        JTextField periodField = new JTextField(String.valueOf(newPeriod), 4);
        periodPanel.add(periodSlider);
        periodPanel.add(periodField);
        periodSlider.setMinorTickSpacing(400);
        periodSlider.setPaintTicks(true);

        periodField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int value = getParsedInteger(periodField.getText());
                if(value < 1) value = 200; else if(value > 3000) value = 3000;
                periodSlider.setValue(value);
                newPeriod =  value;
            }
        });
        periodSlider.addChangeListener(e-> {
            int value = periodSlider.getValue();
            periodField.setText(String.valueOf(value));
            newPeriod =  value;
        });
        //periodSlider.setPaintLabels(true);
            //add set impacts button
        JButton setImpButton = new JButton("Set impacts");
        mainPanel.add(setImpButton);
        setImpButton.addActionListener(e-> setImpacts.setVisible(true));
            //add OK and Cancel Buttons
        JPanel okCancelPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        okCancelPanel.add(okButton);
        okCancelPanel.add(cancelButton);
        mainPanel.add(okCancelPanel);
        okButton.addActionListener(e -> onOk());
        cancelButton.addActionListener(e -> setVisible(false));
    }

    public void setValues() {
        newHorizontally = view.getHorizontally();
        newVertically = view.getVertically();
        newRadius = view.getRadius();
        newThickness = view.getThickness();
    }

    private int getParsedInteger(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void onOk() {
        if(frame.XORSelected == replace.isSelected()) frame.XOR();
        if(frame.showImpactsSelected != showImpacts.isSelected()) frame.showImpacts();
        frame.PERIOD_OF_GAME = newPeriod;
        if(!view.setParameters(newHorizontally, newVertically, newRadius, newThickness)) {
            view.updateField();
            view.resetImpAndRedraw();
        }
        setVisible(false);
    }
}
