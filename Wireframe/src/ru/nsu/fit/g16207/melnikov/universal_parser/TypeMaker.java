package ru.nsu.fit.g16207.melnikov.universal_parser;

public interface TypeMaker {
    Object create(String string) throws TypeConversionException, ParserException;
}
