package com.example.l1t2_term_project.Main;

import com.example.l1t2_term_project.Model.Player.Player;
import com.example.l1t2_term_project.Model.Player.PlayerCollection;
import com.example.l1t2_term_project.Utils.ActivityLogger;

import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        ActivityLogger.start();
//        ActivityLogger.log("Success");

        PlayerCollection.readFromFile();
        for (Player player : PlayerCollection.getPlayers())
        {
            System.out.println(player);
        }

//        PlayerCollection.getPlayers().get(0).setName("Changed");
        PlayerCollection.writeToFile();

        ActivityLogger.close();
    }
}
