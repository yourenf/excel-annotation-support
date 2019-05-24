package excel.write.internal;

import java.text.DecimalFormat;
import java.util.Objects;

public class NameSequence {
  private static final DecimalFormat TWO_DIGITS = new DecimalFormat("00");
  private int seq = 0;
  private String prefix;
  private String suffix;

  public static NameSequence empty() {
    return new NameSequence("", "");
  }

  public static NameSequence prefix(String prefix) {
    return new NameSequence(prefix, "");
  }

  public static NameSequence full(String prefix, String suffix) {
    return new NameSequence(prefix, suffix);
  }

  private NameSequence(String prefix, String suffix) {
    this.prefix = replaceSpace(prefix);
    this.suffix = replaceSpace(suffix);
  }

  private String replaceSpace(String value) {
    Objects.requireNonNull(value);
    return value.replace(" ", "_");
  }

  public String current() {
    return prefix + TWO_DIGITS.format(seq) + suffix;
  }

  public String next() {
    seq++;
    return prefix + TWO_DIGITS.format(seq) + suffix;
  }
}
