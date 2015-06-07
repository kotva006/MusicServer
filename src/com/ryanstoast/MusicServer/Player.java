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

            f = new File(songPath);
            media = new Media(f.toURI().toString());

            player = new MediaPlayer(media);
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
