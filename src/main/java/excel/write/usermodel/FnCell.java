package excel.write.usermodel;

import excel.util.ReflectUtil;
import excel.write.plugin.typehandler.TypeHandler;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Method;

public interface FnCell<T> {
  int index();

  void accept(FnRow row, T t);

  class ContentCell<T> implements FnCell<T> {
    private int index;
    private Method getter;
    private TypeHandler typeHandler;

    public ContentCell(int index, TypeHandler typeHandler, Method getter) {
      this.index = index;
      this.typeHandler = typeHandler;
      this.getter = getter;
    }

    @Override
    public int index() {
      return index;
    }

    @Override
    public void accept(FnRow row, T item) {
      Object value = ReflectUtil.getValue(getter, item);
      Cell cell = row.allocateCell();
      typeHandler.handler(cell, value);

    }
  }
}
