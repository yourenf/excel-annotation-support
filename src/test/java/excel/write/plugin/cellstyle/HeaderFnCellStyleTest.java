package excel.write.plugin.cellstyle;

import org.junit.Test;

import java.awt.*;
import java.io.File;

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


  public static void main(String[] args) {
    String JAVA_IO_TMPDIR = "java.io.tmpdir";

    String POIFILES = "poifiles";


    String tmpDir = System.getProperty(JAVA_IO_TMPDIR);
    System.out.println(tmpDir);
    if (tmpDir == null) {
      throw new RuntimeException(
              "Systems temporary directory not defined - set the -D" + JAVA_IO_TMPDIR + " jvm property!");
    }
    File directory = new File(tmpDir, POIFILES);
    if (!directory.exists()) {
//        syncCreatePOIFilesDirectory(directory);
    }
  }
}