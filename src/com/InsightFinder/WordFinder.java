package com.InsightFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class WordFinder {
    private ConcurrentHashMap<String, List<String>> typeaheadMap;
    private int searchCount = 0;
    private int hitCount = 0;

    public WordFinder(ConcurrentHashMap<String, List<String>> typeaheadMap) {

        this.typeaheadMap = typeaheadMap;
    }

    public void find(String word) {
        searchCount++;

        if (typeaheadMap.containsKey(word)) {
            List<String> indexList = typeaheadMap.get(word);
            
            List<String> matchedMsgs = new ArrayList<>();
            for (String msg : indexList) {
                matchedMsgs.add(upperMatchedWord(word, msg));
            }
            
            // print the matchedMsgs
            System.out.println("These are matching messages:");
            for (String msg : matchedMsgs) {
                System.out.println(msg);
            }
                
        } else {
            System.out.println("Not found!");
        }
        System.out.println();
    }

    public void summary() {
        System.out.println("Summary: ");
        System.out.println("searchCount : " + searchCount + " " +
                           "hitCount: " + hitCount);
    }
    
    private String upperMatchedWord(String word, String msg) {
        // count same word in a list

        String[] msgArr = msg.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String str : msgArr) {
            // transform str to pure lowercase word
            String transformedStr = str.replaceAll("[\\W&&[^\"']]", "").toLowerCase();
            if (transformedStr.equals(word)) {
                // append the uppercased orginal str
                sb.append(str.toUpperCase() + " ");
                hitCount++;
            } else {
                sb.append(str + " ");
            }
        }
        // remove last space
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
}
