package excel.write.internal;

import org.junit.Assert;
import org.junit.Test;

public class NameSequenceTest {

  @Test
  public void empty() {
    NameSequence sequence = NameSequence.empty();
    Assert.assertEquals("01", sequence.next());
    Assert.assertEquals("02", sequence.next());
    Assert.assertEquals("02", sequence.current());
  }

  @Test
  public void prefix() {
    NameSequence sequence = NameSequence.prefix("sheet");
    Assert.assertEquals("sheet01", sequence.next());
    Assert.assertEquals("sheet02", sequence.next());
    Assert.assertEquals("sheet02", sequence.current());
  }

  @Test
  public void full() {
    NameSequence sequence = NameSequence.full("f", ".txt");
    Assert.assertEquals("f01.txt", sequence.next());
    Assert.assertEquals("f02.txt", sequence.next());
    Assert.assertEquals("f02.txt", sequence.current());
  }

  @Test
  public void replaceSpace() {
    NameSequence sequence = NameSequence.full("f f ", ".txt");
    Assert.assertEquals("f_f_01.txt", sequence.next());
    Assert.assertEquals("f_f_02.txt", sequence.next());
    Assert.assertEquals("f_f_02.txt", sequence.current());
  }
}
