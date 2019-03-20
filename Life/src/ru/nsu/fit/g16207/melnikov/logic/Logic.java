package ru.nsu.fit.g16207.melnikov.logic;
import javax.swing.*;
public class Logic extends JPanel {
    private static final int MAX_UNPAINTABLE_VALUE_OF_THICKNESS = 4;
    protected static int thickness;
    protected static int horizontally;
    protected static int vertically;
    protected static int radius;
    private double LIVE_BEGIN;
    private double LIVE_END;
    private double BIRTH_BEGIN;
    private double BIRTH_END;
    private double FST_IMPACT;
    private double SND_IMPACT;
    protected int numberOfAliveCells = 0;
    protected Cell[][] cells;
    private double newImpact = 0;
    protected Logic() {
        setParameters(10, 10, 24, 6);
        setImpacts(2.0, 3.3,2.3,2.9,1.0,0.3);
        cellsInitialization();
    }
    protected void cellsInitialization() {
        cells = new Cell[vertically][horizontally];
        for(int i = 0; i < vertically; i++) {
            cells[i] = new Cell[horizontally - (i%2)];
            for(int j = 0; j < horizontally - (i%2); j++) {
                cells[i][j] = new Cell();
            }
        }
        //set centres of cells
            //set into odd lines
        int valueToCalculate = thickness <= MAX_UNPAINTABLE_VALUE_OF_THICKNESS ? 0 : thickness;
        int xCoordinateOfCentre = valueToCalculate + radius;
        int yCoordinateOfCentre = valueToCalculate + radius;
        for(int i = 0; i < vertically; i += 2) {
            for(int j = 0; j < horizontally; j++) {
                cells[i][j].setCentre(xCoordinateOfCentre, yCoordinateOfCentre);
                xCoordinateOfCentre+=2 *radius + valueToCalculate;
            }
            xCoordinateOfCentre = valueToCalculate + radius;
            yCoordinateOfCentre+= (2*(valueToCalculate + radius) + radius);
        }
            //set into even lines
        xCoordinateOfCentre = valueToCalculate + 2 * radius +valueToCalculate/2;
        yCoordinateOfCentre = 2*(radius + valueToCalculate) + radius/2;
        for(int i = 1; i < vertically; i += 2) {
            for(int j = 0; j < horizontally-1; j++) {
                cells[i][j].setCentre(xCoordinateOfCentre, yCoordinateOfCentre);
                xCoordinateOfCentre+=2 *radius + valueToCalculate;
            }
            xCoordinateOfCentre = valueToCalculate + 2 * radius +valueToCalculate/2;
            yCoordinateOfCentre+= (2*(valueToCalculate + radius) + radius);
        }
    }
    protected boolean changedState(int line, int column) {
        if(!cellExist(line, column))
            return false;
        Cell cell = cells[line][column];
        if(!cell.getLife() && (cell.getImpact() >= BIRTH_BEGIN && cell.getImpact() <= BIRTH_END)) {
            numberOfAliveCells++;
            cell.setLife();
            return true;
        }
        else if(cell.getLife() && (cell.getImpact() > LIVE_END || cell.getImpact() < LIVE_BEGIN)) {
            numberOfAliveCells--;
            cell.setDeath();
            return true;
        }
        return false;
    }
    protected void resetImpacts() {
        int line = 0; int column = 0;
        for(Cell[] cell: cells) {
            for(Cell cell1 : cell) {
                newImpact = 0;
                changeImpactIfNeed(line, column + 1, FST_IMPACT);
                changeImpactIfNeed(line, column - 1, FST_IMPACT);
                int offset = column + line%2;
                changeImpactIfNeed(line + 1, offset, FST_IMPACT);
                changeImpactIfNeed(line - 1, offset, FST_IMPACT);
                changeImpactIfNeed(line + 1, offset - 1, FST_IMPACT);
                changeImpactIfNeed(line - 1, offset - 1, FST_IMPACT);
                changeImpactIfNeed(line - 2, column, SND_IMPACT);
                changeImpactIfNeed(line + 2, column, SND_IMPACT);
                offset++;
                changeImpactIfNeed(line + 1, offset, SND_IMPACT);
                changeImpactIfNeed(line - 1, offset, SND_IMPACT);
                changeImpactIfNeed(line + 1, offset - 3, SND_IMPACT);
                changeImpactIfNeed(line - 1, offset - 3, SND_IMPACT);
                column++;
                cell1.setImpact(newImpact);
            }
            column = 0;
            line++;
        }
    }
    private void changeImpactIfNeed(int line, int column, double impact) {
        if(needToChangeImpact(line, column)) {
            newImpact+=impact;
        }
    }
    protected void changeState(int line, int column) {
        double sign = 1;
        if(cells[line][column].getLife()) {
            sign = -1;
            cells[line][column].setDeath();
            numberOfAliveCells--;
        } else {
            numberOfAliveCells++;
            cells[line][column].setLife();
        }
        double valueToAdding = FST_IMPACT * sign;
        resetImpactIfCellExist(line, column+1, valueToAdding);
        resetImpactIfCellExist(line, column-1, valueToAdding);
        int offset = column + line%2;
        resetImpactIfCellExist(line + 1, offset, valueToAdding);
        resetImpactIfCellExist(line - 1, offset, valueToAdding);
        resetImpactIfCellExist(line + 1, offset - 1, valueToAdding);
        resetImpactIfCellExist(line - 1, offset - 1, valueToAdding);
        valueToAdding = SND_IMPACT * sign;
        resetImpactIfCellExist(line - 2, column, valueToAdding);
        resetImpactIfCellExist(line + 2, column, valueToAdding);
        offset++;
        resetImpactIfCellExist(line + 1, offset, valueToAdding);
        resetImpactIfCellExist(line - 1, offset, valueToAdding);
        resetImpactIfCellExist(line + 1, offset - 3, valueToAdding);
        resetImpactIfCellExist(line - 1, offset - 3, valueToAdding);
    }
    private void resetImpactIfCellExist(int line, int column, double impact) {
        if(cellExist(line, column)) {
            cells[line][column].changeImpact( impact);
        }
    }
    private boolean needToChangeImpact(int line, int column) {
        return cellExist(line, column) && cells[line][column].getLife();
    }
    private boolean cellExist(int a, int b) {
        return (a >= 0 && a <=(vertically-1)) && (b >= 0 && b <=(horizontally - a%2 - 1));
    }
    public boolean setParameters(int newHorizontally, int newVertically, int newRadius, int newThickness) {
        boolean ret = horizontally == newHorizontally &&
                vertically == newVertically &&
                thickness ==  newThickness &&
                radius == newRadius;
        horizontally = newHorizontally;
        vertically = newVertically;
        thickness =  newThickness <= MAX_UNPAINTABLE_VALUE_OF_THICKNESS ? 0 : newThickness;
        radius = newRadius;
        return ret;
    }
    public Cell[][] getCells() {
        return cells;
    }
    public int getHorizontally() {
        return horizontally;
    }
    public int getVertically() {
        return vertically;
    }
    public int getRadius() {
        return radius;
    }
    public int getThickness() {
        return thickness;
    }
    public int getNumberOfAliveCells() {
        return numberOfAliveCells;
    }
    public void setImpacts(double lb, double le, double bb, double be, double fi, double si) {
        LIVE_BEGIN = lb;
        LIVE_END = le;
        BIRTH_BEGIN = bb;
        BIRTH_END = be;
        FST_IMPACT = fi;
        SND_IMPACT = si;
    }
}
