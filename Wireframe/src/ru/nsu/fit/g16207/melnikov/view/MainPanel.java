package ru.nsu.fit.g16207.melnikov.view;

import ru.nsu.fit.g16207.melnikov.mf.StatusBar;
import ru.nsu.fit.g16207.melnikov.utils.DrawPanelUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainPanel extends JPanel {
    private final int offset = 10;
    private StatusBar statusBar;
    private BufferedImage image;
    public MainPanel(StatusBar bar) {
        statusBar = bar;
        DrawPanelUtil.setBorder(offset, Color.RED, this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(image == null) {
            image =  DrawPanelUtil.createOffsetedImage (
                    this.getWidth(), this.getHeight(), offset, BufferedImage.TYPE_INT_RGB);
        }
        g.drawImage(image, offset + 1, offset + 1, null);
    }
}
