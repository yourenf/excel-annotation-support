package excel.write;

import excel.write.usermodel.FnSheet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public interface ExcelWriter {

  void initHeader(Consumer<FnSheet> header);

  /**
   * @param items
   * @return rowNum
   */
  int write(Object... items) throws IOException;

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
   * @return
   */
  List<File> getFiles();
}
