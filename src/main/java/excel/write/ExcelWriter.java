package excel.write;

import excel.write.usermodel.FnSheet;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public interface ExcelWriter extends AutoCloseable {

  static ExcelWriter create(String filename) {
    Objects.requireNonNull(filename);
    return new ExcelWriterImpl(SheetConfig.builder().fileName(filename).build());
  }

  static ExcelWriter create(SheetConfig config) {
    Objects.requireNonNull(config);
    return new ExcelWriterImpl(config);
  }

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
