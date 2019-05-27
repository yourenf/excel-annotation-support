package excel.write.plugin.row;

import excel.write.usermodel.FnRow;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;
import java.util.function.Consumer;

public interface RowWriter<T> {

  /**
   * @param sheet
   * @param headerAlignNum 补全header到 headerAlignNum
   */
  void init(Sheet sheet, int headerAlignNum);

  /**
   * @return column  size
   */
  int columnSize();

  /**
   * @return support type
   */
  boolean support(Class<?> type);

  void writeHeaders(List<FnRow> rows);

  Consumer<Sheet> getMergeHeaders(int rowOffset, int colOffset);

  void write(FnRow row, T item);
}
