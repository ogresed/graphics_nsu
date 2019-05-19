package ru.nsu.fit.g16207.melnikov.universal_parser;

import java.util.HashMap;

public class MyFactory implements ObjectFactory {
    private final HashMap<Class<?>, TypeMaker> typeMakers = new HashMap<>();

    @Override
    public <T> Object createObject(String string, Class<T> clazz) throws TypeConversionException, ParserException {
        TypeMaker typeMaker = typeMakers.get(clazz);
        if (null == typeMaker) {
            throw new TypeConversionException("There is not appropriate object maker in Factory for the type: "
                    + clazz.getName());
        }

        return typeMaker.create(string);
    }

    public void addTypeMaker(Class<?> clazz, TypeMaker typeMaker) {
        typeMakers.put(clazz, typeMaker);
    }
}
