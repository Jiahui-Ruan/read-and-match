package com.InsightFinder;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
	    // create a scanner to read from command line
        Scanner sc = new Scanner(System.in);
        // prompt for user notice
        System.out.println("Enter the file path separate by space");
        // get file path
        String filePaths = sc.nextLine();
        // split path
        String[] pathArr = filePaths.split(" ");
        // create thread pool to read line
        ExecutorService executors = Executors.newFixedThreadPool(pathArr.length);

        for (String path : pathArr) {
            executors.execute(new FileHandler(new File(path)));
        }

        executors.shutdown();
    }
}
