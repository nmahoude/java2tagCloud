package java2tagCloud;

public class WordOccurence {
  private final String word;
  private int count;
  
  public WordOccurence(String word) {
    this.word = word;
    this.count = 1;
  }

  public String getWord() {
    return word;
  }

  public int getCount() {
    return count;
  }

  public void incrementCount() {
    count++;
  }
}
