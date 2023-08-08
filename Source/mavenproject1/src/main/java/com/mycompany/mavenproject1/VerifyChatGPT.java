/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import static com.mycompany.mavenproject1.Main.initialize;
import java.util.ArrayList;

/**
 *
 * @author ethan
 */
public class VerifyChatGPT {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // TODO code application logic here
        ArrayList<SLR> slrs = initialize();
        int totalErrors = 0;
        System.out.println("Done initialize");
        for (int j = 2; j < slrs.size(); j++) {
            SLR s = slrs.get(j);
            System.out.println("Verifying SLR " + j);
            //totalErrors += verifyQueries(s.gptTAbQueries, j);
           totalErrors+=verifyQueries(s.gptWRefs, j);
            totalErrors+=verifyQueries(s.gptTitleQueries, j);
            for (String x : s.gptTitleQueries) {

            }
        }
        System.out.println("Total Errors: " + totalErrors);
        SLR.DumpSLRData(slrs);
    }

    /**
     * This method verifies the string arraylist of queries by checking the amount of open hard brackets, closed hard brackets, open and close parantheses etc. 
     * If the query is not properly formed, it applies some quick fixes to see if the problem can be sorted. (ex. if there aren't enough open parentheses, place an additional at the start of the query
     * @param givenqueries String arraylist of queries in pubmed format
     * @param slrID id of the slr.
     * @return the amount of errors in a given slr's queries
     */
    public static int verifyQueries(ArrayList<String> givenqueries, int slrID) {
        int eQ = 0;
        ArrayList<String> queries = (ArrayList) givenqueries.clone();
        ArrayList<String> override = new ArrayList<>();
        //   override.add("TESTING ONE TWO");

        for (String x : queries) {
            //System.out.println("\t" + x + "\n");
            boolean valid = true;
            int openP = 0; //amt open parentheses/soft brackets
            int closeP = 0; //amt closed parentheses/soft brackets
            int openHB = 0; //amt open hard brackets
            int closeHB = 0; //amt closed hard brackets
            int space = 0;
            int quote = 0;
            for (int i = 0; i < x.length(); i++) { //go through each character
                switch (x.charAt(i)) { //if it's a syntax character, count it appropriately.
                    case '(':
                        openP++;
                        break;
                    case ')':
                        closeP++;
                        break;
                    case '"':
                        quote++;
                        break;
                    case '[':
                        openHB++;
                        break;
                    case ']':
                        closeHB++;
                        break;
                    case ' ':
                        space++;
                        break;
                    default:
                        break;

                }

                //amount of open parentheses = amount close parentheses.
                //every every odd (") character must be preceded by either a ( or a space. dont worry about this either
                //
            }
            while (closeP < openP) { //if the amount of open parantheses is greater than close parentheses, put some on the end
                System.out.println("MODIFYING QUERY " + slrID);
                //  if (x.charAt(x.length() - 1) != ')') {
                x = x + ")";
                closeP++;
                //  }
            }
            if (openP != closeP || openHB != closeHB) {
                eQ++;
                System.out.println("Error with SLR QUERY:" + slrID + "\n\n"); //error with a given query, need to go manually adjust it.

                System.out.println(x);
                System.out.printf("\nopenP: %d closeP: %d openHB: %d closeHB: %d \n\n", openP, closeP, openHB, closeHB);
                System.out.println("\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            } else {
                System.out.println("Adding to SLR" + slrID);
                override.add(x);
            }

        }
        givenqueries.clear();
        for (String x : override) {
            givenqueries.add(x);
        }
        return eQ;
    }

}
