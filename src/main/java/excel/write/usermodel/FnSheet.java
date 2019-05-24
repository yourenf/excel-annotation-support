package excel.write.usermodel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

public interface FnSheet {
  /**
   * 申请一行
   *
   * @return row
   */
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

  /**
   * @param type POJO
   */
  void addHeader(Class<?> type);

  /**
   * write header
   */
  void writeHeader();

  /**
   * 如果header type 分别是A、B、C ，对应的值a、b、c
   * 那么 items 参数可以这样使用
   * 1、 a,b,c
   * 2、 a,b
   * 3、 a
   * 4、 null,b,c
   * 5、 a,null,c
   * 6、 a,null,null
   * 7、 null,null,null
   *
   * @param items
   */
  void write(Object... items);

  /**
   * 合并单元格 效率低
   *
   * @param region
   */
  void addMergedRegion(CellRangeAddress region);

  /**
   * 合并单元格 效率高
   *
   * @param region
   */
  void addMergedRegionUnsafe(CellRangeAddress region);


}
