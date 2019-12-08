package com.company;

public class Main {

    public static void main(String[] args) {

    Grid gird = new Grid();
    Matrix matrix = new Matrix();
    matrix.shapeFunctionsElement2D();
    matrix.matrixDerivativesAfterEta();
    matrix.matrixDerivativesAfterXi();
    matrix.matrixOfJacobiego(gird.getElements()[0]);
    matrix.matrixOfJacobiego(gird.getElements()[1]);
    matrix.matrixOfJacobiego(gird.getElements()[2]);
    matrix.matrixOfJacobiego(gird.getElements()[3]);
    }

}
