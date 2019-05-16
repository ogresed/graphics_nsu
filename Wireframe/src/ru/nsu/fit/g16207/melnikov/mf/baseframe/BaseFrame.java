package ru.nsu.fit.g16207.melnikov.mf.baseframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

public abstract class BaseFrame extends JFrame {
    private JMenuBar menuBar;
    protected JToolBar toolBar;

    protected BaseFrame(int option, String name) {
        //set base options
        setDefaultCloseOperation(option);
        setTitle(name);
        //create menu
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        //create toolbar
        toolBar = new JToolBar();
        add(toolBar, BorderLayout.PAGE_START);
        toolBar.setRollover(true);
    }

    protected void createButtons() {

    }

    protected void createAction(
            JMenu menu,
            String nameOfIcon,
            ActionListener listener,
            char mnemonic,
            String description
    ) {
        makeMenuItem(menu, nameOfIcon, listener, mnemonic, description);
        makeButton(nameOfIcon, listener,mnemonic, description);
    }

    private void makeMenuItem(JMenu menu, String name, ActionListener l, char mnemonic, String description) {
        name = getMainName(name);
        JMenuItem item = menu.add(new JMenuItem(name));
        item.addActionListener(l);
        item.setMnemonic(mnemonic);
        item.setToolTipText(description);
    }

    protected JMenu makeMenu(String file, char mnemonic) {
        JMenu menu = new JMenu(file);
        menu.setMnemonic(mnemonic);
        menuBar.add(menu);
        return  menu;
    }

    private void makeButton(String nameOfIcon, ActionListener listener, char mnemonic, String description) {
        Icon icon = new ImageIcon(nameOfIcon);
        JButton button;
        if(icon.getIconWidth() == -1) {
            button = new JButton(getMainName(nameOfIcon));
        } else {
            button = new JButton(icon);
        }
        toolBar.add(button);
        button.addActionListener(listener);
        button.setMnemonic(mnemonic);
        button.setToolTipText(description + "(Alt + " + mnemonic + ")");
    }

    private String getMainName(String string) {
        String tmp[] = string.split("/");
        string = tmp[tmp.length - 1];
        tmp = string.split("[.]");
        return tmp[0];
    }

    private void openFile(File file) {

    }

    private String[] stringsWithoutSpacesSeparatedCommentSymbol(String s) {
        return s.replaceAll(" {4}", " ").
                replaceAll("[\\s]{2,}", " ").
                split("//");
    }

    protected File getOpenFileName() {
        return FileUtils.getOpenFileName(this, "txt", "Text file");
    }
    protected File getSaveFileName () {
        return FileUtils.getSaveFileName(this, "txt", "Text file");
    }
}
