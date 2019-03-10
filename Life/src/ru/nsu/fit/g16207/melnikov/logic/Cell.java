package ru.nsu.fit.g16207.melnikov.logic;

public class Cell {
    private boolean life = false;
    int x, y;
    double IMPACT = 0.0;

    public Cell() {

    }

    public void setCentre(int X, int Y) {
        x = X;
        y = Y;
    }

    public boolean getLife() {
        return life;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setLife() {
        life = true;
    }

    public void setDeath() {
        life = false;
    }

    public boolean isLife() {
        return life;
    }

    public void setImpact(double v) {
        IMPACT = v;
    }

    public double getImpact() {
        return IMPACT;
    }

    public void changeImpact(double v) {
        IMPACT+=v;
    }
}