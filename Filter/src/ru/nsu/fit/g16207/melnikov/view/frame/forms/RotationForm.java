package ru.nsu.fit.g16207.melnikov.view.frame.forms;

import javax.swing.*;

public class RotationForm extends SimpleForm {
    public RotationForm() {
        super("Rotation", "Angle", -180, 180, 45);
    }

    protected void createButtonPanel() {
        super.createButtonPanel();
        okButton.addActionListener(actionEvent -> {
            try {
                filterController.makeRotation(image, sliderWithTextField.getIntValue());
                setVisible(false);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(RotationForm.this, "Wrong value (" + e.getMessage() + ")");
            }
        });
    }
}
