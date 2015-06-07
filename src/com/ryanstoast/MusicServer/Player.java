package com.ryanstoast.MusicServer;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;

public class Player extends Application implements Runnable {

    private Media media;
    private MediaPlayer player;
    private File f;
    private String songPath;


    public Player(){}

    @Override
    public void start(Stage primary) throws Exception {//configures javafx
    }

    public boolean play(String songPath) {
        try {
            //songPath = "C:\\Users\\Rage\\Music\\30 Seconds to Mars\\This Is War\\04 This Is War.mp3";
            //File f = new File(songPath);
            //launch(songPath);

            f = new File(songPath);
            media = new Media(f.toURI().toString());

            player = new MediaPlayer(media);
            if (player == null) {
                return false;
            }
            player.play();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean stopPlayer() {
        try {
            player.stop();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean pausePlayer() {
        try {
            player.pause();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean resumePlayer() {
        try {
            player.play();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setVolume(double v) {
        try {
            player.setVolume(v / 100d);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void run() {
        launch("AndroidMusicPlayer");
    }
}
