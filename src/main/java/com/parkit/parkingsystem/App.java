package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;

/**
 * Launch of the Park'it Application.
 *
 * @author Clévyd
 */
public class App {

/**
 * This method main launch Park'it Application.
 *
 * @param args the method main
 */
    public static void main(String[] args){
        InteractiveShell.loadInterface();
    }
    private App(){
    }
}
