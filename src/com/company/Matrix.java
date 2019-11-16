package com.company;

public class Matrix {

    double E = (1/Math.sqrt(3));
    double n = (1/Math.sqrt(3));
    double weight1 = 1;
    double weight2 = 1;

    UniversalElement pc_1 = new UniversalElement(E,n,weight1,weight2);
    UniversalElement pc_2 = new UniversalElement(E,-n,weight1,weight2);
    UniversalElement pc_3 = new UniversalElement(-E,n,weight1,weight2);
    UniversalElement pc_4 = new UniversalElement(-E,-n,weight1,weight2);

    LocalCoordinateSystem eu = new LocalCoordinateSystem(new UniversalElement[]{pc_1,pc_2,pc_3,pc_4});


    public void shapeFunctionsElement2D( ){

        double[][] matrixOfShapeFunctions = new double[4][4];
        System.out.println("\nMatrix Of Shape Functions\n");
        for(int i=0; i < eu.localCoordinateSystem.length ;i++){
            matrixOfShapeFunctions[i] = new double[]{
                    eu.localCoordinateSystem[i].shapeFunctions[0],
                    eu.localCoordinateSystem[i].shapeFunctions[1],
                    eu.localCoordinateSystem[i].shapeFunctions[2],
                    eu.localCoordinateSystem[i].shapeFunctions[3]};
        }
        for(int i=0; i<4 ;i++){
            for(int j=0; j<4;j++){
                System.out.println(matrixOfShapeFunctions[i][j]);
            }
        }
    }


    public void matrixDerivativesAfterXi(){
        double[][] matrixDerivativesAfterXi = new double[4][4];
        System.out.println("\nMatrix Derivatives After Xi\n");
        for(int i=0; i < eu.localCoordinateSystem.length ;i++){
            matrixDerivativesAfterXi[i] = new double[]{
                    eu.localCoordinateSystem[i].derivativesAfterXi[0],
                    eu.localCoordinateSystem[i].derivativesAfterXi[1],
                    eu.localCoordinateSystem[i].derivativesAfterXi[2],
                    eu.localCoordinateSystem[i].derivativesAfterXi[3]};
        }
        for(int i=0; i<4 ;i++){
            for(int j=0; j<4;j++){
                System.out.println(matrixDerivativesAfterXi[i][j]);
            }
        }
    }

    public void matrixDerivativesAfterEta(){
        double[][] matrixDerivativesAfterEta = new double[4][4];
        System.out.println("\nMatrix Derivatives After Eta\n");
        for(int i=0; i < eu.localCoordinateSystem.length ;i++){
            matrixDerivativesAfterEta[i] = new double[]{
                    eu.localCoordinateSystem[i].derivativesAfterEta[0],
                    eu.localCoordinateSystem[i].derivativesAfterEta[1],
                    eu.localCoordinateSystem[i].derivativesAfterEta[2],
                    eu.localCoordinateSystem[i].derivativesAfterEta[3]};
        }
        for(int i=0; i<4 ;i++){
            for(int j=0; j<4;j++){
                System.out.println(matrixDerivativesAfterEta[i][j]);
            }
        }
    }


}
