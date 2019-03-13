package ru.nsu.fit.g16207.melnikov.mf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class SetImpacts extends JFrame {
    private Property property;

    private double NEW_LIVE_BEGIN = 2.0, NEW_LIVE_END = 3.3, NEW_BIRTH_BEGIN = 2.3, NEW_BIRTH_END = 2.9,
            NEW_FST_IMPACT = 1.0, NEW_SND_IMPACT = 0.3;

    SetImpacts(Property property) {
        this.property = property;
        int height = 550;
        int width = 400;
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(8, 1));
        add(mainPanel);
        //set base options
        setVisible(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        setBounds((dimension.width - width) / 2, (dimension.height - height) / 2, width, height);

        JLabel info = new JLabel(""); mainPanel.add(info);
        JPanel lifeBeginPanel = new JPanel(); mainPanel.add(lifeBeginPanel);
        JPanel lifeEndPanel = new JPanel(); mainPanel.add(lifeEndPanel);
        JPanel birthBeginPanel = new JPanel(); mainPanel.add(birthBeginPanel);
        JPanel birthEbdPanel = new JPanel(); mainPanel.add(birthEbdPanel);
        JPanel firstImpPanel = new JPanel(); mainPanel.add(firstImpPanel);
        JPanel secondImpPanel = new JPanel(); mainPanel.add(secondImpPanel);
        JPanel okCancelPanel = new JPanel(); mainPanel.add(okCancelPanel);

        JTextField lifeBeginField = createField("Life being", lifeBeginPanel, NEW_LIVE_BEGIN);
        lifeBeginField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                double value = getParsedInteger(lifeBeginField.getText());
                double tmp = NEW_LIVE_BEGIN;
                NEW_LIVE_BEGIN = value;
                if(isCorrectValues()) {
                    info.setText("Correct");
                }
                else {
                    NEW_LIVE_BEGIN = tmp;
                    info.setText("Wrong value");
                }
            }
        });

        JTextField lifeEndField = createField("Life End", lifeEndPanel, NEW_LIVE_END);
        lifeEndField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                double value = getParsedInteger(lifeEndField.getText());
                double tmp = NEW_LIVE_END;
                NEW_LIVE_END = value;
                if (isCorrectValues()) {
                    info.setText("Correct");
                } else {
                    info.setText("Wrong value");
                    NEW_LIVE_END = tmp;
                }
            }
        });

        JTextField birthBeginField = createField("Birth begin", birthBeginPanel, NEW_BIRTH_BEGIN);
        birthBeginField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                double value = getParsedInteger(birthBeginField.getText());
                double tmp = NEW_BIRTH_BEGIN;
                NEW_BIRTH_BEGIN = value;
                if(isCorrectValues()) {
                    info.setText("Correct");
                }
                else {
                    info.setText("Wrong value");
                    NEW_BIRTH_BEGIN = tmp;
                }
            }
        });

        JTextField birthEndField = createField("Birth End", birthEbdPanel, NEW_BIRTH_END);
        birthEndField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                double value = getParsedInteger(birthEndField.getText());
                double tmp = NEW_BIRTH_END;
                NEW_BIRTH_END = value;
                if(isCorrectValues()) {
                    info.setText("Correct");
                }
                else {
                    info.setText("Wrong value");
                    NEW_BIRTH_END = tmp;
                }
            }
        });

        JTextField firstImpField = createField("First impact", firstImpPanel, NEW_FST_IMPACT);
        firstImpField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                NEW_FST_IMPACT = getParsedInteger(firstImpField.getText());
            }
        });


        JTextField secImpField = createField("Second impact", secondImpPanel, NEW_SND_IMPACT);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                NEW_SND_IMPACT = getParsedInteger(secImpField.getText());
            }
        });

        JButton okButton = new JButton("Ok");
        JButton cancelButton = new JButton("Cancel");
        okCancelPanel.add(okButton);
        okCancelPanel.add(cancelButton);

        okButton.addActionListener(e->onOk());
        cancelButton.addActionListener(e->setVisible(false));
    }

    private JTextField createField (String name, JPanel panel, double beginVal) {
        JLabel label = new JLabel(name);
        JTextField field = new JTextField(String.valueOf(beginVal), 6);
        panel.add(label);
        panel.add(field);
        return  field;
    }

    private boolean isCorrectValues() {
        return ((NEW_LIVE_BEGIN <= NEW_BIRTH_BEGIN) && (NEW_BIRTH_BEGIN <=NEW_LIVE_END) && (NEW_BIRTH_BEGIN <= NEW_BIRTH_BEGIN));
    }

    private double getParsedInteger(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void onOk() {
        property.view.setImpacts(NEW_LIVE_BEGIN, NEW_LIVE_END, NEW_BIRTH_BEGIN, NEW_BIRTH_END, NEW_FST_IMPACT, NEW_SND_IMPACT);
        property.view.resetImpAndRedraw();
        setVisible(false);
    }
}
