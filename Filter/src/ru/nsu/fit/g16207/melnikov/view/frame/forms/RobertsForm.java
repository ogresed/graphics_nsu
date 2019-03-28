package ru.nsu.fit.g16207.melnikov.view.frame.forms;

import ru.nsu.fit.g16207.melnikov.controller.FilterController;
import ru.nsu.fit.g16207.melnikov.model.filter.RobertsFilter;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class RobertsForm extends SimpleForm {
    public RobertsForm(FilterController controller, BufferedImage selectedImage) {
        super("Roberts diff", "Threshold", 0, 255, 25);
        filterController = controller;
        image = selectedImage;
        setVisible(true);
    }

    protected void createButtonPanel() {
        super.createButtonPanel();
        okButton.addActionListener(actionEvent -> {
            try {
                filterController.makeFilter(new RobertsFilter(sliderWithTextField.getIntValue()), image);
                setVisible(false);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(RobertsForm.this, "Wrong value (" + e.getMessage() + ")");
            }
        });
    }
}
