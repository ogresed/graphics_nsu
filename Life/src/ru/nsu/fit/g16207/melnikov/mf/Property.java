package ru.nsu.fit.g16207.melnikov.mf;

import ru.nsu.fit.g16207.melnikov.view.View;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class Property extends JFrame {
    View view;
    private MainFrame frame;

    JRadioButton xor;
    JRadioButton replace;
    JCheckBox showImpacts;

    private int newHorizontally;
    private int newVertically;
    private int newRadius;
    private int newThickness;
    private int newPeriod;

    private JPanel mainPanel;

    private JSlider horizontallySlider;
    private JTextField horizontallyField;
    private JSlider verticallySlider;
    private JTextField verticallyField;
    private JSlider radiusSlider;
    private JTextField radiusField;
    private JSlider thicknessSlider;
    private JTextField thicknessField;

    Property(MainFrame frame) {
        SetImpacts setImpacts = new SetImpacts(this);
        int height = 700;
        int width = 500;
        this.frame = frame;
        view = frame.view;
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(8, 1));
        add(mainPanel);
            //set base options
        setValues();
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
        horizontallySlider = new JSlider(1, 51, newHorizontally);
        horizontallyField = new JTextField(String.valueOf(newHorizontally), 4);
        makePanel("horizontally", horizontallyField, horizontallySlider, 10);
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
        verticallySlider = new JSlider(1, 51, newVertically);
        verticallyField = new JTextField(String.valueOf(newVertically), 4);
        makePanel("vertically", verticallyField, verticallySlider, 10);
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
        radiusSlider = new JSlider(1, 51, newRadius);
        radiusField = new JTextField(String.valueOf(newRadius), 4);
        makePanel("radius", radiusField, radiusSlider, 10);
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
        thicknessSlider = new JSlider(1, 51, newThickness);
        thicknessField = new JTextField(String.valueOf(newThickness), 4);
        makePanel("thickness", thicknessField, thicknessSlider, 10);
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
        JSlider periodSlider = new JSlider(200, 3000, newPeriod);
        JTextField periodField = new JTextField(String.valueOf(newPeriod), 4);
        makePanel("period", periodField, periodSlider, 400);
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
        JPanel setImpButtonPanel = new JPanel();
        setImpButtonPanel.add(setImpButton);
        mainPanel.add(setImpButtonPanel);
        setImpButton.addActionListener(e-> setImpacts.setVisible(true));
            //add OK and Cancel Buttons
        JPanel okCancelPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        okCancelPanel.add(okButton);
        okCancelPanel.add(cancelButton);
        mainPanel.add(okCancelPanel);
        okButton.addActionListener(e -> onOkey());
        cancelButton.addActionListener(e -> setVisible(false));
    }

    private void makePanel(String name, JTextField field, JSlider slider, int minorTick) {
        JPanel jPanel = new JPanel();
        Border horizontallyPanelBorder = BorderFactory.createTitledBorder(name);
        jPanel.setBorder(horizontallyPanelBorder);
        mainPanel.add(jPanel);
        jPanel.add(slider);
        jPanel.add(field);
        slider.setMajorTickSpacing(minorTick);
        slider.setPaintTicks(true);
        //slider.setPaintLabels(true);
    }

    void setValues() {
        newHorizontally = view.getHorizontally();
        newVertically = view.getVertically();
        newRadius = view.getRadius();
        newThickness = view.getThickness();
        newPeriod = frame.PERIOD_OF_GAME;
    }

    void setValuesIntoComponents () {
        horizontallySlider.setValue(newHorizontally);
        horizontallyField.setText(String.format("%d", newHorizontally));
        verticallySlider.setValue(newVertically);
        verticallyField.setText(String.format("%d", newVertically));
        radiusSlider.setValue(newRadius);
        radiusField.setText(String.format("%d", newRadius));
        thicknessSlider.setValue(newThickness);
        thicknessField.setText(String.format("%d", newThickness));
    }

    private int getParsedInteger(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void onOkey() {
        if(frame.XORSelected == replace.isSelected()) frame.XOR();
        if(frame.showImpactsSelected != showImpacts.isSelected()) frame.showImpacts();
        frame.PERIOD_OF_GAME = newPeriod;
        if(!view.setParameters(newHorizontally, newVertically, newRadius, newThickness)) {
            view.updateField();
            view.resetImpAndRedraw();
            view.setPreferredSize(new Dimension(view.getFieldWidth(), view.getFieldHeight()));
            frame.revalidate();
        }
        setVisible(false);
    }
}
