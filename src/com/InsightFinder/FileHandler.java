package com.InsightFinder;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FileHandler implements Runnable{
    private File file;
    private String msg;
    private ConcurrentHashMap<String, List<String>> typeaheadMap;

    public FileHandler(File file, ConcurrentHashMap<String, List<String>> typeaheadMap) {
        this.file = file;
        this.typeaheadMap = typeaheadMap;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            msg = bufferedReader.readLine();

            while (msg != null) {
                addLine(msg);
                msg = bufferedReader.readLine();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void addLine(String msg) {
        String[] words = msg.split("[\\W&&[^\"']]");
        // create a set to avoid creating multiple index for a msg
        // with duplicate words
        Set<String> set = new HashSet<>();
        for (String word : words) {
            // force every word to lowercase before storage
            String wordLowerCase = word.toLowerCase();
            if (!wordLowerCase.equals("") && !set.contains(wordLowerCase)) {
                createIndex(wordLowerCase, msg);
                set.add(wordLowerCase);
            }
        }
    }

    private void createIndex(String word, String msg) {
        List<String> oldList, newList;
        while (true) {
            oldList = typeaheadMap.get(word);

            if (oldList == null) {
                // add word to it
                newList = new ArrayList<>();
                newList.add(msg);
                if (typeaheadMap.putIfAbsent(word, newList) == null) {
                    break;
                }
            } else {
                // add msg to newList
                newList = new ArrayList<>();
                for (String str : oldList) {
                    newList.add(str);
                }
                newList.add(msg);
                // use atomic action for putting newList
                if (typeaheadMap.replace(word, oldList, newList)) break;
            }
        }
    }
}
