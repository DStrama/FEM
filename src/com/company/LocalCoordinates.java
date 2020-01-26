package com.company;

public class LocalCoordinates {
    double[] integrationPoint;
    double[] weight;
    double[] derivativesAfterXi;
    double[] derivativesAfterEta;
    double[] shapeFunctions;

    public LocalCoordinates(double xi, double eta, double weightOne, double weightTwo) {
        this.integrationPoint = new double[]{xi,eta};
        this.weight = new double[]{weightOne,weightTwo};
        this.derivativesAfterXi = new double[]{dN1dXi(xi, eta), dN2dXi(xi, eta), dN3dXi(xi, eta), dN4dXi(xi, eta)};
        this.derivativesAfterEta = new double[]{dN1dEta(xi, eta), dN2dEta(xi, eta), dN3dEta(xi, eta), dN4dEta(xi, eta)};
        this.shapeFunctions= new double[]{N1(xi, eta), N2(xi, eta), N3(xi, eta), N4(xi, eta)};
    }

    public double N1(double xi, double eta) {
        return 0.25 * (1 - xi) * (1 - eta);
    }

    public double N2(double xi, double eta) {
        return 0.25 * (1 + xi) * (1 - eta);
    }

    public double N3(double xi, double eta) {
        return 0.25 * (1 + xi) * (1 + eta);
    }

    public double N4(double xi, double eta) {
        return 0.25 * (1 - xi) * (1 + eta);
    }

    public double dN1dXi(double xi, double eta) {
        return (-0.25) * (1 - eta);
    }

    public double dN2dXi(double xi, double eta) {
        return 0.25 * (1 - eta);
    }

    public double dN3dXi(double xi, double eta) {
        return 0.25 * (1 + eta);
    }

    public double dN4dXi(double xi, double eta) {
        return (-0.25) * (1 + eta);
    }

    public double dN1dEta(double xi, double eta) {
        return (-0.25) * (1 - xi);
    }

    public double dN2dEta(double xi, double eta) {
        return -(0.25) * (1 + xi);
    }

    public double dN3dEta(double xi, double eta) {
        return 0.25 * (1 + xi);
    }

    public double dN4dEta(double xi, double eta) {
        return 0.25 * (1 - xi);
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
