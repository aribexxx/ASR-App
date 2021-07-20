package com.example.myapplication.control;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public final class LocalFIleUtil {

    public static void writeJsonParamsToFile(String filePath, String json) throws Exception {
        //write output json params to local file
   /*     File myObj;
        try {
             myObj = new File(filePath);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());

            } else {
                System.out.println("File already exists.");
            }*/
            //FileWriter writer = new FileWriter(myObj);
        try(FileWriter fw = new FileWriter(filePath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.append(json);
            //more code
            //more code
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }


}
