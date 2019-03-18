package ru.nsu.fit.g16207.melnikov.mf.property;
import ru.nsu.fit.g16207.melnikov.mf.MainFrame;
import ru.nsu.fit.g16207.melnikov.view.View;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static javax.swing.JOptionPane.showMessageDialog;
public class Property extends JFrame {
    private final static int MAX_SIZE_OF_FIELD = 1500*1000;
    private final static String FIELD_TO_LARGE = "New field will be to large. Change parameters";
    private final static String WRONG_VALUES = "Wrong values";
    private final static String CORRECT_VALUES = "Correct values";
    private final static int minHorizont = 2;
    private final static int maxHorizont = 91;
    private final static int minVertically = 2;
    private final static int maxVertically = 59;
    private final static int minRadius = 7;
    private final static int maxRadius = 36;
    private final static int minThickness = 0;
    private final static int maxThickness = 20;
    private final static int minPeriod = 100;
    private final static int maxPeriod = 2900;
    private  MainFrame frame;
    private JPanel mainPanel;
    private JLabel infoLabel;
    private ChangerValue horizontallyChangerValue;
    private ChangerValue verticallyChangerValue;
    private ChangerValue radiusChangerValue;
    private ChangerValue thicknessChangerValue;
    private ChangerValue periodChangerValue;
    View view;
    public JRadioButton xor;
    public JRadioButton replace;
    public JCheckBox showImpacts;
    public Property(MainFrame frame) {
        SetImpacts setImpacts = new SetImpacts(this);
        int height = 700;
        int width = 500;
        this.frame = frame;
        view = frame.view;
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(9, 1));
        add(mainPanel);
            //set base options
        setVisible(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        setBounds((dimension.width - width) / 2, (dimension.height - height) / 2, width, height);
            //create components
        int newHorizontally = view.getHorizontally();
        JSlider horizontallySlider = new JSlider(minHorizont, maxHorizont, newHorizontally);
        JTextField horizontallyField = new JTextField(String.valueOf(newHorizontally), 4);
        horizontallyChangerValue = new ChangerValue(minHorizont, maxHorizont, newHorizontally,
                horizontallySlider,horizontallyField);
        int newVertically = view.getVertically();
        JSlider verticallySlider = new JSlider(minVertically, maxVertically, newVertically);
        JTextField verticallyField = new JTextField(String.valueOf(newVertically), 4);
        verticallyChangerValue = new ChangerValue(minVertically, maxVertically, newVertically,
                verticallySlider, verticallyField);
        int newRadius = view.getRadius();
        JSlider radiusSlider = new JSlider(minRadius, maxRadius, newRadius);
        JTextField radiusField = new JTextField(String.valueOf(newRadius), 4);
        radiusChangerValue = new ChangerValue(minRadius, maxRadius, newRadius,
                radiusSlider, radiusField);
        int newThickness = view.getThickness();
        JSlider thicknessSlider = new JSlider(minThickness, maxThickness, newThickness);
        JTextField thicknessField = new JTextField(String.valueOf(newThickness), 4);
        thicknessChangerValue = new ChangerValue(minThickness, maxThickness, newThickness,
                thicknessSlider, thicknessField);
        int newPeriod = frame.PERIOD_OF_GAME;
        JSlider periodSlider = new JSlider(minPeriod, maxPeriod, newPeriod);
        JTextField periodField = new JTextField(String.valueOf(newPeriod), 4);
        periodChangerValue = new ChangerValue(minPeriod, maxPeriod, newPeriod,
                periodSlider, periodField);
        setValues();
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
        makePanel("horizontally", horizontallyField, horizontallySlider, 10);
        horizontallyField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                checkValueInField(horizontallyChangerValue);
            }
        });
        horizontallySlider.addChangeListener(e-> horizontallyChangerValue.changeSliderValue());
            //add choice vertically
        makePanel("vertically", verticallyField, verticallySlider, 10);
        verticallyField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                checkValueInField(verticallyChangerValue);
            }
        });
        verticallySlider.addChangeListener(e-> verticallyChangerValue.changeSliderValue());
            //add choice radius
        makePanel("radius", radiusField, radiusSlider, 6);
        radiusField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                checkValueInField(radiusChangerValue);
            }
        });
        radiusSlider.addChangeListener(e-> radiusChangerValue.changeSliderValue());
            //add choice thickness
        makePanel("thickness", thicknessField, thicknessSlider, 4);
        thicknessField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                checkValueInField(thicknessChangerValue);
            }
        });
        thicknessSlider.addChangeListener(e-> thicknessChangerValue.changeSliderValue());
            //add choice period
        makePanel("period", periodField, periodSlider, 200);
        periodField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                checkValueInField(periodChangerValue);
            }
        });
        periodSlider.addChangeListener(e-> periodChangerValue.changeSliderValue());
            //add info label
        JPanel infoPanel = new JPanel();
        mainPanel.add(infoPanel);
        infoLabel = new JLabel("");
        infoPanel.add(infoLabel);
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
        okButton.addActionListener(e -> onOk());
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
    }
    private void checkValueInField(ChangerValue changing) {
        int value = getParsedInteger(changing.getField().getText());
        if(value < changing.min || value > changing.max) {
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
    private int getParsedInteger(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    private void onOk() {
        if(!(horizontallyChangerValue.isCorrect() && verticallyChangerValue.isCorrect() &&
                radiusChangerValue.isCorrect() && thicknessChangerValue.isCorrect() &&
                periodChangerValue.isCorrect())) {
            showMessageDialog(this, WRONG_VALUES,
                    "WARNING", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int sizeOfNewField = view.getNewFieldHeight(verticallyChangerValue.getNewValue(), radiusChangerValue.getNewValue(), thicknessChangerValue.getNewValue()) *
                view.getNewFieldWidth(horizontallyChangerValue.getNewValue(), radiusChangerValue.getNewValue(), thicknessChangerValue.getNewValue());
        if(sizeOfNewField > MAX_SIZE_OF_FIELD) {
            showMessageDialog(this, FIELD_TO_LARGE,
                    "WARNING", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(frame.XORSelected == replace.isSelected()) frame.XOR();
        if(frame.showImpactsSelected != showImpacts.isSelected()) frame.showImpacts();
        frame.PERIOD_OF_GAME = periodChangerValue.getNewValue();
        if(!view.setParameters(horizontallyChangerValue.getNewValue(),
                verticallyChangerValue.getNewValue(), radiusChangerValue.getNewValue()
                , thicknessChangerValue.getNewValue())) {
            view.updateField();
            view.resetImpAndRedraw();
            view.setPreferredSize(new Dimension(view.getFieldWidth(), view.getFieldHeight()));
            frame.revalidate();
        }
        setVisible(false);
    }
    public void setValues() {
        horizontallyChangerValue.setNewValue(view.getHorizontally());
        verticallyChangerValue.setNewValue(view.getVertically());
        radiusChangerValue.setNewValue(view.getRadius());
        thicknessChangerValue.setNewValue(view.getThickness());
        periodChangerValue.setNewValue(frame.PERIOD_OF_GAME);
    }
    public void setValuesIntoComponents() {
        horizontallyChangerValue.setValuesIntoComponents();
        verticallyChangerValue.setValuesIntoComponents();
        radiusChangerValue.setValuesIntoComponents();
        thicknessChangerValue.setValuesIntoComponents();
        periodChangerValue.setValuesIntoComponents();
    }
}
