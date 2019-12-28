package com.company;

import static java.lang.StrictMath.sqrt;

public class Main {

    public static void main(String[] args) {

    Grid gird = new Grid();
    Matrix matrix = new Matrix();
    matrix.matrixOfJacobiego(gird.getElements()[0]);
    }
}
