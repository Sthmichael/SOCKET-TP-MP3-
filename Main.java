package main;
import serveur.*;

public class Main {
    public static void main(String[] args) {
        try {
            Serveur serveur = new Serveur();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}