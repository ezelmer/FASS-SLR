/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author ethan
 */
public class SLR {

    public static int IDGen = 2;
    public String name;
    public int id;
    public String link;
    public String dbSearches;
    public String refs;
    public ArrayList<Reference> references;
    public LocalDate date;
    public String doi;
    public String pmcID;
    public String abs;
    public String publisher;
    ArrayList<Author> authors; //Arraylist of Author objects.

    public SLR() {
        authors = new ArrayList<>();
        name = "";
        link = "";
        this.id = IDGen;
        IDGen++;
        date = LocalDate.EPOCH;
    }

    public SLR(String name, String link) {
        this.name = name;
        this.link = link;
        this.id = IDGen;
        IDGen++;
    }

    public void dumpData(int k) {
        /*
        for (int i = 0; i < references.size(); i++) {
            Reference r = references.get(i);
            System.out.println("\n{" + k + ":\n " + r.title + "\n" + r.Abstract + "\n" + r.id + "\n" + r.idFormat + "\n" + r.dateAccepted + "\n" + "}");
        }
         */

        //
        //
        try {
            File myFile = new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\CovidClef2023\\covidClef2023\\Covid_19_Dataset_and_References\\References\\" + k + ".xlsx");

            FileInputStream file;

            file = new FileInputStream(myFile);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet spreadsheet = workbook.getSheetAt(0);
            XSSFRow row;
            Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
            data.put(1, new Object[]{"Title", "Abstract", "Authors", "ID", "ID Format", "Date Accepted", "Other found locations", "Misc. Data"});

            for (int i = 0; i < this.references.size(); i++) {

                Reference r = this.references.get(i);

                // String x = (i + 2) + "";
                //   System.out.println(x + "  " + r.title + " " + r.doi + " " + r.dateAccepted);
                //THIS IS AN IMPORTANT LINE!
                //if(!r.hasBeenFound){ //if it wasn't previously found
                data.put(i + 2, new Object[]{r.title, r.Abstract, r.authors.toString(), r.id, r.idFormat, r.dateAccepted.toString(), r.foundApis, r.miscInfo});

                // }
            }
            System.out.println("\n" + k + " THE SIZE: " + data.size() + "\n\n");
            Set<Integer> keyid = data.keySet();
            int rowid = 0; // row number, row 1 = 0

            for (int key : keyid) {
                // System.out.println(key);
                row = spreadsheet.getRow(rowid);
                rowid++;
                Object[] objectArr = data.get(key);
                int cellid = 2; // column number,  A = 0
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
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    

    public String toString() {
        return "TITLE: " + this.name + " \nLINK: " + this.link + "\nREFERENCES: " + this.references;
    }

    /**
     * returns the information about the slr in json format
     *
     * @return json formatted text.s
     */
    public String toJson() {
        SLR s = this;
        File myFile;

        //Reference r = s.references.get(0);
        //  System.out.println(r.toJson());
        String txt = ("{" + "\"name\":\"" + s.name + "\",\"id\":\"" + s.id + "\",\"link\":\"" + s.link + "\",\"dbsearches\":\"" + s.dbSearches + "\",\"refs\":\"" + s.refs + "\",\"references\":[");
        for (int i = 0; i < s.references.size(); i++) {
            Reference z = s.references.get(i);
            if (i != s.references.size() - 1) {
                txt = txt + z.toJson() + ",";
            } else {
                txt = txt + z.toJson();
            }
        }
        txt = txt + ("]}");
        return txt;
    }
}
