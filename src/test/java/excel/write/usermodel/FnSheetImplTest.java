package excel.write.usermodel;

import excel.write.AbstractCommonPath;
import excel.write.annotation.CellStyle;
import excel.write.annotation.Column;
import excel.write.internal.NameSequence;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FnSheetImplTest extends AbstractCommonPath {

  @Test
  public void test() throws IOException {
    NameSequence sequence = NameSequence.full("copy-row", ".xlsx");
    List<List<Row>> oldRow = getOldRow(2, 100);
    List<Row> headers = oldRow.get(0);
    List<Row> contents = oldRow.get(1);
    XSSFWorkbook workbook = XSSFWorkbookFactory.createWorkbook();
    FnSheet fnSheet = create(workbook.createSheet(), headers);
    Random random = new Random();
    for (int i = 0; i < 100; i++) {
      if (fnSheet.offset() > 30) {
        Path path = Paths.get(getBasePath(), sequence.next());
        System.out.println("file path: " + path.toString());
        try (OutputStream os = new FileOutputStream(path.toFile())) {
          workbook.write(os);
          workbook.close();
        }
        workbook = XSSFWorkbookFactory.createWorkbook();
        fnSheet = create(workbook.createSheet(), headers);
      }
      String i1 = random.nextInt(1000) + "";
      Model model = new Model(i1, i1);
      Model2 model2 = new Model2(i1, i1);
      Row cells = contents.get(i);
      fnSheet.write(cells, model, model2);
    }
    Path path = Paths.get(getBasePath(), sequence.next());
    System.out.println("file path: " + path.toString());
    try (OutputStream os = new FileOutputStream(path.toFile())) {
      workbook.write(os);
      workbook.close();
    }
  }

  private FnSheet create(XSSFSheet sheet, List<Row> headers) {
    FnSheetImpl fnSheet = new FnSheetImpl(sheet);

    fnSheet.addMultiHeader(headers);
    fnSheet.addHeader(Model.class);
    fnSheet.addHeader(Model2.class);
    fnSheet.writeHeader();
    return fnSheet;
  }


  private List<List<Row>> getOldRow(int headerSize, int contentSize) {
    List<List<Row>> rows = new ArrayList<>(2);
    try (Workbook workbook = XSSFWorkbookFactory.createWorkbook()) {
      Sheet sheet = workbook.createSheet();
      List<Row> headers = new ArrayList<>();
      int offset = 0;
      for (; offset < headerSize; offset++) {
        Row row = sheet.createRow(offset);
        row.createCell(0).setCellValue("A");
        row.createCell(1).setCellValue("B");
        row.createCell(2).setCellValue("C");
        row.createCell(3).setCellValue("D");
        row.createCell(4).setCellValue("E");
        headers.add(row);
      }
      rows.add(headers);
      List<Row> contents = new ArrayList<>();
      for (; offset < contentSize + headerSize; offset++) {
        Row row = sheet.createRow(offset);
        row.createCell(0).setCellValue(offset + "A");
        row.createCell(1).setCellValue(offset + "B");
        row.createCell(2).setCellValue(offset + "C");
        row.createCell(3).setCellValue(offset + "D");
        row.createCell(4).setCellValue(offset + "E");
        contents.add(row);
      }
      rows.add(contents);
      return rows;
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @CellStyle
  public static class Model {
    @Column(value = {"a", "b"})
    private String a;
    @Column(value = {"a", "b1"})
    private String b;

    public Model(String a, String b) {
      this.a = a;
      this.b = b;
    }

    public String getA() {
      return a;
    }

    public void setA(String a) {
      this.a = a;
    }

    public String getB() {
      return b;
    }

    public void setB(String b) {
      this.b = b;
    }
  }


  @CellStyle
  public static class Model2 {
    @Column(value = {"a", "b"})
    private String a;
    @Column(value = {"a", "b1"})
    private String b;

    public Model2(String a, String b) {
      this.a = a;
      this.b = b;
    }

    public String getA() {
      return a;
    }

    public void setA(String a) {
      this.a = a;
    }

    public String getB() {
      return b;
    }

    public void setB(String b) {
      this.b = b;
    }
  }
}