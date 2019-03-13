package ru.nsu.fit.g16207.melnikov.logic;

import javax.swing.*;

public class Logic extends JPanel {
    protected static int thickness = 1;
    protected static int horizontally = 10;
    protected static int vertically = 10;
    protected static int radius = 24;

    private double LIVE_BEGIN = 2.0;
    private double LIVE_END = 3.3;
    private double BIRTH_BEGIN = 2.3;
    private double BIRTH_END = 2.9;
    private double FST_IMPACT = 1.0;
    private double SND_IMPACT = 0.3;

    protected int numberOfAliveCells = 0;

    protected Cell[][] cells;

    protected Logic() {
        if((thickness+1)/2 == 3)
            radius+=radius%2;
        cellsInitialization();
    }

    public Cell[][] getCells() {
        return cells;
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
        int h = thickness < 5 ? 0 : (thickness + 1) /2;
        int a = h + radius + h;
        int b = h + radius + h;
        for(int i = 0; i < vertically; i += 2) {
            for(int j = 0; j < horizontally; j++) {
                cells[i][j].setCentre(a, b);
                a+=2 *(radius + h);
            }
            a = h + radius + h;
            b+=3* radius + 3*h;
        }
        a = 2*(h + radius)+ h;
        b = 2*(h + radius) + radius /2+ h;
        for(int i = 1; i < vertically; i += 2) {
            for(int j = 0; j < horizontally-1; j++) {
                cells[i][j].setCentre(a, b);
                a+=2 *(radius + h);
            }
            a = 2*(h + radius)+ h;
            b+=3* radius + 3*h;
        }
    }

    protected boolean changedState(int a, int b) {
        if(!cellExist(a, b))
            return false;
        Cell cell = cells[a][b];
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
        int a = 0; int b = 0;
        for(Cell[] cell: cells) {
            for(Cell cell1 : cell) {
                double imp = 0;
                if(cellExist(a, b+1)) if(cells[a][b+1].getLife()) imp+=FST_IMPACT;
                if(cellExist(a, b-1)) if(cells[a][b-1].getLife()) imp+=FST_IMPACT;
                int rn = b + a%2;
                if(cellExist(a+1, rn)) if(cells[a+1][rn].getLife()) imp+=FST_IMPACT;
                if(cellExist(a-1, rn)) if(cells[a-1][rn].getLife()) imp+=FST_IMPACT;
                if(cellExist(a+1, rn-1)) if(cells[a+1][rn-1].getLife()) imp+=FST_IMPACT;
                if(cellExist(a-1, rn-1)) if(cells[a-1][rn-1].getLife()) imp+=FST_IMPACT;

                if(cellExist(a-2, b)) if(cells[a-2][b].getLife()) imp+=SND_IMPACT;
                if(cellExist(a+2, b)) if(cells[a+2][b].getLife()) imp+=SND_IMPACT;
                rn++;
                if(cellExist(a+1, rn)) if(cells[a+1][rn].getLife()) imp+=SND_IMPACT;
                if(cellExist(a-1, rn)) if(cells[a-1][rn].getLife()) imp+=SND_IMPACT;
                if(cellExist(a+1, rn-3)) if(cells[a+1][rn-3].getLife()) imp+=SND_IMPACT;
                if(cellExist(a-1, rn-3)) if(cells[a-1][rn-3].getLife()) imp+=SND_IMPACT;
                b++;
                cell1.setImpact(imp);
            }
            b = 0;
            a++;
        }
    }
    protected void changeState(int a, int b) {
        double sign = 1;
        if(cells[a][b].getLife()) {
            sign = -1;
            cells[a][b].setDeath();
            numberOfAliveCells--;
        } else {
            numberOfAliveCells++;
            cells[a][b].setLife();
        }
        if(cellExist(a, b+1)) cells[a][b+1].changeImpact(FST_IMPACT * sign);
        if(cellExist(a, b-1)) cells[a][b-1].changeImpact( FST_IMPACT * sign);
        int rn = b + a%2;
        if(cellExist(a+1,rn)) cells[a+1][rn].changeImpact( FST_IMPACT * sign);
        if(cellExist(a-1,rn)) cells[a-1][rn].changeImpact( FST_IMPACT * sign);
        if(cellExist(a+1, rn-1)) cells[a+1][rn-1].changeImpact( FST_IMPACT * sign);
        if(cellExist(a-1, rn-1)) cells[a-1][rn-1].changeImpact( FST_IMPACT * sign);
        if(cellExist(a-2, b)) cells[a-2][b].changeImpact( SND_IMPACT * sign);
        if(cellExist(a+2, b)) cells[a+2][b].changeImpact( SND_IMPACT * sign);
        rn++;
        if(cellExist(a+1, rn)) cells[a+1][rn].changeImpact( SND_IMPACT * sign);
        if(cellExist(a-1, rn)) cells[a-1][rn].changeImpact( SND_IMPACT * sign);
        if(cellExist(a+1, rn-3)) cells[a+1][rn-3].changeImpact( SND_IMPACT * sign);
        if(cellExist(a-1, rn-3)) cells[a-1][rn-3].changeImpact( SND_IMPACT * sign);
    }

    private boolean cellExist(int a, int b) {
        return (a >= 0 && a <=(vertically-1)) && (b >= 0 && b <=(horizontally - a%2 - 1));
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
