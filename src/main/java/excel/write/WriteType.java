package excel.write;

public enum WriteType {
  /**
   * 如果数据过多将会生成多个Sheet，写在一个文件里
   */
  MULTI_SHEET,
  /**
   * 如果数据过多将会生成多个文件，每个文件只有一个Sheet
   */
  SINGLE_SHEET;

  public boolean isMultiSheet() {
    return this == WriteType.MULTI_SHEET;
  }

  public boolean isSingleSheet() {
    return this == WriteType.SINGLE_SHEET;
  }

}
