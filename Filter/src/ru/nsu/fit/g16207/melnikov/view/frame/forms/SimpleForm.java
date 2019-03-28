package ru.nsu.fit.g16207.melnikov.view.frame.forms;

import ru.nsu.fit.g16207.melnikov.controller.FilterController;
import ru.nsu.fit.g16207.melnikov.view.panels.SliderWithTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class SimpleForm extends JFrame {
    protected SliderWithTextField sliderWithTextField;
    protected String title;
    protected String labelString;
    protected int minValue;
    protected int maxValue;
    protected int defaultValue;

    protected JButton okButton;

    protected FilterController filterController;
    protected BufferedImage image;

    public SimpleForm(String title, String labelString, int minValue, int maxValue, int defaultValue) throws HeadlessException {
        this.title = title;
        this.labelString = labelString;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.defaultValue = defaultValue;

        setSize(300, 150);
        setTitle(title);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        sliderWithTextField = new SliderWithTextField(new JLabel(labelString), minValue, maxValue, defaultValue);

        createAndShowGUI();
    }

    private void createAndShowGUI() {
        createAnglePanel();
        createButtonPanel();
    }

    private void createAnglePanel() {
        add(sliderWithTextField, BorderLayout.NORTH);
    }

    protected void createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 2));

        okButton = new JButton("OK");
        okButton.setActionCommand("OK");

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(actionEvent -> {
            setVisible(false);
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
