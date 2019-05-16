package ru.nsu.fit.g16207.melnikov.mf;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JLabel {
    public StatusBar() {
        super("Alright");
        super.setPreferredSize(new Dimension(100, 16));
    }

    public void setMessage(String message) {
        setText(message);
    }
}
