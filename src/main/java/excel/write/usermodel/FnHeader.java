package excel.write.usermodel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.List;

public interface FnHeader {
  /**
   * @return 列顺序
   */
  int index();

  List<String> values();

  void accept(List<FnRow> rows);


  class HeaderCell implements FnHeader {
    private int index;
    private int width;
    private List<String> values;
    private CellStyle cellStyle;

    public HeaderCell(int index, int width, List<String> values) {
      this(index, width, values, null);
    }

    public HeaderCell(int index, int width, List<String> values, CellStyle cellStyle) {
      this.index = index;
      this.width = width;
      this.values = values;
      this.cellStyle = cellStyle;
    }

    @Override
    public int index() {
      return index;
    }

    @Override
    public List<String> values() {
      return values;
    }

    @Override
    public void accept(List<FnRow> rows) {
      for (int i = 0; i < values.size(); i++) {
        String value = values.get(i);
        FnRow row = rows.get(i);
        Cell cell = row.allocateCell();
        if (width > 0) {
          int columnIndex = row.offset() - 1;
          cell.getRow().getSheet().setColumnWidth(columnIndex, width);
        }
        cell.setCellValue(value);
        if (cellStyle != null) {
          cell.setCellStyle(cellStyle);
        }
      }

    }
  }

}
