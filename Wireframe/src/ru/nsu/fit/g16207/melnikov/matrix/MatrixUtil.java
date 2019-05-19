package ru.nsu.fit.g16207.melnikov.matrix;

public class MatrixUtil {
    private MatrixUtil() {

    }

    public static Matrix multiply(Matrix matrix, double value) {
        if (null == matrix) {
            throw new IllegalArgumentException("Matrix can not be null");
        }

        double[][] matrixData = new double[matrix.getHeight()][matrix.getWidth()];

        for (int i = 0; i < matrix.getHeight(); ++i) {
            for (int k = 0; k < matrix.getWidth(); ++k) {
                matrixData[i][k] = matrix.get(i, k) * value;
            }
        }

        return new Matrix(matrixData);
    }

    public static Matrix multiply(Matrix leftMatrix, Matrix rightMatrix) {
        if (null == rightMatrix || null == leftMatrix) {
            throw new IllegalArgumentException("Matrixes can not be null");
        }

        if (leftMatrix.getWidth() != rightMatrix.getHeight()) {
            throw new IllegalArgumentException("These matrixes can not be multiplied");
        }

        double newMatrixData[][] = new double[leftMatrix.getHeight()][rightMatrix.getWidth()];

        for (int i = 0; i < newMatrixData.length; ++i) {
            for (int k = 0; k < newMatrixData[0].length; ++k) {
                newMatrixData[i][k] = 0;
                for (int z = 0; z < leftMatrix.getWidth(); ++z) {
                    newMatrixData[i][k] += leftMatrix.get(i, z) * rightMatrix.get(z, k);
                }
            }
        }

        return new Matrix(newMatrixData);
    }

    public static Matrix plus(Matrix matrix1, Matrix matrix2) {
        if (null == matrix1 || null == matrix2) {
            throw new IllegalArgumentException("Matrixes can not be null");
        }
        if ((matrix1.getWidth() != matrix2.getWidth()) || (matrix1.getHeight() != matrix2.getHeight())) {
            throw new IllegalArgumentException("It's impossible to add the matrix to current matrix. Different sizes");
        }

        double[][] newMatrixData = new double[matrix1.getHeight()][matrix1.getWidth()];

        for (int i = 0; i < matrix1.getHeight(); ++i) {
            for (int k = 0; k < matrix2.getWidth(); ++k) {
                newMatrixData[i][k] = matrix1.get(i, k) + matrix2.get(i, k);
            }
        }

        return new Matrix(newMatrixData);
    }
}