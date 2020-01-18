package com.company;

import org.la4j.linear.GaussianSolver;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.dense.BasicVector;

import java.util.Arrays;

public class Simulation {
    public void Simulation(Grid grid){
        Matrix matrix = new Matrix();
        int iterationSteps = (int)(matrix.getSimulationTime()/matrix.getSimulationStepTime());
        matrix.shapeFunctionsElement2D();
        matrix.matrixDerivativesAfterXi();
        matrix.matrixDerivativesAfterEta();
        for(int z=0;z<grid.getElements().length;z++){
            matrix.matrixOfJacobiego(grid.getElements()[z]);
            matrix.detJacobi();
            matrix.dNafterDx();
            matrix.dNafterDy();

            double [] nodesTemperatures = new double[new GlobalDate().getNumberOfNodes()];
            Arrays.fill(nodesTemperatures,100);

            double [] minTemperatures = new double[iterationSteps];
            double [] maxTemperatures = new double[iterationSteps];
            double time = 0;
            double [] arraytime= new double[iterationSteps];

            for(int k=0;k<iterationSteps;k++){
                double [][] global_H = matrix.calculate_H_global(grid);
                double[][] global_H_BC= matrix.calculate_H_BC_global(grid);
                double[][] global_C = matrix.calculate_C_global(grid);
                double [] vector_P = matrix.calculate_Vector_P(grid);

                time += matrix.getSimulationStepTime();
                arraytime[k] = time;
                for(int j=0;j<global_C.length;j++){
                    vector_P[j] *=(-1);
                }

                for(int i=0;i<global_C.length;i++){
                    for(int j=0;j<global_C.length;j++){
                        global_H[i][j] += global_H_BC[i][j];
                        global_H[i][j]  +=  global_C[i][j]*1/matrix.getSimulationStepTime();
                        vector_P[j] = vector_P[j] + (global_C[i][j]/matrix.getSimulationStepTime())*nodesTemperatures[i];
                    }
                }

                Basic2DMatrix MatrixH = new Basic2DMatrix(global_H);
                BasicVector vector_p_type = new BasicVector(vector_P);
                GaussianSolver gaussianSolver = new GaussianSolver(MatrixH);
                BasicVector t1 = (BasicVector) gaussianSolver.solve(vector_p_type);
                nodesTemperatures = t1.toArray();

                double[] aditionalArray = nodesTemperatures.clone();
                for(int i=0;i<aditionalArray.length;i++){
                    grid.getNodes()[i].setTempatature(aditionalArray[i]);

                }
                for(int i=0;i<grid.getElements().length;i++){
                    for(int j=0;j<4;j++){
                        System.out.println(grid.getElements()[i].getNodes()[j]);
                    }
                }
                Arrays.sort(aditionalArray);
                minTemperatures[k] = aditionalArray[0];
                maxTemperatures[k] = aditionalArray[aditionalArray.length-1];

            System.out.println("H Matrix ([H]+[C]/dT)");
            System.out.println(MatrixH);
            System.out.println();

            System.out.println("P_Vector ([{P}+{[C]/dT}*{T0})");
            System.out.println(vector_p_type);
            System.out.println();
                System.out.println(  "Time [s]: "+ arraytime[k] + " MinTemp [s]:" + Math.round(minTemperatures[k] * 1000.0) / 1000.0 +" MaxTemp[s]: " + Math.round(maxTemperatures[k] * 1000.0) / 1000.0);
            }

            for(int k=0;k<iterationSteps;k++){
                System.out.println(  "Time [s]: "+ arraytime[k] + " MinTemp [s]:" + Math.round(minTemperatures[k] * 1000.0) / 1000.0 +" MaxTemp[s]: " + Math.round(maxTemperatures[k] * 1000.0) / 1000.0);
            }

        }



    }
}
