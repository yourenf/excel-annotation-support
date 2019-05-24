package excel.write.usermodel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public interface FnHeader {
  /**
   * @return 列顺序
   */
  int index();

  List<String> values();

  void accept(List<FnRow> rows);


  class HeaderCell implements FnHeader {
    private final int index;
    private final int width;
    private final List<String> values;
    private final CellStyle cellStyle;

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
        cell.setCellValue(value);
        if (cellStyle != null) {
          cell.setCellStyle(cellStyle);
        }

        int columnIndex = row.offset() - 1;
        Sheet sheet = cell.getRow().getSheet();
        if (width > 0) {
          sheet.setColumnWidth(columnIndex, width);
        } else {
          sheet.autoSizeColumn(columnIndex);
        }
      }

    }
  }

}
