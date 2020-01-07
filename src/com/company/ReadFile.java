package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadFile {
    public double[] readDataFromFile(){

        double []data = new double[4];
        int i = 0;
        File file = new File("/Users/dominikstrama/Desktop/University/5 semester/Metoda elementów skończonych/Piotr Kustra/MES/src/com/company/mes.txt");

        try{
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextDouble()){
                double number = scanner.nextDouble();
                data[i] = number;
                i++;
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        return data;
    }
}
