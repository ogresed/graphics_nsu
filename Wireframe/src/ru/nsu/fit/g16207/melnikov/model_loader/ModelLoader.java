package ru.nsu.fit.g16207.melnikov.model_loader;

import ru.nsu.fit.g16207.melnikov.bspline.BSplineBuilder;
import ru.nsu.fit.g16207.melnikov.ListUtil;
import ru.nsu.fit.g16207.melnikov.Model;
import ru.nsu.fit.g16207.melnikov.ShapeBuildingException;
import ru.nsu.fit.g16207.melnikov.matrix.Matrix;
import ru.nsu.fit.g16207.melnikov.universal_parser.*;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class ModelLoader {
    private static class IntegerWrapper {
        private int integer;
        IntegerWrapper(int integer) {
            this.integer = integer;
        }
    }

    private final Model model = new Model();

    public ModelLoader(File file) throws TypeConversionException, NoObjectFactoryException, TypeMatchingException, ParserException {
        Matrix matrix = new Matrix(new double[4][4]);
        model.setRoundMatrix(matrix);
        final IntegerWrapper rowCountForMatrix = new IntegerWrapper(0);
        final IntegerWrapper countOfShapes = new IntegerWrapper(0);

        MyFactory objectFactory = new MyFactory();
        objectFactory.addTypeMaker(Integer.class, Integer::parseInt);
        objectFactory.addTypeMaker(Double.class, Double::parseDouble);
        objectFactory.addTypeMaker(Comment.class, (string) -> {
            if (!string.substring(0, 2).equals("//")) {
                throw new ParserException("Invalid comment");
            }

            return null;
        });

        ParserConfigBuilder parserConfigBuilder = new ParserConfigBuilder()
                .with(objectFactory)
                .add(new TypeCheckRunnable(ListUtil.asList(Integer.class,
                        Integer.class, Integer.class, Double.class, Double.class, Integer.class,
                        Integer.class, Comment.class)) {
                    @Override
                    public void run(Object[] objects, ParserConfig parserConfig) {
                        model.setN((Integer) objects[0]);
                        model.setM((Integer) objects[1]);
                        model.setK((Integer) objects[2]);
                        model.setA((Double) objects[3]);
                        model.setB((Double) objects[4]);
                        model.setC((Integer) objects[5]);
                        model.setD((Integer) objects[6]);
                        parserConfig.nextIndex();
                    }
                })
                .add(new TypeCheckRunnable(ListUtil.asList(Double.class,
                        Double.class, Double.class, Double.class, Comment.class)) {
                    @Override
                    public void run(Object[] objects, ParserConfig parserConfig) {
                        model.setZn((Double) objects[0]);
                        model.setZf((Double) objects[1]);
                        model.setSw((Double) objects[2]);
                        model.setSh((Double) objects[3]);
                        parserConfig.nextIndex();
                    }
                })
                .add(new TypeCheckRunnable(ListUtil.asList(Double.class, Double.class, Double.class,
                        Comment.class)) {
                    @Override
                    public void run(Object[] objects, ParserConfig parserConfig) {
                        for (int k = 0; k < 3; ++k) {
                            matrix.add(rowCountForMatrix.integer, k, (double) objects[k]);
                        }

                        matrix.add(0, 3, 0);
                        matrix.add(1, 3, 0);
                        matrix.add(2, 3, 0);
                        matrix.add(3, 0, 0);
                        matrix.add(3, 1, 0);
                        matrix.add(3, 2, 0);
                        matrix.add(3, 3, 1);

                        if (++rowCountForMatrix.integer > 2) {
                            parserConfig.nextIndex();
                        }
                    }
                })
                .add(new TypeCheckRunnable(ListUtil.asList(Integer.class, Integer.class, Integer.class,
                        Comment.class)) {
                    @Override
                    public void run(Object[] objects, ParserConfig parserConfig) {
                        model.setBackgroundColor(
                                new Color((Integer) objects[0],
                                        (Integer) objects[1],
                                        (Integer) objects[2]));
                        parserConfig.nextIndex();
                    }
                })
                .add(new TypeCheckRunnable(ListUtil.asList(Integer.class, Comment.class)) {
                    @Override
                    public void run(Object[] objects, ParserConfig parserConfig) {
                        countOfShapes.integer = (Integer) objects[0];
                        parserConfig.nextIndex();
                    }
                });

        final BSplineBuilder[] bSplineBuilders = {new BSplineBuilder()};
            IntegerWrapper rowCounterForMatrix = new IntegerWrapper(0);
            ArrayList<Matrix> roundMatrix = new ArrayList<>();
            roundMatrix.add(new Matrix(new double[4][4]));
            bSplineBuilders[0].withRoundMatrix(roundMatrix.get(0));
            IntegerWrapper countOfPoints = new IntegerWrapper(0);
            IntegerWrapper currentParserRunnableIndex = new IntegerWrapper(0);
            IntegerWrapper currentShape = new IntegerWrapper(0);

            parserConfigBuilder
                    .add(new TypeCheckRunnable(ListUtil.asList(Integer.class, Integer.class, Integer.class,
                            Comment.class)) {
                        @Override
                        public void run(Object[] objects, ParserConfig parserConfig) {
                            bSplineBuilders[0].withColor(new Color((Integer) objects[0],
                                    (Integer) objects[1],
                                    (Integer) objects[2]));
                            currentParserRunnableIndex.integer = parserConfig.getCurrentRunnableIndex();
                            parserConfig.nextIndex();
                        }
                    })
                    .add(new TypeCheckRunnable(ListUtil.asList(Double.class, Double.class, Double.class,
                            Comment.class)) {
                        @Override
                        public void run(Object[] objects, ParserConfig parserConfig) {
                            bSplineBuilders[0]
                                    .withCx((Double) objects[0])
                                    .withCy((Double) objects[1])
                                    .withCz((Double) objects[2]);

                            parserConfig.nextIndex();
                        }
                    })
                    .add(new TypeCheckRunnable(ListUtil.asList(Double.class, Double.class, Double.class,
                            Comment.class)) {
                        @Override
                        public void run(Object[] objects, ParserConfig parserConfig) {
                            for (int k = 0; k < 3; ++k) {
                                roundMatrix.get(0).add(rowCounterForMatrix.integer, k, (Double) objects[k]);
                            }

                            roundMatrix.get(0).add(0, 3, 0);
                            roundMatrix.get(0).add(1, 3, 0);
                            roundMatrix.get(0).add(2, 3, 0);
                            roundMatrix.get(0).add(3, 0, 0);
                            roundMatrix.get(0).add(3, 1, 0);
                            roundMatrix.get(0).add(3, 2, 0);
                            roundMatrix.get(0).add(3, 3, 1);

                            if (++rowCounterForMatrix.integer > 2) {
                                parserConfig.nextIndex();
                            }
                        }
                    })
                    .add(new TypeCheckRunnable(ListUtil.asList(Integer.class, Comment.class)) {
                        @Override
                        public void run(Object[] objects, ParserConfig parserConfig) {
                            countOfPoints.integer = (Integer) objects[0];
                            parserConfig.nextIndex();
                        }
                    });


        final IntegerWrapper[] currentPoint = {new IntegerWrapper(0)};
        parserConfigBuilder.add(new TypeCheckRunnable(ListUtil.asList(Double.class, Double.class,
                Comment.class)) {
            @Override
            public void run(Object[] objects, ParserConfig parserConfig) {
                bSplineBuilders[0].addPoint((Double) objects[0], (Double) objects[1]);
                if (++currentPoint[0].integer >= countOfPoints.integer) {
                    try {
                        model.addShape(bSplineBuilders[0].build());
                    } catch (ShapeBuildingException e) {
                        System.out.println("BSpline was not added.");
                    }
                    ++currentShape.integer;
                    rowCounterForMatrix.integer = 0;
                    currentPoint[0].integer = 0;
                    parserConfig.setCurrentRunnableIndex(currentParserRunnableIndex.integer);
                    bSplineBuilders[0] = new BSplineBuilder();
                    roundMatrix.set(0, new Matrix(new double[4][4]));
                    bSplineBuilders[0].withRoundMatrix(roundMatrix.get(0));
                }
            }
        });

        new Parser(file, parserConfigBuilder.build(), ListUtil.asList(5), s -> {
            if (s.isEmpty()) {
                return false;
            }

            String trimmedString = s.trim();

            if (trimmedString.length() > 2) {
                return !trimmedString.substring(0, 2).equals("//");
            }

            return true;
        });

    }

    public Model getModel() {
        return model;
    }
}
