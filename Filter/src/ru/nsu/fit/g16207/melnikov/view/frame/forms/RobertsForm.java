package ru.nsu.fit.g16207.melnikov.view.frame.forms;

import javax.swing.*;

public class RobertsForm extends SimpleForm {
    public RobertsForm() {
        super("Roberts diff", "Threshold", 0, 255, 25);
    }

    protected void createButtonPanel() {
        super.createButtonPanel();
        okButton.addActionListener(actionEvent -> {
            try {
                filterController.makeRoberts(image, sliderWithTextField.getIntValue());
                setVisible(false);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(RobertsForm.this, "Wrong value (" + e.getMessage() + ")");
            }
        });
    }
}
