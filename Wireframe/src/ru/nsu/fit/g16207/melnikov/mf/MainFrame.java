package ru.nsu.fit.g16207.melnikov.mf;

import ru.nsu.fit.g16207.melnikov.Model;
import ru.nsu.fit.g16207.melnikov.mf.baseframe.FileUtils;
import ru.nsu.fit.g16207.melnikov.view.SettingsDialog;
import ru.nsu.fit.g16207.melnikov.view.ShapeView;
import ru.nsu.fit.g16207.melnikov.matrix.Matrix;
import ru.nsu.fit.g16207.melnikov.mf.baseframe.BaseFrame;
import ru.nsu.fit.g16207.melnikov.model_loader.ModelLoader;
import ru.nsu.fit.g16207.melnikov.model_loader.ModelSaver;
import ru.nsu.fit.g16207.melnikov.universal_parser.NoObjectFactoryException;
import ru.nsu.fit.g16207.melnikov.universal_parser.ParserException;
import ru.nsu.fit.g16207.melnikov.universal_parser.TypeConversionException;
import ru.nsu.fit.g16207.melnikov.universal_parser.TypeMatchingException;
import ru.nsu.fit.g16207.melnikov.utils.MathUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import static javax.swing.JOptionPane.showMessageDialog;

public class MainFrame extends BaseFrame {
    private static final String DATA_FOLDER = "FIT_16207_Melnikov_Wireframe_Data";
    private final String ABOUT = "Init, version 1.0\nCopyright" +
            "  2019 Sergey Melnikov, FIT, group 16207";
    private Model model;
    private static final int DEFAULT_WIDTH = 1024;
    private static final int DEFAULT_HEIGHT = 800;
    private final ShapeView shapeView = new ShapeView(DEFAULT_WIDTH, DEFAULT_HEIGHT);
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
        JPanel mainFrameJPanel = new JPanel();
        shapeView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!shapeView.hasFocus()) {
                    shapeView.requestFocus();
                } else {
                    shapeView.transferFocus();
                }
            }
        });

        shapeView.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
            }
        });
        add(mainFrameJPanel);
        mainFrameJPanel.add(shapeView);

        setVisible(true);
        revalidate();
    }

    private ActionListener exitListener = e -> System.exit(0);
    private ActionListener openListener = e -> onOpenButtonClick();
    private ActionListener saveListener = e -> onSaveButtonClick();
    private ActionListener initListener = e -> onInitButtonClick();
    private ActionListener settingsListener = e -> onSettingButtonClick();
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

    private void onOpenButtonClick() {
            try {
                loadModel(FileUtils.getOpenFileName(this, "txt", "Text file"));
            } catch (NullPointerException ignore) {
            } catch (NoObjectFactoryException | TypeConversionException | ParserException | TypeMatchingException e) {
                System.out.println("Impossible to open file");
            }
    }

    public void loadModel(File file) throws NoObjectFactoryException, TypeConversionException, TypeMatchingException, ParserException {
        ModelLoader modelLoader = new ModelLoader(file);
        this.model = modelLoader.getModel();
        shapeView.setModel(model);

        if (!model.getbSpline().isEmpty()) {
            shapeView.setSelectedShape(0);
        }
    }

    private void onSaveButtonClick() {
        try {
            ModelSaver.saveModel(FileUtils.getSaveFileName(this, "txt", "Text file"),
                    model);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error", "Can't save. Reason: "
                            + e.getMessage(),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onInitButtonClick() {
        if (null != model) {
            shapeView.setSceneRotationMatrix(new Matrix(new double[][] {
                    {1, 0, 0, 0},
                    {0, 1, 0, 0},
                    {0, 0, 1, 0},
                    {0, 0, 0, 1}
            }));
            shapeView.update();
        }
    }

    private void onSettingButtonClick() {
        if (null != model) {
            Integer selectedShape = shapeView.getSelectedShape();
            SettingsDialog settingsDialog = new SettingsDialog(this, "Settings", -1,
                    model, selectedShape);
            settingsDialog.setVisible(true);
            shapeView.setSelectedShape(settingsDialog.getSelectedShape());
            shapeView.update();
        }
    }

}
