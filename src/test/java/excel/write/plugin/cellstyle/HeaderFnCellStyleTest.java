package excel.write.plugin.cellstyle;

import org.junit.Test;

import java.awt.*;

public class HeaderFnCellStyleTest {

  @Test
  public void createCellStyle() {
    java.awt.Color color2 = new java.awt.Color(254, 230, 153);
    int rgb = color2.getRGB();
    System.out.println(rgb);
    Color color = new Color(rgb);
    System.out.println(color.getRed());
    System.out.println(color.getGreen());
    System.out.println(color.getBlue());
    System.out.println((short) color.getBlue());
  }
}