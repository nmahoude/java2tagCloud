package java2tagCloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class JavaKeywordFilter {

  private static Set<String> keywords = new HashSet<>();
  
  static {
    loadOneKeywordFile("javakeywords");
    loadOneKeywordFile("javaclasses");
    loadOneKeywordFile("specialkeywords");
  }
  
  private static void loadOneKeywordFile(String filename) {
    ClassLoader classLoader = Java2TagCloud.class.getClassLoader();
    Path file = Paths.get(classLoader.getResource(filename).getFile());
    
    Charset charset = Charset.forName("US-ASCII");
    try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
        String keyword = null;
        while ((keyword = reader.readLine()) != null) {
          keywords.add(keyword.trim());
        }
    } catch (IOException x) {
        System.err.format("IOException: %s%n", x);
    }
  }
  
  /**
   * return true if the word is a javakeyword
   * @param word
   * @return
   */
  public static boolean filter(String word) {
    return keywords.contains(word);
  }

  public static boolean filterLine(String line) {
    if (line.trim().startsWith("import")) {
      return true;
    }
    return false;
  }
}
