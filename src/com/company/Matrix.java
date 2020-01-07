package com.company;

import org.w3c.dom.ls.LSOutput;

import java.util.Arrays;

public class Matrix {
    //private double E = (1/Math.sqrt(3));
    private double E = (1/Math.sqrt(3));
    private double n = (1/Math.sqrt(3));
    private double weight1 = 1;
    private double weight2 = 1;
    //CONDUCTIVITY
    private double K = 25;
    //SPECIFIC HEAT
    private double c = 700;
    //DENSITY
    private double ro = 7800;
    private double alfa = 300;
    private double initialTemperature = 1200;

    private UniversalElement[] localCoordinate = new UniversalElement[]{
            new UniversalElement(-E,-n,weight1,weight2),
            new UniversalElement(E,-n,weight1,weight2),
            new UniversalElement(E,n,weight1,weight2),
            new UniversalElement(-E,n,weight1,weight2)
    };


    public double[][] shapeFunctionsElement2D(){

        double[][] matrixOfShapeFunctions = new double[4][4];

        for(int i=0; i < localCoordinate.length ;i++){
            matrixOfShapeFunctions[i] = new double[]{
                    localCoordinate[i].shapeFunctions[0],
                    localCoordinate[i].shapeFunctions[1],
                    localCoordinate[i].shapeFunctions[2],
                    localCoordinate[i].shapeFunctions[3]};
        }
        return matrixOfShapeFunctions;
    }


    public double [][] matrixDerivativesAfterXi(){

        double[][] matrixDerivativesAfterXi = new double[4][4];

        for(int i=0; i < localCoordinate.length ;i++){
            matrixDerivativesAfterXi[i] = new double[]{
                    localCoordinate[i].derivativesAfterXi[0],
                    localCoordinate[i].derivativesAfterXi[1],
                    localCoordinate[i].derivativesAfterXi[2],
                    localCoordinate[i].derivativesAfterXi[3]};
        }
        return matrixDerivativesAfterXi;
    }

    public double[][] matrixDerivativesAfterEta(){

        double[][] matrixDerivativesAfterEta = new double[4][4];

        for(int i=0; i < localCoordinate.length ;i++){
            matrixDerivativesAfterEta[i] = new double[]{
                    localCoordinate[i].derivativesAfterEta[0],
                    localCoordinate[i].derivativesAfterEta[1],
                    localCoordinate[i].derivativesAfterEta[2],
                    localCoordinate[i].derivativesAfterEta[3]};
        }
        return matrixDerivativesAfterEta;
    }

    public double[][] matrixOfJacobiego(Element element){

        double[][] matrixOfElementJacobian = new double[4][4];
        double[] dx_dxi = new double[4];
        double[] dx_dn = new double[4];
        double[] dy_dxi = new double[4];
        double[] dy_dn = new double[4];

        for(int i=0;i<localCoordinate.length;i++){
            for(int j=0; j<localCoordinate.length;j++){
                dx_dxi[i] += matrixDerivativesAfterXi()[i][j] * element.Nodes[j].getX();
                dx_dn[i] +=  matrixDerivativesAfterEta()[i][j] * element.Nodes[j].getX();
                dy_dxi[i] += matrixDerivativesAfterXi()[i][j] * element.Nodes[j].getY();
                dy_dn[i] += matrixDerivativesAfterEta()[i][j] * element.Nodes[j].getY();
            }
            matrixOfElementJacobian[0][i] = dx_dxi[i];
            matrixOfElementJacobian[1][i] = dx_dn[i];
            matrixOfElementJacobian[2][i] = dy_dxi[i];
            matrixOfElementJacobian[3][i] = dy_dn[i];
        }
        return matrixOfElementJacobian;
    }

    public double[] detJacobi(Element element){

        double [] matrixDetJacobi = new double[localCoordinate.length];

        for(int j = 0; j<localCoordinate.length; j++){
            matrixDetJacobi[j] = (matrixOfJacobiego(element)[0][j] * matrixOfJacobiego(element)[3][j]) - (matrixOfJacobiego(element)[2][j] * matrixOfJacobiego(element)[1][j]);
        }
        return matrixDetJacobi;
    }


    public double[][] dNafterDx(Element element){

        double [][] Matrix_dNAfterdx = new double[localCoordinate.length][localCoordinate.length];

        for(int i=0; i<localCoordinate.length;i++){
            for(int j=0; j<localCoordinate.length;j++){
                Matrix_dNAfterdx[i][j] = (1/detJacobi(element)[i]) * ( (matrixOfJacobiego(element)[3][i] * matrixDerivativesAfterXi()[i][j]) - (matrixOfJacobiego(element)[2][i] * matrixDerivativesAfterEta()[i][j]));
            }
        }
        return Matrix_dNAfterdx;
    }

    public double[][] dNafterDy(Element element){

        double [][] Matrix_dNAfterdy = new double[localCoordinate.length][localCoordinate.length];

        for(int i=0; i<localCoordinate.length;i++){
            for(int j=0; j<localCoordinate.length;j++){
                Matrix_dNAfterdy[i][j] = (1/detJacobi(element)[i]) * ( (matrixOfJacobiego(element)[0][i] * matrixDerivativesAfterEta()[i][j]) - (matrixOfJacobiego(element)[1][i] *  matrixDerivativesAfterXi()[i][j]));

            }
        }
        return Matrix_dNAfterdy;
    }

    public double [][] matrixH(Element element){

        double [][][] eleMatrixH = new double [4][4][4];
        double [][] matrixH = new double[localCoordinate.length][localCoordinate.length];

        for(int z=0; z<localCoordinate.length;z++) {
            for(int i = 0; i < localCoordinate.length; i++){
                for(int j = 0; j < localCoordinate.length; j++){
                    eleMatrixH[z][i][j] = K*(dNafterDx(element)[z][i]*dNafterDx(element)[z][j] + dNafterDy(element)[z][i]*dNafterDy(element)[z][j])*detJacobi(element)[z];
                    matrixH[i][j] += eleMatrixH[z][i][j];
                }
            }
        }
        return matrixH;
    }

    public double [][] matrixC(Element element){

        double [][][] eleMatrixC = new double [4][4][4];
        double [][] matrixC = new double[4][4];
        for(int z=0; z<localCoordinate.length;z++) {
            for(int i = 0; i < localCoordinate.length; i++){
                for(int j = 0; j < localCoordinate.length; j++){
                    eleMatrixC[z][i][j] = c*ro*(shapeFunctionsElement2D()[z][i]*shapeFunctionsElement2D()[z][j])*detJacobi(element)[z];
                    matrixC[i][j] += eleMatrixC[z][i][j];
                }
            }
        }
        return matrixC;
    }

    public double [][][] elementSurface(Element element){
        double [][][] surfaces = new double[localCoordinate.length][2][localCoordinate.length];
        for(int i=0;i<2;i++){
            int [] liczba = {0,1};
            for(int j=0;j<4;j++){
                double tmp = localCoordinate[liczba[i]].integrationPoint[1];
                localCoordinate[liczba[i]].integrationPoint[1] = -1;
                localCoordinate[liczba[i]].setShapeFunctions();
                if(element.Nodes[0].isBoundaryCondition() && element.Nodes[1].isBoundaryCondition()){
                    surfaces[0][i][j] = localCoordinate[liczba[i]].shapeFunctions[j];
                }
                else{
                    surfaces[0][i][j] = 0;
                }
                localCoordinate[liczba[i]].integrationPoint[1] = tmp;
                localCoordinate[liczba[i]].setShapeFunctions();
            }
        }

        for(int i=0;i<2;i++){
            int [] liczba = {1,2};
            for(int j=0;j<4;j++){
                double tmp = localCoordinate[liczba[i]].integrationPoint[0];
                localCoordinate[liczba[i]].integrationPoint[0] = 1;
                localCoordinate[liczba[i]].setShapeFunctions();
                if(element.Nodes[1].isBoundaryCondition() && element.Nodes[2].isBoundaryCondition()){
                    surfaces[1][i][j] = localCoordinate[liczba[i]].shapeFunctions[j];
                }
                else{
                    surfaces[1][i][j] = 0;
                }
                localCoordinate[liczba[i]].integrationPoint[0] = tmp;
                localCoordinate[liczba[i]].setShapeFunctions();
            }
        }

        for(int i=0;i<2;i++){
            int [] liczba = {2,3};
            for(int j=0;j<4;j++){
                double tmp = localCoordinate[liczba[i]].integrationPoint[1];
                localCoordinate[liczba[i]].integrationPoint[1] = 1;
                localCoordinate[liczba[i]].setShapeFunctions();
                if(element.Nodes[2].isBoundaryCondition() && element.Nodes[3].isBoundaryCondition()){
                    surfaces[2][i][j] = localCoordinate[liczba[i]].shapeFunctions[j];
                }
                else{
                    surfaces[2][i][j] = 0;
                }
                localCoordinate[liczba[i]].integrationPoint[1] = tmp;
                localCoordinate[liczba[i]].setShapeFunctions();
            }
        }

        for(int i=0;i<2;i++){
            int [] liczba = {3,0};
            for(int j=0;j<4;j++){
                double tmp = localCoordinate[liczba[i]].integrationPoint[0];
                localCoordinate[liczba[i]].integrationPoint[0] = -1;
                localCoordinate[liczba[i]].setShapeFunctions();
                if(element.Nodes[3].isBoundaryCondition() && element.Nodes[0].isBoundaryCondition()){
                    surfaces[3][i][j] = localCoordinate[liczba[i]].shapeFunctions[j];
                }
                else{
                    surfaces[3][i][j] = 0;
                }
                localCoordinate[liczba[i]].integrationPoint[0] = tmp;
                localCoordinate[liczba[i]].setShapeFunctions();
            }
        }
        return surfaces;
    }

    public double [][] matrixH_BC(Element element){
        double [][] matrixH_BC = new double[4][4];

        for(int z=0;z<localCoordinate.length;z++){
                for(int i=0; i<localCoordinate.length; i++) {
                    for (int k = 0; k < localCoordinate.length; k++) {
                        matrixH_BC[i][k] += matrixOfJacobiego(element)[0][z]*((elementSurface(element)[z][0][i] * elementSurface(element)[z][0][k])*alfa*weight1 +  (elementSurface(element)[z][1][i] * elementSurface(element)[z][1][k])*alfa*weight2);
                    }
                }
        }
        return matrixH_BC;
    }

    public void vector_P(Element element){
        double [][] vectorP = new double [1][localCoordinate.length];
        double [][] matrix_P = new double[4][4];
        for(int z=0;z<localCoordinate.length;z++){
            for(int i=0; i<localCoordinate.length; i++) {
                for (int k = 0; k < localCoordinate.length; k++) {
                    matrix_P[i][k] += matrixOfJacobiego(element)[0][i]*((initialTemperature*(-alfa)*elementSurface(element)[z][0][i] * elementSurface(element)[z][0][k]) +  initialTemperature*(-alfa)*(elementSurface(element)[z][1][i] * elementSurface(element)[z][1][k]));
                }
            }
        }

        for(int i=0; i<localCoordinate.length; i++){
            for(int j = 0; j < localCoordinate.length; j++){
                vectorP[0][j] += matrix_P[i][j];

            }
        }
    }

    public double [][] Hlocal(Element element){
        double [][] matrixHLocal = new double[4][4];
        double [][] matrixH = matrixH(element);
        double [][] matrixHBC = matrixH_BC(element);
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                matrixHLocal[i][j] = matrixH[i][j] ;
            }
        }
        return matrixHLocal;
    }

    public void aggregation(Grid gird){

        Element [] elements = gird.getElements();
        int [][] H_global = new int[gird.getNodes().length][gird.getNodes().length];
        int [][] C_global = new int[gird.getNodes().length][gird.getNodes().length];
        int [][] H_BC_global = new int[gird.getNodes().length][gird.getNodes().length];

        for(int i=0;i<elements.length;i++){
            int[] ID = new int[4];
            for(int j=0;j<4;j++){
                ID[j] = elements[i].Nodes[j].getIndex() -1;
            }

            for(int z=0;z<4;z++){
                for(int k=0;k<4;k++){
                    H_global[ID[z]][ID[k]] += matrixH(elements[i])[z][k];
                    H_BC_global[ID[z]][ID[k]] += matrixH_BC(elements[i])[z][k];
                    C_global[ID[z]][ID[k]] += matrixC(elements[i])[z][k];

                }
            }
        }

        System.out.println();
        System.out.println("Matrix H global");
        for(int i=0;i<gird.getNodes().length;i++){
            System.out.println(Arrays.toString(H_global[i]));
        }

        System.out.println();
        System.out.println("Matrix H BC global");
        for(int i=0;i<gird.getNodes().length;i++){
            System.out.println(Arrays.toString(H_BC_global[i]));
        }

        System.out.println();
        System.out.println("Matrix C global");
        for(int i=0;i<gird.getNodes().length;i++){
            System.out.println(Arrays.toString(C_global[i]));
        }


    }



}
