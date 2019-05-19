package ru.nsu.fit.g16207.melnikov.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public abstract class MyDialog extends JDialog {
    private static final String OK = "OK";
    private static final String CANCEL = "Cancel";
    private boolean isCancelled = true;

    public MyDialog(JFrame jFrame, String title, int rowForOkAndCancelButtons) {
        this(jFrame, title, null, rowForOkAndCancelButtons);
    }

    public MyDialog(JFrame jFrame, String title, HashMap<String, Object> args, int rowForOkAndCancelButtons) {
        super(jFrame, title, true);

        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        onDialogCreated(args);
        if (rowForOkAndCancelButtons > 0) {
            addOkAndCancelButtons(rowForOkAndCancelButtons);
        }

        pack();
    }

    protected void addComponent(int row, int column, JComponent jComponent) {
        addComponent(row, column, 1, 1, jComponent);
    }

    protected void addComponent(int row, int column, int width, int height, JComponent jComponent) {
        addComponent(row, column, width, height, GridBagConstraints.NONE, jComponent);
    }

    protected void addComponent(int row, int column, int width, int height, int fill, JComponent jComponent) {
        GridBagConstraints constraints = new GridBagConstraints();

        Insets insets = new Insets(5, 10, 5, 10);

        constraints.gridx = column;
        constraints.gridy = row;
        constraints.insets = insets;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        constraints.fill = fill;

        add(jComponent, constraints);
    }

    private void addOkAndCancelButtons(int row) {
        JButton okButton = new JButton(OK);
        JButton cancelButton = new JButton(CANCEL);

        addComponent(row, 0, okButton);
        addComponent(row, 1, cancelButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                isCancelled = true;
                setVisible(false);
            }
        });

        cancelButton.addActionListener(e -> {
            isCancelled = true;
            setVisible(false);
        });

        okButton.addActionListener(e -> {
            isCancelled = false;
            setVisible(false);
        });
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    protected abstract void onDialogCreated(HashMap<String, Object> propertyResourceBundle);
}
