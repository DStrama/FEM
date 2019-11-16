package com.company;

import java.util.Arrays;

public class Grid {

    GlobalDate globalDate = new GlobalDate();
    private Node[] nodes;
    private Element[] elements;

    public Grid() {
        this.nodes = new Node[(int)globalDate.getNumberOfNodes() +1];
        this.elements = new Element[(int)globalDate.getNumberOfElements() +1];

        double deltaX = (globalDate.getWidth()) / (globalDate.getNumberNodeWidth() -1);
        double deltaY = (globalDate.getHeight()) / (globalDate.getNumberNodeHight() -1);

        //NODE
        int index = 0;
        for(int i=0; i<globalDate.getNumberNodeWidth(); i++){
            for(int j=0; j<globalDate.getNumberNodeHight(); j++ ){
                Node node = new Node(index+1,i*deltaX,j*deltaY,0,globalDate.getHeight(),globalDate.getWidth());
                nodes[index] = node;
                System.out.println(node.toString());
                index++;
            }
        }

        //ELEMENTS
        int indexE = 1;
        for(int i=0; i< globalDate.getNumberOfElements(); i++){
            elements[i] = new Element(i+1);
            elements[i].setNodes(indexE,globalDate.getNumberNodeHight());
            indexE++;
            if(indexE % globalDate.getNumberNodeHight() == 0 ) {
                indexE++;
            }
            System.out.println(elements[i].toString());

        }
    }
}
