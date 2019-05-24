package excel.write.plugin.row;

import excel.write.internal.Area;
import excel.write.internal.HeaderArea;
import excel.write.internal.Matrix;
import excel.write.internal.Region;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class ObjectHeader {
  /**
   * 输入字符串与数字的映射关系
   */
  private final Map<String, Integer> relations = new HashMap<>();

  private List<List<String>> headers;
  private boolean transpose;

  public ObjectHeader(List<List<String>> headers) {
    this(headers, true);
  }

  /**
   * 说明  每一行是一个List<String>
   * <p>
   * 不需要转置
   * '' |   A  |   B    |   C
   * -------------------------
   * 1  |  'a' |   'b'  |   'c'
   * - - - - - - - - - - - - -
   * 2  |  'a' |   'b'  |   'c'
   * - - - - - - - - - - - - -
   * <p>
   * 需要转置
   * '' |  1   |   2
   * ---------------------
   * A  | 'a'  |  'a'
   * - - - - - - - - - -
   * B  | 'b'  |  'b'
   * - - - - - - - - - -
   * C  | 'c'  |  'c'
   * - - - - - - - - - -
   *
   * @param headers
   * @param transpose 是否进行矩阵转置
   */
  public ObjectHeader(List<List<String>> headers, boolean transpose) {
    this.headers = headers;
    this.transpose = transpose;
  }

  public Consumer<Sheet> getMergeHeaders(int rowOffset, int colOffset) {
    Matrix grid;
    if (transpose) {
      grid = create(headers).transpose();
    } else {
      grid = create(headers);
    }
    HeaderArea headerArea = new HeaderArea(grid);
    Map<Integer, String> map = new HashMap<>();
    relations.forEach((k, v) -> map.put(v, k));
    List<Area> areas = headerArea.getAreas();
    return sheet -> {
      for (Area area : areas) {
        Region region = area.getRegion();
        Matrix matrix = headerArea.getSubMatrix(region);
        Set<Integer> set = new HashSet<>();
        matrix.forEach(i -> set.add(i));
        if (set.size() > 1) {
          String error = error(matrix, map);
          throw new IllegalArgumentException("不能合并单元格" + error);
        }
        region.getCellRangeAddress(rowOffset, colOffset)
                .ifPresent(r -> sheet.addMergedRegionUnsafe(r));
      }
    };
  }

  private String error(Matrix matrix, Map<Integer, String> relations) {
    StringBuilder s = new StringBuilder(matrix.getRowDimension() * matrix.getColumnDimension() * 10);
    s.append('\n');
    int[][] array = matrix.getArray();
    for (int[] ints : array) {
      for (int i : ints) {
        s.append(relations.get(i)).append('\t');
      }
      s.append('\n');
    }
    s.trimToSize();
    return s.toString();
  }

  private Matrix create(List<List<String>> headers) {
    AtomicInteger seq = new AtomicInteger(1);
    int[][] x = new int[headers.size()][];
    for (int i = 0; i < headers.size(); i++) {
      List<String> list = headers.get(i);
      int[] ints = new int[list.size()];
      for (int j = 0; j < list.size(); j++) {
        String s = list.get(j);
        Integer value = relations.computeIfAbsent(s, k -> seq.getAndIncrement());
        ints[j] = value;
      }
      x[i] = ints;
    }
    return new Matrix(x);
  }


}
