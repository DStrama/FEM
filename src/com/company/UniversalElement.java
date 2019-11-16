package com.company;

public class UniversalElement {
    double[] integrationPoint;
    double[] weight;
    double[] derivativesAfterXi;
    double[] derivativesAfterEta;
    double[] shapeFunctions;

    public UniversalElement(double e, double n, double weightOne, double weightTwo) {
        this.integrationPoint = new double[]{e,n};
        this.weight = new double[]{weightOne,weightTwo};
        this.derivativesAfterXi = new double[]{-( 1 - n)/4,( 1 - n)/4,( 1 + n)/4,-( 1 + n)/4};
        this.derivativesAfterEta = new double[]{-( 1 - e)/4, -( 1 + e)/4,( 1 + e)/4,( 1 - e)/4};
        this.shapeFunctions= new double[]{( 1 - e )*( 1 - n)/4,( 1 + e )*( 1 - n)/4,( 1 + e )*( 1 + n)/4,( 1 - e )*( 1 + n)/4};
    }




}
