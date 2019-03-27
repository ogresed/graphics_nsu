package ru.nsu.fit.g16207.melnikov.view.frame.forms;

import javax.swing.*;

public class SobelForm extends SimpleForm {
    public SobelForm() {
        super("Sobel diff", "Threshold", 0, 255, 25);
    }

    protected void createButtonPanel() {
        super.createButtonPanel();
        okButton.addActionListener(actionEvent -> {
            try {
                filterController.makeSobel(image, sliderWithTextField.getIntValue());
                setVisible(false);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(SobelForm.this, "Wrong value (" + e.getMessage() + ")");
            }
        });
    }
}
