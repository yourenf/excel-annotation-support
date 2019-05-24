package excel.write;

import excel.write.usermodel.FnSheet;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public interface ExcelWriter extends AutoCloseable {

  void initHeader(Consumer<FnSheet> header);

  /**
   * @param items
   * @return rowNum
   */
  int write(Object... items);

  /**
   * Unsafe merge
   *
   * @param startRowOffset
   * @param endRowOffset
   * @param startColumnOffset
   * @param endColumnOffset
   */
  void addMergedRegion(int startRowOffset, int endRowOffset, int startColumnOffset, int endColumnOffset);

  /**
   * 关闭workbook
   */
  @Override
  void close();

  /**
   * @return
   */
  List<File> getFiles();
}
