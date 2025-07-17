package com.example.l1t2_term_project.Server;


import com.example.l1t2_term_project.Utils.ActivityLogger;
import com.example.l1t2_term_project.Utils.SocketWrapper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {

    public HashMap<String, SocketWrapper> clientMap;

    Server() {
        startServer();
    }

    private void startServer()
    {
        ActivityLogger.log("Server Started");
        clientMap = new HashMap<>();
        try (ServerSocket serverSocket = new ServerSocket(12913)) {
            while (true) { // Will be running forever
                Socket clientSocket = serverSocket.accept();
                serve(clientSocket);
            }
        } catch (Exception e) {
            ActivityLogger.log("Server starts:" + e);
            System.out.println("Server starts:" + e);
        }
    }

    public void serve(Socket clientSocket) throws IOException, ClassNotFoundException {
        SocketWrapper socketWrapper = new SocketWrapper(clientSocket);
        String clientName = (String) socketWrapper.read();
        clientMap.put(clientName, socketWrapper);
        new ServerThread(clientMap, socketWrapper).start();
    }

    public static void main(String[] args) {
        new Server();
    }
}

