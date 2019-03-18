package ru.nsu.fit.g16207.melnikov.mf.property;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static javax.swing.JOptionPane.showMessageDialog;
class SetImpacts extends JFrame {
    private final static String WRONG_VALUES = "Wrong values";
    private Property property;
    private static boolean correct = true;
    private Impact NEW_LIVE_BEGIN = new Impact( 2.0);
    private Impact NEW_LIVE_END = new Impact(3.3);
    private Impact NEW_BIRTH_BEGIN = new Impact(2.3);
    private Impact NEW_BIRTH_END = new Impact( 2.9);
    private Impact NEW_FST_IMPACT = new Impact( 1.0);
    private Impact NEW_SND_IMPACT = new Impact( 0.3);
    private JLabel info;
    SetImpacts(Property property) {
        this.property = property;
        int height = 400;
        int width = 200;
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(8, 1));
        add(mainPanel);
        //set base options
        setVisible(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        setBounds((dimension.width - width) / 2, (dimension.height - height) / 2, width, height);
        //set panels
        info = new JLabel(""); mainPanel.add(info);
        JPanel lifeBeginPanel = new JPanel(); mainPanel.add(lifeBeginPanel);
        JPanel lifeEndPanel = new JPanel(); mainPanel.add(lifeEndPanel);
        JPanel birthBeginPanel = new JPanel(); mainPanel.add(birthBeginPanel);
        JPanel birthEbdPanel = new JPanel(); mainPanel.add(birthEbdPanel);
        JPanel firstImpPanel = new JPanel(); mainPanel.add(firstImpPanel);
        JPanel secondImpPanel = new JPanel(); mainPanel.add(secondImpPanel);
        JPanel okCancelPanel = new JPanel(); mainPanel.add(okCancelPanel);
        //set listeners
        JTextField lifeBeginField = createField("Life being", lifeBeginPanel, NEW_LIVE_BEGIN);
        lifeBeginField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                setValue(NEW_LIVE_BEGIN, lifeBeginField);
            }
        });
        JTextField lifeEndField = createField("Life End", lifeEndPanel, NEW_LIVE_END);
        lifeEndField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                setValue(NEW_LIVE_END, lifeEndField);
            }
        });
        JTextField birthBeginField = createField("Birth begin", birthBeginPanel, NEW_BIRTH_BEGIN);
        birthBeginField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                setValue(NEW_BIRTH_BEGIN, birthBeginField);
            }
        });
        JTextField birthEndField = createField("Birth End", birthEbdPanel, NEW_BIRTH_END);
        birthEndField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                setValue(NEW_BIRTH_END, birthEndField);
            }
        });
        JTextField firstImpField = createField("First impact", firstImpPanel, NEW_FST_IMPACT);
        firstImpField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                NEW_FST_IMPACT.setImp(getParsedDouble(firstImpField.getText()));
            }
        });
        JTextField secImpField = createField("Second impact", secondImpPanel, NEW_SND_IMPACT);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                NEW_SND_IMPACT.setImp(getParsedDouble(secImpField.getText()));
            }
        });
        //set ok/cancel panel
        JButton okButton = new JButton("Ok");
        JButton cancelButton = new JButton("Cancel");
        okCancelPanel.add(okButton);
        okCancelPanel.add(cancelButton);
        //set ok.cancel listeners
        okButton.addActionListener(e->onOk());
        cancelButton.addActionListener(e->setVisible(false));
    }
    private JTextField createField (String name, JPanel panel, Impact beginVal) {
        JLabel label = new JLabel(name);
        JTextField field = new JTextField(String.valueOf(beginVal.getImp()), 6);
        panel.add(label);
        panel.add(field);
        return  field;
    }
    private void onOk() {
        if(!correct) {
            showMessageDialog(this, WRONG_VALUES,
                    "WARNING", JOptionPane.WARNING_MESSAGE);
            return;
        }
        property.view.setImpacts(NEW_LIVE_BEGIN.getImp(), NEW_LIVE_END.getImp(), NEW_BIRTH_BEGIN.getImp(),
                NEW_BIRTH_END.getImp(), NEW_FST_IMPACT.getImp(), NEW_SND_IMPACT.getImp());
        property.view.resetImpAndRedraw();
        setVisible(false);
    }
    private void setValue(Impact impact, JTextField field) {
        double value = getParsedDouble(field.getText());
        double tmp = impact.getImp();
        impact.setImp(value);
        if(isCorrectValues()) {
            setCorrect(true);
        }
        else {
            setCorrect(false);
            impact.setImp(tmp);
        }
    }
    private boolean isCorrectValues() {
        return (NEW_LIVE_BEGIN.getImp() >= 0.0 && NEW_BIRTH_BEGIN.getImp() >= 0.0 &&
                NEW_BIRTH_END.getImp() >= 0.0 && NEW_LIVE_END.getImp() >= 0.0) &&
                (NEW_LIVE_BEGIN.getImp() <= NEW_BIRTH_BEGIN.getImp() &&
                        NEW_BIRTH_BEGIN.getImp() <= NEW_BIRTH_END.getImp() &&
                        NEW_BIRTH_END.getImp() <= NEW_LIVE_END.getImp());
    }
    private void setCorrect(boolean bool) {
        correct = bool;
        if(bool) {
            info.setText("Correct");
        } else {
            info.setText("Wrong value");
        }
    }
    private double getParsedDouble(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }
}
