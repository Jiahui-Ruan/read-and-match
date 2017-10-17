package com.InsightFinder;

import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
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

        // use ConcurrentHashMap for concurrence
        ConcurrentHashMap<String, List<String>> typeaheadMap = new ConcurrentHashMap<>();

        // create thread pool to read line
        ExecutorService executors = Executors.newFixedThreadPool(pathArr.length);

        for (String path : pathArr) {
            executors.execute(new FileHandler(new File(path), typeaheadMap));
        }

        executors.shutdown();

        // print out the map
//        System.out.println("created Map: ");
//        System.out.println(typeaheadMap.toString());

        // create word finder class
        WordFinder wordFinder = new WordFinder(typeaheadMap);

        // prompt user to input search word
        System.out.println("Enter the word you want to search without space, enter -1 to exit");

        Scanner wordScan = new Scanner(System.in);
        while (wordScan.hasNext()) {
            String word = wordScan.next();
            if (word.equals("-1")) {
                wordFinder.summary();
                break;
            }
            wordFinder.find(word.toLowerCase());
        }
    }
}
