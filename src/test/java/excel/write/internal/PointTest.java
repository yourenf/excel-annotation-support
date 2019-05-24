package excel.write.internal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PointTest {

  @Test
  public void row() {
    Point point = new Point(0, 0);
    assertEquals(0, point.row());
  }

  @Test
  public void col() {
    Point point = new Point(0, 0);
    assertEquals(0, point.row());
  }
}