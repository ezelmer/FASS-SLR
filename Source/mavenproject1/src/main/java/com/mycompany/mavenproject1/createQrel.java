/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import static com.mycompany.mavenproject1.Main.getSLRs;
import static com.mycompany.mavenproject1.Main.refFileToDOIs;
import static com.mycompany.mavenproject1.Main.initialize;
import static com.mycompany.mavenproject1.Main.RefLoc;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author ethan
 */
public class createQrel {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<SLR> slrs = initialize();
        // System.out.println(slrs.get(2).references.get(3));
        int k = 2;

        File myFile;
        File myFileMod;
        /*
        File file1;
        File file2;
        File file3;
        File file4;
        File file5;
         */
        myFile = new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\FASS-SLR\\Dataset\\Qrel\\qrel.txt");
        myFileMod = new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\FASS-SLR\\Dataset\\Qrel\\qrelMod.txt");
        /*
        file1 = new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\FASS-SLR\\Dataset\\Qrel\\fold_1.txt");
        file2 = new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\FASS-SLR\\Dataset\\Qrel\\fold_2.txt");
        file3 = new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\FASS-SLR\\Dataset\\Qrel\\fold_3.txt");
        file4 = new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\FASS-SLR\\Dataset\\Qrel\\fold_4.txt");
        file5 = new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\FASS-SLR\\Dataset\\Qrel\\fold_5.txt");
         */
        String[] folds = {"","","","",""};
        int topic = 1;
        int iteration = 0;
        int relevancy = 1;
        try {
            FileWriter writer = new FileWriter(myFile);
            FileWriter writermod = new FileWriter(myFileMod);
            for (k = 2; k < slrs.size(); k++) {
                SLR s = slrs.get(k);
                if (s.references != null) {
                    for (Reference r : s.references) {
                        String z = String.format("%d %d %50s %d\n", k, iteration, r.doi, relevancy);
                        String zMod = String.format("%d %d %s %d\n", k, iteration, r.doi, relevancy);
                        System.out.println(r.doi);
                        writer.write(z);
                        writermod.write(zMod);
                        folds[RefLoc[k - 2] - 1] = folds[RefLoc[k - 2] - 1] + z;
                        
                    }
                }
                System.out.println("^");
            }
            
            writer.close();
            writermod.close();
           
            for (int i = 0; i < 5; i++) {
                System.out.println(i);
                myFile = new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\FASS-SLR\\Dataset\\Qrel\\fold_" + (i + 1) + ".txt");
                writer = new FileWriter(myFile);
                writer.write(folds[i]);
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("ERROR" + e);
        } catch (Exception e) {
            System.out.println("ERROR" + e);
        }
    }

}
