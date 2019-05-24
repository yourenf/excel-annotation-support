package excel.write.internal;

import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.Assert;
import org.junit.Test;

public class RegionTest {

  @Test
  public void getCellRangeAddress() {
    Region region = new Region(0, 0, 0, 1);
    CellRangeAddress a1 = region.getCellRangeAddress(0, 0).orElse(null);
    Assert.assertEquals("A1:B1", a1.formatAsString());

    CellRangeAddress a2 = region.getCellRangeAddress(1, 0).orElse(null);
    Assert.assertEquals("A2:B2", a2.formatAsString());

    CellRangeAddress a3 = region.getCellRangeAddress(0, 1).orElse(null);
    Assert.assertEquals("B1:C1", a3.formatAsString());


    CellRangeAddress a4 = region.getCellRangeAddress(1, 1).orElse(null);
    Assert.assertEquals("B2:C2", a4.formatAsString());

    CellRangeAddress a5 = region.getCellRangeAddress(-1, 1).orElse(null);
    Assert.assertNull(a5);
  }
}