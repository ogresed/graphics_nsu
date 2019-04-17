package ru.nsu.fit.g16207.melnikov.mf;

import ru.nsu.fit.g16207.melnikov.Configuration;
import ru.nsu.fit.g16207.melnikov.Main;
import ru.nsu.fit.g16207.melnikov.WrongValueException;
import ru.nsu.fit.g16207.melnikov.function.Function;
import ru.nsu.fit.g16207.melnikov.mf.property.Property;
import ru.nsu.fit.g16207.melnikov.view.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MainFrame extends JFrame {
    private static int width;
    private static int height;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private Configuration configuration;
    private MainPanel panel;
    private Property property;
    public MainFrame()  {
        //set base options
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        width = Main.multiplyByFraction(6, 7, dimension.width);
        height = Main.multiplyByFraction    (6, 7, dimension.height);
        setBounds((dimension.width - width) / 2, (dimension.height - height) / 2, width, height);
        //setResizable(false);
        setTitle("Isolines");
        //create menu
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        createMenu();
        //create toolbar
        toolBar = new JToolBar();
        add(toolBar, BorderLayout.PAGE_START);
        toolBar.setRollover(true);
        createToolbar();
        //create main panel
        configuration = new Configuration(new Function(-10, 10, -10, 10));
        panel = new MainPanel(this, configuration);
        property =  new Property(panel, configuration);
        add(panel);
        //end options
        setVisible(true);
        revalidate();
    }

    private ActionListener exitListener = e -> System.exit(0);
    private ActionListener openListener = e -> openFile(getOpenFileName());
    private ActionListener interpolationListener = e -> panel.setInterpolation();
    private ActionListener gridListener = e -> panel.setGrid();
    private ActionListener optionsListener = e -> property.setVisible(true);

    private void createMenu() {
        JMenu file =  makeMenu("File", 'F');
        makeMenuItem(file, "Exit", exitListener, 'E', "Exit");
        makeMenuItem(file, "Open", openListener, 'O', "Open configuration");
        JMenu image = makeMenu("Image", 'I');
        makeMenuItem(image, "View", interpolationListener, 'V', "Change view image");
        makeMenuItem(image, "Grid", gridListener, 'G', "Show grid");
        makeMenuItem(image, "Options", optionsListener, 'P', "Show options");
    }

    private void createToolbar() {
        makeButton("Exit", exitListener,'E', "Exit");
        makeButton("Open", openListener,'O', "Exit");
        makeButton("View", interpolationListener,'V', "Change view image");
        makeButton("Grid", gridListener,'D', "Show grid");
        makeButton("Options", optionsListener,'P', "Show options");
    }

    private void makeMenuItem(JMenu menu, String name, ActionListener l, char mnemonic, String description) {
        JMenuItem item = menu.add(new JMenuItem(name));
        item.addActionListener(l);
        item.setMnemonic(mnemonic);
        item.setToolTipText(description);
    }

    private JMenu makeMenu(String file, char mnemonic) {
        JMenu menu = new JMenu(file);
        menu.setMnemonic(mnemonic);
        menuBar.add(menu);
        return  menu;
    }

    private void makeButton(String nameOfIcon, ActionListener listener, char mnemonic, String description) {
        Icon icon = new ImageIcon(nameOfIcon);
        JButton button;
        if(icon.getIconWidth() == -1) {
            button = new JButton(nameOfIcon);
        } else {
            button = new JButton(icon);
        }
        toolBar.add(button);
        button.addActionListener(listener);
        button.setMnemonic(mnemonic);
        button.setToolTipText(description + "(Alt + " + mnemonic + ")");
    }

    public void openFile(File file) {
        if(file == null) {
            return;
        }
        try {
            Scanner scanner = new Scanner(file);
            //load grid parameters
            String strings[] = stringsWithoutSpacesSeparatedCommentSymbol(scanner.nextLine());
            String[] gridValues = strings[0].split(" ");
            if(gridValues.length != 2) {
                throw new WrongValueException();
            }
            int xSize = Integer.parseInt(gridValues[0]);
            int ySize = Integer.parseInt(gridValues[1]);
            //load number of functions values
            strings = stringsWithoutSpacesSeparatedCommentSymbol(scanner.nextLine());
            String[] number = strings[0].split(" ");
            if(number.length != 1) {
                throw new WrongValueException();
            }
            String numberOfValues = number[0];
            int valuesNumber = Integer.parseInt(numberOfValues);
            if(valuesNumber < 0) {
                throw new WrongValueException();
            }
            //load colors
            int colorComponents[] = new int [3];
            Color colors[] = new Color[valuesNumber + 2];
            for(int i = 0; i < valuesNumber + 2; i++) {
                strings = stringsWithoutSpacesSeparatedCommentSymbol(scanner.nextLine());
                String colorsValues[] = strings[0].split(" ");
                if(colorsValues.length < 3) {
                    throw new WrongValueException();
                }
                for(int j = 0; j < 3; j++) {
                    colorComponents[j] = Integer.parseInt(colorsValues[j]);
                }
                colors[i] = new Color(colorComponents[0], colorComponents[1], colorComponents[2]);
            }
            //set loaded parameters
            configuration.setConfiguration(colors, xSize, ySize, valuesNumber);
            panel.createLegend();
            panel.createAndShowImage();
        } catch (FileNotFoundException e) {
            System.out.println("File not file");
        } catch (WrongValueException | NumberFormatException e) {
            System.out.println("Wrong files format");
        }
    }

    private String[] stringsWithoutSpacesSeparatedCommentSymbol(String s) {
        return s.replaceAll(" {4}", " "). replaceAll("[\\s]{2,}", " ").split("//");
    }

    private File getOpenFileName() {
        return FileUtils.getOpenFileName(this, "txt", "Text file");
    }
}
