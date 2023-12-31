/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//asdf

import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.time.LocalDate;
import java.time.Month;
import java.util.StringTokenizer;


/**
 * Populate series of related works for SLR given doi.
 * This main file is meant to be modified to fit your needs. It's currently configured to populate all documents using pubmed.
 * @author ethan
 */
public class Main {

    static int[] RefLoc = {4, 1, 2, 5, 1, 2, 1, 1, 3, 3, 4, 4, 5, 2, 2, 2, 1, 4, 5, 1, 5, 1, 4, 3, 4, 5, 4, 1, 3, 5, 2, 4, 5, 1, 3, 5, 3, 5, 4, 3, 4, 2, 2, 5, 1, 1, 2, 1, 5, 2, 2, 4, 4, 5, 1, 1, 3, 5, 5, 3, 2, 4, 1, 4, 2, 2, 3, 4, 3, 4, 1, 5, 1, 1, 3, 2, 3, 2, 3, 4, 4, 3, 5, 2, 5, 2, 5, 1, 5, 2, 3, 1, 2, 3, 2, 1, 4, 5, 1, 3, 4, 5, 3, 5, 2, 4, 4, 5, 3, 3, 3};
    //specific fold
    public static String ElsevierApiKey = "";
    public static String SpringerApiKey = "";
    public static String CoreApiKey = "";
    public static int searchOffset = 0;// starts at 0
    public static int searchAmt = Math.min(searchOffset + 2 + 113, 113);//ends at 5
   
    /**
     * @param args the command line arguments
     */
    //"C:\Users\ethan\Desktop\2023USRAResearch\CovidClef2023\API KEYS\CoreApiKey.txt"
    public static void main(String[] args) {
        int pmcs = 0;
        int elsevs = 0;
        int springs = 0;
        int crosses = 0;
        int cores = 0;
        int meds = 0;
        int elastics = 0;
        Scanner keyboard = new Scanner(System.in);
        try {
            Scanner coreIn = new Scanner(new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\keys\\CoreApiKey.txt"));
            CoreApiKey = coreIn.nextLine();
            Scanner elsIn = new Scanner(new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\keys\\ElsevierApiKey.txt"));
            ElsevierApiKey = elsIn.nextLine();
            Scanner springerIn = new Scanner(new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\keys\\SpringerApiKey.txt"));
            SpringerApiKey = springerIn.nextLine();
        } catch (FileNotFoundException f) {
            System.out.println("ERROR READING APIS:\n" + f);
        }
        String path = "";
        path = ("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\CovidClef2023\\covidClef2023\\Covid_19_Dataset_and_References\\AdditionalFiles\\6-6-23-ELASTIC.txt");
        ArrayList<String> docs = new ArrayList<>();
        try {
            String contents = Files.readString(Paths.get(path));

            int x = 0;
            int y = 0;
            while (contents.indexOf("s2_id", y) != -1) {
                x = contents.indexOf("s2_id", y);
                if (contents.indexOf("{", x) != -1) {
                    x = contents.indexOf("{", x);
                }

                docs.add(contents.substring(y, x));
                y = x + 1;
            }

        
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(docs.size());

        ArrayList<SLR> slrs = initialize();
        ArrayList<Reference> uniqRefs = new ArrayList<>();
        //pmcPopulate(slrs, searchAmt, searchOffset); //PMC gets 74 in 45 sec
        //elsevierPopulate(slrs, searchAmt, searchOffset); //ELSEVIER gets 20 in 29.8 sec
        //springerPopulate(slrs, searchAmt, searchOffset); //springer gets 10, 50 sec
        // medxrivPopulate(slrs, searchAmt, searchOffset);
        //corePopulate(slrs, searchAmt, searchOffset);
        //     crossrefPopulate(slrs, searchAmt, searchOffset);
        System.out.println("Done searching");
        int ct = 0;
        for (int i = 2; i <= 112; i++) {
            SLR s = slrs.get(i);
            for (int j = 0; j < s.references.size(); j++) {
                Reference r = s.references.get(j);
                if (r.idFormat.equals("PMC") || r.foundApis.contains("PMC")) {
                    System.out.println(i + ": " + r.title);
                    ct++;
                 ///   r.clear();
                }
            }
        }
        System.out.println("\n\n\n\n\n");
        for(int i = 2; i<=112; i++){
            SLR s = slrs.get(i);
            for(int j = 0; j<s.references.size(); j++){
                Reference r = s.references.get(j);
                r.populate();
                //System.out.println(r.miscInfo);
            }
        }
        
        
        System.out.println(ct + " Found");
        //go through each of the references
        

        System.out.println("\n\n\n");
      

      
        System.out.println("FOUND {" + Reference.found + "}" + "out of " + Reference.total + " Referenced documents");
        System.out.printf("DOCUMENT RETRIEVAL BREAKDOWN: \nPMC: %d, ELSEVIER: %d, CORE: %d, MEDRXIV: %d, SPRINGER: %d, CROSSREF: %d, ELASTIC: %d\n%d TOTAL\n", pmcs, elsevs, cores, meds, springs, crosses, elastics, uniqRefs.size());
        System.out.println("Commit above changes?\ny/n");
        String uIn = keyboard.nextLine();

        if (uIn.toLowerCase().charAt(0) == 'y') {
            System.out.println("Committing");
            for (int k = 2 + searchOffset; k < searchAmt; k++) {
                for (int l = 0; l < slrs.get(k).references.size(); l++) {
                    //  System.out.println(k + " " + l + " " + slrs.get(k).references.get(l).title + "ENDTITLE :" + slrs.get(k).references.get(l).foundApis); //print out all references within the scope of your search
                }
                 //   slrs.get(k).dumpData(k); //dump the data of each SLR on the spreadsheet.
            }
        } else {
            System.out.println("Aborting!");
        }

        // System.out.println(r.title + ":\n" + r.Abstract + "\n" + r.dateAccepted+ "\n" + r.authors + "\n" + r.idFormat + " " + r.id + "\n" + r.doi);
        System.out.println("\n\nDONE WITH THAT\n\n");

        //  System.out.println(in.substring(in.indexOf("abstract")));
        //  System.out.println(in.substrsing(in.indexOf("abstract")));
    }

    /**
     * reads SLR document file and creates both SLRs and references accordingly.
     * If data is already present in the slr file, the document is added, and
     * relevant fields are filled in.
     *
     * @return arraylist of SLRS, with their respective arraylists of
     * references. Contains information such as their DOI.
     */
    public static ArrayList<SLR> initialize() {
        ArrayList<SLR> slrs = new ArrayList<SLR>();
        slrs = getSLRs();
        System.out.println("SLRs got");
        for (int j = 2; j <= 112; j++) { //need to put 112 here because of a ghost '113'th' SLR being detected.
            System.out.println("creating reference object " + j + " of " + (slrs.size() - 1));
            slrs.get(j).references = refFileToDOIs(j);
        }
        return slrs;
    }

    /**
     * Populates the current list of SLRs utilizing the PMC database including:
     * Pubmed, PMC
     *
     * @param slrs arraylist of SLRS you wish to populate
     * @param k quantity of documents you wish to populate
     * @param offset offset- where we should 'start' looking.
     */
    public static void pmcPopulate(ArrayList<SLR> slrs, int k, int offset) {
        for (int i = 2 + offset; i < k; i++) {
            System.out.println("Searching for PMCIDs of documents in SLR" + i + " of SLR" + k);
            searchPMCForRefs(slrs, i);
            for (int j = 0; j < slrs.get(i).references.size(); j++) {
                if (slrs.get(i).references.get(j).idFormat.equals("PMC")) {
                    System.out.println("Searching PMC for document " + j + " of SLR " + i);
                    String pmcid = slrs.get(i).references.get(j).id.substring(3);
                    if (pmcid.contains(".")) {
                        pmcid = pmcid.substring(0, pmcid.indexOf("."));
                    }
                    String base = "https://www.ncbi.nlm.nih.gov/pmc/oai/oai.cgi?verb=GetRecord&identifier=oai:pubmedcentral.nih.gov:" + pmcid
                            + "&metadataPrefix=pmc";
                    String in = getHTML(base);
                    //   slrs.get(i).references.get(j).populate(in);//populate
                }
            }
        }
    }

    /**
     * modified version of the pmcpopulate command that function more similarly
     * to how other high level populate methods work.
     *
     * @param slrs
     * @param k
     * @param offset
     */
    public static void pmcPopulatev2(ArrayList<SLR> slrs, int k, int offset) {
        for (int i = 2 + offset; i < k; i++) {
            for (int j = 0; j < slrs.get(i).references.size(); j++) {
                slrs.get(i).references.get(j).populate();
            }
        }
    }

    /**
     * Populates the SLRs arraylist's of References using the Elsevier
     * database.This includes Scopus, ScienceDirect, SciVal, Engineering
     * Village, Embase, Reaxys, PharmaPendium.
     *
     * @param slrs ArrayList of SLR objects. It is assumed that this arraylist
     * was created using the Initialize code to ensure that they are created
     * properly.
     * @param offset the offset used to determine where to start querying.
     * @see initialize
     * @param k the last index of the SLR that should be populated. This is done
     * to prevent excessive compile times, as well as adhere to rate limits from
     * various api providers.
     */
    public static void elsevierPopulate(ArrayList<SLR> slrs, int k, int offset) {
        for (int i = 2 + offset; i < k; i++) {
            for (int j = 0; j < slrs.get(i).references.size(); j++) {
                //if (!slrs.get(i).references.get(j).hasBeenFound) {
                System.out.println("Searching Elsevier for document " + j + " of SLR " + i);
                slrs.get(i).references.get(j).populateElsevier(ElsevierApiKey);
                //  }
            }
        }
    }

    /**
     * Populates the SLRs arraylist's of References using the Elsevier
     * database.This includes Scopus, ScienceDirect, SciVal, Engineering
     * Village, Embase, Reaxys, PharmaPendium.
     *
     * @param slrs
     * @param k
     * @param offset
     */
    public static void medxrivPopulate(ArrayList<SLR> slrs, int k, int offset) {
        for (int i = 2 + offset; i < k; i++) {
            for (int j = 0; j < slrs.get(i).references.size(); j++) {
                //  if (!slrs.get(i).references.get(j).hasBeenFound) {
                System.out.println("Searching MEDXRIV for document " + j + " of SLR " + i);
                slrs.get(i).references.get(j).populateMedrxiv();
                //  }
            }
        }
    }

    /**
     * Populates the SLRs arraylist's of References using the CORE database.
     *
     * @param slrs ArrayList of SLR objects. It is assumed that this arraylist
     * was created using the Initialize code to ensure that they are created
     * properly.
     * @see initialize
     * @param k the last index of the SLR that should be populated. This is done
     * to prevent excessive compile times, as well as adhere to rate limits from
     * various api providers.
     */
    public static void corePopulate(ArrayList<SLR> slrs, int k, int offset) {
        for (int i = 2 + offset; i < k; i++) {
            for (int j = 0; j < slrs.get(i).references.size(); j++) {
                if (!slrs.get(i).references.get(j).hasBeenFound) {
                    System.out.println("Searching Core for document " + j + " of SLR " + i);
                    slrs.get(i).references.get(j).populateCore(CoreApiKey);
                }
            }
        }
    }

    /**
     * Populates the SLRs arraylist's of References using the Springer database.
     *
     * @param slrs ArrayList of SLR objects. It is assumed that this arraylist
     * was created using the Initialize code to ensure that they are created
     * properly.
     * @see initialize
     * @param k the last index of the SLR that should be populated. This is done
     * to prevent excessive compile times, as well as adhere to rate limits from
     * various api providers.
     */
    public static void springerPopulate(ArrayList<SLR> slrs, int k, int offset) {
        for (int i = 2 + offset; i < k; i++) {
            for (int j = 0; j < slrs.get(i).references.size(); j++) {
                //     if (!slrs.get(i).references.get(j).hasBeenFound) {
                System.out.println("Searching Springer for document " + j + " of SLR " + i);
                slrs.get(i).references.get(j).populateSpringer(SpringerApiKey);
                //     }
            }
        }
    }

    /**
     *
     * @param slrs ArrayList of SLR objects. It is assumed that this arraylist
     * was created using the Initialize code to ensure that they are created
     * properly.
     * @see initialize
     * @param k the last index of the SLR that should be populated. This is done
     * to prevent excessive compile times, as well as adhere to rate limits from
     * various api providers.
     * @param offset the offset from the first slr for which should be
     * populated.
     */
    public static void crossrefPopulate(ArrayList<SLR> slrs, int k, int offset) {
        for (int i = 2 + offset; i < k; i++) {
            for (int j = 0; j < slrs.get(i).references.size(); j++) {
                //  if (!slrs.get(i).references.get(j).hasBeenFound) {
                System.out.println("Searching CrossRef for document " + j + " of SLR " + i);
                slrs.get(i).references.get(j).populateCrossref();
                //  }
            }
        }
    }

    /**
     * Populates the SLRs arraylist's of References using the Crossref database
     *
     * @param slrs ArrayList of SLR objects. It is assumed that this arraylist
     * was created using the Initialize code to ensure that they are created
     * properly.
     * @see initialize
     */
    public static void crossrefPopulate(ArrayList<SLR> slrs) {
        crossrefPopulate(slrs, slrs.size(), 0);
    }

    /**
     * Populates the SLRs arraylist's of References using the MedXRIV and
     * BioXRIV database.
     *
     * @param slrs ArrayList of SLR objects. It is assumed that this arraylist
     * was created using the Initialize code to ensure that they are created
     * properly.
     * @see initialize
     */
    public static void medxrivPopulate(ArrayList<SLR> slrs) {
        medxrivPopulate(slrs, slrs.size(), 0);
    }

    /**
     * Populates the SLRs arraylist's of References using the Springer database.
     *
     * @param slrs ArrayList of SLR objects. It is assumed that this arraylist
     * was created using the Initialize code to ensure that they are created
     * properly.
     * @see initialize
     */
    public static void springerPopulate(ArrayList<SLR> slrs) {
        springerPopulate(slrs, slrs.size(), 0);
    }

    /**
     * Populates the SLRs arraylist's of References using the CORE database.
     *
     * @param slrs ArrayList of SLR objects. It is assumed that this arraylist
     * was created using the Initialize code to ensure that they are created
     * properly.
     * @see initialize
     */
    public static void corePopulate(ArrayList<SLR> slrs) {
        corePopulate(slrs, slrs.size(), 0);
    }

    /**
     * Populates all SLRS using PMC. Hopefully will be revised to behave
     * similarly to the other servicePopulate methods.
     *
     * @param slrs list of slrs to be populated.
     */
    public static void pmcPopulate(ArrayList<SLR> slrs) {
        pmcPopulate(slrs, slrs.size(), 0);
    }

    /**
     * Utility method used by various other methods. Example "HellBADDATAo"
     * removeLike("HellBADDATAo", "BAD", 7) returns "Hello"
     *
     * @param in The string you'd like to remove something from
     * @param del The first few characters matching the deletion
     * @param length the length of characters being deleted.
     * @return String with removals applied
     */
    public static String removeLike(String in, String del, int length) {
        while (in.indexOf(del) != -1) {
            int loc = in.indexOf(del);
            in = in.substring(0, loc) + in.substring(loc + length);
        }
        return in;
    }

    /**
     * Converts all DOIs of the j'th SLR's References to PMCIDs, if applicable.
     *
     * @param slrs
     * @param j
     */
    public static void searchPMCForRefs(ArrayList<SLR> slrs, int j) {
        for (int i = 0; i < slrs.get(j).references.size(); i++) {
            //    if (slrs.get(j).references.get(i).id.equals("not found")) {
            String doi = slrs.get(j).references.get(i).doi;
            String PMCID = "NULL";
            if (doi.indexOf("10") == 0) {
                PMCID = doiToPMC(slrs.get(j).references.get(i).doi);
            }
            if (!PMCID.equals("NULL")) {//if we found a pmcID
                slrs.get(j).references.get(i).id = PMCID;
                slrs.get(j).references.get(i).idFormat = "PMC";
            } else {
                slrs.get(j).references.get(i).id = "not found";
                slrs.get(j).references.get(i).idFormat = "N/A";
            }
            //  }
        }
    }

    /**
     *
     * @return List of SLRS, unpopulated, based on the SLR excel file.
     */
    public static ArrayList<SLR> getSLRs() {
        ArrayList<SLR> slrs = new ArrayList<SLR>();
        SLR dummy = new SLR();
        dummy.references = new ArrayList<Reference>();
        slrs.add(dummy);//add a dummy value so that the indices of slrs and the index on the excel sheet line up (ease of use)
        try {
            File myFile = new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\FASS-SLR\\Dataset\\Covid_SLR_Dataset.xlsx");
            FileInputStream file = new FileInputStream(myFile);
            Workbook workbook = new XSSFWorkbook(file);
            DataFormatter df = new DataFormatter();
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();
            int rowTerator = 0;
            while (iterator.hasNext()) {
                Row row = iterator.next();
                Iterator<Cell> cellIterator = row.iterator();
                SLR added = new SLR();
                while (cellIterator.hasNext()) {
                    rowTerator++;
                    Cell cell = cellIterator.next();
                    String cellValue = df.formatCellValue(cell);
                    //System.out.println("\t\tvalue:" + cellValue);
                    try {
                        parseSLRInfo(added, cell, rowTerator);
                    } catch (StringIndexOutOfBoundsException e) {
                        System.out.println("Error parsing SLR info in Main.java, ~line 520:\n" + e);
                    }
                }
                rowTerator = 0;
                slrs.add(added);
            }
            workbook.close();
            file.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return slrs;
    }

    /**
     * Creates references for a given SLR given a SLR XLS file.
     *
     * @param fileID the id of the file, as a number. Files must be stored in
     * this format (#.xlsx)
     * @return an arrayList of references, filled with preliminary information,
     * such as DOI, and Date. NOTE: Date is currently not carried over, this
     * information is instead determined from the PMC entry we retrieve.
     */
    public static ArrayList<Reference> refFileToDOIs(int fileID) {
        ArrayList<Reference> References = new ArrayList<Reference>();
        String foldPath = "C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\FASS-SLR\\Dataset\\Folds\\";
        int specFold = RefLoc[fileID - 2]; //specific fold
        try {
            File referenceFile;

            foldPath = foldPath + "Fold_" + specFold + "\\Excel\\" + fileID + ".xlsx";
            referenceFile = new File(foldPath);

            Workbook workbook = new XSSFWorkbook(referenceFile);
            Sheet sheet = workbook.getSheetAt(0); //only dealing with sheet 0 (main sheet)
            int colTerator = 0;
            int rowTerator = 0;
            DataFormatter df = new DataFormatter();
            Iterator<Row> iterator = sheet.iterator();
            Row row = iterator.next();
            while (iterator.hasNext()) {
                Reference added = new Reference();
                row = iterator.next();
                rowTerator++;
                Iterator<Cell> cellIterator = row.iterator();
                while (cellIterator.hasNext()) {
                    colTerator++;
                    Cell cell = cellIterator.next();
                    String cellValue = df.formatCellValue(cell);
                    try {
                        switch (colTerator) {
                            case 1:
                                added.doi = cellValue;
                                break;
                            case 2:
                                //parse the spreadsheet date and store that as our date (if wanted)
                                break;
                            case 3:
                                if (!cellValue.equals("Unknown Title") && cellValue.length() > 5) {
                                    // System.out.println(fileID + ", " + (rowTerator + 1) + " HAS BEEN FOUND");
                                    added.hasBeenFound = true;
                                    Reference.found++;
                                    added.title = cellValue;
                                } else {
                                    //  System.out.println(fileID + ", " + rowTerator + " HAS NOT BEEN FOUND");
                                }
                                break;
                            case 4:
                                if (added.hasBeenFound) {
                                    added.Abstract = cellValue;
                                }
                                break;
                            case 5:
                                if (added.hasBeenFound) {
                                    if (cellValue.length() > 2) {
                                        StringTokenizer auths = new StringTokenizer(cellValue.substring(cellValue.lastIndexOf("[") + 1, cellValue.indexOf("]")), ",");
                                        while (auths.hasMoreTokens()) {
                                            StringTokenizer bits = new StringTokenizer(auths.nextToken(), "%");
                                            String fn = bits.nextToken();
                                            String ln = bits.nextToken();
                                            String email = bits.nextToken();
                                            Author x = new Author(fn, ln, email);
                                            added.authors.add(x);
                                        }
                                    }
                                }
                                break;
                            case 6:
                                if (added.hasBeenFound) {
                                    added.id = cellValue;
                                }
                                break;
                            case 7:
                                if (added.hasBeenFound) {
                                    added.idFormat = cellValue;
                                }
                                break;
                            case 8:
                                if (added.hasBeenFound && cellValue.length() > 4) {
                                    try {
                                        added.dateAccepted = LocalDate.parse(cellValue);
                                    } catch (Exception e) {
                                        System.out.println("error adding date" + e);
                                    }
                                }
                                break;
                            case 9:
                                if (added.hasBeenFound && cellValue.length() > 2) {
                                    added.foundApis = cellValue;
                                }
                                break;
                            case 10:
                                if (added.hasBeenFound && cellValue.length() > 1) {
                                    added.miscInfo = cellValue;
                                }
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        System.out.println("Inner error while parsing data from excel input on file " + fileID + ":\n" + e);
                    }
                }
                colTerator = 0;
                if (added.doi.length() > 3 && added.doi.indexOf("10.") == 0) {
                    References.add(added);
                    Reference.total++;
                } else {
                    if (added.doi.length() > 3) {
                        System.out.println("Warning: SLR" + fileID + " Reference" + (rowTerator + 1) + " has DOI:" + added.doi + ". As this is not the proper format for a doi, it will not be added. Your spreadsheets may read improperly if this is the case.");
                    }
                }
            }
            workbook.close();
            //System.out.println(fileID + " " + rowTerator);
        } catch (Exception e) {
            System.out.println(e + " on file " + fileID);
        }

        return References;
    }

    /**
     *
     * @param input the input for which we will be grabbing info from
     * @param tag the searched tag
     * @param exact whether or not the tag is 'exact' or needs to be
     * supplemented with &lt, &gt, and /
     * @return information found in the tag.
     */
    public static String grabTag(String input, String tag, boolean exact) {
        return grabTag(input, tag, tag, exact);
    }

    /**
     *
     * @param doi the doi value in format 10.PREFIX/SUFFIX
     * @return the PMC id of the DOI
     */
    public static String doiToPMC(String doi) {
        String base = "https://www.ncbi.nlm.nih.gov/pmc/utils/idconv/v1.0/";
        String in = getHTML(base + "?ids=" + doi);
        return grabTag(in, "pmcid=\"", "\"", true);
    }

    /**
     *
     * @param input the input for which we will be grabbing info from
     * @param opentag the opening tag, With or without {@literal < or >}
     * @param closetag the closing tag, With or without {@literal </ or >}
     * @param exact Specify true to indicate you have put in the exact
     * open/closetag with opentag/closetag character
     * ({@literal<example> / </example>}) otherwise, the tag open/close
     * characters are added.
     * @return the found value.
     *
     */
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

    /**
     * Allows access to the converter api for DOI to PMCs, PMIDs, and other
     * formats.
     *
     * @param doi the doi value in format 10.PREFIX/SUFFIX
     * @return the html output of this text.
     */
    public static String pubmedConvertDOI(String doi) {
        String base = "https://www.ncbi.nlm.nih.gov/pmc/utils/idconv/v1.0/";
        return getHTML(base + "?ids=" + doi);
    }

    /**
     * Basic HTML input method for use with RESTful APIs.
     *
     * @param urlToRead The URL of the API.
     * @return the HTML of the output, in text form
     */
    public static String getHTML(String urlToRead) {
        String result = "error: Some failure in getHTML line\n" + urlToRead;
        String header = "";
        try {
            URL url = new URL(urlToRead);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            result = IOUtils.toString(is, StandardCharsets.UTF_8);
            header = conn.getHeaderFields().toString();
        } catch (java.io.FileNotFoundException e) {
            System.out.println("unable to read url " + urlToRead);
        } catch (Exception e) {

            System.out.println(header);
            System.out.println(e);

        }
        return result;
    }

    /**
     * Parse SLR information as specified in the attached Covid_SLR_Dataset
     * file. This method only works with that document's formatting of data.
     *
     * @param s the given SLR
     * @param c the given cell
     * @param r the column position of the cell.
     */
    public static void parseSLRInfo(SLR s, Cell c, int r) {
        DataFormatter df = new DataFormatter();
        String cellValue = df.formatCellValue(c);
        switch (r) {
            case 1:
                s.name = cellValue;
                break;
            case 2:
                s.link = cellValue;
                break;
            case 3:
             try {
                s.dbSearches = Integer.parseInt(cellValue) + "";
            } catch (Exception e) {
                s.dbSearches = "non int value";
                System.out.println(e);
            }
            break;
            case 4:
                try {
                s.date = LocalDate.parse(cellValue.replace("/", "-"));
            } catch (Exception e) {
                System.out.println("PROBLEM PARSING DATE OF SLR '" + s.name + "':\n" + e);
            }

            break;
            case 5:
                s.refs = cellValue;
                break;
            case 6:
                s.doi = cellValue;
                break;
            case 7:
                s.pmcID = cellValue;
                break;
            case 8:
                s.abs = cellValue;
                break;
            case 9:
                if (cellValue.length() > 2 && cellValue.charAt(0) == '[') {
                    StringTokenizer auths = new StringTokenizer(cellValue.substring(cellValue.lastIndexOf("[") + 1, cellValue.indexOf("]")), ",");
                    while (auths.hasMoreTokens()) {
                        StringTokenizer bits = new StringTokenizer(auths.nextToken(), "%");
                        String fn = bits.nextToken();
                        String ln = bits.nextToken();
                        String email = bits.nextToken();
                        Author x = new Author(fn, ln, email);
                        s.authors.add(x);
                    }
                }
                break;
            case 10:
                s.publisher = cellValue;
                break;
            case 11:
                String in = cellValue;
                in = in.replace("\n\n\n", "\n");
                String[] titlequeries = in.split("\n\\(", 0);
                for (String q : titlequeries) {
                    if (q.length() > 3) {
                        while (q.charAt(q.length() - 1) == '\n') {
                            q = q.substring(0, q.length() - 1);
                        }
                    }
                }
                // StringTokenizer indivQueries = new StringTokenizer(in, "\n(");
                s.gptTitleQueries.add(titlequeries[0]);
                for (int i = 1; i < titlequeries.length; i++) {
                    String x = titlequeries[i];
                    s.gptTitleQueries.add("(" + x);
                }

                break;
            case 12:
                break;
            case 13:
                String inTAb = cellValue;
                inTAb = inTAb.replace("\n\n\n", "\n");

                String[] titleABqueries = inTAb.split("\n\\(", 0);
                for (String q : titleABqueries) {
                    if (q.length() > 3) {
                        while (q.charAt(q.length() - 1) == '\n') {
                            q = q.substring(0, q.length() - 1);
                        }
                    }
                }
                s.gptTAbQueries.add(titleABqueries[0]);
                for (int i = 1; i < titleABqueries.length; i++) {
                    String x = titleABqueries[i];
                    s.gptTAbQueries.add("(" + x);
                }
                break;
                
            case 14:
                break;
            case 15:
                 String inGPT = cellValue;
                inGPT = inGPT.replace("\n\n\n", "\n");

                String[] GPTqueries = inGPT.split("\n\\(", 0);
                for (String q : GPTqueries) {
                    if (q.length() > 3) {
                        while (q.charAt(q.length() - 1) == '\n') {
                            q = q.substring(0, q.length() - 1);
                        }
                    }
                }
                s.gptWRefs.add(GPTqueries[0]);
                for (int i = 1; i < GPTqueries.length; i++) {
                    String x = GPTqueries[i];
                    s.gptWRefs.add("(" + x);
                }
                break;
            default:
                break;
        }

    }

}
