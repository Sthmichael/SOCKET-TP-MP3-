package client;
import player.*;
import music.*;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Client extends JFrame{

    private JButton btn = new JButton("image");
    private JButton btn2 = new JButton("musique");
    private JPanel pan = new JPanel();
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    Socket soc = null;

    public void getClient(Socket s) throws IOException,ClassNotFoundException {

        InputStream inputStream = soc.getInputStream();
        System.out.println("reading"+System.currentTimeMillis());
        System.out.println("File received");
        byte[] sizear = new byte[4];
        inputStream.read(sizear);
        int size = ByteBuffer.wrap(sizear).asIntBuffer().get();
        byte [] imagear = new byte[size];
        inputStream.read(imagear);
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imagear));
        ImageIcon imageIcon = new ImageIcon(image);
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(500,500);
        JLabel jlabel = new JLabel();
        jlabel.setIcon(imageIcon);
        frame.add(jlabel);
        frame.setVisible(true);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


    public Client() {
        try {
            soc = new Socket("localhost",6000);
            pan.add(btn);
            pan.add(btn2);
            this.setSize(400,400);
            this.setContentPane(pan);
            this.setVisible(true);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            btn.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ev){
                    try{
                        ObjectOutputStream objout = new ObjectOutputStream(soc.getOutputStream());
                        objout.writeObject("image");
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            btn2.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ev){
                    try{
                        ObjectOutputStream objout = new ObjectOutputStream(soc.getOutputStream());
                        objout.writeObject("music");
                    }catch(Exception e) {
                        System.out.println(e.getMessage());
                        //e.printStackTrace();
                    }
                }
            });


            ObjectInputStream objinp = new ObjectInputStream(soc.getInputStream());
            String input =(String) objinp.readObject();
            System.out.println(input);

            if(input.equals("image")) {
                getClient(soc);
            }

            if(input.equals("music")) {
                soundClient(soc);
            }
            
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void soundClient(Socket s) throws IOException,ClassNotFoundException {
        DataInputStream data = new DataInputStream(s.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

        String fichierName = data.readUTF();

        JLabel label = new JLabel();
        label.setText("Playing:  ");


        JButton b1 = new JButton();
        b1.setText(fichierName);
        b1.setBackground(Color.RED);
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    oos.writeObject(e.getActionCommand());
                    oos.flush();
                }catch(Exception exp) {
                    exp.printStackTrace();
                }
            }
        });

        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(500,500);
        frame.add(label);
        frame.add(b1);
        frame.setVisible(true);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
        String envoye = (String) ois.readObject();

        if(envoye.contains(".mp3")) {
            //envoye mp3
            int len = 1000000;
            byte[] mybytearray = new byte[len];

            JFrame frame1 = new JFrame();
            frame1.setLayout(new FlowLayout());
            frame1.setSize(500,500);
            JLabel label1 = new JLabel();
            label1.setText(fichierName);
            frame1.add(label1);
            frame1.setVisible(true);
            //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          

            while(true) {
                data.read(mybytearray,0,len);
                Thread play = new Thread(new PlayMp3(mybytearray));
                try{
                    play.start();
                    play(mybytearray);
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void play(byte[] data) throws Exception{
        DataInputStream in= new DataInputStream(new ByteArrayInputStream(data));
        Player player = new Player(in);
        player.play();
    }

}