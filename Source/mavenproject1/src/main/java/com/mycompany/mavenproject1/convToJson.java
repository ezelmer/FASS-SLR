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

                String foldPath = "C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\FASS-SLR\\Dataset\\Folds\\";
                if (2 <= k && k <= 23) {
                    foldPath = foldPath + "Fold_1\\";
                }
                if (24 <= k && k <= 45) {
                    foldPath = foldPath + "Fold_2\\";
                }
                if (46 <= k && k <= 67) {
                    foldPath = foldPath + "Fold_3\\";
                }
                if (68 <= k && k <= 89) {
                    foldPath = foldPath + "Fold_4\\";
                }
                if (90 <= k && k <= 112) {
                    foldPath = foldPath + "Fold_5\\";
                }
                foldPath = foldPath+ "Json\\" + k + ".json";

                myFile = new File(foldPath);
                if (myFile.createNewFile()) {
                    System.out.println("File Created: '" + myFile.getName() + "'");
                } else {
                    System.out.println("File '" + myFile.getName() + "' already Exists.");
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
