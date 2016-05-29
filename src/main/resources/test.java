package java2tagCloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Java2TagCloud {
  private Map<String, WordOccurence> wordsMap = new HashMap<>();

  public static void main(String[] args) {
    new Java2TagCloud().handleFile();
  }

  private void handleFile() {
    ClassLoader classLoader = Java2TagCloud.class.getClassLoader();
    Path file = Paths.get(classLoader.getResource("test.java").getFile());

    Charset charset = Charset.forName("US-ASCII");
    try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        handleOneLine(line);
      }
    } catch (IOException x) {
      System.err.format("IOException: %s%n", x);
    }

    printReport();
  }

  private void printReport() {
    Collection<WordOccurence> occurences = wordsMap.values();
    for (WordOccurence occurence : occurences) {
      System.out.println(occurence.getWord() + " =>" + occurence.getCount());
    }
  }

  private void handleOneLine(String line) {
    String words[] = line.split("\\W*\\W");
    for (String word : words) {
      if (!JavaKeyworkFilter.filter(word)) {
        handleOneWord(word);
      }
    }
  }

  private void handleOneWord(String word) {
    String camelCutWords[] = word.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
    for (String part : camelCutWords) {
      part = part.toLowerCase();
      WordOccurence occurence = wordsMap.get(part);
      if (occurence == null) {
        wordsMap.put(part, new WordOccurence(part));
      } else {
        occurence.incrementCount();
      }
    }
  }
}
