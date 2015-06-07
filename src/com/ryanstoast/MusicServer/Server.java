package com.ryanstoast.MusicServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;

public class Server {

    private static Player player = new Player();
    private static Thread playerThread = new Thread(player, "Android Music Server");

    public static void listen() throws Exception {
        //start player
        playerThread.start();


        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/music/list", new SongListHandler());

        server.createContext("/music/play", new SongPlayHandler());

        server.createContext("/music/pause", new SongPauseHandler());
        server.createContext("/music/stop", new SongStopHandler());
        server.createContext("/music/resume", new SongResumeHandler());

        server.createContext("/music/setVolume", new PlayerSetVolumeHandler());

        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class SongListHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            //use t to get arguments
            String response = "This is the response";
            StringBuffer sb = new StringBuffer();
            sb.append("{songs:[");
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
            player.play(FileTree.getSongPath(songKey));

            String response = "{status:playing}";

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
                response = "{status:paused}";
            } else {
                response = "{status:error}";
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
                response = "{status:stop}";
            } else {
                response = "{status:error}";
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
                response = "{status:playing}";
            } else {
                response = "{status:error}";
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

            String response = "{status:okay}";

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}
