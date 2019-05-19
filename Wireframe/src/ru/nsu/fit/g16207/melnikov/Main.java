package ru.nsu.fit.g16207.melnikov;

import ru.nsu.fit.g16207.melnikov.mf.MainFrame;
import ru.nsu.fit.g16207.melnikov.universal_parser.NoObjectFactoryException;
import ru.nsu.fit.g16207.melnikov.universal_parser.ParserException;
import ru.nsu.fit.g16207.melnikov.universal_parser.TypeConversionException;
import ru.nsu.fit.g16207.melnikov.universal_parser.TypeMatchingException;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        try {
            new MainFrame().loadModel(new File("FIT_16207_Melnikov_Wireframe_Data/conf.txt"));
        } catch (NoObjectFactoryException | TypeConversionException | ParserException | TypeMatchingException e) {
            System.out.println("Open file error");
        }
    }
}
