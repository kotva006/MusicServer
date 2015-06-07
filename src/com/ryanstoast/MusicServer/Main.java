package com.ryanstoast.MusicServer;


import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class Main{

    private static String root = "path_to_root_music_dir";

    public static void main(String[] args) {

        //read_config
        try {
            FileTree.setRoot(root);
            FileTree.populate();
            //launch(args);



            Server.listen();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
