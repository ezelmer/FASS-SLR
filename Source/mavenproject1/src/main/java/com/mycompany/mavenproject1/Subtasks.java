/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import static com.mycompany.mavenproject1.Main.initialize;
import java.util.ArrayList;
import static com.mycompany.mavenproject1.Main.getHTML;
import static com.mycompany.mavenproject1.Main.grabTag;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;
//problems: 24, 34, 40, 41, 54, 55, 57, 62, 68, 76, 80, 84, 87, 88, 92, 94, 99, 100, 101, 

/**
 *
 * @author ethan zelmer this file is used to create the citation graphs, as well
 * as the list of
 */
public class Subtasks {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        authorsCreatedWorks(2, 112); //create files for 2nd-112th SLR
    }

    /**
     * Create files related to each author's created works, starting at the
     * start-th slr, and ending at the 'end'-th slr
     *
     * @param start index of starting slr
     * @param end index of ending slr.
     */
    public static void authorsCreatedWorks(int start, int end) {
        ArrayList<SLR> slrs = initialize();
        ArrayList<Author> mAuthors = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            System.out.println("Starting work on document " + i);
            ArrayList<Author> authors = new ArrayList<>();
            SLR s = slrs.get(i);
            int curloc = 0;
            int prevloc = 0;
            int count = 0;
            try {
                Scanner reader = new Scanner(new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\FASS-SLR\\Dataset\\Additional_Files\\citationGraph\\" + i + ".txt"));
                String in = "";
                while (reader.hasNextLine()) {
                    in = in + reader.nextLine();
                }
                System.out.println(in);
                if (in.length() > 10) {
                    while (in.indexOf("\"Full Text\":\"", curloc + 1) != -1) {
                        prevloc = curloc + 1;
                        System.out.println("\nTEXT:  ");
                        count++;
                        curloc = in.indexOf("\"Full Text\":\"", prevloc);
                        System.out.println("got curloc" + curloc);
                        String x = in.substring(prevloc, curloc);
                        System.out.println("Got x\n\nx:\n\n" + x);
                        x = x.substring(x.lastIndexOf("\"authors\":["));
                        System.out.println("filtered x");
                        System.out.println(x);

                        int authorloc = 0;
                        while (x.indexOf("firstname\":\"", authorloc) != -1) {
                            authorloc = x.indexOf("firstname\":\"", authorloc);

                            String fn = grabTag(x.substring(authorloc), "firstname\":\"", "\"", true);
                            String ln = grabTag(x.substring(authorloc), "surname\":\"", "\"", true);
                            authorloc++;
                            System.out.println(fn + " " + ln);
                            Author a = new Author();

                            a.firstname = fn;
                            a.surname = ln;
                            if (!authors.contains(a)) {
                                authors.add(a);
                            }
                            if (!mAuthors.contains(a)) {
                                mAuthors.add(a);
                            }
                        }

                    }
                }

            } catch (Exception e) {
                System.out.println(e);
            }
            //file by file writing.
            try {
                String out = "";
                for (int j = 0; j < authors.size(); j++) {
                    System.out.println("Trying to get index " + j);
                    Author a = authors.get(j);
                    try {
                        Thread.sleep(300);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    if (!a.firstname.equals("NULL") && !a.surname.equals("NULL") && !a.firstname.contains("?") && !a.surname.contains("?")) {
                        System.out.println("(" + (j + 1) + " out of " + authors.size() + "). searching for " + a.firstname + " " + a.surname + "'s works:");
                        String base = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=" + a.firstname.replace(" ", "+") + "+" + a.surname.replace(" ", "+") + "[Author]";
                        String ret = getHTML(base);
                        out = out + a.firstname + " " + a.surname + "\n";
                        while (ret.contains("<Id>")) {
                            out = out + grabTag(ret, "Id", false) + "\n";
                            ret = ret.substring(ret.indexOf("<Id") + 1);
                            //  System.out.println(ret);
                        }
                    }

                }
                FileWriter writer = new FileWriter(new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\FASS-SLR\\Dataset\\Additional_Files\\authorWorks\\" + i + ".txt"));

                writer.write(out);
                writer.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("count:" + count);
        }

        //master writing
        try {
            String out = "";
            for (int j = 0; j < mAuthors.size(); j++) {

                System.out.println("Trying to get index " + j);
                Author a = mAuthors.get(j);
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                    System.out.println(e);
                }
                if (!a.firstname.equals("NULL") && !a.surname.equals("NULL") && !a.firstname.contains("?") && !a.surname.contains("?")) {
                    System.out.println("(" + (j + 1) + " out of " + mAuthors.size() + "). searching for " + a.firstname + " " + a.surname + "'s works:");
                    String base = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=" + a.firstname.replace(" ", "+") + "+" + a.surname.replace(" ", "+") + "[Author]";
                    String ret = getHTML(base);
                    out = out + a.firstname + " " + a.surname + "\n";
                    while (ret.contains("<Id>")) {
                        out = out + grabTag(ret, "Id", false) + "\n";
                        ret = ret.substring(ret.indexOf("<Id") + 1);
                        //  System.out.println(ret);
                    }
                }

            }

            FileWriter writer = new FileWriter(new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\FASS-SLR\\Dataset\\Additional_Files\\authorWorks\\master.txt"));
            writer.write(out);
            writer.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void fullTextAndCitations() {

        // TODO code application logic here
        ArrayList<SLR> slrs = initialize();
        int minFTRefs = 999;
        int FTforSLR = 0;
        int slrNum = 0;
        int curlow = -1;
        ArrayList<Integer> retrievals = new ArrayList<>();

        //can't tell what is full text vs just abstract
        for (int i = 2; i <= 112; i++) {
            File f = new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\FASS-SLR\\Dataset\\Additional_Files\\citationGraph\\" + i + ".txt");
            try {
                FileWriter writer = new FileWriter(f);
                SLR s = slrs.get(i);
                ArrayList<String> Depth1 = new ArrayList<>();
                ArrayList<String> Depth2 = new ArrayList<>();
                String output = ("{\"SLR NAME\":\"" + s.name + "\",\"SlR References\":[");
                int numRefsComplete = 0;
                int index = 0;
                System.out.println("Starting work on SLR " + i);
                if (s.references != null) {
                    while (numRefsComplete < 5 && index + 1 < s.references.size()) {
                        System.out.printf("Working on document %d of 5 in SLR %d\n", (numRefsComplete + 1), i);
                        Reference r = s.references.get(index);
                        if (r.idFormat.equals("PMC")) {
                            System.out.println(r.title);
                            r.genCitations(r.id, true);
                            if (r.references.size() > 0) {
                                numRefsComplete++;
                                //r.toJson.substring(1,length-1) + , 
                                r.FT.replaceAll("\"", "'");

                                output = output + "{" + r.toJson().substring(1, r.toJson().length() - 1) + ",\"Full Text\":\"" + r.FT + "\",\"References depth 1\":[";

                                for (Reference rr : r.references) {
                                    if (rr.doi.indexOf("10.") == 0) {
                                        rr.populate();
                                    }
                                    String title = rr.title;
                                    output = output + "{" + rr.toJson().substring(1, rr.toJson().length() - 1) + ",\"References depth 2\":[";
                                    System.out.println("\t" + rr.title);
                                    title = title.replace(" ", "%20");
                                    String url = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&retmode=xml&retmax=1&term=" + title + "&field=title";
                                    String pmcID = (grabTag(getHTML(url), "Id", false));
                                    rr.genCitations(pmcID, false);
                                    for (Reference rrr : rr.references) {
                                        if (rrr.doi.indexOf("10.") == 0) {
                                            rrr.populate();
                                        }
                                        String x = rrr.title;
                                        output = output + "{" + rrr.toJson().substring(1, rrr.toJson().length() - 1) + "},";
                                        System.out.println("\t\t" + x);
                                    }
                                    if (output.charAt(output.length() - 1) == ',') {
                                        output = output.substring(0, output.length() - 1);
                                    }
                                    output = output + "]},";

                                }
                                if (output.charAt(output.length() - 1) == ',') {
                                    output = output.substring(0, output.length() - 1);
                                }
                                output = output + "]},";

                            }
                        }
                        index++; //move on to next doc
                    }
                }
                if (output.charAt(output.length() - 1) == ',') {
                    output = output.substring(0, output.length() - 1);
                }
                output = output + "]}";
                writer.write(output);
                writer.close();
                System.out.println("\n\nDONE SLR " + i + " \n\n");
            } catch (Exception e) {
                System.out.println("ERROR: \n" + e);
            }

            //grab 3, 5 random articles, citation graph depth of 2, and citation network.
        }

        /*
        include only the ones with source_x = "PMC
         */
    }

    public static String formatAsAbstract(String Abstract) {
        for (int i = 0; i < Abstract.length(); i++) {
            if (Abstract.charAt(i) == '.' && !Character.isDigit(Abstract.charAt(i - 1))) {
                Abstract = Abstract.substring(0, i + 1) + '\n' + Abstract.substring(i + 1);
            }
        }

        while (Abstract.contains("<") && Abstract.contains(">")) {

            //  System.out.println(Abstract);
            int x = Abstract.indexOf("<");
            int y = Abstract.indexOf(">", x) + 1;
            if (x != -1 && y != 0) {
                Abstract = Abstract.substring(0, x) + Abstract.substring(y);
            }
        }
        return Abstract;
    }
}
