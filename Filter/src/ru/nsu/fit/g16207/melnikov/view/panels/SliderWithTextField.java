package ru.nsu.fit.g16207.melnikov.view.panels;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SliderWithTextField extends JPanel {
    private static final int COLUMNS_COUNT = 3;

    private JSlider slider;
    private JLabel label;
    private JTextField textField;

    private int maxValue;
    private int minValue;

    public SliderWithTextField(JLabel jLabel, int min, int max, int defaultValue) {
        this.label = jLabel;

        this.slider = new JSlider(min, max);
        this.slider.setMinorTickSpacing(min);
        this.slider.setMajorTickSpacing(max);
        this.slider.setPaintTicks(true);
        this.slider.setPaintLabels(true);
        this.slider.setValue(defaultValue);
        this.slider.addChangeListener(new SliderListener());

        this.textField = new JTextField(Integer.toString(defaultValue), COLUMNS_COUNT);
        this.textField.addActionListener(new TextFieldListener());

        this.minValue = min;
        this.maxValue = max;

        add(label);
        add(textField);
        add(slider);
    }

    private class SliderListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent changeEvent) {
            int value = slider.getValue();
            textField.setText(Integer.toString(value));
        }
    }
    public int getIntValue() {
        return Integer.parseInt(textField.getText());
    }
    private class TextFieldListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                int value = Integer.parseInt(textField.getText());
                if ((value < minValue) || (value > maxValue)) {
                    throw new NumberFormatException("Incorrect value");
                }
                slider.setValue(value);
            } catch (NumberFormatException e) {
                textField.setForeground(Color.RED);
            }
        }
    }

}

