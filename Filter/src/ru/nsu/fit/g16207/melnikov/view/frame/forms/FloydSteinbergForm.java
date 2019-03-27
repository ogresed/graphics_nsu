package ru.nsu.fit.g16207.melnikov.view.frame.forms;

import ru.nsu.fit.g16207.melnikov.controller.FilterController;
import ru.nsu.fit.g16207.melnikov.view.panels.TextFieldWithName;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class FloydSteinbergForm extends JFrame {
    private TextFieldWithName nRed;
    private TextFieldWithName nGreen;
    private TextFieldWithName nBlue;

    private FilterController filterController;
    private BufferedImage image;

    public FloydSteinbergForm() {
        setSize(300, 150);
        setTitle("Floyd-Steinberg Dithering");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        nRed = new TextFieldWithName(new JLabel("Count of red colors"), Integer.toString(2));
        nGreen = new TextFieldWithName(new JLabel("Count of green colors"), Integer.toString(2));
        nBlue = new TextFieldWithName(new JLabel("Count of blue colors"), Integer.toString(2));

        createAndShowGUI();
    }

    private void createAndShowGUI() {
        createCountsPanel();
        createButtonPanel();
    }

    private void createCountsPanel() {
        JPanel countsPanel = new JPanel();
        countsPanel.setLayout(new GridLayout(0, 1));
        countsPanel.add(nRed);
        countsPanel.add(nGreen);
        countsPanel.add(nBlue);
        add(countsPanel, BorderLayout.NORTH);
    }

    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 2));

        JButton okButton = new JButton("OK");
        okButton.setActionCommand("OK");
        okButton.addActionListener(actionEvent -> {
            try {
                filterController.makeFloydSteinbergDithering(image, Integer.parseInt(nRed.getText()),
                        Integer.parseInt(nGreen.getText()), Integer.parseInt(nBlue.getText()));
                setVisible(false);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(FloydSteinbergForm.this, "Wrong value (" + e.getMessage() + ")");
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
