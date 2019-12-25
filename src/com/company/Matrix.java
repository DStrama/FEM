package com.company;

public class Matrix {

    private double E = (1/Math.sqrt(3));
    private double n = (1/Math.sqrt(3));
    private double weight1 = 1;
    private double weight2 = 1;
    private double k;

    // jeden element uniwerslany
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

    public void shapeFunctionsElement2D( ){
        double[][] matrixOfShapeFunctions = new double[4][4];
        for(int i=0; i < localCoordinate.length ;i++){
            matrixOfShapeFunctions[i] = new double[]{
                    localCoordinate[i].shapeFunctions[0],
                    localCoordinate[i].shapeFunctions[1],
                    localCoordinate[i].shapeFunctions[2],
                    localCoordinate[i].shapeFunctions[3]};
        }
        shapeFunctions = matrixOfShapeFunctions;
        System.out.println("shape");
        for(int j = 0; j<localCoordinate.length; j++){
            for(int i = 0; i<localCoordinate.length; i++){
                System.out.println(shapeFunctions[j][i]);
            }
        }
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
        System.out.println("dN _ dxi");
        for(int j = 0; j<localCoordinate.length; j++){
            for(int i = 0; i<localCoordinate.length; i++){
                System.out.println(derivativesAfterXi[j][i]);
            }
        }
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
        System.out.println("dN _ dn");
        for(int j = 0; j<localCoordinate.length; j++){
            for(int i = 0; i<localCoordinate.length; i++){
                System.out.println(derivativesAfterEta[j][i]);
            }
        }
    }

    public void matrixOfJacobiego(Element element){
        shapeFunctionsElement2D();
        matrixDerivativesAfterXi();
        matrixDerivativesAfterEta();
        double[][] matrixOfElementJacobian = new double[4][4];
        double[] dx_dxi = new double[4];
        double[] dx_dn = new double[4];
        double[] dy_dxi = new double[4];
        double[] dy_dn = new double[4];
        for(int i=0;i<localCoordinate.length;i++){
            for(int j=0; j<localCoordinate.length;j++){
                System.out.println("X: " +element.Nodes[j].getX() + " Y: " +element.Nodes[j].getY() );
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
        for(int i=0; i<localCoordinate.length;i++){
            for(int j=0; j<localCoordinate.length;j++){
                System.out.println(matrixOfElementJacobian[i][j]);
            }
        }

        matrixJacobi = matrixOfElementJacobian;
        detJacobi();
        dNafterDx();
        dNafterDy();
        littlematrixH();
    }

    public void detJacobi(){
        double [] matrixDetJacobi = new double[localCoordinate.length];
        for(int j = 0; j<localCoordinate.length; j++){
            matrixDetJacobi[j] = (matrixJacobi[0][j] * matrixJacobi[3][j]) - (matrixJacobi[2][j] * matrixJacobi[1][j]);
            System.out.println(matrixDetJacobi[j]);
        }
        detJacobiTab = matrixDetJacobi;
    }


    public void dNafterDx(){
        double [][] Matrix_dNAfterdx = new double[localCoordinate.length][localCoordinate.length];

        for(int i=0; i<localCoordinate.length;i++){
            for(int j=0; j<localCoordinate.length;j++){
                System.out.println("jacobian 1/[detj] " +(1/detJacobiTab[i]) + " " + matrixJacobi[3][i] + " " + derivativesAfterEta[i][j] + " " + matrixJacobi[2][i] + " " + derivativesAfterXi[i][j] );
                Matrix_dNAfterdx[i][j] = (1/detJacobiTab[i]) * ( (matrixJacobi[3][i] * derivativesAfterXi[i][j]) - (matrixJacobi[2][i] * derivativesAfterEta[i][j]));
            }
        }
        dNAfterdx = Matrix_dNAfterdx;
        System.out.println("dN after dx");
        for(int i=0; i<localCoordinate.length;i++){
            for(int j=0; j<localCoordinate.length;j++){
                System.out.println("N"+ (j+1) + "/dx    "  + dNAfterdx[i][j]);
            }
        }
    }

    public void dNafterDy(){
        double [][] Matrix_dNAfterdy = new double[localCoordinate.length][localCoordinate.length];

        for(int i=0; i<localCoordinate.length;i++){
            for(int j=0; j<localCoordinate.length;j++){
                Matrix_dNAfterdy[i][j] = (1/detJacobiTab[i]) * ( (matrixJacobi[0][i] * derivativesAfterEta[i][j]) - (matrixJacobi[1][i] * derivativesAfterXi[i][j]));

            }
        }
        dNAfterdy = Matrix_dNAfterdy;
        System.out.println("dn after dy");
        for(int i=0; i<localCoordinate.length;i++){
            for(int j=0; j<localCoordinate.length;j++){
                System.out.println("N"+ (j+1) + "/dy    "  + dNAfterdy[i][j]);
            }
        }
    }

    public void littlematrixH(){

        double [][] matrixMultiplicationDx = new double [4][4];
        double [][] matrixMultiplicationDy = new double [4][4];
        double [][] vector_dNAfterdx = new double[4][4];
        double [][] vector_dNAfterdy = new double[4][4];
        double [][] matrixSum = new double [4][4];
        double [][] vector = new double [4][4];
        double [] pc_1_vector = new double[4];
        double [] pc_2_vector = new double[4];
        double [] pc_3_vector = new double[4];
        double [] pc_4_vector = new double[4];

        for(int k=0;k<localCoordinate.length;k++){
            for (int j=3,i = 0; i < localCoordinate.length; i++,j--){
                vector[k][i] = dNAfterdx[k][j];
            }
        }

//        for (int i = 0; i < localCoordinate.length; i++) { // A rows
//            for (int j = 0; j < localCoordinate.length; j++) { // B columns
//                for (int k = 0; k < 1; k++) { // A columns
//                    matrixMultiplicationDx[i][j] += dNAfterdx[i][k] * dNAfterdx[k][j];
//                    System.out.println( matrixMultiplicationDx[i][j]);
//                }
//            }
//        }
    }


}
