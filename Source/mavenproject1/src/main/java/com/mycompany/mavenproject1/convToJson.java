/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import static com.mycompany.mavenproject1.Main.getSLRs;
import static com.mycompany.mavenproject1.Main.refFileToDOIs;
import static com.mycompany.mavenproject1.Main.initialize;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 *
 * @author ethan
 */
public class convToJson {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ArrayList<SLR> slrs = initialize();
        // System.out.println(slrs.get(2).references.get(3));
        int k = 2;

        File myFile;

        //Reference r = s.references.get(0);
        //  System.out.println(r.toJson());
        for (k = 2; k < slrs.size(); k++) {
            SLR s = slrs.get(k);
            try {
                myFile = new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\CovidClef2023\\covidClef2023\\Covid_19_Dataset_and_References\\References_JSON\\" + k + ".json");
                if (myFile.createNewFile()) {
                    System.out.println("File Created: '" + myFile.getName() + "'");
                } else {
                    System.out.println("File '" + myFile. getName() + "' already Exists.");
                }

                try {
                    FileWriter writer = new FileWriter(myFile);
                    writer.write(s.toJson());
                    writer.close();

                } catch (Exception e) {
                    System.out.println("Error Writing to file: " + e);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

}
