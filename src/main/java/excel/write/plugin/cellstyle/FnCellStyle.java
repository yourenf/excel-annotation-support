package excel.write.plugin.cellstyle;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public interface FnCellStyle {

  /**
   * 设置Color
   *
   * @param color
   */
  default void setColor(java.awt.Color color) {

  }

  CellStyle createCellStyle(Workbook workbook);

}