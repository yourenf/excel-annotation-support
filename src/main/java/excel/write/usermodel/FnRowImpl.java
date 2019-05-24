package excel.write.usermodel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class FnRowImpl implements FnRow {

  private final Row row;
  private int offset = 0;

  public FnRowImpl(Row row) {
    this.row = row;
  }

  @Override
  public Cell allocateCell() {
    Cell cell = row.createCell(offset);
    offset++;
    return cell;
  }

  @Override
  public int offset() {
    return offset;
  }
}
