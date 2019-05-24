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
import java.util.stream.Stream;

public class SheetConfig {
  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SheetConfig.class);
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
    dir = checkDir(builder.dir);
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
   * 如果dir不为空会新建一个子文件夹使用
   *
   * @param dir
   * @return
   */
  private Path checkDir(String dir) {
    if (Objects.isNull(dir) || dir.trim().isEmpty()) {
      throw new ExcelException("存放结果的文件夹不能为空");
    }
    Path path = Paths.get(dir);
    if (!path.toFile().exists()) {
      throw new ExcelException("存放结果的文件夹" + path.toString() + "不存在");
    }
    if (Files.isRegularFile(path)) {
      throw new ExcelException(path.toString() + "不是文件夹");
    }
    if (!Files.isReadable(path)) {
      throw new ExcelException(path.toString() + "不可读");
    }
    if (!Files.isWritable(path)) {
      throw new ExcelException(path.toString() + "不可写");
    }
    try (Stream<Path> list = Files.list(path)) {
      if (list.count() > 0) {
        String uuid = UUIDUtils.getId();
        log.info("[{}]不为空,将在该目录下创建[{}]目录并使用该目录", dir, uuid);
        return Paths.get(dir, uuid);
      } else {
        return path;
      }
    } catch (IOException e) {
      throw new ExcelException(e);
    }
  }

  public static final class Builder {
    private String dir;
    private String fileName;
    /**
     * 默认使用ExcelType limit
     */
    private int limit = -1;
    private String sheetName = "Sheet";
    private ExcelType excelType = ExcelType.XLSX;
    private WriteType writeType = WriteType.SINGLE_SHEET;

    public Builder dir(String val) {
      dir = val;
      return this;
    }

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
