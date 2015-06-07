package com.ryanstoast.MusicServer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class Main{

    public static String CONFIG_FILE = "config.properties";
    public static String ROOT_KEY = "root";

    public static void main(String[] args) {

        try {
            //read_config
            FileInputStream fis = new FileInputStream(CONFIG_FILE);
            Properties properties = new Properties();
            properties.load(fis);
            fis.close();
            System.out.print(properties.getProperty(ROOT_KEY));

            if (properties.containsKey(ROOT_KEY)) {
                FileTree.setRoot(properties.getProperty(ROOT_KEY));
                FileTree.populate();

                Server.listen(properties);
            } else {
                System.err.print("Root configuration not found.  Please check config file");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
