package excel.write.usermodel;

import excel.ExcelException;
import excel.util.ObjectResolver;
import excel.write.annotation.Column;
import excel.write.plugin.row.CopyRowWriter;
import excel.write.plugin.row.ObjectRowWriter;
import excel.write.plugin.row.RowWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FnSheetImpl implements FnSheet {
  private int offset = 0;
  private Sheet sheet;
  private List<RowWriter<?>> writers;
  private int height;
  private boolean done = false;

  public FnSheetImpl(Sheet sheet) {
    this.sheet = sheet;
    this.writers = new ArrayList<>();
  }

  @Override
  public Row allocateRow() {
    Row row = sheet.createRow(offset);
    offset++;
    return row;
  }

  @Override
  public int offset() {
    return offset;
  }

  @Override
  public void addMergedRegion(CellRangeAddress region) {
    Objects.requireNonNull(region);
    sheet.addMergedRegion(region);
  }

  @Override
  public void addMergedRegionUnsafe(CellRangeAddress region) {
    Objects.requireNonNull(region);
    sheet.addMergedRegionUnsafe(region);
  }

  private List<FnRow> allocateFnRow(int size) {
    if (size <= 0) {
      return Collections.emptyList();
    }
    return Stream.generate(this::allocateFnRow).limit(size).collect(Collectors.toList());
  }

  private FnRow allocateFnRow() {
    return new FnRowImpl(allocateRow());
  }

  @Override
  public void addCopyRowHeader(List<Row> multiHeader) {
    height = Math.max(height, multiHeader.size());
    CopyRowWriter writer = new CopyRowWriter(multiHeader);
    writers.add(writer);
  }

  @Override
  public void addCopyRowHeader(Row row) {
    height = Math.max(height, 1);
    CopyRowWriter writer = new CopyRowWriter(Arrays.asList(row));
    writers.add(writer);
  }

  @Override
  public void addHeader(Class<?> type) {
    List<Column> collect = ObjectResolver.INSTANCE.getProperties(type).stream()
            .map(r -> r.getAnnotation(Column.class))
            .filter(Objects::nonNull).collect(Collectors.toList());
    if (collect.isEmpty()) {
      throw new ExcelException(type + " 没有找到 excel.write.annotation.Column 注解");
    }
    int h = collect.stream()
            .mapToInt(column -> column.value().length)
            .max().getAsInt();
    height = Math.max(height, h);
    RowWriter<?> writer = new ObjectRowWriter(type);
    writers.add(writer);
  }

  private void initWriter() {
    if (done) {
      return;
    }
    done = true;
    if (writers.isEmpty()) {
      throw new IllegalArgumentException("未添加header");
    }
    for (RowWriter<?> writer : writers) {
      writer.init(sheet.getWorkbook(), height);
    }
  }

  @Override
  public void writeHeader() {
    initWriter();
    int rowOffset = offset;
    int colOffset = 0;
    List<FnRow> rows = allocateFnRow(height);
    for (RowWriter<?> writer : writers) {
      writer.writeHeaders(rows);
      writer.getMergeHeaders(rowOffset, colOffset).accept(sheet);
      colOffset += writer.columnSize();
    }
  }

  @Override
  public void write(Object... items) {
    initWriter();
    Objects.requireNonNull(items);
    FnRow row = allocateFnRow();
    int i = 0;
    for (RowWriter writer : writers) {
      if (i >= items.length) {
        break;
      }
      Object item = items[i];
      if (Objects.nonNull(item)) {
        if (writer.support(item.getClass())) {
          writer.write(row, item);
        } else {
          throw new ExcelException("not support type " + item.getClass());
        }
      } else {
        writer.write(row, null);
      }
      i++;
    }
  }

}
