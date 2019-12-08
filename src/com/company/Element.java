package com.company;

import java.util.Arrays;

public class Element {

    public Node[] Nodes = new Node[4];
    int Index;

    public Element( int index) {
        Index = index;
    }

    public void setNodes(Node nodeOne , Node nodeTwo, Node nodeThree, Node nodeFour) {
        Nodes[0] = nodeOne;
        Nodes[1] = nodeTwo;
        Nodes[2] = nodeThree;
        Nodes[3] = nodeFour;
}

    public Node[] getNodes() {
        return Nodes;
    }

    @Override
    public String toString() {
        return "Element at index: " + Index + " has nodes: " + Nodes[0].getIndex() + "," + Nodes[1].getIndex() + "," + Nodes[2].getIndex() + "," + Nodes[3].getIndex();
    }
}
