package excel.write;

import excel.write.annotation.CellStyle;
import excel.write.annotation.Column;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExcelWriterImplTest extends AbstractCommonPath {

  @Test
  public void write() throws IOException {
    SheetConfig config = SheetConfig.builder().dir(getBasePath()).fileName("aa").limit(500)
            .writeType(WriteType.SINGLE_SHEET)
            .excelType(ExcelType.XLS)
            .build();
    ExcelWriterImpl writer = new ExcelWriterImpl(config);
    writer.initHeader(fnSheet -> {
      fnSheet.addHeader(HM.class);
      fnSheet.addHeader(HM.class);
      fnSheet.writeHeader();
    });

    for (int i = 0; i < 1000; i++) {
      HM hm = new HM(i + "a");
      HM hm2 = new HM(i + "b");
      int write = writer.write(hm, hm2);
    }
    List<File> files = writer.getFiles();
    for (File file : files) {
      System.out.println(file);
    }
  }

  @CellStyle(rgb = 0xAABBCCdd)
  public static class HM {
    @Column("a")
    private String name;

    public HM(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}