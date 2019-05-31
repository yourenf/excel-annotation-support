package excel.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectResolverTest {

  @Test
  public void getProperties() {
    List<String> collect = ObjectResolver.INSTANCE.getProperties(Model.class)
            .stream().map(Property::getName).collect(Collectors.toList());
    System.out.println(collect);
    List<String> strings = Arrays.asList("a", "b", "ra", "c");
    Assert.assertArrayEquals(strings.toArray(), collect.toArray());
  }


  public static class Model {
    private String a;
    private String b;
    private String ra;
    private String c;

    public String getA() {
      return a;
    }

    public void setA(String a) {
      this.a = a;
    }

    public String getB() {
      return b;
    }

    public void setB(String b) {
      this.b = b;
    }

    public String getC() {
      return c;
    }

    public String getRa() {
      return ra;
    }

    public void setC(String c) {
      this.c = c;
    }


  }
}