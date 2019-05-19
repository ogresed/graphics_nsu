package ru.nsu.fit.g16207.melnikov.universal_parser;

import java.util.ArrayList;

public class ParserConfigBuilder {
    private final ArrayList<TypeCheckRunnable> runnables = new ArrayList<>();
    private MyFactory objectFactory;

    public ParserConfigBuilder() {

    }

    public ParserConfigBuilder add(TypeCheckRunnable typeCheckRunnable) {
        runnables.add(typeCheckRunnable);
        return this;
    }

    public ParserConfigBuilder with(MyFactory myFactory) {
        this.objectFactory = myFactory;
        return this;
    }

    public ParserConfig build() throws NoObjectFactoryException {
        if (null == objectFactory) {
            throw new NoObjectFactoryException("You have to pass object factory before building ParserConfig");
        }

        return new ParserConfig(runnables, objectFactory);
    }
}
