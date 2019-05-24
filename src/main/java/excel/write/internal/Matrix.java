package excel.write.internal;

import java.util.function.Consumer;
import java.util.function.Function;

public class Matrix {

  private int[][] a;

  /**
   * Row and column dimensions.
   *
   * @serial row dimension.
   * @serial column dimension.
   */
  private int m;
  private int n;

  public Matrix(int m, int n) {
    this.m = m;
    this.n = n;
    a = new int[m][n];
  }

  public Matrix(int m, int n, int s) {
    this.m = m;
    this.n = n;
    a = new int[m][n];
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        a[i][j] = s;
      }
    }
  }

  public Matrix(int[][] arr) {
    m = arr.length;
    n = arr[0].length;
    for (int i = 0; i < m; i++) {
      if (arr[i].length != n) {
        throw new IllegalArgumentException("All rows must have the same length.");
      }
    }
    this.a = arr;
  }

  /**
   * Make a one-dimensional column packed copy of the internal array.
   *
   * @return Matrix elements packed in a one-dimensional array by columns.
   */
  public int[] getColumnPackedCopy() {
    int[] values = new int[m * n];
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        values[i + j * m] = a[i][j];
      }
    }
    return values;
  }

  /**
   * Make a one-dimensional row packed copy of the internal array.
   *
   * @return Matrix elements packed in a one-dimensional array by rows.
   */
  public int[] getRowPackedCopy() {
    int[] values = new int[m * n];
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        values[i * n + j] = a[i][j];
      }
    }
    return values;
  }

  /**
   * Get row dimension.
   *
   * @return m, the number of rows.
   */
  public int getRowDimension() {
    return m;
  }

  /**
   * Get column dimension.
   *
   * @return n, the number of columns.
   */
  public int getColumnDimension() {
    return n;
  }

  public Matrix copy() {
    Matrix matrix = new Matrix(m, n);
    int[][] x = matrix.getArray();
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        x[i][j] = a[i][j];
      }
    }
    return matrix;
  }

  public int get(int i, int j) {
    return a[i][j];
  }

  public void set(int i, int j, int s) {
    a[i][j] = s;
  }


  /**
   * Access the internal two-dimensional array.
   *
   * @return Pointer to the two-dimensional array of matrix elements.
   */
  public int[][] getArray() {
    return a;
  }

  /**
   * Get a submatrix.
   *
   * @param i0 Initial row index
   * @param i1 Final row index
   * @param j0 Initial column index
   * @param j1 Final column index
   * @return A(i0 : i1, j0 : j1)
   * @throws ArrayIndexOutOfBoundsException Submatrix indices
   */

  public Matrix getMatrix(int i0, int i1, int j0, int j1) {
    Matrix x = new Matrix(i1 - i0 + 1, j1 - j0 + 1);
    int[][] b = x.getArray();
    try {
      for (int i = i0; i <= i1; i++) {
        for (int j = j0; j <= j1; j++) {
          b[i - i0][j - j0] = a[i][j];
        }
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new ArrayIndexOutOfBoundsException("Submatrix indices");
    }
    return x;
  }

  public Matrix transform(Function<Integer, Integer> func) {
    Matrix matrix = new Matrix(m, n);
    int[][] x = matrix.getArray();
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        x[i][j] = func.apply(this.a[i][j]);
      }
    }
    return matrix;
  }

  public void forEach(Consumer<Integer> consumer) {
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        consumer.accept(this.a[i][j]);
      }
    }
  }

  /**
   * Matrix transpose.
   *
   * @return A'
   */
  public Matrix transpose() {
    Matrix matrix = new Matrix(n, m);
    int[][] x = matrix.getArray();
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        x[j][i] = a[i][j];
      }
    }
    return matrix;
  }

  public String print() {
    return print(null);
  }

  public String print(Function<Integer, String> print) {
    StringBuilder s = new StringBuilder(m * n * 10);
    for (int[] arr : a) {
      for (int v : arr) {
        if (print == null) {
          s.append(v);
        } else {
          String apply = print.apply(v);
          s.append(apply);
        }
        s.append('\t');
      }
      s.append('\n');
    }
    s.trimToSize();
    return s.toString();
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append("Matrix: ");
    s.append("m = ").append(m).append(',').append(' ');
    s.append("n = ").append(n).append(',').append(' ');
    s.append("arr = ");
    int i = 1;
    for (int[] ints : a) {
      s.append('[');
      int j = 1;
      for (int v : ints) {
        s.append(v);
        if (j < ints.length) {
          s.append(',').append(' ');
        }
        j++;
      }
      s.append(']');
      if (i < a.length) {
        s.append(',').append(' ');
      }
      i++;
    }
    return s.toString();
  }
}