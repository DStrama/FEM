package com.company;

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
    private double initialTemperature = 120;

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
    double [][] matrixH = new double[localCoordinate.length][localCoordinate.length];
    double [][] matrixH_BC = new double[localCoordinate.length][localCoordinate.length];
    double [][] matrixC = new double[localCoordinate.length][localCoordinate.length];

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
        matrixH();
        matrixC();
        matrixH_BC();
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

    public void matrixH(){
        double [][][] eleMatrixH = new double [4][4][4];
        for(int z=0; z<localCoordinate.length;z++) {
            for(int i = 0; i < localCoordinate.length; i++){
                for(int j = 0; j < localCoordinate.length; j++){
                eleMatrixH[z][i][j] = K*(dNAfterdx[z][i]*dNAfterdx[z][j] + dNAfterdy[z][i]*dNAfterdy[z][j])*detJacobiTab[z];
                matrixH[i][j] += eleMatrixH[z][i][j];
            }
        }
      }

      System.out.println("Macierz H");
      for(int i = 0; i < localCoordinate.length; i++){
          for(int j = 0; j < localCoordinate.length; j++){
              System.out.println(matrixH[i][j]);
          }
      }
      System.out.println();
    }

    public void matrixC(){
        double [][][] eleMatrixC = new double [4][4][4];
        for(int z=0; z<localCoordinate.length;z++) {
            for(int i = 0; i < localCoordinate.length; i++){
                for(int j = 0; j < localCoordinate.length; j++){
                    eleMatrixC[z][i][j] = c*ro*(shapeFunctions[z][i]*shapeFunctions[z][j])*detJacobiTab[z];
                    matrixC[i][j] += eleMatrixC[z][i][j];
                }
            }
        }

        System.out.println("Macierz C");
        for(int i = 0; i < localCoordinate.length; i++){
            for(int j = 0; j < localCoordinate.length; j++){
                System.out.println(matrixC[i][j]);
            }
        }
        System.out.println();

    }

    public void matrixH_BC(){
        double [][][] pow = new double[localCoordinate.length][2][localCoordinate.length];


        for(int i=0;i<2;i++){
            int [] liczba = {0,1};
            for(int j=0;j<4;j++){
                double tmp = localCoordinate[liczba[i]].integrationPoint[1];
                localCoordinate[liczba[i]].integrationPoint[1] = -1;
                localCoordinate[liczba[i]].setShapeFunctions();
                pow[0][i][j] = localCoordinate[liczba[i]].shapeFunctions[j];
                System.out.println(pow[0][i][j]);
                localCoordinate[liczba[i]].integrationPoint[1] = tmp;
                localCoordinate[liczba[i]].setShapeFunctions();
            }
        }
        System.out.println();

        for(int i=0;i<2;i++){
            int [] liczba = {1,2};
            for(int j=0;j<4;j++){
                double tmp = localCoordinate[liczba[i]].integrationPoint[0];
                localCoordinate[liczba[i]].integrationPoint[0] = 1;
                localCoordinate[liczba[i]].setShapeFunctions();
                pow[1][i][j] = localCoordinate[liczba[i]].shapeFunctions[j];
                System.out.println(pow[1][i][j]);
                localCoordinate[liczba[i]].integrationPoint[0] = tmp;
                localCoordinate[liczba[i]].setShapeFunctions();
            }
        }
        System.out.println();
        for(int i=0;i<2;i++){
            int [] liczba = {2,3};
            for(int j=0;j<4;j++){
                double tmp = localCoordinate[liczba[i]].integrationPoint[1];
                localCoordinate[liczba[i]].integrationPoint[1] = 1;
                localCoordinate[liczba[i]].setShapeFunctions();
                pow[2][i][j] = localCoordinate[liczba[i]].shapeFunctions[j];
                System.out.println(pow[2][i][j]);
                localCoordinate[liczba[i]].integrationPoint[1] = tmp;
                localCoordinate[liczba[i]].setShapeFunctions();
            }
        }
        System.out.println();
        for(int i=0;i<2;i++){
            int [] liczba = {3,0};
            for(int j=0;j<4;j++){
                double tmp = localCoordinate[liczba[i]].integrationPoint[0];
                localCoordinate[liczba[i]].integrationPoint[0] = -1;
                localCoordinate[liczba[i]].setShapeFunctions();
                pow[3][i][j] = localCoordinate[liczba[i]].shapeFunctions[j];
                //System.out.println(pow[3][i][j]);
                localCoordinate[liczba[i]].integrationPoint[0] = tmp;
                localCoordinate[liczba[i]].setShapeFunctions();
            }
        }
        System.out.println();

        System.out.println("H BC");
        for(int z=0;z<localCoordinate.length;z++){
                for(int i=0; i<localCoordinate.length; i++) {
                    for (int k = 0; k < localCoordinate.length; k++) {
                        matrixH_BC[i][k] += (detJacobiTab[z]*((pow[z][0][i] * pow[z][0][k])*alfa*weight1 + (pow[z][1][i] * pow[z][1][k]*alfa*weight2)));
                    }
                }
        }

        for(int i=0; i<localCoordinate.length; i++) {
            for (int k = 0; k < localCoordinate.length; k++) {
        System.out.println(matrixH_BC[i][k]);
            }
        }

    }

    public void vector_P(){
        double [][] p = new double[localCoordinate.length][localCoordinate.length];
        for(int i=0;i<localCoordinate.length;i++){
            for(int j=0;j<localCoordinate.length;j++){
                p[i][j] = -alfa*shapeFunctions[i][j]*initialTemperature;
            }
        }

    }

}
