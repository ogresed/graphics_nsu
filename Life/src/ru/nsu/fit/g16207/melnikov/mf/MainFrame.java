package ru.nsu.fit.g16207.melnikov.mf;
import ru.nsu.fit.g16207.melnikov.logic.Cell;
import ru.nsu.fit.g16207.melnikov.mf.property.Property;
import ru.nsu.fit.g16207.melnikov.view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showOptionDialog;

public class MainFrame extends JFrame {
    public int PERIOD_OF_GAME = 400;
    private static String stringAbout = "Init, version 1.0\nCopyright" +
            "  2019 Sergey Melnikov, FIT, group 16207";
    public View view;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private Property property;

    private volatile boolean Run = false;

    private JToggleButton SI;
    public boolean showImpactsSelected = false;

    private JToggleButton XOR;
    public boolean XORSelected = false;

    private JButton runStopButton;
    private Icon playIcon;
    private Icon stopIcon;

    public MainFrame() {
        //set start size
        final int height = 600;
        final int width = 900;
        //set base options
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        setBounds((dimension.width - width) / 2, (dimension.height - height) / 2, width, height);
        setVisible(true);
        //add Menu options
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu file = makeMenu("FIle", 'F');
        JMenu help = makeMenu("Help", 'H');
        JMenu field = makeMenu("Field", 'D');
        JMenu options = makeMenu("Options", 'P');
        //add menu items
        makeMenuItem(file, "Load", openFileListener, 'L', "Load file");
        makeMenuItem(file, "Save", saveFileListener, 'S', "Save file");
        file.addSeparator();
        makeMenuItem(file, "Exit", exitListener, 'E', "Exit application");
        makeMenuItem(help, "About", aboutListener, 'A', "Shows program version and copyright information");
        //makeMenuItem(field, "Run/Stop", Listener, 'N', "Next");
        makeMenuItem(field, "Next", stepListener, 'N', "Next");
        makeMenuItem(field, "Clear field", clearListener, 'C', "Clear field");
        makeMenuItem(field, "Run / stop", runStopListener, 'R', "Run / stop");
        makeMenuItem(options, "Set options", optionListener, 'O', "Properties");
        makeMenuItem(options, "Show impacts", showImpactsListener, 'W', "Show impacts");
        makeMenuItem(options, "XOR", XORListener, 'X', "Set 'XOR' mode");
        //add toolbar and set base options
        toolBar = new JToolBar("Toolbar");
        add(toolBar, BorderLayout.PAGE_START);
        toolBar.setRollover(true);
        //add drawing component
        view = new View();
        view.setPreferredSize(new Dimension(view.getFieldWidth(), view.getFieldHeight()));
        add(view);
        JScrollPane pane = new JScrollPane(view);
        add(pane);
        //set options
        property = new Property(this);
        //add(property);
        //add buttons
        makeButton("gifs/exit.gif", exitListener, 'E', "Exit application (Alt + E)");
        makeButton("gifs/about.gif", aboutListener, 'A', "Shows program version and copyright information (Alt + A)");
        makeButton("gifs/step.gif", stepListener, 'N', "Next (Alt + N)");
        makeButton("gifs/clear.gif", clearListener, 'C', "Clear field (Alt + C)");
        makeButton("gifs/settings.gif", optionListener, 'O', "Set options (Alt + O)");
        makeButton("gifs/saveFile.gif", saveFileListener, 'S', "Save file(Alt + S)");
        makeButton("gifs/loadFile.gif", openFileListener, 'L', "Load file (Alt + L)");
        //make run/stop button
        playIcon = new ImageIcon("gifs/play.gif");
        stopIcon = new ImageIcon("gifs/pause.gif");
        runStopButton = new JButton();
        runStopButton.setMnemonic('R');
        runStopButton.setToolTipText("Run / stop game (Alt + R)");
        runStopButton.setIcon(playIcon);
        runStopButton.addActionListener(runStopListener);
        toolBar.addSeparator();
        toolBar.add(runStopButton);
        //set toggle buttons
            //set XOR button
        XOR = new JToggleButton();
        XOR.setMnemonic('X');
        XOR.setToolTipText("Set 'XOR' mode (Alt + X)");
        Icon XOROffIcon = new ImageIcon("gifs/xor.gif");
        XOR.setIcon(XOROffIcon);
        XOR.addActionListener(XORListener);
        toolBar.add(XOR);
            //set show impacts button
        SI = new JToggleButton();
        SI.setMnemonic('W');
        SI.setToolTipText("Shows impacts (Alt + W)");
        Icon showImpactOffIcon = new ImageIcon("gifs/show.gif");
        SI.setIcon(showImpactOffIcon);
        SI.addActionListener(showImpactsListener);
        toolBar.add(SI);
        //revalidate
        revalidate();
    }

    private ActionListener exitListener = e -> onExit();
    private ActionListener runStopListener = e->runStop();
    private ActionListener XORListener = e -> XOR();
    private ActionListener showImpactsListener = e -> showImpacts();
    private ActionListener saveFileListener = e->save(getSaveFileName());
    private ActionListener openFileListener = e-> loadFile();
    private ActionListener stepListener = e -> view.step();
    private ActionListener clearListener = e -> view.clear();
    private ActionListener optionListener = e-> onOpenOptions();
    private ActionListener aboutListener =  e -> showMessageDialog(this, stringAbout,
            "About Init", JOptionPane.INFORMATION_MESSAGE);

    private void onOpenOptions () {
        property.setValues();
        property.setValuesIntoComponents();
        property.setVisible(true);

    }

    private void save(File file) {
        if(file == null)
            return;
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.write(String.format("%d %d\n", view.getHorizontally(), view.getVertically()));
            writer.write(String.format("%d\n", view.getThickness()));
            writer.write(String.format("%d\n", view.getRadius()));
            writer.write(String.format("%d\n", view.getNumberOfAliveCells()));
            int i = 0; int j = 0;
            for(Cell[] cell: view.getCells()) {
                for(Cell cell1 : cell) {
                    if(cell1.isLife()) {
                        writer.write(String.format("%d %d\n",j, i));
                    }
                    j++;
                }
                i++;
                j = 0;
            }
            writer.flush();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    public void showImpacts() {
        if(!showImpactsSelected) {
            property.showImpacts.setSelected(true);
            SI.setSelected(true);
            showImpactsSelected = true;
            view.setShowImp(true);
            return;
        }
        property.showImpacts.setSelected(false);
        SI.setSelected(false);
        showImpactsSelected = false;
        view.setShowImp(false);
    }

    public void XOR() {
        if(!XORSelected) {
            property.xor.setSelected(true);
            XOR.setSelected(true);
            XORSelected = true;
            view.setXOR();
            return;
        }
        property.replace.setSelected(true);
        XOR.setSelected(false);
        XORSelected = false;
        view.setReplace();
    }

    private void runStop() {
        if(Run) {
            Run = false;
            runStopButton.setIcon(playIcon);
        }
        else {
            Run = true;
            runStopButton.setIcon(stopIcon);
            Thread thread = new Thread(() -> {
                while (Run && !view.step()) {
                    try {
                        Thread.sleep(PERIOD_OF_GAME);
                    } catch (InterruptedException g) {
                        g.printStackTrace();
                    }
                }
                Run = false;
                runStopButton.setIcon(playIcon);
            });
            thread.start();
        }
    }

    private void onExit() {
        String[] options = {"Yes", "No"};
        int x =
        showOptionDialog(null, "Save this configuration?", "Exit", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (x == 0) {
            save(getSaveFileName());
        }
        System.exit(0);
    }

    private void loadFile() {
        view.load(getOpenFileName());
        view.setPreferredSize(new Dimension(view.getFieldWidth(), view.getFieldHeight()));
        revalidate();
    }

    private void makeButton(String icon, ActionListener l, char f, String description) {
        Icon ICon = new ImageIcon(icon);
        JButton button = new JButton(ICon);
        toolBar.add(button);
        button.addActionListener(l);
        button.setMnemonic(f);
        button.setToolTipText(description);
    }

    private void makeMenuItem(JMenu menu, String s, ActionListener l, char f, String description) {
        JMenuItem item = menu.add(new JMenuItem(s));
        item.addActionListener(l);
        item.setMnemonic(f);
        item.setToolTipText(description);
    }

    private JMenu makeMenu(String file, char f) {
        JMenu menu = new JMenu(file);
        menu.setMnemonic(f);
        menuBar.add(menu);
        return  menu;
    }

    private File getSaveFileName() {
        return FileUtils.getSaveFileName(this, "txt", "Text file");
    }

    private File getOpenFileName() {
        return FileUtils.getOpenFileName(this, "txt", "Text file");
    }
}
