# read-and-match
an application to simulate the creation of inverted index

## How to use

First, extract the .tar file

```bash
$ tar -xvzf read-and-match.tar
```

Second, go to `src` folder and compile the java file

```bash
$ cd src
$ javac com/InsightFinder/Main.java
```

Third, run the compiled Main class file

```bash
$ java com.InsightFinder.Main
```

Then, you will see it prompts out a reminder for you

```bash
Enter the file path separate by space
```

Enter the file you want to import

```bash
Enter the file path separate by space
~/Downloads/read-and-match-master/test/sample1.txt ~/Downloads/read-and-match-master/test/sample2.txt ~/Downloads/read-and-match-master/test/sample3.txt 
```

After the import thread finished, it prompts out: 

```bash
Enter the word you want to search without space, enter -1 to exit:
```

Enter the word you want to search, for example:
![example](https://github.com/Jiahui-Ruan/read-and-match/blob/master/example.png)