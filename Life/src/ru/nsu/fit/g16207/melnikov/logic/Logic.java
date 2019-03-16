package ru.nsu.fit.g16207.melnikov.logic;
import javax.swing.*;
public class Logic extends JPanel {
    protected static int horizontally = 8;
    protected static int vertically = 7;
    protected static int thickness = 10;
    protected static int radius = 26;
    private double LIVE_BEGIN = 2.0;
    private double LIVE_END = 3.3;
    private double BIRTH_BEGIN = 2.3;
    private double BIRTH_END = 2.9;
    private double FST_IMPACT = 1.0;
    private double SND_IMPACT = 0.3;
    protected int numberOfAliveCells = 0;
    protected Cell[][] cells;
    protected Logic() {
        makeEvenThickness();
        cellsInitialization();
    }
    public Cell[][] getCells() {
        return cells;
    }
    private void makeEvenThickness() {
        thickness =  thickness + thickness % 2;
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
        int xCoordinateOfCentre = thickness + radius;
        int yCoordinateOfCentre = thickness + radius;
        for(int i = 0; i < vertically; i += 2) {
            for(int j = 0; j < horizontally; j++) {
                cells[i][j].setCentre(xCoordinateOfCentre, yCoordinateOfCentre);
                xCoordinateOfCentre+=2 *radius + thickness;
            }
            xCoordinateOfCentre = thickness + radius;
            yCoordinateOfCentre+= (2*(thickness + radius) + radius);
        }
        //=================================
        xCoordinateOfCentre = thickness + 2 * radius +thickness/2;
        yCoordinateOfCentre = 2*(radius + thickness) + radius/2;
        for(int i = 1; i < vertically; i += 2) {
            for(int j = 0; j < horizontally-1; j++) {
                cells[i][j].setCentre(xCoordinateOfCentre, yCoordinateOfCentre);
                xCoordinateOfCentre+=2 *radius + thickness;
            }
            xCoordinateOfCentre = thickness + 2 * radius +thickness/2;
            yCoordinateOfCentre+= (2*(thickness + radius) + radius);
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
                if(needToChangeImpact(line, column + 1)) {
                    newImpact += FST_IMPACT;
                }
                if(needToChangeImpact(line, column - 1)) {
                    newImpact += FST_IMPACT;
                }

                int offset = column + line%2;
                if(needToChangeImpact(line + 1, offset)) {
                    newImpact += FST_IMPACT;
                }
                if(needToChangeImpact(line - 1, offset)) {
                    newImpact += FST_IMPACT;
                }
                if(needToChangeImpact(line + 1, offset - 1)) {
                    newImpact += FST_IMPACT;
                }
                if(needToChangeImpact(line -  1, offset - 1)) {
                    newImpact += FST_IMPACT;
                }
                if(needToChangeImpact(line - 2, column)) {
                    newImpact += SND_IMPACT;
                }
                if(needToChangeImpact(line + 2, column)) {
                    newImpact += SND_IMPACT;
                }

                offset++;
                if(needToChangeImpact(line +  1, offset)) {
                    newImpact += SND_IMPACT;
                }
                if(needToChangeImpact(line - 1, offset)) {
                    newImpact += SND_IMPACT;
                }
                if(needToChangeImpact(line + 1, offset - 3)) {
                    newImpact += SND_IMPACT;
                }
                if(needToChangeImpact(line - 1, offset-3)) {
                    newImpact += SND_IMPACT;
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
            cells[line][column - 1].changeImpact(FST_IMPACT * sign);
        }

        int offset = column + line%2;
        if(cellExist(line+1,offset)) {
            cells[line + 1][offset].changeImpact(FST_IMPACT * sign);
        }
        if(cellExist(line-1,offset)) {
            cells[line - 1][offset].changeImpact(FST_IMPACT * sign);
        }
        if(cellExist(line+1, offset-1)) {
            cells[line + 1][offset - 1].changeImpact(FST_IMPACT * sign);
        }
        if(cellExist(line-1, offset-1)) {
            cells[line - 1][offset - 1].changeImpact(FST_IMPACT * sign);
        }
        if(cellExist(line-2, column)) {
            cells[line - 2][column].changeImpact(SND_IMPACT * sign);
        }
        if(cellExist(line+2, column)) {
            cells[line + 2][column].changeImpact(SND_IMPACT * sign);
        }

        offset++;
        if(cellExist(line+1, offset)) {
            cells[line+1][offset].changeImpact( SND_IMPACT * sign);
        }
        if(cellExist(line-1, offset)) {
            cells[line - 1][offset].changeImpact(SND_IMPACT * sign);
        }
        if(cellExist(line+1, offset-3)) {
            cells[line + 1][offset - 3].changeImpact(SND_IMPACT * sign);
        }
        if(cellExist(line-1, offset-3)) {
            cells[line-1][offset-3].changeImpact( SND_IMPACT * sign);
        }
    }
    private boolean needToChangeImpact(int line, int column) {
        return cellExist(line, column+1) && cells[line][column+1].getLife();
    }
    private boolean cellExist(int line, int column) {
        return (line >= 0 && line <=(vertically-1)) && (column >= 0 && column <=(horizontally - line%2 - 1));
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
