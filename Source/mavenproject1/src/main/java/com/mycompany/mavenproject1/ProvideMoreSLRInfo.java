/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import static com.mycompany.mavenproject1.Main.getSLRs;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author ethan
 */
public class ProvideMoreSLRInfo {

    static ArrayList<String> strList;

    /**
     * @param args the command line arguments
     */
    
    //this code returned all but 19 SLR's information, the remainder of which had to be done manually
    public static void main(String[] args) {
        ArrayList<SLR> slrs;
        slrs = getSLRs();
        int i = 2;
        String fout = "";
        for (i = 2; i < slrs.size(); i++) {
            SLR s = slrs.get(i);
            Reference r = new Reference();
            if ((s.pmcID != null)) {
                if (!s.pmcID.contains("N")) {
                    System.out.println(i + ", " + s.pmcID);
                    String base = "https://www.ncbi.nlm.nih.gov/pmc/oai/oai.cgi?verb=GetRecord&identifier=oai:pubmedcentral.nih.gov:" + s.pmcID
                            + "&metadataPrefix=pmc";
                    System.out.println(base);
                    String in = getHTML(base);
                    r.populate(in);
                    s.abs = r.Abstract;
                    s.authors = r.authors;
                    s.publisher = r.publisherName;

                    System.out.println(i + ":\n " + r.Abstract);
                    System.out.println("\n\n\n\n");

                }
            }
        }
        System.out.println("\n\n\nAUTHORS:");

        for (i = 2; i < slrs.size(); i++) {
            if (slrs.get(i).authors != null) {
                if (slrs.get(i).authors.size() > 1) {
                    System.out.println(i + ":\n " + slrs.get(i).authors);
                }
            }
        }
        DumpSLRData(slrs);
        System.out.println("\n\n\n");
        // System.out.println(fout);
        String pmcID = slrs.get(4).pmcID;
        System.out.println("\n\n" + pmcID);
        String base = "https://www.ncbi.nlm.nih.gov/pmc/oai/oai.cgi?verb=GetRecord&identifier=oai:pubmedcentral.nih.gov:" + pmcID
                + "&metadataPrefix=pmc";
        String in = getHTML(base);
        //System.out.println(in);

    }

    public static String getHTML(String urlToRead) {
        String result = "error: Some failure in getHTML line\n" + urlToRead;
        try {
            URL url = new URL(urlToRead);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            result = IOUtils.toString(is, StandardCharsets.UTF_8);

        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    public static String BioC_PMC(String format, String ID, String encoding) {
        String base = "https://www.ncbi.nlm.nih.gov/research/bionlp/RESTful/pmcoa.cgi/BioC_";

        encoding = encoding.toLowerCase();
        String url = base + format + "/" + ID + "/" + encoding;
        return getHTML(url);

    }

    public static String ESearch(String db, String query, boolean exact) {
        String base = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/";

        if (exact) {
            // getHTML()
        } else {
            //parse the string query manually.

            // System.out.println(query);
        }
        query = query.replaceAll(" ", "%20");
        query = query.replaceAll("\n", "");
        String url = base + "esearch.fcgi?db=" + db + "&term=\"" + query + "\"&usehistory=y&retmax=1&sort=relevance";
        query = query.replaceAll("%5B", "[");
        query = query.replaceAll("%5D", "]");
        query = query.replaceAll("%20", " ");
        query = query.replaceAll("\\+", " ");
        System.out.println("Query:\n" + query);
        System.out.println("\n\n" + url + "\n\n");
        //System.out.println("\n\nTHEURL:" + url + "\n\n");
        //System.out.println(getHTML(url));
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println(e);
        }
        return getHTML(url);
    }

    public static String SearchToIds(String input) {
        String id;

        String output = "";
        String added = "";
        int curIndex = 0;
        while (input.indexOf("</Id>") != -1) {
            curIndex = input.indexOf("</Id>") + 5; //9 = length of <Id> + length of </Id>
            // System.out.println("curIndex: " + curIndex);
            added = grabTag(input, "Id", false);

            output = output + added + ",";
            // System.out.println(grabTag(input, "Id"));
            if (curIndex <= input.length()) {
                input = input.substring(curIndex);
            } else {

                curIndex = -1;
            }
            //System.out.println(" input after grabbing tag " + input + "    \n");
        }
        if (output.length() > 0) {
            output = output.substring(0, output.length() - 1);

        }
        return output;

    }

    public static String grabTag(String input, String tag, boolean exact) {
        return grabTag(input, tag, tag, exact);
    }

    public static String grabTag(String input, String opentag, String closetag, boolean exact) {
        if (!exact) {
            opentag = "<" + opentag + ">";
            closetag = "</" + closetag + ">";
        }
        int x = input.indexOf(opentag);
        int y = input.indexOf((closetag), x + opentag.length());
        String output = "NULL";
        if (x != -1 && y != -1) {
            output = input.substring(x + (opentag).length(), y);
        } else {
        }
        if (x == -1 || y == -1) {
        }
        return output;
    }

    public static String PMCtoDOI(String pmc) {
        String base = "https://www.ncbi.nlm.nih.gov/pmc/utils/idconv/v1.0/";
        String in = getHTML(base + "?ids=" + pmc);
        // System.out.println(in);
        return grabTag(in, "doi=\"", "\"", true);
    }

    public static String doiToPMC(String doi) {
        String base = "https://www.ncbi.nlm.nih.gov/pmc/utils/idconv/v1.0/";
        String in = getHTML(base + "?ids=" + doi);
        return grabTag(in, "pmcid=\"", "\"", true);
    }

    public static void DumpSLRData(ArrayList<SLR> slrs) {
        try {
            File myFile = new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\CovidClef2023\\covidClef2023\\Covid_19_Dataset_and_References\\Covid_SLR_Dataset.xlsx");

            FileInputStream file;

            file = new FileInputStream(myFile);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet spreadsheet = workbook.getSheetAt(0);
            XSSFRow row;
            Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
            data.put(1, new Object[]{"Abstract", "Authors", "Publisher"});
            for (int i = 2; i < slrs.size(); i++) {

                SLR s = slrs.get(i);
                
                data.put(i, new Object[]{s.abs, s.authors.toString(), s.publisher});

                // }
            }
            Set<Integer> keyid = data.keySet();
            int rowid = 0; // row number, row 1 = 0

            for (int key : keyid) {
                // System.out.println(key);
                row = spreadsheet.getRow(rowid);
                rowid++;
                Object[] objectArr = data.get(key);
                int cellid = 7; // column number,  A = 0
                for (Object obj : objectArr) {
                    Cell cell = row.createCell(cellid++);
                    try {
                        cell.setCellValue((String) (obj));
                    } catch (Exception e) {
                        System.out.println(e + "\n\nHERES THE ERROR STRING:" + (String) obj);
                    }
                }
            }

            FileOutputStream out = new FileOutputStream(myFile);
            workbook.write(out);
            workbook.close();
            out.close();
        } catch (Exception e) {
            System.out.println("ERROR DUMPING SLR METADATA:\n" + e);
        }
    }

}
