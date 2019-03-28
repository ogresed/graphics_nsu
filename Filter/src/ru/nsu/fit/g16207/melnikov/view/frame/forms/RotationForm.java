package ru.nsu.fit.g16207.melnikov.view.frame.forms;

import ru.nsu.fit.g16207.melnikov.controller.FilterController;
import ru.nsu.fit.g16207.melnikov.model.filter.RotationFilter;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class RotationForm extends SimpleForm {
    public RotationForm(FilterController controller, BufferedImage selectedIimage) {
        super("Rotation", "Angle", -180, 180, 0);
        filterController = controller;
        image = selectedIimage;
        setVisible(true);
    }

    protected void createButtonPanel() {
        super.createButtonPanel();
        okButton.addActionListener(actionEvent -> {
            try {
                filterController.makeFilter(new RotationFilter(sliderWithTextField.getIntValue()), image);
                setVisible(false);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(RotationForm.this, "Wrong value (" + e.getMessage() + ")");
            }
        });
    }
}
