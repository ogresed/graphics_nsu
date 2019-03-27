package ru.nsu.fit.g16207.melnikov.view.frame.forms;

import ru.nsu.fit.g16207.melnikov.controller.FilterController;
import ru.nsu.fit.g16207.melnikov.view.panels.TextFieldWithName;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GammaForm extends JFrame {
    private TextFieldWithName gammaField;

    private FilterController filterController;
    private BufferedImage image;

    public GammaForm() {
        gammaField = new TextFieldWithName(new JLabel("Gamma"), Double.toString(0.5));

        setSize(300, 150);
        setTitle("Gamma");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        createAndShowGUI();
    }

    private void createAndShowGUI() {
        add(gammaField, BorderLayout.NORTH);
        createButtonPanel();
    }

    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 2));

        JButton okButton = new JButton("OK");
        okButton.setActionCommand("OK");
        okButton.addActionListener(actionEvent -> {
            try {
                filterController.makeGammaCorrection(image, Double.parseDouble(gammaField.getText()));
                setVisible(false);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(GammaForm.this, "Wrong value (" + e.getMessage() + ")");
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(actionEvent -> {
            setVisible(false);
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setFilterController(FilterController filterController) {
        this.filterController = filterController;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
