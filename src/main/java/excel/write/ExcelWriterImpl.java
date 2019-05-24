package excel.write;

import excel.ExcelException;
import excel.write.usermodel.FnSheet;
import excel.write.usermodel.FnSheetImpl;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExcelWriterImpl implements ExcelWriter {
  private SheetConfig config;
  private boolean closed = false;
  private boolean init = false;
  private Consumer<FnSheet> header;
  private Workbook workbook = null;
  private FnSheet fnSheet = null;

  public ExcelWriterImpl(SheetConfig config) {
    this.config = config;
  }

  @Override
  public void initHeader(Consumer<FnSheet> header) {
    this.header = Objects.requireNonNull(header);
  }

  @Override
  public int write(Object... items) throws IOException {
    if (Objects.isNull(header)) {
      throw new ExcelException("未初始化header");
    }
    if (!init) {
      init();
    }
    if (fnSheet.offset() >= config.getLimit()) {
      flush();
      init();
    }
    fnSheet.write(items);
    return fnSheet.offset() - 1;
  }

  @Override
  public void addMergedRegion(int startRowOffset, int endRowOffset, int startColumnOffset, int endColumnOffset) {
    if (!init) {
      init();
    }
    CellRangeAddress region = new CellRangeAddress(startRowOffset, endRowOffset, startColumnOffset, endColumnOffset);
    fnSheet.addMergedRegionUnsafe(region);
  }

  private void init() {
    init = true;
    Sheet sheet;
    if (config.getWriteType().isSingleSheet()) {
      workbook = config.getExcelType().createEmptyWorkbook();
      sheet = workbook.createSheet(config.getSheetName().current());
    } else {
      if (Objects.isNull(workbook)) {
        workbook = config.getExcelType().createEmptyWorkbook();
      }
      sheet = workbook.createSheet(config.getSheetName().next());
    }
    fnSheet = new FnSheetImpl(sheet);
    header.accept(fnSheet);
    if (fnSheet.offset() >= config.getLimit()) {
      try {
        workbook.close();
      } catch (IOException e) {
      }
      throw new IllegalArgumentException("limit 参数配置太小");
    }
  }

  private void flush() throws IOException {
    if (config.getWriteType().isSingleSheet()) {
      File file = config.getNewFile();
      try (OutputStream os = new FileOutputStream(file)) {
        workbook.write(os);
        workbook.close();
      }
    }
  }

  private void close() {
    if (closed) {
      return;
    }
    closed = true;
    File file = config.getNewFile();
    try (OutputStream os = new FileOutputStream(file)) {
      workbook.write(os);
      workbook.close();
    } catch (IOException e) {
      throw new ExcelException(e);
    }
  }

  @Override
  public List<File> getFiles() {
    close();
    try (Stream<Path> pathStream = Files.list(config.getDir())) {
      return pathStream.map(Path::toFile).collect(Collectors.toList());
    } catch (IOException e) {
      throw new ExcelException(e);
    }
  }
}
