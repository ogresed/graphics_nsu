package ru.nsu.fit.g16207.melnikov;

public class Line<T> {
    private final T start;
    private final T end;

    public Line(T start, T end) {
        this.start = start;
        this.end = end;
    }

    public T getStart() {
        return start;
    }

    public T getEnd() {
        return end;
    }
}
