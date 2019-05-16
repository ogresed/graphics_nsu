package ru.nsu.fit.g16207.melnikov.mf;

import ru.nsu.fit.g16207.melnikov.mf.baseframe.BaseFrame;
import ru.nsu.fit.g16207.melnikov.settings.Settings;
import ru.nsu.fit.g16207.melnikov.utils.MathUtil;
import ru.nsu.fit.g16207.melnikov.view.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static javax.swing.JOptionPane.showMessageDialog;

public class MainFrame extends BaseFrame {
    private MainPanel panel;
    private Settings settings;
    private final String ABOUT = "Init, version 1.0\nCopyright" +
            "  2019 Sergey Melnikov, FIT, group 16207";
    public MainFrame() {
        super(JFrame.EXIT_ON_CLOSE, "Wireframe");
        setResizable(false);
        //set bounds
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        int startWidth = MathUtil.multiplyByFraction(10, 11, dimension.width);
        int startHeight = MathUtil.multiplyByFraction(8, 9, dimension.height);
        setBounds(0, 0, startWidth, startHeight);
        //create buttons
        createButtons();
        //set components
        StatusBar bar = new StatusBar();
        panel = new MainPanel(bar);
        add(panel, BorderLayout.CENTER);
        add(bar, BorderLayout.SOUTH);
        settings = new Settings(this);
        setVisible(true);
        revalidate();
    }

    private ActionListener exitListener = e -> System.exit(0);
    private ActionListener openListener = e -> {};
    private ActionListener saveListener = e -> {};
    private ActionListener initListener = e -> {};
    private ActionListener settingsListener = e -> settings.setVisible(true);
    private ActionListener aboutListener = e -> showMessageDialog(this, ABOUT,
            "About Init", JOptionPane.INFORMATION_MESSAGE);

    @Override
    public void createButtons() {
        JMenu file =  makeMenu("File", 'F');
        createAction(file, "pictures/Exit.png", exitListener,'E', "Exit application");
        createAction(file, "pictures/Open.png", openListener,'O', "Open file");
        createAction(file, "pictures/Save.png", saveListener,'S', "Save file");
        toolBar.addSeparator();
        JMenu settings = makeMenu("Settings", 'N');
        createAction(settings, "pictures/Settings.png", settingsListener,'T', "Settings");
        createAction(settings, "pictures/Init.png", initListener,'I', "Init image");
        toolBar.addSeparator();
        JMenu about = makeMenu("About", 'B');
        createAction(about, "pictures/About.png", aboutListener,'A', "About this application");
    }
}
