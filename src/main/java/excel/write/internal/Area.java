package excel.write.internal;

import java.util.ArrayList;
import java.util.List;

public class Area {
  private int key;
  private List<Point> points;


  public Area(int key) {
    this.key = key;
  }

  public void add(Point p) {
    if (points == null) {
      this.points = new ArrayList<>();
    }
    points.add(p);
  }

  public Region getRegion() {
    if (points.size() <= 1) {
      throw new IllegalArgumentException("只有一个点，不是一个有效的Area");
    }
    int minRow = points.stream().mapToInt(Point::row).min().getAsInt();
    int maxRow = points.stream().mapToInt(Point::row).max().getAsInt();
    int minCol = points.stream().mapToInt(Point::col).min().getAsInt();
    int maxCol = points.stream().mapToInt(Point::col).max().getAsInt();
    if (maxRow < 0 || maxRow < 0 || minCol < 0 || maxCol < 0) {
      throw new IllegalArgumentException("越界");
    }
    return new Region(minRow, maxRow, minCol, maxCol);
  }

  public int size() {
    if (points == null) {
      return 0;
    }
    return points.size();
  }

  @Override
  public String toString() {
    return "Area{" +
            "key=" + key +
            ", points=" + points +
            '}';
  }
}
