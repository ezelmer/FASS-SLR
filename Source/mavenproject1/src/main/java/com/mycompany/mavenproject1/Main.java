/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

/*
july 8th deadline

 */
/**
 * Project is prepare a dataset. ML can learn from this dataset, to be able to
 * search biomedical texts BM25 algorithm, LM algorithm Prepare the
 * data/dataset.
 *
 * Learn when someone is searching, what the results should be. We will learn
 * more
 *
 * Trec EVal QREL
 *
 */
//show where the crossref data comes from
/**
 *
 * @author ethan
 */
public class Main {

    public static String ElsevierApiKey = "";
    public static String SpringerApiKey = "";
    public static String CoreApiKey = "";
    public static int searchOffset = 0;// starts at 0
    public static int searchAmt = Math.min(searchOffset + 2 + 113, 113);//ends at 5
    // have 97.6 of references, missing 55.

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
            Scanner coreIn = new Scanner(new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\CovidClef2023\\keys\\CoreApiKey.txt"));
            CoreApiKey = coreIn.nextLine();
            Scanner elsIn = new Scanner(new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\CovidClef2023\\keys\\ElsevierApiKey.txt"));
            ElsevierApiKey = elsIn.nextLine();
            Scanner springerIn = new Scanner(new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\CovidClef2023\\keys\\SpringerApiKey.txt"));
            SpringerApiKey = springerIn.nextLine();
        } catch (FileNotFoundException f) {
            System.out.println("ERROR READING APIS:\n" + f);
        }

        String path = ("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\CovidClef2023\\covidClef2023\\Covid_19_Dataset_and_References\\AdditionalFiles\\6-6-23-ELASTIC.txt");
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

            // System.out.println(docs.get(docs.size()-1));
            //System.out.println("\n\n"+contents.substring(x));
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(docs.size());

        // TODO code application logic here
        ArrayList<SLR> slrs = initialize();
        ArrayList<Reference> uniqRefs = new ArrayList<>();
        //pmcPopulate(slrs, searchAmt, searchOffset); //PMC gets 74 in 45 sec
        //elsevierPopulate(slrs, searchAmt, searchOffset); //ELSEVIER gets 20 in 29.8 sec
        //springerPopulate(slrs, searchAmt, searchOffset); //springer gets 10, 50 sec
        // medxrivPopulate(slrs, searchAmt, searchOffset);
        //corePopulate(slrs, searchAmt, searchOffset);
        //     crossrefPopulate(slrs, searchAmt, searchOffset);
        System.out.println("Done searching");

        //go through each of the references
        int i = 0;
        int j = 0;
        int count = 0;
        int scount = 0;
        ArrayList<String> dois = new ArrayList<String>();
        for (SLR s : slrs) {
            if (s.references != null) {
                for (Reference r : s.references) {
                    j++;
                    if (!dois.contains(r.doi)) {
                        dois.add(r.doi);
                        uniqRefs.add(r);
                    }

                    count++;
                    //System.out.println("\n\n\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    //  System.out.println(r.doi);
                    // if (r.id.equals("not found")) {
                    // System.out.println("\n\nOLD^, new below, \n\n");
                    // scount++;

                    System.out.println("HAVE NOT FOUND: SLR" + i + ", REF:" + (j + 1) + ": " + r.doi);

                    if (!r.title.equals("Unknown Title")) {
                        scount++;
                    }
                    //  System.out.println(r.title + ":\n" + r.Abstract + "\n" + r.dateAccepted + "\n" + r.authors + "\n" + r.idFormat + " " + r.id + "\n" + r.doi);
                    System.out.println("\n\n");
                    //  }

                }
            }
            i++;
            j = 0;
        }

        for (Reference r : uniqRefs) {
            if (r.foundApis.contains("PMC") || r.idFormat.equals("PMC")) {
                pmcs++;
            }
            if (r.foundApis.contains("elsevier") || r.idFormat.equals("elsevier_pii")) {
                elsevs++;
            }
            if (r.foundApis.contains("Core") || r.idFormat.equals("CORE")) {
                cores++;
            }
            if (r.foundApis.contains("MedBiorxiv") || r.idFormat.equals("medrxiv/biorxiv doi")) {
                meds++;
            }
            if (r.foundApis.contains("Springer") || r.idFormat.equals("Springer")) {
                springs++;
            }
            if (r.foundApis.contains("CROSSREF") || r.idFormat.equals("CROSSREF")) {
                crosses++;
            }
            if (r.idFormat.equals("ELASTIC")) {
                elastics++;
            }
        }

        System.out.println("\n\n\n");
        System.out.println(slrs.get(21).doi + "\n" + slrs.get(21).pmcID + "\n" + slrs.get(21).abs);

        // System.out.println("COUNT: " + scount + " OF " + count);
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
                //    slrs.get(k).dumpData(k); //dump the data of each SLR on the spreadsheet.
            }
        } else {
            System.out.println("Aborting!");
        }
        System.out.println("TOTAL UNIQUE DOCUMENTS:" + dois.size());
        for (String doi : dois) {
            System.out.println(doi);
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
        for (int j = 2; j < slrs.size(); j++) {
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
            File myFile = new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\CovidClef2023\\covidClef2023\\Covid_19_Dataset_and_References\\Covid_SLR_Dataset.xlsx");
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
                    parseSLRInfo(added, cell, rowTerator);
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
        try {
            File referenceFile = new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\CovidClef2023\\covidClef2023\\Covid_19_Dataset_and_References\\References\\" + fileID + ".xlsx");
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
                                    try{
                                    added.dateAccepted = LocalDate.parse(cellValue);
                                    }catch(Exception e){
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

                break;
            default:
                break;
        }

    }

}
