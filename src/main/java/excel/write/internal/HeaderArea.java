package excel.write.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HeaderArea {
  /**
   * 上下左右四个方向
   */
  private static final int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
  /**
   * 自动补全并转置
   */
  private Matrix grid;
  /**
   * 是否访问过的标记
   */
  private Matrix visited;
  /**
   * 相同元素区域集合
   */
  private final List<Area> areas = new ArrayList();

  private boolean initialized = false;

  public HeaderArea(Matrix matrix) {
    this.grid = matrix;
  }

  public List<Area> getAreas() {
    init();
    return areas.stream().filter(area -> area.size() > 1).collect(Collectors.toList());
  }

  public Matrix getSubMatrix(Region region) {
    return grid.getMatrix(region.getMinRow(), region.getMaxRow(), region.getMinCol(), region.getMaxCol());
  }

  private void init() {
    if (initialized) {
      return;
    }
    initialized = true;
    this.visited = new Matrix(this.grid.getRowDimension(), this.grid.getColumnDimension(), 0);
    findArea();
  }

  private void findArea() {
    for (int i = 0; i < grid.getRowDimension(); i++) {
      for (int j = 0; j < grid.getColumnDimension(); j++) {
        if (visited.get(i, j) == 1) {
          continue;
        }
        int value = grid.get(i, j);
        Area area = new Area(value);
        dfs(i, j, area, value);
        areas.add(area);
      }
    }
  }


  private void dfs(int row, int col, Area area, int expect) {
    //越界
    if (row < 0 || row >= grid.getRowDimension() || col < 0 || col >= grid.getColumnDimension()) {
      return;
    }
    if (visited.get(row, col) == 1) {
      return;
    }
    if (grid.get(row, col) != expect) {
      return;
    }
    area.add(new Point(row, col));
    visited.set(row, col, 1);//标记访问
    for (int[] direction : directions) {
      int r = row + direction[0];
      int c = col + direction[1];
      dfs(r, c, area, expect);
    }
  }
}
