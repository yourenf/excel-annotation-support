package excel.write;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public enum ExcelType {
  XLSX(".xlsx", 2 << 19),
  XLS(".xls", 2 << 15);

  private String suffix;
  private int limit;

  ExcelType(String suffix, int limit) {
    this.suffix = suffix;
    this.limit = limit;
  }

  public String getSuffix() {
    return suffix;
  }

  public int getLimit() {
    return limit;
  }

  public Workbook createEmptyWorkbook() {
    if (this == ExcelType.XLSX) {
      return new SXSSFWorkbook();
    } else {
      return new HSSFWorkbook();
    }
  }
}
