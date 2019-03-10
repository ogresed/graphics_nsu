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
        JPanel lbPanel = new JPanel(); mainPanel.add(lbPanel);
        JPanel lePanel = new JPanel(); mainPanel.add(lePanel);
        JPanel bbPanel = new JPanel(); mainPanel.add(bbPanel);
        JPanel bePanel = new JPanel(); mainPanel.add(bePanel);
        JPanel fiPanel = new JPanel(); mainPanel.add(fiPanel);
        JPanel siPanel = new JPanel(); mainPanel.add(siPanel);
        JPanel okCancelPanel = new JPanel(); mainPanel.add(okCancelPanel);

        JLabel lbLabel = new JLabel("Life begin");
        JTextField lbfield = new JTextField(String.valueOf(NEW_LIVE_BEGIN), 6);
        lbPanel.add(lbLabel);
        lbPanel.add(lbfield);
        lbfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                double value = getParsedInteger(lbfield.getText());
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

        JLabel leLabel = new JLabel("Life end");
        JTextField lefield = new JTextField(String.valueOf(NEW_LIVE_END), 6);
        lePanel.add(leLabel);
        lePanel.add(lefield);
        lefield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                double value = getParsedInteger(lefield.getText());
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


        JLabel bbLabel = new JLabel("Birth begin");
        JTextField bbfield = new JTextField(String.valueOf(NEW_BIRTH_BEGIN), 6);
        bbPanel.add(bbLabel);
        bbPanel.add(bbfield);
        bbfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                double value = getParsedInteger(bbfield.getText());
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

        JLabel beLabel = new JLabel("Birth end");
        JTextField befield = new JTextField(String.valueOf(NEW_BIRTH_END), 6);
        bePanel.add(beLabel);
        bePanel.add(befield);
        befield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                double value = getParsedInteger(befield.getText());
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

        JLabel fiLabel = new JLabel("First impact");
        JTextField fifield = new JTextField(String.valueOf(NEW_FST_IMPACT), 6);
        fiPanel.add(fiLabel);
        fiPanel.add(fifield);
        fifield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                double value = getParsedInteger(fifield.getText());
                NEW_FST_IMPACT = value;
            }
        });

        JLabel siLabel = new JLabel("Second impact");
        JTextField sifield = new JTextField(String.valueOf(NEW_SND_IMPACT), 6);
        siPanel.add(siLabel);
        siPanel.add(sifield);
        sifield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                double value = getParsedInteger(sifield.getText());
                NEW_SND_IMPACT = value;
            }
        });

        JButton okButton = new JButton("Ok");
        JButton cancelButton = new JButton("Cancel");
        okCancelPanel.add(okButton);
        okCancelPanel.add(cancelButton);

        okButton.addActionListener(e->onOk());
        cancelButton.addActionListener(e->setVisible(false));
    }

    private boolean isCorrectValues() {
        return !((NEW_LIVE_BEGIN <= NEW_BIRTH_BEGIN) && (NEW_BIRTH_BEGIN <=NEW_LIVE_END) && (NEW_BIRTH_BEGIN <= NEW_BIRTH_BEGIN));
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
