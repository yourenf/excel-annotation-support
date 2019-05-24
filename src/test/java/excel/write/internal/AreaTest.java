package excel.write.internal;

import org.junit.Assert;
import org.junit.Test;

public class AreaTest {

  @Test
  public void add() {
    Area area = new Area(1);
    area.add(new Point(0, 0));
    area.add(new Point(0, 1));
    Assert.assertEquals(2, area.size());
  }

  @Test
  public void getRegion() {
    Area area = new Area(1);
    area.add(new Point(0, 0));
    area.add(new Point(0, 1));
    Region region = area.getRegion();
    Assert.assertEquals(0, region.getMinRow());
    Assert.assertEquals(0, region.getMaxRow());
    Assert.assertEquals(0, region.getMinCol());
    Assert.assertEquals(1, region.getMaxCol());
  }


  @Test
  public void getRegion2() {
    Area area = new Area(1);
    area.add(new Point(0, 0));
    area.add(new Point(0, 1));
    area.add(new Point(1, 0));
    area.add(new Point(1, 1));
    Region region = area.getRegion();
    Assert.assertEquals(0, region.getMinRow());
    Assert.assertEquals(1, region.getMaxRow());
    Assert.assertEquals(0, region.getMinCol());
    Assert.assertEquals(1, region.getMaxCol());
  }

  @Test
  public void size() {
    Area area = new Area(1);
    Assert.assertEquals(0, area.size());
  }
}