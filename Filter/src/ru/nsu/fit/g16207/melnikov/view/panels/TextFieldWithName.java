package ru.nsu.fit.g16207.melnikov.view.panels;
import javax.swing.*;
public class TextFieldWithName extends JPanel {
    private static final int COLUMNS_COUNT = 3;
    private JLabel name;
    private JTextField textField;
    public TextFieldWithName(JLabel jLabel, String defaultValue) {
        this.name = jLabel;
        this.textField = new JTextField(defaultValue, COLUMNS_COUNT);
        add(this.name);
        add(this.textField);
    }
    public String getText() {
        return textField.getText();
    }
}
