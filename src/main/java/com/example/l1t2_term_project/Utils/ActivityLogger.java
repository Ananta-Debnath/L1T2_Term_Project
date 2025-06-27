package com.example.l1t2_term_project.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ActivityLogger
{
    private static PrintWriter logFile;

    public static void start()
    {
        try
        {
            logFile = new PrintWriter(new FileWriter("SystemLog.log", true));
        }
        catch (IOException e)
        {
            System.err.println("Failed to open SystemLog.log: " + e.getMessage());
        }
    }

    public static void close()
    {
        if (logFile != null) logFile.close();
    }

    private static void logTime()
    {
        LocalDateTime timestamp = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        logFile.print("[" + timestamp.format(formatter) + "] ");
    }

    public static void log(String message)
    {
        logTime();
        logFile.println(message);
    }
}
