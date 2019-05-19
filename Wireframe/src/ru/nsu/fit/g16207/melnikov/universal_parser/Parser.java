package ru.nsu.fit.g16207.melnikov.universal_parser;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Predicate;

public class Parser {
    public Parser(File file, ParserConfig parserConfig,
                  List<Integer> endPoints, Predicate<String> stringFilter) throws ParserException, TypeConversionException, TypeMatchingException {
        try (BufferedReader bufferedReader =
                     new BufferedReader(
                             new InputStreamReader(
                                     new FileInputStream(file), Charset.forName("UTF-8")))) {
            String nextString;
            while ((nextString = bufferedReader.readLine()) != null) {
                if (stringFilter.test(nextString)) {
                    parserConfig.execute(nextString);
                }
            }
        } catch (IOException e) {
            throw new ParserException(e.getMessage(), e);
        }

        for (Integer endPoint : endPoints) {
            if (endPoint != parserConfig.getCurrentRunnableIndex()) {
                throw new ParserException("At the end of working the parser is in not finish state");
            }
        }
    }
}
