package main;
import client.*;

public class MainC {
    public static void main(String[] args) {
        try {
            Client client = new Client();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}