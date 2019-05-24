package excel.write.plugin.row;

import excel.write.usermodel.FnRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 *
 */
public class CopyRowWriter implements RowWriter<Row> {
  private List<Row> headers;
  private int colSize;

  public CopyRowWriter(List<Row> headers) {
    if (headers.isEmpty()) {
      throw new IllegalArgumentException("headers must be not empty");
    }
    this.headers = headers;
    this.colSize = maxCellNum(headers);
  }

  private int maxCellNum(List<Row> rows) {
    int max = 0;
    for (Row row : rows) {
      max = Math.max(row.getLastCellNum(), max);
    }
    return max;
  }

  @Override
  public void init(Workbook workbook, int headerAlign) {

  }

  @Override
  public int columnSize() {
    return colSize;
  }

  @Override
  public Class<?> getType() {
    return Row.class;
  }

  /**
   * headers size <= rows size
   *
   * @param rows
   */
  @Override
  public void writeHeaders(List<FnRow> rows) {
    int i = 0;
    Row temp = null;
    for (FnRow row : rows) {
      if (i < headers.size()) {
        temp = headers.get(i);
      }
      write(row, temp);
      i++;
    }
  }

  @Override
  public Consumer<Sheet> getMergeHeaders(int rowOffset, int colOffset) {
    List<List<String>> collect = headers.stream().map(row -> {
      List<String> list = new ArrayList<>();
      for (Cell cell : row) {
        list.add(cell.getStringCellValue());
      }
      return list;
    }).collect(Collectors.toList());
    ObjectHeader header = new ObjectHeader(collect, false);
    return header.getMergeHeaders(rowOffset, colOffset);
  }

  @Override
  public void write(FnRow row, Row copyFromRow) {
    for (int i = 0; i < colSize; i++) {
      Cell newCell = row.allocateCell();
      if (Objects.isNull(copyFromRow)) {
        continue;
      }
      Cell copyFromCell = copyFromRow.getCell(i);
      if (Objects.nonNull(copyFromCell)) {
        switch (copyFromCell.getCellType()) {
          case STRING:
            newCell.setCellValue(copyFromCell.getStringCellValue());
            break;
          case BOOLEAN:
            newCell.setCellValue(copyFromCell.getBooleanCellValue());
            break;
          case FORMULA:
            newCell.setCellFormula(copyFromCell.getCellFormula());
            break;
          case NUMERIC:
            newCell.setCellValue(copyFromCell.getNumericCellValue());
            break;
          case _NONE:
          case BLANK:
          case ERROR:
          default:
            break;
        }
      }

    }
  }
}
