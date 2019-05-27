package excel.write.plugin.row;

import excel.write.usermodel.FnRow;
import org.apache.poi.ss.usermodel.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 *
 */
public class CopyRowWriter implements RowWriter<Row> {
  private List<Row> headers;
  private int colSize;
  private Map<Integer, CellStyle> cellStyleMap = new HashMap<>();
  private Drawing<?> patriarch;
  private CreationHelper creationHelper;

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
  public void init(Sheet sheet, int headerAlign) {
    patriarch = sheet.createDrawingPatriarch();
    creationHelper = sheet.getWorkbook().getCreationHelper();
  }

  @Override
  public int columnSize() {
    return colSize;
  }

  @Override
  public boolean support(Class<?> type) {
    return Row.class.isAssignableFrom(type);
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
      Cell source = copyFromRow.getCell(i);
      Cell target = row.allocateCell();
      if (Objects.isNull(copyFromRow) || Objects.isNull(source)) {
        continue;
      }
      copyCellValue(source, target);
      copyComment(source, target);
      copyHyperlink(source, target);
      copyCellStyle(source, target);
    }
  }


  private void copyCellValue(Cell source, Cell target) {
    switch (source.getCellType()) {
      case STRING:
        target.setCellValue(source.getStringCellValue());
        break;
      case BOOLEAN:
        target.setCellValue(source.getBooleanCellValue());
        break;
      case FORMULA:
        target.setCellFormula(source.getCellFormula());
        break;
      case NUMERIC:
        target.setCellValue(source.getNumericCellValue());
        break;
      case _NONE:
      case BLANK:
      case ERROR:
      default:
        break;
    }
  }

  private void copyCellStyle(Cell source, Cell target) {
    Workbook targetWorkbook = target.getSheet().getWorkbook();
    CellStyle cellStyle = cellStyleMap.computeIfAbsent(source.getCellStyle().hashCode(), k -> {
      CellStyle targetCellStyle = targetWorkbook.createCellStyle();
      targetCellStyle.cloneStyleFrom(source.getCellStyle());
      return targetCellStyle;
    });
    target.setCellStyle(cellStyle);
  }

  /**
   * If there is a cell comment, copy
   *
   * @param source
   * @param target
   */
  private void copyComment(Cell source, Cell target) {
    if (Objects.equals(source.getClass().getName(), "com.monitorjbl.xlsx.impl.StreamingCell")) {
      return;
    }
    Comment comment = source.getCellComment();
    if (Objects.isNull(comment)) {
      return;
    }
    ClientAnchor clientAnchor = comment.getClientAnchor();
    Comment newComment = patriarch.createCellComment(clientAnchor);
    newComment.setAuthor(comment.getAuthor());
    newComment.setColumn(comment.getColumn());
    newComment.setString(comment.getString());
    newComment.setRow(comment.getRow());
    newComment.setVisible(comment.isVisible());
    target.setCellComment(newComment);
  }

  /**
   * If there is a cell hyperlink, copy
   */
  private void copyHyperlink(Cell source, Cell target) {
    if (Objects.equals(source.getClass().getName(), "com.monitorjbl.xlsx.impl.StreamingCell")) {
      return;
    }
    Hyperlink sh = source.getHyperlink();
    if (Objects.isNull(sh)) {
      return;
    }
    Hyperlink th = creationHelper.createHyperlink(sh.getType());
    th.setAddress(sh.getAddress());
    th.setLabel(sh.getLabel());
    target.setHyperlink(th);
  }


}
