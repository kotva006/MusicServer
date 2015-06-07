package com.ryanstoast.MusicServer;

import com.ryanstoast.MusicServer.Exceptions.UnknownPathException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;

public class FileTree {

    private static String[] extensions = {"mp3", "m4a", "wma", "flac", "wav"};
    private static String root;

    private static Map<Integer, Song> songs = new HashMap<Integer, Song>();

    public static void setRoot(String r) {root = r;}
    public static Map<Integer, Song> getSongs(){return songs;}

    public static String getSongPath(Integer i) {
        Song song = songs.get(i);
        if (song != null) {
            return song.getPath();
        } else {
            return "";
        }
    }

    public static void populate() throws Exception {

        if (root == null || root.isEmpty()) {
            throw new UnknownPathException("Root directory is null or empty!");
        }

        File path = new File(root);
        Iterator<File> files = FileUtils.iterateFiles(path, extensions, true);

        if (files == null) {
            throw new UnknownPathException("Given path does not exist!");
        }

        while (files.hasNext()) {
            File file = files.next();
            InputStream stream = new FileInputStream(file);
            AutoDetectParser parser = new AutoDetectParser();
            BodyContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            try {
                parser.parse(stream, handler, metadata);
                String[] names = metadata.names();
                String title = "";
                String artist = "";
                String album = "";
                String duration = "";
                String trackNumber = "";
                for (String n : names) {
                    if (n.contains("title")) {
                        title = metadata.get(n);
                    }
                    if (n.contains("artist")) {
                        artist = metadata.get(n);
                    }
                    if (n.contains("duration")) {
                        duration = metadata.get(n);
                    }
                    if (n.contains("album")) {
                        album = metadata.get(n);
                    }
                    if (n.contains("trackNumber")) {
                        trackNumber = metadata.get(n);
                    }
                }
                songs.put(file.getPath().hashCode(), new Song(artist, album, title, duration, trackNumber, file.getPath()));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }
        }

        System.out.println(songs.size());

    }
}
