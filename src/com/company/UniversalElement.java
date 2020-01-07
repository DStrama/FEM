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
        this.derivativesAfterXi = new double[]{( n - 1 )/4,( 1 - n)/4,( 1 + n)/4,-( 1 + n)/4};
        this.derivativesAfterEta = new double[]{( e - 1 )/4, -( 1 + e)/4,( 1 + e)/4,( 1 - e)/4};
        this.shapeFunctions= new double[]{( 1 - e )*( 1 - n)/4,( 1 + e )*( 1 - n)/4,( 1 + e )*( 1 + n)/4,( 1 - e )*( 1 + n)/4};
    }

    public void setIntegrationPoint(double[] integrationPoint) {
        this.integrationPoint = integrationPoint;
    }

    public void setDerivativesAfterXi() {
        derivativesAfterXi = new double[]{( integrationPoint[1] - 1 )/4,( 1 - integrationPoint[1])/4,( 1 + integrationPoint[1])/4,-( 1 + integrationPoint[1])/4};
    }

    public void setDerivativesAfterEta() {
        derivativesAfterEta = new double[]{( integrationPoint[0] - 1 )/4, -( 1 + integrationPoint[0])/4,( 1 + integrationPoint[0])/4,( 1 - integrationPoint[0])/4};
    }

    public void setShapeFunctions() {
        shapeFunctions= new double[]{( 1 - integrationPoint[0]  )*( 1 - integrationPoint[1])/4,( 1 + integrationPoint[0]  )*( 1 - integrationPoint[1])/4,( 1 + integrationPoint[0]  )*( 1 + integrationPoint[1])/4,( 1 - integrationPoint[0]  )*( 1 + integrationPoint[1])/4};
    }
}
