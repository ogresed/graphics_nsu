package ru.nsu.fit.g16207.melnikov.segment;

import ru.nsu.fit.g16207.melnikov.function.GridFunction;

public class Lines {
    private Segment verticalLines[][];
    private Segment horizontalLines[][];

    public Lines(GridFunction function) {
        int numberVertical = function.getNumberVerticalDotes();
        int numberHorizontal = function.getNumberHorizontalDotes();
        verticalLines = new Segment[numberHorizontal][numberVertical-1];
        horizontalLines = new Segment[numberVertical][numberHorizontal-1];
        double values[][] = function.getValues();
        double x = function.getLeftBorder();
        double y = function.getLowerBorder();
        double xOffset = function.getOffsetOfHorizontal();
        double yOffset = function.getOffsetOfVertical();
        for(int i = 0; i < numberVertical; i++) {
            for(int j = 0; j < numberHorizontal; j++) {
                if(i != numberHorizontal -1)
                verticalLines[j][i] = new Segment(x, x, y, y + yOffset,values[i][j], values[i+1][j]);
                if(j != numberVertical -1)
                horizontalLines[i][j] = new Segment(x, x + xOffset, y, y, values[i][j], values[i][j+1]);
                x+=xOffset;
            }
            x = function.getOffsetOfHorizontal();
            y+=yOffset;
        }
    }
    public Segment[][] getVerticalLines() {
        return verticalLines;
    }
    public Segment[][] getHorizontalLines() {
        return horizontalLines;
    }
}
