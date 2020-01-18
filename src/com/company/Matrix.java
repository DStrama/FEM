package com.company;

import org.la4j.Vector;
import org.la4j.Vectors;
import org.la4j.linear.GaussianSolver;
import org.la4j.matrix.dense.Basic1DMatrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.dense.BasicVector;
import org.w3c.dom.ls.LSOutput;

import java.util.Arrays;

public class Matrix {

    private double E = (1/Math.sqrt(3));
    private double n = (1/Math.sqrt(3));
    private double weight1 = 1;
    private double weight2 = 1;

    GlobalDate globalDate = new GlobalDate();
    private double initialTemperature = globalDate.getInitialTemperature();
    private double simulationTime = globalDate.getSimulationTime();
    private double simulationStepTime = globalDate.getSimulationStepTime();
    private double ambientTemperature = globalDate.getAmbientTemperature();
    private double alfa = globalDate.getAlfa();
    private double specificHeat = globalDate.getSpecificHeat();
    private double conductivity = globalDate.getConductivity();
    private double density = globalDate.getDensity();

    private UniversalElement[] localCoordinate = new UniversalElement[]{
            new UniversalElement(-E,-n,weight1,weight2),
            new UniversalElement(E,-n,weight1,weight2),
            new UniversalElement(E,n,weight1,weight2),
            new UniversalElement(-E,n,weight1,weight2)
    };

    private double [][] shapeFunctions = new double[localCoordinate.length][localCoordinate.length];
    private double [][] derivativesAfterXi = new double[localCoordinate.length][localCoordinate.length];
    private double [][] derivativesAfterEta = new double[localCoordinate.length][localCoordinate.length];
    private double [][] matrixJacobi = new double[localCoordinate.length][localCoordinate.length];
    private double [] detJacobiTab = new double[localCoordinate.length];
    double [][] dNAfterdy = new double[localCoordinate.length][localCoordinate.length];
    double [][] dNAfterdx = new double[localCoordinate.length][localCoordinate.length];
    double [][] matrixH = new double[localCoordinate.length][localCoordinate.length];
    double [][] matrixH_BC = new double[localCoordinate.length][localCoordinate.length];
    double [][] matrixC = new double[localCoordinate.length][localCoordinate.length];


    public void shapeFunctionsElement2D(){

        double[][] matrixOfShapeFunctions = new double[4][4];

        for(int i=0; i < localCoordinate.length ;i++){
            matrixOfShapeFunctions[i] = new double[]{
                    localCoordinate[i].shapeFunctions[0],
                    localCoordinate[i].shapeFunctions[1],
                    localCoordinate[i].shapeFunctions[2],
                    localCoordinate[i].shapeFunctions[3]};
        }
        shapeFunctions = matrixOfShapeFunctions;
    }


    public void matrixDerivativesAfterXi(){

        double[][] matrixDerivativesAfterXi = new double[4][4];

        for(int i=0; i < localCoordinate.length ;i++){
            matrixDerivativesAfterXi[i] = new double[]{
                    localCoordinate[i].derivativesAfterXi[0],
                    localCoordinate[i].derivativesAfterXi[1],
                    localCoordinate[i].derivativesAfterXi[2],
                    localCoordinate[i].derivativesAfterXi[3]};
        }
        derivativesAfterXi = matrixDerivativesAfterXi;
    }

    public void matrixDerivativesAfterEta(){

        double[][] matrixDerivativesAfterEta = new double[4][4];

        for(int i=0; i < localCoordinate.length ;i++){
            matrixDerivativesAfterEta[i] = new double[]{
                    localCoordinate[i].derivativesAfterEta[0],
                    localCoordinate[i].derivativesAfterEta[1],
                    localCoordinate[i].derivativesAfterEta[2],
                    localCoordinate[i].derivativesAfterEta[3]};
        }
        derivativesAfterEta = matrixDerivativesAfterEta;
    }

    public void matrixOfJacobiego(Element element){

        double[][] matrixOfElementJacobian = new double[4][4];
        double[] dx_dxi = new double[4];
        double[] dx_dn = new double[4];
        double[] dy_dxi = new double[4];
        double[] dy_dn = new double[4];

        for(int i=0;i<localCoordinate.length;i++){
            for(int j=0; j<localCoordinate.length;j++){
                dx_dxi[i] += derivativesAfterXi[i][j] * element.Nodes[j].getX();
                dx_dn[i] +=  derivativesAfterEta[i][j] * element.Nodes[j].getX();
                dy_dxi[i] += derivativesAfterXi[i][j] * element.Nodes[j].getY();
                dy_dn[i] += derivativesAfterEta[i][j] * element.Nodes[j].getY();
            }
            matrixOfElementJacobian[0][i] = dx_dxi[i];
            matrixOfElementJacobian[1][i] = dx_dn[i];
            matrixOfElementJacobian[2][i] = dy_dxi[i];
            matrixOfElementJacobian[3][i] = dy_dn[i];
        }
        matrixJacobi = matrixOfElementJacobian;
    }

    public void detJacobi(){

        double [] matrixDetJacobi = new double[localCoordinate.length];

        for(int j = 0; j<localCoordinate.length; j++){
            matrixDetJacobi[j] = (matrixJacobi[0][j] * matrixJacobi[3][j]) - (matrixJacobi[2][j] * matrixJacobi[1][j]);
        }
        detJacobiTab = matrixDetJacobi;
    }


    public void dNafterDx(){

        double [][] Matrix_dNAfterdx = new double[localCoordinate.length][localCoordinate.length];

        for(int i=0; i<localCoordinate.length;i++){
            for(int j=0; j<localCoordinate.length;j++){
                Matrix_dNAfterdx[i][j] = (1/detJacobiTab[i]) * ( (matrixJacobi[3][i] * derivativesAfterXi[i][j]) - (matrixJacobi[2][i] * derivativesAfterEta[i][j]));
            }
        }
        dNAfterdx = Matrix_dNAfterdx;
    }

    public void dNafterDy(){

        double [][] Matrix_dNAfterdy = new double[localCoordinate.length][localCoordinate.length];

        for(int i=0; i<localCoordinate.length;i++){
            for(int j=0; j<localCoordinate.length;j++){
                Matrix_dNAfterdy[i][j] = (1/detJacobiTab[i]) * ( (matrixJacobi[0][i] * derivativesAfterEta[i][j]) - (matrixJacobi[1][i] *  derivativesAfterXi[i][j]));

            }
        }
        dNAfterdy = Matrix_dNAfterdy;
    }

    public double [][] matrixH(Element element){

        double [][][] eleMatrixH = new double [4][4][4];
        double [][] matrixH = new double[localCoordinate.length][localCoordinate.length];

        for(int z=0; z<localCoordinate.length;z++) {
            for(int i = 0; i < localCoordinate.length; i++){
                for(int j = 0; j < localCoordinate.length; j++){
                    eleMatrixH[z][i][j] = conductivity*(dNAfterdx[z][i]*dNAfterdx[z][j] + dNAfterdy[z][i]*dNAfterdy[z][j])*detJacobiTab[z];
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
                    eleMatrixC[z][i][j] = specificHeat*density*(shapeFunctions[z][i]*shapeFunctions[z][j])*detJacobiTab[z];
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
                        matrixH_BC[i][k] += matrixJacobi[0][z]*((elementSurface(element)[z][0][i] * elementSurface(element)[z][0][k])*alfa*weight1 +  (elementSurface(element)[z][1][i] * elementSurface(element)[z][1][k])*alfa*weight2);
                    }
                }
        }
        return matrixH_BC;
    }

    public double[] vector_P(Element element){
        double [] vectorP = new double [localCoordinate.length];
        double [][] matrix_P = new double[4][4];
        for(int z=0;z<localCoordinate.length;z++){
            for(int i=0; i<localCoordinate.length; i++) {
                for (int k = 0; k < localCoordinate.length; k++) {
                    matrix_P[i][k] += matrixJacobi[0][i]*((ambientTemperature*(-alfa)*elementSurface(element)[z][0][i] * elementSurface(element)[z][0][k]) +  ambientTemperature*(-alfa)*(elementSurface(element)[z][1][i] * elementSurface(element)[z][1][k]));
                }
            }
        }

        for(int i=0; i<localCoordinate.length; i++){
            for(int j = 0; j < localCoordinate.length; j++){
                vectorP[j] += matrix_P[i][j];

            }
        }
        return vectorP;
    }

    public double[][] calculate_H_global(Grid gird){
        Element [] elements = gird.getElements();
        double [][] H_global = new double[gird.getNodes().length][gird.getNodes().length];
        for(int i=0;i<elements.length;i++){
            int[] ID = new int[4];
            for(int j=0;j<4;j++){
                ID[j] = elements[i].Nodes[j].getIndex() -1;
            }

            for(int z=0;z<4;z++){
                for(int k=0;k<4;k++){
                    H_global[ID[z]][ID[k]] += matrixH(elements[i])[z][k];
                }
            }
        }

//        System.out.println();
//        System.out.println("Matrix H global");
//        for(int i=0;i<gird.getNodes().length;i++){
//            System.out.println(Arrays.toString(H_global[i]));
//        }

        return H_global;
    }

    public double[][] calculate_C_global(Grid gird){
        Element [] elements = gird.getElements();
        double [][] C_global = new double[gird.getNodes().length][gird.getNodes().length];
        for(int i=0;i<elements.length;i++){
            int[] ID = new int[4];
            for(int j=0;j<4;j++){
                ID[j] = elements[i].Nodes[j].getIndex() -1;
            }

            for(int z=0;z<4;z++){
                for(int k=0;k<4;k++){
                    C_global[ID[z]][ID[k]] += matrixC(elements[i])[z][k];
                }
            }
        }

//        System.out.println();
//        System.out.println("Matrix C global");
//        for(int i=0;i<gird.getNodes().length;i++){
//            System.out.println(Arrays.toString(C_global[i]));
//        }

        return C_global;
    }

    public double[][] calculate_H_BC_global(Grid gird){
        Element [] elements = gird.getElements();
        double [][] H_BC_global = new double[gird.getNodes().length][gird.getNodes().length];
        for(int i=0;i<elements.length;i++){
            int[] ID = new int[4];
            for(int j=0;j<4;j++){
                ID[j] = elements[i].Nodes[j].getIndex() -1;
            }

            for(int z=0;z<4;z++){
                for(int k=0;k<4;k++){
                    H_BC_global[ID[z]][ID[k]] += matrixH_BC(elements[i])[z][k];
                }
            }
        }

//        System.out.println();
//        System.out.println("Matrix H BC global");
//        for(int i=0;i<gird.getNodes().length;i++){
//            System.out.println(Arrays.toString(H_BC_global[i]));
//        }
        return H_BC_global;
    }

    public double[] calculate_Vector_P(Grid gird){
        Element [] elements = gird.getElements();
        double [] P_vector_global = new double[gird.getNodes().length];
        for(int i=0;i<elements.length;i++){
            int[] ID = new int[4];
            for(int j=0;j<4;j++){
                ID[j] = elements[i].Nodes[j].getIndex() -1;
            }
            for(int k=0;k<4;k++){
                    P_vector_global[ID[k]] += vector_P(elements[i])[k];
            }
        }

//        System.out.println();
//        System.out.println("Vector P global");
//        System.out.println(Arrays.toString(P_vector_global));

        return P_vector_global;
    }


    public double getInitialTemperature() {
        return initialTemperature;
    }

    public double getSimulationTime() {
        return simulationTime;
    }

    public double getSimulationStepTime() {
        return simulationStepTime;
    }
}
