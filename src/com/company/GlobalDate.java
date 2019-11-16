package com.company;

public class GlobalDate {

    private double height;
    private double width;
    private double numberNodeHight;
    private double numberNodeWidth;
    private double numberOfElements;
    private double numberOfNodes;

    public GlobalDate() {
        ReadFile readFile = new ReadFile();
        double[] dataArray = readFile.readDataFromFile();
        height =  dataArray[0];
        width = dataArray[1];
        numberNodeHight = dataArray[2];
        numberNodeWidth = dataArray[3];
        numberOfElements = (numberNodeWidth - 1) * (numberNodeHight - 1);
        numberOfNodes = (numberNodeHight * numberNodeWidth);
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


    public double getNumberOfElements() {
        return numberOfElements;
    }

    public double getNumberOfNodes() {
        return numberOfNodes;
    }
}
