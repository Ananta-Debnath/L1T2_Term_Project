package com.example.l1t2_term_project.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ActivityLogger
{
    private static final String logFilePath = "SystemLog.log";

    private static void logTime(PrintWriter logFile)
    {
        LocalDateTime timestamp = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        logFile.print("[" + timestamp.format(formatter) + "] ");
    }

    public static void log(String message)
    {
        try (PrintWriter logFile = new PrintWriter(new FileWriter(logFilePath, true)))
        {
            logTime(logFile);
            logFile.println(message);
            logFile.flush();
        }
        catch (IOException e)
        {
            System.err.println("Failed to open SystemLog.log: " + e.getMessage());
        }
    }
}
