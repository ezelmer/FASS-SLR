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
        myFile = new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\CovidClef2023\\covidClef2023\\Covid_19_Dataset_and_References\\qrel.txt");

        int topic = 1;
        int iteration = 0;
        int relevancy = 1;
        try {
            FileWriter writer = new FileWriter(myFile);
            for (k = 2; k < slrs.size(); k++) {
                SLR s = slrs.get(k);
                for (Reference r : s.references) {
                    String z = String.format("%d %d %50s %d\n", k, iteration, r.doi, relevancy);
                    System.out.println(r.doi);
                    writer.write(z);
                }
                System.out.println("^");
            }
            writer.close();

        } catch (IOException e) {

        } catch (Exception e) {

        }
    }

}
