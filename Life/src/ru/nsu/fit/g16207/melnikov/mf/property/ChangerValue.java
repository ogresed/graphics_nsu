package ru.nsu.fit.g16207.melnikov.mf.property;
import javax.swing.*;
class ChangerValue {
    int min;
    int max;
    private int newValue;
    private boolean correct = true;
    private JSlider slider;
    private JTextField field;
    ChangerValue(int min, int max, int newValue, JSlider slider, JTextField field) {
        this.min = min;
        this.max = max;
        this.newValue = newValue;
        this.slider = slider;
        this.field = field;
    }
    void setNewValue(int newValue) {
        this.newValue = newValue;
    }
    void setCorrect(boolean correct) {
        this.correct = correct;
    }
    boolean isCorrect() {
        return correct;
    }
    int getNewValue() {
        return newValue;
    }
    JSlider getSlider() {
        return slider;
    }
    JTextField getField() {
        return field;
    }
    void changeSliderValue() {
        int value = slider.getValue();
        field.setText(String.valueOf(value));
        newValue = value;
    }
    void setValuesIntoComponents() {
        slider.setValue(newValue);
        field.setText(String.format("%d", newValue));
        correct = true;
    }
}
