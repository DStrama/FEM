package com.company;

public class Node {

    private int index;
    private double x;
    private double y;
    private double tempatature;
    private boolean boundaryCondition;

    public Node(int index, double x, double y, double tempatature,double H, double W) {
        this.index = index;
        this.x = x;
        this.y = y;
        this.tempatature = tempatature;
        this.boundaryCondition = boundryCondition(H,W);
    }

    public boolean boundryCondition(double H,double W){
        return( (x == 0) || (y == 0) || (x == W) || (y == H)  );
    }

    @Override
    public String toString() {
        return "index: " + this.index + " x: " + this.x + " y: " + this.y + " temperatura: " +  this.tempatature + " boundaryCondition: " + this.boundaryCondition;
    }

}


