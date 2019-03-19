package ru.nsu.fit.g16207.melnikov.view;
import ru.nsu.fit.g16207.melnikov.logic.Cell;
import ru.nsu.fit.g16207.melnikov.logic.Logic;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
public class View extends Logic {
    private static final int MIN_RADIUS_T0_DRAW_IMPACTS = 10;
    private BufferedImage image;
    private static int fieldWidth;
    private static int fieldHeight;
    private static Color borderColor = new Color(1, 1, 1);
    private static Color fillingColor = new Color(10, 140, 50);
    private static Color emptyCellColor = new Color(0,0,0,0);
    private static Color changeableColor = emptyCellColor;
    private static Color paintingColor = fillingColor;
    private boolean XOR = false;
    private boolean showImp = false;
    /**
     * algorithm drawing lines of Brasenhem
     * */
    private void drawBrasenhem(int xStart, int yStart, int xEnd, int yEnd, Color color) {
        int colorRGB = color.getRGB();
        int x, y, xSaveDirection, ySaveDirection, offset, orthogonalDirection;
        int dx = xEnd - xStart;
        int dy = yEnd - yStart;
        int xDirection = Integer.compare(dx, 0);
        int yDirection = Integer.compare(dy, 0);
        dx = Math.abs(dx);
        dy = Math.abs(dy);
        if (dx > dy) {
            xSaveDirection = xDirection;
            ySaveDirection = 0;
            offset = dy;
            orthogonalDirection = dx;
        }
        else {
            xSaveDirection = 0;
            ySaveDirection = yDirection;
            offset = dx;
            orthogonalDirection = dy;
        }
        x = xStart;
        y = yStart;
        int err = orthogonalDirection/2;
        image.setRGB(x, y, colorRGB);
        for (int t = 0; t < orthogonalDirection; t++) {
            err -= offset;
            if (err < 0) {
                err += orthogonalDirection;
                x += xDirection;
                y += yDirection;
            }
            else {
                x += xSaveDirection;
                y += ySaveDirection;
            }
            image.setRGB(x, y, colorRGB);
        }
    }
    /**
     * span filling algorithm
     * */
    private void spanFilling(int x, int y, Color color) {
        if(image.getRGB(x, y) == paintingColor.getRGB()) {
            return;
        }
        int changeable = (image.getRGB(x,y) == emptyCellColor.getRGB() ? emptyCellColor.getRGB() : changeableColor.getRGB());
        Span span;
        //set into stack first value
        Stack<Span> stack = new Stack<>();
        stack.push(searchSpan(x, y));
        while (!stack.empty()) {
            span = stack.pop();
            int saveX1 = span.X1, saveX2 = span.X2, saveY = span.Y;
            int X1 = saveX1, X2 = saveX2, Y = saveY;
            fillSpan(span, color);
            //search all spans below 1
            do {
                while (X1 <= X2 && image.getRGB(X1, Y + 1) != changeable)
                    X1++;
                if (X1 <= X2 && image.getRGB(X1, Y+1) == changeable) {
                    span = searchSpan(X1, Y + 1);
                    stack.push(span);
                    X1 = span.X2 + 2;
                }
            } while (X1 < X2);
            //search all spans higher 1
            X1 = saveX1; X2 = saveX2; Y = saveY;
            do {
                while (X1 <= X2 && image.getRGB(X1, Y - 1) != changeable)
                    X1++;
                if (X1 <= X2 && image.getRGB(X1, Y-1) == changeable) {
                    span = searchSpan(X1, Y - 1);
                    stack.push(span);
                    X1 = span.X2 + 2;
                }
            } while (X1 < X2);
        }
    }
    private Span searchSpan(int x, int y) {
        int x1 = x, x2 = x;
        int changeable = changeableColor.getRGB();
        try {
            while (image.getRGB(x1 - 1, y) == changeable)
                x1--;
            while (image.getRGB(x2 + 1, y) == changeable)
                x2++;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(x1 +" "+x);
        }
        return  new Span(x1, x2, y);
    }
    private void fillSpan(Span span, Color color) {
        drawBrasenhem(span.X1, span.Y, span.X2, span.Y, color);
    }
    private void setSizes() {
        fieldHeight = getNewFieldHeight(vertically, radius, thickness);
        fieldWidth = getNewFieldWidth(horizontally, radius, thickness);
        image = new BufferedImage(fieldWidth, fieldHeight, BufferedImage.TYPE_INT_ARGB);
    }
    public View() {
        setSizes();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                mouseAction(x, y);
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if(!XOR) {
                    mouseAction(x, y);
                } else {
                    XOR = false;
                    swapColor();
                    mouseAction(x,y);
                    swapColor();
                    XOR = true;
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
    private int getNewFieldWidth(int hor, int rad, int thick) {
        return (hor + 1)*(thick + 2*rad);
    }
    private int getNewFieldHeight(int vent, int rad, int thick) {
        return 3*(vent + 1) *(rad + thick)/2 ;
    }
    private void mouseAction(int x, int y) {
        if(isIntoField(x, y)) {
            double comparableDistance = Double.MAX_VALUE;
            int line = 0; int column = 0;
            for(int i = 0; i < vertically; i++) {
                for(int j = 0; j < horizontally - i%2; j++) {
                    Cell cell = cells[i][j];
                    double distance = Math.sqrt((cell.getX() - x)*(cell.getX() - x) + (cell.getY() - y)*(cell.getY() - y));
                    if(distance < comparableDistance) {
                        comparableDistance = distance;
                        line = i; column = j;
                    }
                }
            }
            int xSeed = cells[line][column].getX();
            int ySeed = cells[line][column].getY();
            int currentColor = image.getRGB(xSeed, ySeed);
            if(XOR || currentColor == changeableColor.getRGB()) {
                if (XOR && currentColor == paintingColor.getRGB()) {
                    swapColor();
                    spanFilling(xSeed, ySeed, paintingColor);
                    swapColor();
                } else {
                    spanFilling(xSeed, ySeed, paintingColor);
                }
                changeState(line, column);
            }
        }
    }
    private boolean isIntoField(int x, int y) {
        try {
            int currentColor = image.getRGB(x,y);
                return currentColor!=borderColor.getRGB() && searchBorder(x, y);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }
    private boolean searchBorder(int x, int y) {
        int yMin = y < fieldHeight - y ? y : fieldHeight - y;
        int xMin = x < fieldWidth - x ? x : fieldWidth - x;
        int border =  borderColor.getRGB();
        try {
            if (yMin < xMin) {
                boolean sCase =  checkSpecialCase(x, y);
                if(!sCase) {
                    return false;
                }
                else {
                    int yBorder = yMin == y ? 0 : fieldHeight;
                    int direction = yBorder == 0 ? -1 : 1;
                    int currentColor = image.getRGB(x, y);
                    while (y != yBorder && currentColor != border) {
                        y += direction;
                        currentColor = image.getRGB(x, y);
                    }
                    return currentColor == border;
                }
            } else {
                int xBorder = xMin == x ? 0 : fieldWidth;
                int direction = xBorder == 0 ? -1 : 1;
                int currentColor = image.getRGB(x, y);
                while (x != xBorder && image.getRGB(x, y) != border) {
                    x += direction;
                    currentColor = image.getRGB(x, y);
                }
                return currentColor == border;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawField();
        g.drawImage(image,0, 0, null);
        if(showImp &&  radius > MIN_RADIUS_T0_DRAW_IMPACTS) {
            drawImpacts(g);
        }
        repaint();
    }
    private void drawImpacts(Graphics g) {
        for (Cell[] cell : cells) {
            for (Cell cell1 : cell) {
                int sizeOfNumbers = 2 * radius / 3;
                g.setFont(new Font(null, Font.BOLD, sizeOfNumbers));
                double impact = Math.abs(cell1.getImpact());
                String number = String.format("%.1f",impact );
                int xOffsetForDrawImpact = -(5* radius /4) / 2;
                int yOffsetForDrawImpact = (radius + thickness/2) / 2;
                int x = cell1.getX() + xOffsetForDrawImpact;
                int y = cell1.getY() + yOffsetForDrawImpact;
                g.drawString(number, x, y);
            }
        }
    }
    private void drawField() {
        if(thickness == 0) {
            drawHexesForThickness1();
            return;
        }
        //draw external border
        drawExternalBorder();
        //draw internal hexes
        int xCoordinateOfCell = thickness;
        int yCoordinateOfCell = thickness;
        //draw odd lines
        for(int i = 0; i < (vertically + 1)/2; i++) {
            for(int j = 0; j< horizontally; j++) {
                drawHex(xCoordinateOfCell, yCoordinateOfCell, radius);
                xCoordinateOfCell+=2* radius + thickness;
            }
            xCoordinateOfCell = thickness;
            yCoordinateOfCell += 3 * radius + 2*thickness;
        }
        //draw even lines
        xCoordinateOfCell = radius + thickness/2 + thickness;
        yCoordinateOfCell = thickness + thickness + 3* radius /2;
        for(int i = 0; i < vertically /2; i++) {
            for (int j = 0; j < horizontally - 1; j++) {
                drawHex(xCoordinateOfCell, yCoordinateOfCell, radius);
                xCoordinateOfCell += 2 * radius + thickness;
            }
            xCoordinateOfCell = radius + thickness /2 + thickness;
            yCoordinateOfCell += 3 * radius + 2 * thickness;
        }
        //filling filed
        spanFilling(1, thickness + radius, borderColor);
    }
    private void drawExternalBorder() {
        int size = radius + thickness;
        int xCoordinateOfCell = 0;
        int yCoordinateOfCell = 0;
        int numberOfOddLines = (vertically + 1)/2;
        //draw main skeleton
        drawHexWithoutRightLow(xCoordinateOfCell, yCoordinateOfCell, size);
        xCoordinateOfCell += 2 * size - thickness;
        for(int i = 1; i < horizontally - 1; i++) {
            drawHexWithoutRightAndLeftLow(xCoordinateOfCell, yCoordinateOfCell, size);
            xCoordinateOfCell += 2 * size - thickness;
        }
        drawHexWithoutLeftLow(xCoordinateOfCell, yCoordinateOfCell, size);
        xCoordinateOfCell = 0;
        yCoordinateOfCell += 3 * size - thickness;
        for (int i = 1; i < numberOfOddLines - 1; i++) {
            drawHexWithoutRightUpperLow(xCoordinateOfCell, yCoordinateOfCell, size);
            xCoordinateOfCell += (2 * size - thickness) * (horizontally - 1);
            drawHexWithoutLeftUpperLow(xCoordinateOfCell, yCoordinateOfCell, size);
            xCoordinateOfCell = 0;
            yCoordinateOfCell += 3 * size - thickness;
        }
        if(vertically%2 == 1) {
            drawHexWithoutRightUpper(xCoordinateOfCell, yCoordinateOfCell, size);
            for(int i = 1; i < horizontally; i++) {
                drawHexWithoutLeftRightUpper(xCoordinateOfCell, yCoordinateOfCell, size);
                xCoordinateOfCell += 2 * size - thickness;
            }
            drawHexWithoutLeftUpper(xCoordinateOfCell, yCoordinateOfCell, size);
        } else if(vertically != 2) {
            drawHexWithoutRightUpperLow(xCoordinateOfCell, yCoordinateOfCell, size);
            xCoordinateOfCell += (2 * size - thickness) * (horizontally - 1);
            drawHexWithoutLeftUpperLow(xCoordinateOfCell, yCoordinateOfCell, size);
        }
        //draw two extreme lines
        xCoordinateOfCell = size - thickness/2;
        yCoordinateOfCell = 2 * size - thickness/2;
        for(int i = 0; i < vertically/2; i++) {
            drawBrasenhem(xCoordinateOfCell, yCoordinateOfCell, xCoordinateOfCell,
                    yCoordinateOfCell + size, borderColor);
            xCoordinateOfCell += (2 * size-thickness) * (horizontally - 1);
            drawBrasenhem(xCoordinateOfCell+thickness, yCoordinateOfCell,
                    xCoordinateOfCell+thickness, yCoordinateOfCell + size, borderColor);
            xCoordinateOfCell = size - thickness/2;
            yCoordinateOfCell += 3 * size - thickness;
        }
        //draw pairs of lower lines
        if (vertically % 2 == 0) {
            xCoordinateOfCell = size - thickness/2;
            yCoordinateOfCell = (vertically/2-1)*(2*size + radius) + thickness + 3*radius/2;
            for (int i = 0; i < horizontally - 1; i++) {
                draw2Lines(xCoordinateOfCell, yCoordinateOfCell, size);
                xCoordinateOfCell += 2 * size - thickness;
            }
        }
    }
    private void drawHexesForThickness1() {
        int xCoordinate = 0;
        int yCoordinate = 0;
        for (int i = 0; i < (vertically+1)/2; i++) {
            for (int j = 0; j < horizontally; j++) {
                drawHex(xCoordinate, yCoordinate, radius);
                xCoordinate += 2 * radius;
            }
            xCoordinate = 0;
            yCoordinate += 3 * radius;
        }
        xCoordinate = radius;
        yCoordinate = 2 * radius;
        for (int i = 0; i < vertically/2; i++) {
            for (int j = 0; j < horizontally; j++) {
                drawBrasenhem(xCoordinate, yCoordinate, xCoordinate, yCoordinate + radius, borderColor);
                xCoordinate += 2 * radius;
            }
            xCoordinate = radius;
            yCoordinate += 3 * radius;
        }
        if (vertically % 2 == 0) {
            xCoordinate = radius;
            yCoordinate = (3 * radius * (vertically - 1)) / 2;
            for (int i = 0; i < horizontally - 1; i++) {
                draw2Lines(xCoordinate, yCoordinate, radius);
                xCoordinate += 2 * radius;
            }
        }
    }
    private void drawHexWithoutLeftRightUpper(int x, int y, int size) {
        drawBrasenhem(x + 2 * size, y + 3 * size / 2, x + size, y + 2 * size, borderColor);
        drawBrasenhem(x + size, y + 2 * size, x, y + 3 * size / 2, borderColor);
    }
    private void drawHexWithoutRightUpper(int x, int y, int size) {
        drawBrasenhem(x + 2 * size, y + 3 * size / 2, x + size, y + 2 * size, borderColor);
        drawBrasenhem(x + size, y + 2 * size, x, y + 3 * size / 2, borderColor);
        drawBrasenhem(x, y + 3 * size / 2, x, y + size / 2, borderColor);
        drawBrasenhem(x, y + size / 2, x + size, y, borderColor);
    }
    private void drawHexWithoutLeftUpper(int x, int y, int size) {
        drawBrasenhem(x + size, y, x + 2 * size, y + size / 2, borderColor);
        drawBrasenhem(x + 2 * size, y + size / 2, x + 2 * size, y + 3 * size / 2, borderColor);
        drawBrasenhem(x + 2 * size, y + 3 * size / 2, x + size, y + 2 * size, borderColor);
        drawBrasenhem(x + size, y + 2 * size, x, y + 3 * size / 2, borderColor);
    }
    private void drawHexWithoutLeftUpperLow(int x, int y, int size) {
        drawBrasenhem(x + size, y, x + 2 * size, y + size / 2, borderColor);
        drawBrasenhem(x + 2 * size, y + size / 2, x + 2 * size, y + 3 * size / 2, borderColor);
        drawBrasenhem(x + 2 * size, y + 3 * size / 2, x + size, y + 2 * size, borderColor);
    }
    private void drawHexWithoutRightUpperLow(int x, int y, int size) {
        drawBrasenhem(x + size, y + 2 * size, x, y + 3 * size / 2, borderColor);
        drawBrasenhem(x, y + 3 * size / 2, x, y + size / 2, borderColor);
        drawBrasenhem(x, y + size / 2, x + size, y, borderColor);
    }
    private void drawHexWithoutLeftLow(int x, int y, int size) {
        drawBrasenhem(x + size, y, x + 2 * size, y + size / 2, borderColor);
        drawBrasenhem(x + 2 * size, y + size / 2, x + 2 * size, y + 3 * size / 2, borderColor);
        drawBrasenhem(x + 2 * size, y + 3 * size / 2, x + size, y + 2 * size, borderColor);
        drawBrasenhem(x, y + size / 2, x + size, y, borderColor);
    }
    private void drawHexWithoutRightAndLeftLow(int x, int y, int size) {
        drawBrasenhem(x + size, y, x + 2 * size, y + size / 2, borderColor);
        drawBrasenhem(x, y + size / 2, x + size, y, borderColor);
    }
    private void drawHexWithoutRightLow(int x, int y, int size) {
        drawBrasenhem(x + size, y, x + 2 * size, y + size / 2, borderColor);
        drawBrasenhem(x + size, y + 2 * size, x, y + 3 * size / 2, borderColor);
        drawBrasenhem(x, y + 3 * size / 2, x, y + size / 2, borderColor);
        drawBrasenhem(x, y + size / 2, x + size, y, borderColor);
    }
    private void draw2Lines(int x, int y, int r) {
        drawBrasenhem(x + 2 * r, y + 3 * r / 2, x + r, y + 2 * r, borderColor);
        drawBrasenhem(x + r, y + 2 * r, x, y + 3 * r / 2, borderColor);
    }
    /**
     *
     (x,y)
     .___________
     |/\
     |  |
     |\/
     |
     передается точка .(х,у) - начало координат для каждого шестигранника
     */
    private void drawHex(int x, int y, int size) {
        drawBrasenhem(x + size, y, x + 2 * size, y + size / 2, borderColor);
        drawBrasenhem(x + 2 * size, y + size / 2, x + 2 * size, y + 3 * size / 2, borderColor);
        drawBrasenhem(x + 2 * size, y + 3 * size / 2, x + size, y + 2 * size, borderColor);
        drawBrasenhem(x + size, y + 2 * size, x, y + 3 * size / 2, borderColor);
        drawBrasenhem(x, y + 3 * size / 2, x, y + size / 2, borderColor);
        drawBrasenhem(x, y + size / 2, x + size, y, borderColor);
    }
    public void clear() {
        numberOfAliveCells = 0;
        swapColor();
        for(Cell[] cell : cells) {
            for(Cell cell1 : cell) {
                if(cell1.getLife()) {
                    cell1.setDeath();
                }
                if(image.getRGB(cell1.getX(), cell1.getY()) == fillingColor.getRGB()) {
                    spanFilling(cell1.getX(), cell1.getY(), paintingColor);
                }
                cell1.setImpact(0.0);
            }
        }
        swapColor();
    }
    public int getFieldWidth() {
        return fieldWidth;
    }
    public int getFieldHeight() {
        return fieldHeight;
    }
    public void setXOR() {
        XOR = true;
    }
    public void setReplace() {
        XOR = false;
    }
    private void swapColor() {
        Color tmp = paintingColor;
        paintingColor = changeableColor;
        changeableColor = tmp;
    }
    public void setShowImp(boolean v) {
        showImp = v;
    }
    /**
     * if game is over return true, else return false
     * */
    public boolean step() {
        int numberOfChangedCells = 0;
        for(int i = 0; i < vertically; i++) {
            for(int j = 0; j < horizontally - i%2; j++) {
                if(changedState(i, j)) {
                    numberOfChangedCells ++;
                    if(cells[i][j].getLife()) {
                        spanFilling(cells[i][j].getX(), cells[i][j].getY(), paintingColor);
                    }
                    else {
                        swapColor();
                        spanFilling(cells[i][j].getX(), cells[i][j].getY(), paintingColor);
                        swapColor();
                    }
                }
            }
        }
        resetImpacts();
        return numberOfChangedCells == 0 || numberOfAliveCells == 0;
    }
    private String[] stringsWithoutSpaces(String s) {
        return s.replaceAll(" {4}", " "). replaceAll("[\\s]{2,}", " ").split("//");
    }
    private class WrongValueException extends Exception {
        WrongValueException() {
            super();
        }
    }
    public void load(File file) {
        if(file == null) return;
        try {
            Scanner scanner = new Scanner(file);
            String[] line = stringsWithoutSpaces(scanner.nextLine());
            String[] mn = line[0].split(" ");
            int tmpHorizontally = Integer.parseInt(mn[0]); if(tmpHorizontally < 1) throw new WrongValueException();
            int tmpVertically = Integer.parseInt(mn[1]); if(tmpVertically < 1) throw new WrongValueException();
            line = stringsWithoutSpaces(scanner.nextLine());
            int tmpThickness = Integer.parseInt(line[0].split(" ")[0]); if(tmpThickness < 0) throw new WrongValueException();
            line = stringsWithoutSpaces(scanner.nextLine());
            int tmpRadius = Integer.parseInt(line[0].split(" ")[0]); if(tmpRadius < 7) throw new WrongValueException();
            line = stringsWithoutSpaces(scanner.nextLine());
            int n = Integer.parseInt(line[0].split(" ")[0]);
            ArrayList<Span> pairs = new ArrayList<>(n);
            int x; int y;
            while (n!=0 && scanner.hasNextLine()) {
                n--;
                line = stringsWithoutSpaces(scanner.nextLine());
                String[] c = line[0].split(" ");
                x = Integer.parseInt(c[1]);
                y = Integer.parseInt(c[0]);
                if((x >= 0 && x <=(tmpVertically-1)) && (y >= 0 && y <=(tmpHorizontally - x%2 - 1))) {
                    Span span = new Span(x, 0, y);
                    pairs.add(span);
                } else {
                    System.out.println("Wrong cell");
                    return;
                }
            }
            setParameters(tmpHorizontally, tmpVertically, tmpRadius, tmpThickness);
            updateField();
            for(Span pair : pairs) {
                spanFilling(cells[pair.X1][pair.Y].getX(), cells[pair.X1][pair.Y].getY(), paintingColor);
                cells[pair.X1][pair.Y].setLife();
                numberOfAliveCells++;
            }
            resetImpAndRedraw();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch(NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Wrong format");
        } catch (WrongValueException e) {
            System.out.println("Wrong value");
        }
    }
    public void updateField() {
        setSizes();
        cellsInitialization();
        removeAll();
        drawField();
    }
    public void resetImpAndRedraw() {
        resetImpacts();
        this.getGraphics().drawImage(image, 0, 0, null);
    }
    public boolean isXOR() {
        return XOR;
    }
    private boolean checkSpecialCase(int x, int y) {
        int current = image.getRGB(x, y);
        while (x != fieldWidth && image.getRGB(x, y) != borderColor.getRGB()) {
            x++;
            current= image.getRGB(x, y);
        }
        return current == borderColor.getRGB();
    }
}
