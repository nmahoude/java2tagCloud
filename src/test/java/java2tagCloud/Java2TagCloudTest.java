package java2tagCloud;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

public class Java2TagCloudTest {

  @Test
  public void handleOneWordTest() {
    
  }

  @Test
  public void testExtractCamelCaseWords_whenNull() throws Exception {
    Java2TagCloud j2tc = new Java2TagCloud();
    
    String[] words = j2tc.extractCamelCaseWords(null);
    
    assertThat(words.length, is(0));
  }

  @Test
  public void testExtractCamelCaseWords_whenEmpty() throws Exception {
    Java2TagCloud j2tc = new Java2TagCloud();
    
    String[] words = j2tc.extractCamelCaseWords("");
    
    assertThat(words.length, is(0));
  }

  @Test
  public void testExtractCamelCaseWords_whenSpacesOnly() throws Exception {
    Java2TagCloud j2tc = new Java2TagCloud();
    
    String[] words = j2tc.extractCamelCaseWords("   ");
    
    assertThat(words.length, is(0));
  }

  @Test
  public void testExtractCamelCaseWords_whenNoCamel() throws Exception {
    Java2TagCloud j2tc = new Java2TagCloud();
    
    String[] words = j2tc.extractCamelCaseWords("oneword");
    
    assertThat(words.length, is(1));
    assertThat(words[0], is("oneword"));
  }
  @Test
  public void testExtractCamelCaseWords_whenCamel() throws Exception {
    Java2TagCloud j2tc = new Java2TagCloud();
    
    String[] words = j2tc.extractCamelCaseWords("twoWord");
    
    assertThat(words.length, is(2));
    assertThat(words[0], is("two"));
    assertThat(words[1], is("Word"));
  }

  @Test
  public void testWordFilter_class() throws Exception {
    Java2TagCloud j2tc = new Java2TagCloud();
    
    j2tc.handleOneLine("class Test");
    
    assertThat(j2tc.wordsMap.size(), is(1));
    assertThat(j2tc.wordsMap.get("class"), is(nullValue()));
    assertThat(j2tc.wordsMap.get("test"), is(notNullValue()));
  }
  
}
