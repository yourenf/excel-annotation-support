package excel.write.usermodel;

import org.apache.poi.ss.usermodel.Cell;

public interface FnRow {

  /**
   * @return new cell
   */
  Cell allocateCell();

  /**
   * @return 下一列 columnNum
   */
  int offset();
}
