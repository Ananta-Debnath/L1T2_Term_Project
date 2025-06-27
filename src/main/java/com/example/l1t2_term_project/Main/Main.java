package com.example.l1t2_term_project.Main;

import com.example.l1t2_term_project.Utils.ActivityLogger;

public class Main
{
    public static void main(String[] args)
    {
        ActivityLogger.start();
        ActivityLogger.log("Success");
        ActivityLogger.close();
    }
}
