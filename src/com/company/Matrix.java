package com.company;

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

    private LocalCoordinates[] universalElement2D = new LocalCoordinates[]{
            new LocalCoordinates(-E,-n,weight1,weight2),
            new LocalCoordinates(E,-n,weight1,weight2),
            new LocalCoordinates(E,n,weight1,weight2),
            new LocalCoordinates(-E,n,weight1,weight2)
    };

    private double[] surfacePoints = new double[]{-1 / Math.sqrt(3), -1};
    private LocalCoordinates surface1PC1 = new LocalCoordinates(surfacePoints[0], surfacePoints[1], weight1,weight2);
    private LocalCoordinates surface1PC2 = new LocalCoordinates(-surfacePoints[0], surfacePoints[1], weight1,weight2);
    private LocalCoordinates surface2PC1 = new LocalCoordinates(-surfacePoints[1], surfacePoints[0], weight1,weight2);
    private LocalCoordinates surface2PC2 = new LocalCoordinates(-surfacePoints[1], -surfacePoints[0], weight1,weight2);
    private LocalCoordinates surface3PC1 = new LocalCoordinates(-surfacePoints[0], -surfacePoints[1], weight1,weight2);
    private LocalCoordinates surface3PC2 = new LocalCoordinates(surfacePoints[0], -surfacePoints[1], weight1,weight2);
    private LocalCoordinates surface4PC1 = new LocalCoordinates(surfacePoints[1], -surfacePoints[0], weight1,weight2);
    private LocalCoordinates surface4IP2 = new LocalCoordinates(surfacePoints[1], surfacePoints[0], weight1,weight2);


    private double [][] shapeFunctions = new double[universalElement2D.length][universalElement2D.length];
    private double [][] derivativesAfterXi = new double[universalElement2D.length][universalElement2D.length];
    private double [][] derivativesAfterEta = new double[universalElement2D.length][universalElement2D.length];
    private double [][] matrixJacobi = new double[universalElement2D.length][universalElement2D.length];
    private double [] detJacobiTab = new double[universalElement2D.length];
    double [][] dNAfterdy = new double[universalElement2D.length][universalElement2D.length];
    double [][] dNAfterdx = new double[universalElement2D.length][universalElement2D.length];
    double [][] matrixH = new double[universalElement2D.length][universalElement2D.length];
    double [][] matrixH_BC = new double[universalElement2D.length][universalElement2D.length];
    double [][] matrixC = new double[universalElement2D.length][universalElement2D.length];


    public void shapeFunctionsElement2D(){

        double[][] matrixOfShapeFunctions = new double[4][4];

        for(int i=0; i < universalElement2D.length ;i++){
            matrixOfShapeFunctions[i] = new double[]{
                    universalElement2D[i].shapeFunctions[0],
                    universalElement2D[i].shapeFunctions[1],
                    universalElement2D[i].shapeFunctions[2],
                    universalElement2D[i].shapeFunctions[3]};
        }
        shapeFunctions = matrixOfShapeFunctions;
    }


    public void matrixDerivativesAfterXi(){

        double[][] matrixDerivativesAfterXi = new double[4][4];

        for(int i=0; i < universalElement2D.length ;i++){
            matrixDerivativesAfterXi[i] = new double[]{
                    universalElement2D[i].derivativesAfterXi[0],
                    universalElement2D[i].derivativesAfterXi[1],
                    universalElement2D[i].derivativesAfterXi[2],
                    universalElement2D[i].derivativesAfterXi[3]};
        }
        derivativesAfterXi = matrixDerivativesAfterXi;
    }

    public void matrixDerivativesAfterEta(){

        double[][] matrixDerivativesAfterEta = new double[4][4];

        for(int i=0; i < universalElement2D.length ;i++){
            matrixDerivativesAfterEta[i] = new double[]{
                    universalElement2D[i].derivativesAfterEta[0],
                    universalElement2D[i].derivativesAfterEta[1],
                    universalElement2D[i].derivativesAfterEta[2],
                    universalElement2D[i].derivativesAfterEta[3]};
        }
        derivativesAfterEta = matrixDerivativesAfterEta;
    }

    public void matrixOfJacobiego(Element element){

        double[][] matrixOfElementJacobian = new double[4][4];
        double[] dx_dxi = new double[4];
        double[] dx_dn = new double[4];
        double[] dy_dxi = new double[4];
        double[] dy_dn = new double[4];

        for(int i=0;i<universalElement2D.length;i++){
            for(int j=0; j<universalElement2D.length;j++){
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

        double [] matrixDetJacobi = new double[universalElement2D.length];

        for(int j = 0; j<universalElement2D.length; j++){
            matrixDetJacobi[j] = (matrixJacobi[0][j] * matrixJacobi[3][j]) - (matrixJacobi[2][j] * matrixJacobi[1][j]);
        }
        detJacobiTab = matrixDetJacobi;
    }


    public void dNafterDx(){

        double [][] Matrix_dNAfterdx = new double[universalElement2D.length][universalElement2D.length];

        for(int i=0; i<universalElement2D.length;i++){
            for(int j=0; j<universalElement2D.length;j++){
                Matrix_dNAfterdx[i][j] = (1/detJacobiTab[i]) * ( (matrixJacobi[3][i] * derivativesAfterXi[i][j]) - (matrixJacobi[2][i] * derivativesAfterEta[i][j]));
            }
        }
        dNAfterdx = Matrix_dNAfterdx;
    }

    public void dNafterDy(){

        double [][] Matrix_dNAfterdy = new double[universalElement2D.length][universalElement2D.length];

        for(int i=0; i<universalElement2D.length;i++){
            for(int j=0; j<universalElement2D.length;j++){
                Matrix_dNAfterdy[i][j] = (1/detJacobiTab[i]) * ( (matrixJacobi[0][i] * derivativesAfterEta[i][j]) - (matrixJacobi[1][i] *  derivativesAfterXi[i][j]));

            }
        }
        dNAfterdy = Matrix_dNAfterdy;
    }

    public double [][] matrixH(Element element){

        double [][][] eleMatrixH = new double [4][4][4];
        double [][] matrixH = new double[universalElement2D.length][universalElement2D.length];

        for(int z=0; z<universalElement2D.length;z++) {
            for(int i = 0; i < universalElement2D.length; i++){
                for(int j = 0; j < universalElement2D.length; j++){
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
        for(int z=0; z<universalElement2D.length;z++) {
            for(int i = 0; i < universalElement2D.length; i++){
                for(int j = 0; j < universalElement2D.length; j++){
                    eleMatrixC[z][i][j] = specificHeat*density*(shapeFunctions[z][i]*shapeFunctions[z][j])*detJacobiTab[z];
                    matrixC[i][j] += eleMatrixC[z][i][j];
                }
            }
        }
        return matrixC;
    }

    public double [][][] elementSurface(Element element){
        double [][][] surfaces = new double[universalElement2D.length][2][universalElement2D.length];

        for(int i=0;i<2;i++){
            LocalCoordinates [] liczba = {surface1PC1,surface1PC2};
            for(int j=0;j<4;j++){
                liczba[i].setShapeFunctions();
                if(element.Nodes[0].isBoundaryCondition() && element.Nodes[1].isBoundaryCondition()){
                    surfaces[0][i][j] = liczba[i].shapeFunctions[j];
                }
                else{
                    surfaces[0][i][j] = 0;
                }
            }
        }

        for(int i=0;i<2;i++){
            LocalCoordinates [] liczba = {surface2PC1,surface2PC2};
            for(int j=0;j<4;j++){
                liczba[i].setShapeFunctions();
                if(element.Nodes[1].isBoundaryCondition() && element.Nodes[2].isBoundaryCondition()){
                    surfaces[1][i][j] = liczba[i].shapeFunctions[j];
                }
                else{
                    surfaces[1][i][j] = 0;
                }
            }
        }

        for(int i=0;i<2;i++){
            LocalCoordinates [] liczba = {surface3PC1,surface3PC2};
            for(int j=0;j<4;j++){
                liczba[i].setShapeFunctions();
                if(element.Nodes[2].isBoundaryCondition() && element.Nodes[3].isBoundaryCondition()){
                    surfaces[2][i][j] = liczba[i].shapeFunctions[j];
                }
                else{
                    surfaces[2][i][j] = 0;
                }
            }
        }

        for(int i=0;i<2;i++){
            LocalCoordinates [] liczba = {surface4PC1,surface4IP2};
            for(int j=0;j<4;j++){
                liczba[i].setShapeFunctions();
                if(element.Nodes[3].isBoundaryCondition() && element.Nodes[0].isBoundaryCondition()){
                    surfaces[3][i][j] = liczba[i].shapeFunctions[j];
                }
                else{
                    surfaces[3][i][j] = 0;
                }
            }
        }
        return surfaces;
    }

    public double detJacobian1D(Element element, int[] nodeIndex){
        double x1,x0,y1,y0;
        x0 = element.getNodes()[nodeIndex[0]].getX();
        y0 = element.getNodes()[nodeIndex[0]].getY();

        x1 = element.getNodes()[nodeIndex[1]].getX();
        y1 = element.getNodes()[nodeIndex[1]].getY();

        return 0.5*Math.sqrt(Math.pow(x1-x0,2)+Math.pow(y1-y0,2));
    }

    public double [][] matrixH_BC(Element element){
        double [][] matrixH_BC = new double[4][4];
        int [][] nodeIndexJacobi = {{0,1},{1,2},{2,3},{3,0}};
        for(int z=0;z<universalElement2D.length;z++){
                for(int i=0; i<universalElement2D.length; i++) {
                    for (int k = 0; k < universalElement2D.length; k++) {
                        matrixH_BC[i][k] += detJacobian1D(element,nodeIndexJacobi[z])*((elementSurface(element)[z][0][i] * elementSurface(element)[z][0][k])*alfa*weight1 +  (elementSurface(element)[z][1][i] * elementSurface(element)[z][1][k])*alfa*weight2);
                    }
                }
        }
        return matrixH_BC;
    }

    public double[] vector_P(Element element){
        double [] vectorP = new double [universalElement2D.length];
        double [][] matrix_P = new double[4][4];
        int [][] nodeIndexJacobi = {{0,1},{1,2},{2,3},{3,0}};
        for(int z=0;z<universalElement2D.length;z++){
            for(int i=0; i<universalElement2D.length; i++) {
                for (int k = 0; k < universalElement2D.length; k++) {
                    matrix_P[i][k] += detJacobian1D(element,nodeIndexJacobi[z])*((ambientTemperature*(-alfa)*elementSurface(element)[z][0][i] * elementSurface(element)[z][0][k]) +  ambientTemperature*(-alfa)*(elementSurface(element)[z][1][i] * elementSurface(element)[z][1][k]));
                }
            }
        }

        for(int i=0; i<universalElement2D.length; i++){
            for(int j = 0; j < universalElement2D.length; j++){
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
