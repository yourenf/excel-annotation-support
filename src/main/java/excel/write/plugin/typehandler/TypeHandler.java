package excel.write.plugin.typehandler;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

public interface TypeHandler<T> {
  /**
   * 创建这个类型特有的CellStyle
   *
   * @param workbook
   */
  void initCellStyle(Workbook workbook);

  /**
   * @param cell  cell
   * @param value cellValue 可能为null
   */
  void handler(Cell cell, T value);
}
