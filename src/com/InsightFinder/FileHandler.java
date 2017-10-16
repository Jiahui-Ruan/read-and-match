package com.InsightFinder;

import java.io.*;

public class FileHandler implements Runnable{
    private File file;
    private String line;

    public FileHandler(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            line = bufferedReader.readLine();

            while (line != null) {
                System.out.println(line);
                line = bufferedReader.readLine();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
