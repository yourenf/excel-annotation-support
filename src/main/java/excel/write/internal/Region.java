package excel.write.internal;

import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Optional;

public class Region {
  private int minRow;
  private int maxRow;
  private int minCol;
  private int maxCol;

  public Region(int minRow, int maxRow, int minCol, int maxCol) {
    this.minRow = minRow;
    this.maxRow = maxRow;
    this.minCol = minCol;
    this.maxCol = maxCol;
  }

  public Optional<CellRangeAddress> getCellRangeAddress(int rowOffset, int colOffset) {
    int minRow = this.minRow + rowOffset;
    int maxRow = this.maxRow + rowOffset;
    int minCol = this.minCol + colOffset;
    int maxCol = this.maxCol + colOffset;
    if (maxRow < 0 || maxRow < 0 || minCol < 0 || maxCol < 0) {
      return Optional.empty();
    }
    CellRangeAddress region = new CellRangeAddress(minRow, maxRow, minCol, maxCol);
    return Optional.of(region);
  }

  public int getMinRow() {
    return minRow;
  }

  public int getMaxRow() {
    return maxRow;
  }

  public int getMinCol() {
    return minCol;
  }

  public int getMaxCol() {
    return maxCol;
  }
}
