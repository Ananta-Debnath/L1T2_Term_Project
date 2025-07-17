package com.example.l1t2_term_project.Server;

import com.example.l1t2_term_project.Model.Player.Player;
import com.example.l1t2_term_project.Utils.ActivityLogger;
import com.example.l1t2_term_project.Utils.SocketWrapper;

import java.io.IOException;
import java.util.HashMap;

public class ServerThread extends Thread {
    private final SocketWrapper socketWrapper;
    public HashMap<String, SocketWrapper> clientMap;

    public ServerThread(HashMap<String, SocketWrapper> map, SocketWrapper socketWrapper) {
        ActivityLogger.log("Client connected: " + socketWrapper.getSocket());
        this.clientMap = map;
        this.socketWrapper = socketWrapper;
    }

    public void run() {
        try {
            while (true) {
                Object o = socketWrapper.read();
                if (o instanceof Player) {

                }
                else {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                socketWrapper.closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
