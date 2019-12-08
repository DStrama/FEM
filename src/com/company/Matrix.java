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

    public void matrixOfJacobiego(Element element){

        double dx_After_dXi = 0;
        double dx_After_dEta = 0;
        double dy_After_dXi = 0;
        double dy_After_dEta = 0;

        double detj = 0;
        double[] dN_After_dx = new double[4];
        double[] dN_After_dy = new double[4];

        for(int i=0; i<4; i++){

            dx_After_dXi = dx_After_dXi + eu.localCoordinateSystem[i].derivativesAfterXi[i]*element.Nodes[i].getX();
            dx_After_dEta = dx_After_dEta + eu.localCoordinateSystem[i].derivativesAfterEta[i]*element.Nodes[i].getX();
            dy_After_dXi = dy_After_dXi + eu.localCoordinateSystem[i].derivativesAfterXi[i]*element.Nodes[i].getY();
            dy_After_dEta = dy_After_dEta + eu.localCoordinateSystem[i].derivativesAfterEta[i]*element.Nodes[i].getY();

        }

        detj = (dx_After_dXi*dy_After_dEta - dx_After_dEta*dy_After_dXi);

        for(int i=0; i<4; i++){
            dN_After_dx[i] = (1/detj*(dy_After_dEta*eu.localCoordinateSystem[i].derivativesAfterXi[i] - dy_After_dXi*eu.localCoordinateSystem[i].derivativesAfterEta[i]));
            dN_After_dy[i] = (1/detj*(-dx_After_dEta*eu.localCoordinateSystem[i].derivativesAfterXi[i] + dx_After_dXi*eu.localCoordinateSystem[i].derivativesAfterEta[i]));

        }

            System.out.println("-----------------------------------");
            System.out.println("dx after dXi: " + dx_After_dXi + "|\t" + "dy after dXi: " + dy_After_dXi);
            System.out.println("dx after dEta: " + dx_After_dEta + "|\t" + "dy after dEta: " + dy_After_dEta);
            System.out.println("-----------------------------------");
            System.out.println("-----------------------------------");
            System.out.println("-----------------------------------");

            System.out.println("Jakobian :" + detj + "for element " + element);
            for(int i=0 ; i < 4; i++){
                System.out.println("dN" + (i+1) + " After dx: " + dN_After_dx[i] + "\t\tdN" + (i+1) + " After dy: " + dN_After_dy[i]);
            }



    }

}
