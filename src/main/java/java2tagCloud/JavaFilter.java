package java2tagCloud;

import java.nio.file.Path;

public class JavaFilter extends KeywordFilter {

  public JavaFilter() {
    loadKeywordFile("javakeywords");
    loadKeywordFile("javaclasses");
    loadKeywordFile("specialkeywords");
  }
  
  boolean isFileAcceptable(Path path) {
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

  public boolean filterLine(String line) {
    if (line.trim().startsWith("import")) {
      return true;
    }
    if (line.trim().startsWith("package")) {
      return true;
    }
    return false;
  }
}
