/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

/**
 *
 * @author ethan
 */
import java.util.ArrayList;

/**
 *
 * @author ethan
 */
public class Component {

    public String Contents;
    public ArrayList<Component> subComponents;
    
    public Component() {
        Contents = "";
        subComponents = new ArrayList<Component>();
    }

    public Component(String txt) {
        this.Contents = txt;
        subComponents = new ArrayList<Component>();
    }

    public String toString() {
        return Contents + ":\n" + subComponents;
    }
}
