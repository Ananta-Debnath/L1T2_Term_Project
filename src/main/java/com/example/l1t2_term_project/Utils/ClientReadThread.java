package com.example.l1t2_term_project.Utils;

import com.example.l1t2_term_project.Client;
import com.example.l1t2_term_project.DTO.NotificationDTO;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ClientReadThread extends Thread {
    Object currentObj;
    Client client;

    public ClientReadThread(@NotNull String name, Client client) {
        super(name);
        this.client = client;
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
        System.out.println(Thread.currentThread().getName() + " started");
        Object obj;
        while (true)
        {
            try{
                obj = client.getSocketWrapper().read();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Client disconnected");
                break;
            }

            if (obj instanceof NotificationDTO)
            {
                // TODO: show notification
                synchronized (client.getLock())
                {
                    client.getLock().notifyAll();
                }
            }
            else
            {
                synchronized (this)
                {
                    currentObj = obj;
//                    System.out.println("Object stored");
                    this.notifyAll();
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        System.err.println("Client Read Thread wait failure");
                        break;
                    }
//                    System.out.println("Object has been read");
                }
            }
        }
        System.out.println(Thread.currentThread().getName() + " closed");
    }
}
