/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.Misc;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author ethan
 */
public class filterPubmedFromElastic {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
 String path = "C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\FASS-SLR\\Baselines\\Runs\\Cord-19\\LM Retrievals\\lmtabWithSource.txt";
       filter(path);
    }

    public static void filter(String path) {
        File fin = new File(path);
        String filename = path.substring(path.lastIndexOf("\\") + 1);
        filename = filename.replace(".txt", "_FILTERED.txt");
        String outpath = path.substring(0, path.lastIndexOf("\\")) + "\\";
        try {
            Scanner in = new Scanner(fin);
            String newOutput = "";
            while (in.hasNextLine()) {
                String line = in.nextLine();
                StringTokenizer tk = new StringTokenizer(line, " ");
                int ct = 0;
                while (tk.hasMoreTokens()) {
                    ct++;
                    String token = tk.nextToken();
                    String slrnum = "";
                    String good = "";
                    String doi = "";
                    String rank = "";
                    String sources = "";
                    switch (ct) {
                        case 1:
                            slrnum += token;
                            break;
                        case 2:
                            good += token;
                            break;
                        case 3:
                            doi += token;
                            break;
                        case 4:
                            rank += token;
                            break;
                        default:
                            sources += token;
                            break;
                    }
                }
                if (line.contains("PMC")) {
                    newOutput = newOutput + line + "\n";
                }
            }
            // System.out.println(newOutput);
            //System.out.println(outpath);
            // System.out.println(filename);
            File f = new File(outpath + filename);
            FileWriter writer = new FileWriter(f);
            writer.write(newOutput);
            writer.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
