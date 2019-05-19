package ru.nsu.fit.g16207.melnikov.universal_parser;

public interface ObjectFactory {
    <T> Object createObject(String string, Class<T> clazz) throws TypeConversionException, ParserException;
}
