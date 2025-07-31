package com.example.l1t2_term_project.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ClientReadThread extends Thread {
    Object currentObj;
    SocketWrapper socketWrapper;

    public ClientReadThread(@NotNull String name, SocketWrapper socketWrapper) {
        super(name);
        this.socketWrapper = socketWrapper;
    }

    public Object read()
    {
        synchronized (this)
        {
            if (currentObj == null)
            {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    System.out.println("Object not stored");
                }
            }
            Object obj = currentObj;
            currentObj = null;
            this.notifyAll();
            return obj;
        }
    }

    @Override
    public void run() {
        Object obj;
        while (true)
        {
            try{
                obj = socketWrapper.read();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Client disconnected");
                break;
            }

            synchronized (this)
            {
                currentObj = obj;
                System.out.println("Object stored");
                this.notifyAll();
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    System.err.println("Client Read Thread wait failure");
                    break;
                }
                System.out.println("Object has been read");
            }
        }
    }
}
