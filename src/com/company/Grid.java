package com.company;

import java.util.Arrays;

public class Grid {

    GlobalDate globalDate = new GlobalDate();
    private Node[] nodes;
    private Element[] elements;

    public Grid() {
        buildNodes();
        buildElements();
        // todo: musimy stworzyc tablice 4 nodów i przypisywać je do elementu
    }

    public void buildNodes(){
        this.nodes = new Node[(int)globalDate.getNumberOfNodes() +1];
        double deltaX = (globalDate.getWidth()) / (globalDate.getNumberNodeWidth() -1);
        double deltaY = (globalDate.getHeight()) / (globalDate.getNumberNodeHight() -1);
        int index = 1;
        for(int i=0; i<globalDate.getNumberNodeWidth(); i++){
            for(int j=0; j<globalDate.getNumberNodeHight(); j++ ){
                Node node = new Node(index,i*deltaX,j*deltaY,0,globalDate.getHeight(),globalDate.getWidth());
                nodes[index] = node;
                System.out.println(node.toString());
                index++;
            }
        }
    }

    public Node[] getNodes() {
        return nodes;
    }

    public Element[] getElements() {
        return elements;
    }

    public void buildElements(){
        this.elements = new Element[(int)globalDate.getNumberOfElements() +1];
        int indexE = 1;
        for(int i=0; i< globalDate.getNumberOfElements(); i++){
            elements[i] = new Element(i+1);
            elements[i].setNodes(
                    nodes[ nodes[indexE].getIndex() ],
                    nodes[ nodes[indexE].getIndex() + (int)globalDate.getNumberNodeHight() ],
                    nodes[ nodes[indexE].getIndex() + (int)globalDate.getNumberNodeHight() + 1],
                    nodes[ nodes[indexE].getIndex() + 1 ]);
            indexE++;
            if(indexE % globalDate.getNumberNodeHight() == 0 ) {
                indexE++;
            }
            System.out.println(elements[i].toString());

        }
    }
}
