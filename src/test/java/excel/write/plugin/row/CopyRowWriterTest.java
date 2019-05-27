package excel.write.plugin.row;

import excel.util.UUIDUtils;
import excel.write.AbstractCommonPath;
import excel.write.plugin.cellstyle.HeaderFnCellStyle;
import excel.write.usermodel.FnRow;
import excel.write.usermodel.FnRowImpl;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;
import org.junit.Assert;
import org.junit.Test;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class CopyRowWriterTest extends AbstractCommonPath {

  @Test
  public void init() throws IOException {
    Row header = createWorkbookRow();
    CopyRowWriter writer = new CopyRowWriter(Arrays.asList(header));
    Assert.assertEquals(10, writer.columnSize());
    Assert.assertTrue(writer.support(Row.class));
    Workbook workbook = XSSFWorkbookFactory.createWorkbook();
    Sheet sheet = workbook.createSheet();
    writer.init(sheet, 1);
    Row row = sheet.createRow(0);
    List<FnRow> rows = Arrays.asList(new FnRowImpl(row));
    writer.writeHeaders(rows);
    File file = Paths.get(getBasePath(), UUIDUtils.getHalfId() + ".xlsx").toFile();
    try (OutputStream os = new FileOutputStream(file)) {
      workbook.write(os);
    }
    workbook.close();
    System.out.println(file);
  }


  @Test
  public void writeHeaders() {
  }

  @Test
  public void getMergeHeaders() {
  }

  @Test
  public void write() {
  }

  private Row createWorkbookRow() throws IOException {
    try (Workbook workbook = new XSSFWorkbook()) {
      Sheet sheet = workbook.createSheet();
      Row row = sheet.createRow(0);
      HeaderFnCellStyle style = new HeaderFnCellStyle();
      style.setColor(new Color(254, 250, 153));
      CellStyle cellStyle = style.createCellStyle(workbook);
      CreationHelper creationHelper = workbook.getCreationHelper();
      for (int i = 0; i < 10; i++) {
        Cell cell = row.createCell(i);
        cell.setCellValue(i + "a");
        cell.setCellStyle(cellStyle);
        Hyperlink hyperlink = creationHelper.createHyperlink(HyperlinkType.URL);
        hyperlink.setAddress("http://www.baidu.com");
        hyperlink.setLabel("aaa");
        cell.setHyperlink(hyperlink);
      }
      return row;
    }
  }
}