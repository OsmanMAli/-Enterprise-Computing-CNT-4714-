/*
Name: <Osman Ali>
Course: CNT 4714 Fall 2023
Assignment title: Project 3 – A Two-tier Client-Server Application
Date: October 29, 2023
Class: <Main.java>
*/

import javax.swing.*;

//Main Class, i implemented separately for organizational purposes
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainAppGUI::new);
        DatabaseLogger.connect();
    }
}