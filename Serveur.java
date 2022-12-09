package serveur;
import play.Sound;
import java.io.*;
import java.net.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.BorderLayout;
import java.awt.*;
import java.awt.Image.*;
import java.awt.Image;
import javax.swing.*;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Serveur {
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    private static FileInputStream fis = null;
    private static Socket s = null;

    public Serveur(){
        try{
            ServerSocket serverSocket = new ServerSocket(6000);
            s = serverSocket.accept();
            System.out.println("Client connecte");

            ObjectInputStream objinp = new ObjectInputStream(s.getInputStream());
            String input = (String) objinp.readObject();
            System.out.println(input);

            if(input.equals("image")) {
                ObjectOutputStream objout = new ObjectOutputStream(s.getOutputStream());
                objout.writeObject("image");
                this.getServeur(s);
            }

            if(input.equals("music")){
                ObjectOutputStream objout=new ObjectOutputStream(s.getOutputStream());
                objout.writeObject("music");
                this.songServeur(s);
            }

        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getServeur(Socket s) throws IOException,ClassNotFoundException,InterruptedException {
        System.out.println("Accepted connection :" + s);
        OutputStream outputStream = s.getOutputStream();
        BufferedImage image = ImageIO.read(new File("D:/multimedia/image/IMG_6443.jpg"));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image,"jpg",byteArrayOutputStream);
        byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
        outputStream.write(size);
        outputStream.write(byteArrayOutputStream.toByteArray());
        outputStream.flush();
        System.out.println("Sending image");
        System.out.println("Flushed"+System.currentTimeMillis());
        Thread.sleep(120000);
        System.out.println("Closing"+System.currentTimeMillis());
        
    }

    public void songServeur(Socket s) throws IOException,ClassNotFoundException,InterruptedException{
        DataOutputStream out = new DataOutputStream(s.getOutputStream());

        File fichierMp3 = new File("../multimedia/fichier-mp3/Amir_-_On_dirait_(Clip_officiel)(128k).mp3");
        out.writeUTF(fichierMp3.getName().toLowerCase());

        while(true) {
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            String demande =(String) ois.readObject();

            if(demande.contains(".mp3")) {
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                oos.writeObject(demande);

                //Envoiyer le music
                FileInputStream inputStream = new FileInputStream(fichierMp3);
                byte[] mybytearray = inputStream.readAllBytes();

                while (true) {
                    out.writeUTF(fichierMp3.getName().toLowerCase());
                    out.write(mybytearray);
                }
            }
        }
    }
     
}