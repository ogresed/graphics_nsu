package ru.nsu.fit.g16207.melnikov.view.frame.forms;

import ru.nsu.fit.g16207.melnikov.controller.FilterController;
import ru.nsu.fit.g16207.melnikov.model.filter.SobelFilter;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class SobelForm extends SimpleForm {
    public SobelForm(FilterController controller, BufferedImage selectedImage) {
        super("Sobel diff", "Threshold", 0, 255, 25);
        filterController = controller;
        image = selectedImage;
        setVisible(true);
    }

    protected void createButtonPanel() {
        super.createButtonPanel();
        okButton.addActionListener(actionEvent -> {
            try {
                filterController.makeFilter(new SobelFilter(sliderWithTextField.getIntValue()), image);
                setVisible(false);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(SobelForm.this, "Wrong value (" + e.getMessage() + ")");
            }
        });
    }
}
