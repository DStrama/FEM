package com.company;

public class GlobalDate {

    private double initialTemperature;
    private double simulationTime;
    private double simulationStepTime;
    private double ambientTemperature;
    private double alfa;
    private double height;
    private double width;
    private double numberNodeHight;
    private double numberNodeWidth;
    private double specificHeat;
    private double conductivity;
    private double density;
    private int numberOfElements;
    private int numberOfNodes;

    public GlobalDate() {
        ReadFile readFile = new ReadFile();
        double[] dataArray = readFile.readDataFromFile();

        initialTemperature = dataArray[0];
        simulationTime = dataArray[1];
        simulationStepTime = dataArray[2];
        ambientTemperature = dataArray[3];
        alfa = dataArray[4];
        height =  dataArray[5];
        width = dataArray[6];
        numberNodeHight = dataArray[7];
        numberNodeWidth = dataArray[8];
        specificHeat = dataArray[9];
        conductivity = dataArray[10];
        density = dataArray[11];

        numberOfElements = (int)((numberNodeWidth - 1) * (numberNodeHight - 1));
        numberOfNodes = (int)((numberNodeHight * numberNodeWidth));
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

    public double getAmbientTemperature() {
        return ambientTemperature;
    }

    public double getAlfa() {
        return alfa;
    }

    public double getSpecificHeat() {
        return specificHeat;
    }

    public double getConductivity() {
        return conductivity;
    }

    public double getDensity() {
        return density;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getNumberNodeHight() {
        return numberNodeHight;
    }

    public double getNumberNodeWidth() {
        return numberNodeWidth;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }
}
