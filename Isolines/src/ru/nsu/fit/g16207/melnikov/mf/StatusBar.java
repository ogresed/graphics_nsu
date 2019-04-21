package ru.nsu.fit.g16207.melnikov.mf;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JLabel {
    /** Creates a new instance of StatusBar */
    StatusBar() {
        super();
        super.setPreferredSize(new Dimension(100, 16));
    }

    public void setMessage(double x, double y, double f) {
        setText(String.format("x: %.2f    y: %.2f    value: %.3f", x, y, f));
    }
}
