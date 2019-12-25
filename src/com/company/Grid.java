package com.company;

import java.util.Arrays;

public class Grid {

    GlobalDate globalDate = new GlobalDate();
    private Node[] nodes;
    private Element[] elements;

    public Grid() {
        buildNodes();
        buildElements();
    }

    public void buildNodes(){
        this.nodes = new Node[globalDate.getNumberOfNodes()];
        //double deltaX = (globalDate.getWidth()) / (globalDate.getNumberNodeWidth() -1);
        //double deltaY = (globalDate.getHeight()) / (globalDate.getNumberNodeHight() -1);
        double deltaX = 0.025;
        double deltaY = 0.025;
        int index = 1;
        for(int i=0; i<globalDate.getNumberNodeWidth(); i++){
            for(int j=0; j<globalDate.getNumberNodeHight(); j++ ){
                Node node = new Node(index,i*deltaX,j*deltaY,100,globalDate.getHeight(),globalDate.getWidth());
                nodes[index-1] = node;
                //System.out.println(node.toString());
                index++;
            }
        }
    }

    public void buildElements(){

        this.elements = new Element[globalDate.getNumberOfElements()];
        int indexE = 1;
        for(int i=0; i< globalDate.getNumberOfElements(); i++){
            elements[i] = new Element(i + 1);
            if((indexE % globalDate.getNumberNodeHight() == 0) && (i != 0 )) {
                indexE++;
            }
            elements[i].setNodes(
                    nodes[ nodes[indexE-1].getIndex()-1 ],
                    nodes[ nodes[indexE-1].getIndex()-1 + (int)globalDate.getNumberNodeHight() ],
                    nodes[ nodes[indexE-1].getIndex()-1 + (int)globalDate.getNumberNodeHight() + 1],
                    nodes[ nodes[indexE-1].getIndex()-1 + 1 ]);
            indexE++;
            //System.out.println(elements[i].toString());
        }

    }

    public Node[] getNodes() {
        return nodes;
    }

    public Element[] getElements() {
        return elements;
    }
}
