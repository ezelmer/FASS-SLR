/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.Misc;

import java.io.File;
import java.io.FileWriter;

/**
 *
 * @author ethan
 */
public class SLRNumGen {

    /**
     * @param args the command line arguments
     */
    //'renumber' the slrs such that their numbering be random. 
    //from there, categorize them into  5 folds based off of their new numbering.
    public static void main(String[] args) {
        String out = "";
        boolean[] vals = new boolean[111];
        int[] folds = {0, 0, 0, 0, 0};
        for (int i = 2; i <= 112; i++) {

            int x = ((int) (Math.random() * 111.0)) + 2;
            while (vals[x - 2] == true) {
                x = ((int) (Math.random() * 111.0)) + 2;
            }
            vals[x - 2] = true;
            //System.out.println(x);

            out = out + (i + "," + x);
            if (2 <= x && x <= 23) {
                out = out + ",Fold_1\n";
                folds[0]++;
            }
            if (24 <= x && x <= 45) {
                out = out + ",Fold_2\n";
                folds[1]++;
            }
            if (46 <= x && x <= 67) {
                out = out + ",Fold_3\n";
                folds[2]++;
            }
            if (68 <= x && x <= 89) {
                out = out + ",Fold_4\n";
                folds[3]++;
            }
            if (90 <= x && x <= 112) {
                out = out + ",Fold_5\n";
                folds[4]++;
            }
        }

        System.out.println(out);
        try {
           //FileWriter writer = new FileWriter(new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\FASS-SLR\\Dataset\\Folds\\master\\SLRMapping.txt"));
           // writer.write(out);
           // writer.close();
        } catch (Exception e) {
            System.out.println("Problem writing file:\n" + e);
        }
        for (int x : folds) {
            System.out.println(x);
        }

    }

}
