package com.ryanstoast.MusicServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Properties;

public class Server {

    private static int DEFAULT_PORT = 8000;
    public static String PORT_KEY = "port";

    private static Player player = new Player();
    private static Thread playerThread = new Thread(player, "Android Music Server");

    public static void listen(Properties p) throws Exception {
        //init player
        playerThread.start();

        int port = DEFAULT_PORT;
        if (p.containsKey(PORT_KEY)) {
            try {
                port = Integer.parseInt(p.getProperty(PORT_KEY));
            } catch (Exception e) {
                System.err.println("Error parsing port key. Using default port");
                e.printStackTrace();
                port = DEFAULT_PORT;
            }
        }


        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/music/list", new SongListHandler());

        server.createContext("/music/play", new SongPlayHandler());

        server.createContext("/music/pause", new SongPauseHandler());
        server.createContext("/music/stop", new SongStopHandler());
        server.createContext("/music/resume", new SongResumeHandler());

        server.createContext("/music/setVolume", new PlayerSetVolumeHandler());

        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server listening on port: " + port);
    }

    static class SongListHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            //use t to get arguments
            StringBuffer sb = new StringBuffer();
            sb.append("{\"songs\":[");
            for (Map.Entry<Integer,Song> s: FileTree.getSongs().entrySet()) {
                sb.append(s.getValue().toJSONObject().toString());
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]}");
            String list = sb.toString();
            t.sendResponseHeaders(200, list.length());
            OutputStream os = t.getResponseBody();
            os.write(list.getBytes());
            os.close();
        }
    }

    static class SongPlayHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            Integer songKey = Integer.parseInt(t.getRequestURI().toString().split("/")[3]);

            System.err.println(t.getRequestURI().toString() + " " + t.getRequestURI().toString().split("/")[1]);

            String response;
            if (player.play(FileTree.getSongPath(songKey))) {
                response = "{\"status\":\"playing\"}";
            } else {
                response = "{\"error\":\"song not found\"}";
            }



            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class SongPauseHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String response;

            if (player.pausePlayer()) {
                response = "{\"status\":\"paused\"}";
            } else {
                response = "{\"status\":\"error\"}";
            }

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class SongStopHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String response;

            if (player.stopPlayer()) {
                response = "{\"status\":\"stop\"}";
            } else {
                response = "{\"status\":\"error\"}";
            }

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class SongResumeHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String response;

            if (player.resumePlayer()) {
                response = "{\"status\":\"playing\"}";
            } else {
                response = "{\"status\":\"error\"}";
            }

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class PlayerSetVolumeHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            Double volume = Double.parseDouble(t.getRequestURI().toString().split("/")[3]);

            System.err.println(t.getRequestURI().toString() + " " + volume);
            player.setVolume(volume);

            String response = "{\"status\":\"okay\"}";

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}
