/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.Misc;

import static com.mycompany.mavenproject1.Main.initialize;
import static com.mycompany.mavenproject1.Main.getHTML;
import static com.mycompany.mavenproject1.Main.grabTag;
import com.mycompany.mavenproject1.SLR;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author ethan
 */
public class chatGPTRequestFormatter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //execute all queries and save information.
        ArrayList<SLR> slrs = initialize(); 
        String tab = "";
        String t = "";
        String tabr = "";
        String base = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=";
        for (int k = 2; k <= 112; k++) {
            SLR s = slrs.get(k);
            tab = executeQueries(s.gptTAbQueries);
            t = executeQueries(s.gptTitleQueries);
            tabr = executeQueries(s.gptWRefs);
            System.out.printf("tab: %s\nt: %s\ntabr: %s\n", tab, t, tabr);
            String path = "C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\FASS-SLR\\Baselines\\Runs\\Pubmed\\ChatGPT_Retrievals\\";
            String tpath = path + "Title\\";
            String tabpath = path + "TitleAbstract\\";
            String tabrpath = path + "TitleAbstractRelated\\";
            try {
                FileWriter tW = new FileWriter(tpath + k + ".txt");
                tW.write(t);
                tW.close();
            } catch (Exception e) {
                System.out.println("Exception writing to file" + k + " in title folder");
            }
            try {
                FileWriter tW = new FileWriter(tabpath + k + ".txt");
                tW.write(tab);
                tW.close();
            } catch (Exception e) {
                System.out.println("Exception writing to file" + k + " in title abstrac folder");
            }
            try {
                FileWriter tW = new FileWriter(tabrpath + k + ".txt");
                tW.write(tabr);
                tW.close();
            } catch (Exception e) {
                System.out.println("Exception writing to file" + k + " in title abstract related folder");
            }

        }
        System.out.println("\n\n");

    }

    
    /**
     * Performs the PUBMED queries based on an arraylist of queries in string format.
     * @param queries in pubmed format. 
     * @return String of pmids with a \n character to separate them.
     */
    public static String executeQueries(ArrayList<String> queries) {
        String output = "";
        String base = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=";
        for (int i = 0; i < queries.size(); i++) {
            String query = queries.get(i);
            query = query.replace(" ", "+");
            if (query.length() > 10) {
                String url = base + query;
                String out = getHTML(url);
                while (out.contains("<Id>")) {
                    output += grabTag(out, "Id", false) + "\n";
                    out = out.substring(out.indexOf("<Id>") + 4);
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        return output;
    }

    public static void createChatGPTPrompt() {
        ArrayList<SLR> slrs = initialize();
        for (int i = 2; i <= 112; i++) {
            SLR s = slrs.get(i);
            String filepath = "C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\FASS-SLR\\Dataset\\Additional_Files\\citationGraph\\" + i + ".txt";
            File file = new File(filepath);
            Scanner fr = new Scanner(System.in);
            try {
                fr = new Scanner(file);
            } catch (Exception e) {
                System.out.println(e);
            }
            String in = "";
            while (fr.hasNextLine()) {
                in += (fr.nextLine());
            }
            if (in.length() > 5) {

                String srch = "\"SLR NAME\":\"";
                int nameIndex = in.indexOf(srch) + srch.length();

                String slrname = (in.substring(nameIndex, in.indexOf("\",\"", nameIndex)));

                int ftIndex = 0;

                int titleIndex = 0;
                int absIndex = 0;
                System.out.println("\n\nBased on the following SLR title, abstract, as well as it's 1-5 related works, please provide 10 complex pubmed Entrez formatted queries without descriptions, in plain text, such that they may be used directly on Pubmed's website.\nSLR" + i + ")");
                System.out.println("SLR TITLE:" + s.name.replace("\n", "") + "\nSLR ABSTRACT:" + s.abs.replace("\n", ""));
                for (int j = 0; j < 5; j++) {
                    try {
                        ftIndex = in.indexOf("Full Text", ftIndex + 1);
                        titleIndex = in.substring(0, ftIndex).lastIndexOf("title\":\"") + "title\":\"".length();

                        absIndex = in.substring(0, ftIndex).lastIndexOf("abstract\":\"") + +"abstract\":\"".length();
                        String title = in.substring(titleIndex, in.indexOf("\",\"", titleIndex));
                        String abs = in.substring(absIndex, in.indexOf("\",\"", absIndex));
                        System.out.println("title" + (j + 1) + ": " + title + "\nAbstract" + (j + 1) + ":  " + abs + "");
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }

            }
        }

    }

}
