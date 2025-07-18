package com.example.l1t2_term_project.Main;

import com.example.l1t2_term_project.Model.Player.*;
import com.example.l1t2_term_project.Utils.ActivityLogger;

import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
//        ActivityLogger.log("Success");

        PlayerCollection.readFromFile();
        for (Player player : PlayerCollection.getPlayers())
        {
            System.out.println(player);
        }
        System.out.println();

//        PlayerCollection.getPlayers().get(0).setName("Changed");
        PlayerCollection.writeToFile();
    }
}
