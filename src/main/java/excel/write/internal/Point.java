package excel.write.internal;

import java.util.Objects;

public class Point {
  private int row;
  private int col;


  public Point(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public int row() {
    return row;
  }

  public int col() {
    return col;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Point point = (Point) o;
    return row == point.row &&
            col == point.col;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col);
  }

  @Override
  public String toString() {
    return row + ":" + col;
  }
}