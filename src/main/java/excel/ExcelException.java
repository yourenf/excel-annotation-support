package excel;

public class ExcelException extends RuntimeException {

  public ExcelException() {
    super();
  }

  public ExcelException(String msg) {
    super(msg);
  }

  public ExcelException(Exception e) {
    super(e);
  }

  public ExcelException(String msg, Exception e) {
    super(msg, e);
  }

  public ExcelException(String format, Object... msg) {
    super(String.format(format, msg));
  }
}