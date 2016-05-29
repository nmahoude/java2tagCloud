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
  private KeywordFilter keywordFilter = new KeywordFilter();
  
  private static final ClassLoader classLoader = Java2TagCloud.class.getClassLoader();

  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.err.println("usage : Java2TagCloud <projectFolder>");
    }
    Path file = Paths.get(args[0]);
    
    Java2TagCloud java2TagCloud = new Java2TagCloud();
    
    Files.walk(file, FileVisitOption.FOLLOW_LINKS).allMatch( path -> {
      if (isFileAcceptable(path)) {
        java2TagCloud.handleFile(path);
      }
      return true;
    });
    java2TagCloud.printReport();
  }

  public Java2TagCloud() {
    keywordFilter.loadOneKeywordFile("javakeywords");
    keywordFilter.loadOneKeywordFile("javaclasses");
    keywordFilter.loadOneKeywordFile("specialkeywords");
  }
  
  static boolean isFileAcceptable(Path path) {
    if (!path.toFile().isFile()) {
      return false;
    }
    if (!path.getFileName().toString().endsWith(".java")) {
      return false;
    }
    if (path.toAbsolutePath().toString().contains("/src/test/")) {
      return false;
    }
    return true;
  }

  void handleFile(Path file) {
    Charset charset = Charset.forName("US-ASCII");
    try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        handleOneLine(line);
      }
    } catch (IOException x) {
      System.err.format("IOException: %s", x);
    }
  }

  void handleOneLine(String line) {
    if (keywordFilter.filterLine(line)) {
      return;
    }
    String words[] = line.split("\\W*\\W");
    for (String word : words) {
      if (!keywordFilter.filter(word)) {
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

  void printReport() {
    List<WordOccurence> occurences = wordsMap.values().stream()
        .filter(occurence -> { return occurence.getWord().length() > 3;})
        .filter(occurence -> { return occurence.getCount() > 3;})
        .sorted((occurence1, occurence2) -> Integer.compare(occurence2.getCount(),occurence1.getCount()))
        .collect(Collectors.toList());

    TagCloud tagCloud = new TagCloud(occurences);
    Path output = Paths.get("wordtag.png");
    tagCloud.generateTagCloud(output);
  }
}
