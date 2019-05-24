package excel.write.plugin.row;

import excel.write.usermodel.FnRow;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.function.Consumer;

public interface RowWriter<T> {

  default boolean support(Class<?> type) {
    return getType().isAssignableFrom(type);
  }

  /**
   * @param workbook
   * @param headerAlignNum 补全header到 headerAlignNum
   */
  void init(Workbook workbook, int headerAlignNum);

  /**
   * @return column  size
   */
  int columnSize();

  /**
   * @return support type
   */
  Class<?> getType();

  void writeHeaders(List<FnRow> rows);

  Consumer<Sheet> getMergeHeaders(int rowOffset, int colOffset);

  void write(FnRow row, T item);
}
