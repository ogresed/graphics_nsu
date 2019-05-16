package ru.nsu.fit.g16207.melnikov.settings;

import ru.nsu.fit.g16207.melnikov.mf.MainFrame;
import ru.nsu.fit.g16207.melnikov.mf.StatusBar;
import ru.nsu.fit.g16207.melnikov.utils.MathUtil;

import javax.swing.*;
import java.awt.*;

public class Settings extends JFrame {
    public Settings(MainFrame frame) {
        setTitle("Settings");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        int startWidth = MathUtil.multiplyByFraction(2, 3, frame.getWidth());
        int startHeight = MathUtil.multiplyByFraction(2, 3, frame.getHeight());
        setBounds(0, 0, startWidth, startHeight);
        StatusBar bar = new StatusBar();
        BSplinePanel setSplinePanel = new BSplinePanel(this, bar);
        add(setSplinePanel);
        add(bar, BorderLayout.SOUTH);
        setResizable(false);
        revalidate();
    }
}
