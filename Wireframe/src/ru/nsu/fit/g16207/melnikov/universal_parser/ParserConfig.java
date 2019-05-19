package ru.nsu.fit.g16207.melnikov.universal_parser;

import java.util.ArrayList;

public class ParserConfig {
    private final ArrayList<TypeCheckRunnable> runnables;
    private final MyFactory objectFactory;
    private int currentRunnableIndex = 0;
    private String splitRegex = " ";

    public ParserConfig(ArrayList<TypeCheckRunnable> runnables, MyFactory objectFactory) {
        this.runnables = runnables;
        this.objectFactory = objectFactory;
    }

    public void setSplitRegex(String splitRegex) {
        this.splitRegex = splitRegex;
    }

    void execute(String string) throws TypeConversionException, TypeMatchingException, ParserException {
        if (currentRunnableIndex >= runnables.size()) {
            throw new ParserException("Can't choose runnable by offered index!");
        }

        runnables.get(currentRunnableIndex).run(string.split(splitRegex), this, objectFactory);
    }

    public void nextIndex() {
        this.currentRunnableIndex++;
    }

    public void setCurrentRunnableIndex(int currentRunnableIndex) {
        this.currentRunnableIndex = currentRunnableIndex;
    }

    public int getCurrentRunnableIndex() {
        return currentRunnableIndex;
    }
}
