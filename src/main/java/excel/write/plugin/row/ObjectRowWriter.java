package excel.write.plugin.row;

import excel.util.ObjectResolver;
import excel.util.Property;
import excel.util.ReflectUtil;
import excel.write.annotation.CellStyle;
import excel.write.annotation.Column;
import excel.write.plugin.cellstyle.FnCellStyle;
import excel.write.plugin.typehandler.TypeHandler;
import excel.write.usermodel.FnCell;
import excel.write.usermodel.FnHeader;
import excel.write.usermodel.FnRow;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ObjectRowWriter<T> implements RowWriter<T> {
  private Class<T> type;
  private List<FnHeader> headers;
  private List<FnCell<T>> contents;

  public ObjectRowWriter(Class<T> type) {
    this.type = type;
    this.headers = new ArrayList<>();
    this.contents = new ArrayList<>();
  }

  @Override
  public void init(Workbook workbook, int maxHeight) {
    headers.clear();
    contents.clear();
    List<Property> properties = ObjectResolver.INSTANCE.getProperties(type);
    for (Property property : properties) {
      Column column = property.getAnnotation(Column.class);
      if (Objects.isNull(column)) {
        continue;
      }
      CellStyle cellStyle = Optional.ofNullable(property.getAnnotation(CellStyle.class))
              .orElse(type.getAnnotation(CellStyle.class));
      List<String> hs = completion(column.value(), maxHeight);
      FnHeader header;
      if (Objects.isNull(cellStyle)) {
        header = new FnHeader.HeaderCell(column.order(), column.width(), hs);
      } else {
        Color color = new Color(cellStyle.rgb());
        FnCellStyle f = ReflectUtil.create(cellStyle.value());
        f.setColor(color);
        header = new FnHeader.HeaderCell(column.order(), column.width(), hs, f.createCellStyle(workbook));
      }
      headers.add(header);
      TypeHandler<?> typeHandler = ReflectUtil.create(column.typeHandler());
      FnCell<T> contentCell = new FnCell.ContentCell(column.order(), typeHandler, property.getReadMethod());
      contents.add(contentCell);
    }
    headers.sort(Comparator.comparing(FnHeader::index));
    contents.sort(Comparator.comparing(FnCell::index));
  }

  private List<String> completion(String[] values, int maxHeight) {
    List<String> list = new ArrayList<>(Arrays.asList(values));
    if (maxHeight > values.length) {
      String s = list.get(values.length - 1);
      for (int i = 0; i < maxHeight - values.length; i++) {
        list.add(s);
      }
    }
    return Collections.unmodifiableList(list);
  }

  @Override
  public int columnSize() {
    return headers.size();
  }

  @Override
  public Class<?> getType() {
    return type;
  }

  @Override
  public void writeHeaders(List<FnRow> rows) {
    for (FnHeader header : headers) {
      header.accept(rows);
    }
  }

  @Override
  public Consumer<Sheet> getMergeHeaders(int rowOffset, int colOffset) {
    List<List<String>> collect = headers.stream().map(FnHeader::values).collect(Collectors.toList());
    ObjectHeader header = new ObjectHeader(collect);
    return header.getMergeHeaders(rowOffset, colOffset);
  }

  @Override
  public void write(FnRow row, T item) {
    for (FnCell<T> header : contents) {
      header.accept(row, item);
    }
  }
}
