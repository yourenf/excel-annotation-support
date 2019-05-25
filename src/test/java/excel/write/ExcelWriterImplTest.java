package excel.write;

import excel.write.annotation.CellStyle;
import excel.write.annotation.Column;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class ExcelWriterImplTest extends AbstractCommonPath {

  @Test
  public void write() {
    SheetConfig config = SheetConfig.builder().fileName("aa").limit(500)
            .writeType(WriteType.SINGLE_SHEET)
            .excelType(ExcelType.XLSX)
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

  @CellStyle(rgb = 0xAAFFCCdd)
  public static class HM {
    @Column(value = {"我的的的的额的额", "我的的的的额的额", "a"}, order = 1)
    private String name;
    @Column(value = {"我的的的的额的额", "我的的的的额的额", "b"}, order = 2)
    private String name2;

    public HM() {
    }

    public HM(String name) {
      this.name = name;
      this.name2 = name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getName2() {
      return name2;
    }

    public void setName2(String name2) {
      this.name2 = name2;
    }
  }
}