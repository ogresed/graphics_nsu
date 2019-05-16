package ru.nsu.fit.g16207.melnikov.utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DrawPanelUtil {
    public static void setBorder(int offset, Color color, JPanel panel) {
        Border marginBorder = BorderFactory.createEmptyBorder(offset, offset, offset, offset);
        if(color != null) {
            Border lineBorder = BorderFactory.createLineBorder(color);
            Border border = BorderFactory.createCompoundBorder(marginBorder, lineBorder);
            panel.setBorder(border);
        }
        else {
            panel.setBorder(marginBorder);
        }
    }

    public static void setNamedBorder(String name, JPanel panel) {
        Border border = BorderFactory.createTitledBorder(name);
        panel.setBorder(border);
    }

    public static BufferedImage createOffsetedImage (int width, int height, int offset, int option) {
        return new BufferedImage(width - 2* (offset + 1),
                height - 2*(offset + 1), option);
    }
}
