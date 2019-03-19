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
                double newImpact = 0;
                if(needToChangeImpact(line,column + 1)) {
                    newImpact+=FST_IMPACT;
                }
                if(needToChangeImpact(line,column - 1)) {
                    newImpact+=FST_IMPACT;
                }
                int offset = column + line%2;
                if(needToChangeImpact(line+1,offset)) {
                    newImpact+=FST_IMPACT;
                }
                if(needToChangeImpact(line-1,offset)) {
                    newImpact+=FST_IMPACT;
                }
                if(needToChangeImpact(line + 1, offset - 1)) {
                    newImpact+=FST_IMPACT;
                }
                if(needToChangeImpact(line - 1, offset - 1)) {
                    newImpact+=FST_IMPACT;
                }
                if(needToChangeImpact(line - 2, column)) {
                    newImpact+=SND_IMPACT;
                }
                if(needToChangeImpact(line + 2, column)) {
                    newImpact+=SND_IMPACT;
                }
                offset++;
                if(needToChangeImpact(line + 1, offset)) {
                    newImpact+=SND_IMPACT;
                }
                if(needToChangeImpact(line - 1, offset)) {
                    newImpact+=SND_IMPACT;
                }
                if(needToChangeImpact(line + 1, offset - 3)) {
                    newImpact+=SND_IMPACT;
                }
                if(needToChangeImpact(line - 1, offset - 3)) {
                    newImpact+=SND_IMPACT;
                }
                column++;
                cell1.setImpact(newImpact);
            }
            column = 0;
            line++;
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
        if(cellExist(line, column+1)) {
            cells[line][column+1].changeImpact(FST_IMPACT * sign);
        }
        if(cellExist(line, column-1)) {
            cells[line][column-1].changeImpact( FST_IMPACT * sign);
        }
        int offset = column + line%2;
        if(cellExist(line+1,offset)) {
            cells[line+1][offset].changeImpact( FST_IMPACT * sign);
        }
        if(cellExist(line-1,offset)) {
            cells[line-1][offset].changeImpact( FST_IMPACT * sign);
        }
        if(cellExist(line+1, offset-1)) {
            cells[line+1][offset-1].changeImpact( FST_IMPACT * sign);
        }
        if(cellExist(line-1, offset-1)) {
            cells[line-1][offset-1].changeImpact( FST_IMPACT * sign);
        }
        if(cellExist(line-2, column)) {
            cells[line-2][column].changeImpact( SND_IMPACT * sign);
        }
        if(cellExist(line+2, column)) {
            cells[line+2][column].changeImpact( SND_IMPACT * sign);
        }
        offset++;
        if(cellExist(line+1, offset)) {
            cells[line+1][offset].changeImpact( SND_IMPACT * sign);
        }
        if(cellExist(line-1, offset)) {
            cells[line-1][offset].changeImpact( SND_IMPACT * sign);
        }
        if(cellExist(line+1, offset-3)) {
            cells[line+1][offset-3].changeImpact( SND_IMPACT * sign);
        }
        if(cellExist(line-1, offset-3)) {
            cells[line-1][offset-3].changeImpact( SND_IMPACT * sign);
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
