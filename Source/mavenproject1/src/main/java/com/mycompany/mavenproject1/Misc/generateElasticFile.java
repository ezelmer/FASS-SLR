/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.Misc;

/**
 *
 * @author ethan
 */

import static com.mycompany.mavenproject1.Main.initialize;
import static com.mycompany.mavenproject1.Main.getHTML;
import static com.mycompany.mavenproject1.Main.grabTag;
import com.mycompany.mavenproject1.SLR;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
public class generateElasticFile {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ArrayList<SLR> slrs = initialize();
        for(int i = 2; i<=112; i++){
            SLR s = slrs.get(i);
            String x =  s.name + " " + s.abs ;
            x = x.replace("\n", " ");
            System.out.println(x);
        }
    }
    
}
