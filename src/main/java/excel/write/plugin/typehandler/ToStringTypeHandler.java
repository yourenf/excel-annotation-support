package excel.write.plugin.typehandler;

import excel.write.plugin.cellstyle.BorderFnCellStyle;
import org.apache.poi.ss.usermodel.*;

import java.util.Objects;

public class ToStringTypeHandler implements TypeHandler<Object> {

  private BorderFnCellStyle fnCellStyle;
  private CellStyle cellStyle;

  public ToStringTypeHandler() {
    this.fnCellStyle = new BorderFnCellStyle();
  }

  @Override
  public void initCellStyle(Workbook workbook) {
    cellStyle = fnCellStyle.createCellStyle(workbook);
    cellStyle.setAlignment(HorizontalAlignment.LEFT);
    cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
    cellStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat("text"));
  }

  @Override
  public void handler(Cell cell, Object value) {
    if (Objects.nonNull(value)) {
      cell.setCellValue(value.toString());
    }
    cell.setCellStyle(cellStyle);
  }
}
