package player;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.*;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class PlayMp3 implements Runnable{
    byte[] taille;

    public PlayMp3(byte[] taille) {
        this.taille=taille;
    }

    public void run() {
        try{
            play();
            pause();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public  void play() throws Exception{
        DataInputStream in= new DataInputStream(new ByteArrayInputStream(this.taille));
        Player player = new Player(in);
        player.play();
    }

    public void pause() throws Exception{
        DataInputStream in= new DataInputStream(new ByteArrayInputStream(this.taille));
        Player player = new Player(in);
        player.close();
    }
}