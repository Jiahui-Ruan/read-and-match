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

## Implementation Detail

Since maybe we have to import multiple files, and each file varies in their length, I decide to use a 
[newFixedThreadPool](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Executors.html) to 
concurrently execute the importing thread which is a class named FileHandle that implements 
[Runnable](https://docs.oracle.com/javase/7/docs/api/java/lang/Runnable.html)

```java
// use ConcurrentHashMap for concurrence
ConcurrentHashMap<String, List<String>> typeaheadMap = new ConcurrentHashMap<>();

// create thread pool to read line
ExecutorService executors = Executors.newFixedThreadPool(pathArr.length);

for (String path : pathArr) {
    executors.execute(new FileHandler(new File(path), typeaheadMap));
}
```

To store the messages and create the inverted index, I use 
[ConcurrentHashMap](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html) 
to concurrently `get` and `put` key value pair.

```java
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
```

Here, `key` is a word of each message, `value` is a List of message that a word refer to.
To unify the storage in map, I force every word to lowercase before storage. Since the replace method
in [ConcurrentHashMap](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html) 
is atomic, like a transaction, it may fail, I use a while loop to ensure it executes successfully

After all the `runnable` has been assigned a thread to execute, I call

```java
// shut down the ThreadPool
executors.shutdown();

try {
    // wait for all the thread finshed import process
    executors.awaitTermination(1, TimeUnit.DAYS);
} catch (InterruptedException e) {
    e.printStackTrace();
} finally {
    // new line for better UX
    System.out.println();
}
```

Then, I create a class named WordFinder to find the message and use a while loop to listen to user input

```java
// create word finder class
WordFinder wordFinder = new WordFinder(typeaheadMap);

Scanner wordScan = new Scanner(System.in);
while (wordScan.hasNext()) {
    // prompt user to input search word

    String word = wordScan.next();
    if (word.equals("-1")) {
        wordFinder.summary();
        break;
    }
    wordFinder.find(word.toLowerCase());
}
```