package excel.write.usermodel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

public interface FnSheet {

  Row allocateRow();

  /**
   * @return 下一行rowNum
   */
  int offset();

  /**
   * @param multiHeader 另一个excel的多行header
   */
  void addCopyRowHeader(List<Row> multiHeader);

  /**
   * @param row 另一个excel的单行header
   */
  void addCopyRowHeader(Row row);

  void addHeader(Class<?> type);

  void writeHeader();

  void write(Object... items);

  /**
   * 合并单元格
   *
   * @param region
   */
  void addMergedRegion(CellRangeAddress region);

  void addMergedRegionUnsafe(CellRangeAddress region);


}
