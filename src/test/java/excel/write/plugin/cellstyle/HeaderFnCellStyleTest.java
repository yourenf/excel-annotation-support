package excel.write.plugin.cellstyle;

import org.junit.Test;

public class HeaderFnCellStyleTest {

  @Test
  public void createCellStyle() {
    java.awt.Color color = new java.awt.Color(254, 230, 153);
    int rgb = color.getRGB();
    System.out.println(color.getRed());
    System.out.println(color.getGreen());
    System.out.println(color.getBlue());
    System.out.println((short) color.getBlue());
  }
}