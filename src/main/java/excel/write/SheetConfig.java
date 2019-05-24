package excel.write;

import excel.ExcelException;
import excel.util.UUIDUtils;
import excel.write.internal.NameSequence;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class SheetConfig {
  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SheetConfig.class);
  private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";
  private static final String RESULT_DIR = "fn_excel";
  private Path dir;
  /**
   * 允许写入最大的rowNum
   */
  private int limit;
  private final NameSequence filename;
  private final NameSequence sheetName;
  private ExcelType excelType;
  private WriteType writeType;

  public static Builder builder() {
    return new Builder();
  }

  private SheetConfig(Builder builder) {
    this.filename = NameSequence.full(builder.fileName, builder.excelType.getSuffix());
    this.sheetName = NameSequence.prefix(builder.sheetName);
    this.dir = getWorkSpace();
    this.limit = builder.limit;
    this.excelType = builder.excelType;
    this.writeType = builder.writeType;
  }

  public Path getDir() {
    return dir;
  }

  public int getLimit() {
    if (limit <= 0) {
      return excelType.getLimit();
    } else {
      return limit;
    }
  }

  public NameSequence getSheetName() {
    return sheetName;
  }

  public ExcelType getExcelType() {
    return excelType;
  }

  public WriteType getWriteType() {
    return writeType;
  }

  public File getNewFile() {
    return Paths.get(dir.toString(), filename.next()).toFile();
  }


  /**
   * 检查dir
   * <p>
   */
  private Path getWorkSpace() {
    String tmpDir = System.getProperty(JAVA_IO_TMPDIR);
    if (tmpDir == null) {
      throw new ExcelException(
              "Systems temporary directory not defined - set the -D" + JAVA_IO_TMPDIR + " jvm property!");
    }
    Path directory = Paths.get(tmpDir, RESULT_DIR, UUIDUtils.getId());
    if (!directory.toFile().exists()) {
      createDir(directory);
    }
    return directory;
  }

  private synchronized void createDir(Path path) {
    try {
      Files.createDirectories(path);
    } catch (IOException e) {
      throw new ExcelException(e);
    }
  }


  public static final class Builder {
    private String fileName;
    /**
     * 默认使用ExcelType limit
     */
    private int limit = -1;
    private String sheetName = "Sheet";
    private ExcelType excelType = ExcelType.XLSX;
    private WriteType writeType = WriteType.SINGLE_SHEET;

    public Builder fileName(String val) {
      fileName = val;
      return this;
    }

    public Builder limit(int val) {
      limit = val;
      return this;
    }

    public Builder sheetName(String val) {
      sheetName = val;
      return this;
    }

    public Builder excelType(ExcelType val) {
      excelType = val;
      return this;
    }

    public Builder writeType(WriteType val) {
      this.writeType = val;
      return this;
    }


    public SheetConfig build() {
      Objects.requireNonNull(fileName, "文件名称不能为空");
      return new SheetConfig(this);
    }
  }
}
