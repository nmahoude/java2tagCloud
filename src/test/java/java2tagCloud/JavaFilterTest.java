package java2tagCloud;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class JavaFilterTest {
  @Test
  public void testFilterLine_import() throws Exception {
    JavaFilter filter = new JavaFilter();
    
    boolean result = filter.filterLine("import test.test");
    
    assertThat(result, is(true));
  }

  @Test
  public void testFilterLine_importWithSpaces() throws Exception {
    JavaFilter filter = new JavaFilter();
    
    boolean result = filter.filterLine("   import test.test");
    
    assertThat(result, is(true));
  }

}
