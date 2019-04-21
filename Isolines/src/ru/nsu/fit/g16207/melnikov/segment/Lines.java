package ru.nsu.fit.g16207.melnikov.segment;

import ru.nsu.fit.g16207.melnikov.function.GridFunction;

public class Lines {
    private Segment verticalSegments[][];
    private Segment horizontalSegments[][];

    public Lines(GridFunction function) {
        int numberVertical = function.getNumberVerticalDotes();
        int numberHorizontal = function.getNumberHorizontalDotes();
        verticalSegments = new Segment[numberHorizontal][numberVertical-1];
        horizontalSegments = new Segment[numberVertical][numberHorizontal-1];
        double values[][] = function.getValues();
        double x = function.getLeftBorder();
        double y = function.getLowerBorder();
        double xOffset = function.getOffsetOfHorizontal();
        double yOffset = function.getOffsetOfVertical();
        for(int i = 0; i < numberVertical; i++) {
            for(int j = 0; j < numberHorizontal - 1; j++) {
                    horizontalSegments[i][j] = new Segment(x, x + xOffset, y, y, values[i][j], values[i][j + 1]);
                x+=xOffset;
            }
            x = function.getLeftBorder();
            y+=yOffset;
        }
        x = function.getLeftBorder();
        y = function.getLowerBorder();
        for(int i = 0; i < numberHorizontal; i++) {
            for(int j = 0; j < numberVertical - 1; j++) {
                verticalSegments[i][j] = new Segment(x, x, y, y + yOffset, values[j][i], values[j + 1][i]);
                y+=yOffset;
            }
            y = function.getLowerBorder();
            x+=xOffset;
        }
    }
    public Segment[][] getVerticalLines() {
        return verticalSegments;
    }
    public Segment[][] getHorizontalLines() {
        return horizontalSegments;
    }
}
