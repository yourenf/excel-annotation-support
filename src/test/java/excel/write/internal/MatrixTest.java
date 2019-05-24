package excel.write.internal;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class MatrixTest {

  @Test
  public void transpose() {
    int[][] vars = {{1, 1, 1}, {1, 2, 3}, {2, 2, 2}};
    Matrix matrix = new Matrix(vars);
    Matrix transpose = matrix.transpose();
    int[][] array = transpose.getArray();
    Assert.assertArrayEquals(new int[]{1, 1, 2}, array[0]);
    Assert.assertArrayEquals(new int[]{1, 2, 2}, array[1]);
    Assert.assertArrayEquals(new int[]{1, 3, 2}, array[2]);
  }

  @Test
  public void transform() {
    int[][] arr = {{1, 2, 2}, {1, 2, 3}, {4, 2, 6}};
    Matrix matrix = new Matrix(arr);
    AtomicInteger seq = new AtomicInteger(1);
    Matrix transform = matrix.transform(i -> {
      if (i == 1) {
        return seq.getAndIncrement();
      } else {
        return 0;
      }
    });
    int[][] array = transform.getArray();
    Assert.assertArrayEquals(new int[]{1, 0, 0}, array[0]);
    Assert.assertArrayEquals(new int[]{2, 0, 0}, array[1]);
    Assert.assertArrayEquals(new int[]{0, 0, 0}, array[2]);
  }
}