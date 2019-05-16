package ru.nsu.fit.g16207.melnikov.pair;

public class Pair<Type> {
    private Type first;
    private Type second;

    public Pair(Type first, Type second) {
        this.first = first;
        this.second = second;
    }

    public Type getFirst() {
        return first;
    }

    public void setFirst(Type first) {
        this.first = first;
    }

    public Type getSecond() {
        return second;
    }

    public void setSecond(Type second) {
        this.second = second;
    }
}
