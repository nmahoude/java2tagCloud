package java2tagCloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Java2TagCloud {
  Map<String, WordOccurence> wordsMap = new HashMap<>();

  public static void old_main(String[] args) {
    ClassLoader classLoader = Java2TagCloud.class.getClassLoader();
    Path file = Paths.get(classLoader.getResource("test.java").getFile());

    new Java2TagCloud().handleFile(file);
  }
  
  
  public static void main(String[] args) throws IOException {
    ClassLoader classLoader = Java2TagCloud.class.getClassLoader();
    Path file = Paths.get("/home/nicolas/Dev/projects/DT/quizzadm/sources/");
    
    Java2TagCloud java2TagCloud = new Java2TagCloud();
    
    Files.walk(file, FileVisitOption.FOLLOW_LINKS).allMatch( path -> {
      if (path.toFile().isFile() && path.getFileName().toString().endsWith(".java")) {
        java2TagCloud.handleFile(path);
      }
      return true;
    });
    java2TagCloud.printReport();
  }

  void handleFile(Path file) {

    Charset charset = Charset.forName("US-ASCII");
    try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        handleOneLine(line);
      }
    } catch (IOException x) {
      System.err.format("IOException: %s%n", x);
    }
  }

  void printReport() {
    List<WordOccurence> occurences = wordsMap.values().stream()
        .sorted((occurence1, occurence2) -> Integer.compare(occurence2.getCount(),occurence1.getCount()))
        .collect(Collectors.toList());

    for (WordOccurence occurence : occurences) {
      System.out.println(occurence.getWord() + " =>" + occurence.getCount());
    }
  }

  void handleOneLine(String line) {
    if (JavaKeywordFilter.filterLine(line)) {
      return;
    }
    String words[] = line.split("\\W*\\W");
    for (String word : words) {
      if (!JavaKeywordFilter.filter(word)) {
        handleOneWord(word);
      }
    }
  }

  void handleOneWord(String word) {
    String[] camelCutWords = extractCamelCaseWords(word);
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

  String[] extractCamelCaseWords(String word) {
    if (word == null) {
      return new String[]{};
    }
    word = word.trim();
    if ("".equals(word)) {
      return new String[]{};
    }
    String camelCutWords[] = word.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
    return camelCutWords;
  }
}
