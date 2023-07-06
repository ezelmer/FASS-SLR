/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import java.util.ArrayList;
import java.math.BigDecimal;

/**
 *
 * @author ethan
 */
public class Author {

    public static ArrayList<Author> registeredAuthors;
    public String firstname;
    public String surname;
    public String email;
    public static double meanContributions;
    public static int totalContributions;
    int contributions;

    public Author() {
        firstname = "Unknown surname";
        surname = "unknown Surname";
        if (registeredAuthors == null) {
            registeredAuthors = new ArrayList<Author>();
        }
    }

    public Author(String firstname, String surname, String email) {
        totalContributions++;
        this.firstname = removeLike(firstname, "&#", 9);
        this.email = email;
        this.surname = removeLike(surname, "&#", 9);
        if (registeredAuthors == null) {
            registeredAuthors = new ArrayList<Author>();
        }
        if (!registeredAuthors.contains(this)) {

            this.contributions = 1;
            registeredAuthors.add(this);
        } else {
           // System.out.println("CONTAINS THE AUTHOR!");
            registeredAuthors.get(registeredAuthors.indexOf(this)).contributions++;
        }
        meanContributions = (double) totalContributions/((double)registeredAuthors.size());
    }

    @Override
    public boolean equals(Object e) {
        if (e != null && e instanceof Author) {
            if (this.firstname.equals(((Author) e).firstname) && this.surname.equals(((Author)e).surname)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return this.firstname + "%" + this.surname + "%" + this.email +  "%" + contributions;
    }
    
    public String toJson(){
        return ("{\"firstname\":\"" + this.firstname + "\",\"surname\":\"" + this.surname + "\",\"email\":\"" + this.email + "\",\"contributions\":\"" + this.contributions+"\"}");
    }

    public static String removeLike(String in, String del, int length) {
        while (in.indexOf(del) != -1) {
            int loc = in.indexOf(del);
            in = in.substring(0, loc) + in.substring(loc + length);
        }

        return in;
    }

}
