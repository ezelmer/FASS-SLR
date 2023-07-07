
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.apache.commons.io.IOUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ethan
 */
public class MainDated {

    /**
     * @param args the command line arguments
     */
    /**
     * @param args the command line arguments
     */
    static ArrayList<String> strList;

    public static void main(String[] args) {
        String filepath = "C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\FASS-SLR\\Source\\Utility_Files\\slrTitles.txt";
        BufferedReader reader;
        BufferedReader dReader;
        ArrayList<String> titleNames = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(filepath));
            dReader = new BufferedReader(new FileReader("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\FASS-SLR\\Source\\Utility_Files\\slrDates.txt"));
            String x = reader.readLine();
            String date = dReader.readLine();
            while (x != null) {
                titleNames.add(x);
                x = reader.readLine();
            }
            while (date != null) {
                dates.add(date);
                date = dReader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        for (String title : dates) {
            System.out.println(title);
        }
        String[] badWords = {"the", "in", "and", "with", "from", "for", "or", "to", "a", "of"};
        strList = new ArrayList<>();
        for (String word : badWords) {
            strList.add(word); //for each of the bad words, add it to the list of Strings exempt from queries.
        }
        // TODO code application logic here
        int i = 2;
        for (String titles : titleNames) {
            System.out.println(titles);
            StringTokenizer tk = new StringTokenizer(titles, ",");
            String output = "";
            while (tk.hasMoreTokens()) {
                String title = tk.nextToken();
                System.out.println(title);
                String x = ((SearchToIds(ESearch("pmc", title, false, dates.get(i - 2)))));
                StringTokenizer tx = new StringTokenizer(x, ",");

                while (tx.hasMoreTokens()) {
                    output = output + i + " 0 " + (PMCtoDOI("pmc" + tx.nextToken())) + "\n";
                }

                try {
                   // File outputFile = new File("C:\\Users\\ethan\\Desktop\\2023USRAResearch\\FASS-SLR\\FASS-SLR\\Baselines\\Runs\\Pubmed\\PubmedRetrievals\\Dated\\dated" + i + ".txt");
                    //  outputFile.createNewFile();
                    //  FileWriter myWriter = new FileWriter(outputFile);
                    //  myWriter.write(output);
                    //  myWriter.close();
                } catch (Exception e) {
                    System.out.println("Error Writing to file:" + e);
                }

            }
            System.out.println(output);
            System.out.println("\n" + i);
            i++;
            System.out.println("\n\n\n");
        }
        //System.out.println(PMCtoDOI("pmc10276750"));
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

    public static String ESearch(String db, String query, boolean exact, String date) {
        String base = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/";
        db = db;

        if (exact) {
            // getHTML()
        } else {
            //parse the string query manually.

            StringTokenizer tokenizer = new StringTokenizer(query, " ");
            query = "";
            while (tokenizer.hasMoreElements()) {
                String ele = tokenizer.nextToken();
                if (!strList.contains(ele.toLowerCase())) { //if its not one of the bad words.
                    query = query + ele + "%5BAll%20Fields%5D";
                    if (tokenizer.hasMoreElements()) {
                        query = query + "+AND+";

                        //
                    } else {
                        date = date.replaceAll("-", "/");
                        String dateRange = "(1900/1/1:" + date + "[pdat])";
                        query = query + "+AND+";
                        query = query + dateRange;
                    }
                }

            }
            System.out.println(query);

            //System.out.println(query);
        }
        String url = base + "esearch.fcgi?db=" + db + "&term=" + query + "&usehistory=y&retmax=1000&sort=relevance";
        System.out.println("\n\n" + url + "\n\n");
        //System.out.println("\n\nTHEURL:" + url + "\n\n");
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

}
