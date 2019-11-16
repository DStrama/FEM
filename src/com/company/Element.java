package com.company;

import java.util.Arrays;

public class Element {

    public int[] Nodes =new int[4];
    int Index;

    public Element( int index) {
        Index = index;
    }

    public void setNodes(int index,double nH) {
        Nodes[0] = index;
        Nodes[1] = Nodes[0] + (int)nH;
        Nodes[2] = Nodes[1] + 1;
        Nodes[3] = Nodes[0] + 1;
}

    @Override
    public String toString() {
        return "Element at index: " + Index + " has nodes: " + Nodes[0] + "," + Nodes[1] + "," + Nodes[2] + "," + Nodes[3];
    }
}
